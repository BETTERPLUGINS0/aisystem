/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.helpers;

import com.andrei1058.bedwars.libs.slf4j.ILoggerFactory;
import com.andrei1058.bedwars.libs.slf4j.Logger;
import com.andrei1058.bedwars.libs.slf4j.event.SubstituteLoggingEvent;
import com.andrei1058.bedwars.libs.slf4j.helpers.SubstituteLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SubstituteLoggerFactory
implements ILoggerFactory {
    volatile boolean postInitialization = false;
    final Map<String, SubstituteLogger> loggers = new ConcurrentHashMap<String, SubstituteLogger>();
    final LinkedBlockingQueue<SubstituteLoggingEvent> eventQueue = new LinkedBlockingQueue();

    @Override
    public synchronized Logger getLogger(String name) {
        SubstituteLogger logger = this.loggers.get(name);
        if (logger == null) {
            logger = new SubstituteLogger(name, this.eventQueue, this.postInitialization);
            this.loggers.put(name, logger);
        }
        return logger;
    }

    public List<String> getLoggerNames() {
        return new ArrayList<String>(this.loggers.keySet());
    }

    public List<SubstituteLogger> getLoggers() {
        return new ArrayList<SubstituteLogger>(this.loggers.values());
    }

    public LinkedBlockingQueue<SubstituteLoggingEvent> getEventQueue() {
        return this.eventQueue;
    }

    public void postInitialization() {
        this.postInitialization = true;
    }

    public void clear() {
        this.loggers.clear();
        this.eventQueue.clear();
    }
}

