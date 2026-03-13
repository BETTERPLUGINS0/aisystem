package org.terraform.v1_21_R5;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsMobs;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.BiomeBase.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeBase.a;
import net.minecraft.world.level.biome.BiomeFog.GrassColor;
import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_21_R5.CraftServer;
import org.bukkit.craftbukkit.v1_21_R5.block.CraftBiome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;

public class CustomBiomeHandler {
   public static final HashMap<CustomBiomeType, ResourceKey<BiomeBase>> terraformGenBiomeRegistry = new HashMap();

   public static IRegistry<BiomeBase> getBiomeRegistry() {
      return (IRegistry)MinecraftServer.getServer().ba().a(Registries.aK).orElseThrow();
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

      Holder<BiomeBase> forestbiome = registrywritable.p().b(Biomes.i);
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

   private static void registerCustomBiomeBase(@NotNull CustomBiomeType biomeType, DedicatedServer dedicatedserver, @NotNull IRegistryWritable<BiomeBase> registrywritable, @NotNull Holder<BiomeBase> forestBiomeHolder) throws Throwable {
      BiomeBase forestbiome = (BiomeBase)forestBiomeHolder.a();
      Field defaultRegInfoField = ReloadableServerRegistries.class.getDeclaredField("b");
      defaultRegInfoField.setAccessible(true);
      Object regInfo = defaultRegInfoField.get((Object)null);
      ResourceKey<BiomeBase> newKey = ResourceKey.a(Registries.aK, MinecraftKey.a("terraformgenerator", biomeType.toString().toLowerCase(Locale.ENGLISH)));
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
      newFog.a(GrassColor.a).a(biomeType.getFogColor().isEmpty() ? forestbiome.e() : Integer.parseInt(biomeType.getFogColor(), 16)).b(biomeType.getWaterColor().isEmpty() ? forestbiome.j() : Integer.parseInt(biomeType.getWaterColor(), 16)).c(biomeType.getWaterFogColor().isEmpty() ? forestbiome.k() : Integer.parseInt(biomeType.getWaterFogColor(), 16)).d(biomeType.getSkyColor().isEmpty() ? forestbiome.a() : Integer.parseInt(biomeType.getSkyColor(), 16)).e(biomeType.getFoliageColor().isEmpty() ? forestbiome.e() : Integer.parseInt(biomeType.getFoliageColor(), 16)).g(biomeType.getGrassColor().isEmpty() ? Integer.parseInt("79C05A", 16) : Integer.parseInt(biomeType.getGrassColor(), 16));
      newBiomeBuilder.a(newFog.b());
      BiomeBase biome = newBiomeBuilder.a();
      if (registrywritable.p().a(newKey).isPresent() && ((c)registrywritable.p().a(newKey).get()).b()) {
         TerraformGeneratorPlugin.logger.info(String.valueOf(newKey) + " was already registered. Was there a plugin/server reload?");
      } else {
         Method register = registrywritable.getClass().getDeclaredMethod("a", ResourceKey.class, Object.class, Class.forName("net.minecraft.core.RegistrationInfo"));
         register.setAccessible(true);
         c<BiomeBase> holder = (c)register.invoke(registrywritable, newKey, biome, regInfo);
         Method bindValue = c.class.getDeclaredMethod("b", Object.class);
         bindValue.setAccessible(true);
         bindValue.invoke(holder, biome);
         Set<TagKey<BiomeBase>> tags = new HashSet();
         Stream var10000 = forestBiomeHolder.c();
         Objects.requireNonNull(tags);
         var10000.forEach(tags::add);
         Method bindTags = c.class.getDeclaredMethod("a", Collection.class);
         bindTags.setAccessible(true);
         bindTags.invoke(holder, tags);
         terraformGenBiomeRegistry.put(biomeType, newKey);
      }
   }

   public static Set<Holder<BiomeBase>> biomeListToBiomeBaseSet(@NotNull IRegistry<BiomeBase> registry) {
      List<Holder<BiomeBase>> biomeBases = new ArrayList();
      Registry.BIOME.iterator().forEachRemaining((biome) -> {
         try {
            if (biome == null) {
               return;
            }

            Holder<BiomeBase> holder = CraftBiome.bukkitToMinecraftHolder(biome);
            if (holder == null) {
               return;
            }

            biomeBases.add(holder);
         } catch (Throwable var3) {
            TerraformGeneratorPlugin.logger.info("Ignoring biome " + String.valueOf(biome));
         }

      });
      CustomBiomeType[] var2 = CustomBiomeType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CustomBiomeType cbt = var2[var4];
         if (cbt != CustomBiomeType.NONE) {
            ResourceKey<BiomeBase> rkey = (ResourceKey)terraformGenBiomeRegistry.get(cbt);
            Optional<c<BiomeBase>> holder = registry.a(rkey);
            Objects.requireNonNull(biomeBases);
            holder.ifPresent(biomeBases::add);
         }
      }

      return Set.copyOf(biomeBases);
   }
}
