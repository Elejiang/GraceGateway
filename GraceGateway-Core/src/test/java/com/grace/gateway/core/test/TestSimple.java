package com.grace.gateway.core.test;

import com.grace.gateway.core.algorithm.ConsistentHashing;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.grace.gateway.common.constant.BasicConstant.DATE_DEFAULT_FORMATTER;

public class TestSimple {

    @Test
    public void testDate() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_DEFAULT_FORMATTER)) + "---" + UUID.randomUUID());
    }

    @Test
    public void testClassName() {
        System.out.println(TestSimple.class.getName());
    }

    @Test
    public void testAtomicInteger() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE - 2);
        System.out.println(atomicInteger.incrementAndGet());
        System.out.println(atomicInteger.incrementAndGet());
        System.out.println(atomicInteger.incrementAndGet());
        System.out.println(atomicInteger.incrementAndGet());
    }

    @Test
    public void testAsync() throws InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("异步任务执行完成");
            throw new RuntimeException("异步异常");
        });

        Thread.sleep(1000);
        System.out.println("添加后处理");
        future.whenComplete((re, th) -> {
            if (th != null) {
                System.out.println("再抛异常");
                throw new RuntimeException("再抛异常");
            }
            System.out.println("final1");
        });
        Thread.sleep(1000);
        future.exceptionally(throwable -> {
            System.out.println("接收到异常 + " + throwable);
            return null;
        });
    }

    @Test
    public void testSupplierAsync() throws InterruptedException {
        Supplier<CompletableFuture<Void>> supplier = () -> {
            System.out.println("supplier begin");
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("异步任务");
                throw new RuntimeException();
            });
            future.whenComplete((re, th) -> {
                if (th != null) {
                    System.out.println("收到异常：" + th);
                    throw new RuntimeException(th);
                }
                System.out.println("代码不走了");
            });
            return future;
        };
        CompletableFuture<Void> future = supplier.get();
        Thread.sleep(2000);
        future.exceptionally(throwable -> {
            System.out.println("外面收到异常1：" + throwable);
            return null;
        });
        future.exceptionally(throwable -> {
            System.out.println("外面收到异常2：" + throwable);
            return null;
        });
    }

    @Test
    public void testMap() {
        Map<String, String> map = new ConcurrentHashMap<>();
        System.out.println(map.get("aaa"));
        map.computeIfAbsent("aaa", name -> "vvv");
        System.out.println(map.get("aaa"));
    }

    @Test
    public void testHash() {
        List<String> nodes = Arrays.asList("user1", "user2");
        ConsistentHashing consistentHashing = new ConsistentHashing(nodes, 20);
        System.out.println(consistentHashing.getNode("aad12wa"));
        System.out.println(consistentHashing.getNode("dd5l4k5d"));
        System.out.println(consistentHashing.getNode("cck45hj"));
        System.out.println(consistentHashing.getNode("cc4dw4a35dc"));
    }

}
