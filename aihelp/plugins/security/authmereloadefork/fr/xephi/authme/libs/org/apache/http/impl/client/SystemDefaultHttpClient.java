package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.ConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.ClientConnectionManager;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoutePlanner;
import fr.xephi.authme.libs.org.apache.http.impl.DefaultConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.NoConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.conn.PoolingClientConnectionManager;
import fr.xephi.authme.libs.org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import fr.xephi.authme.libs.org.apache.http.impl.conn.SchemeRegistryFactory;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import java.net.ProxySelector;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.SAFE_CONDITIONAL
)
public class SystemDefaultHttpClient extends DefaultHttpClient {
   public SystemDefaultHttpClient(HttpParams params) {
      super((ClientConnectionManager)null, params);
   }

   public SystemDefaultHttpClient() {
      super((ClientConnectionManager)null, (HttpParams)null);
   }

   protected ClientConnectionManager createClientConnectionManager() {
      PoolingClientConnectionManager connmgr = new PoolingClientConnectionManager(SchemeRegistryFactory.createSystemDefault());
      String s = System.getProperty("http.keepAlive", "true");
      if ("true".equalsIgnoreCase(s)) {
         s = System.getProperty("http.maxConnections", "5");
         int max = Integer.parseInt(s);
         connmgr.setDefaultMaxPerRoute(max);
         connmgr.setMaxTotal(2 * max);
      }

      return connmgr;
   }

   protected HttpRoutePlanner createHttpRoutePlanner() {
      return new ProxySelectorRoutePlanner(this.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
   }

   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
      String s = System.getProperty("http.keepAlive", "true");
      return (ConnectionReuseStrategy)("true".equalsIgnoreCase(s) ? new DefaultConnectionReuseStrategy() : new NoConnectionReuseStrategy());
   }
}
