package com.jvmprofiler;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;
import java.util.stream.IntStream;

public class MetricsConsumer {
    public static void main(String[] args) {
            Properties properties = new Properties();
            // normal consumer
            properties.setProperty("bootstrap.servers","10.116.137.250:9092");
            properties.put("group.id", "metrics-consumer-group-v1");
            properties.put("auto.commit.enable", "false");
            properties.put("auto.offset.reset", "earliest");

            // avro part (deserializer)
            properties.setProperty("key.deserializer", StringDeserializer.class.getName());
            properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
            properties.setProperty("schema.registry.url", "http://10.116.137.250:8081");
            properties.setProperty("specific.avro.reader", "true");

            KafkaConsumer<String, JVMMetrics> kafkaConsumer = new KafkaConsumer<>(properties);
            String topic = "jvm-metrics";
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
                }

                kafkaConsumer.commitSync();
            }
    }
}
