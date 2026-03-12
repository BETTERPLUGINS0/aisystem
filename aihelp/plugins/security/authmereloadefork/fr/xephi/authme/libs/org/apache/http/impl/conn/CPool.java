package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.ManagedHttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.pool.AbstractConnPool;
import fr.xephi.authme.libs.org.apache.http.pool.ConnFactory;
import fr.xephi.authme.libs.org.apache.http.pool.PoolEntryCallback;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Contract(
   threading = ThreadingBehavior.SAFE
)
class CPool extends AbstractConnPool<HttpRoute, ManagedHttpClientConnection, CPoolEntry> {
   private static final AtomicLong COUNTER = new AtomicLong();
   private final Log log = LogFactory.getLog(CPool.class);
   private final long timeToLive;
   private final TimeUnit timeUnit;

   public CPool(ConnFactory<HttpRoute, ManagedHttpClientConnection> connFactory, int defaultMaxPerRoute, int maxTotal, long timeToLive, TimeUnit timeUnit) {
      super(connFactory, defaultMaxPerRoute, maxTotal);
      this.timeToLive = timeToLive;
      this.timeUnit = timeUnit;
   }

   protected CPoolEntry createEntry(HttpRoute route, ManagedHttpClientConnection conn) {
      String id = Long.toString(COUNTER.getAndIncrement());
      return new CPoolEntry(this.log, id, route, conn, this.timeToLive, this.timeUnit);
   }

   protected boolean validate(CPoolEntry entry) {
      return !((ManagedHttpClientConnection)entry.getConnection()).isStale();
   }

   protected void enumAvailable(PoolEntryCallback<HttpRoute, ManagedHttpClientConnection> callback) {
      super.enumAvailable(callback);
   }

   protected void enumLeased(PoolEntryCallback<HttpRoute, ManagedHttpClientConnection> callback) {
      super.enumLeased(callback);
   }
}
