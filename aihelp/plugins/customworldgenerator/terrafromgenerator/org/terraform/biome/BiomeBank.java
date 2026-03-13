package org.terraform.biome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.biome.beach.BadlandsBeachHandler;
import org.terraform.biome.beach.BlackOceanBeachHandler;
import org.terraform.biome.beach.BogBeachHandler;
import org.terraform.biome.beach.CherryGroveBeachHandler;
import org.terraform.biome.beach.DarkForestBeachHandler;
import org.terraform.biome.beach.IcyBeachHandler;
import org.terraform.biome.beach.MudflatsHandler;
import org.terraform.biome.beach.MushroomBeachHandler;
import org.terraform.biome.beach.RockBeachHandler;
import org.terraform.biome.beach.SandyBeachHandler;
import org.terraform.biome.beach.ScarletForestBeachHandler;
import org.terraform.biome.cavepopulators.AbstractCavePopulator;
import org.terraform.biome.cavepopulators.ForestedMountainsCavePopulator;
import org.terraform.biome.cavepopulators.FrozenCavePopulator;
import org.terraform.biome.cavepopulators.MossyCavePopulator;
import org.terraform.biome.flat.ArchedCliffsHandler;
import org.terraform.biome.flat.BadlandsHandler;
import org.terraform.biome.flat.BambooForestHandler;
import org.terraform.biome.flat.CherryGroveHandler;
import org.terraform.biome.flat.DarkForestHandler;
import org.terraform.biome.flat.DesertHandler;
import org.terraform.biome.flat.ElevatedPlainsHandler;
import org.terraform.biome.flat.ErodedPlainsHandler;
import org.terraform.biome.flat.FlowerForestHandler;
import org.terraform.biome.flat.ForestHandler;
import org.terraform.biome.flat.GorgeHandler;
import org.terraform.biome.flat.IceSpikesHandler;
import org.terraform.biome.flat.JungleHandler;
import org.terraform.biome.flat.MangroveHandler;
import org.terraform.biome.flat.MeadowHandler;
import org.terraform.biome.flat.MuddyBogHandler;
import org.terraform.biome.flat.PaleForestHandler;
import org.terraform.biome.flat.PetrifiedCliffsHandler;
import org.terraform.biome.flat.PlainsHandler;
import org.terraform.biome.flat.SavannaHandler;
import org.terraform.biome.flat.ScarletForestHandler;
import org.terraform.biome.flat.SnowyTaigaHandler;
import org.terraform.biome.flat.SnowyWastelandHandler;
import org.terraform.biome.flat.SparseJungleHandler;
import org.terraform.biome.flat.SwampHandler;
import org.terraform.biome.flat.TaigaHandler;
import org.terraform.biome.mountainous.BadlandsCanyonHandler;
import org.terraform.biome.mountainous.BirchMountainsHandler;
import org.terraform.biome.mountainous.DesertHillsHandler;
import org.terraform.biome.mountainous.ForestedMountainsHandler;
import org.terraform.biome.mountainous.JaggedPeaksHandler;
import org.terraform.biome.mountainous.PaintedHillsHandler;
import org.terraform.biome.mountainous.RockyMountainsHandler;
import org.terraform.biome.mountainous.ShatteredSavannaHandler;
import org.terraform.biome.mountainous.SnowyMountainsHandler;
import org.terraform.biome.ocean.BlackOceansHandler;
import org.terraform.biome.ocean.ColdOceansHandler;
import org.terraform.biome.ocean.CoralReefOceanHandler;
import org.terraform.biome.ocean.FrozenOceansHandler;
import org.terraform.biome.ocean.LukewarmOceansHandler;
import org.terraform.biome.ocean.MushroomIslandHandler;
import org.terraform.biome.ocean.OceansHandler;
import org.terraform.biome.ocean.WarmOceansHandler;
import org.terraform.biome.river.BadlandsRiverHandler;
import org.terraform.biome.river.BogRiverHandler;
import org.terraform.biome.river.CherryGroveRiverHandler;
import org.terraform.biome.river.DarkForestRiverHandler;
import org.terraform.biome.river.DesertRiverHandler;
import org.terraform.biome.river.FrozenRiverHandler;
import org.terraform.biome.river.JungleRiverHandler;
import org.terraform.biome.river.RiverHandler;
import org.terraform.biome.river.ScarletForestRiverHandler;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.CoordPair;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.utils.datastructs.ConcurrentLRUCache;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;
import org.terraform.utils.version.Version;

