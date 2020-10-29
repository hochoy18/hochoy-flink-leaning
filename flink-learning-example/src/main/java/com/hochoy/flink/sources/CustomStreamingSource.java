package com.hochoy.flink.sources;

import com.hochoy.utils.CustomGenData;
import com.hochoy.utils.HochoyUtils;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

import java.util.Random;

public class CustomStreamingSource implements ParallelSourceFunction<String> {
    private boolean isRunning = true;
    private String[] types = new String[]{"metric", "score"};
    private Random random = new Random();
    private int count = 0;

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while (isRunning) {
            String value = CustomGenData.genValue(types[random.nextInt(100) % 2 ]);
            ctx.collect(value);
            HochoyUtils.sleep(500);
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
