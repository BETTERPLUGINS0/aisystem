package org.terraform.v1_21_R6;

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
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeSpecialEffects.Builder;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.version.TerraformFieldHandler;
import org.terraform.utils.version.TerraformMethodHandler;

public class CustomBiomeHandler {
   public static final HashMap<CustomBiomeType, ResourceKey<Biome>> terraformGenBiomeRegistry = new HashMap();

   public static Registry<Biome> getBiomeRegistry() {
      return (Registry)MinecraftServer.getServer().registryAccess().lookup(Registries.BIOME).orElseThrow();
   }

   public static void init() {
      CraftServer craftserver = (CraftServer)Bukkit.getServer();
      DedicatedServer dedicatedserver = craftserver.getServer();
      WritableRegistry registrywritable = (WritableRegistry)getBiomeRegistry();

      try {
         TerraformFieldHandler frozen = new TerraformFieldHandler(MappedRegistry.class, new String[]{"frozen", "l"});
         frozen.field.set(registrywritable, false);
         TerraformGeneratorPlugin.logger.info("Unfreezing biome registry...");
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var11) {
         TerraformGeneratorPlugin.logger.error(var11.toString());
         TerraformGeneratorPlugin.logger.stackTrace(var11);
         return;
      }

      Holder<Biome> forestbiome = registrywritable.createRegistrationLookup().getOrThrow(Biomes.FOREST);
      CustomBiomeType[] var4 = CustomBiomeType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         CustomBiomeType type = var4[var6];
         if (type != CustomBiomeType.NONE) {
            try {
               assert forestbiome != null;

               registerCustomBiome(type, dedicatedserver, registrywritable, forestbiome);
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
         TerraformFieldHandler frozen = new TerraformFieldHandler(MappedRegistry.class, new String[]{"frozen", "l"});
         frozen.field.set(registrywritable, true);
         TerraformGeneratorPlugin.logger.info("Freezing biome registry");
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var9) {
         TerraformGeneratorPlugin.logger.stackTrace(var9);
      }

   }

   private static void registerCustomBiome(@NotNull CustomBiomeType biomeType, DedicatedServer dedicatedserver, @NotNull WritableRegistry<Biome> registrywritable, @NotNull Holder<Biome> forestBiomeHolder) throws Throwable {
      Biome forestbiome = (Biome)forestBiomeHolder.value();
      TerraformFieldHandler defaultRegInfoField = new TerraformFieldHandler(ReloadableServerRegistries.class, new String[]{"b", "DEFAULT_REGISTRATION_INFO"});
      Object regInfo = defaultRegInfoField.field.get((Object)null);
      ResourceKey<Biome> newKey = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("terraformgenerator", biomeType.toString().toLowerCase(Locale.ENGLISH)));
      BiomeBuilder newBiomeBuilder = new BiomeBuilder();
      newBiomeBuilder.hasPrecipitation(forestbiome.hasPrecipitation());
      TerraformFieldHandler biomeSettingMobsField = new TerraformFieldHandler(Biome.class, new String[]{"mobSettings", "k"});
      MobSpawnSettings biomeSettingMobs = (MobSpawnSettings)biomeSettingMobsField.field.get(forestbiome);
      newBiomeBuilder.mobSpawnSettings(biomeSettingMobs);
      TerraformFieldHandler biomeSettingGenField = new TerraformFieldHandler(Biome.class, new String[]{"generationSettings", "j"});
      BiomeGenerationSettings biomeSettingGen = (BiomeGenerationSettings)biomeSettingGenField.field.get(forestbiome);
      newBiomeBuilder.generationSettings(biomeSettingGen);
      newBiomeBuilder.temperature(0.7F);
      newBiomeBuilder.downfall(biomeType.getRainFall());
      if (biomeType.isCold()) {
         newBiomeBuilder.temperatureAdjustment(TemperatureModifier.FROZEN);
      } else {
         newBiomeBuilder.temperatureAdjustment(TemperatureModifier.NONE);
      }

      Builder newFog = new Builder();
      newFog.fogColor(biomeType.getFogColor().isEmpty() ? forestbiome.getFogColor() : Integer.parseInt(biomeType.getFogColor(), 16)).waterColor(biomeType.getWaterColor().isEmpty() ? forestbiome.getWaterColor() : Integer.parseInt(biomeType.getWaterColor(), 16)).waterFogColor(biomeType.getWaterFogColor().isEmpty() ? forestbiome.getWaterFogColor() : Integer.parseInt(biomeType.getWaterFogColor(), 16)).skyColor(biomeType.getSkyColor().isEmpty() ? forestbiome.getSkyColor() : Integer.parseInt(biomeType.getSkyColor(), 16)).foliageColorOverride(biomeType.getFoliageColor().isEmpty() ? forestbiome.getFoliageColor() : Integer.parseInt(biomeType.getFoliageColor(), 16)).grassColorOverride(biomeType.getGrassColor().isEmpty() ? Integer.parseInt("79C05A", 16) : Integer.parseInt(biomeType.getGrassColor(), 16));
      newBiomeBuilder.specialEffects(newFog.build());
      Biome biome = newBiomeBuilder.build();
      if (registrywritable.createRegistrationLookup().get(newKey).isPresent() && ((Reference)registrywritable.createRegistrationLookup().get(newKey).get()).isBound()) {
         TerraformGeneratorPlugin.logger.info(String.valueOf(newKey) + " was already registered. Was there a plugin/server reload?");
      } else {
         TerraformMethodHandler register = new TerraformMethodHandler(registrywritable.getClass(), new String[]{"register", "a"}, new Class[]{ResourceKey.class, Object.class, Class.forName("net.minecraft.core.RegistrationInfo")});
         Reference<Biome> holder = (Reference)register.method.invoke(registrywritable, newKey, biome, regInfo);
         TerraformMethodHandler bindValue = new TerraformMethodHandler(Reference.class, new String[]{"bindValue", "b"}, new Class[]{Object.class});
         bindValue.method.invoke(holder, biome);
         Set<TagKey<Biome>> tags = new HashSet();
         Stream var10000 = forestBiomeHolder.tags();
         Objects.requireNonNull(tags);
         var10000.forEach(tags::add);
         TerraformMethodHandler bindTags = new TerraformMethodHandler(Reference.class, new String[]{"bindTags", "a"}, new Class[]{Collection.class});
         bindTags.method.invoke(holder, tags);
         terraformGenBiomeRegistry.put(biomeType, newKey);
      }
   }

   public static Set<Holder<Biome>> biomeListToBiomeSet(@NotNull Registry<Biome> registry) {
      List<Holder<Biome>> Biomes = new ArrayList();
      org.bukkit.Registry.BIOME.iterator().forEachRemaining((biome) -> {
         try {
            if (biome == null) {
               return;
            }

            Holder<Biome> holder = CraftBiome.bukkitToMinecraftHolder(biome);
            if (holder == null) {
               return;
            }

            Biomes.add(holder);
         } catch (Throwable var3) {
            TerraformGeneratorPlugin.logger.info("Ignoring biome " + String.valueOf(biome));
         }

      });
      CustomBiomeType[] var2 = CustomBiomeType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CustomBiomeType cbt = var2[var4];
         if (cbt != CustomBiomeType.NONE) {
            ResourceKey<Biome> rkey = (ResourceKey)terraformGenBiomeRegistry.get(cbt);
            Optional<Reference<Biome>> holder = registry.get(rkey);
            Objects.requireNonNull(Biomes);
            holder.ifPresent(Biomes::add);
         }
      }

      return Set.copyOf(Biomes);
   }
}
