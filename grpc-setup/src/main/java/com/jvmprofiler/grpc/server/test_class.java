package com.jvmprofiler.grpc.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class test_class {



    public static void main(String[] args) {
        ExecutorService sender = Executors.newFixedThreadPool(100) ;
        for(int i = 0; i<=100000;i++) {
            sender.execute(new test_thread());
        }
        sender.shutdown();
    }
}
