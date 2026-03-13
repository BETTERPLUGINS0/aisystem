package org.terraform.utils;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;

public class StalactiteBuilder {
   private Material[] solidBlockType;
   private Material[] wallType;
   private boolean isFacingUp;
   private int verticalSpace;
   private float minRadius = 0.0F;

   public StalactiteBuilder(Material... wallType) {
      this.wallType = wallType;
   }

   public void build(@NotNull Random rand, @NotNull Wall w) {
      if (TConfig.areDecorationsEnabled()) {
         if (this.verticalSpace >= 6) {
            int stalactiteHeight;
            if (this.verticalSpace > 60) {
               stalactiteHeight = GenUtils.randInt(rand, 6, 25);
            } else if (this.verticalSpace > 30) {
               stalactiteHeight = GenUtils.randInt(rand, 5, 17);
            } else if (this.verticalSpace > 15) {
               stalactiteHeight = GenUtils.randInt(rand, 3, 10);
            } else {
               stalactiteHeight = GenUtils.randInt(rand, 1, 2);
            }

            if (stalactiteHeight < 4) {
               if (this.isFacingUp) {
                  w.LPillar(stalactiteHeight, rand, this.wallType);
               } else {
                  w.downLPillar(rand, stalactiteHeight, this.wallType);
               }
            } else if (stalactiteHeight < 7) {
               if (this.isFacingUp) {
                  w.LPillar(stalactiteHeight, rand, this.wallType);
                  w.Pillar(GenUtils.randInt(rand, 2, 3), rand, this.solidBlockType);
               } else {
                  w.downLPillar(rand, stalactiteHeight, this.wallType);
                  w.downPillar(GenUtils.randInt(rand, 2, 3), this.solidBlockType);
               }
            } else if (this.isFacingUp) {
               this.makeSpike(w.getDown(), GenUtils.randDouble(rand, (double)stalactiteHeight / 6.0D, (double)stalactiteHeight / 4.0D), stalactiteHeight, true);
            } else {
               this.makeSpike(w.getUp(), GenUtils.randDouble(rand, (double)stalactiteHeight / 6.0D, (double)stalactiteHeight / 4.0D), stalactiteHeight, false);
            }

         }
      }
   }

   @NotNull
   public StalactiteBuilder setSolidBlockType(Material... solidBlockType) {
      this.solidBlockType = solidBlockType;
      return this;
   }

   @NotNull
   public StalactiteBuilder setWallType(Material... wallType) {
      this.wallType = wallType;
      return this;
   }

   @NotNull
   public StalactiteBuilder setFacingUp(boolean isFacingUp) {
      this.isFacingUp = isFacingUp;
      return this;
   }

   @NotNull
   public StalactiteBuilder setMinRadius(int minRadius) {
      this.minRadius = (float)minRadius;
      return this;
   }

   @NotNull
   public StalactiteBuilder setVerticalSpace(int verticalSpace) {
      this.verticalSpace = verticalSpace;
      return this;
   }

   public void makeSpike(@NotNull SimpleBlock root, double baseRadius, int height, boolean facingUp) {
      if (height >= 8) {
         float maxRadius = 3.0F;
         baseRadius = Math.min((double)maxRadius, Math.max(baseRadius, (double)this.minRadius));
         Queue<SimpleBlock> queue = new ArrayDeque((int)(3.141592653589793D * Math.pow(baseRadius, 2.0D) * ((double)height / 2.5D)));
         queue.add(root);
         HashSet<SimpleBlock> seen = new HashSet();
         seen.add(root);

         while(!queue.isEmpty()) {
            SimpleBlock v = (SimpleBlock)queue.remove();
            v.setType(this.solidBlockType);
            BlockFace[] var10 = BlockUtils.sixBlockFaces;
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               BlockFace rel = var10[var12];
               SimpleBlock neighbour = v.getRelative(rel);
               if (!seen.contains(neighbour)) {
                  int yOffset = neighbour.getY() - root.getY();
                  if ((!facingUp || yOffset <= height && yOffset >= 0) && (facingUp || yOffset >= -height && yOffset <= 0)) {
                     double coneEqn = facingUp ? Math.pow((double)(neighbour.getX() - root.getX()), 2.0D) + Math.pow((double)(neighbour.getZ() - root.getZ()), 2.0D) - Math.pow((double)(yOffset - height) / ((double)height / baseRadius), 2.0D) : Math.pow((double)(neighbour.getX() - root.getX()), 2.0D) + Math.pow((double)(neighbour.getZ() - root.getZ()), 2.0D) - Math.pow((double)(yOffset + height) / ((double)height / baseRadius), 2.0D);
                     if (!(coneEqn > 0.0D)) {
                        queue.add(neighbour);
                        seen.add(neighbour);
                     }
                  }
               }
            }
         }

      }
   }
}
