package com.lim.kafka;

import com.alibaba.fastjson.JSONObject;
import com.lim.bean.Rate;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinhao
 */
public class MessageUtil {

    private static final String SPACE_SIGN = " ";
    private static final String LINE_SIGN = "\\|";
    private static final String JSON_KEY = "message";
    private static final String RATE_KEY = "sysFlag";
    private static final String SEARCH_KEY = "3008|";
    private static final String DEV_KEY = "dev";
    public static Map<String, List<String>> tempMap = new ConcurrentHashMap<>();

    /**
     * 获取：3008|201|201|1111|2222
     * MONITOR: 2018-10-19 16:10:10 [7624393] [100000000] [z_lrm.c:446] 3008|201|201|1111|2222
     * @return
     */
    public static void parse(List<ConsumerRecord<String, String>> records, Map<String, Rate> buffer) {
        List<String> result = new ArrayList<>();
        records.forEach(record ->
                {
                    String val = record.value();
                    if (val.contains(SEARCH_KEY)) {
                        JSONObject jsonObject = JSONObject.parseObject(val);
                        if (jsonObject.containsKey(JSON_KEY)) {
                            String mess = jsonObject.getString(JSON_KEY);
                            String[] splitBySpace = mess.split(SPACE_SIGN);
                            result.add(splitBySpace[splitBySpace.length - 1]);
                        }
                    }
                }
        );
        group(result, buffer);
    }

    public static void group(List<String> list, Map<String, Rate> buffer) {
        for (String nums : list) {
            String[] splitBySign = nums.split(LINE_SIGN);
            String mainDev = splitBySign[1];
            if (tempMap.containsKey(DEV_KEY + mainDev)) {
                tempMap.get(DEV_KEY + mainDev).add(splitBySign[3] + "|" + splitBySign[4]);
            } else {
                List<String> l = new ArrayList<>();
                l.add(splitBySign[3] + "|" + splitBySign[4]);
                tempMap.put(DEV_KEY + mainDev, l);
                buffer.put(RATE_KEY + mainDev, new Rate());
            }
        }
    }

    public static void result(Map<String, Rate> buffer) {
        tempMap.forEach((mainDev, val) -> {
            for (String speeds : val) {
                String[] splitBySign = speeds.split(LINE_SIGN);
                String send = splitBySign[0];
                String receive = splitBySign[1];
                Rate rate = buffer.get(mainDev.replaceAll(DEV_KEY, RATE_KEY));
                rate.setMaxSend(rate.getMaxSend() > (Integer.parseInt(send) - rate.getSend()) ? rate.getMaxSend() : (Integer.parseInt(send) - rate.getSend()));
                rate.setMaxReceive(rate.getMaxReceive() > (Integer.parseInt(receive) - rate.getReceive()) ? rate.getMaxReceive() : (Integer.parseInt(receive) - rate.getMaxReceive()));
                rate.setSend(Integer.parseInt(send));
                rate.setReceive(Integer.parseInt(receive));
            }
        });
    }

}
