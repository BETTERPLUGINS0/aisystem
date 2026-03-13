/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j;

import com.andrei1058.bedwars.libs.slf4j.IMarkerFactory;
import com.andrei1058.bedwars.libs.slf4j.LoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.Marker;
import com.andrei1058.bedwars.libs.slf4j.helpers.BasicMarkerFactory;
import com.andrei1058.bedwars.libs.slf4j.helpers.Util;
import com.andrei1058.bedwars.libs.slf4j.spi.SLF4JServiceProvider;

public class MarkerFactory {
    static IMarkerFactory MARKER_FACTORY;

    private MarkerFactory() {
    }

    public static Marker getMarker(String name) {
        return MARKER_FACTORY.getMarker(name);
    }

    public static Marker getDetachedMarker(String name) {
        return MARKER_FACTORY.getDetachedMarker(name);
    }

    public static IMarkerFactory getIMarkerFactory() {
        return MARKER_FACTORY;
    }

    static {
        SLF4JServiceProvider provider = LoggerFactory.getProvider();
        if (provider != null) {
            MARKER_FACTORY = provider.getMarkerFactory();
        } else {
            Util.report("Failed to find provider");
            Util.report("Defaulting to BasicMarkerFactory.");
            MARKER_FACTORY = new BasicMarkerFactory();
        }
    }
}

