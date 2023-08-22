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
@Document(collection = "companies")
public class CompanyEntity extends AbstractEntity {

    @Field(name = "company_id")
    private String companyId;

    @Field(name = "company_name")
    private String name;

    @Field(name = "zoho_id")
    private String zohoId;

    @Field(name = "zoho_account_details")
    private Object zohoAccount;


    public CompanyEntity(String companyName) {
        this.companyId = UUID.randomUUID().toString();
        this.name = companyName;
        this.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        this.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

    }
}
