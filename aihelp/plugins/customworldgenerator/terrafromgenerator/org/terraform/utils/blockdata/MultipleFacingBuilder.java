package org.terraform.utils.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.utils.GenUtils;

public class MultipleFacingBuilder {
   @NotNull
   private final MultipleFacing blockData;

   public MultipleFacingBuilder(@NotNull Material mat) {
      this.blockData = (MultipleFacing)Bukkit.createBlockData(mat);
   }

   public MultipleFacingBuilder(Material... mat) {
      this.blockData = (MultipleFacing)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])mat));
   }

   @NotNull
   public MultipleFacingBuilder setFace(@NotNull BlockFace face, boolean isEnabled) {
      this.blockData.setFace(face, isEnabled);
      return this;
   }

   @NotNull
   public MultipleFacingBuilder apply(@NotNull SimpleBlock block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public MultipleFacingBuilder apply(@NotNull Wall block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public MultipleFacingBuilder apply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      data.setBlockData(x, y, z, this.blockData);
      return this;
   }

   @NotNull
   public MultipleFacing get() {
      return this.blockData;
   }
}