public enum BiomeBank {
   SNOWY_MOUNTAINS(new SnowyMountainsHandler(), BiomeType.MOUNTAINOUS, BiomeClimate.SNOWY, TConfig.c.BIOME_SNOWY_MOUNTAINS_WEIGHT, new FrozenCavePopulator()),
   BIRCH_MOUNTAINS(new BirchMountainsHandler(), BiomeType.MOUNTAINOUS, BiomeClimate.COLD, TConfig.c.BIOME_BIRCH_MOUNTAINS_WEIGHT),
   ROCKY_MOUNTAINS(new RockyMountainsHandler(), BiomeType.MOUNTAINOUS, BiomeClimate.TRANSITION, TConfig.c.BIOME_ROCKY_MOUNTAINS_WEIGHT),
   FORESTED_MOUNTAINS(new ForestedMountainsHandler(), BiomeType.MOUNTAINOUS, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_FORESTED_MOUNTAINS_WEIGHT, new ForestedMountainsCavePopulator()),
   SHATTERED_SAVANNA(new ShatteredSavannaHandler(), BiomeType.MOUNTAINOUS, BiomeClimate.DRY_VEGETATION, TConfig.c.BIOME_SHATTERED_SAVANNA_WEIGHT),
   PAINTED_HILLS(new PaintedHillsHandler(), BiomeType.MOUNTAINOUS, BiomeClimate.DRY_VEGETATION, TConfig.c.BIOME_PAINTED_HILLS_WEIGHT),
   BADLANDS_CANYON(new BadlandsCanyonHandler(), BiomeType.MOUNTAINOUS, BiomeClimate.HOT_BARREN, TConfig.c.BIOME_BADLANDS_MOUNTAINS_WEIGHT),
   DESERT_MOUNTAINS(new DesertHillsHandler(), BiomeType.MOUNTAINOUS, BiomeClimate.HOT_BARREN, TConfig.c.BIOME_DESERT_MOUNTAINS_WEIGHT),
   JAGGED_PEAKS(new JaggedPeaksHandler(), BiomeType.HIGH_MOUNTAINOUS, BiomeClimate.SNOWY, TConfig.c.BIOME_JAGGED_PEAKS_WEIGHT, new FrozenCavePopulator()),
   COLD_JAGGED_PEAKS(new JaggedPeaksHandler(), BiomeType.HIGH_MOUNTAINOUS, BiomeClimate.COLD, TConfig.c.BIOME_JAGGED_PEAKS_WEIGHT, new FrozenCavePopulator()),
   TRANSITION_JAGGED_PEAKS(new JaggedPeaksHandler(), BiomeType.HIGH_MOUNTAINOUS, BiomeClimate.TRANSITION, TConfig.c.BIOME_JAGGED_PEAKS_WEIGHT, new FrozenCavePopulator()),
   FORESTED_PEAKS(new ForestedMountainsHandler(), BiomeType.HIGH_MOUNTAINOUS, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_FORESTED_MOUNTAINS_WEIGHT, new ForestedMountainsCavePopulator()),
   SHATTERED_SAVANNA_PEAK(new ShatteredSavannaHandler(), BiomeType.HIGH_MOUNTAINOUS, BiomeClimate.DRY_VEGETATION, TConfig.c.BIOME_SHATTERED_SAVANNA_WEIGHT),
   BADLANDS_CANYON_PEAK(new BadlandsCanyonHandler(), BiomeType.HIGH_MOUNTAINOUS, BiomeClimate.HOT_BARREN, TConfig.c.BIOME_BADLANDS_MOUNTAINS_WEIGHT),
   OCEAN(new OceansHandler(BiomeType.OCEANIC), BiomeType.OCEANIC, BiomeClimate.TRANSITION, TConfig.c.BIOME_OCEAN_WEIGHT),
   BLACK_OCEAN(new BlackOceansHandler(BiomeType.OCEANIC), BiomeType.OCEANIC, BiomeClimate.TRANSITION, TConfig.c.BIOME_BLACK_OCEAN_WEIGHT),
   COLD_OCEAN(new ColdOceansHandler(BiomeType.OCEANIC), BiomeType.OCEANIC, BiomeClimate.COLD, TConfig.c.BIOME_COLD_OCEAN_WEIGHT),
   FROZEN_OCEAN(new FrozenOceansHandler(BiomeType.OCEANIC), BiomeType.OCEANIC, BiomeClimate.SNOWY, TConfig.c.BIOME_FROZEN_OCEAN_WEIGHT, new FrozenCavePopulator()),
   WARM_OCEAN(new WarmOceansHandler(BiomeType.OCEANIC), BiomeType.OCEANIC, BiomeClimate.HOT_BARREN, TConfig.c.BIOME_WARM_OCEAN_WEIGHT),
   HUMID_OCEAN(new WarmOceansHandler(BiomeType.OCEANIC), BiomeType.OCEANIC, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_HUMID_OCEAN_WEIGHT),
   DRY_OCEAN(new WarmOceansHandler(BiomeType.OCEANIC), BiomeType.OCEANIC, BiomeClimate.DRY_VEGETATION, TConfig.c.BIOME_DRY_OCEAN_WEIGHT),
   CORAL_REEF_OCEAN(new CoralReefOceanHandler(BiomeType.OCEANIC), BiomeType.OCEANIC, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_CORALREEF_OCEAN_WEIGHT),
   RIVER(new RiverHandler(), BiomeType.RIVER, BiomeClimate.TRANSITION),
   BOG_RIVER(new BogRiverHandler(), BiomeType.RIVER, BiomeClimate.DRY_VEGETATION),
   CHERRY_GROVE_RIVER(new CherryGroveRiverHandler(), BiomeType.RIVER, BiomeClimate.COLD),
   SCARLET_FOREST_RIVER(new ScarletForestRiverHandler(), BiomeType.RIVER, BiomeClimate.COLD),
   JUNGLE_RIVER(new JungleRiverHandler(), BiomeType.RIVER, BiomeClimate.HUMID_VEGETATION),
   FROZEN_RIVER(new FrozenRiverHandler(), BiomeType.RIVER, BiomeClimate.SNOWY, new FrozenCavePopulator()),
   DARK_FOREST_RIVER(new DarkForestRiverHandler(), BiomeType.RIVER, BiomeClimate.HUMID_VEGETATION, new FrozenCavePopulator()),
   DESERT_RIVER(new DesertRiverHandler(), BiomeType.RIVER, BiomeClimate.HOT_BARREN),
   BADLANDS_RIVER(new BadlandsRiverHandler(), BiomeType.RIVER, BiomeClimate.HOT_BARREN),
   DEEP_OCEAN(new OceansHandler(BiomeType.DEEP_OCEANIC), BiomeType.DEEP_OCEANIC, BiomeClimate.TRANSITION, TConfig.c.BIOME_DEEP_OCEAN_WEIGHT),
   DEEP_COLD_OCEAN(new ColdOceansHandler(BiomeType.DEEP_OCEANIC), BiomeType.DEEP_OCEANIC, BiomeClimate.COLD, TConfig.c.BIOME_DEEP_COLD_OCEAN_WEIGHT),
   DEEP_BLACK_OCEAN(new BlackOceansHandler(BiomeType.DEEP_OCEANIC), BiomeType.DEEP_OCEANIC, BiomeClimate.TRANSITION, TConfig.c.BIOME_DEEP_BLACK_OCEAN_WEIGHT),
   DEEP_FROZEN_OCEAN(new FrozenOceansHandler(BiomeType.DEEP_OCEANIC), BiomeType.DEEP_OCEANIC, BiomeClimate.SNOWY, TConfig.c.BIOME_DEEP_FROZEN_OCEAN_WEIGHT, new FrozenCavePopulator()),
   DEEP_WARM_OCEAN(new WarmOceansHandler(BiomeType.DEEP_OCEANIC), BiomeType.DEEP_OCEANIC, BiomeClimate.HOT_BARREN, TConfig.c.BIOME_DEEP_WARM_OCEAN_WEIGHT),
   DEEP_HUMID_OCEAN(new WarmOceansHandler(BiomeType.DEEP_OCEANIC), BiomeType.DEEP_OCEANIC, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_DEEP_HUMID_OCEAN_WEIGHT),
   DEEP_DRY_OCEAN(new WarmOceansHandler(BiomeType.DEEP_OCEANIC), BiomeType.DEEP_OCEANIC, BiomeClimate.DRY_VEGETATION, TConfig.c.BIOME_DEEP_DRY_OCEAN_WEIGHT),
   DEEP_LUKEWARM_OCEAN(new LukewarmOceansHandler(BiomeType.DEEP_OCEANIC), BiomeType.DEEP_OCEANIC, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_DEEP_LUKEWARM_OCEAN_WEIGHT),
   MUSHROOM_ISLANDS(new MushroomIslandHandler(), BiomeType.DEEP_OCEANIC, BiomeClimate.TRANSITION, TConfig.c.BIOME_MUSHROOM_ISLAND_WEIGHT),
   PLAINS(new PlainsHandler(), BiomeType.FLAT, BiomeClimate.TRANSITION, TConfig.c.BIOME_PLAINS_WEIGHT),
   MEADOW(new MeadowHandler(), BiomeType.FLAT, BiomeClimate.TRANSITION, TConfig.c.BIOME_MEADOW_WEIGHT),
   ELEVATED_PLAINS(new ElevatedPlainsHandler(), BiomeType.FLAT, BiomeClimate.TRANSITION, TConfig.c.BIOME_ELEVATED_PLAINS_WEIGHT),
   GORGE(new GorgeHandler(), BiomeType.FLAT, BiomeClimate.TRANSITION, TConfig.c.BIOME_GORGE_WEIGHT),
   PETRIFIED_CLIFFS(new PetrifiedCliffsHandler(), BiomeType.FLAT, BiomeClimate.TRANSITION, TConfig.c.BIOME_PETRIFIEDCLIFFS_WEIGHT),
   ARCHED_CLIFFS(new ArchedCliffsHandler(), BiomeType.FLAT, BiomeClimate.TRANSITION, TConfig.c.BIOME_ARCHED_CLIFFS_WEIGHT),
   SAVANNA(new SavannaHandler(), BiomeType.FLAT, BiomeClimate.DRY_VEGETATION, TConfig.c.BIOME_SAVANNA_WEIGHT),
   MUDDY_BOG(new MuddyBogHandler(), BiomeType.FLAT, BiomeClimate.DRY_VEGETATION, TConfig.c.BIOME_MUDDYBOG_WEIGHT),
   FOREST(new ForestHandler(), BiomeType.FLAT, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_FOREST_WEIGHT),
   FLOWER_FOREST(new FlowerForestHandler(), BiomeType.FLAT, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_FLOWERFOREST_WEIGHT),
   JUNGLE(new JungleHandler(), BiomeType.FLAT, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_JUNGLE_WEIGHT),
   SPARSE_JUNGLE(new SparseJungleHandler(), BiomeType.FLAT, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_SPARSE_JUNGLE_WEIGHT),
   BAMBOO_FOREST(new BambooForestHandler(), BiomeType.FLAT, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_BAMBOO_FOREST_WEIGHT),
   DESERT(new DesertHandler(), BiomeType.FLAT, BiomeClimate.HOT_BARREN, TConfig.c.BIOME_DESERT_WEIGHT),
   BADLANDS(new BadlandsHandler(), BiomeType.FLAT, BiomeClimate.HOT_BARREN, TConfig.c.BIOME_BADLANDS_WEIGHT),
   ERODED_PLAINS(new ErodedPlainsHandler(), BiomeType.FLAT, BiomeClimate.COLD, TConfig.c.BIOME_ERODED_PLAINS_WEIGHT),
   SCARLET_FOREST(new ScarletForestHandler(), BiomeType.FLAT, BiomeClimate.COLD, TConfig.c.BIOME_SCARLETFOREST_WEIGHT),
   CHERRY_GROVE(new CherryGroveHandler(), BiomeType.FLAT, BiomeClimate.COLD, TConfig.c.BIOME_CHERRYGROVE_WEIGHT),
   TAIGA(new TaigaHandler(), BiomeType.FLAT, BiomeClimate.COLD, TConfig.c.BIOME_TAIGA_WEIGHT),
   SNOWY_TAIGA(new SnowyTaigaHandler(), BiomeType.FLAT, BiomeClimate.SNOWY, TConfig.c.BIOME_SNOWY_TAIGA_WEIGHT, new FrozenCavePopulator()),
   SNOWY_WASTELAND(new SnowyWastelandHandler(), BiomeType.FLAT, BiomeClimate.SNOWY, TConfig.c.BIOME_SNOWY_WASTELAND_WEIGHT, new FrozenCavePopulator()),
   ICE_SPIKES(new IceSpikesHandler(), BiomeType.FLAT, BiomeClimate.SNOWY, TConfig.c.BIOME_ICE_SPIKES_WEIGHT, new FrozenCavePopulator()),
   DARK_FOREST(new DarkForestHandler(), BiomeType.FLAT, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_DARK_FOREST_WEIGHT),
   PALE_FOREST(new PaleForestHandler(), BiomeType.FLAT, BiomeClimate.HUMID_VEGETATION, Version.VERSION.isAtLeast(Version.v1_21_4) ? TConfig.c.BIOME_PALE_FOREST_WEIGHT : 0),
   SWAMP(new SwampHandler(), BiomeType.FLAT, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_SWAMP_WEIGHT),
   MANGROVE(new MangroveHandler(), BiomeType.FLAT, BiomeClimate.HUMID_VEGETATION, TConfig.c.BIOME_MANGROVE_WEIGHT),
   SANDY_BEACH(new SandyBeachHandler(), BiomeType.BEACH, BiomeClimate.TRANSITION),
   BOG_BEACH(new BogBeachHandler(), BiomeType.BEACH, BiomeClimate.DRY_VEGETATION),
   DARK_FOREST_BEACH(new DarkForestBeachHandler(), BiomeType.BEACH, BiomeClimate.HUMID_VEGETATION),
   BADLANDS_BEACH(new BadlandsBeachHandler(), BiomeType.BEACH, BiomeClimate.HOT_BARREN),
   MUSHROOM_BEACH(new MushroomBeachHandler(), BiomeType.BEACH, BiomeClimate.TRANSITION),
   BLACK_OCEAN_BEACH(new BlackOceanBeachHandler(), BiomeType.BEACH, BiomeClimate.COLD),
   ROCKY_BEACH(new RockBeachHandler(), BiomeType.BEACH, BiomeClimate.COLD),
   ICY_BEACH(new IcyBeachHandler(), BiomeType.BEACH, BiomeClimate.SNOWY, new FrozenCavePopulator()),
   MUDFLATS(new MudflatsHandler(), BiomeType.BEACH, BiomeClimate.HUMID_VEGETATION),
   CHERRY_GROVE_BEACH(new CherryGroveBeachHandler(), BiomeType.BEACH, BiomeClimate.COLD),
   SCARLET_FOREST_BEACH(new ScarletForestBeachHandler(), BiomeType.BEACH, BiomeClimate.COLD);

