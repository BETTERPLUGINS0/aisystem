package org.terraform.structure.room;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class LegacyPathGenerator {
   @NotNull
   final HashSet<PathPopulatorData> path = new HashSet();
   final Random rand;
   final Material[] mat;
   private final int[] upperBound;
   private final int[] lowerBound;
   private final int maxNoBend;
   PathPopulatorAbstract populator;
   private SimpleBlock base;
   private BlockFace dir;
   private int straightInARow = 0;
   private int length = 0;
   private int pathWidth = 3;
   private int pathHeight = 3;
   private boolean dead = false;

   public LegacyPathGenerator(SimpleBlock origin, Material[] mat, @NotNull Random rand, int[] upperBound, int[] lowerBound, int maxNoBend) {
      this.base = origin;
      this.rand = rand;
      this.dir = BlockUtils.directBlockFaces[GenUtils.randInt(rand, 0, 3)];
      this.upperBound = upperBound;
      this.lowerBound = lowerBound;
      this.mat = mat;
      if (maxNoBend != -1) {
         this.maxNoBend = maxNoBend;
      } else {
         this.maxNoBend = (int)((double)(upperBound[0] - lowerBound[0]) * 0.5D);
      }

   }

   public boolean isDead() {
      return this.dead;
   }

   private boolean isOutOfBounds(@NotNull SimpleBlock base) {
      return base.getX() >= this.upperBound[0] + 10 || base.getZ() >= this.upperBound[1] + 10 || base.getX() <= this.lowerBound[0] - 10 || base.getZ() <= this.lowerBound[1] - 10;
   }

   public void die() {
      this.dead = true;
      this.wall();
      PathPopulatorData candidate = new PathPopulatorData(this.base, this.dir, this.pathWidth, false);
      candidate.isEnd = true;
      if (!this.path.add(candidate)) {
         this.path.remove(candidate);
         candidate.isOverlapped = true;
         this.path.add(candidate);
      }

   }

   public void populate() {
      if (this.populator != null) {
         Iterator var1 = this.path.iterator();

         while(var1.hasNext()) {
            PathPopulatorData pathPopulatorData = (PathPopulatorData)var1.next();
            this.populator.populate(pathPopulatorData);
         }
      }

   }

   public void placeNext() {
      if (this.length > this.upperBound[0] - this.lowerBound[0]) {
         this.die();
      } else if (this.isOutOfBounds(this.base)) {
         this.die();
      } else {
         BlockFace oldDir = this.dir;
         if (this.length % (1 + 2 * this.pathWidth) == 0) {
            while(true) {
               int cover;
               int i;
               if (!this.isOutOfBounds(this.base.getRelative(this.dir))) {
                  ++this.straightInARow;
                  if (this.straightInARow > this.maxNoBend || GenUtils.chance(this.rand, 1, 500)) {
                     this.straightInARow = 0;
                     cover = this.pathWidth - 1;
                     if (cover == 0) {
                        cover = 1;
                     }

                     for(i = 0; i < cover; ++i) {
                        this.setHall();
                        this.base = this.base.getRelative(this.dir);
                     }

                     for(i = 0; i < cover; ++i) {
                        this.base = this.base.getRelative(this.dir.getOppositeFace());
                     }

                     this.dir = BlockUtils.getTurnBlockFace(this.rand, this.dir);
                  }
                  break;
               }

               this.straightInARow = 0;
               cover = this.pathWidth - 1;
               if (cover == 0) {
                  cover = 1;
               }

               for(i = 0; i < cover; ++i) {
                  this.setHall();
                  this.base = this.base.getRelative(this.dir);
               }

               for(i = 0; i < cover; ++i) {
                  this.base = this.base.getRelative(this.dir.getOppositeFace());
               }

               this.dir = BlockUtils.getTurnBlockFace(this.rand, this.dir);
            }
         }

         if (!this.populator.customCarve(this.base, this.dir, this.pathWidth)) {
            this.setHall();
         }

         PathPopulatorData candidate = new PathPopulatorData(this.base, this.dir, this.pathWidth, oldDir != this.dir);
         if (!this.path.contains(candidate)) {
            this.path.add(candidate);
         } else {
            this.path.remove(candidate);
            candidate.isOverlapped = true;
            this.path.add(candidate);
         }

         this.base = this.base.getRelative(this.dir);
         ++this.length;
      }
   }

   private void wall() {
      if (this.mat[0] != Material.BARRIER) {
         for(int h = 1; h <= this.pathHeight; ++h) {
            if (this.base.getRelative(0, h, 0).getType() != Material.CAVE_AIR) {
               this.base.getRelative(0, h, 0).setType((Material)GenUtils.randChoice((Object[])this.mat));
            }
         }

         BlockFace[] var8 = BlockUtils.getAdjacentFaces(this.dir);
         int var2 = var8.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            BlockFace f = var8[var3];
            SimpleBlock rel = this.base;

            for(int i = 0; i <= this.pathWidth / 2; ++i) {
               rel = rel.getRelative(f);

               for(int h = 1; h <= this.pathHeight; ++h) {
                  if (rel.getRelative(0, h, 0).getType() != Material.CAVE_AIR) {
                     rel.getRelative(0, h, 0).setType((Material)GenUtils.randChoice((Object[])this.mat));
                  }
               }
            }
         }

      }
   }

   private void setHall() {
      if (this.mat[0] != Material.BARRIER) {
         if (this.base.getType() != Material.CAVE_AIR) {
            this.base.setType((Material)GenUtils.randChoice((Object[])this.mat));
         }

         Wall w = (new Wall(this.base)).getUp();
         w.Pillar(this.pathHeight, this.rand, new Material[]{Material.CAVE_AIR});
         if (this.base.getRelative(0, this.pathHeight + 1, 0).getType() != Material.CAVE_AIR) {
            this.base.getRelative(0, this.pathHeight + 1, 0).setType((Material)GenUtils.randChoice((Object[])this.mat));
         }

         BlockFace[] var2 = BlockUtils.getAdjacentFaces(this.dir);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BlockFace f = var2[var4];
            SimpleBlock rel = this.base;

            for(int i = 0; i <= this.pathWidth / 2; ++i) {
               rel = rel.getRelative(f);
               if (i == this.pathWidth / 2) {
                  for(int h = 1; h <= this.pathHeight; ++h) {
                     if (rel.getRelative(0, h, 0).getType() != Material.CAVE_AIR) {
                        rel.getRelative(0, h, 0).setType((Material)GenUtils.randChoice((Object[])this.mat));
                     }
                  }
               } else {
                  if (rel.getType() != Material.CAVE_AIR) {
                     rel.setType((Material)GenUtils.randChoice((Object[])this.mat));
                  }

                  w = (new Wall(rel)).getUp();
                  w.Pillar(this.pathHeight, this.rand, new Material[]{Material.CAVE_AIR});
                  if (rel.getRelative(0, this.pathHeight + 1, 0).getType() != Material.CAVE_AIR) {
                     rel.getRelative(0, this.pathHeight + 1, 0).setType((Material)GenUtils.randChoice((Object[])this.mat));
                  }
               }
            }
         }

      }
   }

   public void generateStraightPath(@Nullable SimpleBlock start, @Nullable BlockFace direction, int length) {
      ArrayList<PathPopulatorData> pathPopulatorDatas = new ArrayList();
      if (direction == null) {
         direction = this.dir;
      }

      if (start == null) {
         start = this.base;
      }

      for(int i = 0; i < length; ++i) {
         if (!this.populator.customCarve(start, direction, this.pathWidth)) {
            this.setHall();
         }

         start = start.getRelative(direction);
         pathPopulatorDatas.add(new PathPopulatorData(start, direction, this.pathWidth, false));
      }

      if (this.populator != null) {
         Iterator var7 = pathPopulatorDatas.iterator();

         while(var7.hasNext()) {
            PathPopulatorData pathPopulatorData = (PathPopulatorData)var7.next();
            this.populator.populate(pathPopulatorData);
         }
      }

   }

   public void setPopulator(@NotNull PathPopulatorAbstract populator) {
      this.populator = populator;
      this.pathWidth = populator.getPathWidth();
      this.pathHeight = populator.getPathHeight();
   }
}
