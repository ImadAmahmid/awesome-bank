package com.awesome.bank.event;

import java.util.List;

/**
 * BI event subscriber abstract type, the implementation receives a list of BI events and process on them according to
 * their need.
 */
public interface BiEventSubscriber {

  /**
   * receive the list of new events to handle
   */
  void receive(List<BiEvent<?>> biEvents);
}