   private static final ConcurrentLRUCache<BiomeSection, BiomeSection> BIOMESECTION_CACHE = new ConcurrentLRUCache("BIOMESECTION_CACHE", 250, (key) -> {
      key.doCalculations();
      return key;
   });
   public static boolean debugPrint = false;
   @Nullable
   public static BiomeBank singleLand = null;
   @Nullable
   public static BiomeBank singleOcean = null;
   @Nullable
   public static BiomeBank singleDeepOcean = null;
   @Nullable
   public static BiomeBank singleMountain = null;
   @Nullable
   public static BiomeBank singleHighMountain = null;
   private final BiomeHandler handler;
   private final BiomeType type;
   private final AbstractCavePopulator cavePop;
   private final BiomeClimate climate;
   private final int biomeWeight;

   private BiomeBank(BiomeHandler param3, BiomeType param4, BiomeClimate param5) {
      this.handler = handler;
      this.type = type;
      this.climate = climate;
      this.biomeWeight = 0;
      this.cavePop = new MossyCavePopulator();
   }

   private BiomeBank(BiomeHandler param3, BiomeType param4, BiomeClimate param5, AbstractCavePopulator param6) {
      this.handler = handler;
      this.type = type;
      this.climate = climate;
      this.biomeWeight = 0;
      this.cavePop = cavePop;
   }

