package com.baizhi.kafkalog.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class statefulless {
    public static void main(String[] args) {
        //设置kafka配置信息
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "HadoopNode01:9092,HadoopNode02:9092,HadoopNode03:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-highlevel-application");
        properties.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);
        //编织拓扑任务
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream("t8");
        final String regex = "^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*\\[(.*)\\]\\s\"(\\w*)\\s(.*)\\sHTTP\\/1\\.1\"\\s(\\d{3}).*$";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);


        KTable<String, Long> kTable = stream
                .flatMapValues((v) -> {
                    ArrayList<String> logs = new ArrayList<>();
                    Matcher matcher = pattern.matcher(v);
                    while (matcher.find()) {
                        String ip = matcher.group(1);
                        // 对日期进行处理
                        String strDate = matcher.group(2);
                        String method = matcher.group(3);
                        String resource = matcher.group(4);
                        String status = matcher.group(5);
                        // 07/Oct/2019:01:22:52 +0800
                        // 2019-10-07 01:22:50
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
                        //解析，把StringDate解析成java.util.Date
                        Date date = null;
                        try {
                            date = dateFormat.parse(strDate);
                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                            //进行格式化
                            String formatTime = dateFormat1.format(date);

                            //把数据清洗的结果保存起来
//                            String value = ip+" "+formatTime+" "+method+" "+resource+" "+status;
                            String value = ip;
                            System.out.println(value);
                            logs.add(value);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return logs;
                })
                .map((k, v) -> new KeyValue<>(v, 1L))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                .count();
        kTable.toStream().to("result", Produced.with(Serdes.String(), Serdes.Long()));

        Topology topology = builder.build();

        //打印生产的topology信息
        System.out.println(topology.describe().toString());
        //初始化流处理应用
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        //启动流处理应用
        kafkaStreams.start();
    }
}