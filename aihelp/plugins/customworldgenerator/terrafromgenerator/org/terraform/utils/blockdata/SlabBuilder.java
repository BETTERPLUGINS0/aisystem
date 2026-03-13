package org.terraform.utils.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.utils.GenUtils;

public class SlabBuilder {
   @NotNull
   private final Slab blockData;

   public SlabBuilder(@NotNull Material mat) {
      this.blockData = (Slab)Bukkit.createBlockData(mat);
   }

   public SlabBuilder(Material... mat) {
      this.blockData = (Slab)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])mat));
   }

   @NotNull
   public SlabBuilder setType(@NotNull Type type) {
      this.blockData.setType(type);
      return this;
   }

   @NotNull
   public SlabBuilder setWaterlogged(boolean bool) {
      this.blockData.setWaterlogged(bool);
      return this;
   }

   @NotNull
   public SlabBuilder apply(@NotNull SimpleBlock block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public SlabBuilder apply(@NotNull Wall block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public SlabBuilder apply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      data.setBlockData(x, y, z, this.blockData);
      return this;
   }

   @NotNull
   public SlabBuilder lapply(@NotNull SimpleBlock block) {
      if (block.isSolid()) {
         return this;
      } else {
         block.setBlockData(this.blockData);
         return this;
      }
   }

   @NotNull
   public SlabBuilder lapply(@NotNull Wall block) {
      if (block.isSolid()) {
         return this;
      } else {
         block.setBlockData(this.blockData);
         return this;
      }
   }

   @NotNull
   public SlabBuilder lapply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (data.getType(x, y, z).isSolid()) {
         return this;
      } else {
         data.setBlockData(x, y, z, this.blockData);
         return this;
      }
   }

   @NotNull
   public Slab get() {
      return this.blockData;
   }
}
