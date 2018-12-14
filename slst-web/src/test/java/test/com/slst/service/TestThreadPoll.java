package test.com.slst.service;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import test.com.slst.BaseTest;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-04-27 18:32
 */
public class TestThreadPoll extends BaseTest {
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void  testThreadPoolTaskExecutor(){
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    System.out.println(Thread.currentThread().getName() + " Runnable-------------------->" + System.currentTimeMillis() );
                }
            }
        });

        Future<Integer> result =  threadPoolTaskExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return Long.valueOf(System.currentTimeMillis()).intValue();
            }
        });

        try {
            System.out.println("Callable---------->"+result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

}
