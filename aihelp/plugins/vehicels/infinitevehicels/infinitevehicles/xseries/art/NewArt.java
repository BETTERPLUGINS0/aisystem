package me.PM2.infinitevehicles.xseries.art;

import org.bukkit.Art;

class NewArt extends BukkitArt {
   private final Art art;

   public NewArt(Object var1) {
      this.art = (Art)var1;
   }

   public int getBlockWidth() {
      return this.art.getBlockWidth();
   }

   public int getBlockHeight() {
      return this.art.getBlockHeight();
   }

   public String getKey() {
      return this.art.getKey().getKey();
   }

   public int getId() {
      return this.art.getId();
   }

   public Art object() {
      return this.art;
   }
}
