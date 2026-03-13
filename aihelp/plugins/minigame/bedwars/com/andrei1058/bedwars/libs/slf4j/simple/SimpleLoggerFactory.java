/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.simple;

import com.andrei1058.bedwars.libs.slf4j.ILoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.Logger;
import com.andrei1058.bedwars.libs.slf4j.simple.SimpleLogger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleLoggerFactory
implements ILoggerFactory {
    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    public SimpleLoggerFactory() {
        SimpleLogger.lazyInit();
    }

    @Override
    public Logger getLogger(String name) {
        Logger simpleLogger = (Logger)this.loggerMap.get(name);
        if (simpleLogger != null) {
            return simpleLogger;
        }
        SimpleLogger newInstance = new SimpleLogger(name);
        Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
        return oldInstance == null ? newInstance : oldInstance;
    }

    void reset() {
        this.loggerMap.clear();
    }
}

