package com.lim.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.BatchMessageListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinhao
 */
public class CustomBatchMessageListener implements BatchMessageListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomBatchMessageListener.class);
    private Map<String, Rate> buffer;

    public CustomBatchMessageListener() {
        logger.info("CustomBatchMessageListener initial...");
        buffer = new HashMap<>();
        buffer.put("SysFlag|601-201|", new Rate());
    }

    /**
     * Invoked with data from kafka.
     * @param records the data to be processed.
     */
    @Override
    public void onMessage(List<ConsumerRecord<String, String>> records) {
        long start = System.currentTimeMillis();
        List<String> list = MessageUtil.parse(records);
        MessageUtil.result(list,buffer);
        long end = System.currentTimeMillis();
        logger.info("used time: " + (end - start));
        logger.info("count1: " + records.size());
        logger.info("count2: " + list.size());
        logger.info("buffer"+buffer);
    }

    public class Rate {
        private int send;
        private int receive;
        private int maxSend;
        private int maxReceive;

        public int getSend() {
            return send;
        }

        public void setSend(int send) {
            this.send = send;
        }

        public int getReceive() {
            return receive;
        }

        public void setReceive(int receive) {
            this.receive = receive;
        }

        public int getMaxSend() {
            return maxSend;
        }

        public void setMaxSend(int maxSend) {
            this.maxSend = maxSend;
        }

        public int getMaxReceive() {
            return maxReceive;
        }

        public void setMaxReceive(int maxReceive) {
            this.maxReceive = maxReceive;
        }

        @Override
        public String toString() {
            return "Rate{" +
                    "send=" + send +
                    ", receive=" + receive +
                    ", maxSend=" + maxSend +
                    ", maxReceive=" + maxReceive +
                    '}';
        }
    }
}
