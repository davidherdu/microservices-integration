package com.github.davidherdu.microservices.integration.sparkKafkaConsumer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaStreamingContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SparkKafkaConsumer {
    public static void main(String[] args) throws InterruptedException {
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("KafkaSparkConsumer");
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(10));

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, "spark-group");
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        kafkaParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        JavaInputDStream<org.apache.kafka.clients.consumer.ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(Collections.singletonList("example-topic"), kafkaParams)
                );

        stream.map(record -> record.value()).foreachRDD(rdd -> {
            rdd.collect().forEach(message -> {
                System.out.println("Received message: " + message);
                // Add your processing logic here
            });
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