   private BiomeBank(BiomeHandler param3, BiomeType param4, BiomeClimate param5, int param6) {
      this.handler = handler;
      this.type = type;
      this.climate = climate;
      this.biomeWeight = biomeWeight;
      this.cavePop = new MossyCavePopulator();
   }

   private BiomeBank(BiomeHandler param3, BiomeType param4, BiomeClimate param5, int param6, AbstractCavePopulator param7) {
      this.handler = handler;
      this.type = type;
      this.climate = climate;
      this.cavePop = cavePop;
      this.biomeWeight = biomeWeight;
   }

   @NotNull
   public static BiomeSection getBiomeSectionFromBlockCoords(TerraformWorld tw, int x, int z) {
      BiomeSection sect = new BiomeSection(tw, x, z);
      sect = (BiomeSection)BIOMESECTION_CACHE.get(sect);
      return sect;
   }

   @NotNull
   public static BiomeSection getBiomeSectionFromChunk(TerraformWorld tw, int chunkX, int chunkZ) {
      BiomeSection sect = new BiomeSection(tw, chunkX << 4, chunkZ << 4);
      sect = (BiomeSection)BIOMESECTION_CACHE.get(sect);
      return sect;
   }

   @NotNull
   public static BiomeSection getBiomeSectionFromSectionCoords(TerraformWorld tw, int x, int z, boolean useSectionCoords) {
      BiomeSection sect = new BiomeSection(tw, x, z, useSectionCoords);
      sect = (BiomeSection)BIOMESECTION_CACHE.get(sect);
      return sect;
   }

