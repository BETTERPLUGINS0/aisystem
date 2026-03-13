/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.slf4j.event;

import com.andrei1058.bedwars.libs.slf4j.Marker;
import com.andrei1058.bedwars.libs.slf4j.event.Level;
import com.andrei1058.bedwars.libs.slf4j.event.SubstituteLoggingEvent;
import com.andrei1058.bedwars.libs.slf4j.helpers.LegacyAbstractLogger;
import com.andrei1058.bedwars.libs.slf4j.helpers.SubstituteLogger;
import java.util.Queue;

public class EventRecordingLogger
extends LegacyAbstractLogger {
    private static final long serialVersionUID = -176083308134819629L;
    String name;
    SubstituteLogger logger;
    Queue<SubstituteLoggingEvent> eventQueue;
    static final boolean RECORD_ALL_EVENTS = true;

    public EventRecordingLogger(SubstituteLogger logger, Queue<SubstituteLoggingEvent> eventQueue) {
        this.logger = logger;
        this.name = logger.getName();
        this.eventQueue = eventQueue;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String msg, Object[] args, Throwable throwable) {
        SubstituteLoggingEvent loggingEvent = new SubstituteLoggingEvent();
        loggingEvent.setTimeStamp(System.currentTimeMillis());
        loggingEvent.setLevel(level);
        loggingEvent.setLogger(this.logger);
        loggingEvent.setLoggerName(this.name);
        if (marker != null) {
            loggingEvent.addMarker(marker);
        }
        loggingEvent.setMessage(msg);
        loggingEvent.setThreadName(Thread.currentThread().getName());
        loggingEvent.setArgumentArray(args);
        loggingEvent.setThrowable(throwable);
        this.eventQueue.add(loggingEvent);
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return null;
    }
}

