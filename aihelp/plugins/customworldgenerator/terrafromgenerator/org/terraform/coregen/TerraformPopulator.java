package org.terraform.coregen;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.cavepopulators.MasterCavePopulatorDistributor;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataSpigotAPI;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.populators.AmethystGeodePopulator;
import org.terraform.populators.OrePopulator;
import org.terraform.structure.MultiMegaChunkStructurePopulator;
import org.terraform.structure.StructureBufferDistanceHandler;
import org.terraform.structure.StructureRegistry;
import org.terraform.utils.GenUtils;

public class TerraformPopulator extends BlockPopulator {
   private static final OrePopulator[] ORE_POPS;
   private final AmethystGeodePopulator amethystGeodePopulator;
   private final MasterCavePopulatorDistributor caveDistributor;

   public TerraformPopulator() {
      this.amethystGeodePopulator = new AmethystGeodePopulator(TConfig.c.ORE_AMETHYST_GEODE_SIZE, TConfig.c.ORE_AMETHYST_CHANCE, TConfig.c.ORE_AMETHYST_MIN_DEPTH, TConfig.c.ORE_AMETHYST_MIN_DEPTH_BELOW_SURFACE);
      this.caveDistributor = new MasterCavePopulatorDistributor();
   }

   public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
      TerraformWorld tw = TerraformWorld.get(worldInfo.getName(), worldInfo.getSeed());
      PopulatorDataAbstract data = new PopulatorDataSpigotAPI(limitedRegion, tw, chunkX, chunkZ);
      this.populate(tw, data);
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      Random random = tw.getHashedRand(571162L, data.getChunkX(), data.getChunkZ());
      OrePopulator[] var4 = ORE_POPS;
      int var5 = var4.length;

      int rawX;
      for(rawX = 0; rawX < var5; ++rawX) {
         OrePopulator ore = var4[rawX];
         ore.populate(tw, random, data);
      }

      EnumSet<BiomeBank> banks = EnumSet.noneOf(BiomeBank.class);
      boolean[] canDecorate = StructureBufferDistanceHandler.canDecorateChunk(tw, data.getChunkX(), data.getChunkZ());
      if (canDecorate[1]) {
         this.amethystGeodePopulator.populate(tw, random, data);
      }

      for(rawX = data.getChunkX() * 16; rawX <= data.getChunkX() * 16 + 16; ++rawX) {
         for(int rawZ = data.getChunkZ() * 16; rawZ <= data.getChunkZ() * 16 + 16; ++rawZ) {
            int surfaceY = GenUtils.getTransformedHeight(data.getTerraformWorld(), rawX, rawZ);
            BiomeBank bank = tw.getBiomeBank(rawX, surfaceY, rawZ);
            banks.add(bank);
            if (bank.isDry() || data.getType(rawX, surfaceY + 1, rawZ) == Material.WATER) {
               bank.getHandler().populateSmallItems(tw, random, rawX, surfaceY, rawZ, data);
            }
         }
      }

      Iterator var13;
      if (canDecorate[0]) {
         var13 = banks.iterator();

         while(var13.hasNext()) {
            BiomeBank bank = (BiomeBank)var13.next();
            bank.getHandler().populateLargeItems(tw, random, data);
         }
      }

      this.caveDistributor.populate(tw, random, data, canDecorate[1]);
      var13 = StructureRegistry.smallStructureRegistry.iterator();

