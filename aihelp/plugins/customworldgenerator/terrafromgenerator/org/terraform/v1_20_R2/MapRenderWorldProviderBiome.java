package org.terraform.v1_20_R2;

import com.mojang.serialization.Codec;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_20_R2.block.CraftBiome;
import org.terraform.coregen.HeightMap;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;

public class MapRenderWorldProviderBiome extends WorldChunkManager {
   private static final boolean debug = false;
   private final TerraformWorld tw;
   private final Set<Holder<BiomeBase>> biomeList = CustomBiomeHandler.biomeListToBiomeBaseSet(CustomBiomeHandler.getBiomeRegistry());
   private final Holder<BiomeBase> river;
   private final Holder<BiomeBase> plains;

   public MapRenderWorldProviderBiome(TerraformWorld tw, WorldChunkManager delegate) {
      this.tw = tw;
      IRegistry<BiomeBase> registry = CustomBiomeHandler.getBiomeRegistry();
      this.river = CraftBiome.bukkitToMinecraftHolder(Biome.RIVER);
      this.plains = CraftBiome.bukkitToMinecraftHolder(Biome.PLAINS);
   }

   public Stream<Holder<BiomeBase>> b() {
      return this.biomeList.stream();
   }

   public Set<Holder<BiomeBase>> c() {
      return this.biomeList;
   }

   protected Codec<? extends WorldChunkManager> a() {
      throw new UnsupportedOperationException("Cannot serialize MapRenderWorldProviderBiome");
   }

   public Holder<BiomeBase> getNoiseBiome(int x, int y, int z, Sampler arg3) {
      return HeightMap.getBlockHeight(this.tw, x, z) <= TConfig.c.HEIGHT_MAP_SEA_LEVEL ? this.river : this.plains;
   }
}
