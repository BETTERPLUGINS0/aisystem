/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.hikari.metrics;

import com.andrei1058.bedwars.libs.hikari.metrics.IMetricsTracker;
import com.andrei1058.bedwars.libs.hikari.metrics.PoolStats;

public interface MetricsTrackerFactory {
    public IMetricsTracker create(String var1, PoolStats var2);
}

