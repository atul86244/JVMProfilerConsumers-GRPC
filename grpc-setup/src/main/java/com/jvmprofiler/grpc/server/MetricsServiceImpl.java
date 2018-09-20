package com.jvmprofiler.grpc.server;

import com.proto.metrics.Metrics;
import com.proto.metrics.MetricsData;
import com.proto.metrics.MetricsResponse;
import com.proto.metrics.MetricsServiceGrpc;
import io.grpc.stub.StreamObserver;

public class MetricsServiceImpl extends MetricsServiceGrpc.MetricsServiceImplBase {

    @Override
    public StreamObserver<MetricsData> metrics(StreamObserver<MetricsResponse> responseObserver) {
        StreamObserver<MetricsData> requestObserver = new StreamObserver<MetricsData>() {

            String result = "";

            @Override
            public void onNext(MetricsData value) {
                // client sends a msg
                result += "JVM Metrics received for " + value.getHost();
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
