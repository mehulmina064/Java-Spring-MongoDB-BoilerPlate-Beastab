package com.beastab.dataservice.identityservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountRfqOrdersDetailsResponse {
    @JsonProperty("rfq_details")
    private Map<String, Integer> rfqDetails;

    @JsonProperty("order_details")
    private Map<String, Integer> orderDetails;

    @JsonProperty("user_count")
    private int userCount;

    @JsonProperty("payment_due")
    private BigDecimal paymentDue;

    @JsonProperty("currency_symbol")
    private String currencySymbol;
}
