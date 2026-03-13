package org.terraform.coregen.populatordata;

import java.util.EnumSet;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.TerraLootTable;
import org.terraform.data.TerraformWorld;

public abstract class PopulatorDataAbstract {
   @NotNull
   public abstract Material getType(int var1, int var2, int var3);

   @NotNull
   public Material getType(@NotNull Vector v) {
      return this.getType((int)Math.round(v.getX()), (int)Math.round(v.getY()), (int)Math.round(v.getZ()));
   }

   @NotNull
   public abstract BlockData getBlockData(int var1, int var2, int var3);

   public abstract void setType(int var1, int var2, int var3, Material var4);

   public void setType(int x, int y, int z, @NotNull Material... type) {
      this.setType(x, y, z, type[(new Random()).nextInt(type.length)]);
   }

   public void setType(@NotNull Vector add, Material... mat) {
      this.setType((int)Math.round(add.getX()), (int)Math.round(add.getY()), (int)Math.round(add.getZ()), mat);
   }

   public void setBlockData(@NotNull Vector add, BlockData data) {
      this.setBlockData((int)Math.round(add.getX()), (int)Math.round(add.getY()), (int)Math.round(add.getZ()), data);
   }

   public void lsetBlockData(int x, int y, int z, @NotNull BlockData data) {
      if (!this.getType(x, y, z).isSolid()) {
         this.setBlockData(x, y, z, data);
      }

   }

   public void lsetType(int x, int y, int z, @NotNull Material... type) {
      if (!this.getType(x, y, z).isSolid()) {
         this.setType(x, y, z, type);
      }

   }

   public void lsetType(@NotNull Vector v, @NotNull Material... type) {
      if (!this.getType(v).isSolid()) {
         this.setType(v, type);
      }

   }

   public void rsetType(@NotNull Vector v, @NotNull EnumSet<Material> replaceable, Material... toSet) {
      if (replaceable.contains(this.getType(v))) {
         this.setType(v, toSet);
      }
   }

   public void rsetBlockData(@NotNull Vector v, @NotNull EnumSet<Material> replaceable, BlockData data) {
      if (replaceable.contains(this.getType(v))) {
         this.setBlockData(v, data);
      }
   }

   public abstract void setBlockData(int var1, int var2, int var3, @NotNull BlockData var4);

   @Nullable
   public abstract Biome getBiome(int var1, int var2);

   public abstract void addEntity(int var1, int var2, int var3, EntityType var4);

   public abstract int getChunkX();

   public abstract int getChunkZ();

   public abstract void setSpawner(int var1, int var2, int var3, EntityType var4);

   public abstract void lootTableChest(int var1, int var2, int var3, TerraLootTable var4);

   @NotNull
   public abstract TerraformWorld getTerraformWorld();

   /** @deprecated */
   @Deprecated
   public int hashCode() {
      return this.getClass().getCanonicalName().hashCode();
   }

   /** @deprecated */
   @Deprecated
   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else {
         return obj == null ? false : obj instanceof PopulatorDataAbstract;
      }
   }
}
