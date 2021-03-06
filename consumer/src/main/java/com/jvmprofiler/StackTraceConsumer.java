package com.jvmprofiler;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import sun.jvm.hotspot.ui.action.JavaStackTraceAction;

import java.util.*;

public class StackTraceConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // normal consumer
        properties.setProperty("bootstrap.servers","10.227.215.228:9092");
        properties.put("group.id", "stacktrace-consumer-group-v1");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        // avro part (deserializer)
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://10.227.215.228:8081");
        properties.setProperty("specific.avro.reader", "true");

        KafkaConsumer<String, Stacktrace> kafkaConsumer = new KafkaConsumer<>(properties);
        //String topic = "jvm-metrics-1";
        String topic = "jvmprofiler_Stacktrace";
        TopicPartition topicPartition = new TopicPartition(topic,0);
        List<TopicPartition> topics = Arrays.asList(topicPartition);

        kafkaConsumer.assign(topics);
        kafkaConsumer.seekToEnd(topics);

        System.out.println("Waiting for data...");

        while (true){
            //System.out.println("Polling");
            ConsumerRecords<String, Stacktrace> records = kafkaConsumer.poll(1000);

            for (ConsumerRecord<String, Stacktrace> record : records){
                Stacktrace metrics = record.value();
                System.out.println(metrics);
                //Client main = new Client();
                StackTraceClient.run(metrics);
            }

            kafkaConsumer.commitSync();

        }
    }
}
