/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.spi;

import com.andrei1058.bedwars.libs.slf4j.ILoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.IMarkerFactory;
import com.andrei1058.bedwars.libs.slf4j.spi.MDCAdapter;

public interface SLF4JServiceProvider {
    public ILoggerFactory getLoggerFactory();

    public IMarkerFactory getMarkerFactory();

    public MDCAdapter getMDCAdapter();

    public String getRequestedApiVersion();

    public void initialize();
}

