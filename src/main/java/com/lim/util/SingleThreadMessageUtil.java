package com.lim.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lim.bean.Rate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qinhao
 */
public class SingleThreadMessageUtil {

    private static final Logger logger = LoggerFactory.getLogger(SingleThreadMessageUtil.class);

    private static final List<String> TYPE_CODE = new ArrayList<>();
    private static final String CHECK_CODE = "\"message\":\"MONITOR:";
    private static final String SYS_FLAG = "sysFlag";
    /**
     * 分隔符
     */
    private static final String SPACE_SIGN = " ";
    private static final String LINE_SIGN = "\\|";

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

    public static void parse(Map<String, Rate> buffer, ConsumerRecord<String, String> record) {
        String recordValue = record.value();
        String codeStr = format(recordValue);
        if (StringUtils.isEmpty(codeStr)) {
            logger.error("log parse error, recordValue:{}, codeStr:{}", recordValue, codeStr);
        }
        String[] codes = codeStr.split(LINE_SIGN);
        String mainDevCode = codes[1];
        String sendStr = codes[3];
        String receiveStr = codes[4];
        Rate rate;
        if (buffer.containsKey(mainDevCode + SYS_FLAG)) {
            rate = buffer.get(mainDevCode + SYS_FLAG);
        } else {
            rate = new Rate();
            buffer.put(mainDevCode + SYS_FLAG, rate);
        }
        rate.setMaxSend(rate.getMaxSend() > (Integer.parseInt(sendStr) - rate.getSend()) ? rate.getMaxSend() : (Integer.parseInt(sendStr) - rate.getSend()));
        rate.setMaxReceive(rate.getMaxReceive() > (Integer.parseInt(receiveStr) - rate.getReceive()) ? rate.getMaxReceive() : (Integer.parseInt(receiveStr) - rate.getMaxReceive()));
        rate.setSend(Integer.parseInt(sendStr) - rate.getSend());
        rate.setReceive(Integer.parseInt(receiveStr) - rate.getReceive());
    }
}
