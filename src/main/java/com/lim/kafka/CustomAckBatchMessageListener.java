package com.lim.kafka;

import com.lim.bean.Rate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinhao
 */
public class CustomAckBatchMessageListener implements BatchAcknowledgingMessageListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomAckBatchMessageListener.class);
    private Map<String, Rate> buffer;

    public CustomAckBatchMessageListener() {
        buffer = new ConcurrentHashMap<>();
    }

    /**
     * Invoked with data from kafka.
     * @param records        the data to be processed.
     * @param acknowledgment the acknowledgment.
     */
    @Override
    public void onMessage(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment) {
        MessageUtil.parse(records, buffer);
        MessageUtil.result(buffer);
        logger.info("count: " + records.size());
        logger.info("buffer: " + buffer);
        acknowledgment.acknowledge();
    }
}
