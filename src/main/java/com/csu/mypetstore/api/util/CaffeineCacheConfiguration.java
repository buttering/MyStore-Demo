package com.csu.mypetstore.api.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfiguration {
    // 初始容量
    private final static int CAPACITY = 100;
    // 最大容量
    private final static int MAX_SIZE = 500;
    // 持续时间
    private final static int DURATION = 500;

    @Bean
    public Cache<String, String> localCache() {
        return Caffeine.newBuilder()
                .initialCapacity(CAPACITY)
                .maximumSize(MAX_SIZE)
                .expireAfterWrite(DURATION, TimeUnit.MINUTES)
                .build();
    }
}
