package com.beastab.dataservice.identityservice.db.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.beastab.dataservice.common.db.entity.AbstractEntity;
import com.beastab.dataservice.common.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(collection = "countries")
public class CountryEntity extends AbstractEntity {
    @Field(name = "country_id")
    private String countryId;

    @Field(name = "country")
    private Country country;

    @Field(name = "is_disabled")
    private boolean isDisabled;

    @Field(name = "flag_icon")
    private String flagIcon;
}
