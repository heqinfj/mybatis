package org.apache.ibatis.cache;

import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author heqin
 */
public class BlockingCacheTest {

    @Test
    void shouldTestIdEqual() {
        BlockingCache blockingCache = new BlockingCache(new PerpetualCache("default"));
        assertEquals("default", blockingCache.getId());
        assertEquals(0, blockingCache.getSize());
    }

    @Test
    void shouldTestPutAndGetObject() throws InterruptedException {
        BlockingCache blockingCache = new BlockingCache(new PerpetualCache("default"));
        String objectKey = "userName";
        assertEquals(null, blockingCache.getObject(objectKey));
        blockingCache.putObject(objectKey, "helloKitty");
        assertEquals("helloKitty", blockingCache.getObject(objectKey));

        //模拟并发访问getObject方法
        ExecutorService executorService = Executors.newCachedThreadPool();

        //并发开关
        CountDownLatch concurrentSwitch = new CountDownLatch(1);
        int count = 3;
        //模拟并发任务数
        CountDownLatch concurrentTaskSignal = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("子线程" + Thread.currentThread().getName() + "正准备接受命令。");
                        //当前线程阻塞在concurrentSwitch对象上
                        concurrentSwitch.await();
                        System.out.println(String.format("线程：%s 已接受命令,获取值：%s。", Thread.currentThread().getName(), blockingCache.getObject(objectKey)));
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    } finally {
                        //当concurrentTaskSignal减为0,则唤醒阻塞在concurrentTaskSignal对象上的线程
                        concurrentTaskSignal.countDown();
                    }
                }
            };
            //为线程池添加任务
            executorService.execute(runnable);
        }
        //模拟main线程休眠2秒
        Thread.sleep(2000);
        //唤醒阻塞在concurrentSwitch对象上的线程
        concurrentSwitch.countDown();
        System.out.println("主线程" + Thread.currentThread().getName() + "已打开并发开关，正在等待所有子线程的处理结果。");
        //当前线程阻塞在concurrentTaskSignal对象上
        concurrentTaskSignal.await();
        System.out.println("主线程" + Thread.currentThread().getName() + "已收到所有子线程的处理结果。");

        //任务结束，停止线程池的所有线程
        executorService.shutdown();

    }
}
