package com.volmit.iris.util.plugin;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import lombok.Generated;

public class PluginRegistry<T> {
   private final KMap<String, T> registry = new KMap();
   private final String namespace;

   public void unregisterAll() {
      this.registry.clear();
   }

   public KList<String> getRegistries() {
      return this.registry.k();
   }

   public T get(String s) {
      return !this.registry.containsKey(var1) ? null : this.registry.get(var1);
   }

   public void register(String s, T t) {
      this.registry.put(var1, var2);
   }

   public void unregister(String s) {
      this.registry.remove(var1);
   }

   public T resolve(String id) {
      return this.registry.isEmpty() ? null : this.registry.get(var1);
   }

   @Generated
   public PluginRegistry(final String namespace) {
      this.namespace = var1;
   }

   @Generated
   public String getNamespace() {
      return this.namespace;
   }
}
