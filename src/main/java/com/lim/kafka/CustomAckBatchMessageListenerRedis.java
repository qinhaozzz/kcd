package com.lim.kafka;

import com.lim.util.MultiThreadMessageUtilRedis;
import com.lim.util.RedisUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qinhao
 */
//@Component
public class CustomAckBatchMessageListenerRedis implements BatchAcknowledgingMessageListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomAckBatchMessageListenerRedis.class);

    @Autowired
    private RedisUtil redisUtil;

    /**
     * Invoked with data from kafka.
     * @param records        the data to be processed.
     * @param acknowledgment the acknowledgment.
     */
    @Override
    public void onMessage(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment) {
        MultiThreadMessageUtilRedis.parse(redisUtil, records);
        acknowledgment.acknowledge();
    }
}
