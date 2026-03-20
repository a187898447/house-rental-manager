package com.rental.common.utils;

import java.util.UUID;

/**
 * ID生成工具
 */
public class IdGenerator {

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateInviteCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
