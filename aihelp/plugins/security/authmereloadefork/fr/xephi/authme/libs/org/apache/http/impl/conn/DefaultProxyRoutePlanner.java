package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.SchemePortResolver;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class DefaultProxyRoutePlanner extends DefaultRoutePlanner {
   private final HttpHost proxy;

   public DefaultProxyRoutePlanner(HttpHost proxy, SchemePortResolver schemePortResolver) {
      super(schemePortResolver);
      this.proxy = (HttpHost)Args.notNull(proxy, "Proxy host");
   }

   public DefaultProxyRoutePlanner(HttpHost proxy) {
      this(proxy, (SchemePortResolver)null);
   }

   protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
      return this.proxy;
   }
}
