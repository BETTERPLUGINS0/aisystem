/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.helpers;

import com.andrei1058.bedwars.libs.slf4j.Marker;
import com.andrei1058.bedwars.libs.slf4j.helpers.AbstractLogger;

public abstract class LegacyAbstractLogger
extends AbstractLogger {
    private static final long serialVersionUID = -7041884104854048950L;

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.isErrorEnabled();
    }
}

