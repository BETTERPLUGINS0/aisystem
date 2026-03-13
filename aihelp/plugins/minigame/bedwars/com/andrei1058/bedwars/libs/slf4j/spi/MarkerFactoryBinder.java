/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.spi;

import com.andrei1058.bedwars.libs.slf4j.IMarkerFactory;

public interface MarkerFactoryBinder {
    public IMarkerFactory getMarkerFactory();

    public String getMarkerFactoryClassStr();
}

