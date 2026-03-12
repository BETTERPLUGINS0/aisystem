package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.RedirectHandler;
import fr.xephi.authme.libs.org.apache.http.client.RedirectStrategy;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpGet;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpHead;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpUriRequest;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.net.URI;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
class DefaultRedirectStrategyAdaptor implements RedirectStrategy {
   private final RedirectHandler handler;

   public DefaultRedirectStrategyAdaptor(RedirectHandler handler) {
      this.handler = handler;
   }

   public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
      return this.handler.isRedirectRequested(response, context);
   }

   public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
      URI uri = this.handler.getLocationURI(response, context);
      String method = request.getRequestLine().getMethod();
      return (HttpUriRequest)(method.equalsIgnoreCase("HEAD") ? new HttpHead(uri) : new HttpGet(uri));
   }

   public RedirectHandler getHandler() {
      return this.handler;
   }
}
