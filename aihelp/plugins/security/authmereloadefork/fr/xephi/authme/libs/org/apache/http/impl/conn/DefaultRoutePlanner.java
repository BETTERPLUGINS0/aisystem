package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.config.RequestConfig;
import fr.xephi.authme.libs.org.apache.http.client.protocol.HttpClientContext;
import fr.xephi.authme.libs.org.apache.http.conn.SchemePortResolver;
import fr.xephi.authme.libs.org.apache.http.conn.UnsupportedSchemeException;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoutePlanner;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.net.InetAddress;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class DefaultRoutePlanner implements HttpRoutePlanner {
   private final SchemePortResolver schemePortResolver;

   public DefaultRoutePlanner(SchemePortResolver schemePortResolver) {
      this.schemePortResolver = (SchemePortResolver)(schemePortResolver != null ? schemePortResolver : DefaultSchemePortResolver.INSTANCE);
   }

   public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context) throws HttpException {
      Args.notNull(request, "Request");
      if (host == null) {
         throw new ProtocolException("Target host is not specified");
      } else {
         HttpClientContext clientContext = HttpClientContext.adapt(context);
         RequestConfig config = clientContext.getRequestConfig();
         InetAddress local = config.getLocalAddress();
         HttpHost proxy = config.getProxy();
         if (proxy == null) {
            proxy = this.determineProxy(host, request, context);
         }

         HttpHost target;
         if (host.getPort() <= 0) {
            try {
               target = new HttpHost(host.getHostName(), this.schemePortResolver.resolve(host), host.getSchemeName());
            } catch (UnsupportedSchemeException var10) {
               throw new HttpException(var10.getMessage());
            }
         } else {
            target = host;
         }

         boolean secure = target.getSchemeName().equalsIgnoreCase("https");
         return proxy == null ? new HttpRoute(target, local, secure) : new HttpRoute(target, local, proxy, secure);
      }
   }

   protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
      return null;
   }
}
