package fr.xephi.authme.libs.org.apache.http.impl.pool;

import fr.xephi.authme.libs.org.apache.http.HttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.pool.PoolEntry;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.SAFE_CONDITIONAL
)
public class BasicPoolEntry extends PoolEntry<HttpHost, HttpClientConnection> {
   public BasicPoolEntry(String id, HttpHost route, HttpClientConnection conn) {
      super(id, route, conn);
   }

   public void close() {
      try {
         HttpClientConnection connection = (HttpClientConnection)this.getConnection();

         try {
            int socketTimeout = connection.getSocketTimeout();
            if (socketTimeout <= 0 || socketTimeout > 1000) {
               connection.setSocketTimeout(1000);
            }

            connection.close();
         } catch (IOException var3) {
            connection.shutdown();
         }
      } catch (IOException var4) {
      }

   }

   public boolean isClosed() {
      return !((HttpClientConnection)this.getConnection()).isOpen();
   }
}
