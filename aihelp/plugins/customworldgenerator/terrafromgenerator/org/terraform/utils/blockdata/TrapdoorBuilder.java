package org.terraform.utils.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.TrapDoor;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.utils.GenUtils;

public class TrapdoorBuilder {
   @NotNull
   private final TrapDoor blockData;

   public TrapdoorBuilder(@NotNull Material mat) {
      this.blockData = (TrapDoor)Bukkit.createBlockData(mat);
   }

   public TrapdoorBuilder(Material... mat) {
      this.blockData = (TrapDoor)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])mat));
   }

   @NotNull
   public TrapdoorBuilder setWaterlogged(boolean water) {
      this.blockData.setWaterlogged(water);
      return this;
   }

   @NotNull
   public TrapdoorBuilder setHalf(@NotNull Half h) {
      this.blockData.setHalf(h);
      return this;
   }

   @NotNull
   public TrapdoorBuilder setPowered(boolean powered) {
      this.blockData.setPowered(powered);
      return this;
   }

   @NotNull
   public TrapdoorBuilder setOpen(boolean open) {
      this.blockData.setOpen(open);
      return this;
   }

   @NotNull
   public TrapdoorBuilder setFacing(@NotNull BlockFace face) {
      this.blockData.setFacing(face);
      return this;
   }

   @NotNull
   public TrapdoorBuilder apply(@NotNull SimpleBlock block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public TrapdoorBuilder apply(@NotNull Wall block) {
      block.setBlockData(this.blockData);
      return this;
   }

   @NotNull
   public TrapdoorBuilder lapply(@NotNull Wall block) {
      if (!block.isSolid()) {
         block.setBlockData(this.blockData);
      }

      return this;
   }

   @NotNull
   public TrapdoorBuilder apply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      data.setBlockData(x, y, z, this.blockData);
      return this;
   }

   @NotNull
   public TrapDoor get() {
      return this.blockData;
   }
}
