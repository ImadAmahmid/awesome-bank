package com.awesome.bank.event;

/**
 * BI event subscriber abstract type, the implementation receives a list of BI events and process on them according to
 * their need.
 */
public interface BiEventConsumer extends IDispatch {
    <T extends BiEvent> void onBiEvent(T biEvent);
}
