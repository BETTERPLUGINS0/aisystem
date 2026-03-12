package fr.xephi.authme.libs.org.apache.http.impl.conn.tsccm;

import fr.xephi.authme.libs.org.apache.http.conn.ConnectionPoolTimeoutException;
import java.util.concurrent.TimeUnit;

/** @deprecated */
@Deprecated
public interface PoolEntryRequest {
   BasicPoolEntry getPoolEntry(long var1, TimeUnit var3) throws InterruptedException, ConnectionPoolTimeoutException;

   void abortRequest();
}
