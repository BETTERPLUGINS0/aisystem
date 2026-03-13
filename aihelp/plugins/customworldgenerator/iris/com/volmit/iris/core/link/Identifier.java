package com.volmit.iris.core.link;

import org.bukkit.NamespacedKey;

public record Identifier(String namespace, String key) {
   private static final String DEFAULT_NAMESPACE = "minecraft";

   public Identifier(String namespace, String key) {
      this.namespace = var1;
      this.key = var2;
   }

   public static Identifier fromNamespacedKey(NamespacedKey key) {
      return new Identifier(var0.getNamespace(), var0.getKey());
   }

   public static Identifier fromString(String id) {
      String[] var1 = var0.split(":", 2);
      return var1.length == 1 ? new Identifier("minecraft", var1[0]) : new Identifier(var1[0], var1[1]);
   }

   public String toString() {
      return this.namespace + ":" + this.key;
   }

   public boolean equals(Object obj) {
      if (var1 instanceof Identifier) {
         Identifier var2 = (Identifier)var1;
         return var2.namespace().equals(this.namespace) && var2.key().equals(this.key);
      } else if (!(var1 instanceof NamespacedKey)) {
         return false;
      } else {
         NamespacedKey var3 = (NamespacedKey)var1;
         return var3.getNamespace().equals(this.namespace) && var3.getKey().equals(this.key);
      }
   }

   public String namespace() {
      return this.namespace;
   }

   public String key() {
      return this.key;
   }
}
