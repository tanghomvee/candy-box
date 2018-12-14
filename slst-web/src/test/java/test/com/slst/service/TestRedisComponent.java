package test.com.slst.service;

import com.slst.common.components.RedisComponent;
import org.junit.Test;
import test.com.slst.BaseTest;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-07-23 16:58
 */
public class TestRedisComponent  extends BaseTest {

    @Resource
    private RedisComponent redisComponent;
    @Test
    public void incr() {
        System.out.println(redisComponent.incr("STORE_RULE_ST_PH_LIMIT_217" , 3600));
    }

    @Test
    public void dncr() {
    }

    @Test
    public void getAtomicLong() {
        System.out.println(redisComponent.getAtomicLong("STORE_RULE_ST_PH_LIMIT_2107"));
    }

    @Test
    public void lock() {
        redisComponent.lock("sb" , 1L);
    }

    @Test
    public void lock1() {
    }

    @Test
    public void lock2() {
        System.out.println();
    }

    @Test
    public void lock3() {
    }

    @Test
    public void lock4() {
    }

    @Test
    public void lock5() {
    }

    @Test
    public void unLock() {
    }

    @Test
    public void get() {
    }

    @Test
    public void set() {
    }

    @Test
    public void set1() {
    }

    @Test
    public void hSetNx() {
        Object rs = redisComponent.hSetNx("test_hash_map" , "map_key" , "map_val" , 60);
        System.out.println("1:" + rs);
        rs = redisComponent.hSetNx("test_hash_map" , "map_key" , "map_val" , 60);
        System.out.println("2:"+rs);

        rs = redisComponent.hdelKeyByFieldVal("test_hash_map" , "map_key" , "map_val");
        System.out.println("del:"+rs);

        hGet();
    }

    @Test
    public void hGet() {
        System.out.println(redisComponent.hGet("test_hash_map" , "map_key"));
    }
}