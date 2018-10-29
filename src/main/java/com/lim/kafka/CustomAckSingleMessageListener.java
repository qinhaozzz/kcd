package com.lim.kafka;

import com.lim.bean.Rate;
import com.lim.util.SingleThreadMessageUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinhao
 */
public class CustomAckSingleMessageListener implements AcknowledgingMessageListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomAckSingleMessageListener.class);

    private Map<String, Rate> buffer;

    public CustomAckSingleMessageListener() {
        buffer = new ConcurrentHashMap<>();
    }

    /**
     * Invoked with data from kafka.
     * @param record         the data to be processed.
     * @param acknowledgment the acknowledgment.
     */
    @Override
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        SingleThreadMessageUtil.parse(buffer, record);
        acknowledgment.acknowledge();
        logger.info("buffer: {}", buffer);
    }
}
