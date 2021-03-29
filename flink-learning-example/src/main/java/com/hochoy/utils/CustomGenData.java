package com.hochoy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hochoy.flink.entities.Metric;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;

public class CustomGenData {
    private static final Random random = new Random();

    public static String genWordCount(){
        String[] bigData = new String[]{
                "Flink","Hadoop", "Kafka","Kudu","Spark","ZooKeeper"
        };
        int start = random.nextInt(4);
        int len = start + random.nextInt(bigData.length - start);
        StringJoiner joiner = new StringJoiner(" ");
        for (int i = start; i < len; i++) {
            joiner.add(bigData[i]);
        }

        return joiner.toString();
    }


    public static String genScore() {
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

    public static String genMetric() {
        Metric metric = new Metric();
        metric.setTimestamp(System.currentTimeMillis());
        metric.setName("mem");
        Map<String, String> tags = new HashMap<>();
        Map<String, Object> fields = new HashMap<>();
        String[] ips = {"208.111.222.28", "97.175.198.251", "209.253.185.97", "84.65.84.130", "202.20.214.178", "205.239.31.228", "129.236.93.53", "38.217.124.159", "223.115.121.69", "125.10.193.197", "203.10.163.150", "168.217.61.32", "116.92.6.86", "227.178.88.92", "189.14.226.88", "11.31.143.8", "163.255.110.109", "109.20.130.97", "74.119.246.65", "153.200.13.43", "34.20.129.34", "178.218.127.152", "210.98.240.245", "117.128.254.55"};

        int ipLen = ips.length;

        tags.put("cluster", "hcohoy");
        tags.put("host_ip", ips[random.nextInt(ipLen)]);

        DecimalFormat df = new DecimalFormat("#.00");
        double percent = random.nextDouble();

        fields.put("used_percent", df.format(percent * 100) + "%");
        fields.put("max", df.format(random.nextDouble() * 100000000));
        fields.put("used", df.format(random.nextDouble() * 1000000));
        fields.put("init", df.format(random.nextDouble() * 100000));

        metric.setTags(tags);
        metric.setFields(fields);
        return JSON.toJSONString(metric);
    }

    public static String genValue(String type) {
        String data;
        switch (type) {
            case "score":
                data = genScore();
                break;
            case "word_count":
                data = genWordCount();
                return data;
            default:
                data = genMetric();
                break;
        }
        JSONObject res = new JSONObject();
        res.put("type",type);
        res.put("data",JSON.parseObject(data));
        return JSON.toJSONString(res);
    }


}
