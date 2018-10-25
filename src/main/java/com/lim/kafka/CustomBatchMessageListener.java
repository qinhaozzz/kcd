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
public class CustomBatchMessageListener implements BatchMessageListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomBatchMessageListener.class);
    private Map<String, Rate> buffer;

    public CustomBatchMessageListener() {
        logger.info("CustomBatchMessageListener initial...");
        buffer = new ConcurrentHashMap<>();
        buffer.put("SysFlag|601-201|", new Rate());
    }

    /**
     * Invoked with data from kafka.
     * @param records the data to be processed.
     */
    @Override
    public void onMessage(List<ConsumerRecord<String, String>> records) {
        long start = System.currentTimeMillis();
        MessageUtil.result(MessageUtil.parse(records), buffer);
        long end = System.currentTimeMillis();
        logger.info("used time: " + (end - start));
        logger.info("count: " + records.size());
        logger.info("buffer: " + buffer);
    }

}
