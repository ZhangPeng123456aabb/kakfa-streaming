package com.baizhi.kafkalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication
public class KafkaLogApplication {
    public static void main(String[] args) {
        run(KafkaLogApplication.class,args);
    }
}
