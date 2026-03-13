package github.nighter.smartspawner.libs.hikari.metrics.prometheus;

import github.nighter.smartspawner.libs.hikari.metrics.IMetricsTracker;
import github.nighter.smartspawner.libs.hikari.metrics.MetricsTrackerFactory;
import github.nighter.smartspawner.libs.hikari.metrics.PoolStats;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrometheusMetricsTrackerFactory implements MetricsTrackerFactory {
   private static final Map<CollectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus> registrationStatuses = new ConcurrentHashMap();
   private final HikariCPCollector collector;
   private final CollectorRegistry collectorRegistry;

   public PrometheusMetricsTrackerFactory() {
      this(CollectorRegistry.defaultRegistry);
   }

   public PrometheusMetricsTrackerFactory(CollectorRegistry collectorRegistry) {
      this.collector = new HikariCPCollector();
      this.collectorRegistry = collectorRegistry;
   }

   public IMetricsTracker create(String poolName, PoolStats poolStats) {
      this.registerCollector(this.collector, this.collectorRegistry);
      this.collector.add(poolName, poolStats);
      return new PrometheusMetricsTracker(poolName, this.collectorRegistry, this.collector);
   }

   private void registerCollector(Collector collector, CollectorRegistry collectorRegistry) {
      if (registrationStatuses.putIfAbsent(collectorRegistry, PrometheusMetricsTrackerFactory.RegistrationStatus.REGISTERED) == null) {
         collector.register(collectorRegistry);
      }

   }

   static enum RegistrationStatus {
      REGISTERED;
   }
}
