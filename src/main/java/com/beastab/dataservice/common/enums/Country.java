package com.beastab.dataservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Country {
    INDIA("India", "91", "INR", "₹"),
    BANGLADESH("Bangladesh","880","BDT", "৳");

    private String countryName;
    private String ISDCode;
    private String currencyCode;
    private String currencySymbol;

    public static final Map<String, Country> countryNameEnumMap = new HashMap<>();

    static {
        for (Country country : Country.values()) {
            countryNameEnumMap.put(country.getCountryName().toUpperCase(),
                    country);
        }
    }

    public static Country getCountryByName(String name) {
        return countryNameEnumMap.get(name.toUpperCase());
    }
}
