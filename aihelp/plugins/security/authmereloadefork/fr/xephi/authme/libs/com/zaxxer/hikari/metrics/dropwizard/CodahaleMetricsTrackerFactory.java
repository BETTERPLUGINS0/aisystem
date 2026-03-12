package fr.xephi.authme.libs.com.zaxxer.hikari.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import fr.xephi.authme.libs.com.zaxxer.hikari.metrics.IMetricsTracker;
import fr.xephi.authme.libs.com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import fr.xephi.authme.libs.com.zaxxer.hikari.metrics.PoolStats;

public final class CodahaleMetricsTrackerFactory implements MetricsTrackerFactory {
   private final MetricRegistry registry;

   public CodahaleMetricsTrackerFactory(MetricRegistry registry) {
      this.registry = registry;
   }

   public MetricRegistry getRegistry() {
      return this.registry;
   }

   public IMetricsTracker create(String poolName, PoolStats poolStats) {
      return new CodaHaleMetricsTracker(poolName, poolStats, this.registry);
   }
}
