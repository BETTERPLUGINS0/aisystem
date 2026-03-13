package github.nighter.smartspawner.libs.mariadb.pool;

public interface PoolMBean {
   long getActiveConnections();

   long getTotalConnections();

   long getIdleConnections();

   long getConnectionRequests();
}
