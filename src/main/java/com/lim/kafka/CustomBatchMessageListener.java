package com.lim.kafka;

import com.lim.bean.Rate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.BatchMessageListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinhao
 */
//@Component
public class CustomBatchMessageListener implements BatchMessageListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomBatchMessageListener.class);
    private Map<String, Rate> buffer;



    public CustomBatchMessageListener() {
        logger.info("CustomBatchMessageListener initial...");
        buffer = new ConcurrentHashMap<>();
    }

    /**
     * Invoked with data from kafka.
     * @param records the data to be processed.
     */
    @Override
    public void onMessage(List<ConsumerRecord<String, String>> records) {
        long start = System.currentTimeMillis();

        long end = System.currentTimeMillis();
        logger.info("buffer: " + buffer);
    }

}
