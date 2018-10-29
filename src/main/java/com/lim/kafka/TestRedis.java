package com.lim.kafka;

import com.lim.util.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qinhao
 */
@Component
public class TestRedis {

    @Resource(name = "redisUtil")
    private RedisUtil redisUtil;

    public TestRedis() {
    }

}
