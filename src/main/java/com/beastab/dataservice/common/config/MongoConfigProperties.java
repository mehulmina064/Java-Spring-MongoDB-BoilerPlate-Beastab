package com.beastab.dataservice.common.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "default.mongo")
@NoArgsConstructor
@Data
public class MongoConfigProperties {

    private String uri;
    private String database;
    private Long connectionMaxIdleTimeMs = 10000L;
    private Long connectionMaxLifeTimeMs = 20000L;
    private Integer poolMaxSize = 30;
    private Integer poolMinSize = 10;
}
