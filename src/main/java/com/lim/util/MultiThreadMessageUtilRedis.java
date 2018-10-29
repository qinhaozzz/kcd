package com.lim.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinhao
 */
public class MultiThreadMessageUtilRedis {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadMessageUtilRedis.class);

    private static final List<String> TYPE_CODE = new ArrayList<>();
    private static final String CHECK_CODE = "\"message\":\"MONITOR:";
    private static final String SYS_FLAG = "sysFlag";

    /**
     * 分隔符
     */
    private static final String SPACE_SIGN = " ";
    private static final String LINE_SIGN = "\\|";
    private static final String WILD_SIGN = "*";

    static {
        TYPE_CODE.add("3008");
    }

    /**
     * 关键信息截取
     * @param json
     * @return 3008|201|201|1111|2222
     */
    private static String format(String json) {
        if (StringUtils.isEmpty(json) || !json.contains(CHECK_CODE)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray tags = jsonObject.getJSONArray("tags");
        String message = jsonObject.getString("message");
        String[] splitBySpaceSign = message.split(SPACE_SIGN);
        return splitBySpaceSign[splitBySpaceSign.length - 1];
    }

    /**
     * 数据处理
     * @param redisUtil
     * @param records
     */
    public static void parse(RedisUtil redisUtil, List<ConsumerRecord<String, String>> records) {
        records.forEach(record -> {
            String recordValue = record.value();
            String codeStr = format(recordValue);
            if (StringUtils.isEmpty(codeStr)) {
                logger.error("log parse error, recordValue:{}, codeStr:{}", recordValue, codeStr);
            }
            String[] codes = codeStr.split(LINE_SIGN);
            String mainDevCode = codes[1];
            String sendStr = codes[3];
            String receiveStr = codes[4];
            String flag = SYS_FLAG + mainDevCode;
            if (redisUtil.hasKey(flag + WILD_SIGN)) {
                int maxSend = (int) redisUtil.get(flag + "MaxSend");
                int maxReceive = (int) redisUtil.get(flag + "MaxReceive");
                int send = (int) redisUtil.get(flag + "Send");
                int receive = (int) redisUtil.get(flag + "Receive");

                int a = Integer.parseInt(sendStr) - send;
                if (maxSend < a) {
                    redisUtil.set(flag + "MaxSend", a);
                }
                int b = Integer.parseInt(receiveStr) - receive;
                if (maxReceive < b) {
                    redisUtil.set(flag + "MaxReceive", b);
                }
                redisUtil.set(flag + "Send", send);
                redisUtil.set(flag + "Receive", receive);
            } else {
                redisUtil.set(flag + "MaxSend", 0);
                redisUtil.set(flag + "MaxReceive", 0);
                redisUtil.set(flag + "Send", Integer.parseInt(sendStr));
                redisUtil.set(flag + "Receive", Integer.parseInt(receiveStr));
            }
        });
    }
}
