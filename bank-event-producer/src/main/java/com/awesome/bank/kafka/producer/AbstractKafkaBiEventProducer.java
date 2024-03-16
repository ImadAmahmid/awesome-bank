package com.awesome.bank.kafka.producer;


import com.awesome.bank.event.BiEvent;
import com.awesome.bank.event.BiEventSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * An abstract class that will be the base for all kafka producers subscribing to
 * the bank bi events. This class will contain a method that checks if the BiEvent
 * correspond to the child class (i.e. the child class is handeling this type of Bi Event)
 * and send a kafka message ifn case it matches.
 * In case the message does not match it only exits
 */
@Slf4j
public abstract class AbstractKafkaBiEventProducer<KEY, VALUE> implements BiEventSubscriber {


    @Autowired
    KafkaTemplate<KEY, VALUE> kafkaTemplate;


    @Override
    public void receive(List<BiEvent<?>> biEvents) {
        biEvents.stream().filter(event -> event.getProperties().getClass().equals(getEventTypeClass())).forEach(this::produce);
    }


    void publishMessageOnKafkaTopic(KEY key, VALUE value) {
        CompletableFuture<SendResult<KEY, VALUE>> future = kafkaTemplate.send(getTopicName(), key, value);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                LOG.info("[Producer] Sent message=[{}] with offset=[{}]", value, result.getRecordMetadata().offset());
            } else {
                LOG.error("[Producer] Unable to send message=[{}]  due to : [{}]", ex.getMessage());
            }
        });
    }

    abstract Class<? extends Object> getEventTypeClass();

    abstract String getTopicName();

    public abstract void produce(BiEvent<?> biEvent);
}