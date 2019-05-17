package com.jvmprofiler.grpc.server;

import com.proto.metrics.*;
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

                String appId = value.getAppId().toString();
                //String appId = "MyTestApp";
                String host = value.getHost();
                String schema = value.getGraphiteSchema();
                String profiler = value.getProfiler();

                final int bufferpools_len = value.getBufferPoolsCount();
                final int memorypools_len = value.getMemoryPoolsCount();
                final int gc_len = value.getGcCount();

                System.out.println("Buff Pool length " + bufferpools_len);
                System.out.println("Memory pool length " + memorypools_len);
                System.out.println("GC length " + gc_len);

                SimpleGraphiteClient client = new SimpleGraphiteClient("10.227.215.228", 2003);

                Map<String, Double> allMetrics = new HashMap<String, Double>() {{
                   put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + "." + "nonHeapMemoryTotalUsed ", value.getNonHeapMemoryTotalUsed());
                   put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + "." + "heapMemoryTotalUsed " , value.getHeapMemoryTotalUsed());
                   put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + "." + "nonHeapMemoryCommitted " , value.getNonHeapMemoryCommitted());
                   put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + "." + "processCpuLoad " , value.getProcessCpuLoad());
                   put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + "." + "systemCpuLoad " , value.getSystemCpuLoad());
                   put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + "." + "processCpuTime " , value.getProcessCpuTime());
                  // put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + "processUuid " , Double.valueOf(value.getProcessUuid()));

                }};

                for (int i = 0; i < bufferpools_len; ++i) {
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host +  "." + profiler + ".bufferPools." + value.getBufferPools(i).getName().replace(" ","_") + ".totalCapacity" , value.getBufferPools(i).getTotalCapacity());
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".bufferPools." + value.getBufferPools(i).getName().replace(" ","_") + ".count" , value.getBufferPools(i).getCount());
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".bufferPools." + value.getBufferPools(i).getName().replace(" ","_") + ".memoryUsed" , value.getBufferPools(i).getMemoryUsed());
                }

                for (int i = 0; i < memorypools_len; ++i) {
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".peakUsageMax" , value.getMemoryPools(i).getPeakUsageMax());
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".usageMax" , value.getMemoryPools(i).getUsageMax());
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".peakUsageUsed" , value.getMemoryPools(i).getPeakUsageUsed());
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".peakUsageCommitted" , value.getMemoryPools(i).getPeakUsageCommitted());
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".usageUsed" , value.getMemoryPools(i).getUsageUsed());
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".memoryPools." + value.getMemoryPools(i).getName().replace(" ","_") + ".usageCommitted" , value.getMemoryPools(i).getUsageCommitted());
                }

                for (int i = 0; i < gc_len; ++i) {
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".gc." + value.getGc(i).getName().replace(" ","_") + ".collectionTime" , value.getGc(i).getCollectionTime());
                    allMetrics.put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + ".gc." + value.getGc(i).getName().replace(" ","_") + ".collectionCount" , value.getGc(i).getCollectionCount());
                }

               // System.out.println(allMetrics);
                System.out.println("===============");
                System.out.println(((long) value.getEpochMillis() / 1000 ));
                client.sendMetrics(allMetrics, ((long) value.getEpochMillis() / 1000 ));

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

    @Override
    public StreamObserver<StackTrace> stacktrace(StreamObserver<MetricsResponse> responseObserver) {
        StreamObserver<StackTrace> requestObserver = new StreamObserver<StackTrace>() {

            String result = "";

            @Override
            public void onNext(StackTrace value) {
                // client sends a msg
                result += "JVM Metrics received for " + value.getHost();
                //result += "JVM Metrics received for " + value.getAppId();

                String appId = value.getAppId();
                //String appId = "MyTestApp";
                String host = value.getHost();
                String schema = value.getGraphiteSchema();
                String profiler = value.getProfiler();

                final int stacktrace_len = value.getStacktraceCount();

                SimpleGraphiteClient client = new SimpleGraphiteClient("10.227.215.228", 2003);

                Map<String, String> allMetrics = new HashMap<String, String>() {{
//                    put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + profiler + "." + "endEpoch ", String.valueOf(value.getEndEpoch()));
//                    //put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + "profiler ", Double.valueOf(value.getProfiler()));
//                    put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + profiler + "." + "name ", value.getName());
//                    put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + profiler + "." + "processUuid ", value.getProcessUuid());
//                    put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + profiler + "." + "threadState ", value.getThreadState());
//                    put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + profiler + "." + "count ", String.valueOf(value.getCount()));
//                    put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + profiler + "." + "startEpoch ", String.valueOf(value.getStartEpoch()));
//                    put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + profiler + "." + "threadName ", value.getThreadName());
                    put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + "." + value.getThreadName().replace(" ","_").replace(".","_") + "." + value.getThreadState() + "." + "count ", String.valueOf(value.getCount()));
                    put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + "." + value.getThreadName().replace(" ","_").replace(".","_") + "." + value.getThreadState() + "." + "endEpoch ", String.valueOf(value.getEndEpoch()));
                    put("jvmprofiler_metrics." + schema + "." + appId + "." + host + "." + profiler + "." + value.getThreadName().replace(" ","_").replace(".","_") + "." + value.getThreadState() + "." + "startEpoch ", String.valueOf(value.getStartEpoch()));
                }};

//                for (int i = 0; i < stacktrace_len; ++i) {
//                    allMetrics.put("jvmprofiler_metrics." + schema + "." + host + "." + appId + "." + profiler + "." + "Stacktrace ", value.getStacktrace(i));
//                }

                // System.out.println(allMetrics);
                System.out.println("===============");
                System.out.println(((long) value.getEndEpoch() / 1000 ));
                client.sendStackTraceMetrics(allMetrics, ((long) value.getEndEpoch() / 1000 ));

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
