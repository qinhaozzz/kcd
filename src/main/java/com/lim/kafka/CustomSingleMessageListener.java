package com.lim.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

/**
 * @author qinhao
 */
public class CustomSingleMessageListener implements MessageListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomSingleMessageListener.class);

    public CustomSingleMessageListener() {
        logger.info("CustomSingleMessageListener initial...");
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        logger.info("message: " + record.value());
    }
}
