package org.terraform.v1_21_R1;

import java.lang.reflect.Field;
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
import net.minecraft.server.ReloadableServerRegistries;
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
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.block.CraftBiome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;

public class CustomBiomeHandler {
   public static final HashMap<CustomBiomeType, ResourceKey<BiomeBase>> terraformGenBiomeRegistry = new HashMap();

   public static IRegistry<BiomeBase> getBiomeRegistry() {
      return MinecraftServer.getServer().bc().d(Registries.aF);
   }

   public static void init() {
      CraftServer craftserver = (CraftServer)Bukkit.getServer();
      DedicatedServer dedicatedserver = craftserver.getServer();
      IRegistryWritable registrywritable = (IRegistryWritable)getBiomeRegistry();

      try {
         Field frozen = RegistryMaterials.class.getDeclaredField("l");
         frozen.setAccessible(true);
         frozen.set(registrywritable, false);
         TerraformGeneratorPlugin.logger.info("Unfreezing biome registry...");
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var11) {
         TerraformGeneratorPlugin.logger.stackTrace(var11);
      }

      BiomeBase forestbiome = (BiomeBase)registrywritable.a(Biomes.i);
      CustomBiomeType[] var4 = CustomBiomeType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         CustomBiomeType type = var4[var6];
         if (type != CustomBiomeType.NONE) {
            try {
               assert forestbiome != null;

               registerCustomBiomeBase(type, dedicatedserver, registrywritable, forestbiome);
               TLogger var10000 = TerraformGeneratorPlugin.logger;
               String var10001 = type.toString();
               var10000.info("Registered custom biome: " + var10001.toLowerCase(Locale.ENGLISH));
            } catch (Throwable var10) {
               TerraformGeneratorPlugin.logger.error("Failed to register custom biome: " + type.getKey());
               TerraformGeneratorPlugin.logger.stackTrace(var10);
            }
         }
      }

      try {
         Field frozen = RegistryMaterials.class.getDeclaredField("l");
         frozen.setAccessible(true);
         frozen.set(registrywritable, true);
         TerraformGeneratorPlugin.logger.info("Freezing biome registry");
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var9) {
         TerraformGeneratorPlugin.logger.stackTrace(var9);
      }

   }

   private static void registerCustomBiomeBase(@NotNull CustomBiomeType biomeType, DedicatedServer dedicatedserver, @NotNull IRegistryWritable<BiomeBase> registrywritable, @NotNull BiomeBase forestbiome) throws Throwable {
      Field defaultRegInfoField = ReloadableServerRegistries.class.getDeclaredField("c");
      defaultRegInfoField.setAccessible(true);
      Object regInfo = defaultRegInfoField.get((Object)null);
      ResourceKey<BiomeBase> newKey = ResourceKey.a(Registries.aF, MinecraftKey.a("terraformgenerator", biomeType.toString().toLowerCase(Locale.ENGLISH)));
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
      if (registrywritable.d(newKey)) {
         TerraformGeneratorPlugin.logger.info(String.valueOf(newKey) + " was already registered. Was there a plugin/server reload?");
      } else {
         Method register = registrywritable.getClass().getDeclaredMethod("a", ResourceKey.class, Object.class, Class.forName("net.minecraft.core.RegistrationInfo"));
         register.setAccessible(true);
         c<BiomeBase> holder = (c)register.invoke(registrywritable, newKey, biome, regInfo);
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
