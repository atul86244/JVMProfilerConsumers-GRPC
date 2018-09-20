package com.jvmprofiler.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class MetricsServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting GRPC Server");
        Server server = ServerBuilder.forPort(50051)
                .addService(new MetricsServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Received shutdown request");
            server.shutdown();
            System.out.println("Successfully stopped GRPC Server");
        }));
        server.awaitTermination();
    }
}
