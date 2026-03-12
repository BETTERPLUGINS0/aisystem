package fr.xephi.authme.libs.com.maxmind.db.cache;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CHMCache implements NodeCache {
   private static final int DEFAULT_CAPACITY = 4096;
   private final int capacity;
   private final Map<Integer, JsonElement> cache;
   private boolean cacheFull;

   public CHMCache() {
      this(4096);
   }

   public CHMCache(int capacity) {
      this.capacity = capacity;
      this.cache = new ConcurrentHashMap(capacity);
   }

   public JsonElement get(int key, NodeCache.Loader loader) throws IOException {
      Integer k = key;
      JsonElement value = (JsonElement)this.cache.get(k);
      if (value == null) {
         value = loader.load(key);
         if (!this.cacheFull) {
            if (this.cache.size() < this.capacity) {
               this.cache.put(k, value);
            } else {
               this.cacheFull = true;
            }
         }
      }

      return value;
   }
}
