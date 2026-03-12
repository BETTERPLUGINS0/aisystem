package fr.xephi.authme.libs.ch.jalu.configme.resource;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public class MapNormalizer {
   private final boolean splitDotPaths;

   public MapNormalizer(boolean splitDotPaths) {
      this.splitDotPaths = splitDotPaths;
   }

   protected final boolean splitDotPaths() {
      return this.splitDotPaths;
   }

   @Nullable
   public Map<String, Object> normalizeMap(@Nullable Map<Object, Object> loadedMap) {
      return loadedMap == null ? null : (Map)this.createNormalizedMapIfNeeded(loadedMap).orElse(loadedMap);
   }

   protected Optional<Map<String, Object>> createNormalizedMapIfNeeded(Object value) {
      if (!(value instanceof Map)) {
         return Optional.empty();
      } else {
         Map<Object, Object> map = (Map)value;
         boolean mapNeedsModification = false;
         Iterator var4 = map.entrySet().iterator();

         while(var4.hasNext()) {
            Entry<Object, Object> entry = (Entry)var4.next();
            this.createNormalizedMapIfNeeded(entry.getValue()).ifPresent((newMap) -> {
               map.put(entry.getKey(), newMap);
            });
            if (!mapNeedsModification && this.isKeyInvalid(entry.getKey())) {
               mapNeedsModification = true;
            }
         }

         if (!mapNeedsModification) {
            return Optional.empty();
         } else {
            Map<String, Object> cleanedMap = new LinkedHashMap(map.size());
            Iterator var8 = map.entrySet().iterator();

            while(var8.hasNext()) {
               Entry<Object, Object> entry = (Entry)var8.next();
               this.addValueIntoMap(cleanedMap, Objects.toString(entry.getKey()), entry.getValue());
            }

            return Optional.of(cleanedMap);
         }
      }
   }

   protected boolean isKeyInvalid(Object key) {
      if (!(key instanceof String)) {
         return true;
      } else {
         return this.splitDotPaths && ((String)key).contains(".");
      }
   }

   protected void addValueIntoMap(Map<String, Object> map, String path, Object value) {
      int dotPosition = this.splitDotPaths ? path.indexOf(".") : -1;
      Map mapValue;
      if (dotPosition > -1) {
         String pathElement = path.substring(0, dotPosition);
         mapValue = this.getOrInsertMap(map, pathElement);
         this.addValueIntoMap(mapValue, path.substring(dotPosition + 1), value);
      } else if (value instanceof Map) {
         Map<String, Object> mapAtPath = this.getOrInsertMap(map, path);
         mapValue = (Map)value;
         mapValue.forEach((entryKey, entryValue) -> {
            this.addValueIntoMap(mapAtPath, Objects.toString(entryKey), entryValue);
         });
      } else {
         map.put(path, value);
      }

   }

   protected Map<String, Object> getOrInsertMap(Map<String, Object> parentMap, String path) {
      Object value = parentMap.get(path);
      if (value instanceof Map) {
         return (Map)value;
      } else {
         Map<String, Object> newMap = new LinkedHashMap();
         parentMap.put(path, newMap);
         return newMap;
      }
   }
}
