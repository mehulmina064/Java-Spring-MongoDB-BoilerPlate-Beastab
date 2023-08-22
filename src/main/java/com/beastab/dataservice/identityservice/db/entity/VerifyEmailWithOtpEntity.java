package com.beastab.dataservice.identityservice.db.entity;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(collection = "email_verification")
public class VerifyEmailWithOtpEntity {

    private String email;

    private String otp;

    private LocalDateTime createdAt;

    @Builder.Default
    private boolean emailVerification = false;

    @Builder.Default
    private boolean isExpired = false;
}
