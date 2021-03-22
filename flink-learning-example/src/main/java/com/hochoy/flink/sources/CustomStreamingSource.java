package com.hochoy.flink.sources;

import com.hochoy.common.EnumType;
import com.hochoy.utils.CustomGenData;
import com.hochoy.utils.HochoyUtils;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

import java.util.Random;

public class CustomStreamingSource implements ParallelSourceFunction<String> {
    private boolean isRunning = true;
    private final String[] types = new String[]{"metric", "score"};
    private final Random random = new Random();
    private int count = 0;

    private final EnumType.SourceEnum sourceEnum;

    public CustomStreamingSource(EnumType.SourceEnum sourceEnum) {
        this.sourceEnum = sourceEnum;
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {

        String data;

        while (isRunning) {

            switch (sourceEnum) {
                case word_count:
                    data = CustomGenData.genWordCount();
                    break;
                case score:
                    data = CustomGenData.genScore();
                    break;
                case metric:
                    data = CustomGenData.genMetric();
                    break;
                default:
                    data = CustomGenData.genValue(types[random.nextInt(100) % 2 ]);
                    break;
            }

            ctx.collect(data);
            HochoyUtils.sleep(1000);
            if (count == 10) {
                cancel();
            }
            count++;
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }

}
