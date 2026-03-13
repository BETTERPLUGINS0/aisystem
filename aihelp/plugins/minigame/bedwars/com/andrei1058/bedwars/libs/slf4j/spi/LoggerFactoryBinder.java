/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.spi;

import com.andrei1058.bedwars.libs.slf4j.ILoggerFactory;

public interface LoggerFactoryBinder {
    public ILoggerFactory getLoggerFactory();

    public String getLoggerFactoryClassStr();
}

