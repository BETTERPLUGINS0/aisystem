package org.terraform.structure.stronghold;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.StairBuilder;

public class HallwayPopulator extends RoomPopulatorAbstract {
   public HallwayPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      Iterator var3 = room.getFourWalls(data, 1).entrySet().iterator();

      Entry entry;
      Wall w;
      int j;
      while(var3.hasNext()) {
         entry = (Entry)var3.next();
         w = (Wall)entry.getKey();
         boolean wasAir = false;

         for(j = 0; j < (Integer)entry.getValue(); ++j) {
            (new StairBuilder(new Material[]{Material.STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS})).setFacing(w.getDirection().getOppositeFace()).setHalf(Half.TOP).apply(w.getUp(4));
            w.getUp(5).LPillar(room.getHeight(), this.rand, BlockUtils.stoneBricks);
            if (!w.getRear().getUp().isSolid()) {
               wasAir = true;
               w.getUp(5).setType(new Material[]{Material.CHISELED_STONE_BRICKS, Material.CHISELED_STONE_BRICKS, Material.COBBLESTONE});
            } else {
               if (wasAir || !w.getLeft().getRear().getUp().isSolid()) {
                  w.getRear().Pillar(5, this.rand, new Material[]{Material.STONE, Material.SMOOTH_STONE});
               }

               wasAir = false;
            }

            w = w.getLeft();
         }
      }

      var3 = room.getFourWalls(data, 1).entrySet().iterator();

      int j;
      while(var3.hasNext()) {
         entry = (Entry)var3.next();
         w = ((Wall)entry.getKey()).getRelative(0, room.getHeight() - 1, 0);

         for(j = 0; j < (Integer)entry.getValue(); ++j) {
            (new StairBuilder(new Material[]{Material.ANDESITE_STAIRS, Material.STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS})).setFacing(w.getDirection().getOppositeFace()).setHalf(Half.TOP).apply(w);
            w = w.getLeft();
         }
      }

      int[][] var9 = room.getAllCorners(1);
      int var11 = var9.length;

      for(int var13 = 0; var13 < var11; ++var13) {
         int[] coords = var9[var13];
         (new Wall(new SimpleBlock(data, coords[0], room.getY() + 1, coords[1]))).Pillar(room.getHeight() - 1, this.rand, BlockUtils.stoneBricks);
      }

      for(int i = 0; i < GenUtils.randInt(this.rand, room.getWidthX(), room.getWidthX() * room.getWidthZ() / 10); ++i) {
         int[] randomCoords = room.randomCoords(this.rand, 1);
         SimpleBlock ceil = new SimpleBlock(data, randomCoords[0], room.getY() + room.getHeight(), randomCoords[2]);
         if (GenUtils.chance(this.rand, 4, 25)) {
            for(j = 0; j < GenUtils.randInt(this.rand, 1, 5); ++j) {
               this.dropDownBlock(ceil.getRelative(GenUtils.randInt(this.rand, -1, 1), 0, GenUtils.randInt(this.rand, -1, 1)));
            }
         }

         if (GenUtils.chance(this.rand, 1, 5)) {
            SimpleBlock webBase = ceil.getDown();
            webBase.setType(Material.COBWEB);

            for(j = 0; j < GenUtils.randInt(this.rand, 0, 3); ++j) {
               BlockFace face = CoralGenerator.getRandomBlockFace();
               if (face == BlockFace.UP) {
                  face = BlockFace.SELF;
               }

               if (!webBase.getRelative(face).isSolid()) {
                  webBase.getRelative(face).setType(Material.COBWEB);
               }
            }
         }
      }

   }

   private void dropDownBlock(@NotNull SimpleBlock block) {
      if (block.isSolid()) {
         BlockData type = block.getBlockData();
         block.setType(Material.CAVE_AIR);
         int depth = 0;

         while(!block.isSolid()) {
            block = block.getDown();
            ++depth;
            if (depth > 50) {
               return;
            }
         }

         if (type instanceof Slab) {
            ((Slab)type).setType(Type.BOTTOM);
         } else if (type instanceof Stairs) {
            ((Stairs)type).setHalf(Half.BOTTOM);
         }

         if (GenUtils.chance(1, 3)) {
            block.getUp().setBlockData(BlockUtils.infestStone(type));
         } else {
            block.getUp().setBlockData(type);
         }
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return !(new PrisonRoomPopulator(new Random(), false, false)).canPopulate(room) && room.isBig() && !room.isHuge();
   }
}
