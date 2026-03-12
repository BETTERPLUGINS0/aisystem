package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.params.ConnRouteParams;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoutePlanner;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.Scheme;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.SchemeRegistry;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.Asserts;
import java.net.InetAddress;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.SAFE
)
public class DefaultHttpRoutePlanner implements HttpRoutePlanner {
   protected final SchemeRegistry schemeRegistry;

   public DefaultHttpRoutePlanner(SchemeRegistry schreg) {
      Args.notNull(schreg, "Scheme registry");
      this.schemeRegistry = schreg;
   }

   public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
      Args.notNull(request, "HTTP request");
      HttpRoute route = ConnRouteParams.getForcedRoute(request.getParams());
      if (route != null) {
         return route;
      } else {
         Asserts.notNull(target, "Target host");
         InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
         HttpHost proxy = ConnRouteParams.getDefaultProxy(request.getParams());

         Scheme schm;
         try {
            schm = this.schemeRegistry.getScheme(target.getSchemeName());
         } catch (IllegalStateException var9) {
            throw new HttpException(var9.getMessage());
         }

         boolean secure = schm.isLayered();
         if (proxy == null) {
            route = new HttpRoute(target, local, secure);
         } else {
            route = new HttpRoute(target, local, proxy, secure);
         }

         return route;
      }
   }
}
