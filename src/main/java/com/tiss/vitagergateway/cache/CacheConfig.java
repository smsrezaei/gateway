package com.tiss.vitagergateway.cache;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static java.util.Arrays.asList;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    @Bean
    @Primary
    public static CacheManager memCacheManager() {
        ConcurrentMapCacheManager mgr = new ConcurrentMapCacheManager("cache");
        mgr.setCacheNames(asList("cache"));
        return mgr;
    }
    @CacheEvict(allEntries = true, value = "cache")
    @Scheduled(fixedDelay = 10*60*1000 ,  initialDelay = 5000)
    public void reportCacheEvict() {
        System.out.println("Flush Cache ");
    }


}