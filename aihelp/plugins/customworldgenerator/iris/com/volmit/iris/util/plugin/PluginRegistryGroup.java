package com.volmit.iris.util.plugin;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;

public class PluginRegistryGroup<T> {
   private final KMap<String, PluginRegistry<T>> registries = new KMap();

   public T resolve(String namespace, String id) {
      if (this.registries.isEmpty()) {
         return null;
      } else {
         PluginRegistry var3 = (PluginRegistry)this.registries.get(var1);
         return var3 == null ? null : var3.resolve(var2);
      }
   }

   public void clearRegistries() {
      this.registries.clear();
   }

   public void removeRegistry(String namespace) {
      this.registries.remove(var1);
   }

   public PluginRegistry<T> getRegistry(String namespace) {
      return (PluginRegistry)this.registries.computeIfAbsent(var1, PluginRegistry::new);
   }

   public KList<String> compile() {
      KList var1 = new KList();
      this.registries.values().forEach((var1x) -> {
         var1x.getRegistries().forEach((var2) -> {
            String var10001 = var1x.getNamespace();
            var1.add((Object)(var10001 + ":" + var2));
         });
      });
      return var1;
   }
}
