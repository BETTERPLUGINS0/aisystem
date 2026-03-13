package com.volmit.iris.util.data;

import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.inventorygui.RandomColor;
import org.bukkit.block.Biome;

public class VanillaBiomeMap {
   private static final KMap<Biome, Integer> BIOME_HEX = new KMap();
   private static final KMap<Biome, RandomColor.Color> BIOME_COLOR = new KMap();
   private static final KMap<Biome, RandomColor.Luminosity> BIOME_LUMINOSITY = new KMap();
   private static final KMap<Biome, RandomColor.SaturationType> BIOME_SATURATION = new KMap();
   private static final KMap<Biome, Short> BIOME_IDs = new KMap();

   private static void add(Biome biome, int color, RandomColor.Color randomColor, RandomColor.Luminosity luminosity, RandomColor.SaturationType saturation) {
      BIOME_HEX.put(var0, var1);
      BIOME_COLOR.put(var0, var2);
      if (var3 != null) {
         BIOME_LUMINOSITY.put(var0, var3);
      }

      if (var4 != null) {
         BIOME_SATURATION.put(var0, var4);
      }

      BIOME_IDs.put(var0, (short)var0.ordinal());
   }

   private static void add(Biome biome, int color, RandomColor.Color randomColor, RandomColor.Luminosity luminosity) {
      add(var0, var1, var2, var3, (RandomColor.SaturationType)null);
   }

   public static int getColor(Biome biome) {
      return (Integer)BIOME_HEX.get(var0);
   }

   public static RandomColor.Color getColorType(Biome biome) {
      return (RandomColor.Color)BIOME_COLOR.get(var0);
   }

   public static RandomColor.Luminosity getColorLuminosity(Biome biome) {
      return (RandomColor.Luminosity)BIOME_LUMINOSITY.get(var0);
   }

   public static RandomColor.SaturationType getColorSaturatiom(Biome biome) {
      return (RandomColor.SaturationType)BIOME_SATURATION.get(var0);
   }

   public static short getId(Biome biome) {
      return (Short)BIOME_IDs.get(var0);
   }

