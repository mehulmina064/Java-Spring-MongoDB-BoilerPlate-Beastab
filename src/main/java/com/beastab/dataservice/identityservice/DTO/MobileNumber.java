package com.beastab.dataservice.identityservice.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MobileNumber {
    @Field(name = "isd_code")
    private String ISDCode;

    @Field(name = "mobile")
    private String mobile;

    @Field(name = "is_primary")
    private Boolean isPrimary;


}
