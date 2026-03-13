/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.helpers;

import com.andrei1058.bedwars.libs.slf4j.ILoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.IMarkerFactory;
import com.andrei1058.bedwars.libs.slf4j.helpers.BasicMDCAdapter;
import com.andrei1058.bedwars.libs.slf4j.helpers.BasicMarkerFactory;
import com.andrei1058.bedwars.libs.slf4j.helpers.SubstituteLoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.spi.MDCAdapter;
import com.andrei1058.bedwars.libs.slf4j.spi.SLF4JServiceProvider;

public class SubstituteServiceProvider
implements SLF4JServiceProvider {
    private final SubstituteLoggerFactory loggerFactory = new SubstituteLoggerFactory();
    private final IMarkerFactory markerFactory = new BasicMarkerFactory();
    private final MDCAdapter mdcAdapter = new BasicMDCAdapter();

    @Override
    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    public SubstituteLoggerFactory getSubstituteLoggerFactory() {
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void initialize() {
    }
}

