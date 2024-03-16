package com.awesome.bank.kafka.service;

import com.awesome.bank.kafka.generated.dto.OperationV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;


/**
 * A dummy class to test the consuming of the events and the retry mechanism.
 *
 * PS: The consuming will fail if the id of the operation is an odd number
 *
 */
@Slf4j
@Service
public class DummyNotificationSender implements NotificationSender {

    @Override
    public void dispatch(OperationV2 operation) {
        LOG.info("[Notification sender] Sending email to account holder regarding operation | OperationId=[{}] Amount=[{}]", operation.getId(), operation.getAmount());

        Long id = operation.getId();

        if (id % 2 == 1) {
            LOG.error("Simulating a sending error for operation ID = [{}]", id);
            throw new RuntimeException(format("Try again for operation id={%s}", operation.getId()));
        }

        // For now, we do not send an email, we only log
        LOG.info("Successfully ingested the operation Id = [{}] in consumer", id);
    }
}
