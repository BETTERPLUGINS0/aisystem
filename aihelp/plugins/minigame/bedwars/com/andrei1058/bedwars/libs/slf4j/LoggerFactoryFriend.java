/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j;

import com.andrei1058.bedwars.libs.slf4j.LoggerFactory;

public class LoggerFactoryFriend {
    public static void reset() {
        LoggerFactory.reset();
    }

    public static void setDetectLoggerNameMismatch(boolean enabled) {
        LoggerFactory.DETECT_LOGGER_NAME_MISMATCH = enabled;
    }
}

