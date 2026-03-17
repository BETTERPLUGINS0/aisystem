package me.PM2.infinitevehicles.xseries.art;

import org.bukkit.Art;

public class XArt {
   private static final boolean USE_INTERFACE = Art.class.isInterface();

   private XArt() {
   }

   public static BukkitArt of(Art var0) {
      return (BukkitArt)(USE_INTERFACE ? new NewArt(var0) : new OldArt(var0));
   }
}
