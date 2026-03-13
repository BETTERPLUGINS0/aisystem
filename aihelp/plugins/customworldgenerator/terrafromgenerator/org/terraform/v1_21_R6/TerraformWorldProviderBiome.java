package org.terraform.v1_21_R6;

import com.mojang.serialization.MapCodec;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder.Reference;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.jetbrains.annotations.Nullable;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;

public class TerraformWorldProviderBiome extends BiomeSource {
   private static final boolean debug = false;
   private final TerraformWorld tw;
   private final Registry<Biome> registry;
   private final Set<Holder<Biome>> biomeList = CustomBiomeHandler.biomeListToBiomeSet(CustomBiomeHandler.getBiomeRegistry());

   public TerraformWorldProviderBiome(TerraformWorld tw, BiomeSource delegate) {
      this.tw = tw;
      this.registry = CustomBiomeHandler.getBiomeRegistry();
   }

   public Stream<Holder<Biome>> collectPossibleBiomes() {
      return this.biomeList.stream();
   }

   public Set<Holder<Biome>> possibleBiomes() {
      return this.biomeList;
   }

   protected MapCodec<? extends BiomeSource> codec() {
      throw new UnsupportedOperationException("Cannot serialize TerraformWorldProviderBiome");
   }

   @Nullable
   public Holder<Biome> getNoiseBiome(int x, int y, int z, Sampler arg3) {
      BiomeBank bank = this.tw.getBiomeBank(x << 2, z << 2);
      if (bank.getHandler().getCustomBiome() == CustomBiomeType.NONE) {
         return CraftBiome.bukkitToMinecraftHolder(bank.getHandler().getBiome());
      } else {
         ResourceKey<Biome> rkey = (ResourceKey)CustomBiomeHandler.terraformGenBiomeRegistry.get(bank.getHandler().getCustomBiome());
         Optional<Reference<Biome>> holder = this.registry.get(rkey);
         if (holder.isEmpty()) {
            TerraformGeneratorPlugin.logger.error("Custom biome was not found in the vanilla registry!");
         }

         return holder.isPresent() ? (Holder)holder.get() : CraftBiome.bukkitToMinecraftHolder(bank.getHandler().getBiome());
      }
   }
}
