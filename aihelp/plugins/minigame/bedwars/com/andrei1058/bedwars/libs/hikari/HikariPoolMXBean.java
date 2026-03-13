/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.hikari;

public interface HikariPoolMXBean {
    public int getIdleConnections();

    public int getActiveConnections();

    public int getTotalConnections();

    public int getThreadsAwaitingConnection();

    public void softEvictConnections();

    public void suspendPool();

    public void resumePool();
}

