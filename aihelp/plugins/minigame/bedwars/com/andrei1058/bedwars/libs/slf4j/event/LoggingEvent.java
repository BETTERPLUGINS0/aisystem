/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.event;

import com.andrei1058.bedwars.libs.slf4j.Marker;
import com.andrei1058.bedwars.libs.slf4j.event.KeyValuePair;
import com.andrei1058.bedwars.libs.slf4j.event.Level;
import java.util.List;

public interface LoggingEvent {
    public Level getLevel();

    public String getLoggerName();

    public String getMessage();

    public List<Object> getArguments();

    public Object[] getArgumentArray();

    public List<Marker> getMarkers();

    public List<KeyValuePair> getKeyValuePairs();

    public Throwable getThrowable();

    public long getTimeStamp();

    public String getThreadName();

    default public String getCallerBoundary() {
        return null;
    }
}

