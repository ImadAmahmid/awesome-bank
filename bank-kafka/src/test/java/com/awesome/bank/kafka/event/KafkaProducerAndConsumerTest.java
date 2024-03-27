package com.awesome.bank.kafka.event;


import com.awesome.bank.domain.event.OperationBiEvent;
import com.awesome.bank.domain.model.Account;
import com.awesome.bank.domain.model.Operation;
import com.awesome.bank.domain.model.OperationType;
import com.awesome.bank.event.BiEventSubscriber;
import com.awesome.bank.kafka.KafkaDummyApp;
import com.awesome.bank.kafka.base.KafkaIntegrationTestsBase;
import com.awesome.bank.kafka.config.KafkaTestConfig;
import com.awesome.bank.kafka.service.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Test class for publication and consumption of events with Kafka.
 */
@Slf4j
@DirtiesContext
@SpringBootTest(classes = {KafkaDummyApp.class, KafkaTestConfig.class})
public class KafkaProducerAndConsumerTest extends KafkaIntegrationTestsBase {

    @SpyBean
    @Autowired
    NotificationSender notificationSender;

    //  This subscribed is none but the Kafka Producer. One the kafka producer will receive the events, he will publish
    // them to the kafka topic. Then the consumer will receive the event and call the notification sender
    @Autowired
    BiEventSubscriber biEventSubscriber;

    @BeforeAll
    static void setup() {
        waitFiveSecondsUntilConsumerIsUpAndRunning();
    }
    @Nested
    class TestKafkaConsumer {

        @Test
        @DisplayName("Produce a message successfully then consume it. The operation id is even, so the consuming will be successfully and no retry will be triggered")
        public void produceMessageSuccessfully_consumeMessageSuccessfully() {
            Operation operation = Operation.builder().id(0l).account(Account.builder().id(5l).build()).type(OperationType.DEPOSIT).amount(BigDecimal.TEN).build();
            OperationBiEvent biEvent = OperationBiEvent.builder().event("WITHDRAWAL").timestamp(Instant.now()).properties(operation).build();

            biEventSubscriber.receive(List.of(biEvent));
            biEventSubscriber.receive(List.of(biEvent, biEvent));

            await().pollInterval(Duration.ofSeconds(1)).atMost(30, SECONDS)
                    .untilAsserted(() -> verify(notificationSender, times(3)).dispatch(any()));
        }

        @Test
        @DisplayName("Produce a message successfully then consume it." +
                " The operation id is odd, so the consuming will be fail and the retry will be triggered")
        public void produceMessageSuccessfully_consumeMessageFail() {
            Operation operation = Operation.builder().id(1l).account(Account.builder().id(5l).build()).type(OperationType.DEPOSIT).amount(BigDecimal.TEN).build();
            OperationBiEvent biEvent = OperationBiEvent.builder().event("WITHDRAWAL").timestamp(Instant.now()).properties(operation).build();

            // the producer will then publish the operation events on kafka topic, later on will be consumed by the kafka consumer and the notification service will be called
            biEventSubscriber.receive(List.of(biEvent, biEvent));

            await().pollInterval(Duration.ofSeconds(1)).atMost(30, SECONDS)
                    // it's four times because Kafka will try to consume the message twice for each message (2 x 2)
                    .untilAsserted(() -> verify(notificationSender, times(4)).dispatch(any()));
        }
    }

}