package fr.xephi.authme.libs.com.zaxxer.hikari.metrics.micrometer;

import fr.xephi.authme.libs.com.zaxxer.hikari.metrics.IMetricsTracker;
import fr.xephi.authme.libs.com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import fr.xephi.authme.libs.com.zaxxer.hikari.metrics.PoolStats;
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
