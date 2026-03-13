package org.terraform.v1_20_R1;

import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.Holder.c;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlock;
import org.jetbrains.annotations.Nullable;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;

public class TerraformWorldProviderBiome extends WorldChunkManager {
   private static final boolean debug = false;
   private final TerraformWorld tw;
   private final IRegistry<BiomeBase> registry;
   private final Set<Holder<BiomeBase>> biomeList = CustomBiomeHandler.biomeListToBiomeBaseSet(CustomBiomeHandler.getBiomeRegistry());

   public TerraformWorldProviderBiome(TerraformWorld tw, WorldChunkManager delegate) {
      this.tw = tw;
      this.registry = CustomBiomeHandler.getBiomeRegistry();
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
}
