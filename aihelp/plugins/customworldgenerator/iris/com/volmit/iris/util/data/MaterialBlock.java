package com.volmit.iris.util.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class MaterialBlock {
   private Material material;
   private Byte data;

   public MaterialBlock(Material material, Byte data) {
      this.material = var1;
      this.data = var2;
   }

   public MaterialBlock(Material material) {
      this.material = var1;
      this.data = 0;
   }

   public MaterialBlock(Location location) {
      this(var1.getBlock());
   }

   public MaterialBlock(BlockState state) {
      this.material = var1.getType();
      this.data = var1.getData().getData();
   }

   public MaterialBlock(Block block) {
      this.material = var1.getType();
      this.data = var1.getData();
   }

   public MaterialBlock() {
      this.material = Material.AIR;
      this.data = 0;
   }

   public Material getMaterial() {
      return this.material;
   }

   public void setMaterial(Material material) {
      this.material = var1;
   }

   public Byte getData() {
      return this.data;
   }

   public void setData(Byte data) {
      this.data = var1;
   }

   public String toString() {
      if (this.getData() == 0) {
         return this.getMaterial().toString();
      } else {
         String var10000 = this.getMaterial().toString();
         return var10000 + ":" + this.getData();
      }
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = 31 * var2 + (this.data == null ? 0 : this.data.hashCode());
      var3 = 31 * var3 + (this.material == null ? 0 : this.material.hashCode());
      return var3;
   }

   public boolean equals(Object obj) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         MaterialBlock var2 = (MaterialBlock)var1;
         if (this.data == null) {
            if (var2.data != null) {
               return false;
            }
         } else if (!this.data.equals(var2.data)) {
            return false;
         }

         return this.material == var2.material;
      }
   }
}
