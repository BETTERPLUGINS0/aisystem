package me.PM2.infinitevehicles.commands;

import org.bukkit.NamespacedKey;

class BukkitCommandContexts_1_12 {
   static void register(BukkitCommandContexts contexts) {
      var0.registerContext(NamespacedKey.class, (var0x) -> {
         String var1 = var0x.popFirstArg();
         String[] var2 = ACFPatterns.COLON.split(var1, 2);
         if (var2.length == 1) {
            String var3 = var0x.getFlagValue("namespace", (String)null);
            return var3 == null ? NamespacedKey.minecraft(var2[0]) : new NamespacedKey(var3, var2[0]);
         } else {
            return new NamespacedKey(var2[0], var2[1]);
         }
      });
   }
}
