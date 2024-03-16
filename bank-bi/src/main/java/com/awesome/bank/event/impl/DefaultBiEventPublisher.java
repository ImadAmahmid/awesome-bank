package com.awesome.bank.event.impl;

import com.awesome.bank.event.BiEvent;
import com.awesome.bank.event.BiEventPublisher;
import com.awesome.bank.event.BiEventSubscriber;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * BI event concrete publisher.
 *
 * Publish the given BI event collection to the subscribers to perform the updates on them.
 */
@Slf4j
public class DefaultBiEventPublisher implements BiEventPublisher {

  /**
   * list of subscribers interested in receiving the events
   */
  private final List<BiEventSubscriber> subscribers = new ArrayList<>();

  @Override
  public void publish(List<BiEvent<?>> biEvents) {
    subscribers.forEach(sub -> sub.receive(biEvents));
  }

  @Override
  public void subscribe(BiEventSubscriber subscriber) {
    subscribers.add(subscriber);
  }
}