   @NotNull
   public static BiomeBank calculateBiome(@NotNull TerraformWorld tw, int rawX, int height, int rawZ) {
      if (debugPrint) {
         TerraformGeneratorPlugin.logger.info("calculateBiome called with args: " + tw.getName() + "," + rawX + "," + height + "," + rawZ);
      }

      BiomeBank bank = calculateHeightIndependentBiome(tw, rawX, rawZ);
      FastNoise beachNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_BEACH_HEIGHT, (world) -> {
         FastNoise n = new FastNoise((int)world.getSeed());
         n.SetNoiseType(FastNoise.NoiseType.PerlinFractal);
         n.SetFrequency(0.01F);
         n.SetFractalOctaves(4);
         return n;
      });
      if (height < TerraformGenerator.seaLevel && (double)height + HeightMap.getRawRiverDepth(tw, rawX, rawZ) >= (double)TerraformGenerator.seaLevel) {
         bank = bank.getHandler().getRiverType();
         if (debugPrint) {
            TerraformGeneratorPlugin.logger.info("calculateBiome -> River Detected");
         }
      } else if (height >= TerraformGenerator.seaLevel && (float)height <= (float)TerraformGenerator.seaLevel + 8.0F * Math.abs(beachNoise.GetNoise((float)rawX, (float)rawZ))) {
         bank = bank.getHandler().getBeachType();
         if (debugPrint) {
            TerraformGeneratorPlugin.logger.info("calculateBiome -> Beach calculated");
         }
      }

