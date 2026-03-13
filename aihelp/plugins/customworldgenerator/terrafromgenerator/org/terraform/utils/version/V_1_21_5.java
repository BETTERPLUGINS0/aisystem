package org.terraform.utils.version;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;

public class V_1_21_5 {
   public static Material BUSH;
   public static Material FIREFLY_BUSH;
   public static Material CACTUS_FLOWER;
   public static Material SHORT_DRY_GRASS;
   public static Material TALL_DRY_GRASS;
   public static Material LEAF_LITTER;
   public static Material WILDFLOWERS;
   private static BlockData[] leafLitters;
   private static BlockData[] wildflowerSet;

   public static void leafLitter(Random random, PopulatorDataAbstract data, int x, int y, int z) {
      if (Version.VERSION.isAtLeast(Version.v1_21_5)) {
         if (TConfig.c.FEATURE_PLANTS_ENABLED) {
            if (data.getType(x, y, z) == Material.AIR) {
               if (leafLitters == null) {
                  leafLitters = new BlockData[]{Bukkit.createBlockData("leaf_litter[segment_amount=1]"), Bukkit.createBlockData("leaf_litter[segment_amount=2]"), Bukkit.createBlockData("leaf_litter[segment_amount=3]"), Bukkit.createBlockData("leaf_litter[segment_amount=4]")};
               }

               Directional d = (Directional)leafLitters[random.nextInt(leafLitters.length)];
               d.setFacing(BlockUtils.getDirectBlockFace(random));
               data.setBlockData(x, y, z, d);
            }
         }
      }
   }

   public static void wildflowers(Random random, PopulatorDataAbstract data, int x, int y, int z) {
      if (Version.VERSION.isAtLeast(Version.v1_21_5)) {
         if (TConfig.c.FEATURE_PLANTS_ENABLED) {
            if (data.getType(x, y, z) == Material.AIR) {
               if (wildflowerSet == null) {
                  wildflowerSet = new BlockData[]{Bukkit.createBlockData("wildflowers[flower_amount=1]"), Bukkit.createBlockData("wildflowers[flower_amount=2]"), Bukkit.createBlockData("wildflowers[flower_amount=3]"), Bukkit.createBlockData("wildflowers[flower_amount=4]")};
               }

               Directional d = (Directional)wildflowerSet[random.nextInt(wildflowerSet.length)];
               d.setFacing(BlockUtils.getDirectBlockFace(random));
               data.setBlockData(x, y, z, d);
            }
         }
      }
   }

   static {
      BUSH = Version.VERSION.isAtLeast(Version.v1_21_5) ? Material.valueOf("BUSH") : Material.GRASS;
      FIREFLY_BUSH = Version.VERSION.isAtLeast(Version.v1_21_5) ? Material.valueOf("FIREFLY_BUSH") : Material.GRASS;
      CACTUS_FLOWER = Version.VERSION.isAtLeast(Version.v1_21_5) ? Material.valueOf("CACTUS_FLOWER") : Material.CACTUS;
      SHORT_DRY_GRASS = Version.VERSION.isAtLeast(Version.v1_21_5) ? Material.valueOf("SHORT_DRY_GRASS") : Material.DEAD_BUSH;
      TALL_DRY_GRASS = Version.VERSION.isAtLeast(Version.v1_21_5) ? Material.valueOf("TALL_DRY_GRASS") : Material.DEAD_BUSH;
      LEAF_LITTER = Version.VERSION.isAtLeast(Version.v1_21_5) ? Material.valueOf("LEAF_LITTER") : Material.GRASS;
      WILDFLOWERS = Version.VERSION.isAtLeast(Version.v1_21_5) ? Material.valueOf("WILDFLOWERS") : Material.DANDELION;
   }
}
