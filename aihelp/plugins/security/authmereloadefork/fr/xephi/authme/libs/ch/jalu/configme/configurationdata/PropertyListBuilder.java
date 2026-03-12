package fr.xephi.authme.libs.ch.jalu.configme.configurationdata;

import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PropertyListBuilder {
   private Map<String, Object> rootEntries = new LinkedHashMap();

   public void add(Property<?> property) {
      String[] paths = property.getPath().split("\\.");
      Map<String, Object> map = this.rootEntries;

      for(int i = 0; i < paths.length - 1; ++i) {
         map = getChildMap(map, paths[i]);
      }

      String end = paths[paths.length - 1];
      if (map.containsKey(end)) {
         throw new ConfigMeException("Path at '" + property.getPath() + "' already exists");
      } else {
         map.put(end, property);
      }
   }

   public List<Property<?>> create() {
      List<Property<?>> result = new ArrayList();
      collectEntries(this.rootEntries, result);
      return result;
   }

   protected final Map<String, Object> getRootEntries() {
      return this.rootEntries;
   }

   private static Map<String, Object> getChildMap(Map<String, Object> parent, String path) {
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

   private static void collectEntries(Map<String, Object> map, List<Property<?>> results) {
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

   private static Map<String, Object> asTypedMap(Object o) {
      return (Map)o;
   }
}
