package com.jvmprofiler.grpc.server;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class test_thread implements Runnable  {

    SimpleGraphiteClient client = new SimpleGraphiteClient("10.116.137.250", 2003);

    private static int counter = 0;

    @Override
    public void run() {
        System.out.println("Running....." + counter++);
        Map<String, Double> allMetrics = new HashMap<String, Double>() {{
            put("atul.SampleMetrics.MyTestApp.gc.PS_Scavenge.collectionCount ", (double) 10);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Eden_Space.peakUsageMax ", 1409286144d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Survivor_Space.peakUsageCommitted ", 11010048d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Code_Cache.peakUsageCommitted ", 3145728d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Compressed_Class_Space.usageUsed ", 1224424d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Survivor_Space.peakUsageMax ", 11010048d);
            put("atul.SampleMetrics.MyTestApp.gc.PS_MarkSweep.collectionTime ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Code_Cache.peakUsageMax ", 251658240d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Survivor_Space.usageCommitted ", 11010048d);
            put("atul.SampleMetrics.MyTestApp.bufferPools.direct.count ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.bufferPools.mapped.count ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Old_Gen.peakUsageCommitted ", 179306496d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Old_Gen.usageMax ", 2863661056d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Compressed_Class_Space.peakUsageUsed ", 1224424d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Code_Cache.usageMax ", 251658240d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Eden_Space.peakUsageUsed ", 54000944d);
            put("atul.SampleMetrics.MyTestApp.gc.PS_MarkSweep.collectionCount ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.nonHeapMemoryTotalUsed  ", (double) 3000000);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Survivor_Space.usageUsed ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.gc.PS_Scavenge.collectionTime ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Metaspace.peakUsageUsed ", 11126272d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Compressed_Class_Space.usageCommitted ", 1310720d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Code_Cache.usageCommitted ", 3145728d);
            put("atul.SampleMetrics.MyTestApp.systemCpuLoad  ", (double) 80);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Survivor_Space.usageMax ", 11010048d);
            put("atul.SampleMetrics.MyTestApp.bufferPools.direct.totalCapacity ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.heapMemoryTotalUsed  ", (double) 5000000);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Old_Gen.usageUsed ", (double) 40000);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Eden_Space.peakUsageCommitted ", 67108864d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Old_Gen.peakUsageUsed ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Eden_Space.usageCommitted ", 67108864d);
            put("atul.SampleMetrics.MyTestApp.processCpuLoad  ", (double) 30);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Compressed_Class_Space.peakUsageMax ", 1073741824d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Survivor_Space.peakUsageUsed ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.bufferPools.mapped.totalCapacity ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.bufferPools.mapped.memoryUsed ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Code_Cache.usageUsed ", 3074560d);
            put("atul.SampleMetrics.MyTestApp.processCpuTime  ", (double) 258987889);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Compressed_Class_Space.peakUsageCommitted ", 1310720d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Old_Gen.usageCommitted ", 179306496d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Old_Gen.peakUsageMax ", 2863661056d);
            put("atul.SampleMetrics.MyTestApp.nonHeapMemoryCommitted  ", (double) 7000000);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Metaspace.peakUsageCommitted ", 11534336d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Eden_Space.usageMax ", 1409286144d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Metaspace.peakUsageMax ", (double) -1);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Metaspace.usageUsed ", 11126272d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Compressed_Class_Space.usageMax ", 1073741824d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.PS_Eden_Space.usageUsed ", 54000944d);
            put("atul.SampleMetrics.MyTestApp.bufferPools.direct.memoryUsed ", (double) 0);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Code_Cache.peakUsageUsed ", 3078784d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Metaspace.usageCommitted ", 11534336d);
            put("atul.SampleMetrics.MyTestApp.memoryPools.Metaspace.usageMax ", (double) -1);

        }};
        long now = Instant.now().toEpochMilli();
        client.sendMetrics(allMetrics, now / 1000);
    }
}
