/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.helpers;

import com.andrei1058.bedwars.libs.slf4j.ILoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.Logger;
import com.andrei1058.bedwars.libs.slf4j.helpers.NOPLogger;

public class NOPLoggerFactory
implements ILoggerFactory {
    @Override
    public Logger getLogger(String name) {
        return NOPLogger.NOP_LOGGER;
    }
}

