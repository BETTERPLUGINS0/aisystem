package fr.xephi.authme.libs.org.apache.http.conn.params;

import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.net.InetAddress;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class ConnRouteParams implements ConnRoutePNames {
   public static final HttpHost NO_HOST = new HttpHost("127.0.0.255", 0, "no-host");
   public static final HttpRoute NO_ROUTE;

   private ConnRouteParams() {
   }

   public static HttpHost getDefaultProxy(HttpParams params) {
      Args.notNull(params, "Parameters");
      HttpHost proxy = (HttpHost)params.getParameter("http.route.default-proxy");
      if (proxy != null && NO_HOST.equals(proxy)) {
         proxy = null;
      }

      return proxy;
   }

   public static void setDefaultProxy(HttpParams params, HttpHost proxy) {
      Args.notNull(params, "Parameters");
      params.setParameter("http.route.default-proxy", proxy);
   }

   public static HttpRoute getForcedRoute(HttpParams params) {
      Args.notNull(params, "Parameters");
      HttpRoute route = (HttpRoute)params.getParameter("http.route.forced-route");
      if (route != null && NO_ROUTE.equals(route)) {
         route = null;
      }

      return route;
   }

   public static void setForcedRoute(HttpParams params, HttpRoute route) {
      Args.notNull(params, "Parameters");
      params.setParameter("http.route.forced-route", route);
   }

   public static InetAddress getLocalAddress(HttpParams params) {
      Args.notNull(params, "Parameters");
      InetAddress local = (InetAddress)params.getParameter("http.route.local-address");
      return local;
   }

   public static void setLocalAddress(HttpParams params, InetAddress local) {
      Args.notNull(params, "Parameters");
      params.setParameter("http.route.local-address", local);
   }

   static {
      NO_ROUTE = new HttpRoute(NO_HOST);
   }
}
