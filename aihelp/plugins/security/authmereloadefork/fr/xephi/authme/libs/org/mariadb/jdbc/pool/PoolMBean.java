package fr.xephi.authme.libs.org.mariadb.jdbc.pool;

public interface PoolMBean {
   long getActiveConnections();

   long getTotalConnections();

   long getIdleConnections();

   long getConnectionRequests();
}
