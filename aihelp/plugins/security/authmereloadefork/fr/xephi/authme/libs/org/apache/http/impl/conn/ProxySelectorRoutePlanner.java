package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.conn.params.ConnRouteParams;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoutePlanner;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.Scheme;
import fr.xephi.authme.libs.org.apache.http.conn.scheme.SchemeRegistry;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.Asserts;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.Proxy.Type;
import java.util.Collection;
import java.util.List;

/** @deprecated */
@Deprecated
public class ProxySelectorRoutePlanner implements HttpRoutePlanner {
   protected final SchemeRegistry schemeRegistry;
   protected ProxySelector proxySelector;

   public ProxySelectorRoutePlanner(SchemeRegistry schreg, ProxySelector prosel) {
      Args.notNull(schreg, "SchemeRegistry");
      this.schemeRegistry = schreg;
      this.proxySelector = prosel;
   }

   public ProxySelector getProxySelector() {
      return this.proxySelector;
   }

   public void setProxySelector(ProxySelector prosel) {
      this.proxySelector = prosel;
   }

   public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
      Args.notNull(request, "HTTP request");
      HttpRoute route = ConnRouteParams.getForcedRoute(request.getParams());
      if (route != null) {
         return route;
      } else {
         Asserts.notNull(target, "Target host");
         InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
         HttpHost proxy = this.determineProxy(target, request, context);
         Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
         boolean secure = schm.isLayered();
         if (proxy == null) {
            route = new HttpRoute(target, local, secure);
         } else {
            route = new HttpRoute(target, local, proxy, secure);
         }

         return route;
      }
   }

   protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
      ProxySelector psel = this.proxySelector;
      if (psel == null) {
         psel = ProxySelector.getDefault();
      }

      if (psel == null) {
         return null;
      } else {
         URI targetURI = null;

         try {
            targetURI = new URI(target.toURI());
         } catch (URISyntaxException var10) {
            throw new HttpException("Cannot convert host to URI: " + target, var10);
         }

         List<Proxy> proxies = psel.select(targetURI);
         Proxy p = this.chooseProxy(proxies, target, request, context);
         HttpHost result = null;
         if (p.type() == Type.HTTP) {
            if (!(p.address() instanceof InetSocketAddress)) {
               throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
            }

            InetSocketAddress isa = (InetSocketAddress)p.address();
            result = new HttpHost(this.getHost(isa), isa.getPort());
         }

         return result;
      }
   }

   protected String getHost(InetSocketAddress isa) {
      return isa.isUnresolved() ? isa.getHostName() : isa.getAddress().getHostAddress();
   }

   protected Proxy chooseProxy(List<Proxy> proxies, HttpHost target, HttpRequest request, HttpContext context) {
      Args.notEmpty((Collection)proxies, "List of proxies");
      Proxy result = null;
      int i = 0;

      while(result == null && i < proxies.size()) {
         Proxy p = (Proxy)proxies.get(i);
         switch(p.type()) {
         case DIRECT:
         case HTTP:
            result = p;
         case SOCKS:
         default:
            ++i;
         }
      }

      if (result == null) {
         result = Proxy.NO_PROXY;
      }

      return result;
   }
}
