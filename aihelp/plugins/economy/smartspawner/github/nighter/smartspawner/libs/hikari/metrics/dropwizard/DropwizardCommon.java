package github.nighter.smartspawner.libs.hikari.metrics.dropwizard;

final class DropwizardCommon {
   static final String METRIC_CATEGORY = "pool";
   static final String METRIC_NAME_WAIT = "Wait";
   static final String METRIC_NAME_USAGE = "Usage";
   static final String METRIC_NAME_CONNECT = "ConnectionCreation";
   static final String METRIC_NAME_TIMEOUT_RATE = "ConnectionTimeoutRate";
   static final String METRIC_NAME_TOTAL_CONNECTIONS = "TotalConnections";
   static final String METRIC_NAME_IDLE_CONNECTIONS = "IdleConnections";
   static final String METRIC_NAME_ACTIVE_CONNECTIONS = "ActiveConnections";
   static final String METRIC_NAME_PENDING_CONNECTIONS = "PendingConnections";
   static final String METRIC_NAME_MAX_CONNECTIONS = "MaxConnections";
   static final String METRIC_NAME_MIN_CONNECTIONS = "MinConnections";

   private DropwizardCommon() {
   }
}
