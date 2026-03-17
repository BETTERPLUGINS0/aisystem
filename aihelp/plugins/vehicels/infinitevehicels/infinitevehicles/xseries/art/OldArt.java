package me.PM2.infinitevehicles.xseries.art;

import org.bukkit.Art;

class OldArt extends BukkitArt {
   private final Art art;

   public OldArt(Object var1) {
      this.art = (Art)var1;
   }

   public int getBlockWidth() {
      return this.art.getBlockWidth();
   }

   public int getBlockHeight() {
      return this.art.getBlockHeight();
   }

   public String getKey() {
      return this.art.name();
   }

   public int getId() {
      return this.art.getId();
   }

   public Art object() {
      return this.art;
   }
}
