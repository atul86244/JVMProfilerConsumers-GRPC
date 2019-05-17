package com.jvmprofiler;
import com.proto.metrics.MetricsData;
import com.proto.metrics.MetricsResponse;
import com.proto.metrics.MetricsServiceGrpc;
import com.proto.metrics.StackTrace;
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

public class StackTraceClient {

    /*public static void main(String[] args) {
        System.out.println("GRPC Client");

      //  Client main = new Client();
      //  main.run();
    }*/

    public static void run(Stacktrace metrics) {
        System.out.println("Starting GRPC Client");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        doClientStreamingCall(channel, metrics);

        System.out.println("Shutting down channel");
        channel.shutdown();

    }

    private static void doClientStreamingCall(ManagedChannel channel, Stacktrace metrics) {
        // create an async client
        MetricsServiceGrpc.MetricsServiceStub asyncClient = MetricsServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<StackTrace> requestObserver =  asyncClient.stacktrace(new StreamObserver<MetricsResponse>() {
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

        final int stacktrace_len = metrics.getStacktrace().size();

        List<String> stacktraces = new ArrayList<String>(stacktrace_len);

        for (int i = 0; i < stacktrace_len; ++i) {
            String item = metrics.getStacktrace().get(i);
            stacktraces.add(item);
        }

        requestObserver.onNext(StackTrace.newBuilder()
                .addAllStacktrace(stacktraces)
                .setCount(metrics.getCount())
                .setEndEpoch(metrics.getEndEpoch())
                .setStartEpoch(metrics.getStartEpoch())
                .setThreadName(metrics.getThreadName())
                .setThreadState(metrics.getThreadState())
                .setAppId(metrics.getAppId())
                .setName(metrics.getName())
                .setHost(metrics.getHost())
                .setProcessUuid(metrics.getProcessUuid())
                .setGraphiteSchema(metrics.getGraphiteSchema())
                .setProfiler(metrics.getProfiler())
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


