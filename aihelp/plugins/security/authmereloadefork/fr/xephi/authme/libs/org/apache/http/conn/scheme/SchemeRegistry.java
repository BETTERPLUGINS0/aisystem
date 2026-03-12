package fr.xephi.authme.libs.org.apache.http.conn.scheme;

import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.SAFE
)
public final class SchemeRegistry {
   private final ConcurrentHashMap<String, Scheme> registeredSchemes = new ConcurrentHashMap();

   public final Scheme getScheme(String name) {
      Scheme found = this.get(name);
      if (found == null) {
         throw new IllegalStateException("Scheme '" + name + "' not registered.");
      } else {
         return found;
      }
   }

   public final Scheme getScheme(HttpHost host) {
      Args.notNull(host, "Host");
      return this.getScheme(host.getSchemeName());
   }

   public final Scheme get(String name) {
      Args.notNull(name, "Scheme name");
      Scheme found = (Scheme)this.registeredSchemes.get(name);
      return found;
   }

   public final Scheme register(Scheme sch) {
      Args.notNull(sch, "Scheme");
      Scheme old = (Scheme)this.registeredSchemes.put(sch.getName(), sch);
      return old;
   }

   public final Scheme unregister(String name) {
      Args.notNull(name, "Scheme name");
      Scheme gone = (Scheme)this.registeredSchemes.remove(name);
      return gone;
   }

   public final List<String> getSchemeNames() {
      return new ArrayList(this.registeredSchemes.keySet());
   }

   public void setItems(Map<String, Scheme> map) {
      if (map != null) {
         this.registeredSchemes.clear();
         this.registeredSchemes.putAll(map);
      }
   }
}
