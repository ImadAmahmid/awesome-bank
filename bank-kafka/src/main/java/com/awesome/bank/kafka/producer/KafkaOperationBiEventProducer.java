package com.awesome.bank.kafka.producer;

import com.awesome.bank.event.BiEvent;
import com.awesome.bank.kafka.generated.dto.OperationV2;
import com.awesome.bank.kafka.mapper.OperationToDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * A subscriber that will be called to dispatch the events to a kafka topic.
 * The implementation of BiEventSubscriber can be a little misleading but in the BI context this producer will
 * be subscribing to receive events in the DefaultBiEventPublisher, then producing the events in form of kafka messages.
 */
@Slf4j
public class KafkaOperationBiEventProducer extends AbstractKafkaBiEventProducer<String, OperationV2> {

    @Value("${event-producer.operation.topic.name}")
    private String topicName;

    @Override
    Class<? extends Object> getEventTypeClass() {
        return com.awesome.bank.domain.model.Operation.class;
    }

    @Override
    String getTopicName() {
        return topicName;
    }

    @Override
    public void produce(BiEvent<?> biEvent) {
        // The key is picked in order to send all operation for one given account into the same partition to ensure proper ordering.
        // This way a consumer would not have inconsistencies such as getting an operation before the account for example.
        com.awesome.bank.domain.model.Operation operation = (com.awesome.bank.domain.model.Operation) biEvent.getProperties();
        String accountId = operation.getAccount().getId().toString();
        OperationV2 operationDto = OperationToDtoMapper.MAPPER.map(operation);
        publishMessageOnKafkaTopic(accountId, operationDto);
    }


}