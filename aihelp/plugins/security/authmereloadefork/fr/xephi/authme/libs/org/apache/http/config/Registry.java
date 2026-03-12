package fr.xephi.authme.libs.org.apache.http.config;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Contract(
   threading = ThreadingBehavior.SAFE
)
public final class Registry<I> implements Lookup<I> {
   private final Map<String, I> map;

   Registry(Map<String, I> map) {
      this.map = new ConcurrentHashMap(map);
   }

   public I lookup(String key) {
      return key == null ? null : this.map.get(key.toLowerCase(Locale.ROOT));
   }

   public String toString() {
      return this.map.toString();
   }
}
