package com.z5n.autoexcel.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class UuidUtils {
    public static String randomUUIDWithoutDash() {
        return StringUtils.remove(UUID.randomUUID().toString(), '-');
    }

}
