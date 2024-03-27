package com.awesome.bank.kafka.consumer;

import com.awesome.bank.kafka.generated.dto.OperationV2;
import com.awesome.bank.kafka.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.retry.annotation.Backoff;

/**
 * A consumer on the topic for operations
 */
@Slf4j
@RequiredArgsConstructor
public class KafkaOperationBiEventConsumer {

    private final NotificationSender notificationSender;

    /**
     * Will retry in case of error two times and then move these operations to the dead queue letter
     */
    @RetryableTopic(
            kafkaTemplate = "kafkaTemplate",
            attempts = "2",
            backoff = @Backoff(multiplierExpression = "5"),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(id = "#{'${spring.kafka.consumer.group-id}'}", topics = "#{'${event-producer.operation.topic.name}'}", containerFactory = "filterKafkaListenerContainerFactory")
    public void onOperationMessage(OperationV2 operation) {
        notificationSender.dispatch(operation);
    }


}