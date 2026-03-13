/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.MetricRegistry
 */
package com.andrei1058.bedwars.libs.hikari.metrics.dropwizard;

import com.andrei1058.bedwars.libs.hikari.metrics.IMetricsTracker;
import com.andrei1058.bedwars.libs.hikari.metrics.MetricsTrackerFactory;
import com.andrei1058.bedwars.libs.hikari.metrics.PoolStats;
import com.andrei1058.bedwars.libs.hikari.metrics.dropwizard.CodaHaleMetricsTracker;
import com.codahale.metrics.MetricRegistry;

public final class CodahaleMetricsTrackerFactory
implements MetricsTrackerFactory {
    private final MetricRegistry registry;

    public CodahaleMetricsTrackerFactory(MetricRegistry registry) {
        this.registry = registry;
    }

    public MetricRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        return new CodaHaleMetricsTracker(poolName, poolStats, this.registry);
    }
}

