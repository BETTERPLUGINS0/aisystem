package com.nisovin.shopkeepers.util.java;

import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public class MapBasedKeyValueStore implements KeyValueStore {
   private final Map<String, Object> map;

   public MapBasedKeyValueStore() {
      this(new HashMap());
   }

   public MapBasedKeyValueStore(Map<String, Object> map) {
      Validate.notNull(map, (String)"map is null");
      this.map = map;
   }

   @Nullable
   public <T> T get(String key) {
      Validate.notEmpty(key, "key is null or empty");
      return this.map.get(key);
   }

   public void set(String key, @Nullable Object value) {
      Validate.notEmpty(key, "key is null or empty");
      if (value == null) {
         this.map.remove(key);
      } else {
         this.map.put(key, value);
      }

   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("MapBasedKeyValueStore [map=");
      builder.append(this.map);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      return this.map.hashCode();
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof MapBasedKeyValueStore)) {
         return false;
      } else {
         MapBasedKeyValueStore other = (MapBasedKeyValueStore)obj;
         return this.map.equals(other.map);
      }
   }
}
