package org.terraform.v1_21_R7;

import com.mojang.serialization.MapCodec;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.terraform.coregen.HeightMap;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;

public class MapRenderWorldProviderBiome extends BiomeSource {
   private static final boolean debug = false;
   private final TerraformWorld tw;
   private final Set<Holder<Biome>> biomeList = CustomBiomeHandler.biomeListToBiomeSet(CustomBiomeHandler.getBiomeRegistry());
   private final Holder<Biome> river;
   private final Holder<Biome> plains;

   public MapRenderWorldProviderBiome(TerraformWorld tw, BiomeSource delegate) {
      this.tw = tw;
      Registry<Biome> registry = CustomBiomeHandler.getBiomeRegistry();
      this.river = CraftBiome.bukkitToMinecraftHolder(org.bukkit.block.Biome.RIVER);
      this.plains = CraftBiome.bukkitToMinecraftHolder(org.bukkit.block.Biome.PLAINS);
   }

   public Stream<Holder<Biome>> collectPossibleBiomes() {
      return this.biomeList.stream();
   }

   public Set<Holder<Biome>> possibleBiomes() {
      return this.biomeList;
   }

   protected MapCodec<? extends BiomeSource> codec() {
      throw new UnsupportedOperationException("Cannot serialize MapRenderWorldProviderBiome");
   }

   public Holder<Biome> getNoiseBiome(int x, int y, int z, Sampler arg3) {
      return HeightMap.getBlockHeight(this.tw, x, z) <= TConfig.c.HEIGHT_MAP_SEA_LEVEL ? this.river : this.plains;
   }
}
