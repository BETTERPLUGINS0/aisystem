package github.nighter.smartspawner.libs.hikari.metrics.micrometer;

import github.nighter.smartspawner.libs.hikari.metrics.IMetricsTracker;
import github.nighter.smartspawner.libs.hikari.metrics.MetricsTrackerFactory;
import github.nighter.smartspawner.libs.hikari.metrics.PoolStats;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerMetricsTrackerFactory implements MetricsTrackerFactory {
   private final MeterRegistry registry;

   public MicrometerMetricsTrackerFactory(MeterRegistry registry) {
      this.registry = registry;
   }

   public IMetricsTracker create(String poolName, PoolStats poolStats) {
      return new MicrometerMetricsTracker(poolName, poolStats, this.registry);
   }
}
