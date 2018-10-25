package com.lim.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

/**
 * @author qinhao
 */
public class CustomAckBatchMessageListener implements BatchAcknowledgingMessageListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomAckBatchMessageListener.class);

    /**
     * Invoked with data from kafka.
     * @param records        the data to be processed.
     * @param acknowledgment the acknowledgment.
     */
    @Override
    public void onMessage(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment) {
        long start = System.currentTimeMillis();
        logger.info("count: " + records.size());
        records.forEach(record ->
                logger.info("message: " + record.value())
        );
        logger.info("batch akc start...");
        acknowledgment.acknowledge();
        long end = System.currentTimeMillis();
        logger.info("batch akc success,used time: " + (end - start));
        end = System.currentTimeMillis();
        logger.info("all used time: " + (end - start));
    }
}
