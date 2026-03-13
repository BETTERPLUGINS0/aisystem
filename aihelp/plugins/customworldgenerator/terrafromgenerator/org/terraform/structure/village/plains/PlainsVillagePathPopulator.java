package org.terraform.structure.village.plains;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class PlainsVillagePathPopulator extends PathPopulatorAbstract {
   final TerraformWorld tw;
   private final Random random;
   private final Collection<DirectionalCubeRoom> knownRooms;

   public PlainsVillagePathPopulator(TerraformWorld tw, Collection<DirectionalCubeRoom> collection, Random rand) {
      this.tw = tw;
      this.random = rand;
      this.knownRooms = collection;
   }

   public static void placeLamp(@NotNull Random rand, @NotNull SimpleBlock b) {
      b.setType((Material)GenUtils.randChoice(rand, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS));
      b.getUp().setType((Material)GenUtils.randChoice(rand, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
      b.getUp(2).setType((Material)GenUtils.randChoice(rand, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
      b.getUp(3).setType((Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE));
      b.getUp(4).setType(Material.CAMPFIRE);
      b.getUp(5).setType((Material)GenUtils.randChoice(rand, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS));
      BlockFace[] var2 = BlockUtils.directBlockFaces;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         Slab tSlab = (Slab)Bukkit.createBlockData((Material)GenUtils.randChoice(rand, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB));
         tSlab.setType(Type.TOP);
         b.getRelative(face).getUp(3).setBlockData(tSlab);
         b.getRelative(face).getUp(4).setType((Material)GenUtils.randChoice(rand, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
         b.getRelative(face).getUp(5).setType((Material)GenUtils.randChoice(rand, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB));
      }

   }

   public static boolean canPlaceLamp(@NotNull SimpleBlock target) {
      if (target.getType() == Material.WATER) {
         return false;
      } else {
         BlockFace[] var1 = BlockUtils.xzPlaneBlockFaces;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            BlockFace face = var1[var3];

            for(int i = 0; i < 6; ++i) {
               if (target.getRelative(face).getRelative(0, i, 0).isSolid()) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public void populate(@NotNull PathPopulatorData ppd) {
      ppd.base = new SimpleBlock(ppd.base.getPopData(), ppd.base.getX(), GenUtils.getHighestGround(ppd.base.getPopData(), ppd.base.getX(), ppd.base.getZ()), ppd.base.getZ());
      Wall pathCore;
      if (BlockUtils.isWet(ppd.base.getUp())) {
         pathCore = (new Wall(ppd.base, ppd.dir)).getAtY(TerraformGenerator.seaLevel);
         if (BlockUtils.getAxisFromBlockFace(ppd.dir) == Axis.X && ppd.base.getX() % 2 == 0 || BlockUtils.getAxisFromBlockFace(ppd.dir) == Axis.Z && ppd.base.getZ() % 2 == 0) {
            pathCore.getDown().downLPillar(this.random, 50, new Material[]{Material.OAK_LOG});
            pathCore.setType(Material.CHISELED_STONE_BRICKS);
         }

      } else {
         pathCore = new Wall(ppd.base, ppd.dir);
         BlockFace[] var3 = BlockUtils.getAdjacentFaces(ppd.dir);
         int var4 = var3.length;

         int i;
         for(int var5 = 0; var5 < var4; ++var5) {
            BlockFace face = var3[var5];

            for(i = 0; i < 4; ++i) {
               Wall target = pathCore.getRelative(face, i).getGround();
               if (!target.getUp().isSolid() && target.getUp().getType() != Material.WATER && BlockUtils.isDirtLike(target.getType()) && target.getType() != Material.DIRT_PATH) {
                  if (GenUtils.chance(2, 5)) {
                     PlantBuilder.OAK_LEAVES.build(target.getUp());
                  } else if (GenUtils.chance(1, 5)) {
                     BlockUtils.pickTallFlower().build(target);
                  } else if (GenUtils.chance(1, 10)) {
                     target.getUp().setType(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
                     if (TConfig.areDecorationsEnabled()) {
                        target.getUp(2).setType(Material.LANTERN);
                     }
                  }
                  break;
               }
            }
         }

         if (GenUtils.chance(this.random, 1, 15)) {
            BlockFace side = BlockUtils.getTurnBlockFace(this.random, ppd.dir);
            SimpleBlock target = new SimpleBlock(ppd.base.getPopData(), ppd.base.getX() + side.getModX() * 3, GenUtils.getHighestGround(ppd.base.getPopData(), ppd.base.getX() + side.getModX() * 3, ppd.base.getZ() + side.getModZ() * 3), ppd.base.getZ() + side.getModZ() * 3);
            if (target.getType() == Material.DIRT_PATH) {
               return;
            }

            BlockFace[] var11 = BlockUtils.xzPlaneBlockFaces;
            int var13 = var11.length;

            for(i = 0; i < var13; ++i) {
               BlockFace face = var11[i];
               if (target.getRelative(face).getGround().getUp().isSolid()) {
                  return;
               }
            }

            Iterator var12 = this.knownRooms.iterator();

            while(var12.hasNext()) {
               CubeRoom room = (CubeRoom)var12.next();
               if (room.isPointInside(target)) {
                  return;
               }
            }

            placeLamp(this.random, target.getUp());
         }

      }
   }
}
