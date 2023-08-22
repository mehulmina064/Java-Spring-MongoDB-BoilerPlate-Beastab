package com.beastab.dataservice.identityservice.db.entity;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(collection = "Mailer_Group")
public class EmailerEntity {


    @Field(name = "mail_group_id")
    private String mailGroupId;

    @Field(name = "client_id")
    private String clientId;

    @Field(name = "company_id")
    private String companyId;

    @Field(name = "mail_group_name")
    private String mailGroupName;

    @Field(name = "toList")
    private List<String> toList;

    @Field(name = "ccList")
    private List<String> ccList;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;

    @Field(name = "created_by")
    private String CreatedBy;

    @Field(name = "updated_by")
    private String UpdatedBy;

    @Field(name = "is_deleted")
    private boolean isDeleted;
}
