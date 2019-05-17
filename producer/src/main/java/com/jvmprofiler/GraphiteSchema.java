package com.jvmprofiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphiteSchema {
    public static void main(String[] args) throws FileNotFoundException {
        String oneops_variable = "ONEOPS_NSPATH";
        String schema = "";
        String org = "";
        String assembly = "";
        String platform = "";
        String env = "";
        String s = "";
        Scanner scanner = new Scanner(new File("/Users/a0s01h8/Downloads/oneops")); // path to file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            if (line.contains(oneops_variable)) { // check if line has your finding word
                schema = line.split("=")[1];
                System.out.println(schema);
            }
        }
        org = schema.split("/")[1];
        assembly = schema.split("/")[2];
        platform = schema.split("/")[5];
        env = schema.split("/")[3];
        System.out.println(org);
        System.out.println(assembly);
        System.out.println(platform);
        System.out.println(env);

        s = org+"."+assembly+"."+env+"."+platform;
        System.out.println(s);
    }
}
