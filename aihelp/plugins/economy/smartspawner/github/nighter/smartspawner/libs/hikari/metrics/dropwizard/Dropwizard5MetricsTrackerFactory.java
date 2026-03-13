package github.nighter.smartspawner.libs.hikari.metrics.dropwizard;

import github.nighter.smartspawner.libs.hikari.metrics.IMetricsTracker;
import github.nighter.smartspawner.libs.hikari.metrics.MetricsTrackerFactory;
import github.nighter.smartspawner.libs.hikari.metrics.PoolStats;
import io.dropwizard.metrics5.MetricRegistry;

public class Dropwizard5MetricsTrackerFactory implements MetricsTrackerFactory {
   private final MetricRegistry registry;

   public Dropwizard5MetricsTrackerFactory(MetricRegistry registry) {
      this.registry = registry;
   }

   public MetricRegistry getRegistry() {
      return this.registry;
   }

   public IMetricsTracker create(String poolName, PoolStats poolStats) {
      return new Dropwizard5MetricsTracker(poolName, poolStats, this.registry);
   }
}
