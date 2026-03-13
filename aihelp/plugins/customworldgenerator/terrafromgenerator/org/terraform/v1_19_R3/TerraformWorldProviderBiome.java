package org.terraform.v1_19_R3;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.Holder.c;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;

public class TerraformWorldProviderBiome extends WorldChunkManager {
   private static final boolean debug = false;
   private final TerraformWorld tw;
   private final IRegistry<BiomeBase> registry;
   private final Set<Holder<BiomeBase>> biomeList = biomeListToBiomeBaseSet(CustomBiomeHandler.getBiomeRegistry());

   public TerraformWorldProviderBiome(TerraformWorld tw, WorldChunkManager delegate) {
      this.tw = tw;
      this.registry = CustomBiomeHandler.getBiomeRegistry();
   }

   private static Set<Holder<BiomeBase>> biomeListToBiomeBaseSet(@NotNull IRegistry<BiomeBase> registry) {
      List<Holder<BiomeBase>> biomeBases = new ArrayList();
      Biome[] var2 = Biome.values();
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         Biome biome = var2[var4];
         if (biome != null && biome != Biome.CUSTOM) {
            try {
               biomeBases.add(CraftBlock.biomeToBiomeBase(registry, biome));
            } catch (IllegalStateException var8) {
               TerraformGeneratorPlugin.logger.info("Ignoring biome " + String.valueOf(biome));
            }
         }
      }

      CustomBiomeType[] var9 = CustomBiomeType.values();
      var3 = var9.length;

      for(var4 = 0; var4 < var3; ++var4) {
         CustomBiomeType cbt = var9[var4];
         if (cbt != CustomBiomeType.NONE) {
            ResourceKey<BiomeBase> rkey = (ResourceKey)CustomBiomeHandler.terraformGenBiomeRegistry.get(cbt);
            Optional<c<BiomeBase>> holder = registry.b(rkey);
            Objects.requireNonNull(biomeBases);
            holder.ifPresent(biomeBases::add);
         }
      }

      return Set.copyOf(biomeBases);
   }

   public Stream<Holder<BiomeBase>> b() {
      return this.biomeList.stream();
   }

   public Set<Holder<BiomeBase>> c() {
      return this.biomeList;
   }

   protected Codec<? extends WorldChunkManager> a() {
      throw new UnsupportedOperationException("Cannot serialize TerraformWorldProviderBiome");
   }

   @Nullable
   public Holder<BiomeBase> getNoiseBiome(int x, int y, int z, Sampler arg3) {
      BiomeBank bank = this.tw.getBiomeBank(x << 2, z << 2);
      if (bank.getHandler().getCustomBiome() == CustomBiomeType.NONE) {
         return CraftBlock.biomeToBiomeBase(this.registry, bank.getHandler().getBiome());
      } else {
         ResourceKey<BiomeBase> rkey = (ResourceKey)CustomBiomeHandler.terraformGenBiomeRegistry.get(bank.getHandler().getCustomBiome());
         Optional<c<BiomeBase>> holder = this.registry.b(rkey);
         if (holder.isEmpty()) {
            TerraformGeneratorPlugin.logger.error("Custom biome was not found in the vanilla registry!");
         }

         return holder.isPresent() ? (Holder)holder.get() : CraftBlock.biomeToBiomeBase(this.registry, bank.getHandler().getBiome());
      }
   }

   public static class TerraformBiomeResolverProxy implements BiomeResolver {
      final TerraformWorldProviderBiome delegate;

      public TerraformBiomeResolverProxy(TerraformWorldProviderBiome delegate) {
         this.delegate = delegate;
      }

      @Nullable
      public Holder<BiomeBase> getNoiseBiome(int arg0, int arg1, int arg2, Sampler arg3) {
         return this.delegate.getNoiseBiome(arg0, arg1, arg2, arg3);
      }
   }
}
