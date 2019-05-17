package com.jvmprofiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SFTPExecuteProfiler {
    public static void main(String[] args) {
        profiler();
    }
    public static void profiler() {
        String s = null;

        try {

            //Process delete = Runtime.getRuntime().exec("rm -f /Users/a0s01h8/Documents/MyDocuments/Documents/WalmartLabs/JVMProfiler/Metrics/CpuAndMemory.json /Users/a0s01h8/Documents/MyDocuments/Documents/WalmartLabs/JVMProfiler/Metrics/ProcessInfo.json ");
            //Process p = Runtime.getRuntime().exec("java -javaagent:src/main/resources/jvm-profiler-0.0.7.jar=reporter=com.uber.profiling.reporters.FileOutputReporter,outputDir=/Users/a0s01h8/Documents/MyDocuments/Documents/WalmartLabs/JVMProfiler/Metrics -cp target/jvm-profiler-0.0.7.jar com.uber.profiling.examples.HelloWorldApplication");

            Process delete = Runtime.getRuntime().exec("rm -f /tmp/CpuAndMemory.json /tmp/ProcessInfo.json ");
            Process p = Runtime.getRuntime().exec("java -javaagent:/tmp/jvm-profiler-0.0.7.jar=reporter=com.uber.profiling.reporters.FileOutputReporter,metricInterval=60000,appIdVariable=APP_ID1,appIdRegex=sftp,outputDir=/tmp -Xmx1g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -Djava.awt.headless=true -cp /app/ImageSanitizer/imagesanitizer-jar-with-dependencies.jar com.walmart.services.imagesanitizer.Sanitizer -scandirectory /app/ImageSanitizer/images -threads 1 -server localhost:9092 -topic qarth_sftpimage_metadata -partnerfile /app/vajra/partner.txt ");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        }
        catch (IOException e) {
            System.out.println("Error: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
