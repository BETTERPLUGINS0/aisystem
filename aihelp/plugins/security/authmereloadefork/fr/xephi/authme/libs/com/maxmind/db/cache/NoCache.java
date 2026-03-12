package fr.xephi.authme.libs.com.maxmind.db.cache;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import java.io.IOException;

public class NoCache implements NodeCache {
   private static final NoCache INSTANCE = new NoCache();

   private NoCache() {
   }

   public JsonElement get(int key, NodeCache.Loader loader) throws IOException {
      return loader.load(key);
   }

   public static NoCache getInstance() {
      return INSTANCE;
   }
}
