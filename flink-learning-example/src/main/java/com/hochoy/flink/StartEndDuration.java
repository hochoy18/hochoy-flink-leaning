package com.hochoy.flink;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import scala.Tuple2;

public class StartEndDuration extends KeyedProcessFunction<String, Tuple2<String, String>, Tuple2<String, Long>> {

    private ValueState<Long> startTime;

    @Override
    public void open(Configuration conf) {
        startTime = getRuntimeContext()
                .getState(new ValueStateDescriptor<Long>("startTime", Long.class));
    }

    @Override
    public void processElement(Tuple2<String, String> in, Context context, Collector<Tuple2<String, Long>> out) throws Exception {
        switch (in._1) {
            case "START":
                startTime.update(context.timestamp());
                context.timerService().registerEventTimeTimer(context.timestamp() + 4 * 60 * 60 * 1000);
                break;
            case "END":
                Long sTime = startTime.value();
                if (sTime != null) {
                    out.collect(new Tuple2<>(in._1, context.timestamp() - sTime));
                    startTime.clear();

                }
            default:
        }
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<String, Long>> out) throws Exception {
        startTime.clear();
    }
}
