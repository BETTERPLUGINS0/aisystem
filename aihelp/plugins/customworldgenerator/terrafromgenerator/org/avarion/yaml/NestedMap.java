package org.avarion.yaml;

import java.util.LinkedHashMap;
import java.util.Map;
import org.avarion.yaml.exceptions.DuplicateKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class NestedMap {
   private final Map<String, Object> map = new LinkedHashMap();

   public void put(@NotNull String key, @Nullable String comment, Object value) throws DuplicateKey {
      String[] keys = key.split("\\.");
      Map<String, Object> current = this.map;

      for(int i = 0; i < keys.length - 1; ++i) {
         String k = keys[i];
         current = (Map)current.computeIfAbsent(k, (x) -> {
            return new LinkedHashMap();
         });
      }

      String lastKey = keys[keys.length - 1];
      if (current.containsKey(lastKey)) {
         throw new DuplicateKey(key);
      } else {
         current.put(lastKey, new NestedMap.NestedNode(value, comment));
      }
   }

   public Map<String, Object> getMap() {
      return this.map;
   }

   public static class NestedNode {
      public final Object value;
      @Nullable
      public final String comment;

      public NestedNode(@Nullable Object value, @Nullable String comment) {
         this.value = value;
         this.comment = comment;
      }
   }
}
