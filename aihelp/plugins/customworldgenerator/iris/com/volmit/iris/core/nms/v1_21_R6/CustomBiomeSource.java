package com.volmit.iris.core.nms.v1_21_R6;

import com.mojang.serialization.MapCodec;
import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisBiomeCustom;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.math.RNG;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.IRegistryCustom.Dimension;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_21_R6.CraftServer;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;

public class CustomBiomeSource extends WorldChunkManager {
   private final long seed;
   private final Engine engine;
   private final IRegistry<BiomeBase> biomeCustomRegistry;
   private final IRegistry<BiomeBase> biomeRegistry;
   private final AtomicCache<IRegistryCustom> registryAccess = new AtomicCache();
   private final RNG rng;
   private final KMap<String, Holder<BiomeBase>> customBiomes;

   public CustomBiomeSource(long seed, Engine engine, World world) {
      this.engine = var3;
      this.seed = var1;
      this.biomeCustomRegistry = (IRegistry)this.registry().a(Registries.aN).orElse((Object)null);
      this.biomeRegistry = (IRegistry)((IRegistryCustom)getFor(Dimension.class, ((CraftServer)Bukkit.getServer()).getHandle().b())).a(Registries.aN).orElse((Object)null);
      this.rng = new RNG(var3.getSeedManager().getBiome());
      this.customBiomes = this.fillCustomBiomes(this.biomeCustomRegistry, var3);
   }

   private static List<Holder<BiomeBase>> getAllBiomes(IRegistry<BiomeBase> customRegistry, IRegistry<BiomeBase> registry, Engine engine) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var2.getAllBiomes().iterator();

      while(true) {
         while(var4.hasNext()) {
            IrisBiome var5 = (IrisBiome)var4.next();
            if (var5.isCustom()) {
               Iterator var6 = var5.getCustomDerivitives().iterator();

               while(var6.hasNext()) {
                  IrisBiomeCustom var7 = (IrisBiomeCustom)var6.next();
                  var3.add((Holder)var0.a((ResourceKey)var0.d((BiomeBase)var0.a(MinecraftKey.a(var2.getDimension().getLoadKey(), var7.getId()))).get()).get());
               }
            } else {
               var3.add(NMSBinding.biomeToBiomeBase(var1, var5.getVanillaDerivative()));
            }
         }

         return var3;
      }
   }

   private static Object getFor(Class<?> type, Object source) {
      Object var2 = fieldFor(var0, var1);
      return var2 != null ? var2 : invokeFor(var0, var1);
   }

   private static Object fieldFor(Class<?> returns, Object in) {
      return fieldForClass(var0, var1.getClass(), var1);
   }

   private static Object invokeFor(Class<?> returns, Object in) {
      Method[] var2 = var1.getClass().getMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method var5 = var2[var4];
         if (var5.getReturnType().equals(var0)) {
            var5.setAccessible(true);

            try {
               String var10000 = var0.getSimpleName();
               Iris.debug("[NMS] Found " + var10000 + " in " + var1.getClass().getSimpleName() + "." + var5.getName() + "()");
               return var5.invoke(var1);
            } catch (Throwable var7) {
               var7.printStackTrace();
            }
         }
      }

      return null;
   }

   private static <T> T fieldForClass(Class<T> returnType, Class<?> sourceType, Object in) {
      Field[] var3 = var1.getDeclaredFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field var6 = var3[var5];
         if (var6.getType().equals(var0)) {
            var6.setAccessible(true);

            try {
               String var10000 = var0.getSimpleName();
               Iris.debug("[NMS] Found " + var10000 + " in " + var1.getSimpleName() + "." + var6.getName());
               return var6.get(var2);
            } catch (IllegalAccessException var8) {
               var8.printStackTrace();
            }
         }
      }

      return null;
   }

   protected Stream<Holder<BiomeBase>> b() {
      return getAllBiomes((IRegistry)((IRegistryCustom)getFor(Dimension.class, ((CraftServer)Bukkit.getServer()).getHandle().b())).a(Registries.aN).orElse((Object)null), (IRegistry)((CraftWorld)this.engine.getWorld().realWorld()).getHandle().L_().a(Registries.aN).orElse((Object)null), this.engine).stream();
   }

   private KMap<String, Holder<BiomeBase>> fillCustomBiomes(IRegistry<BiomeBase> customRegistry, Engine engine) {
      KMap var3 = new KMap();
      Iterator var4 = var2.getAllBiomes().iterator();

      while(true) {
         IrisBiome var5;
         do {
            if (!var4.hasNext()) {
               return var3;
            }

            var5 = (IrisBiome)var4.next();
         } while(!var5.isCustom());

         Iterator var6 = var5.getCustomDerivitives().iterator();

         while(var6.hasNext()) {
            IrisBiomeCustom var7 = (IrisBiomeCustom)var6.next();
            MinecraftKey var8 = MinecraftKey.a(var2.getDimension().getLoadKey(), var7.getId());
            BiomeBase var9 = (BiomeBase)var1.a(var8);
            Optional var10 = var1.d(var9);
            if (var10.isEmpty()) {
               Iris.error("Cannot find biome for IrisBiomeCustom " + var7.getId() + " from engine " + var2.getName());
            } else {
               ResourceKey var11 = (ResourceKey)var10.get();
               Optional var12 = var1.a(var11);
               if (var12.isEmpty()) {
                  Iris.error("Cannot find reference to biome " + String.valueOf(var11) + " for engine " + var2.getName());
               } else {
                  var3.put(var7.getId(), (Holder)var12.get());
               }
            }
         }
      }
   }

   private IRegistryCustom registry() {
      return (IRegistryCustom)this.registryAccess.aquire(() -> {
         return (IRegistryCustom)getFor(Dimension.class, ((CraftServer)Bukkit.getServer()).getHandle().b());
      });
   }

   protected MapCodec<? extends WorldChunkManager> a() {
      throw new UnsupportedOperationException("Not supported");
   }

   public Holder<BiomeBase> getNoiseBiome(int x, int y, int z, Sampler sampler) {
      int var5 = var2 - this.engine.getMinHeight() << 2;
      IrisBiome var6 = (IrisBiome)this.engine.getComplex().getTrueBiomeStream().get((double)(var1 << 2), (double)(var3 << 2));
      if (var6.isCustom()) {
         return (Holder)this.customBiomes.get(var6.getCustomBiome(this.rng, (double)(var1 << 2), (double)var5, (double)(var3 << 2)).getId());
      } else {
         Biome var7 = var6.getSkyBiome(this.rng, (double)(var1 << 2), (double)var5, (double)(var3 << 2));
         return NMSBinding.biomeToBiomeBase(this.biomeRegistry, var7);
      }
   }
}
