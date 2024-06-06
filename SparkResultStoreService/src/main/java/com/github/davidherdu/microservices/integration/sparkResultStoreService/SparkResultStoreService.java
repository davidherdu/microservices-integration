package com.github.davidherdu.microservices.integration.sparkResultStoreService;

import java.util.concurrent.TimeoutException;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

public class SparkResultStoreService {

    public static void main(String[] args) throws StreamingQueryException, TimeoutException {
        SparkSession spark = SparkSession.builder()
                .appName("SparkResultStoreService")
                .master("local[*]")
                .getOrCreate();

        // Read from Kafka
        Dataset<Row> kafkaDF = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "example-topic")
                .load();

        Dataset<Row> values = kafkaDF.selectExpr("CAST(value AS STRING)");

        // Write to HDFS
        StreamingQuery hdfsQuery = values.writeStream()
                .outputMode("append")
                .format("parquet")
                .option("path", "hdfs://localhost:9000/path/to/output")
                .option("checkpointLocation", "hdfs://localhost:9000/path/to/checkpoint")
                .start();

        // Write to S3
        StreamingQuery s3Query = values.writeStream()
                .outputMode("append")
                .format("parquet")
                .option("path", "s3a://your-bucket/path/to/output")
                .option("checkpointLocation", "s3a://your-bucket/path/to/checkpoint")
                .start();

        hdfsQuery.awaitTermination();
        s3Query.awaitTermination();
    }
}
