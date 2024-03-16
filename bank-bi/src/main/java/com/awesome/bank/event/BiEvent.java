package com.awesome.bank.event;


import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
public abstract class BiEvent<T> {

  /**
   * event name, e.g. CREATE_OPERATION
   */
  private final String event;

  /**
   * time when event is generated, contains timezone
   */
  private final Instant timestamp;

  /**
   * properties coming from specific event
   */
  private final T properties;
}
