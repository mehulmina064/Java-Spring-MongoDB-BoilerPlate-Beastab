package com.beastab.dataservice.identityservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.beastab.dataservice.common.enums.Country;
import com.beastab.dataservice.identityservice.AddressType;
import com.beastab.dataservice.identityservice.DTO.MobileNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAddressRequest {

    @JsonProperty("address_id")
    private String addressId;
    @JsonProperty("full_name")
    private String fullName;
    private List<MobileNumber> mobileNumber;
    @JsonProperty("pincode")
    private String pincode;
    @JsonProperty("city")
    private String city;
    @JsonProperty("state")
    private String state;
    @JsonProperty("country")
    private Country country;
    @JsonProperty("address_line_1")
    private String addressLine1;
    @JsonProperty("address_line_2")
    private String addressLine2;
    @JsonProperty("address_line_3")
    private String addressLine3;
    @JsonProperty("gst_number")
    private String GSTNumber;
    @JsonProperty("address_type")
    private AddressType addressType;
}
