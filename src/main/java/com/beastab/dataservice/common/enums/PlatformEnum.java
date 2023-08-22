package com.beastab.dataservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PlatformEnum {
    CUSTOMER_APP("Customer App"),
    PARTNER_APP("Partner App"),
    WEB("Website"),
    PULSE("Pulse"),
    ZOHO("Zoho");

    private String name;

    public static final Map<String, PlatformEnum> platformNameEnumMap = new HashMap<>();

    static {
        for (PlatformEnum platformEnum : PlatformEnum.values()) {
            platformNameEnumMap.put(platformEnum.getName(),
                    platformEnum);
        }
    }

    public static PlatformEnum getPlatformByName(String name) {
            return platformNameEnumMap.get(name);

    }

}
