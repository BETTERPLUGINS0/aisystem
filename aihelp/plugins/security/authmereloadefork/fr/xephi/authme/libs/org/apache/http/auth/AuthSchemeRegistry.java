package fr.xephi.authme.libs.org.apache.http.auth;

import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.config.Lookup;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.SAFE
)
public final class AuthSchemeRegistry implements Lookup<AuthSchemeProvider> {
   private final ConcurrentHashMap<String, AuthSchemeFactory> registeredSchemes = new ConcurrentHashMap();

   public void register(String name, AuthSchemeFactory factory) {
      Args.notNull(name, "Name");
      Args.notNull(factory, "Authentication scheme factory");
      this.registeredSchemes.put(name.toLowerCase(Locale.ENGLISH), factory);
   }

   public void unregister(String name) {
      Args.notNull(name, "Name");
      this.registeredSchemes.remove(name.toLowerCase(Locale.ENGLISH));
   }

   public AuthScheme getAuthScheme(String name, HttpParams params) throws IllegalStateException {
      Args.notNull(name, "Name");
      AuthSchemeFactory factory = (AuthSchemeFactory)this.registeredSchemes.get(name.toLowerCase(Locale.ENGLISH));
      if (factory != null) {
         return factory.newInstance(params);
      } else {
         throw new IllegalStateException("Unsupported authentication scheme: " + name);
      }
   }

   public List<String> getSchemeNames() {
      return new ArrayList(this.registeredSchemes.keySet());
   }

   public void setItems(Map<String, AuthSchemeFactory> map) {
      if (map != null) {
         this.registeredSchemes.clear();
         this.registeredSchemes.putAll(map);
      }
   }

   public AuthSchemeProvider lookup(final String name) {
      return new AuthSchemeProvider() {
         public AuthScheme create(HttpContext context) {
            HttpRequest request = (HttpRequest)context.getAttribute("http.request");
            return AuthSchemeRegistry.this.getAuthScheme(name, request.getParams());
         }
      };
   }
}
