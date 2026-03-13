/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.micrometer.core.instrument.MeterRegistry
 */
package com.andrei1058.bedwars.libs.hikari.metrics.micrometer;

import com.andrei1058.bedwars.libs.hikari.metrics.IMetricsTracker;
import com.andrei1058.bedwars.libs.hikari.metrics.MetricsTrackerFactory;
import com.andrei1058.bedwars.libs.hikari.metrics.PoolStats;
import com.andrei1058.bedwars.libs.hikari.metrics.micrometer.MicrometerMetricsTracker;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerMetricsTrackerFactory
implements MetricsTrackerFactory {
    private final MeterRegistry registry;

    public MicrometerMetricsTrackerFactory(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        return new MicrometerMetricsTracker(poolName, poolStats, this.registry);
    }
}

