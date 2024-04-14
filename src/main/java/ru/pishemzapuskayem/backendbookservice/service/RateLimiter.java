package ru.pishemzapuskayem.backendbookservice.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RateLimiter {
    private final long minIntervalMillis;
    private long lastCallTime;

    public RateLimiter(long minIntervalMillis) {
        this.minIntervalMillis = minIntervalMillis;
        this.lastCallTime = 0;
    }

    public synchronized void tryInvoke(Runnable method, boolean forced) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCallTime >= minIntervalMillis || forced) {
            method.run();
            lastCallTime = currentTime;
        } else {
            log.info("Method call skipped");
        }
    }
}
