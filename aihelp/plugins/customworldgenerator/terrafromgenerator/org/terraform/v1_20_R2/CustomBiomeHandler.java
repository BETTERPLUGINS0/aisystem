package org.terraform.v1_20_R2;

import com.mojang.serialization.Lifecycle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.core.Holder.c;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsMobs;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.BiomeBase.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeBase.a;
import net.minecraft.world.level.biome.BiomeFog.GrassColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.block.CraftBiome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;

public class CustomBiomeHandler {
   public static final HashMap<CustomBiomeType, ResourceKey<BiomeBase>> terraformGenBiomeRegistry = new HashMap();

   public static IRegistry<BiomeBase> getBiomeRegistry() {
      return MinecraftServer.getServer().aU().d(Registries.ap);
   }

   public static void init() {
      CraftServer craftserver = (CraftServer)Bukkit.getServer();
      DedicatedServer dedicatedserver = craftserver.getServer();
      MinecraftServer minecraftServer = MinecraftServer.getServer();
      IRegistryWritable registrywritable = (IRegistryWritable)getBiomeRegistry();

      try {
         Field frozen = RegistryMaterials.class.getDeclaredField("l");
         frozen.setAccessible(true);
         frozen.set(registrywritable, false);
         TerraformGeneratorPlugin.logger.info("Unfreezing biome registry...");
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var12) {
         TerraformGeneratorPlugin.logger.stackTrace(var12);
      }

      BiomeBase forestbiome = (BiomeBase)registrywritable.a(Biomes.i);
      CustomBiomeType[] var5 = CustomBiomeType.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         CustomBiomeType type = var5[var7];
         if (type != CustomBiomeType.NONE) {
            try {
               assert forestbiome != null;

               registerCustomBiomeBase(type, dedicatedserver, registrywritable, forestbiome);
               TLogger var10000 = TerraformGeneratorPlugin.logger;
               String var10001 = type.toString();
               var10000.info("Registered custom biome: " + var10001.toLowerCase(Locale.ENGLISH));
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException var11) {
               TerraformGeneratorPlugin.logger.error("Failed to register custom biome: " + type.getKey());
               TerraformGeneratorPlugin.logger.stackTrace(var11);
            }
         }
      }

      try {
         Field frozen = RegistryMaterials.class.getDeclaredField("l");
         frozen.setAccessible(true);
         frozen.set(registrywritable, true);
         TerraformGeneratorPlugin.logger.info("Freezing biome registry");
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var10) {
         TerraformGeneratorPlugin.logger.stackTrace(var10);
      }

   }

   private static void registerCustomBiomeBase(@NotNull CustomBiomeType biomeType, DedicatedServer dedicatedserver, @NotNull IRegistryWritable<BiomeBase> registrywritable, @NotNull BiomeBase forestbiome) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
      ResourceKey<BiomeBase> newKey = ResourceKey.a(Registries.ap, new MinecraftKey("terraformgenerator", biomeType.toString().toLowerCase(Locale.ENGLISH)));
      a newBiomeBuilder = new a();
      newBiomeBuilder.a(forestbiome.c());
      Field biomeSettingMobsField = BiomeBase.class.getDeclaredField("k");
      biomeSettingMobsField.setAccessible(true);
      BiomeSettingsMobs biomeSettingMobs = (BiomeSettingsMobs)biomeSettingMobsField.get(forestbiome);
      newBiomeBuilder.a(biomeSettingMobs);
      Field biomeSettingGenField = BiomeBase.class.getDeclaredField("j");
      biomeSettingGenField.setAccessible(true);
      BiomeSettingsGeneration biomeSettingGen = (BiomeSettingsGeneration)biomeSettingGenField.get(forestbiome);
      newBiomeBuilder.a(biomeSettingGen);
      newBiomeBuilder.a(0.7F);
      newBiomeBuilder.b(biomeType.getRainFall());
      if (biomeType.isCold()) {
         newBiomeBuilder.a(TemperatureModifier.b);
      } else {
         newBiomeBuilder.a(TemperatureModifier.a);
      }

      net.minecraft.world.level.biome.BiomeFog.a newFog = new net.minecraft.world.level.biome.BiomeFog.a();
      newFog.a(GrassColor.a);
      newFog.a(biomeType.getFogColor().isEmpty() ? forestbiome.e() : Integer.parseInt(biomeType.getFogColor(), 16));
      newFog.b(biomeType.getWaterColor().isEmpty() ? forestbiome.i() : Integer.parseInt(biomeType.getWaterColor(), 16));
      newFog.c(biomeType.getWaterFogColor().isEmpty() ? forestbiome.j() : Integer.parseInt(biomeType.getWaterFogColor(), 16));
      newFog.d(biomeType.getSkyColor().isEmpty() ? forestbiome.a() : Integer.parseInt(biomeType.getSkyColor(), 16));
      newFog.e(biomeType.getFoliageColor().isEmpty() ? forestbiome.f() : Integer.parseInt(biomeType.getFoliageColor(), 16));
      newFog.f(biomeType.getGrassColor().isEmpty() ? Integer.parseInt("79C05A", 16) : Integer.parseInt(biomeType.getGrassColor(), 16));
      newBiomeBuilder.a(newFog.a());
      BiomeBase biome = newBiomeBuilder.a();
      if (registrywritable.c(newKey)) {
         TerraformGeneratorPlugin.logger.info(String.valueOf(newKey) + " was already registered. Was there a plugin/server reload?");
      } else {
         c<BiomeBase> holder = registrywritable.a(newKey, biome, Lifecycle.stable());
         Method bindValue = c.class.getDeclaredMethod("b", Object.class);
         bindValue.setAccessible(true);
         bindValue.invoke(holder, biome);
         terraformGenBiomeRegistry.put(biomeType, newKey);
      }
   }

   public static Set<Holder<BiomeBase>> biomeListToBiomeBaseSet(@NotNull IRegistry<BiomeBase> registry) {
      List<Holder<BiomeBase>> biomeBases = new ArrayList();
      Biome[] var2 = Biome.values();
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         Biome biome = var2[var4];
         if (biome != null && biome != Biome.CUSTOM) {
            try {
               biomeBases.add(CraftBiome.bukkitToMinecraftHolder(biome));
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
            ResourceKey<BiomeBase> rkey = (ResourceKey)terraformGenBiomeRegistry.get(cbt);
            Optional<c<BiomeBase>> holder = registry.b(rkey);
            Objects.requireNonNull(biomeBases);
            holder.ifPresent(biomeBases::add);
         }
      }

      return Set.copyOf(biomeBases);
   }
}
