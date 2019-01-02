package com.jvmprofiler.grpc.client;
import com.jvmprofiler.grpc.server.MetricsServer;
import com.proto.metrics.MetricsData;
import com.proto.metrics.MetricsResponse;
import com.proto.metrics.MetricsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) {
        System.out.println("GRPC Client");

        Client main = new Client();
        main.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        doClientStreamingCall(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();

    }

    private void doClientStreamingCall(ManagedChannel channel) {
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
        // To Do: Use loop to send metrics

        requestObserver.onNext(MetricsData.newBuilder()
                .setHost("Test")
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
