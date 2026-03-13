/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.spi;

import com.andrei1058.bedwars.libs.slf4j.event.LoggingEvent;

public interface LoggingEventAware {
    public void log(LoggingEvent var1);
}

