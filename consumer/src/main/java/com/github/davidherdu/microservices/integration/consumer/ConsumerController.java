package com.github.davidherdu.microservices.integration.consumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consume")
public class ConsumerController {

    @GetMapping("/hdfs")
    public String consumeFromHDFS() throws Exception {
        String hdfsPath = "hdfs-namenode://localhost:9000/user/spark/output";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(new URI(hdfsPath)))))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    @GetMapping("/s3")
    public String consumeFromS3() throws Exception {
        String s3Path = "s3a://your-bucket/output";
        // Implement S3 reading logic, e.g., using AWS SDK
        return "S3 reading logic goes here";
    }
}
