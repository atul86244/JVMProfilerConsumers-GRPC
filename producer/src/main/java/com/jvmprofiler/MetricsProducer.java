package com.jvmprofiler;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import com.jvmprofiler.JVMMetrics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.kafka.clients.producer.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MetricsProducer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        // normal producer
        properties.setProperty("bootstrap.servers", "10.116.137.250:9092");
        properties.setProperty("acks", "all");
        properties.setProperty("retries", "10");
        // avro part
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://10.116.137.250:8081");

        ExecuteProfiler.profiler();

        Producer<String, JVMMetrics> producer = new KafkaProducer<String, JVMMetrics>(properties);

        String topic = "jvm-metrics-1";

        String filename = "/tmp/CpuAndMemory.json";
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {

                final JSONObject obj = new JSONObject(sCurrentLine);
                final JSONArray bufferpools_array = obj.getJSONArray("bufferPools");
                final JSONArray memorypools_array = obj.getJSONArray("memoryPools");
                final JSONArray gc_array = obj.getJSONArray("gc");

                final int bufferpools_len = bufferpools_array.length();
                final int memorypools_len = memorypools_array.length();
                final int gc_len = gc_array.length();

                List<com.jvmprofiler.bufferPools> buffpools = new ArrayList<bufferPools>(bufferpools_len);

                for (int i = 0; i < bufferpools_len; ++i) {
                    final JSONObject bufferpool = bufferpools_array.getJSONObject(i);
                    bufferPools bufferpool_item = bufferPools.newBuilder()
                      .setTotalCapacity(bufferpool.getLong("totalCapacity"))
                      .setName(bufferpool.getString("name"))
                      .setCount(bufferpool.getLong("count"))
                      .setMemoryUsed(bufferpool.getLong("memoryUsed"))
                    .build();
                    buffpools.add(bufferpool_item);
                }

                List<com.jvmprofiler.memoryPools> mempools = new ArrayList<memoryPools>(memorypools_len);

                for (int i = 0; i < memorypools_len; ++i) {
                    final JSONObject memorypool = memorypools_array.getJSONObject(i);
                    memoryPools memorypool_item = memoryPools.newBuilder()
                        .setPeakUsageMax(memorypool.getLong("peakUsageMax"))
                        .setUsageMax(memorypool.getLong("usageMax"))
                        .setPeakUsageUsed(memorypool.getLong("peakUsageUsed"))
                        .setName(memorypool.getString("name"))
                        .setPeakUsageCommitted(memorypool.getLong("peakUsageCommitted"))
                        .setUsageUsed(memorypool.getLong("usageUsed"))
                        .setType(memorypool.getString("type"))
                        .setUsageCommitted(memorypool.getLong("usageCommitted"))
                    .build();
                    mempools.add(memorypool_item);
                }

                List<com.jvmprofiler.gc> gcs = new ArrayList<gc>(gc_len);

                for (int i = 0; i < gc_len; ++i) {
                    final JSONObject gc_json = gc_array.getJSONObject(i);
                    gc gc_item = gc.newBuilder()
                        .setCollectionTime(gc_json.getLong("collectionTime"))
                        .setName(gc_json.getString("name"))
                        .setCollectionCount(gc_json.getLong("collectionCount"))
                    .build();
                    gcs.add(gc_item);
                }

                JVMMetrics jvmmetrics = JVMMetrics.newBuilder()
                    .setNonHeapMemoryTotalUsed(obj.getDouble("nonHeapMemoryTotalUsed"))
                    .setBufferPools(buffpools)
                    .setHeapMemoryTotalUsed(obj.getDouble("heapMemoryTotalUsed"))
                    //.setVmRSS(obj.isNull("vmRSS") ? null : obj.getLong("vmRSS"))
                    .setEpochMillis(obj.getLong("epochMillis"))
                    .setNonHeapMemoryCommitted(obj.getDouble("nonHeapMemoryCommitted"))
                    .setHeapMemoryCommitted(obj.getDouble("heapMemoryCommitted"))
                    .setMemoryPools(mempools)
                    .setProcessCpuLoad(obj.getDouble("processCpuLoad"))
                    .setSystemCpuLoad(obj.getDouble("systemCpuLoad"))
                    .setProcessCpuTime(obj.getLong("processCpuTime"))
                    //.setVmHWM(obj.getLong("vmHWM"))
                    .setAppId("MyTestApp")
                    .setName(obj.getString("name"))
                    .setHost(obj.getString("host"))
                    .setProcessUuid(obj.getString("processUuid"))
                    .setGc(gcs)
                .build();

                ProducerRecord<String, JVMMetrics> producerRecord = new ProducerRecord<String, JVMMetrics>(
                        topic, jvmmetrics
                );

                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception == null) {
                            System.out.println(metadata);
                        } else {
                            exception.printStackTrace();
                        }
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
                producer.flush();
                producer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
