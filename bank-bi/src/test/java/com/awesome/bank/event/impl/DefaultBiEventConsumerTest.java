package com.awesome.bank.event.impl;

import com.awesome.bank.event.BiEvent;
import com.awesome.bank.event.BiEventConsumer;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DefaultBiEventConsumerTest {

    @Mock
    DefaultBiEventPublisher publisher;

    BiEventConsumer consumer;

    @BeforeEach
    void setup() {
        consumer = new DefaultBiEventConsumer(publisher, 2);
    }

    @Test
    void onBiEvent_onlyPublishAfterThirdEvent() {
        doNothing().when(publisher).publish(any());
        consumer.onBiEvent(TestBiEvent.builder().build());
        verify(publisher, times(0)).publish(any());
        consumer.onBiEvent(TestBiEvent.builder().build());
        verify(publisher, times(1)).publish(any());
        consumer.onBiEvent(TestBiEvent.builder().build());
        verify(publisher, times(1)).publish(any());
        // call again, the event collection should have one element and the publish will be called again
        consumer.dispatch();
        verify(publisher, times(2)).publish(any());
        // now that the list is cleared call again and the publish should not be triggered
        consumer.dispatch();
        verify(publisher, times(2)).publish(any());
    }

    @SuperBuilder
    static class TestBiEvent extends BiEvent<String> {

    }
}