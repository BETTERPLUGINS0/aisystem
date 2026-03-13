package org.terraform.utils.version;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class V_1_19 {
   public static final EntityType ALLAY = getEntityType();
   @NotNull
   public static final Material MUD;
   @Nullable
   public static final Material MUD_BRICKS;
   @Nullable
   public static final Material REINFORCED_DEEPSLATE;
   @Nullable
   public static final Material MANGROVE_LEAVES;
   @Nullable
   public static final Material MANGROVE_LOG;
   @Nullable
   public static final Material MANGROVE_WOOD;
   @Nullable
   public static final Material MANGROVE_PROPAGULE;
   @Nullable
   public static final Material MANGROVE_ROOTS;
   @Nullable
   public static final Material MUDDY_MANGROVE_ROOTS;
   @Nullable
   public static final Material MANGROVE_FENCE;
   @Nullable
   public static final Material SCULK_VEIN;
   @Nullable
   public static final Material SCULK;
   @Nullable
   public static final Material SCULK_CATALYST;
   @Nullable
   public static final Material SCULK_SHRIEKER;
   @Nullable
   public static final Material SCULK_SENSOR;
   public static final Biome MANGROVE_SWAMP;
   public static final Biome DEEP_DARK;
   private static final String shriekerDataString = "minecraft:sculk_shrieker[can_summon=true,shrieking=false,waterlogged=false]";
   private static final String propaguleDataString = "minecraft:mangrove_propagule[hanging=true,age=4,waterlogged=false]";

   @NotNull
   private static Biome getBiome(String name, String fallback) {
      try {
         return Biome.valueOf(name);
      } catch (IllegalArgumentException var3) {
         return Biome.valueOf(fallback);
      }
   }

   @NotNull
   private static EntityType getEntityType() {
      try {
         return EntityType.valueOf("ALLAY");
      } catch (IllegalArgumentException var1) {
         return EntityType.valueOf("CHICKEN");
      }
   }

   @NotNull
   public static BlockData getActiveSculkShrieker() {
      return Bukkit.createBlockData("minecraft:sculk_shrieker[can_summon=true,shrieking=false,waterlogged=false]");
   }

   @NotNull
   public static BlockData getHangingMangrovePropagule() {
      return Bukkit.createBlockData("minecraft:mangrove_propagule[hanging=true,age=4,waterlogged=false]");
   }

   static {
      MUD = (Material)Objects.requireNonNull(!Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("PODZOL") : Material.getMaterial("MUD"));
      MUD_BRICKS = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("BRICKS") : Material.getMaterial("MUD_BRICKS");
      REINFORCED_DEEPSLATE = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("POLISHED_DIORITE") : Material.getMaterial("REINFORCED_DEEPSLATE");
      MANGROVE_LEAVES = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("OAK_LEAVES") : Material.getMaterial("MANGROVE_LEAVES");
      MANGROVE_LOG = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("OAK_LOG") : Material.getMaterial("MANGROVE_LOG");
      MANGROVE_WOOD = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("OAK_WOOD") : Material.getMaterial("MANGROVE_WOOD");
      MANGROVE_PROPAGULE = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("AIR") : Material.getMaterial("MANGROVE_PROPAGULE");
      MANGROVE_ROOTS = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("OAK_WOOD") : Material.getMaterial("MANGROVE_ROOTS");
      MUDDY_MANGROVE_ROOTS = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("OAK_WOOD") : Material.getMaterial("MUDDY_MANGROVE_ROOTS");
      MANGROVE_FENCE = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("OAK_FENCE") : Material.getMaterial("MANGROVE_FENCE");
      SCULK_VEIN = Material.getMaterial("SCULK_VEIN");
      SCULK = !Version.VERSION.isAtLeast(Version.v1_19_4) ? Material.getMaterial("STONE") : Material.getMaterial("SCULK");
      SCULK_CATALYST = Material.getMaterial("SCULK_CATALYST");
      SCULK_SHRIEKER = Material.getMaterial("SCULK_SHRIEKER");
      SCULK_SENSOR = Material.getMaterial("SCULK_SENSOR");
      MANGROVE_SWAMP = getBiome("MANGROVE_SWAMP", "SWAMP");
      DEEP_DARK = getBiome("DEEP_DARK", "PLAINS");
   }
}
