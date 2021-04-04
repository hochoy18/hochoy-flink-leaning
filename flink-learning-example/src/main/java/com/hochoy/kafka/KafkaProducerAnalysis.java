package com.hochoy.kafka;

import com.hochoy.utils.CustomGenData;
import com.hochoy.utils.HochoyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProducerAnalysis {


    private final static int MSG_SIZE = (int) 1e4;

    private static Properties props = HochoyUtils.getProperties("producer.properties");

    private static KafkaProducer<String, String> producer = new KafkaProducer<>(props);

    public static void main(String[] args) {

        // args type
        // metric ,  score   word_count
        String type = args[0];
        String value;
        for (int i = 0; i < MSG_SIZE; i++) {
            value = CustomGenData.genValue(type);
            if (StringUtils.isNotBlank(value)){
                System.out.println(value);
                String TOPIC = "hochoy_flink";
                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, value);
                Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
//                    System.out.println("Msg send success !!!! ");
                        long offset = metadata.offset();
                        int partition = metadata.partition();
//                    System.out.printf("partition : %d , offset : %d  %n", partition, offset);
                    } else {
                        exception.printStackTrace();
                    }

                });
                RecordMetadata recordMetadata;
                try {
                    recordMetadata = future.get();
                    long offset = recordMetadata.offset();
//                System.out.println("==========================" + offset);
                    HochoyUtils.sleep(3000);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}



