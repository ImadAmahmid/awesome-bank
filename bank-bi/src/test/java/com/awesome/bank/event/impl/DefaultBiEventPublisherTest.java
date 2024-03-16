package com.awesome.bank.event.impl;


import com.awesome.bank.event.BiEvent;
import com.awesome.bank.event.BiEventSubscriber;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DefaultBiEventPublisherTest {

    private final DefaultBiEventPublisher publisher = new DefaultBiEventPublisher();

    @Mock
    private BiEventSubscriber testSubscriber;

    @Test
    @DisplayName("Test subscription and publish events to the subscribers")
    void publishAndSubscribe_mockSubscriber_verifyTriggered() {
        doNothing().when(testSubscriber).receive(anyList());
        BiEvent event = TestBiEvent.builder().build();
        publisher.subscribe(testSubscriber);
        publisher.publish(Collections.singletonList(event));
        verify(testSubscriber).receive(anyList());
    }

    @SuperBuilder
    static class TestBiEvent extends BiEvent<String> {

    }
}