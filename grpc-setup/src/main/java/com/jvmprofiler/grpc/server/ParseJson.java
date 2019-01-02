package com.jvmprofiler.grpc.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParseJson {

    public static void parse(String metrics) {
            String metrics_data = metrics;

        JsonObject jsonObject = new JsonParser().parse(metrics_data).getAsJsonObject();
        System.out.println(jsonObject.get("bufferPools").getAsJsonArray());
    }

    public static void main(String[] args) {
        String metrics = "{\n" +
                "   \"nonHeapMemoryTotalUsed\":1.1980376E7,\n" +
                "   \"bufferPools\":[\n" +
                "      {\n" +
                "         \"totalCapacity\":0,\n" +
                "         \"name\":\"direct\",\n" +
                "         \"count\":0,\n" +
                "         \"memoryUsed\":0\n" +
                "      },\n" +
                "      {\n" +
                "         \"totalCapacity\":0,\n" +
                "         \"name\":\"mapped\",\n" +
                "         \"count\":0,\n" +
                "         \"memoryUsed\":0\n" +
                "      }\n" +
                "   ],\n" +
                "   \"heapMemoryTotalUsed\":2.4418424E7,\n" +
                "   \"epochMillis\":1539106578624,\n" +
                "   \"nonHeapMemoryCommitted\":1.4352384E7,\n" +
                "   \"heapMemoryCommitted\":2.57425408E8,\n" +
                "   \"memoryPools\":[\n" +
                "      {\n" +
                "         \"peakUsageMax\":251658240,\n" +
                "         \"usageMax\":251658240,\n" +
                "         \"peakUsageUsed\":558464,\n" +
                "         \"name\":\"Code Cache\",\n" +
                "         \"peakUsageCommitted\":2555904,\n" +
                "         \"usageUsed\":558464,\n" +
                "         \"type\":\"Non-heap memory\",\n" +
                "         \"usageCommitted\":2555904\n" +
                "      },\n" +
                "      {\n" +
                "         \"peakUsageMax\":-1,\n" +
                "         \"usageMax\":-1,\n" +
                "         \"peakUsageUsed\":10256824,\n" +
                "         \"name\":\"Metaspace\",\n" +
                "         \"peakUsageCommitted\":10485760,\n" +
                "         \"usageUsed\":10256824,\n" +
                "         \"type\":\"Non-heap memory\",\n" +
                "         \"usageCommitted\":10485760\n" +
                "      },\n" +
                "      {\n" +
                "         \"peakUsageMax\":1073741824,\n" +
                "         \"usageMax\":1073741824,\n" +
                "         \"peakUsageUsed\":1166800,\n" +
                "         \"name\":\"Compressed Class Space\",\n" +
                "         \"peakUsageCommitted\":1310720,\n" +
                "         \"usageUsed\":1166800,\n" +
                "         \"type\":\"Non-heap memory\",\n" +
                "         \"usageCommitted\":1310720\n" +
                "      },\n" +
                "      {\n" +
                "         \"peakUsageMax\":1409286144,\n" +
                "         \"usageMax\":1409286144,\n" +
                "         \"peakUsageUsed\":24418424,\n" +
                "         \"name\":\"PS Eden Space\",\n" +
                "         \"peakUsageCommitted\":67108864,\n" +
                "         \"usageUsed\":24418424,\n" +
                "         \"type\":\"Heap memory\",\n" +
                "         \"usageCommitted\":67108864\n" +
                "      },\n" +
                "      {\n" +
                "         \"peakUsageMax\":11010048,\n" +
                "         \"usageMax\":11010048,\n" +
                "         \"peakUsageUsed\":0,\n" +
                "         \"name\":\"PS Survivor Space\",\n" +
                "         \"peakUsageCommitted\":11010048,\n" +
                "         \"usageUsed\":0,\n" +
                "         \"type\":\"Heap memory\",\n" +
                "         \"usageCommitted\":11010048\n" +
                "      },\n" +
                "      {\n" +
                "         \"peakUsageMax\":2863661056,\n" +
                "         \"usageMax\":2863661056,\n" +
                "         \"peakUsageUsed\":0,\n" +
                "         \"name\":\"PS Old Gen\",\n" +
                "         \"peakUsageCommitted\":179306496,\n" +
                "         \"usageUsed\":0,\n" +
                "         \"type\":\"Heap memory\",\n" +
                "         \"usageCommitted\":179306496\n" +
                "      }\n" +
                "   ],\n" +
                "   \"processCpuLoad\":0.0,\n" +
                "   \"systemCpuLoad\":0.0,\n" +
                "   \"processCpuTime\":824966000,\n" +
                "   \"appId\":\"MyTestApp\",\n" +
                "   \"name\":\"28329@m-c02tr3kbg8wn\",\n" +
                "   \"host\":\"m-c02tr3kbg8wn\",\n" +
                "   \"processUuid\":\"5e452a71-f6be-4053-b245-ea725d87942f\",\n" +
                "   \"gc\":[\n" +
                "      {\n" +
                "         \"collectionTime\":0,\n" +
                "         \"name\":\"PS Scavenge\",\n" +
                "         \"collectionCount\":0\n" +
                "      },\n" +
                "      {\n" +
                "         \"collectionTime\":0,\n" +
                "         \"name\":\"PS MarkSweep\",\n" +
                "         \"collectionCount\":0\n" +
                "      }\n" +
                "   ]\n" +
                "}";

        parse(metrics);
    }
}

