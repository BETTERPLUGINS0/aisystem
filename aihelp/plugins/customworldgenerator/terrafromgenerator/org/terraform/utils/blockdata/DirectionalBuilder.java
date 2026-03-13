package org.terraform.utils.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.utils.GenUtils;

public class DirectionalBuilder {
   @NotNull
   private final Directional blockData;

   public DirectionalBuilder(@NotNull Material mat) {
      this.blockData = (Directional)Bukkit.createBlockData(mat);
   }

   public DirectionalBuilder(Material... mat) {
      this.blockData = (Directional)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])mat));
   }

   @NotNull
   public DirectionalBuilder setFacing(@NotNull BlockFace face) {
      this.blockData.setFacing(face);
      return this;
   }

   @NotNull
   public DirectionalBuilder apply(@NotNull SimpleBlock block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public DirectionalBuilder apply(@NotNull Wall block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public DirectionalBuilder apply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      data.setBlockData(x, y, z, this.blockData);
      return this;
   }

   @NotNull
   public Directional get() {
      return this.blockData;
   }
}