   static {
      add(Biome.OCEAN, 112, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.PLAINS, 9286496, RandomColor.Color.GREEN, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.DESERT, 16421912, RandomColor.Color.YELLOW, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.WINDSWEPT_HILLS, 6316128, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.BRIGHT, (RandomColor.SaturationType)null);
      add(Biome.FOREST, 353825, RandomColor.Color.GREEN, RandomColor.Luminosity.BRIGHT, (RandomColor.SaturationType)null);
      add(Biome.TAIGA, 747097, RandomColor.Color.GREEN, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.SWAMP, 522674, RandomColor.Color.ORANGE, RandomColor.Luminosity.DARK, RandomColor.SaturationType.MEDIUM);
      add(Biome.RIVER, 255, RandomColor.Color.BLUE, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.LOW);
      add(Biome.NETHER_WASTES, 12532539, RandomColor.Color.RED, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.THE_END, 8421631, RandomColor.Color.PURPLE, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.LOW);
      add(Biome.FROZEN_OCEAN, 7368918, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.FROZEN_RIVER, 10526975, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.SNOWY_PLAINS, 16777215, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.MUSHROOM_FIELDS, 16711935, RandomColor.Color.PURPLE, RandomColor.Luminosity.BRIGHT, (RandomColor.SaturationType)null);
      add(Biome.BEACH, 16440917, RandomColor.Color.YELLOW, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.LOW);
      add(Biome.JUNGLE, 5470985, RandomColor.Color.GREEN, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.HIGH);
      add(Biome.SPARSE_JUNGLE, 6458135, RandomColor.Color.GREEN, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.HIGH);
      add(Biome.DEEP_OCEAN, 48, RandomColor.Color.BLUE, RandomColor.Luminosity.DARK, (RandomColor.SaturationType)null);
      add(Biome.STONY_SHORE, 10658436, RandomColor.Color.GREEN, RandomColor.Luminosity.DARK, (RandomColor.SaturationType)null);
      add(Biome.SNOWY_BEACH, 16445632, RandomColor.Color.YELLOW, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.BIRCH_FOREST, 3175492, RandomColor.Color.GREEN, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.DARK_FOREST, 4215066, RandomColor.Color.GREEN, RandomColor.Luminosity.DARK, (RandomColor.SaturationType)null);
      add(Biome.SNOWY_TAIGA, 3233098, RandomColor.Color.BLUE, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.OLD_GROWTH_PINE_TAIGA, 5858897, RandomColor.Color.ORANGE, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.WINDSWEPT_FOREST, 5271632, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.BRIGHT, (RandomColor.SaturationType)null);
      add(Biome.SAVANNA, 12431967, RandomColor.Color.GREEN, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.SAVANNA_PLATEAU, 10984804, RandomColor.Color.GREEN, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.BADLANDS, 14238997, RandomColor.Color.ORANGE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.WOODED_BADLANDS, 11573093, RandomColor.Color.ORANGE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.HIGH);
      add(Biome.SMALL_END_ISLANDS, 16718476, RandomColor.Color.PURPLE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.END_MIDLANDS, 8421631, RandomColor.Color.YELLOW, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.LOW);
      add(Biome.END_HIGHLANDS, 8421631, RandomColor.Color.PURPLE, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.LOW);
      add(Biome.END_BARRENS, 8421631, RandomColor.Color.PURPLE, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.WARM_OCEAN, 172, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.LOW);
      add(Biome.LUKEWARM_OCEAN, 144, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.COLD_OCEAN, 2105456, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.HIGH);
      add(Biome.DEEP_LUKEWARM_OCEAN, 64, RandomColor.Color.BLUE, RandomColor.Luminosity.DARK, RandomColor.SaturationType.MEDIUM);
      add(Biome.DEEP_COLD_OCEAN, 2105400, RandomColor.Color.BLUE, RandomColor.Luminosity.DARK, RandomColor.SaturationType.HIGH);
      add(Biome.DEEP_FROZEN_OCEAN, 4210832, RandomColor.Color.BLUE, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.LOW);
      add(Biome.THE_VOID, 0, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.DARK, (RandomColor.SaturationType)null);
      add(Biome.SUNFLOWER_PLAINS, 11918216, RandomColor.Color.GREEN, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.LOW);
      add(Biome.WINDSWEPT_GRAVELLY_HILLS, 7903352, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.FLOWER_FOREST, 2985545, RandomColor.Color.RED, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.LOW);
      add(Biome.ICE_SPIKES, 11853020, RandomColor.Color.BLUE, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.LOW);
      add(Biome.OLD_GROWTH_BIRCH_FOREST, 5807212, RandomColor.Color.GREEN, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.OLD_GROWTH_SPRUCE_TAIGA, 8490617, RandomColor.Color.ORANGE, RandomColor.Luminosity.DARK, RandomColor.SaturationType.HIGH);
      add(Biome.WINDSWEPT_SAVANNA, 15063687, RandomColor.Color.ORANGE, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.HIGH);
      add(Biome.ERODED_BADLANDS, 16739645, RandomColor.Color.ORANGE, RandomColor.Luminosity.LIGHT, RandomColor.SaturationType.HIGH);
      add(Biome.BAMBOO_JUNGLE, 7769620, RandomColor.Color.GREEN, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.HIGH);
      add(Biome.SOUL_SAND_VALLEY, 6174768, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.CRIMSON_FOREST, 14485512, RandomColor.Color.RED, RandomColor.Luminosity.DARK, RandomColor.SaturationType.HIGH);
      add(Biome.WARPED_FOREST, 4821115, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, (RandomColor.SaturationType)null);
      add(Biome.BASALT_DELTAS, 4208182, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.DARK, (RandomColor.SaturationType)null);
      add(Biome.DRIPSTONE_CAVES, 13395456, RandomColor.Color.ORANGE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.LUSH_CAVES, 13056, RandomColor.Color.GREEN, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.MEADOW, 16711935, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.LOW);
      add(Biome.GROVE, 8454016, RandomColor.Color.GREEN, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.SNOWY_SLOPES, 65535, RandomColor.Color.BLUE, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.FROZEN_PEAKS, 10526880, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.JAGGED_PEAKS, 4029378, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.BRIGHT, RandomColor.SaturationType.MEDIUM);
      add(Biome.STONY_PEAKS, 8947848, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.LIGHT, (RandomColor.SaturationType)null);
      add(Biome.CUSTOM, 16777215, RandomColor.Color.MONOCHROME, RandomColor.Luminosity.DARK, RandomColor.SaturationType.MONOCHROME);
   }
}
