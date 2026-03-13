package me.casperge.realisticseasons.blockscanner;

import java.util.Objects;
import org.bukkit.Location;

public class SimpleLocation {
   private int x;
   private int y;
   private int z;
   private String worldName;
   private int maxY;
   private int minY;
   private int hash;

   public SimpleLocation(Location var1, int var2, int var3) {
      this.x = var1.getBlockX();
      this.y = var1.getBlockY();
      this.z = var1.getBlockZ();
      this.worldName = var1.getWorld().getName();
      this.maxY = var2;
      this.minY = var3;
      this.hash = 0;
   }

   public SimpleLocation(int var1, int var2, int var3, String var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.worldName = var4;
      this.maxY = 0;
      this.minY = 0;
      this.hash = 0;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public int getMaxY() {
      return this.maxY;
   }

   public int getMinY() {
      return this.minY;
   }

   public String getWorldName() {
      return this.worldName;
   }

   public int hashCode() {
      if (this.hash == 0) {
         this.hash = Objects.hash(new Object[]{this.x, this.y, this.z, this.worldName, this.maxY, this.minY});
      }

      return this.hash;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         SimpleLocation var2 = (SimpleLocation)var1;
         if (this.x != var2.x) {
            return false;
         } else if (this.y != var2.y) {
            return false;
         } else if (this.z != var2.z) {
            return false;
         } else if (!this.worldName.equals(var2.worldName)) {
            return false;
         } else if (this.maxY != var2.maxY) {
            return false;
         } else {
            return this.minY == var2.minY;
         }
      }
   }
}
