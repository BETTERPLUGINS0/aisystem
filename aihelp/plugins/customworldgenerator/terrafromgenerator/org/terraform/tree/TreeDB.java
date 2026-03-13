package org.terraform.tree;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class TreeDB {
   private static final FractalTypes.Tree[] FRACTAL_CORAL_TYPES;

   public static void spawnAzalea(@NotNull Random random, @NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.areTreesEnabled()) {
         FractalTreeBuilder builder = new FractalTreeBuilder(FractalTypes.Tree.AZALEA_TOP);
         builder.build(tw, data, x, y, z);
         SimpleBlock rooter = new SimpleBlock(data, x, y - 1, z);
         rooter.setType(Material.ROOTED_DIRT);

         for(rooter = rooter.getDown(); !BlockUtils.isAir(rooter.getType()); rooter = rooter.getDown()) {
            rooter.setType(Material.ROOTED_DIRT);
            BlockFace[] var8 = BlockUtils.xzPlaneBlockFaces;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               BlockFace face = var8[var10];
               SimpleBlock rel = rooter.getRelative(face);
               if (random.nextBoolean() && BlockUtils.isStoneLike(rel.getType())) {
                  rel.setType(Material.ROOTED_DIRT);
                  if (BlockUtils.isAir(rel.getDown().getType())) {
                     rel.getDown().setType(Material.HANGING_ROOTS);
                  }
               }
            }
         }

         rooter.setType(Material.HANGING_ROOTS);
      }
   }

   public static void spawnCoconutTree(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.areTreesEnabled()) {
         SimpleBlock base = new SimpleBlock(data, x, y, z);
         FractalTreeBuilder builder = new FractalTreeBuilder(FractalTypes.Tree.COCONUT_TOP);
         if (builder.checkGradient(data, x, z)) {
            Material log = Material.JUNGLE_WOOD;
            if (TConfig.c.MISC_TREES_FORCE_LOGS) {
               log = Material.JUNGLE_LOG;
            }

            BlockFace[] var8 = BlockUtils.directBlockFaces;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               BlockFace face = var8[var10];
               (new Wall(base.getRelative(face), BlockFace.NORTH)).downUntilSolid(new Random(), new Material[]{log});
            }

            builder.build(tw, data, x, y, z);
         }
      }
   }

   public static void spawnSmallJungleTree(boolean skipGradientCheck, @NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.areTreesEnabled()) {
         FractalTreeBuilder ftb;
         if (GenUtils.chance(1, 8)) {
            ftb = new FractalTreeBuilder(FractalTypes.Tree.JUNGLE_EXTRA_SMALL);
         } else {
            ftb = new FractalTreeBuilder(FractalTypes.Tree.JUNGLE_SMALL);
         }

         if (skipGradientCheck) {
            ftb.skipGradientCheck();
         }

         ftb.build(tw, data, x, y, z);
      }
   }

   public static void spawnRandomGiantCoral(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      FractalTypes.Tree type = FRACTAL_CORAL_TYPES[tw.getHashedRand((long)x, y, z).nextInt(5)];
      FractalTreeBuilder ftb = new FractalTreeBuilder(type);
      ftb.setMaxHeight(TerraformGenerator.seaLevel - y - 1);
      ftb.build(tw, data, x, y - 2, z);
   }

   public static void spawnBreathingRoots(@NotNull TerraformWorld tw, @NotNull SimpleBlock centre, Material type) {
      if (TConfig.areTreesEnabled()) {
         Random rand = tw.getHashedRand(centre.getX(), centre.getY(), centre.getZ(), 178782L);

         for(int i = 0; i < 4 + rand.nextInt(8); ++i) {
            SimpleBlock core = centre.getRelative(GenUtils.getSign(rand) * GenUtils.randInt(4, 8), 0, GenUtils.getSign(rand) * GenUtils.randInt(4, 8)).getGround().getUp();
            if (core.getY() <= TerraformGenerator.seaLevel + 2) {
               int min = core.getY() < TerraformGenerator.seaLevel ? TerraformGenerator.seaLevel - core.getY() + 1 : 1;
               core.LPillar(min + rand.nextInt(4), type);
            }
         }

      }
   }

   static {
      FRACTAL_CORAL_TYPES = new FractalTypes.Tree[]{FractalTypes.Tree.FIRE_CORAL, FractalTypes.Tree.BRAIN_CORAL, FractalTypes.Tree.TUBE_CORAL, FractalTypes.Tree.HORN_CORAL, FractalTypes.Tree.BUBBLE_CORAL};
   }
}
