package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.util.Map;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.SAFE
)
public class HttpRequestHandlerRegistry implements HttpRequestHandlerResolver {
   private final UriPatternMatcher<HttpRequestHandler> matcher = new UriPatternMatcher();

   public void register(String pattern, HttpRequestHandler handler) {
      Args.notNull(pattern, "URI request pattern");
      Args.notNull(handler, "Request handler");
      this.matcher.register(pattern, handler);
   }

   public void unregister(String pattern) {
      this.matcher.unregister(pattern);
   }

   public void setHandlers(Map<String, HttpRequestHandler> map) {
      this.matcher.setObjects(map);
   }

   public Map<String, HttpRequestHandler> getHandlers() {
      return this.matcher.getObjects();
   }

   public HttpRequestHandler lookup(String requestURI) {
      return (HttpRequestHandler)this.matcher.lookup(requestURI);
   }
}
