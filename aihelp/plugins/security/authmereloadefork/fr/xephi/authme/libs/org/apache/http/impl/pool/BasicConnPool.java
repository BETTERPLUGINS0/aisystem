package fr.xephi.authme.libs.org.apache.http.impl.pool;

import fr.xephi.authme.libs.org.apache.http.HttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.config.ConnectionConfig;
import fr.xephi.authme.libs.org.apache.http.config.SocketConfig;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.pool.AbstractConnPool;
import fr.xephi.authme.libs.org.apache.http.pool.ConnFactory;
import java.util.concurrent.atomic.AtomicLong;

@Contract(
   threading = ThreadingBehavior.SAFE_CONDITIONAL
)
public class BasicConnPool extends AbstractConnPool<HttpHost, HttpClientConnection, BasicPoolEntry> {
   private static final AtomicLong COUNTER = new AtomicLong();

   public BasicConnPool(ConnFactory<HttpHost, HttpClientConnection> connFactory) {
      super(connFactory, 2, 20);
   }

   /** @deprecated */
   @Deprecated
   public BasicConnPool(HttpParams params) {
      super(new BasicConnFactory(params), 2, 20);
   }

   public BasicConnPool(SocketConfig sconfig, ConnectionConfig cconfig) {
      super(new BasicConnFactory(sconfig, cconfig), 2, 20);
   }

   public BasicConnPool() {
      super(new BasicConnFactory(SocketConfig.DEFAULT, ConnectionConfig.DEFAULT), 2, 20);
   }

   protected BasicPoolEntry createEntry(HttpHost host, HttpClientConnection conn) {
      return new BasicPoolEntry(Long.toString(COUNTER.getAndIncrement()), host, conn);
   }

   protected boolean validate(BasicPoolEntry entry) {
      return !((HttpClientConnection)entry.getConnection()).isStale();
   }
}
