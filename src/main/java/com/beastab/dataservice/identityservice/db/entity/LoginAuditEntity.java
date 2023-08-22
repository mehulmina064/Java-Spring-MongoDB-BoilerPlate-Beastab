package com.beastab.dataservice.identityservice.db.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.beastab.dataservice.common.db.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(collection = "login_audit")
public class LoginAuditEntity extends AbstractEntity {
    @Field(name = "login_audit_id")
    private String loginAuditId;
    @Field(name = "source")
    private String source;
    @Field(name = "login_type")
    private String loginType;
    private String email;
    private String password;
    private String mobile;
    @Field(name = "client_id")
    private String clientId;
    private String reason;
    private boolean isSuccessful;

    public LoginAuditEntity(String source, String loginType, String email,
                            String password, String mobile, String clientId, String reason,
                            boolean isSuccessful){
        this.loginAuditId = UUID.randomUUID().toString();
        this.source = source;
        this.loginType = loginType;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.clientId = clientId;
        this.reason = reason;
        this.isSuccessful = isSuccessful;
        this.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        this.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
    }
}
