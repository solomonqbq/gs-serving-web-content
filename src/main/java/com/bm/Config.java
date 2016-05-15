package com.bm;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by qinbaoqi on 2016/5/15.
 */
@Configuration
@EnableCaching
public class Config  {

    @Bean
    public CacheManager getEhCacheManager(){
        return  new EhCacheCacheManager(getEhCacheFactory().getObject());
    }
    @Bean
    public EhCacheManagerFactoryBean getEhCacheFactory(){
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factoryBean.setShared(true);
        return factoryBean;
    }

    @Value("${spring.thymeleaf.cache}")
    private String cache;

    @Bean(name = "mapdb")
    public ConcurrentMap getMap(){
        DB db = DBMaker.fileDB("file.db").make();
        ConcurrentMap map = db.hashMap("map").make();
        map.put("something", "here");
        return map;
    }
}
