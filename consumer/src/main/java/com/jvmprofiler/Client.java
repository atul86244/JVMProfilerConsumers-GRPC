package com.jvmprofiler;
import com.proto.metrics.MetricsData;
import com.proto.metrics.MetricsResponse;
import com.proto.metrics.MetricsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client {

    /*public static void main(String[] args) {
        System.out.println("GRPC Client");

      //  Client main = new Client();
      //  main.run();
    }*/

    public static void run(JVMMetrics metrics) {
        System.out.println("Starting GRPC Client");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        doClientStreamingCall(channel, metrics);

        System.out.println("Shutting down channel");
        channel.shutdown();

    }

    private static void doClientStreamingCall(ManagedChannel channel, JVMMetrics metrics) {
        // create an async client
        MetricsServiceGrpc.MetricsServiceStub asyncClient = MetricsServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<MetricsData> requestObserver =  asyncClient.metrics(new StreamObserver<MetricsResponse>() {
            @Override
            public void onNext(MetricsResponse value) {
                // we get a response from the server
                // onNext() will be called only once
                System.out.println("Received response from server");
                System.out.println(value.getResults());
            }

            @Override
            public void onError(Throwable t) {
                // we get an error from server
            }

            @Override
            public void onCompleted() {
                // server is done sending the data
                // onCompleted() will be called immediately after onNext()
                System.out.println("Server completed sending the response");
                latch.countDown();
            }
        });

        // send streaming metrics data
        // bufferpool
        final int bufferpools_len =  metrics.getBufferPools().size();
        List<MetricsData.BUFFERPOOLS> buff_array = new ArrayList<MetricsData.BUFFERPOOLS>(bufferpools_len);

        for (int i = 0; i < bufferpools_len; ++i) {
            bufferPools bufferpools_array = metrics.getBufferPools().get(i);
            MetricsData.BUFFERPOOLS bufferpool_item = MetricsData.BUFFERPOOLS.newBuilder()
                    .setTotalCapacity(bufferpools_array.getTotalCapacity())
                    .setName(bufferpools_array.getName())
                    .setCount(bufferpools_array.getCount())
                    .setMemoryUsed(bufferpools_array.getMemoryUsed())
                    .build();
            buff_array.add(bufferpool_item);
        }

        // mempool
        final int mempools_len =  metrics.getMemoryPools().size();
        List<MetricsData.MEMORYPOOLS> mem_array = new ArrayList<MetricsData.MEMORYPOOLS>(mempools_len);

        for (int i = 0; i < mempools_len; ++i) {
            memoryPools mempools_array = metrics.getMemoryPools().get(i);
            MetricsData.MEMORYPOOLS mempool_item = MetricsData.MEMORYPOOLS.newBuilder()
                    .setPeakUsageMax(mempools_array.getPeakUsageMax())
                    .setUsageMax(mempools_array.getUsageMax())
                    .setPeakUsageUsed(mempools_array.getPeakUsageUsed())
                    .setName(mempools_array.getName())
                    .setPeakUsageCommitted(mempools_array.getPeakUsageCommitted())
                    .setUsageUsed(mempools_array.getUsageUsed())
                    .setType(mempools_array.getType())
                    .setUsageCommitted(mempools_array.getUsageCommitted())
                    .build();
            mem_array.add(mempool_item);
        }

        // gc
        final int gc_len =  metrics.getGc().size();
        List<MetricsData.GC> gc_array = new ArrayList<MetricsData.GC>(gc_len);

        for (int i = 0; i < gc_len; ++i) {
            gc gcpools_array = metrics.getGc().get(i);
            MetricsData.GC gc_item = MetricsData.GC.newBuilder()
                    .setCollectionTime(gcpools_array.getCollectionTime())
                    .setName(gcpools_array.getName())
                    .setCollectionCount(gcpools_array.getCollectionCount())
                    .build();
            gc_array.add(gc_item);
        }

        //Iterable<MetricsData.BUFFERPOOLS> buff = new ArrayList<MetricsData.BUFFERPOOLS>();
        //Iterable<MetricsData.BUFFERPOOLS> buff = buff_array;
       // Iterable<MetricsData.MEMORYPOOLS> mem = mem_array;
        //Iterable<MetricsData.GC> gcs = gc_array;

        requestObserver.onNext(MetricsData.newBuilder()
                    .setHost(metrics.getHost())
                    .setNonHeapMemoryTotalUsed(metrics.getNonHeapMemoryTotalUsed())
                    .addAllBufferPools(buff_array)
                    .setHeapMemoryTotalUsed(metrics.getHeapMemoryTotalUsed())
                    //.setVmRSS(obj.isNull("vmRSS") ? null : obj.getLong("vmRSS"))
                    .setEpochMillis(metrics.getEpochMillis().doubleValue())
                    .setNonHeapMemoryCommitted(metrics.getNonHeapMemoryTotalUsed())
                    .setHeapMemoryCommitted(metrics.getHeapMemoryCommitted())
                    .addAllMemoryPools(mem_array)
                    .setProcessCpuLoad(metrics.getProcessCpuLoad())
                    .setSystemCpuLoad(metrics.getSystemCpuLoad())
                    .setProcessCpuTime(metrics.getProcessCpuTime())
                    //.setVmHWM(obj.getLong("vmHWM"))
                    //.setAppId()
                    .setName(metrics.getName())
                    .setProcessUuid(metrics.getProcessUuid())
                    .addAllGc(gc_array)
                .build());

        // client is done sending data
        requestObserver.onCompleted();

        // latch is required in async to get a response from the server
        try {
            latch.await(3L,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

