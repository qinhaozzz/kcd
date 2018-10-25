package com.lim.kafka;

import com.alibaba.fastjson.JSONObject;
import com.lim.bean.Rate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qinhao
 */
public class MessageUtil {

    private static final String SPACE_SIGN = " ";
    private static final String LINE_SIGN = "\\|";
    private static final String JSON_KEY = "message";
    private static final String RATE_KEY = "SysFlag|601-201|";
    private static final String SEARCH_KEY = "3008|601|201|";

    /**
     * MONITOR: 2018-10-19 16:10:10 [7624393] [100000000] [z_lrm.c:446] 3008|201|201|1111|2222
     * @return
     */
    public static List<String> parse(List<ConsumerRecord<String, String>> records) {
        List<String> result = new ArrayList<>();
        records.forEach(record ->
                {
                    String val = record.value();
                    if (val.contains(SEARCH_KEY)) {
                        JSONObject jsonObject = JSONObject.parseObject(record.value());
                        if (jsonObject.containsKey(JSON_KEY)) {
                            String mess = jsonObject.getString(JSON_KEY);
                            result.add(mess);
                        }
                        result.add(val);
                    }
                }
        );
        return result;
    }


    public static void result(List<String> list, Map<String, Rate> map) {
        for (String mess : list) {
            String[] splitBySpace = mess.split(SPACE_SIGN);
            String nums = splitBySpace[splitBySpace.length - 1];
            String[] splitBySign = nums.split(LINE_SIGN);
            Rate rate = map.get(RATE_KEY);
            rate.setMaxSend(rate.getMaxSend() > (Integer.parseInt(splitBySign[3]) - rate.getSend()) ? rate.getMaxSend() : (Integer.parseInt(splitBySign[3]) - rate.getSend()));
            rate.setMaxReceive(rate.getMaxReceive() > (Integer.parseInt(splitBySign[4]) - rate.getReceive()) ? rate.getMaxReceive() : (Integer.parseInt(splitBySign[4]) - rate.getMaxReceive()));
            rate.setSend(Integer.parseInt(splitBySign[3]));
            rate.setReceive(Integer.parseInt(splitBySign[4]));
        }
    }
}
