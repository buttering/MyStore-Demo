package com.csu.mypetstore.api.util;

import com.csu.mypetstore.api.domain.ImageToken;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfiguration {

    @Bean
    public Cache<String, String> localCache() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public Cache<String, ImageToken> imageCache() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(50, TimeUnit.MINUTES)
                .build();
    }
}
