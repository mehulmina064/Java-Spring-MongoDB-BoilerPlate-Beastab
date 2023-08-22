package com.beastab.dataservice.identityservice.db.entity;

import com.beastab.dataservice.common.db.entity.AbstractEntity;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

public class ResetPasswordTokenEntity extends AbstractEntity {
    @Field(name = "token_id")
    private String tokenId;

    @Field(name = "token")
    private String token;

    @Field(name = "client_id")
    private String clientId;

    @Field(name = "expiration_time")
    private LocalDateTime expirationTime;
}
