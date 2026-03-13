package org.terraform.utils.blockdata;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Stairs.Shape;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class StairBuilder {
   @NotNull
   final ArrayList<SimpleBlock> placed = new ArrayList();
   @NotNull
   private final Stairs blockData;

   public StairBuilder(@NotNull Material mat) {
      this.blockData = (Stairs)Bukkit.createBlockData(mat);
   }

   public StairBuilder(Material... mat) {
      this.blockData = (Stairs)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])mat));
   }

   @NotNull
   public StairBuilder setFacing(@NotNull BlockFace face) {
      this.blockData.setFacing(face);
      return this;
   }

   @NotNull
   public StairBuilder setHalf(@NotNull Half half) {
      this.blockData.setHalf(half);
      return this;
   }

   @NotNull
   public StairBuilder setShape(@NotNull Shape shape) {
      this.blockData.setShape(shape);
      return this;
   }

   @NotNull
   public StairBuilder setWaterlogged(boolean bool) {
      this.blockData.setWaterlogged(bool);
      return this;
   }

   @NotNull
   public StairBuilder apply(@NotNull SimpleBlock block) {
      block.setBlockData(this.blockData);
      this.placed.add(block);
      return this;
   }

   @NotNull
   public StairBuilder lapply(@NotNull SimpleBlock block) {
      if (block.isSolid()) {
         return this;
      } else {
         block.setBlockData(this.blockData);
         this.placed.add(block);
         return this;
      }
   }

   @NotNull
   public StairBuilder apply(@NotNull Wall block) {
      block.setBlockData(this.blockData);
      this.placed.add(block.get());
      return this;
   }

   @NotNull
   public StairBuilder apply(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      data.setBlockData(x, y, z, this.blockData);
      this.placed.add(new SimpleBlock(data, x, y, z));
      return this;
   }

   @NotNull
   public StairBuilder correct() {
      Iterator var1 = this.placed.iterator();

      while(var1.hasNext()) {
         SimpleBlock b = (SimpleBlock)var1.next();
         BlockUtils.correctSurroundingStairData(b);
      }

      return this;
   }

   @NotNull
   public Stairs get() {
      return this.blockData;
   }
}
