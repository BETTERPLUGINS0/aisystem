package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.collection.MaterialSet;
import java.lang.reflect.AnnotatedType;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class MaterialSetLoader implements TypeLoader<MaterialSet> {
   public MaterialSet load(@NotNull AnnotatedType type, @NotNull Object o, @NotNull ConfigLoader configLoader, DepthTracker depthTracker) throws LoadException {
      List<String> stringData = (List)o;
      if (stringData.size() == 1) {
         return MaterialSet.singleton((BlockType)configLoader.loadType(BlockType.class, stringData.get(0), depthTracker));
      } else {
         MaterialSet set = new MaterialSet();
         Iterator var7 = stringData.iterator();

         while(var7.hasNext()) {
            String string = (String)var7.next();

            try {
               set.add((BlockType)configLoader.loadType(BlockType.class, string, depthTracker));
            } catch (NullPointerException var10) {
               throw new LoadException("Invalid data identifier \"" + string + "\"", var10, depthTracker);
            }
         }

         return set;
      }
   }
}
