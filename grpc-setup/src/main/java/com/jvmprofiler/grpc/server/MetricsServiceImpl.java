package com.jvmprofiler.grpc.server;

import com.proto.metrics.Metrics;
import com.proto.metrics.MetricsData;
import com.proto.metrics.MetricsResponse;
import com.proto.metrics.MetricsServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricsServiceImpl extends MetricsServiceGrpc.MetricsServiceImplBase {

    @Override
    public StreamObserver<MetricsData> metrics(StreamObserver<MetricsResponse> responseObserver) {
        StreamObserver<MetricsData> requestObserver = new StreamObserver<MetricsData>() {

            String result = "";

            @Override
            public void onNext(MetricsData value) {
                // client sends a msg
                result += "JVM Metrics received for " + value.getHost();
                //result += "JVM Metrics received for " + value.getAppId();

                //String appId = value.getAppId().toString();
                String appId = "MyTestApp";
                String host = value.getHost();
                String processUuid = value.getProcessUuid();

                final int bufferpools_len = value.getBufferPoolsCount();
                final int memorypools_len = value.getMemoryPoolsCount();
                final int gc_len = value.getGcCount();

                System.out.println("Buff Pool length " + bufferpools_len);
                System.out.println("Memory pool length " + memorypools_len);
                System.out.println("GC length " + gc_len);

                SimpleGraphiteClient client = new SimpleGraphiteClient("10.116.137.250", 2003);

                Map<String, Double> allMetrics = new HashMap<String, Double>() {{
                   put("jvmprofiler_metrics." + host + "." + appId + "." + "nonHeapMemoryTotalUsed ", value.getNonHeapMemoryTotalUsed());
                   put("jvmprofiler_metrics." + host + "." + appId + "." + "heapMemoryTotalUsed " , value.getHeapMemoryTotalUsed());
                   put("jvmprofiler_metrics." + host + "." + appId + "." + "nonHeapMemoryCommitted " , value.getNonHeapMemoryCommitted());
                   put("jvmprofiler_metrics." + host + "." + appId + "." + "processCpuLoad " , value.getProcessCpuLoad());
                   put("jvmprofiler_metrics." + host + "." + appId + "." + "systemCpuLoad " , value.getSystemCpuLoad());
                   put("jvmprofiler_metrics." + host + "." + appId + "." + "processCpuTime " , value.getProcessCpuTime());

                }};

                for (int i = 0; i < bufferpools_len; ++i) {
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".bufferPools." + value.getBufferPools(i).getName().replace(" ","_") + ".totalCapacity" , value.getBufferPools(i).getTotalCapacity());
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".bufferPools." + value.getBufferPools(i).getName().replace(" ","_") + ".count" , value.getBufferPools(i).getCount());
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".bufferPools." + value.getBufferPools(i).getName().replace(" ","_") + ".memoryUsed" , value.getBufferPools(i).getMemoryUsed());
                }

                for (int i = 0; i < memorypools_len; ++i) {
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".peakUsageMax" , value.getMemoryPools(i).getPeakUsageMax());
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".usageMax" , value.getMemoryPools(i).getUsageMax());
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".peakUsageUsed" , value.getMemoryPools(i).getPeakUsageUsed());
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".peakUsageCommitted" , value.getMemoryPools(i).getPeakUsageCommitted());
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".usageUsed" , value.getMemoryPools(i).getUsageUsed());
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".usageCommitted" , value.getMemoryPools(i).getUsageCommitted());
                }

                for (int i = 0; i < gc_len; ++i) {
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".gc." + value.getGc(i).getName().replace(" ","_") + ".collectionTime" , value.getGc(i).getCollectionTime());
                    allMetrics.put("jvmprofiler_metrics." + host + "." + appId + ".gc." + value.getGc(i).getName().replace(" ","_") + ".collectionCount" , value.getGc(i).getCollectionCount());
                }

               // System.out.println(allMetrics);
                System.out.println("===============");
                System.out.println(((long) value.getEpochMillis() / 1000 ));
                client.sendMetrics(allMetrics, ((long) value.getEpochMillis() / 1000 ));


                // =========================================

//                System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + "." + "nonHeapMemoryTotalUsed " + value.getNonHeapMemoryTotalUsed() + " " + value.getEpochMillis() );
//
//                for (int i = 0; i < bufferpools_len; ++i) {
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".bufferPools." + value.getBufferPools(i).getName() + ".totalCapacity " + value.getBufferPools(i).getTotalCapacity() + " " + value.getEpochMillis() );
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".bufferPools." + value.getBufferPools(i).getName() + ".count " + value.getBufferPools(i).getCount() + " " + value.getEpochMillis() );
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".bufferPools." + value.getBufferPools(i).getName() + ".memoryUsed " + value.getBufferPools(i).getMemoryUsed() + " " + value.getEpochMillis() );
//                }
//
//                System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + "." + "heapMemoryTotalUsed " + value.getHeapMemoryTotalUsed() + " " + value.getEpochMillis() );
//
//                System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + "." + "nonHeapMemoryCommitted " + value.getNonHeapMemoryCommitted() + " " + value.getEpochMillis() );
//
//                for (int i = 0; i < memorypools_len; ++i) {
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".memoryPools." + value.getMemoryPools(i).getName() + ".peakUsageMax " + value.getMemoryPools(i).getPeakUsageMax() + " " + value.getEpochMillis() );
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".memoryPools." + value.getMemoryPools(i).getName() + ".usageMax " + value.getMemoryPools(i).getUsageMax() + " " + value.getEpochMillis() );
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".memoryPools." + value.getMemoryPools(i).getName() + ".peakUsageUsed " + value.getMemoryPools(i).getPeakUsageUsed() + " " + value.getEpochMillis() );
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".memoryPools." + value.getMemoryPools(i).getName() + ".peakUsageCommitted " + value.getMemoryPools(i).getPeakUsageCommitted() + " " + value.getEpochMillis() );
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".memoryPools." + value.getMemoryPools(i).getName() + ".usageUsed " + value.getMemoryPools(i).getUsageUsed() + " " + value.getEpochMillis() );
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".memoryPools." + value.getMemoryPools(i).getName() + ".usageCommitted " + value.getMemoryPools(i).getUsageCommitted() + " " + value.getEpochMillis() );
//                }
//
//                System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + "." + "processCpuLoad " + value.getProcessCpuLoad() + " " + value.getEpochMillis() );
//
//                System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + "." + "systemCpuLoad " + value.getSystemCpuLoad() + " " + value.getEpochMillis() );
//
//                System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + "." + "processCpuTime " + value.getProcessCpuTime() + " " + value.getEpochMillis() );
//
//                for (int i = 0; i < gc_len; ++i) {
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".gc." + value.getGc(i).getName() + ".collectionTime " + value.getGc(i).getCollectionTime() + " " + value.getEpochMillis() );
//                    System.out.println("jvmprofiler.metrics." + value.getName() + "." + host + "." + appId + "." + processUuid + ".gc." + value.getGc(i).getName() + ".collectionCount " + value.getGc(i).getCollectionCount() + " " + value.getEpochMillis() );
//                }

            }

            @Override
            public void onError(Throwable t) {
                // client sends an error
            }

            @Override
            public void onCompleted() {
                // client is done
                responseObserver.onNext(
                        MetricsResponse.newBuilder()
                                .setResults(result)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }
}
