package org.terraform.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Candle;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Bed.Part;
import org.bukkit.block.data.type.PointedDripstone.Thickness;
import org.bukkit.block.data.type.Stairs.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleChunkLocation;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.blockdata.StairBuilder;
import org.terraform.utils.blockdata.fixers.v1_16_R1_BlockDataFixer;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.version.V_1_19;
import org.terraform.utils.version.V_1_20;
import org.terraform.utils.version.V_1_21_5;
import org.terraform.utils.version.Version;

public class BlockUtils {
   public static final EnumSet<Material> replacableByTrees;
   public static final BlockFace[] xzPlaneBlockFaces;
   public static final EnumSet<Material> fluids;
   public static final EnumSet<Material> wetMaterials;
   public static final EnumSet<Material> amethysts;
   public static final BlockFace[] flatBlockFaces3x3;
   public static final BlockFace[] BLOCK_FACES;
   public static final BlockFace[] xzDiagonalPlaneBlockFaces;
   public static final Material[] stoneBricks;
   public static final Material[] stoneBrickSlabs;
   public static final BlockFace[] directBlockFaces;
   public static final BlockFace[][] cornerBlockFaces;
   public static final BlockFace[] sixBlockFaces;
   public static final EnumSet<Material> stoneLike;
   public static final EnumSet<Material> caveDecoratorMaterials;
   public static final EnumSet<Material> badlandsStoneLike;
   public static final EnumSet<Material> caveCarveReplace;
   public static final EnumSet<Material> ores;
   public static final EnumSet<Material> airs;
   public static final EnumSet<Material> glassPanes;
   public static final Material[] WOOLS;
   public static final Material[] GLAZED_TERRACOTTA;
   public static final Material[] TERRACOTTA;
   private static final PlantBuilder[] TALL_FLOWER;
   private static final PlantBuilder[] FLOWER;
   private static final PlantBuilder[] POTTED;
   private static final Material[] CARPETS;
   private static final Material[] BED;
   private static final HashMap<String, BlockData> deepslateMap;

   public static void initBlockUtils() {
      Material[] var0 = Material.values();
      int var1 = var0.length;

      int var2;
      Material mat;
      for(var2 = 0; var2 < var1; ++var2) {
         mat = var0[var2];
         if (mat.toString().endsWith("_ORE")) {
            if (!mat.toString().contains("NETHER")) {
               ores.add(mat);
            }

            stoneLike.add(mat);
         }
      }

      if (Version.VERSION.isAtLeast(Version.v1_19_4)) {
         caveDecoratorMaterials.add(V_1_19.SCULK);
         caveDecoratorMaterials.add(V_1_19.SCULK_SENSOR);
         caveDecoratorMaterials.add(V_1_19.SCULK_SHRIEKER);
         caveDecoratorMaterials.add(V_1_19.SCULK_VEIN);
         caveDecoratorMaterials.add(V_1_19.SCULK_CATALYST);
      }

      badlandsStoneLike.addAll(stoneLike);
      caveCarveReplace.addAll(badlandsStoneLike);
      caveCarveReplace.addAll(caveDecoratorMaterials);
      PlantBuilder[] var4 = FLOWER;
      var1 = var4.length;

      for(var2 = 0; var2 < var1; ++var2) {
         PlantBuilder pb = var4[var2];
         replacableByTrees.add(pb.material);
      }

      replacableByTrees.addAll(Tag.SAPLINGS.getValues());
      var0 = Material.values();
      var1 = var0.length;

      for(var2 = 0; var2 < var1; ++var2) {
         mat = var0[var2];
         if (mat.toString().endsWith("_GLASS_PANE")) {
            glassPanes.add(mat);
         }
      }

   }

   public static boolean isDirectBlockFace(@NotNull BlockFace facing) {
      boolean var10000;
      switch(facing) {
      case NORTH:
      case SOUTH:
      case EAST:
      case WEST:
         var10000 = true;
         break;
      default:
         var10000 = false;
      }

      return var10000;
   }

   @NotNull
   public static BlockFace rotateFace(@NotNull BlockFace original, int times) {
      for(int i = 0; i < times; ++i) {
         switch(original) {
         case NORTH:
            original = BlockFace.EAST;
            break;
         case SOUTH:
            original = BlockFace.WEST;
            break;
         case EAST:
            original = BlockFace.SOUTH;
            break;
         case WEST:
            original = BlockFace.NORTH;
         }
      }

      return original;
   }

   @NotNull
   public static BlockFace rotateXZPlaneBlockFace(@NotNull BlockFace original, int times) {
      for(int i = 0; i < times; ++i) {
         switch(original) {
         case NORTH:
            original = BlockFace.NORTH_EAST;
            break;
         case SOUTH:
            original = BlockFace.SOUTH_WEST;
            break;
         case EAST:
            original = BlockFace.SOUTH_EAST;
            break;
         case WEST:
            original = BlockFace.NORTH_WEST;
            break;
         case NORTH_EAST:
            original = BlockFace.EAST;
            break;
         case SOUTH_EAST:
            original = BlockFace.SOUTH;
            break;
         case SOUTH_WEST:
            original = BlockFace.WEST;
            break;
         case NORTH_WEST:
            original = BlockFace.NORTH;
         }
      }

      return original;
   }

   @NotNull
   public static BlockFace rotateXZPlaneBlockFaceOppositeAngle(@NotNull BlockFace original, int times) {
      for(int i = 0; i < times; ++i) {
         switch(original) {
         case NORTH:
            original = BlockFace.NORTH_EAST;
            break;
         case SOUTH:
            original = BlockFace.SOUTH_WEST;
            break;
         case EAST:
            original = BlockFace.SOUTH_EAST;
            break;
         case WEST:
            original = BlockFace.NORTH_WEST;
            break;
         case NORTH_EAST:
            original = BlockFace.EAST;
            break;
         case SOUTH_EAST:
            original = BlockFace.SOUTH;
            break;
         case SOUTH_WEST:
            original = BlockFace.WEST;
            break;
         case NORTH_WEST:
            original = BlockFace.NORTH;
         }
      }

      return original;
   }

