package com.beastab.dataservice.identityservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressType {
    HOME("Home"),
    OFFICE("Office"),
    WAREHOUSE("Warehouse");

    private String displayName;
}