      while(var13.hasNext()) {
         MultiMegaChunkStructurePopulator spop = (MultiMegaChunkStructurePopulator)var13.next();
         if (TConfig.areStructuresEnabled() && spop.canSpawn(tw, data.getChunkX(), data.getChunkZ())) {
            TLogger var10000 = TerraformGeneratorPlugin.logger;
            String var10001 = spop.getClass().getName();
            var10000.info("Generating " + var10001 + " at chunk: " + data.getChunkX() + "," + data.getChunkZ());
            spop.populate(tw, data);
         }
      }

   }

   static {
      ORE_POPS = new OrePopulator[]{new OrePopulator(Material.DEEPSLATE, TConfig.c.ORE_DEEPSLATE_CHANCE, TConfig.c.ORE_DEEPSLATE_VEINSIZE, TConfig.c.ORE_DEEPSLATE_MAXVEINNUMBER, TConfig.c.ORE_DEEPSLATE_MINSPAWNHEIGHT, TConfig.c.ORE_DEEPSLATE_COMMONSPAWNHEIGHT, TConfig.c.ORE_DEEPSLATE_MAXSPAWNHEIGHT, true, new BiomeBank[0]), new OrePopulator(Material.TUFF, TConfig.c.ORE_TUFF_CHANCE, TConfig.c.ORE_TUFF_VEINSIZE, TConfig.c.ORE_TUFF_MAXVEINNUMBER, TConfig.c.ORE_TUFF_MINSPAWNHEIGHT, TConfig.c.ORE_TUFF_COMMONSPAWNHEIGHT, TConfig.c.ORE_TUFF_MAXSPAWNHEIGHT, true, new BiomeBank[0]), new OrePopulator(Material.COPPER_ORE, TConfig.c.ORE_COPPER_CHANCE, TConfig.c.ORE_COPPER_VEINSIZE, TConfig.c.ORE_COPPER_MAXVEINNUMBER, TConfig.c.ORE_COPPER_MINSPAWNHEIGHT, TConfig.c.ORE_COPPER_COMMONSPAWNHEIGHT, TConfig.c.ORE_COPPER_MAXSPAWNHEIGHT, false, new BiomeBank[0]), new OrePopulator(Material.COAL_ORE, TConfig.c.ORE_COAL_CHANCE, TConfig.c.ORE_COAL_VEINSIZE, TConfig.c.ORE_COAL_MAXVEINNUMBER, TConfig.c.ORE_COAL_MINSPAWNHEIGHT, TConfig.c.ORE_COAL_COMMONSPAWNHEIGHT, TConfig.c.ORE_COAL_MAXSPAWNHEIGHT, false, new BiomeBank[0]), new OrePopulator(Material.IRON_ORE, TConfig.c.ORE_IRON_CHANCE, TConfig.c.ORE_IRON_VEINSIZE, TConfig.c.ORE_IRON_MAXVEINNUMBER, TConfig.c.ORE_IRON_MINSPAWNHEIGHT, TConfig.c.ORE_IRON_COMMONSPAWNHEIGHT, TConfig.c.ORE_IRON_MAXSPAWNHEIGHT, false, new BiomeBank[0]), new OrePopulator(Material.GOLD_ORE, TConfig.c.ORE_GOLD_CHANCE, TConfig.c.ORE_GOLD_VEINSIZE, TConfig.c.ORE_GOLD_MAXVEINNUMBER, TConfig.c.ORE_GOLD_MINSPAWNHEIGHT, TConfig.c.ORE_GOLD_COMMONSPAWNHEIGHT, TConfig.c.ORE_GOLD_MAXSPAWNHEIGHT, false, new BiomeBank[0]), new OrePopulator(Material.GOLD_ORE, TConfig.c.ORE_BADLANDSGOLD_CHANCE, TConfig.c.ORE_BADLANDSGOLD_VEINSIZE, TConfig.c.ORE_BADLANDSGOLD_MAXVEINNUMBER, TConfig.c.ORE_BADLANDSGOLD_MINSPAWNHEIGHT, TConfig.c.ORE_BADLANDSGOLD_COMMONSPAWNHEIGHT, TConfig.c.ORE_BADLANDSGOLD_MAXSPAWNHEIGHT, false, new BiomeBank[]{BiomeBank.BADLANDS, BiomeBank.BADLANDS_CANYON, BiomeBank.BADLANDS_CANYON_PEAK, BiomeBank.BADLANDS_BEACH, BiomeBank.BADLANDS_RIVER}), new OrePopulator(Material.DIAMOND_ORE, TConfig.c.ORE_DIAMOND_CHANCE, TConfig.c.ORE_DIAMOND_VEINSIZE, TConfig.c.ORE_DIAMOND_MAXVEINNUMBER, TConfig.c.ORE_DIAMOND_MINSPAWNHEIGHT, TConfig.c.ORE_DIAMOND_COMMONSPAWNHEIGHT, TConfig.c.ORE_DIAMOND_MAXSPAWNHEIGHT, false, new BiomeBank[0]), new OrePopulator(Material.EMERALD_ORE, TConfig.c.ORE_EMERALD_CHANCE, TConfig.c.ORE_EMERALD_VEINSIZE, TConfig.c.ORE_EMERALD_MAXVEINNUMBER, TConfig.c.ORE_EMERALD_MINSPAWNHEIGHT, TConfig.c.ORE_EMERALD_COMMONSPAWNHEIGHT, TConfig.c.ORE_EMERALD_MAXSPAWNHEIGHT, false, new BiomeBank[]{BiomeBank.BIRCH_MOUNTAINS, BiomeBank.ROCKY_MOUNTAINS, BiomeBank.SNOWY_MOUNTAINS, BiomeBank.FORESTED_MOUNTAINS, BiomeBank.COLD_JAGGED_PEAKS, BiomeBank.JAGGED_PEAKS, BiomeBank.FORESTED_PEAKS}), new OrePopulator(Material.LAPIS_ORE, TConfig.c.ORE_LAPIS_CHANCE, TConfig.c.ORE_LAPIS_VEINSIZE, TConfig.c.ORE_LAPIS_MAXVEINNUMBER, TConfig.c.ORE_LAPIS_MINSPAWNHEIGHT, TConfig.c.ORE_LAPIS_COMMONSPAWNHEIGHT, TConfig.c.ORE_LAPIS_MAXSPAWNHEIGHT, false, new BiomeBank[0]), new OrePopulator(Material.REDSTONE_ORE, TConfig.c.ORE_REDSTONE_CHANCE, TConfig.c.ORE_REDSTONE_VEINSIZE, TConfig.c.ORE_REDSTONE_MAXVEINNUMBER, TConfig.c.ORE_REDSTONE_MINSPAWNHEIGHT, TConfig.c.ORE_REDSTONE_COMMONSPAWNHEIGHT, TConfig.c.ORE_REDSTONE_MAXSPAWNHEIGHT, false, new BiomeBank[0]), new OrePopulator(Material.GRAVEL, TConfig.c.ORE_GRAVEL_CHANCE, TConfig.c.ORE_GRAVEL_VEINSIZE, TConfig.c.ORE_GRAVEL_MAXVEINNUMBER, TConfig.c.ORE_GRAVEL_MINSPAWNHEIGHT, TConfig.c.ORE_GRAVEL_COMMONSPAWNHEIGHT, TConfig.c.ORE_GRAVEL_MAXSPAWNHEIGHT, true, new BiomeBank[0]), new OrePopulator(Material.ANDESITE, TConfig.c.ORE_ANDESITE_CHANCE, TConfig.c.ORE_ANDESITE_VEINSIZE, TConfig.c.ORE_ANDESITE_MAXVEINNUMBER, TConfig.c.ORE_ANDESITE_MINSPAWNHEIGHT, TConfig.c.ORE_ANDESITE_COMMONSPAWNHEIGHT, TConfig.c.ORE_ANDESITE_MAXSPAWNHEIGHT, true, new BiomeBank[0]), new OrePopulator(Material.DIORITE, TConfig.c.ORE_DIORITE_CHANCE, TConfig.c.ORE_DIORITE_VEINSIZE, TConfig.c.ORE_DIORITE_MAXVEINNUMBER, TConfig.c.ORE_DIORITE_MINSPAWNHEIGHT, TConfig.c.ORE_DIORITE_COMMONSPAWNHEIGHT, TConfig.c.ORE_DIORITE_MAXSPAWNHEIGHT, true, new BiomeBank[0]), new OrePopulator(Material.GRANITE, TConfig.c.ORE_GRANITE_CHANCE, TConfig.c.ORE_GRANITE_VEINSIZE, TConfig.c.ORE_GRANITE_MAXVEINNUMBER, TConfig.c.ORE_GRANITE_MINSPAWNHEIGHT, TConfig.c.ORE_GRANITE_COMMONSPAWNHEIGHT, TConfig.c.ORE_GRANITE_MAXSPAWNHEIGHT, true, new BiomeBank[0])};
   }
}
