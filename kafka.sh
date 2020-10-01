cd /opt/kafka
bin/kafka-topics.sh --create --topic payment-commands --zookeeper zoo1:2181 --partitions 1 --replication-factor 1
bin/kafka-topics.sh --create --topic payment-events --zookeeper zoo1:2181 --partitions 1 --replication-factor 1
bin/kafka-topics.sh --create --topic order-events --zookeeper zoo1:2181 --partitions 1 --replication-factor 1
bin/kafka-topics.sh --zookeeper zoo1:2181 --list

bin/kafka-topics.sh --zookeeper zoo1:2181 --delete --topic quickstart-commands
bin/kafka-topics.sh --zookeeper zoo1:2181 --delete --topic quickstart-events
bin/kafka-topics.sh --zookeeper zoo1:2181 --delete --topic my-replicated-topic


bin/kafka-topics.sh --create --topic payment-commands --zookeeper kubernetes-kafka-zeebe_zookeeper_1:2181 --partitions 1 --replication-factor 1
bin/kafka-topics.sh --create --topic payment-events --zookeeper kubernetes-kafka-zeebe_zookeeper_1:2181 --partitions 1 --replication-factor 1
bin/kafka-topics.sh --create --topic order-events --zookeeper kubernetes-kafka-zeebe_zookeeper_1:2181 --partitions 1 --replication-factor 1
bin/kafka-topics.sh --zookeeper kubernetes-kafka-zeebe_zookeeper_1:2181 --list