#!/bin/bash

# Start the Kafka server
/kafka/bin/kafka-server-start.sh /kafka/config/server.properties &

# Wait for the Kafka server to start up
sleep 10

# Create the Kafka topic
/kafka/bin/kafka-topics.sh --create --topic example-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1

# Keep the container running
wait