      if (bank != SWAMP && bank != MANGROVE && height < TerraformGenerator.seaLevel && bank.isDry()) {
         bank = bank.getHandler().getRiverType();
         if (debugPrint) {
            TerraformGeneratorPlugin.logger.info("calculateBiome -> Biome is submerged, defaulting to river");
         }
      }

      if (!bank.isDry() && height >= TerraformGenerator.seaLevel) {
         if (debugPrint) {
            TerraformGeneratorPlugin.logger.info("calculateBiome -> Submerged biome above ground detected");
         }

         BiomeBank replacement = null;
         if (!bank.getHandler().forceDefaultToBeach()) {
            int highestDom = Integer.MIN_VALUE;
            Iterator var8 = BiomeSection.getSurroundingSections(tw, rawX, rawZ).iterator();

            while(var8.hasNext()) {
               BiomeSection sect = (BiomeSection)var8.next();
               if (debugPrint) {
                  TerraformGeneratorPlugin.logger.info("calculateBiome -> -> Comparison Section: " + sect.toString());
               }

               if (sect.getBiomeBank().isDry()) {
                  int compDist = (int)sect.getDominanceBasedOnRadius(rawX, rawZ);
                  if (debugPrint) {
                     TerraformGeneratorPlugin.logger.info("calculateBiome -> -> -> Dominance: " + compDist);
                  }

                  if (compDist > highestDom) {
                     replacement = sect.getBiomeBank();
                     highestDom = compDist;
                  }
               }
            }
         }

         bank = replacement == null ? bank.getHandler().getBeachType() : replacement;
         if (debugPrint) {
            TerraformGeneratorPlugin.logger.info("calculateBiome -> -> Submerged biome defaulted to: " + String.valueOf(replacement));
         }
      }

      if (debugPrint) {
         TerraformGeneratorPlugin.logger.info("calculateBiome -> Evaluated: " + String.valueOf(bank));
      }

