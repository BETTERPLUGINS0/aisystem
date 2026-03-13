package org.terraform.utils.version;

import java.util.Locale;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.utils.BlockUtils;

public class V_1_20 {
   @Nullable
   public static final Material PITCHER_PLANT;
   @Nullable
   public static final Material CHERRY_LOG;
   @Nullable
   public static final Material CHERRY_WOOD;
   @Nullable
   public static final Material CHERRY_LEAVES;
   @Nullable
   public static final Material CHERRY_SAPLING;
   @Nullable
   public static final Material SUSPICIOUS_SAND;
   @Nullable
   public static final Material SUSPICIOUS_GRAVEL;
   @Nullable
   public static final EntityType CAMEL;

   @NotNull
   public static BlockData getPinkPetalData(int count) {
      return Bukkit.createBlockData("pink_petals[flower_amount=" + count + ",facing=" + BlockUtils.getDirectBlockFace(new Random()).toString().toLowerCase(Locale.ENGLISH) + "]");
   }

   @Nullable
   private static EntityType getCamel() {
      try {
         return EntityType.valueOf("CAMEL");
      } catch (Exception var1) {
         return null;
      }
   }

   static {
      PITCHER_PLANT = !Version.VERSION.isAtLeast(Version.v1_20) ? Material.TALL_GRASS : Material.getMaterial("PITCHER_PLANT");
      CHERRY_LOG = !Version.VERSION.isAtLeast(Version.v1_20) ? Material.DARK_OAK_LOG : Material.getMaterial("CHERRY_LOG");
      CHERRY_WOOD = !Version.VERSION.isAtLeast(Version.v1_20) ? Material.DARK_OAK_WOOD : Material.getMaterial("CHERRY_WOOD");
      CHERRY_LEAVES = !Version.VERSION.isAtLeast(Version.v1_20) ? Material.DARK_OAK_LEAVES : Material.getMaterial("CHERRY_LEAVES");
      CHERRY_SAPLING = !Version.VERSION.isAtLeast(Version.v1_20) ? Material.DARK_OAK_SAPLING : Material.getMaterial("CHERRY_SAPLING");
      SUSPICIOUS_SAND = !Version.VERSION.isAtLeast(Version.v1_20) ? Material.SAND : Material.getMaterial("SUSPICIOUS_SAND");
      SUSPICIOUS_GRAVEL = !Version.VERSION.isAtLeast(Version.v1_20) ? Material.GRAVEL : Material.getMaterial("SUSPICIOUS_GRAVEL");
      CAMEL = getCamel();
   }
}
