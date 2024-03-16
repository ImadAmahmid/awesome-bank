package com.awesome.bank.event;

import java.util.List;

/**
 * BI event publisher, allows the subscription to the event and publish the list of events to the subscribers.
 *
 * @param <?> is a sub-type of {@link BiEvent}.
 */
public interface BiEventPublisher {

  /**
   * publish to the subscribers the list of events
   *
   * @param biEvents events to be published
   */
  void publish(List<BiEvent<?>> biEvents);

  /**
   * the way allows a subscriber to subscribe itself to receive the update on the events.
   *
   * @param subscriber which is going to be subscribed
   */
  void subscribe(BiEventSubscriber subscriber);
}
