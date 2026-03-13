package github.nighter.smartspawner.libs.hikari.metrics.dropwizard;

import github.nighter.smartspawner.libs.hikari.metrics.IMetricsTracker;
import github.nighter.smartspawner.libs.hikari.metrics.PoolStats;
import io.dropwizard.metrics5.Histogram;
import io.dropwizard.metrics5.Meter;
import io.dropwizard.metrics5.MetricName;
import io.dropwizard.metrics5.MetricRegistry;
import io.dropwizard.metrics5.Timer;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Dropwizard5MetricsTracker implements IMetricsTracker {
   private final String poolName;
   private final Timer connectionObtainTimer;
   private final Histogram connectionUsage;
   private final Histogram connectionCreation;
   private final Meter connectionTimeoutMeter;
   private final MetricRegistry registry;

   Dropwizard5MetricsTracker(String poolName, PoolStats poolStats, MetricRegistry registry) {
      this.poolName = poolName;
      this.registry = registry;
      this.connectionObtainTimer = registry.timer(MetricRegistry.name(poolName, new String[]{"pool", "Wait"}));
      this.connectionUsage = registry.histogram(MetricRegistry.name(poolName, new String[]{"pool", "Usage"}));
      this.connectionCreation = registry.histogram(MetricRegistry.name(poolName, new String[]{"pool", "ConnectionCreation"}));
      this.connectionTimeoutMeter = registry.meter(MetricRegistry.name(poolName, new String[]{"pool", "ConnectionTimeoutRate"}));
      MetricName var10001 = MetricRegistry.name(poolName, new String[]{"pool", "TotalConnections"});
      Objects.requireNonNull(poolStats);
      registry.register(var10001, poolStats::getTotalConnections);
      var10001 = MetricRegistry.name(poolName, new String[]{"pool", "IdleConnections"});
      Objects.requireNonNull(poolStats);
      registry.register(var10001, poolStats::getIdleConnections);
      var10001 = MetricRegistry.name(poolName, new String[]{"pool", "ActiveConnections"});
      Objects.requireNonNull(poolStats);
      registry.register(var10001, poolStats::getActiveConnections);
      var10001 = MetricRegistry.name(poolName, new String[]{"pool", "PendingConnections"});
      Objects.requireNonNull(poolStats);
      registry.register(var10001, poolStats::getPendingThreads);
      var10001 = MetricRegistry.name(poolName, new String[]{"pool", "MaxConnections"});
      Objects.requireNonNull(poolStats);
      registry.register(var10001, poolStats::getMaxConnections);
      var10001 = MetricRegistry.name(poolName, new String[]{"pool", "MinConnections"});
      Objects.requireNonNull(poolStats);
      registry.register(var10001, poolStats::getMinConnections);
   }

   public void close() {
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "Wait"}));
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "Usage"}));
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "ConnectionCreation"}));
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "ConnectionTimeoutRate"}));
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "TotalConnections"}));
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "IdleConnections"}));
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "ActiveConnections"}));
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "PendingConnections"}));
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "MaxConnections"}));
      this.registry.remove(MetricRegistry.name(this.poolName, new String[]{"pool", "MinConnections"}));
   }

   public void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {
      this.connectionObtainTimer.update(elapsedAcquiredNanos, TimeUnit.NANOSECONDS);
   }

   public void recordConnectionUsageMillis(long elapsedBorrowedMillis) {
      this.connectionUsage.update(elapsedBorrowedMillis);
   }

   public void recordConnectionTimeout() {
      this.connectionTimeoutMeter.mark();
   }

   public void recordConnectionCreatedMillis(long connectionCreatedMillis) {
      this.connectionCreation.update(connectionCreatedMillis);
   }

   public Timer getConnectionAcquisitionTimer() {
      return this.connectionObtainTimer;
   }

   public Histogram getConnectionDurationHistogram() {
      return this.connectionUsage;
   }

   public Histogram getConnectionCreationHistogram() {
      return this.connectionCreation;
   }
}
