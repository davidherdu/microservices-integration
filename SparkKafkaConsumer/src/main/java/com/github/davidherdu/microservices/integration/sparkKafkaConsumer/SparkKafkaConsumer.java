package com.github.davidherdu.microservices.integration.sparkKafkaConsumer;

import java.util.concurrent.TimeoutException;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

public class SparkKafkaConsumer {

	// Add --add-exports java.base/sun.nio.ch=ALL-UNNAMED to VM arguments
    public static void main(String[] args) throws StreamingQueryException, TimeoutException {
        SparkSession spark = SparkSession.builder()
                .appName("SparkKafkaConsumer")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> df = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "example-topic")
                .load();

        Dataset<Row> values = df.selectExpr("CAST(value AS STRING)");

        StreamingQuery query = values.writeStream()
                .outputMode("append")
                .format("console")
                .start();

        query.awaitTermination();
    }
}
