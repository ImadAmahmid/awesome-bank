package com.awesome.bank.kafka.service;

import com.awesome.bank.kafka.generated.dto.OperationV2;

/**
 * To be implemented by notification senders to notify the user of updates.
 */
public interface NotificationSender {

    /**
     * Sends a notification
     * @param operation
     */
    void dispatch(OperationV2 operation);

}
