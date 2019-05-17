package com.jvmprofiler;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;

public class SFTPConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // normal consumer
        properties.setProperty("bootstrap.servers","10.227.215.228:9092");
        properties.put("group.id", "metrics-consumer-group-v2");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        // avro part (deserializer)
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://10.227.215.228:8081");
        properties.setProperty("specific.avro.reader", "true");

        KafkaConsumer<String, JVMMetrics> kafkaConsumer = new KafkaConsumer<>(properties);
        String topic = "sftp4_CpuAndMemory";
        TopicPartition topicPartition = new TopicPartition(topic,0);
        List<TopicPartition> topics = Arrays.asList(topicPartition);

        kafkaConsumer.assign(topics);
        kafkaConsumer.seekToEnd(topics);

        System.out.println("Waiting for data...");

        while (true){
            //System.out.println("Polling");
            ConsumerRecords<String, JVMMetrics> records = kafkaConsumer.poll(1000);

            for (ConsumerRecord<String, JVMMetrics> record : records){
                JVMMetrics metrics = record.value();
                System.out.println(metrics);
                //Client main = new Client();
                Client.run(metrics);
            }

            kafkaConsumer.commitSync();

        }
    }
}
