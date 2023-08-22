package com.beastab.dataservice.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Formatter;

@Slf4j
public class EncryptionUtils {
    private static String encryptionKey = "jojo";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static String encryptPassword(String data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(encryptionKey.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            return toHexString(mac.doFinal(data.getBytes()));
        } catch (Exception e){
            log.error("EncryptionUtils.encryptPassword", e);
            return "";
        }
    }
}
