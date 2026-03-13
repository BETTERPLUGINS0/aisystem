/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.simple;

import com.andrei1058.bedwars.libs.slf4j.ILoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.IMarkerFactory;
import com.andrei1058.bedwars.libs.slf4j.helpers.BasicMarkerFactory;
import com.andrei1058.bedwars.libs.slf4j.helpers.NOPMDCAdapter;
import com.andrei1058.bedwars.libs.slf4j.simple.SimpleLoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.spi.MDCAdapter;
import com.andrei1058.bedwars.libs.slf4j.spi.SLF4JServiceProvider;

public class SimpleServiceProvider
implements SLF4JServiceProvider {
    public static String REQUESTED_API_VERSION = "2.0.99";
    private ILoggerFactory loggerFactory;
    private IMarkerFactory markerFactory;
    private MDCAdapter mdcAdapter;

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
        this.loggerFactory = new SimpleLoggerFactory();
        this.markerFactory = new BasicMarkerFactory();
        this.mdcAdapter = new NOPMDCAdapter();
    }
}

