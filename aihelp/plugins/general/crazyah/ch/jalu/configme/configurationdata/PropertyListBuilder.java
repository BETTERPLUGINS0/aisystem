package ch.jalu.configme.configurationdata;

import ch.jalu.configme.exception.ConfigMeException;
import ch.jalu.configme.properties.Property;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class PropertyListBuilder {
   @NotNull
   private final Map<String, Object> rootEntries = new LinkedHashMap();

   public void add(@NotNull Property<?> property) {
      String[] pathElements = property.getPath().split("\\.", -1);
      Map<String, Object> mapForProperty = this.getMapBeforeLastElement(pathElements);
      String lastElement = pathElements[pathElements.length - 1];
      if (mapForProperty.containsKey(lastElement)) {
         throw new ConfigMeException("Path at '" + property.getPath() + "' already exists");
      } else {
         if (pathElements.length > 1 && lastElement.equals("")) {
            this.throwExceptionForMalformedPath(property.getPath());
         }

         mapForProperty.put(lastElement, property);
      }
   }

   @NotNull
   public List<Property<?>> create() {
      List<Property<?>> result = new ArrayList();
      collectEntries(this.rootEntries, result);
      if (result.size() > 1 && this.rootEntries.containsKey("")) {
         throw new ConfigMeException("A property at the root path (\"\") cannot be defined alongside other properties as the paths would conflict");
      } else {
         return result;
      }
   }

   @NotNull
   protected Map<String, Object> getMapBeforeLastElement(@NotNull String[] pathParts) {
      Map<String, Object> map = this.rootEntries;

      for(int i = 0; i < pathParts.length - 1; ++i) {
         map = getChildMap(map, pathParts[i]);
         if (pathParts[i].equals("")) {
            this.throwExceptionForMalformedPath(String.join(".", pathParts));
         }
      }

      return map;
   }

   protected void throwExceptionForMalformedPath(@NotNull String path) {
      throw new ConfigMeException("The path at '" + path + "' is malformed: dots may not be at the beginning or end of a path, and dots may not appear multiple times successively.");
   }

   @NotNull
   protected final Map<String, Object> getRootEntries() {
      return this.rootEntries;
   }

   @NotNull
   private static Map<String, Object> getChildMap(@NotNull Map<String, Object> parent, @NotNull String path) {
      Object o = parent.get(path);
      if (o instanceof Map) {
         return asTypedMap(o);
      } else if (o == null) {
         Map<String, Object> map = new LinkedHashMap();
         parent.put(path, map);
         return map;
      } else if (o instanceof Property) {
         throw new ConfigMeException("Unexpected entry found at path '" + path + "'");
      } else {
         throw new ConfigMeException("Value of unknown type found at '" + path + "': " + o);
      }
   }

   private static void collectEntries(@NotNull Map<String, Object> map, @NotNull List<Property<?>> results) {
      Iterator var2 = map.values().iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         if (o instanceof Map) {
            collectEntries(asTypedMap(o), results);
         } else if (o instanceof Property) {
            results.add((Property)o);
         }
      }

   }

   @NotNull
   private static Map<String, Object> asTypedMap(@NotNull Object o) {
      return (Map)o;
   }
}
