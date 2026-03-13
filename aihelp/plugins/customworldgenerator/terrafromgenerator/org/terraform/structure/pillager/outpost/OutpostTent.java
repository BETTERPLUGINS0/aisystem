package org.terraform.structure.pillager.outpost;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.WoodUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class OutpostTent extends RoomPopulatorAbstract {
   @NotNull
   final Material[] edgyWools;
   final BiomeBank biome;

   public OutpostTent(Random rand, boolean forceSpawn, boolean unique, BiomeBank biome) {
      super(rand, forceSpawn, unique);
      this.edgyWools = new Material[]{Material.BLACK_WOOL, Material.GRAY_WOOL, Material.BROWN_WOOL, Material.LIGHT_GRAY_WOOL, Material.WHITE_WOOL};
      this.biome = biome;
   }

   private void placeProp(int size, @NotNull SimpleBlock core, @NotNull BlockFace facing, @NotNull Material cloth) {
      Material fenceMat = WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.FENCE);
      (new Wall(core)).Pillar(size, this.rand, new Material[]{fenceMat});
      BlockFace[] var6 = BlockUtils.getAdjacentFaces(facing);
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         BlockFace face = var6[var8];
         SimpleBlock corner = core.getRelative(face, size - 1);
         corner.getRelative(facing.getOppositeFace()).setType(cloth);
         corner.getRelative(facing.getOppositeFace()).getUp().setType(fenceMat);
         StairBuilder builder = new StairBuilder(WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.STAIRS));
         builder.setFacing(face.getOppositeFace()).apply(corner).setFacing(face).setHalf(Half.TOP).apply(corner.getRelative(face.getOppositeFace()));
         corner = corner.getRelative(face.getOppositeFace()).getUp();
         builder.setHalf(Half.BOTTOM).setFacing(face.getOppositeFace());

         for(int i = 0; i < size - 2; ++i) {
            builder.apply(corner);

            for(int j = corner.getY() - 1; j > core.getY() - 1; --j) {
               if (!Tag.BEDS.isTagged(corner.getAtY(j).getType()) && !Tag.STAIRS.isTagged(corner.getAtY(j).getType())) {
                  corner.getAtY(j).setType(Material.AIR);
               }
            }

            SimpleBlock target = corner.getRelative(facing.getOppositeFace());
            target.setType(cloth);

            for(int j = target.getY() - 1; j > core.getY() - 1; --j) {
               if (!Tag.BEDS.isTagged(target.getAtY(j).getType())) {
                  target.getAtY(j).setType(Material.AIR);
               }
            }

            target.getUp().setType(fenceMat);
            corner = corner.getUp().getRelative(face.getOppositeFace());
         }
      }

   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      Material planks = WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.PLANKS);
      Material fence = WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.FENCE);
      Material cloth = (Material)GenUtils.randChoice(this.rand, this.edgyWools);
      int height = GenUtils.randInt(this.rand, 4, 6);
      int length = GenUtils.randInt(this.rand, 5, 9);
      BlockFace facing = BlockUtils.getDirectBlockFace(this.rand);
      SimpleBlock firstProp = (new SimpleBlock(data, room.getX(), room.getY(), room.getZ())).getRelative(facing, length / 2).getGroundOrSeaLevel();
      this.placeProp(height, firstProp.getUp(), facing, cloth);
      this.placeProp(height, firstProp.getUp().getRelative(facing.getOppositeFace(), length), facing.getOppositeFace(), cloth);
      Wall wallProp = new Wall(firstProp, facing);

      int relLen;
      for(int relWidth = 0; relWidth < height; ++relWidth) {
         for(relLen = 0; relLen <= length; ++relLen) {
            Wall target = wallProp.getLeft(relWidth).getRelative(facing.getOppositeFace(), relLen);
            target.get().lsetType(planks);
            target.getDown().downUntilSolid(this.getRand(), new Material[]{fence});
            target = wallProp.getRight(relWidth).getRelative(facing.getOppositeFace(), relLen);
            target.get().lsetType(planks);
            target.getDown().downUntilSolid(this.getRand(), new Material[]{fence});
         }
      }

      OrientableBuilder ob = (new OrientableBuilder(WoodUtils.getWoodForBiome(this.biome, WoodUtils.WoodType.LOG))).setAxis(BlockUtils.getAxisFromBlockFace(facing));
      wallProp = wallProp.getRelative(0, height, 0);

      int i;
      for(relLen = 0; relLen <= length; ++relLen) {
         ob.apply(wallProp);
         if (relLen != 0 && relLen != length) {
            for(i = wallProp.getY() - 1; i > firstProp.getY(); --i) {
               if (!Tag.BEDS.isTagged(wallProp.getAtY(i).getType())) {
                  wallProp.getAtY(i).setType(Material.AIR);
               }
            }
         }

         wallProp = wallProp.getRelative(facing.getOppositeFace());
      }

      BlockFace[] var21 = BlockUtils.getAdjacentFaces(facing);
      i = var21.length;

      for(int var14 = 0; var14 < i; ++var14) {
         BlockFace face = var21[var14];

         for(int relLen = 2; relLen <= length - 2; ++relLen) {
            SimpleBlock corner = firstProp.getRelative(face, height - 1).getRelative(facing.getOppositeFace(), relLen).getUp();

            for(int relWidth = height - 2; relWidth >= 0; --relWidth) {
               for(int i = corner.getY(); i > firstProp.getY(); --i) {
                  if (GenUtils.isGroundLike(corner.getAtY(i).getType())) {
                     corner.getAtY(i).setType(Material.AIR);
                  }
               }

               if (relWidth == height - 2) {
                  corner.setType(cloth);
                  SimpleBlock target = corner.getRelative(face.getOppositeFace());
                  if (!target.isSolid() && GenUtils.chance(this.rand, 1, 2)) {
                     switch(this.rand.nextInt(4)) {
                     case 0:
                        target.setType(Material.CRAFTING_TABLE);
                        break;
                     case 1:
                        target.setType(Material.FLETCHING_TABLE);
                        break;
                     case 2:
                        (new ChestBuilder(Material.CHEST)).setFacing(face.getOppositeFace()).setLootTable(TerraLootTable.PILLAGER_OUTPOST).apply(target);
                        break;
                     case 3:
                        BlockUtils.placeBed(target, BlockUtils.pickBed(), face.getOppositeFace());
                     }
                  }

                  if (GenUtils.chance(this.rand, 1, 4)) {
                     target.getRelative(face.getOppositeFace()).addEntity(EntityType.PILLAGER);
                  }
               }

               corner.getUp().setType(cloth);
               corner = corner.getRelative(face.getOppositeFace()).getUp();
            }
         }
      }

   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
