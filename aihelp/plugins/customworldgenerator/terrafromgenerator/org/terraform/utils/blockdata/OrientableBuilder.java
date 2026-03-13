package org.terraform.utils.blockdata;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Orientable;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.utils.GenUtils;

public class OrientableBuilder {
   @NotNull
   private final Orientable blockData;

   public OrientableBuilder(@NotNull Material mat) {
      this.blockData = (Orientable)Bukkit.createBlockData(mat);
   }

   public OrientableBuilder(Material... mat) {
      this.blockData = (Orientable)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])mat));
   }

   @NotNull
   public OrientableBuilder setAxis(@NotNull Axis axis) {
      this.blockData.setAxis(axis);
      return this;
   }

   @NotNull
   public OrientableBuilder apply(@NotNull SimpleBlock block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public OrientableBuilder apply(@NotNull Wall block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public OrientableBuilder apply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      data.setBlockData(x, y, z, this.blockData);
      return this;
   }

   @NotNull
   public OrientableBuilder lapply(@NotNull SimpleBlock block) {
      if (!block.isSolid()) {
         block.setBlockData(this.blockData);
      }

      return this;
   }

   @NotNull
   public OrientableBuilder lapply(@NotNull Wall block) {
      if (!block.isSolid()) {
         block.setBlockData(this.blockData);
      }

      return this;
   }

   @NotNull
   public OrientableBuilder lapply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (!data.getType(x, y, z).isSolid()) {
         data.setBlockData(x, y, z, this.blockData);
      }

      return this;
   }

   @NotNull
   public Orientable get() {
      return this.blockData;
   }
}
