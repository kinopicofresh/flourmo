package com.kinopio.flourmo.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class MyKafkaListener {
    private static final Logger logger = LoggerFactory.getLogger(MyKafkaListener.class);

    @KafkaListener(groupId = "my-group", topics = "ccis_msg")
    public void testManualCommit(ConsumerRecord<String, String> record, Acknowledgment ack) {

        try {
            Timestamp timestamp = new Timestamp(record.timestamp());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
            logger.info("send time: {}, offset: {}, headers: {}", localDateTime, record.offset(), record.headers());
            logger.info("message: {}", record.value());
            Thread.sleep(5000);
            logger.info("acknowledge: {}", LocalDateTime.now());
            ack.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @KafkaListener(groupId = "my-group", topics = "ccis_msg")
    // public void testManualCommit2(String record) {


    //     try {
    //         logger.info("msg: {}", record);
    //         // ack.acknowledge();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

}