      return bank;
   }

   @NotNull
   public static BiomeBank calculateHeightIndependentBiome(TerraformWorld tw, int x, int z) {
      BiomeSection mostDominant = BiomeSection.getMostDominantSection(tw, x, z);
      return mostDominant.getBiomeBank();
   }

   public static void initSinglesConfig() {
      try {
         singleLand = valueOf(TConfig.c.BIOME_SINGLE_TERRESTRIAL_TYPE.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException var5) {
         singleLand = null;
      }

      try {
         singleOcean = valueOf(TConfig.c.BIOME_SINGLE_OCEAN_TYPE.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException var4) {
         singleOcean = null;
      }

      try {
         singleDeepOcean = valueOf(TConfig.c.BIOME_SINGLE_DEEPOCEAN_TYPE.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException var3) {
         singleDeepOcean = null;
      }

      try {
         singleMountain = valueOf(TConfig.c.BIOME_SINGLE_MOUNTAIN_TYPE.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException var2) {
         singleMountain = null;
      }

      try {
         singleHighMountain = valueOf(TConfig.c.BIOME_SINGLE_HIGHMOUNTAIN_TYPE.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException var1) {
         singleHighMountain = null;
      }

   }

   public static boolean isBiomeEnabled(@NotNull BiomeBank bank) {
      if (bank.getBiomeWeight() <= 0) {
         return false;
      } else {
         boolean var10000;
         switch(bank.getType()) {
         case BEACH:
         case RIVER:
            var10000 = true;
            break;
         case DEEP_OCEANIC:
            var10000 = singleDeepOcean == null || singleDeepOcean == bank;
            break;
         case FLAT:
            var10000 = singleLand == null || singleLand == bank;
            break;
         case HIGH_MOUNTAINOUS:
            var10000 = singleHighMountain == null || singleHighMountain == bank;
            break;
         case MOUNTAINOUS:
            var10000 = singleMountain == null || singleMountain == bank;
            break;
         case OCEANIC:
            var10000 = singleOcean == null || singleOcean == bank;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         return var10000;
      }
   }

   @NotNull
   public static BiomeBank selectBiome(@NotNull BiomeSection section, double temperature, double moisture) {
      Random sectionRand = section.getSectionRandom();
      if (TConfig.c.BIOME_FORCE_RADIUS > 0) {
         CoordPair lowerZoneBound = new CoordPair(-TConfig.c.BIOME_FORCE_RADIUS >> BiomeSection.bitshifts, -TConfig.c.BIOME_FORCE_RADIUS >> BiomeSection.bitshifts);
         CoordPair upperZoneBound = new CoordPair(TConfig.c.BIOME_FORCE_RADIUS >> BiomeSection.bitshifts, TConfig.c.BIOME_FORCE_RADIUS >> BiomeSection.bitshifts);
         if (lowerZoneBound.x() <= section.getX() && section.getX() <= upperZoneBound.x() && lowerZoneBound.z() <= section.getZ() && section.getZ() <= upperZoneBound.z()) {
            return valueOf(TConfig.c.BIOME_FORCED_BIOME);
         }
      }

      BiomeType targetType = BiomeType.FLAT;
      BiomeClimate climate = BiomeClimate.selectClimate(temperature, moisture);
      double oceanicNoise = section.getOceanLevel();
      if (!(oceanicNoise < 0.0D) && !(TConfig.c.BIOME_OCEANIC_THRESHOLD < 0.0F)) {
         double mountainousNoise = section.getMountainLevel();
         if (mountainousNoise > 0.0D) {
            if (mountainousNoise >= (double)TConfig.c.BIOME_HIGH_MOUNTAINOUS_THRESHOLD) {
               targetType = BiomeType.HIGH_MOUNTAINOUS;
            } else if (mountainousNoise >= (double)TConfig.c.BIOME_MOUNTAINOUS_THRESHOLD) {
               targetType = BiomeType.MOUNTAINOUS;
            }
         }
      } else {
         oceanicNoise = Math.abs(oceanicNoise);
         if (oceanicNoise >= (double)TConfig.c.BIOME_DEEP_OCEANIC_THRESHOLD) {
            targetType = BiomeType.DEEP_OCEANIC;
         } else if (oceanicNoise >= (double)TConfig.c.BIOME_OCEANIC_THRESHOLD) {
            targetType = BiomeType.OCEANIC;
         }
      }

      switch(targetType) {
      case DEEP_OCEANIC:
         if (singleDeepOcean != null) {
            return singleDeepOcean;
         }
         break;
      case FLAT:
         if (singleLand != null) {
            return singleLand;
         }
         break;
      case HIGH_MOUNTAINOUS:
         if (singleHighMountain != null) {
            return singleHighMountain;
         }
         break;
      case MOUNTAINOUS:
         if (singleMountain != null) {
            return singleMountain;
         }
         break;
      case OCEANIC:
         if (singleOcean != null) {
            return singleOcean;
         }
      }

      ArrayList<BiomeBank> contenders = new ArrayList();
      BiomeBank[] var11 = values();
      int var12 = var11.length;

      for(int var13 = 0; var13 < var12; ++var13) {
         BiomeBank biome = var11[var13];
         if (biome.biomeWeight > 0 && biome.getType() == targetType && biome.climate == climate) {
            for(int i = 0; i < biome.biomeWeight; ++i) {
               contenders.add(biome);
            }
         }
      }

      Collections.shuffle(contenders, sectionRand);
      if (contenders.isEmpty()) {
         TerraformGeneratorPlugin.logger.info("Defaulted for: " + temperature + " : " + moisture + "," + String.valueOf(climate) + ":" + String.valueOf(targetType));
         BiomeBank var10000;
         switch(targetType) {
         case BEACH:
            var10000 = valueOf(TConfig.c.BIOME_DEFAULT_BEACH);
            break;
         case RIVER:
            var10000 = valueOf(TConfig.c.BIOME_DEFAULT_RIVER);
            break;
         case DEEP_OCEANIC:
            var10000 = valueOf(TConfig.c.BIOME_DEFAULT_DEEPOCEANIC);
            break;
         case FLAT:
            var10000 = valueOf(TConfig.c.BIOME_DEFAULT_FLAT);
            break;
         case HIGH_MOUNTAINOUS:
            var10000 = valueOf(TConfig.c.BIOME_DEFAULT_HIGHMOUNTAINOUS);
            break;
         case MOUNTAINOUS:
            var10000 = valueOf(TConfig.c.BIOME_DEFAULT_MOUNTAINOUS);
            break;
         case OCEANIC:
            var10000 = valueOf(TConfig.c.BIOME_DEFAULT_OCEANIC);
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         return var10000;
      } else {
         return (BiomeBank)contenders.get(0);
      }
   }

   public AbstractCavePopulator getCavePop() {
      return this.cavePop;
   }

   public BiomeType getType() {
      return this.type;
   }

   public BiomeHandler getHandler() {
      return this.handler;
   }

   public BiomeClimate getClimate() {
      return this.climate;
   }

   public int getBiomeWeight() {
      return this.biomeWeight;
   }

   public boolean isDry() {
      return this.getType().isDry();
   }

   // $FF: synthetic method
   private static BiomeBank[] $values() {
      return new BiomeBank[]{SNOWY_MOUNTAINS, BIRCH_MOUNTAINS, ROCKY_MOUNTAINS, FORESTED_MOUNTAINS, SHATTERED_SAVANNA, PAINTED_HILLS, BADLANDS_CANYON, DESERT_MOUNTAINS, JAGGED_PEAKS, COLD_JAGGED_PEAKS, TRANSITION_JAGGED_PEAKS, FORESTED_PEAKS, SHATTERED_SAVANNA_PEAK, BADLANDS_CANYON_PEAK, OCEAN, BLACK_OCEAN, COLD_OCEAN, FROZEN_OCEAN, WARM_OCEAN, HUMID_OCEAN, DRY_OCEAN, CORAL_REEF_OCEAN, RIVER, BOG_RIVER, CHERRY_GROVE_RIVER, SCARLET_FOREST_RIVER, JUNGLE_RIVER, FROZEN_RIVER, DARK_FOREST_RIVER, DESERT_RIVER, BADLANDS_RIVER, DEEP_OCEAN, DEEP_COLD_OCEAN, DEEP_BLACK_OCEAN, DEEP_FROZEN_OCEAN, DEEP_WARM_OCEAN, DEEP_HUMID_OCEAN, DEEP_DRY_OCEAN, DEEP_LUKEWARM_OCEAN, MUSHROOM_ISLANDS, PLAINS, MEADOW, ELEVATED_PLAINS, GORGE, PETRIFIED_CLIFFS, ARCHED_CLIFFS, SAVANNA, MUDDY_BOG, FOREST, FLOWER_FOREST, JUNGLE, SPARSE_JUNGLE, BAMBOO_FOREST, DESERT, BADLANDS, ERODED_PLAINS, SCARLET_FOREST, CHERRY_GROVE, TAIGA, SNOWY_TAIGA, SNOWY_WASTELAND, ICE_SPIKES, DARK_FOREST, PALE_FOREST, SWAMP, MANGROVE, SANDY_BEACH, BOG_BEACH, DARK_FOREST_BEACH, BADLANDS_BEACH, MUSHROOM_BEACH, BLACK_OCEAN_BEACH, ROCKY_BEACH, ICY_BEACH, MUDFLATS, CHERRY_GROVE_BEACH, SCARLET_FOREST_BEACH};
   }
}
