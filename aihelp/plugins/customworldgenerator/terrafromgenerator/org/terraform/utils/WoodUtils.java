package org.terraform.utils;

import java.util.Objects;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;

public class WoodUtils {
   @NotNull
   public static Material getWoodForBiome(@NotNull BiomeBank biome, @NotNull WoodUtils.WoodType wood) {
      Material var10000;
      switch(biome) {
      case BADLANDS:
      case BADLANDS_RIVER:
      case SAVANNA:
      case DESERT_MOUNTAINS:
      case DESERT:
      case DESERT_RIVER:
      case BADLANDS_BEACH:
      case BADLANDS_CANYON:
         var10000 = wood.getWood(WoodUtils.WoodSpecies.ACACIA);
         break;
      case BIRCH_MOUNTAINS:
      case SCARLET_FOREST:
         var10000 = wood.getWood(WoodUtils.WoodSpecies.BIRCH);
         break;
      case COLD_OCEAN:
      case WARM_OCEAN:
      case SWAMP:
      case PLAINS:
      case OCEAN:
      case MUDFLATS:
      case CORAL_REEF_OCEAN:
      case DEEP_LUKEWARM_OCEAN:
      case DEEP_OCEAN:
      case DEEP_WARM_OCEAN:
      case DEEP_DRY_OCEAN:
      case DEEP_HUMID_OCEAN:
      case DRY_OCEAN:
      case HUMID_OCEAN:
      case RIVER:
      case ERODED_PLAINS:
      case FOREST:
         var10000 = wood.getWood(WoodUtils.WoodSpecies.OAK);
         break;
      case FROZEN_OCEAN:
      case TAIGA:
      case SNOWY_WASTELAND:
      case SNOWY_TAIGA:
      case SNOWY_MOUNTAINS:
      case ROCKY_MOUNTAINS:
      case ROCKY_BEACH:
      case FROZEN_RIVER:
      case DEEP_COLD_OCEAN:
      case DEEP_FROZEN_OCEAN:
      case ICY_BEACH:
      case ICE_SPIKES:
         var10000 = wood.getWood(WoodUtils.WoodSpecies.SPRUCE);
         break;
      case SANDY_BEACH:
      case JUNGLE:
      case JUNGLE_RIVER:
      case BAMBOO_FOREST:
         var10000 = wood.getWood(WoodUtils.WoodSpecies.JUNGLE);
         break;
      case BLACK_OCEAN:
      case DEEP_BLACK_OCEAN:
      case CHERRY_GROVE:
      case DARK_FOREST:
      case DARK_FOREST_RIVER:
      case DARK_FOREST_BEACH:
         var10000 = wood.getWood(WoodUtils.WoodSpecies.DARK_OAK);
         break;
      default:
         var10000 = wood.getWood(WoodUtils.WoodSpecies.OAK);
      }

      return var10000;
   }

   public static enum WoodSpecies {
      OAK,
      SPRUCE,
      BIRCH,
      JUNGLE,
      ACACIA,
      DARK_OAK;

      // $FF: synthetic method
      private static WoodUtils.WoodSpecies[] $values() {
         return new WoodUtils.WoodSpecies[]{OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK};
      }
   }

   public static enum WoodType {
      PLANKS("%WOOD%_PLANKS"),
      SAPLING("%WOOD%_SAPLING"),
      POTTED_SAPLING("POTTED_%WOOD%_SAPLING"),
      LOG("%WOOD%_LOG"),
      STRIPPED_LOG("STRIPPED_%WOOD%_LOG"),
      WOOD("%WOOD%_WOOD"),
      STRIPPED_WOOD("STRIPPED_%WOOD%_WOOD"),
      LEAVES("%WOOD%_LEAVES"),
      SLAB("%WOOD%_SLAB"),
      PRESSURE_PLATE("%WOOD%_PRESSURE_PLATE"),
      FENCE("%WOOD%_FENCE"),
      TRAPDOOR("%WOOD%_TRAPDOOR"),
      FENCE_GATE("%WOOD%_FENCE_GATE"),
      STAIRS("%WOOD%_STAIRS"),
      BUTTON("%WOOD%_BUTTON"),
      DOOR("%WOOD%_DOOR"),
      SIGN("%WOOD%_SIGN"),
      WALL_SIGN("%WOOD%_WALL_SIGN"),
      BOAT("%WOOD%_BOAT");

      final String template;

      private WoodType(String param3) {
         this.template = template;
      }

      @NotNull
      public static WoodUtils.WoodType parse(@NotNull Material oak) {
         return valueOf(oak.toString().replace("DARK_OAK", "OAK").replace("OAK_", ""));
      }

      @NotNull
      public Material getWood(@NotNull WoodUtils.WoodSpecies species) {
         return (Material)Objects.requireNonNull(Material.getMaterial(this.template.replace("%WOOD%", species.toString())));
      }

      // $FF: synthetic method
      private static WoodUtils.WoodType[] $values() {
         return new WoodUtils.WoodType[]{PLANKS, SAPLING, POTTED_SAPLING, LOG, STRIPPED_LOG, WOOD, STRIPPED_WOOD, LEAVES, SLAB, PRESSURE_PLATE, FENCE, TRAPDOOR, FENCE_GATE, STAIRS, BUTTON, DOOR, SIGN, WALL_SIGN, BOAT};
      }
   }
}
