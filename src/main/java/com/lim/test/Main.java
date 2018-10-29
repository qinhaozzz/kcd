package com.lim.test;

import com.lim.util.RedisUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author qinhao
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        RedisUtil redisUtil = (RedisUtil) applicationContext.getBean("redisUtil");
        System.out.println(redisUtil.hasKey("kafka"));
    }
}
