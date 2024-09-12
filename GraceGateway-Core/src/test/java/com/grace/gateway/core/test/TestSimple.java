package com.grace.gateway.core.test;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

}
