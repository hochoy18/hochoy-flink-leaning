package com.hochoy.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hochoy.flink.entities.Metric;
import com.hochoy.utils.HochoyUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProducerAnalysis {


    private final static int MSG_SIZE = (int) 1e4;
    private static Random random = new Random();
    private static DecimalFormat df = new DecimalFormat("#.00");
    private static Properties props = HochoyUtils.getProperties("producer.properties");

    private static KafkaProducer<String, String> producer = new KafkaProducer<>(props);

    public static void main(String[] args) {

        // args type
        // metric ,  score
        String type = args[0];
        String value;
        for (int i = 0; i < MSG_SIZE; i++) {
            value = genValue(type);
            System.out.println(value);
            String TOPIC = "part3-topic";
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, value);
            Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Msg send success !!!! ");
                    long offset = metadata.offset();
                    int partition = metadata.partition();
                    System.out.printf("partition : %d , offset : %d  %n", partition, offset);
                } else {
                    exception.printStackTrace();
                }

            });
            RecordMetadata recordMetadata;
            try {
                recordMetadata = future.get();
                long offset = recordMetadata.offset();
                System.out.println("==========================" + offset);
                HochoyUtils.sleep(500);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    private static String genScore() {
        String[] dan = new String[]{"李", "王", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴", "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗"};
        String[] fu = new String[]{"司马", "上官", "欧阳", "夏侯", "诸葛", "闻人", "南宫", "西门", "东门", "左丘", "梁丘"};
        String[] ming = new String[]{"凯瑞", "健雄", "耀杰", "潇然", "子涵", "越彬", "钰轩", "智辉", "致远", "俊驰", "雨泽", "烨磊", "晟睿", "文昊", "修洁", "黎昕", "向澜", "宇萧", "星皓", "潇咏", "风明", "成林", "崇宁", "思元", "云浩", "宇鸿"};
        String[] subjects = new String[]{"Hadoop", "Spark", "Flink", "Java", "C++"};
        double[] scores = new double[]{90, 91.5, 99, 98, 93, 89, 88.5, 89, 100, 79, 78.5, 98.5};
        JSONObject jo = new JSONObject();
        jo.put("subject", subjects[random.nextInt(subjects.length)]);
        jo.put("score", scores[random.nextInt(scores.length)]);
        int xingIndex = random.nextInt(3);
        int mingIndex = random.nextInt(4);

        jo.put("name", (random.nextInt(2) == 0 ? dan[xingIndex] : fu[xingIndex]) + ming[mingIndex]);
        return jo.toJSONString();
    }

    private static String genMetric() {
        Metric metric = new Metric();
        metric.setTimestamp(System.currentTimeMillis());
        metric.setName("mem");
        Map<String, String> tags = new HashMap<>();
        Map<String, Object> fields = new HashMap<>();
        String[] ips = {"208.111.222.28", "97.175.198.251", "209.253.185.97", "84.65.84.130", "202.20.214.178", "205.239.31.228", "129.236.93.53", "38.217.124.159", "223.115.121.69", "125.10.193.197", "203.10.163.150", "168.217.61.32", "116.92.6.86", "227.178.88.92", "189.14.226.88", "11.31.143.8", "163.255.110.109", "109.20.130.97", "74.119.246.65", "153.200.13.43", "34.20.129.34", "178.218.127.152", "210.98.240.245", "117.128.254.55"};

        int ipLen = ips.length;

        tags.put("cluster", "hcohoy");
        tags.put("host_ip", ips[random.nextInt(ipLen)]);

        df = new DecimalFormat("#.00");
        double percent = random.nextDouble();

        fields.put("used_percent", df.format(percent * 100) + "%");
        fields.put("max", df.format(random.nextDouble() * 100000000));
        fields.put("used", df.format(random.nextDouble() * 1000000));
        fields.put("init", df.format(random.nextDouble() * 100000));

        metric.setTags(tags);
        metric.setFields(fields);
        return JSON.toJSONString(metric);
    }

    private static String genValue(String type) {
        String res;
        switch (type) {
            case "score":
                res = genScore();
                break;
            case "metric":
                res = genMetric();
                break;
            default:
                res = genMetric();
                break;
        }
        return res;
    }


}



