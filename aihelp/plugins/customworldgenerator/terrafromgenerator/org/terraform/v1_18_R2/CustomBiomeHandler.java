package org.terraform.v1_18_R2;

import com.mojang.serialization.Lifecycle;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.data.RegistryGeneration;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsMobs;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.BiomeBase.Geography;
import net.minecraft.world.level.biome.BiomeBase.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeBase.a;
import net.minecraft.world.level.biome.BiomeFog.GrassColor;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;

public class CustomBiomeHandler {
   public static final HashMap<CustomBiomeType, ResourceKey<BiomeBase>> terraformGenBiomeRegistry = new HashMap();

   public static void init() {
      CraftServer craftserver = (CraftServer)Bukkit.getServer();
      DedicatedServer dedicatedserver = craftserver.getServer();
      IRegistryWritable registrywritable = (IRegistryWritable)dedicatedserver.aU().b(IRegistry.aP);

      try {
         Field frozen = RegistryMaterials.class.getDeclaredField("bL");
         frozen.setAccessible(true);
         frozen.set(registrywritable, false);
         TerraformGeneratorPlugin.logger.info("Unfreezing biome registry...");
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var11) {
         TerraformGeneratorPlugin.logger.stackTrace(var11);
      }

      BiomeBase forestbiome = (BiomeBase)registrywritable.a(Biomes.h);
      CustomBiomeType[] var4 = CustomBiomeType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         CustomBiomeType type = var4[var6];
         if (type != CustomBiomeType.NONE) {
            try {
               registerCustomBiomeBase(type, dedicatedserver, registrywritable, forestbiome);
               TLogger var10000 = TerraformGeneratorPlugin.logger;
               String var10001 = type.toString();
               var10000.info("Registered custom biome: " + var10001.toLowerCase(Locale.ENGLISH));
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var10) {
               TerraformGeneratorPlugin.logger.error("Failed to register custom biome: " + type.getKey());
               TerraformGeneratorPlugin.logger.stackTrace(var10);
            }
         }
      }

      try {
         Field frozen = RegistryMaterials.class.getDeclaredField("bL");
         frozen.setAccessible(true);
         frozen.set(registrywritable, true);
         TerraformGeneratorPlugin.logger.info("Freezing biome registry");
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var9) {
         TerraformGeneratorPlugin.logger.stackTrace(var9);
      }

   }

   private static void registerCustomBiomeBase(@NotNull CustomBiomeType biomeType, @NotNull DedicatedServer dedicatedserver, IRegistryWritable<BiomeBase> registrywritable, @NotNull BiomeBase forestbiome) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
      ResourceKey<BiomeBase> newKey = ResourceKey.a(IRegistry.aP, new MinecraftKey("terraformgenerator", biomeType.toString().toLowerCase(Locale.ENGLISH)));
      a newBiomeBuilder = new a();
      Field f = BiomeBase.class.getDeclaredField("l");
      f.setAccessible(true);
      newBiomeBuilder.a((Geography)f.get(forestbiome));
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
      newFog.a(biomeType.getFogColor().isEmpty() ? forestbiome.f() : Integer.parseInt(biomeType.getFogColor(), 16));
      newFog.b(biomeType.getWaterColor().isEmpty() ? forestbiome.k() : Integer.parseInt(biomeType.getWaterColor(), 16));
      newFog.c(biomeType.getWaterFogColor().isEmpty() ? forestbiome.l() : Integer.parseInt(biomeType.getWaterFogColor(), 16));
      newFog.d(biomeType.getSkyColor().isEmpty() ? forestbiome.a() : Integer.parseInt(biomeType.getSkyColor(), 16));
      newFog.e(biomeType.getFoliageColor().isEmpty() ? forestbiome.g() : Integer.parseInt(biomeType.getFoliageColor(), 16));
      newFog.f(biomeType.getGrassColor().isEmpty() ? Integer.parseInt("79C05A", 16) : Integer.parseInt(biomeType.getGrassColor(), 16));
      newBiomeBuilder.a(newFog.a());
      BiomeBase biome = newBiomeBuilder.a();
      RegistryGeneration.a(RegistryGeneration.i, newKey, biome);
      RegistryMaterials<BiomeBase> registry = (RegistryMaterials)dedicatedserver.aU().b(IRegistry.aP);
      registry.a(newKey, biome, Lifecycle.stable());
      terraformGenBiomeRegistry.put(biomeType, newKey);
   }
}
