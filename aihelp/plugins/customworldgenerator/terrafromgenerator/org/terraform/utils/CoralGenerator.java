package org.terraform.utils;

import java.util.Random;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.block.data.type.SeaPickle;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.main.config.TConfig;

public class CoralGenerator {
   public static final Material[] CORAL_BLOCKS;
   public static final Material[] CORAL_FANS;
   public static final Material[] CORAL_WALL_FANS;
   public static final BlockFace[] FACES;

   public static void generateSingleCoral(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      BlockFace face = getRandomBlockFace();
      if (face == BlockFace.DOWN) {
         face = BlockFace.UP;
      }

      Material coral = CORAL_FANS[GenUtils.randInt(0, CORAL_FANS.length - 1)];
      if (face != BlockFace.UP) {
         coral = CORAL_WALL_FANS[GenUtils.randInt(0, CORAL_WALL_FANS.length - 1)];
      }

      attemptReplace(data, x + face.getModX(), y + face.getModY(), z + face.getModZ(), coral);
      if (face != BlockFace.UP) {
         BlockData var7 = data.getBlockData(x + face.getModX(), y + face.getModY(), z + face.getModZ());
         if (var7 instanceof CoralWallFan) {
            CoralWallFan bdata = (CoralWallFan)var7;
            bdata.setFacing(face);
            data.setBlockData(x + face.getModX(), y + face.getModY(), z + face.getModZ(), bdata);
         }
      }

   }

   public static boolean isSaturatedCoral(@NotNull SimpleBlock block) {
      BlockFace[] var1 = BlockUtils.directBlockFaces;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlockFace face = var1[var3];
         if (block.getRelative(face).getType() == Material.WATER) {
            return true;
         }
      }

      return false;
   }

   public static void generateSingleCoral(@NotNull PopulatorDataAbstract data, int x, int y, int z, String coralType) {
      BlockFace face = getRandomBlockFace();
      coralType = StringUtils.remove(coralType, "_BLOCK");
      if (face == BlockFace.DOWN) {
         face = BlockFace.UP;
      }

      Material coral = Material.getMaterial(coralType + "_FAN");
      if ((new Random()).nextBoolean()) {
         coral = Material.getMaterial(coralType);
      }

      if (face != BlockFace.UP) {
         coral = Material.getMaterial(coralType + "_WALL_FAN");
      } else if (GenUtils.chance(1, 5)) {
         generateSeaPickles(data, x, y + 1, z);
         return;
      }

      attemptReplace(data, x + face.getModX(), y + face.getModY(), z + face.getModZ(), coral);
      if (face != BlockFace.UP) {
         BlockData var8 = data.getBlockData(x + face.getModX(), y + face.getModY(), z + face.getModZ());
         if (var8 instanceof CoralWallFan) {
            CoralWallFan bdata = (CoralWallFan)var8;
            bdata.setFacing(face);
            data.setBlockData(x + face.getModX(), y + face.getModY(), z + face.getModZ(), bdata);
         }
      }

   }

   public static void generateSeaPickles(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.arePlantsEnabled()) {
         int fullSize = GenUtils.randInt(1, 4);
         if (attemptReplace(data, x, y, z, Material.SEA_PICKLE)) {
            BlockData var6 = data.getBlockData(x, y, z);
            if (var6 instanceof SeaPickle) {
               SeaPickle state = (SeaPickle)var6;
               state.setPickles(fullSize);
               data.setBlockData(x, y, z, state);
            }
         }

      }
   }

   public static void generateKelpGrowth(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.arePlantsEnabled()) {
         int fullSize = GenUtils.randInt(1, 2);
         if ((new Random()).nextBoolean()) {
            fullSize += GenUtils.randInt(1, 20);
         }

         if (fullSize == 1) {
            attemptReplace(data, x, y, z, Material.SEAGRASS);
         } else if (fullSize == 2 && y < TerraformGenerator.seaLevel - 3) {
            BlockUtils.setDoublePlant(data, x, y, z, Material.TALL_SEAGRASS);
         } else {
            for(int size = 0; size < fullSize && attemptReplace(data, x, y, z, Material.KELP_PLANT); ++size) {
               ++y;
            }
         }

      }
   }

   public static boolean attemptReplace(@NotNull PopulatorDataAbstract data, int x, int y, int z, Material newType) {
      if (y >= TerraformGenerator.seaLevel) {
         return false;
      } else {
         Material type = data.getType(x, y, z);
         if (type != Material.WATER && type != Material.SEAGRASS && type != Material.TALL_SEAGRASS && type != Material.KELP_PLANT) {
            return false;
         } else {
            data.setType(x, y, z, newType);
            return true;
         }
      }
   }

   public static void generateCoral(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      Material coral = CORAL_BLOCKS[GenUtils.randInt(0, CORAL_BLOCKS.length - 1)];
      int fullSize = GenUtils.randInt(15, 35);
      int[] middle = new int[]{x, y, z};

      for(int size = 0; size < fullSize; ++size) {
         if (attemptReplace(data, middle[0], middle[1], middle[2], coral)) {
            if (GenUtils.randInt(0, 100) < 20) {
               generateSeaPickles(data, middle[0], middle[1] + 1, middle[2]);
            }

            if (GenUtils.randInt(0, 100) < 40) {
               generateSingleCoral(data, middle[0], middle[1], middle[2]);
            }
         }

         getRandomRelative(middle);
      }

   }

   public static void generateSponge(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      int fullSize = GenUtils.randInt(15, 35);
      int[] middle = new int[]{x, y, z};

      for(int size = 0; size < fullSize; ++size) {
         if (attemptReplace(data, middle[0], middle[1], middle[2], Material.WET_SPONGE) && GenUtils.randInt(0, 100) < 20) {
            generateSeaPickles(data, middle[0], middle[1] + 1, middle[2]);
         }

         getRandomRelative(middle);
      }

   }

   public static void getRandomRelative(@NotNull int[] middle) {
      BlockFace face = getRandomBlockFace();
      middle[0] += face.getModX();
      middle[1] += face.getModY();
      middle[2] += face.getModZ();
   }

   public static BlockFace getRandomBlockFace() {
      return FACES[GenUtils.randInt(0, 13)];
   }

   static {
      CORAL_BLOCKS = new Material[]{Material.BRAIN_CORAL_BLOCK, Material.BUBBLE_CORAL_BLOCK, Material.FIRE_CORAL_BLOCK, Material.HORN_CORAL_BLOCK, Material.TUBE_CORAL_BLOCK};
      CORAL_FANS = new Material[]{Material.BRAIN_CORAL_FAN, Material.BUBBLE_CORAL_FAN, Material.FIRE_CORAL_FAN, Material.HORN_CORAL_FAN, Material.TUBE_CORAL_FAN};
      CORAL_WALL_FANS = new Material[]{Material.BRAIN_CORAL_WALL_FAN, Material.BUBBLE_CORAL_WALL_FAN, Material.FIRE_CORAL_WALL_FAN, Material.HORN_CORAL_WALL_FAN, Material.TUBE_CORAL_WALL_FAN};
      FACES = new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN};
   }
}
