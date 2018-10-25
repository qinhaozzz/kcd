package com.lim.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * @author qinhao
 */
public class CustomAckSingleMessageListener implements AcknowledgingMessageListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomAckSingleMessageListener.class);

    /**
     * Invoked with data from kafka.
     * @param record         the data to be processed.
     * @param acknowledgment the acknowledgment.
     */
    @Override
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        logger.info("message: " + record.value());
        logger.info("single ack start...");
        acknowledgment.acknowledge();
        logger.info("single ack success...");
    }
}
