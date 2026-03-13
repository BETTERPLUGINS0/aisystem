package org.terraform.v1_18_R2;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
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

   public TerraformWorldProviderBiome(TerraformWorld tw, WorldChunkManager delegate) {
      super(biomeListToBiomeBaseList(((CraftServer)Bukkit.getServer()).getServer().aU().b(IRegistry.aP)));
      this.tw = tw;
      this.registry = ((CraftServer)Bukkit.getServer()).getServer().aU().b(IRegistry.aP);
   }

   @NotNull
   private static List<Holder<BiomeBase>> biomeListToBiomeBaseList(@NotNull IRegistry<BiomeBase> registry) {
      List<Holder<BiomeBase>> biomeBases = new ArrayList();
      Biome[] var2 = Biome.values();
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         Biome biome = var2[var4];
         if (biome != null && biome != Biome.CUSTOM) {
            biomeBases.add(CraftBlock.biomeToBiomeBase(registry, biome));
         }
      }

      CustomBiomeType[] var8 = CustomBiomeType.values();
      var3 = var8.length;

      for(var4 = 0; var4 < var3; ++var4) {
         CustomBiomeType cbt = var8[var4];
         if (cbt != CustomBiomeType.NONE) {
            ResourceKey<BiomeBase> rkey = (ResourceKey)CustomBiomeHandler.terraformGenBiomeRegistry.get(cbt);
            Holder<BiomeBase> holder = registry.g(rkey);
            if (holder != null) {
               biomeBases.add(holder);
            }
         }
      }

      return biomeBases;
   }

   protected Codec<? extends WorldChunkManager> a() {
      throw new UnsupportedOperationException("Cannot serialize TerraformWorldProviderBiome");
   }

   public WorldChunkManager a(long arg0) {
      throw new UnsupportedOperationException("Cannot serialize TerraformWorldProviderBiome");
   }

   @Nullable
   public Holder<BiomeBase> getNoiseBiome(int x, int y, int z, Sampler arg3) {
      BiomeBank bank = this.tw.getBiomeBank(x << 2, z << 2);
      DedicatedServer dedicatedserver = ((CraftServer)Bukkit.getServer()).getServer();
      IRegistry<BiomeBase> iregistry = dedicatedserver.aU().b(IRegistry.aP);
      if (bank.getHandler().getCustomBiome() == CustomBiomeType.NONE) {
         return CraftBlock.biomeToBiomeBase(this.registry, bank.getHandler().getBiome());
      } else {
         ResourceKey<BiomeBase> rkey = (ResourceKey)CustomBiomeHandler.terraformGenBiomeRegistry.get(bank.getHandler().getCustomBiome());
         Holder<BiomeBase> holder = iregistry.g(rkey);
         if (holder == null) {
            TerraformGeneratorPlugin.logger.error("Custom biome was not found in the vanilla registry!");
         }

         return holder != null ? holder : CraftBlock.biomeToBiomeBase(this.registry, bank.getHandler().getBiome());
      }
   }
}
