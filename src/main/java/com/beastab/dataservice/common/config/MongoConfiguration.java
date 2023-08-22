package com.beastab.dataservice.common.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(MongoConfigProperties.class)
@Slf4j
public class MongoConfiguration {
    @Autowired
    private MongoConfigProperties mongoConfigProperties;

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() throws Exception {
        log.info("Initializing Mongo Template -------");
        MongoTemplate mongoTemplate = new MongoTemplate(mongo(), mongoConfigProperties.getDatabase());
        return mongoTemplate;
    }

    @Bean
    public MongoClient mongo() {
        if (StringUtils.isBlank(mongoConfigProperties.getUri())) {
            log.error("Mongodb uri configuration missing in application properties");
            System.exit(1);
        }
        final ConnectionString connectionString = new ConnectionString(mongoConfigProperties.getUri());

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder.maxConnectionIdleTime(mongoConfigProperties.getConnectionMaxIdleTimeMs(), TimeUnit.MILLISECONDS)
                        .maxConnectionLifeTime(mongoConfigProperties.getConnectionMaxLifeTimeMs(), TimeUnit.MILLISECONDS)
                        .maxSize(mongoConfigProperties.getPoolMaxSize())
                        .minSize(mongoConfigProperties.getPoolMinSize())
                        .build())
                .build();

        return MongoClients.create(mongoClientSettings);
    }

}
