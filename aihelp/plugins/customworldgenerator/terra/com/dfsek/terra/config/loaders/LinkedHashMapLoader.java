package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;

public class LinkedHashMapLoader implements TypeLoader<LinkedHashMap<Object, Object>> {
   public LinkedHashMap<Object, Object> load(@NotNull AnnotatedType t, @NotNull Object c, @NotNull ConfigLoader loader, DepthTracker depthTracker) throws LoadException {
      Map<String, Object> config = (Map)c;
      LinkedHashMap<Object, Object> map = new LinkedHashMap();
      if (!(t instanceof AnnotatedParameterizedType)) {
         throw new LoadException("Unable to load config", depthTracker);
      } else {
         AnnotatedParameterizedType pType = (AnnotatedParameterizedType)t;
         AnnotatedType key = pType.getAnnotatedActualTypeArguments()[0];
         AnnotatedType value = pType.getAnnotatedActualTypeArguments()[1];
         Iterator var10 = config.entrySet().iterator();

         while(var10.hasNext()) {
            Entry<String, Object> entry = (Entry)var10.next();
            map.put(loader.loadType(key, entry.getKey(), depthTracker.entry((String)entry.getKey())), loader.loadType(value, entry.getValue(), depthTracker.entry((String)entry.getKey())));
         }

         return map;
      }
   }
}
