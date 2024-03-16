package com.awesome.bank.event.impl;

import com.awesome.bank.event.BiEvent;
import com.awesome.bank.event.BiEventConsumer;
import com.awesome.bank.event.BiEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * BI event consumer.
 * Consume a generic {@link BiEvent} type event through listening on application events, push them to the event publisher when the size of the collected event touches the defined limit.
 *
 * // todo: Trigger the publishing of event every during periodic intervals in case we do not receive enough events scheduled task gets triggered.
 */
@Slf4j
public class DefaultBiEventConsumer implements BiEventConsumer {

  /**
   * A collection of BI events ready to be published.
   */
  private final BlockingQueue<BiEvent<?>> biEvents;

  /**
   * Event publisher used to publish the events when needed.
   */
  private final BiEventPublisher publisher;

  public DefaultBiEventConsumer(BiEventPublisher publisher, int limit) {
    this.publisher = publisher;
    this.biEvents = new LinkedBlockingDeque<>(limit);
  }

  /**
   * listen to the {@link BiEvent}, which is a spring application event.
   */
  @EventListener
  @Async
  public <T extends BiEvent> void onBiEvent(T biEvent) {
    LOG.trace("[BI consumer] New bi event received [{}]", biEvent);
    this.biEvents.add(biEvent);
    if (this.biEvents.remainingCapacity() == 0) {
      LOG.debug("[BI consumer] Event collection arrives to its size limit");
      dispatch();
    }
  }

  /**
   * Task to publish the event collection.
   *
   * <p> This is called and executed by either a scheduler or event collection arrives to the limit,
   * it's the reason why in the body it checks again the event list and publish the events only when event list is not
   * empty.
   */
  public void dispatch() {
    List<BiEvent<?>> biEvents = new ArrayList<>(this.biEvents);
    if (!biEvents.isEmpty()) {
      LOG.debug("[BI consumer] Publishing event collection with [{}] events", biEvents.size());
      this.biEvents.clear();
      publisher.publish(biEvents);
    }
  }
}
