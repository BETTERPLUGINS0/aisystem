package org.terraform.schematic;

import java.util.ArrayList;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class TerraRegion {
   private Block one;
   private Block two;

   @NotNull
   public ArrayList<Block> getBlocks() {
      ArrayList<Block> blocks = new ArrayList();
      int topBlockX = Math.max(this.one.getX(), this.two.getX());
      int bottomBlockX = Math.min(this.one.getX(), this.two.getX());
      int topBlockY = Math.max(this.one.getY(), this.two.getY());
      int bottomBlockY = Math.min(this.one.getY(), this.two.getY());
      int topBlockZ = Math.max(this.one.getZ(), this.two.getZ());
      int bottomBlockZ = Math.min(this.one.getZ(), this.two.getZ());

      for(int x = bottomBlockX; x <= topBlockX; ++x) {
         for(int z = bottomBlockZ; z <= topBlockZ; ++z) {
            for(int y = bottomBlockY; y <= topBlockY; ++y) {
               Block block = this.one.getWorld().getBlockAt(x, y, z);
               blocks.add(block);
            }
         }
      }

      return blocks;
   }

   public boolean isComplete() {
      return this.one != null && this.two != null;
   }

   public Block getOne() {
      return this.one;
   }

   public void setOne(Block one) {
      this.one = one;
   }

   public Block getTwo() {
      return this.two;
   }

   public void setTwo(Block two) {
      this.two = two;
   }
}
