package fr.xephi.authme.libs.com.maxmind.db.cache;

import fr.xephi.authme.libs.com.google.gson.JsonElement;
import java.io.IOException;

public interface NodeCache {
   JsonElement get(int var1, NodeCache.Loader var2) throws IOException;

   public interface Loader {
      JsonElement load(int var1) throws IOException;
   }
}
