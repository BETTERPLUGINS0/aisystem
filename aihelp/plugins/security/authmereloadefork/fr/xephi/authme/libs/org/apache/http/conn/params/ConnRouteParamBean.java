package fr.xephi.authme.libs.org.apache.http.conn.params;

import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.params.HttpAbstractParamBean;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import java.net.InetAddress;

/** @deprecated */
@Deprecated
public class ConnRouteParamBean extends HttpAbstractParamBean {
   public ConnRouteParamBean(HttpParams params) {
      super(params);
   }

   public void setDefaultProxy(HttpHost defaultProxy) {
      this.params.setParameter("http.route.default-proxy", defaultProxy);
   }

   public void setLocalAddress(InetAddress address) {
      this.params.setParameter("http.route.local-address", address);
   }

   public void setForcedRoute(HttpRoute route) {
      this.params.setParameter("http.route.forced-route", route);
   }
}
