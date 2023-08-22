package com.beastab.dataservice.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
@Data
@Slf4j
public class CustomConfigs {
    @Bean
    public RestTemplate restTemplate() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(3);
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(
                        HttpClientBuilder.create().setConnectionManager(connectionManager)
                                .setDefaultRequestConfig(
                                        RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                                .evictIdleConnections(20, TimeUnit.SECONDS).build());
        requestFactory.setConnectTimeout(50000);
        requestFactory.setReadTimeout(100000);
        requestFactory.setConnectionRequestTimeout(50000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache categoriesCache = buildCache("CategoriesCache", 6 * 60);
        CaffeineCache productCache = buildCache("ProductsCache", 12 * 60);
        CaffeineCache countryCache = buildCache("CountryCache", 12 * 60);
        CaffeineCache oldDisplayData = buildCache("OldDisplayData", 1 * 60);
        CaffeineCache leaderDashBoard = buildCache("LeaderDashBoard", 1 * 60);
        CaffeineCache creditNote = buildCache("CreditNote", 24 * 60);
        CaffeineCache rfqData = buildCache("RfqData", 1 * 60);
        CaffeineCache salesOrderData = buildCache("SalesOrderData", 1 * 60);
        CaffeineCache fullFillMentData = buildCache("FullFillMentData", 1 * 60);
        CaffeineCache fetchCustomer = buildCache("FetchCustomer", 1 * 60);
        CaffeineCache productFamilyCache = buildCache("ProductFamilyCache", 24 *60);
        CaffeineCache productFamilyCacheWhichHaveProducts = buildCache("ProductFamilyCacheWhichHaveProducts", 24 *60);
        CaffeineCache zohoCache = buildCache("ZohoCache",  55);
        CaffeineCache blogCache = buildCache("BlogCache", 24 *60);
        CaffeineCache productColorCache = buildCache("ColorCache",24*60);
        CaffeineCache homeBannerCache = buildCache("HomeBannerCache", 24 *60);
        CaffeineCache home1BannerCache = buildCache("Home1BannerCache", 24 *60);
        CaffeineCache categoryBannerCache = buildCache("CategoryBannerCache",24*60);
        CaffeineCache reorderCache = buildCache("ReorderCache", 60);
        CaffeineCache metaCache = buildCache("MetaCache", 24 *60);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(categoriesCache, productCache, countryCache, zohoCache, blogCache, productFamilyCache,productColorCache,
                homeBannerCache, home1BannerCache, categoryBannerCache, reorderCache, oldDisplayData,leaderDashBoard,creditNote,rfqData,salesOrderData,fullFillMentData,fetchCustomer,metaCache,productFamilyCacheWhichHaveProducts));

        return manager;
    }

    private CaffeineCache buildCache(String name, int minutesToExpire) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
                .build());
    }
}
