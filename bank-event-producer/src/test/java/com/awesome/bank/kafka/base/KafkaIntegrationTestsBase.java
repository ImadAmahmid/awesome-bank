package com.awesome.bank.kafka.base;

import com.awesome.bank.kafka.event.KafkaProducerAndConsumerTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(initializers = {KafkaProducerAndConsumerTest.Initializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaIntegrationTestsBase {

    protected static Network network = Network.newNetwork();

    @Container
    protected static final KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.5.3"))
                    .withEmbeddedZookeeper()
                    .withEnv("KAFKA_LISTENERS", "PLAINTEXT://0.0.0.0:9093 ,BROKER://0.0.0.0:9092")
                    .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "BROKER:PLAINTEXT,PLAINTEXT:PLAINTEXT")
                    .withEnv("KAFKA_INTER_BROKER_LISTENER_NAME", "BROKER")
                    .withEnv("KAFKA_BROKER_ID", "1")
                    .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1")
                    .withEnv("KAFKA_OFFSETS_TOPIC_NUM_PARTITIONS", "1")
                    .withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1")
                    .withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1")
                    .withEnv("KAFKA_LOG_FLUSH_INTERVAL_MESSAGES", Long.MAX_VALUE + "")
                    .withEnv("KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS", "0")
                    .withNetwork(network);

    static {
        Startables.deepStart(Stream.of(kafkaContainer)).join();
    }

    protected static void waitFiveSecondsUntilConsumerIsUpAndRunning() {
        await().atMost(5, SECONDS).untilAsserted(() -> {
        });
    }

    public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                            "spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers(),
                            "spring.kafka.consumer.group-id=consumer-bank",
                            "spring.kafka.consumer.auto-offset-reset=earliest",
                            "messaging.enabled=true")
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}
