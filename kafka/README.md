# Kafka

## Kafka commands
A list of helpful commands for interacting with kafka.

Assumes kafka and zookeeper are running locally as docker containers on ports 9092 and 2181 respectively.

#### Connecting to your local kafka broker
```
docker exec -it kafka bash
cd /opt/kafka
```

#### Managing topics
```
# list topics
bin/kafka-topics.sh --zookeeper localhost:2181 --list

# create topic
bin/kafka-topics.sh --create --topic order-flow --zookeeper localhost:2181 --partitions 1 --replication-factor 1

# delete topic
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic order-flow
```

#### Reading and sending messages
```
# write to topic
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic order-flow

# read from topic
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order-flow

# read from topic from the beginning of time
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order-flow --from-beginning
```