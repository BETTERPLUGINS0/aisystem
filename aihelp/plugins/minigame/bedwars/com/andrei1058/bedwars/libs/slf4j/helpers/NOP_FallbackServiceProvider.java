/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.helpers;

import com.andrei1058.bedwars.libs.slf4j.ILoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.IMarkerFactory;
import com.andrei1058.bedwars.libs.slf4j.helpers.BasicMarkerFactory;
import com.andrei1058.bedwars.libs.slf4j.helpers.NOPLoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.helpers.NOPMDCAdapter;
import com.andrei1058.bedwars.libs.slf4j.spi.MDCAdapter;
import com.andrei1058.bedwars.libs.slf4j.spi.SLF4JServiceProvider;

public class NOP_FallbackServiceProvider
implements SLF4JServiceProvider {
    public static String REQUESTED_API_VERSION = "2.0.99";
    private final ILoggerFactory loggerFactory = new NOPLoggerFactory();
    private final IMarkerFactory markerFactory = new BasicMarkerFactory();
    private final MDCAdapter mdcAdapter = new NOPMDCAdapter();

    @Override
    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return this.markerFactory;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return this.mdcAdapter;
    }

    @Override
    public String getRequestedApiVersion() {
        return REQUESTED_API_VERSION;
    }

    @Override
    public void initialize() {
    }
}

