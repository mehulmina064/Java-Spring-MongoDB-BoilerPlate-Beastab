package com.beastab.dataservice;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.beastab.*"}, exclude = {DataSourceAutoConfiguration.class})
@EnableCaching
@EnableAsync
public class DataService {
    @Value("${environment.type}")
    private String environmentType;

    public static void main(String[] args) {
        SpringApplication.run(DataService.class, args);
    }


    private boolean isProdProfileActive() {
        return StringUtils.equalsIgnoreCase(environmentType, "PROD");
    }
}
