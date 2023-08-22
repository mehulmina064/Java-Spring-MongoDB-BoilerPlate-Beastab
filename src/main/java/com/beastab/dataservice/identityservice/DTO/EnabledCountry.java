package com.beastab.dataservice.identityservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.beastab.dataservice.identityservice.db.entity.CountryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnabledCountry {
    @JsonProperty("country_id")
    private String countryId;
    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("isd_code")
    private String isdCode;
    @JsonProperty("flag_icon")
    private String flagIcon;
    private String currency;
    @JsonProperty("currency_symbol")
    private String currencySymbol;

    public EnabledCountry(CountryEntity entity){
        this.countryId = entity.getCountryId();
        this.countryName = entity.getCountry().getCountryName();
        this.isdCode = "+" + entity.getCountry().getISDCode();
        this.flagIcon = entity.getFlagIcon();
        this.currency = entity.getCountry().getCurrencyCode();
        this.currencySymbol = entity.getCountry().getCurrencySymbol();
    }
}