   @NotNull
   public static BlockFace[] getRandomBlockfaceAxis(@NotNull Random rand) {
      return rand.nextInt(2) == 0 ? new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH} : new BlockFace[]{BlockFace.WEST, BlockFace.EAST};
   }

   public static Material stoneBrick(@NotNull Random rand) {
      return (Material)GenUtils.randChoice(rand, stoneBricks);
   }

   public static Material stoneBrickSlab(@NotNull Random rand) {
      return (Material)GenUtils.randChoice(rand, stoneBrickSlabs);
   }

   public static BlockFace getXZPlaneBlockFace(@NotNull Random rand) {
      return xzPlaneBlockFaces[rand.nextInt(8)];
   }

   @Nullable
   public static BlockFace getBlockFaceFromAxis(@NotNull Axis ax) {
      BlockFace var10000;
      switch(ax) {
      case X:
         var10000 = BlockFace.EAST;
         break;
      case Z:
         var10000 = BlockFace.SOUTH;
         break;
      case Y:
         var10000 = BlockFace.UP;
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   @NotNull
   public static Axis getAxisFromBlockFace(@NotNull BlockFace face) {
      Axis var10000;
      switch(face) {
      case NORTH:
      case SOUTH:
         var10000 = Axis.Z;
         break;
      case EAST:
      case WEST:
         var10000 = Axis.X;
         break;
      case NORTH_EAST:
      case SOUTH_EAST:
      case SOUTH_WEST:
      case NORTH_WEST:
      default:
         throw new IllegalArgumentException("Invalid block facing for axis: " + String.valueOf(face));
      case UP:
      case DOWN:
         var10000 = Axis.Y;
      }

      return var10000;
   }

   @NotNull
   public static Axis getPerpendicularHorizontalPlaneAxis(@NotNull Axis x) {
      Axis var10000;
      switch(x) {
      case X:
         var10000 = Axis.Z;
         break;
      case Z:
         var10000 = Axis.X;
         break;
      default:
         var10000 = x;
      }

      return var10000;
   }

   public static BlockFace getDirectBlockFace(@NotNull Random rand) {
      return directBlockFaces[rand.nextInt(4)];
   }

   public static BlockFace getSixBlockFace(@NotNull Random rand) {
      return sixBlockFaces[rand.nextInt(6)];
   }

   public static Material pickCarpet() {
      return (Material)GenUtils.randChoice((Object[])CARPETS);
   }

   public static Material pickWool() {
      return (Material)GenUtils.randChoice((Object[])WOOLS);
   }

   public static Material pickBed() {
      return (Material)GenUtils.randChoice((Object[])BED);
   }

   public static PlantBuilder pickFlower() {
      return pickFlower(GenUtils.RANDOMIZER);
   }

   public static PlantBuilder pickFlower(Random rand) {
      return (PlantBuilder)GenUtils.randChoice(rand, FLOWER);
   }

   public static PlantBuilder pickPottedPlant() {
      return (PlantBuilder)GenUtils.randChoice((Object[])POTTED);
   }

   public static PlantBuilder pickTallFlower() {
      return (PlantBuilder)GenUtils.randChoice((Object[])TALL_FLOWER);
   }

   public static void dropDownBlock(@NotNull SimpleBlock block) {
      dropDownBlock(block, Material.CAVE_AIR);
   }

   public static void dropDownBlock(@NotNull SimpleBlock block, @NotNull Material fluid) {
      if (block.isSolid()) {
         Material type = block.getType();
         block.setType(fluid);
         int depth = 0;

         while(!block.isSolid()) {
            block = block.getDown();
            ++depth;
            if (depth > 50) {
               return;
            }
         }

         block.getUp().setType(type);
      }

   }

   public static void horizontalGlazedTerracotta(@NotNull PopulatorDataAbstract data, int x, int y, int z, @NotNull Material glazedTerracotta) {
      Directional terracotta = (Directional)Bukkit.createBlockData(glazedTerracotta);
      terracotta.setFacing(BlockFace.NORTH);
      data.setBlockData(x, y, z, terracotta);
      terracotta = (Directional)Bukkit.createBlockData(glazedTerracotta);
      terracotta.setFacing(BlockFace.EAST);
      data.setBlockData(x + 1, y, z, terracotta);
      terracotta = (Directional)Bukkit.createBlockData(glazedTerracotta);
      terracotta.setFacing(BlockFace.WEST);
      data.setBlockData(x, y, z + 1, terracotta);
      terracotta = (Directional)Bukkit.createBlockData(glazedTerracotta);
      terracotta.setFacing(BlockFace.SOUTH);
      data.setBlockData(x + 1, y, z + 1, terracotta);
   }

   public static void setVines(@NotNull PopulatorDataAbstract data, int x, int y, int z, int maxLength) {
      if (TConfig.arePlantsEnabled()) {
         SimpleBlock rel = new SimpleBlock(data, x, y, z);
         BlockFace[] var6 = directBlockFaces;
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            BlockFace face = var6[var8];
            MultipleFacing dir = (MultipleFacing)Bukkit.createBlockData(Material.VINE);
            dir.setFace(face.getOppositeFace(), true);
            SimpleBlock vine = rel.getRelative(face);
            if (!vine.isSolid()) {
               vine.setType(Material.VINE);
               vine.setBlockData(dir);

               for(int i = 0; i < GenUtils.randInt(1, maxLength); ++i) {
                  vine.getRelative(0, -i, 0).setType(Material.VINE);
                  vine.getRelative(0, -i, 0).setBlockData(dir);
               }
            }
         }

      }
   }

   public static double distanceSquared(float x1, float y1, float z1, float x2, float y2, float z2) {
      return Math.pow((double)(x2 - x1), 2.0D) + Math.pow((double)(y2 - y1), 2.0D) + Math.pow((double)(z2 - z1), 2.0D);
   }

   public static void setDownUntilSolid(int x, int y, int z, @NotNull PopulatorDataAbstract data, Material... type) {
      while(!data.getType(x, y, z).isSolid()) {
         data.setType(x, y, z, (Material)GenUtils.randChoice((Object[])type));
         --y;
      }

   }

   public static void downPillar(int x, int y, int z, int height, @NotNull PopulatorDataAbstract data, Material... type) {
      while(!data.getType(x, y, z).isSolid() && height > TerraformGeneratorPlugin.injector.getMinY()) {
         data.setType(x, y, z, (Material)GenUtils.randChoice((Object[])type));
         --height;
         --y;
      }

   }

   public static boolean isStoneLike(@NotNull Material mat) {
      return isDirtLike(mat) || stoneLike.contains(mat) || ores.contains(mat);
   }

   public static boolean isDirtLike(@NotNull Material mat) {
      boolean var10000;
      switch(mat) {
      case DIRT:
      case GRASS_BLOCK:
      case PODZOL:
      case COARSE_DIRT:
      case MYCELIUM:
         var10000 = true;
         break;
      default:
         var10000 = mat == Material.DIRT_PATH || mat == Material.ROOTED_DIRT;
      }

      return var10000;
   }

   public static void setPersistentLeaves(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.arePlantsEnabled()) {
         setPersistentLeaves(data, x, y, z, Material.OAK_LEAVES);
      }
   }

   public static void setPersistentLeaves(@NotNull PopulatorDataAbstract data, int x, int y, int z, @NotNull Material type) {
      if (TConfig.arePlantsEnabled()) {
         data.setType(x, y, z, Material.OAK_LEAVES);
         Leaves bd = (Leaves)Bukkit.createBlockData(type);
         bd.setPersistent(true);
         data.setBlockData(x, y, z, bd);
      }
   }

   public static void setDoublePlant(@NotNull PopulatorDataAbstract data, int x, int y, int z, @NotNull Material doublePlant) {
      if (TConfig.arePlantsEnabled()) {
         if (data.getType(x, y, z) == Material.AIR && data.getType(x, y + 1, z) == Material.AIR) {
            Bisected d = (Bisected)Bukkit.createBlockData(doublePlant);
            d.setHalf(Half.BOTTOM);
            data.lsetBlockData(x, y, z, d);
            d = (Bisected)Bukkit.createBlockData(doublePlant);
            d.setHalf(Half.TOP);
            data.lsetBlockData(x, y + 1, z, d);
         }
      }
   }

   public static boolean isSameChunk(@NotNull Block a, @NotNull Block b) {
      return SimpleChunkLocation.of(a).equals(SimpleChunkLocation.of(b));
   }

   public static boolean areAdjacentChunksLoaded(@NotNull Chunk middle) {
      for(int nx = -1; nx <= 1; ++nx) {
         for(int nz = -1; nz <= 1; ++nz) {
            int x = middle.getX() + nx;
            int z = middle.getZ() + nz;
            if (!middle.getWorld().isChunkLoaded(x, z)) {
               return false;
            }
         }
      }

      return true;
   }

   @NotNull
   public static Material getTerracotta(int height) {
      int mapped = (height + 10) % 17;
      if (mapped != 2 && mapped != 9 && mapped != 13 && mapped != 16) {
         if (mapped != 4 && mapped != 5 && mapped != 12 && mapped != 15) {
            if (mapped == 6) {
               return Material.YELLOW_TERRACOTTA;
            } else {
               return mapped == 8 ? Material.BROWN_TERRACOTTA : Material.ORANGE_TERRACOTTA;
            }
         } else {
            return Material.RED_TERRACOTTA;
         }
      } else {
         return Material.TERRACOTTA;
      }
   }

   public static int spawnPillar(@NotNull Random rand, @NotNull PopulatorDataAbstract data, int x, int y, int z, Material type, int minHeight, int maxHeight) {
      int height = GenUtils.randInt(rand, minHeight, maxHeight);

      for(int i = 0; i < height; ++i) {
         data.setType(x, y + i, z, type);
      }

      return height;
   }

   public static void generateClayDeposit(int x, int y, int z, @NotNull PopulatorDataAbstract data, @NotNull Random random) {
      replaceCircularPatch(random.nextInt(9999), TConfig.c.BIOME_CLAY_DEPOSIT_SIZE, new SimpleBlock(data, x, y, z), Material.CLAY);
   }

   public static void vineUp(@NotNull SimpleBlock base, int maxLength) {
      if (TConfig.arePlantsEnabled()) {
         BlockFace[] var2 = directBlockFaces;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BlockFace face = var2[var4];
            MultipleFacing dir = (MultipleFacing)Bukkit.createBlockData(Material.VINE);
            dir.setFace(face.getOppositeFace(), true);
            SimpleBlock vine = base.getRelative(face);
            if (!vine.isSolid()) {
               vine.setType(Material.VINE);
               vine.setBlockData(dir);

               for(int i = 1; i < GenUtils.randInt(1, maxLength); ++i) {
                  SimpleBlock relative = vine.getRelative(0, -i, 0);
                  if (relative.getType() != Material.AIR) {
                     break;
                  }

                  relative.setType(Material.VINE);
                  relative.setBlockData(dir);
               }
            }
         }

      }
   }

   public static void replaceCircle(int seed, float radius, @NotNull SimpleBlock base, Material... type) {
      if (!(radius <= 0.0F)) {
         if ((double)radius <= 0.5D) {
            base.setType((Material)GenUtils.randChoice(new Random((long)seed), type));
         } else {
            FastNoise noise = new FastNoise(seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.09F);

            for(float x = -radius; x <= radius; ++x) {
               for(float z = -radius; z <= radius; ++z) {
                  SimpleBlock rel = base.getRelative(Math.round(x), 0, Math.round(z));
                  double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)radius, 2.0D);
                  if (equationResult <= 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getZ())) {
                     rel.lsetType((Material)GenUtils.randChoice((Object[])type));
                  }
               }
            }

         }
      }
   }

   public static void replaceCircularPatch(int seed, float radius, @NotNull SimpleBlock base, Material... type) {
      replaceCircularPatch(seed, radius, base, false, type);
   }

   public static void replaceCircularPatch(int seed, float radius, @NotNull SimpleBlock base, boolean snowy, Material... type) {
      if (!(radius <= 0.0F)) {
         if ((double)radius <= 0.5D) {
            base.setType((Material)GenUtils.randChoice(new Random((long)seed), type));
         } else {
            FastNoise noise = new FastNoise(seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.09F);

            for(float x = -radius; x <= radius; ++x) {
               for(float z = -radius; z <= radius; ++z) {
                  SimpleBlock rel = base.getRelative(Math.round(x), 0, Math.round(z));
                  rel = rel.getGround();
                  double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)radius, 2.0D);
                  if (equationResult <= 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getZ())) {
                     rel.setType((Material)GenUtils.randChoice((Object[])type));
                     if (snowy && rel.getUp().isAir()) {
                        rel.getUp().setType(Material.SNOW);
                     }
                  }
               }
            }

         }
      }
   }

   public static void lambdaCircularPatch(int seed, float radius, @NotNull SimpleBlock base, Consumer<SimpleBlock> lambda) {
      if (!(radius <= 0.0F)) {
         if ((double)radius <= 0.5D) {
            lambda.accept(base);
         } else {
            FastNoise noise = new FastNoise(seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.09F);

            for(float x = -radius; x <= radius; ++x) {
               for(float z = -radius; z <= radius; ++z) {
                  SimpleBlock rel = base.getRelative(Math.round(x), 0, Math.round(z));
                  rel = rel.getGround();
                  double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)radius, 2.0D);
                  if (equationResult <= 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getZ())) {
                     lambda.accept(rel);
                  }
               }
            }

         }
      }
   }

   public static void replaceSphere(int seed, float radius, @NotNull SimpleBlock base, boolean hardReplace, Material... type) {
      if (radius > 0.0F) {
         replaceSphere(seed, radius, radius, radius, base, hardReplace, type);
      }

   }

   public static void replaceSphere(int seed, float rX, float rY, float rZ, @NotNull SimpleBlock block, boolean hardReplace, Material... type) {
      replaceSphere(seed, rX, rY, rZ, block, hardReplace, false, type);
   }

   public static void replaceWaterSphere(int seed, float radius, @NotNull SimpleBlock base) {
      if (!(radius <= 0.0F)) {
         if ((double)radius <= 0.5D) {
            if (base.getY() <= TerraformGenerator.seaLevel) {
               base.setType(Material.WATER);
            } else {
               base.setType(Material.AIR);
            }

         } else {
            FastNoise noise = new FastNoise(seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.09F);

            for(float x = -radius; x <= radius; ++x) {
               for(float y = -radius; y <= radius; ++y) {
                  for(float z = -radius; z <= radius; ++z) {
                     SimpleBlock rel = base.getRelative(Math.round(x), Math.round(y), Math.round(z));
                     double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)radius, 2.0D);
                     if (equationResult <= 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ())) {
                        if (rel.getY() <= TerraformGenerator.seaLevel) {
                           rel.setType(Material.WATER);
                        } else {
                           rel.setType(Material.AIR);
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public static void carveCaveAir(int seed, float rX, float rY, float rZ, @NotNull SimpleBlock block, boolean waterToAir, @NotNull EnumSet<Material> toReplace) {
      carveCaveAir(seed, rX, rY, rZ, 0.09F, block, waterToAir, toReplace);
   }

   public static void carveCaveAir(int seed, float rX, float rY, float rZ, float frequency, @NotNull SimpleBlock block, boolean waterToAir, @NotNull EnumSet<Material> toReplace) {
      carveCaveAir(seed, rX, rY, rZ, frequency, block, false, waterToAir, toReplace);
   }

   public static void carveCaveAir(int seed, float rX, float rY, float rZ, float frequency, @NotNull SimpleBlock block, boolean blockWaterHoles, boolean waterToAir, @NotNull EnumSet<Material> toReplace) {
      if (!(rX <= 0.0F) || !(rY <= 0.0F) || !(rZ <= 0.0F)) {
         if ((double)rX <= 0.5D && (double)rY <= 0.5D && (double)rZ <= 0.5D) {
            if (waterToAir || block.getType() != Material.WATER) {
               block.setType(Material.CAVE_AIR);
            }

         } else {
            FastNoise noise = new FastNoise(seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(frequency);

            for(float x = -rX * 1.3F; x <= rX * 1.3F; ++x) {
               for(float y = -rY; y <= rY; ++y) {
                  for(float z = -rZ * 1.3F; z <= rZ * 1.3F; ++z) {
                     SimpleBlock rel = block.getRelative(Math.round(x), Math.round(y), Math.round(z));
                     double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)rX, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)rY, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)rZ, 2.0D);
                     double noiseVal = 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ());
                     if (equationResult <= noiseVal) {
                        if (toReplace.contains(Material.BARRIER)) {
                           if (!toReplace.contains(rel.getType()) && (!isWet(rel) || waterToAir)) {
                              rel.physicsSetType(Material.CAVE_AIR, false);
                           }
                        } else if (toReplace.contains(rel.getType())) {
                           if (!isWet(rel) || waterToAir) {
                              rel.physicsSetType(Material.CAVE_AIR, false);
                           }
                        } else if (!rel.isSolid() && (!isWet(rel) || waterToAir)) {
                           rel.physicsSetType(Material.CAVE_AIR, false);
                        }

                        if (blockWaterHoles) {
                           BlockFace[] var18 = sixBlockFaces;
                           int var19 = var18.length;

                           for(int var20 = 0; var20 < var19; ++var20) {
                              BlockFace face = var18[var20];
                              SimpleBlock relrel = rel.getRelative(face);
                              if (isWet(relrel) || relrel.getType() == Material.LAVA) {
                                 Material setMat = relrel.getY() < 0 ? Material.DEEPSLATE : Material.STONE;
                                 relrel.physicsSetType(setMat, false);
                              }
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public static void replaceSphere(int seed, float rX, float rY, float rZ, @NotNull SimpleBlock block, boolean hardReplace, boolean snowy, Material... type) {
      if (!(rX <= 0.0F) || !(rY <= 0.0F) || !(rZ <= 0.0F)) {
         if ((double)rX <= 0.5D && (double)rY <= 0.5D && (double)rZ <= 0.5D) {
            block.setType((Material)GenUtils.randChoice(new Random((long)seed), type));
         } else {
            Random rand = new Random((long)seed);
            FastNoise noise = new FastNoise(seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.09F);

            for(float x = -rX; x <= rX; ++x) {
               for(float y = -rY; y <= rY; ++y) {
                  for(float z = -rZ; z <= rZ; ++z) {
                     SimpleBlock rel = block.getRelative(Math.round(x), Math.round(y), Math.round(z));
                     double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)rX, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)rY, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)rZ, 2.0D);
                     if (equationResult <= 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ()) && (hardReplace || !rel.isSolid())) {
                        rel.setType((Material)GenUtils.randChoice(rand, type));
                        if (snowy) {
                           rel.getUp().lsetType(Material.SNOW);
                        }
                     }
                  }
               }
            }

         }
      }
   }

   public static void replaceUpperSphere(int seed, float rX, float rY, float rZ, @NotNull SimpleBlock block, boolean hardReplace, Material... type) {
      if (!(rX <= 0.0F) || !(rY <= 0.0F) || !(rZ <= 0.0F)) {
         if ((double)rX <= 0.5D && (double)rY <= 0.5D && (double)rZ <= 0.5D) {
            block.setType((Material)GenUtils.randChoice(new Random((long)seed), type));
         } else {
            Random rand = new Random((long)seed);
            FastNoise noise = new FastNoise(seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.09F);

            for(float x = -rX; x <= rX; ++x) {
               for(float y = 0.0F; y <= rY; ++y) {
                  for(float z = -rZ; z <= rZ; ++z) {
                     SimpleBlock rel = block.getRelative(Math.round(x), Math.round(y), Math.round(z));
                     double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)rX, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)rY, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)rZ, 2.0D);
                     if (equationResult <= 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ()) && (hardReplace || !rel.isSolid())) {
                        rel.setType((Material)GenUtils.randChoice(rand, type));
                     }
                  }
               }
            }

         }
      }
   }

   public static void replaceLowerSphere(int seed, float rX, float rY, float rZ, @NotNull SimpleBlock block, boolean hardReplace, Material... type) {
      if (!(rX <= 0.0F) || !(rY <= 0.0F) || !(rZ <= 0.0F)) {
         if ((double)rX <= 0.5D && (double)rY <= 0.5D && (double)rZ <= 0.5D) {
            block.setType((Material)GenUtils.randChoice(new Random((long)seed), type));
         } else {
            Random rand = new Random((long)seed);
            FastNoise noise = new FastNoise(seed);
            noise.SetNoiseType(FastNoise.NoiseType.Simplex);
            noise.SetFrequency(0.09F);

            for(float x = -rX; x <= rX; ++x) {
               for(float y = -rY; y <= 0.0F; ++y) {
                  for(float z = -rZ; z <= rZ; ++z) {
                     SimpleBlock rel = block.getRelative(Math.round(x), Math.round(y), Math.round(z));
                     double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)rX, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)rY, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)rZ, 2.0D);
                     if (equationResult <= 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ()) && (hardReplace || !rel.isSolid())) {
                        rel.setType((Material)GenUtils.randChoice(rand, type));
                     }
                  }
               }
            }

         }
      }
   }

   @NotNull
   public static BlockFace[] getAdjacentFaces(@NotNull BlockFace original) {
      BlockFace[] var10000;
      switch(original) {
      case NORTH:
         var10000 = new BlockFace[]{BlockFace.EAST, BlockFace.WEST};
         break;
      case SOUTH:
         var10000 = new BlockFace[]{BlockFace.WEST, BlockFace.EAST};
         break;
      case EAST:
         var10000 = new BlockFace[]{BlockFace.SOUTH, BlockFace.NORTH};
         break;
      default:
         var10000 = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH};
      }

      return var10000;
   }

   public static BlockFace getTurnBlockFace(@NotNull Random rand, @NotNull BlockFace original) {
      return getAdjacentFaces(original)[GenUtils.randInt(rand, 0, 1)];
   }

   public static BlockFace getLeft(@NotNull BlockFace original) {
      return getAdjacentFaces(original)[0];
   }

   public static BlockFace getRight(@NotNull BlockFace original) {
      return getAdjacentFaces(original)[1];
   }

   public static void correctMultifacingData(@NotNull SimpleBlock target) {
      BlockData var2 = target.getBlockData();
      if (!(var2 instanceof MultipleFacing)) {
         if (Tag.WALLS.isTagged(target.getType())) {
            v1_16_R1_BlockDataFixer.correctSurroundingWallData(target);
         }

      } else {
         MultipleFacing data = (MultipleFacing)var2;
         Iterator var7 = data.getAllowedFaces().iterator();

         while(var7.hasNext()) {
            BlockFace face = (BlockFace)var7.next();
            Material type = target.getRelative(face).getType();
            boolean facing = type.isSolid() && !Tag.PRESSURE_PLATES.isTagged(type) && !Tag.BANNERS.isTagged(type) && !Tag.SLABS.isTagged(type) && !Tag.TRAPDOORS.isTagged(type);
            if (glassPanes.contains(target.getType()) && (Tag.FENCE_GATES.isTagged(type) || Tag.FENCES.isTagged(type))) {
               facing = false;
            }

            data.setFace(face, facing);
            if (Tag.STAIRS.isTagged(type)) {
               Stairs stairs = (Stairs)target.getRelative(face).getBlockData();
               data.setFace(face, stairs.getFacing() == face.getOppositeFace());
            }
         }

         target.setBlockData(data);
      }
   }

   public static boolean isExposedToNonSolid(@NotNull SimpleBlock target) {
      BlockFace[] var1 = directBlockFaces;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlockFace face = var1[var3];
         if (!target.getRelative(face).isSolid()) {
            return true;
         }
      }

      return false;
   }

   public static boolean isExposedToMaterial(@NotNull SimpleBlock target, @NotNull Set<Material> mats) {
      BlockFace[] var2 = directBlockFaces;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         if (mats.contains(target.getRelative(face).getType())) {
            return true;
         }
      }

      return false;
   }

   public static boolean isExposedToMaterial(@NotNull SimpleBlock target, Material mat) {
      BlockFace[] var2 = directBlockFaces;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         if (target.getRelative(face).getType() == mat) {
            return true;
         }
      }

      return false;
   }

   public static void correctSurroundingMultifacingData(@NotNull SimpleBlock target) {
      if (!(target.getBlockData() instanceof MultipleFacing)) {
         if (Tag.WALLS.isTagged(target.getType())) {
            v1_16_R1_BlockDataFixer.correctSurroundingWallData(target);
         }

      } else {
         correctMultifacingData(target);
         BlockData var2 = target.getBlockData();
         if (var2 instanceof MultipleFacing) {
            MultipleFacing data = (MultipleFacing)var2;
            Iterator var4 = data.getAllowedFaces().iterator();

            while(var4.hasNext()) {
               BlockFace face = (BlockFace)var4.next();
               if (target.getRelative(face).getBlockData() instanceof MultipleFacing) {
                  correctMultifacingData(target.getRelative(face));
               }
            }

         }
      }
   }

   public static void correctStairData(@NotNull SimpleBlock target) {
      BlockData var2 = target.getBlockData();
      if (var2 instanceof Stairs) {
         Stairs data = (Stairs)var2;
         BlockFace left = getLeft(data.getFacing());
         BlockFace right = getRight(data.getFacing());
         if (Tag.STAIRS.isTagged(target.getRelative(left).getType()) && !Tag.STAIRS.isTagged(target.getRelative(right).getType())) {
            if (((Stairs)target.getRelative(left).getBlockData()).getFacing() == data.getFacing()) {
               if (Tag.STAIRS.isTagged(target.getRelative(data.getFacing()).getType())) {
                  if (((Stairs)target.getRelative(data.getFacing()).getBlockData()).getFacing() == getLeft(data.getFacing())) {
                     data.setShape(Shape.OUTER_RIGHT);
                  }
               } else if (Tag.STAIRS.isTagged(target.getRelative(data.getFacing().getOppositeFace()).getType()) && ((Stairs)target.getRelative(data.getFacing().getOppositeFace()).getBlockData()).getFacing() == getRight(data.getFacing())) {
                  data.setShape(Shape.INNER_RIGHT);
               }
            }
         } else if (!Tag.STAIRS.isTagged(target.getRelative(left).getType()) && Tag.STAIRS.isTagged(target.getRelative(right).getType()) && ((Stairs)target.getRelative(right).getBlockData()).getFacing() == data.getFacing()) {
            if (Tag.STAIRS.isTagged(target.getRelative(data.getFacing()).getType())) {
               if (((Stairs)target.getRelative(data.getFacing()).getBlockData()).getFacing() == getRight(data.getFacing())) {
                  data.setShape(Shape.OUTER_LEFT);
               }
            } else if (Tag.STAIRS.isTagged(target.getRelative(data.getFacing().getOppositeFace()).getType()) && ((Stairs)target.getRelative(data.getFacing().getOppositeFace()).getBlockData()).getFacing() == getLeft(data.getFacing())) {
               data.setShape(Shape.INNER_LEFT);
            }
         }

         target.setBlockData(data);
      }
   }

   public static void correctSurroundingStairData(@NotNull SimpleBlock target) {
      BlockData var2 = target.getBlockData();
      if (var2 instanceof Stairs) {
         Stairs data = (Stairs)var2;
         correctStairData(target);
         BlockFace[] var6 = getAdjacentFaces(data.getFacing());
         int var3 = var6.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BlockFace face = var6[var4];
            if (target.getRelative(face).getBlockData() instanceof Stairs) {
               correctStairData(target.getRelative(face));
            }
         }

      }
   }

   private static boolean isMushroom(@NotNull SimpleBlock target) {
      Material material = target.getType();
      return material == Material.BROWN_MUSHROOM_BLOCK || material == Material.RED_MUSHROOM_BLOCK;
   }

   public static void correctMushroomData(@NotNull SimpleBlock target) {
      if (isMushroom(target)) {
         MultipleFacing data = (MultipleFacing)target.getBlockData();
         Iterator var2 = data.getAllowedFaces().iterator();

         while(var2.hasNext()) {
            BlockFace face = (BlockFace)var2.next();
            data.setFace(face, !isMushroom(target.getRelative(face)));
         }

         target.setBlockData(data);
      }
   }

   public static void correctSurroundingMushroomData(@NotNull SimpleBlock target) {
      correctMushroomData(target);
      BlockFace[] var1 = sixBlockFaces;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlockFace face = var1[var3];
         correctMushroomData(target.getRelative(face));
      }

   }

   public static void placeDoor(@NotNull PopulatorDataAbstract data, @NotNull Material mat, @NotNull Wall w) {
      placeDoor(data, mat, w.getX(), w.getY(), w.getZ(), w.getDirection());
   }

   public static void placeDoor(@NotNull PopulatorDataAbstract data, @NotNull Material mat, int x, int y, int z, @NotNull BlockFace dir) {
      data.setType(x, y, z, mat);
      data.setType(x, y + 1, z, mat);
      Door door = (Door)Bukkit.createBlockData(mat);
      door.setFacing(dir);
      door.setHalf(Half.BOTTOM);
      data.setBlockData(x, y, z, door);
      door = (Door)Bukkit.createBlockData(mat);
      door.setFacing(dir);
      door.setHalf(Half.TOP);
      data.setBlockData(x, y + 1, z, door);
   }

   public static void placeBed(@NotNull SimpleBlock block, @NotNull Material mat, @NotNull BlockFace dir) {
      if (isAir(block.getType()) && isAir(block.getRelative(dir).getType())) {
         Bed bed = (Bed)Bukkit.createBlockData(mat);
         bed.setFacing(dir.getOppositeFace());
         bed.setPart(Part.HEAD);
         block.setBlockData(bed);
         bed = (Bed)Bukkit.createBlockData(mat);
         bed.setFacing(dir.getOppositeFace());
         bed.setPart(Part.FOOT);
         block.getRelative(dir).setBlockData(bed);
      }

   }

   @NotNull
   public static BlockFace[] getDirectFacesFromDiagonal(@NotNull BlockFace face) {
      BlockFace[] var10000;
      switch(face) {
      case NORTH_EAST:
         var10000 = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST};
         break;
      case SOUTH_EAST:
         var10000 = new BlockFace[]{BlockFace.SOUTH, BlockFace.EAST};
         break;
      case SOUTH_WEST:
         var10000 = new BlockFace[]{BlockFace.SOUTH, BlockFace.EAST};
         break;
      case NORTH_WEST:
         var10000 = new BlockFace[]{BlockFace.NORTH, BlockFace.WEST};
         break;
      default:
         throw new UnsupportedOperationException("getDirectFacesFromDiagonal can only be used for XZ-Plane diagonals");
      }

      return var10000;
   }

   public static void placeRail(@NotNull SimpleBlock block, @NotNull Material mat) {
      Rail rail = (Rail)Bukkit.createBlockData(mat);
      Set<BlockFace> faces = EnumSet.noneOf(BlockFace.class);
      BlockFace upperFace = null;
      BlockFace[] var5 = directBlockFaces;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         BlockFace face = var5[var7];
         SimpleBlock relative = block.getRelative(face);
         if (Tag.RAILS.isTagged(relative.getType())) {
            faces.add(face);
         }

         if (Tag.RAILS.isTagged(relative.getUp().getType())) {
            upperFace = face;
         }
      }

      if (upperFace != null) {
         switch(upperFace) {
         case NORTH:
            rail.setShape(org.bukkit.block.data.Rail.Shape.ASCENDING_NORTH);
            break;
         case SOUTH:
            rail.setShape(org.bukkit.block.data.Rail.Shape.ASCENDING_SOUTH);
            break;
         case EAST:
            rail.setShape(org.bukkit.block.data.Rail.Shape.ASCENDING_EAST);
            break;
         case WEST:
            rail.setShape(org.bukkit.block.data.Rail.Shape.ASCENDING_WEST);
         }
      } else if (!faces.isEmpty()) {
         if (faces.contains(BlockFace.NORTH) && faces.contains(BlockFace.EAST)) {
            rail.setShape(org.bukkit.block.data.Rail.Shape.NORTH_EAST);
         } else if (faces.contains(BlockFace.NORTH) && faces.contains(BlockFace.WEST)) {
            rail.setShape(org.bukkit.block.data.Rail.Shape.NORTH_WEST);
         } else if (faces.contains(BlockFace.SOUTH) && faces.contains(BlockFace.EAST)) {
            rail.setShape(org.bukkit.block.data.Rail.Shape.SOUTH_EAST);
         } else if (!faces.contains(BlockFace.NORTH) && !faces.contains(BlockFace.SOUTH)) {
            if (faces.contains(BlockFace.EAST) || faces.contains(BlockFace.WEST)) {
               rail.setShape(org.bukkit.block.data.Rail.Shape.EAST_WEST);
            }
         } else {
            rail.setShape(org.bukkit.block.data.Rail.Shape.NORTH_SOUTH);
         }
      }

      block.setBlockData(rail);
   }

   public static void correctSurroundingRails(@NotNull SimpleBlock target) {
      if (target.getBlockData() instanceof Rail) {
         placeRail(target, target.getType());
         BlockFace[] var1 = directBlockFaces;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            BlockFace face = var1[var3];
            SimpleBlock relative = target.getRelative(face);
            if (relative.getBlockData() instanceof Rail) {
               placeRail(relative, relative.getType());
            }

            if (target.getRelative(face).getDown().getBlockData() instanceof Rail) {
               placeRail(relative.getDown(), target.getDown().getRelative(face).getType());
            }
         }

      }
   }

   public static boolean emitsLight(@NotNull Material mat) {
      boolean var10000;
      switch(mat) {
      case TORCH:
      case SEA_PICKLE:
      case SEA_LANTERN:
      case GLOWSTONE:
      case LANTERN:
      case LAVA:
      case CAMPFIRE:
      case REDSTONE_LAMP:
      case FIRE:
         var10000 = true;
         break;
      default:
         var10000 = false;
      }

      return var10000;
   }

   @NotNull
   public static BlockData infestStone(@NotNull BlockData mat) {
      BlockData var10000;
      switch(mat.getMaterial()) {
      case STONE_BRICKS:
         var10000 = Bukkit.createBlockData(Material.INFESTED_STONE_BRICKS);
         break;
      case MOSSY_STONE_BRICKS:
         var10000 = Bukkit.createBlockData(Material.INFESTED_MOSSY_STONE_BRICKS);
         break;
      case CRACKED_STONE_BRICKS:
         var10000 = Bukkit.createBlockData(Material.INFESTED_CRACKED_STONE_BRICKS);
         break;
      case CHISELED_STONE_BRICKS:
         var10000 = Bukkit.createBlockData(Material.INFESTED_CHISELED_STONE_BRICKS);
         break;
      case COBBLESTONE:
         var10000 = Bukkit.createBlockData(Material.INFESTED_COBBLESTONE);
         break;
      case STONE:
         var10000 = Bukkit.createBlockData(Material.INFESTED_STONE);
         break;
      default:
         var10000 = mat;
      }

      return var10000;
   }

   public static void stairwayUntilSolid(@NotNull SimpleBlock start, @NotNull BlockFace extensionDir, Material[] downTypes, Material... stairTypes) {
      while(!start.isSolid()) {
         (new StairBuilder(stairTypes)).setFacing(extensionDir.getOppositeFace()).apply(start);
         setDownUntilSolid(start.getX(), start.getY() - 1, start.getZ(), start.getPopData(), downTypes);
         start = start.getRelative(extensionDir).getDown();
      }

   }

   public static boolean isAir(Material mat) {
      return airs.contains(mat);
   }

   @NotNull
   public static BlockData getRandomBarrel() {
      Directional barrel = (Directional)Bukkit.createBlockData(Material.BARREL);
      barrel.setFacing(sixBlockFaces[GenUtils.randInt(0, sixBlockFaces.length - 1)]);
      return barrel;
   }

   public static void angledStairwayUntilSolid(@NotNull SimpleBlock start, BlockFace extensionDir, Material[] downTypes, Material... stairTypes) {
      for(int threshold = 5; !start.isSolid(); start = start.getRelative(extensionDir).getDown()) {
         if (threshold == 0) {
            extensionDir = getTurnBlockFace(new Random(), extensionDir);
         }

         (new StairBuilder(stairTypes)).setFacing(extensionDir.getOppositeFace()).apply(start);
         setDownUntilSolid(start.getX(), start.getY() - 1, start.getZ(), start.getPopData(), downTypes);
         --threshold;
      }

   }

   public static boolean isWet(@NotNull SimpleBlock target) {
      return wetMaterials.contains(target.getType()) || target.getBlockData() instanceof Waterlogged && ((Waterlogged)target.getBlockData()).isWaterlogged();
   }

   public static float yawFromBlockFace(@NotNull BlockFace face) {
      float var10000;
      switch(face) {
      case NORTH:
         var10000 = 180.0F;
         break;
      case SOUTH:
         var10000 = 0.0F;
         break;
      case EAST:
         var10000 = -90.0F;
         break;
      case WEST:
         var10000 = 90.0F;
         break;
      default:
         var10000 = 180.0F;
      }

      return var10000;
   }

   public static void randRotateBlockData(@NotNull Random rand, BlockData data) {
      if (data instanceof Directional) {
         Set<BlockFace> faces = ((Directional)data).getFaces();
         ((Directional)data).setFacing((BlockFace)faces.stream().skip((long)((int)((double)faces.size() * rand.nextDouble()))).findAny().get());
      } else if (data instanceof Rotatable) {
         ((Rotatable)data).setRotation(getXZPlaneBlockFace(rand));
      }

   }

   public static boolean isOre(Material mat) {
      Iterator var1 = ores.iterator();

      Material ore;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         ore = (Material)var1.next();
      } while(ore != mat);

      return true;
   }

   public static void placeCandle(@NotNull SimpleBlock block, int numCandles, boolean lit) {
      if (TConfig.areDecorationsEnabled()) {
         Candle candle = (Candle)Bukkit.createBlockData(Material.CANDLE);
         candle.setLit(lit);
         candle.setCandles(numCandles);
         block.setBlockData(candle);
      }
   }

   public static void downLPointedDripstone(int height, @NotNull SimpleBlock base) {
      if (TConfig.areDecorationsEnabled()) {
         int realHeight;
         for(realHeight = 0; !base.getRelative(0, -realHeight, 0).isSolid() && height > 0; --height) {
            ++realHeight;
         }

         if (base.getRelative(0, -realHeight, 0).isSolid()) {
            --realHeight;
         }

         if (realHeight > 0) {
            for(int i = realHeight; i > 0; --i) {
               Thickness thickness = Thickness.MIDDLE;
               if (i == 1) {
                  thickness = Thickness.TIP;
               }

               if (i == 2) {
                  thickness = Thickness.FRUSTUM;
               }

               if (i == realHeight && realHeight > 2) {
                  thickness = Thickness.BASE;
               }

               PointedDripstone dripstone = (PointedDripstone)Bukkit.createBlockData(Material.POINTED_DRIPSTONE);
               dripstone.setVerticalDirection(BlockFace.DOWN);
               dripstone.setThickness(thickness);
               base.getRelative(0, -(realHeight - i), 0).setBlockData(dripstone);
            }

         }
      }
   }

   public static Material stoneOrSlate(int y) {
      return y > 0 ? Material.STONE : (y < -3 ? Material.DEEPSLATE : (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.DEEPSLATE)));
   }

   public static Material stoneOrSlateWall(int y) {
      return y > 0 ? Material.COBBLESTONE_WALL : (y < -3 ? Material.COBBLED_DEEPSLATE_WALL : (Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE_WALL, Material.COBBLED_DEEPSLATE_WALL)));
   }

   public static void upLPointedDripstone(int height, @NotNull SimpleBlock base) {
      if (TConfig.areDecorationsEnabled()) {
         int realHeight;
         for(realHeight = 0; !base.getRelative(0, realHeight, 0).isSolid() && height > 0; --height) {
            ++realHeight;
         }

         if (base.getRelative(0, realHeight, 0).isSolid()) {
            --realHeight;
         }

         if (realHeight > 0) {
            for(int i = 0; i < realHeight; ++i) {
               Thickness thickness = Thickness.MIDDLE;
               if (realHeight >= 4) {
                  if (i == realHeight - 1) {
                     thickness = Thickness.TIP;
                  }

                  if (i == realHeight - 2) {
                     thickness = Thickness.FRUSTUM;
                  }

                  if (i == 0) {
                     thickness = Thickness.BASE;
                  }
               } else if (realHeight >= 3) {
                  if (i == realHeight - 1) {
                     thickness = Thickness.TIP;
                  }

                  if (i == realHeight - 2) {
                     thickness = Thickness.FRUSTUM;
                  }

                  if (i == 0) {
                     thickness = Thickness.BASE;
                  }
               } else if (realHeight >= 2) {
                  thickness = Thickness.TIP;
                  if (i == 0) {
                     thickness = Thickness.FRUSTUM;
                  }
               } else {
                  thickness = Thickness.TIP;
               }

               PointedDripstone dripstone = (PointedDripstone)Bukkit.createBlockData(Material.POINTED_DRIPSTONE);
               dripstone.setVerticalDirection(BlockFace.UP);
               dripstone.setThickness(thickness);
               base.getRelative(0, i, 0).setBlockData(dripstone);
            }

         }
      }
   }

   public static void downLCaveVines(int height, @NotNull SimpleBlock base) {
      if (TConfig.arePlantsEnabled()) {
         int realHeight;
         for(realHeight = 0; !base.getRelative(0, -realHeight, 0).isSolid() && height > 0; --height) {
            ++realHeight;
         }

         if (base.getRelative(0, -realHeight, 0).isSolid()) {
            --realHeight;
         }

         if (realHeight > 0) {
            for(int i = realHeight; i > 0; --i) {
               CaveVinesPlant vines = (CaveVinesPlant)Bukkit.createBlockData(i == 1 ? Material.CAVE_VINES : Material.CAVE_VINES_PLANT);
               vines.setBerries((new Random()).nextInt(3) == 0);
               base.getRelative(0, -(realHeight - i), 0).lsetBlockData(vines);
            }

         }
      }
   }

   @NotNull
   public static BlockData deepSlateVersion(@NotNull Material target) {
      BlockData data = (BlockData)deepslateMap.get("DEEPSLATE_" + String.valueOf(target));
      if (data == null) {
         Material mat = Material.getMaterial("DEEPSLATE_" + String.valueOf(target));
         if (mat == null) {
            return Bukkit.createBlockData(target);
         }

         data = Bukkit.createBlockData(mat);
         deepslateMap.put("DEEPSLATE_" + String.valueOf(target), data);
      }

      return data;
   }

   static {
      replacableByTrees = EnumSet.of(V_1_21_5.BUSH, V_1_21_5.FIREFLY_BUSH, V_1_21_5.WILDFLOWERS, V_1_21_5.LEAF_LITTER, Material.ACACIA_LEAVES, Material.AZALEA_LEAVES, Material.DARK_OAK_LEAVES, Material.BIRCH_LEAVES, Material.SPRUCE_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, V_1_20.CHERRY_LEAVES, Material.FLOWERING_AZALEA_LEAVES, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.GRASS, Material.FERN, Material.DEAD_BUSH, Material.VINE, Material.GLOW_LICHEN, Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY, Material.TALL_GRASS, Material.LARGE_FERN, Material.HANGING_ROOTS, V_1_20.PITCHER_PLANT, Material.WATER, Material.AIR, Material.CAVE_AIR, Material.SEAGRASS, Material.TALL_SEAGRASS, Material.WARPED_ROOTS, Material.NETHER_SPROUTS, Material.CRIMSON_ROOTS, Material.SNOW, Material.BRAIN_CORAL_FAN, Material.BUBBLE_CORAL_FAN, Material.FIRE_CORAL_FAN, Material.HORN_CORAL_FAN, Material.TUBE_CORAL_FAN, Material.BRAIN_CORAL_WALL_FAN, Material.BUBBLE_CORAL_WALL_FAN, Material.FIRE_CORAL_WALL_FAN, Material.HORN_CORAL_WALL_FAN, Material.TUBE_CORAL_WALL_FAN, Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_BUBBLE_CORAL_FAN, Material.DEAD_FIRE_CORAL_FAN, Material.DEAD_HORN_CORAL_FAN, Material.DEAD_TUBE_CORAL_FAN, Material.DEAD_BRAIN_CORAL_WALL_FAN, Material.DEAD_BUBBLE_CORAL_WALL_FAN, Material.DEAD_FIRE_CORAL_WALL_FAN, Material.DEAD_HORN_CORAL_WALL_FAN, Material.DEAD_TUBE_CORAL_WALL_FAN);
      xzPlaneBlockFaces = new BlockFace[]{BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};
      fluids = EnumSet.of(Material.WATER, Material.LAVA);
      wetMaterials = EnumSet.of(Material.WATER, Material.KELP_PLANT, Material.SEAGRASS, Material.TALL_SEAGRASS);
      amethysts = EnumSet.of(Material.AMETHYST_BLOCK, Material.AMETHYST_CLUSTER, Material.BUDDING_AMETHYST, Material.LARGE_AMETHYST_BUD, Material.MEDIUM_AMETHYST_BUD, Material.SMALL_AMETHYST_BUD);
      flatBlockFaces3x3 = new BlockFace[]{BlockFace.SELF, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};
      BLOCK_FACES = BlockFace.values();
      xzDiagonalPlaneBlockFaces = new BlockFace[]{BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST};
      stoneBricks = new Material[]{Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS};
      stoneBrickSlabs = new Material[]{Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB};
      directBlockFaces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
      cornerBlockFaces = new BlockFace[][]{{BlockFace.NORTH, BlockFace.EAST}, {BlockFace.NORTH, BlockFace.WEST}, {BlockFace.SOUTH, BlockFace.EAST}, {BlockFace.SOUTH, BlockFace.WEST}};
      sixBlockFaces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
      stoneLike = EnumSet.of(Material.STONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.GRANITE, Material.ANDESITE, Material.DIORITE, Material.GRAVEL, Material.CLAY, Material.DEEPSLATE, Material.TUFF, Material.CALCITE, Material.BUDDING_AMETHYST, Material.AMETHYST_BLOCK, Material.DRIPSTONE_BLOCK, Material.SMOOTH_BASALT, Material.PACKED_ICE, Material.BLUE_ICE, Material.DIRT, Material.PODZOL, Material.GRASS_BLOCK, Material.MYCELIUM, Material.ROOTED_DIRT, Material.DIRT_PATH, V_1_19.SCULK);
      caveDecoratorMaterials = EnumSet.of(Material.ANDESITE_WALL, Material.DIORITE_WALL, Material.GRANITE_WALL, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_SLAB, Material.COBBLED_DEEPSLATE_WALL, Material.COBBLESTONE_SLAB, Material.STONE_SLAB, Material.COBBLED_DEEPSLATE_SLAB, Material.MOSS_BLOCK, Material.MOSS_CARPET, Material.CAVE_VINES, Material.CAVE_VINES_PLANT, Material.HANGING_ROOTS, Material.SPORE_BLOSSOM, Material.SMALL_DRIPLEAF, Material.AZALEA, Material.FLOWERING_AZALEA, Material.BIG_DRIPLEAF, Material.BIG_DRIPLEAF_STEM, Material.GRASS, Material.TALL_GRASS, Material.ICE, Material.PACKED_ICE, Material.DRIPSTONE_BLOCK, Material.POINTED_DRIPSTONE, Material.AMETHYST_CLUSTER, Material.BUDDING_AMETHYST, Material.GLOW_LICHEN, Material.NOTE_BLOCK, Material.SPAWNER, Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK);
      badlandsStoneLike = EnumSet.of(Material.TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.RED_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.RED_SAND);
      caveCarveReplace = EnumSet.of(Material.NOTE_BLOCK);
      ores = EnumSet.noneOf(Material.class);
      airs = EnumSet.of(Material.AIR, Material.CAVE_AIR);
      glassPanes = EnumSet.noneOf(Material.class);
      WOOLS = new Material[]{Material.WHITE_WOOL, Material.BLACK_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.CYAN_WOOL, Material.GRAY_WOOL, Material.GREEN_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIGHT_GRAY_WOOL, Material.LIME_WOOL, Material.MAGENTA_WOOL, Material.ORANGE_WOOL, Material.PINK_WOOL, Material.PURPLE_WOOL, Material.RED_WOOL, Material.YELLOW_WOOL};
      GLAZED_TERRACOTTA = new Material[]{Material.WHITE_GLAZED_TERRACOTTA, Material.BLACK_GLAZED_TERRACOTTA, Material.BLUE_GLAZED_TERRACOTTA, Material.BROWN_GLAZED_TERRACOTTA, Material.CYAN_GLAZED_TERRACOTTA, Material.GRAY_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.LIGHT_GRAY_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA, Material.MAGENTA_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA, Material.PINK_GLAZED_TERRACOTTA, Material.PURPLE_GLAZED_TERRACOTTA, Material.RED_GLAZED_TERRACOTTA, Material.YELLOW_GLAZED_TERRACOTTA};
      TERRACOTTA = new Material[]{Material.WHITE_TERRACOTTA, Material.BLACK_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.GRAY_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA, Material.LIME_TERRACOTTA, Material.MAGENTA_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.PINK_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.RED_TERRACOTTA, Material.YELLOW_TERRACOTTA};
      TALL_FLOWER = new PlantBuilder[]{PlantBuilder.LILAC, PlantBuilder.ROSE_BUSH, PlantBuilder.PEONY, PlantBuilder.LARGE_FERN, PlantBuilder.SUNFLOWER};
      FLOWER = new PlantBuilder[]{PlantBuilder.DANDELION, PlantBuilder.POPPY, PlantBuilder.WHITE_TULIP, PlantBuilder.ORANGE_TULIP, PlantBuilder.RED_TULIP, PlantBuilder.PINK_TULIP, PlantBuilder.BLUE_ORCHID, PlantBuilder.ALLIUM, PlantBuilder.AZURE_BLUET, PlantBuilder.OXEYE_DAISY, PlantBuilder.CORNFLOWER, PlantBuilder.LILY_OF_THE_VALLEY};
      POTTED = new PlantBuilder[]{PlantBuilder.POTTED_DANDELION, PlantBuilder.POTTED_POPPY, PlantBuilder.POTTED_WHITE_TULIP, PlantBuilder.POTTED_ORANGE_TULIP, PlantBuilder.POTTED_RED_TULIP, PlantBuilder.POTTED_PINK_TULIP, PlantBuilder.POTTED_BLUE_ORCHID, PlantBuilder.POTTED_ALLIUM, PlantBuilder.POTTED_AZURE_BLUET, PlantBuilder.POTTED_OXEYE_DAISY, PlantBuilder.POTTED_CORNFLOWER, PlantBuilder.POTTED_LILY_OF_THE_VALLEY};
      CARPETS = new Material[]{Material.WHITE_CARPET, Material.BLACK_CARPET, Material.BLUE_CARPET, Material.BROWN_CARPET, Material.CYAN_CARPET, Material.GRAY_CARPET, Material.GREEN_CARPET, Material.LIGHT_BLUE_CARPET, Material.LIGHT_GRAY_CARPET, Material.LIME_CARPET, Material.MAGENTA_CARPET, Material.ORANGE_CARPET, Material.PINK_CARPET, Material.PURPLE_CARPET, Material.RED_CARPET, Material.YELLOW_CARPET};
      BED = new Material[]{Material.WHITE_BED, Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED, Material.PURPLE_BED, Material.RED_BED, Material.YELLOW_BED};
      deepslateMap = new HashMap();
   }
}
