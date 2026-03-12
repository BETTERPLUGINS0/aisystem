package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.cookie.CommonCookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieAttributeHandler;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpec;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.Asserts;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Contract(
   threading = ThreadingBehavior.SAFE
)
public abstract class AbstractCookieSpec implements CookieSpec {
   private final Map<String, CookieAttributeHandler> attribHandlerMap;

   public AbstractCookieSpec() {
      this.attribHandlerMap = new ConcurrentHashMap(10);
   }

   protected AbstractCookieSpec(HashMap<String, CookieAttributeHandler> map) {
      Asserts.notNull(map, "Attribute handler map");
      this.attribHandlerMap = new ConcurrentHashMap(map);
   }

   protected AbstractCookieSpec(CommonCookieAttributeHandler... handlers) {
      this.attribHandlerMap = new ConcurrentHashMap(handlers.length);
      CommonCookieAttributeHandler[] arr$ = handlers;
      int len$ = handlers.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         CommonCookieAttributeHandler handler = arr$[i$];
         this.attribHandlerMap.put(handler.getAttributeName(), handler);
      }

   }

   /** @deprecated */
   @Deprecated
   public void registerAttribHandler(String name, CookieAttributeHandler handler) {
      Args.notNull(name, "Attribute name");
      Args.notNull(handler, "Attribute handler");
      this.attribHandlerMap.put(name, handler);
   }

   protected CookieAttributeHandler findAttribHandler(String name) {
      return (CookieAttributeHandler)this.attribHandlerMap.get(name);
   }

   protected CookieAttributeHandler getAttribHandler(String name) {
      CookieAttributeHandler handler = this.findAttribHandler(name);
      Asserts.check(handler != null, "Handler not registered for " + name + " attribute");
      return handler;
   }

   protected Collection<CookieAttributeHandler> getAttribHandlers() {
      return this.attribHandlerMap.values();
   }
}
