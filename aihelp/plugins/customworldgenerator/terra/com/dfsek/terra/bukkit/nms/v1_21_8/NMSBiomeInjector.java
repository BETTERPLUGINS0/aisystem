package com.dfsek.terra.bukkit.nms.v1_21_8;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.bukkit.nms.v1_21_8.config.VanillaBiomeProperties;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeGenerationSettings.PlainBuilder;
import net.minecraft.world.level.biome.BiomeSpecialEffects.Builder;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;

public class NMSBiomeInjector {
   public static <T> Optional<Holder<T>> getEntry(Registry<T> registry, ResourceLocation identifier) {
      Optional var10000 = registry.getOptional(identifier);
      Objects.requireNonNull(registry);
      var10000 = var10000.flatMap(registry::getResourceKey);
      Objects.requireNonNull(registry);
      return var10000.flatMap(registry::get);
   }

   public static Biome createBiome(Biome vanilla, VanillaBiomeProperties vanillaBiomeProperties) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
      BiomeBuilder builder = new BiomeBuilder();
      Builder effects = new Builder();
      effects.fogColor((Integer)Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor())).waterColor((Integer)Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor())).waterFogColor((Integer)Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor())).skyColor((Integer)Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor())).grassColorModifier((GrassColorModifier)Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColorModifier(), vanilla.getSpecialEffects().getGrassColorModifier())).backgroundMusicVolume((Float)Objects.requireNonNullElse(vanillaBiomeProperties.getMusicVolume(), vanilla.getBackgroundMusicVolume()));
      Optional var10000;
      if (vanillaBiomeProperties.getGrassColor() == null) {
         var10000 = vanilla.getSpecialEffects().getGrassColorOverride();
         Objects.requireNonNull(effects);
         var10000.ifPresent(effects::grassColorOverride);
      } else {
         effects.grassColorOverride(vanillaBiomeProperties.getGrassColor());
      }

      if (vanillaBiomeProperties.getFoliageColor() == null) {
         var10000 = vanilla.getSpecialEffects().getFoliageColorOverride();
         Objects.requireNonNull(effects);
         var10000.ifPresent(effects::foliageColorOverride);
      } else {
         effects.foliageColorOverride(vanillaBiomeProperties.getFoliageColor());
      }

      if (vanillaBiomeProperties.getParticleConfig() == null) {
         var10000 = vanilla.getSpecialEffects().getAmbientParticleSettings();
         Objects.requireNonNull(effects);
         var10000.ifPresent(effects::ambientParticle);
      } else {
         effects.ambientParticle(vanillaBiomeProperties.getParticleConfig());
      }

      if (vanillaBiomeProperties.getLoopSound() == null) {
         var10000 = vanilla.getSpecialEffects().getAmbientLoopSoundEvent();
         Objects.requireNonNull(effects);
         var10000.ifPresent(effects::ambientLoopSound);
      } else {
         var10000 = RegistryFetcher.soundEventRegistry().get(vanillaBiomeProperties.getLoopSound().location());
         Objects.requireNonNull(effects);
         var10000.ifPresent(effects::ambientLoopSound);
      }

      if (vanillaBiomeProperties.getMoodSound() == null) {
         var10000 = vanilla.getSpecialEffects().getAmbientMoodSettings();
         Objects.requireNonNull(effects);
         var10000.ifPresent(effects::ambientMoodSound);
      } else {
         effects.ambientMoodSound(vanillaBiomeProperties.getMoodSound());
      }

      if (vanillaBiomeProperties.getAdditionsSound() == null) {
         var10000 = vanilla.getSpecialEffects().getAmbientAdditionsSettings();
         Objects.requireNonNull(effects);
         var10000.ifPresent(effects::ambientAdditionsSound);
      } else {
         effects.ambientAdditionsSound(vanillaBiomeProperties.getAdditionsSound());
      }

      if (vanillaBiomeProperties.getMusic() == null) {
         var10000 = vanilla.getSpecialEffects().getBackgroundMusic();
         Objects.requireNonNull(effects);
         var10000.ifPresent(effects::backgroundMusic);
      } else {
         effects.backgroundMusic(vanillaBiomeProperties.getMusic());
      }

      builder.hasPrecipitation((Boolean)Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.hasPrecipitation()));
      builder.temperature((Float)Objects.requireNonNullElse(vanillaBiomeProperties.getTemperature(), vanilla.getBaseTemperature()));
      builder.downfall((Float)Objects.requireNonNullElse(vanillaBiomeProperties.getDownfall(), vanilla.climateSettings.downfall()));
      builder.temperatureAdjustment((TemperatureModifier)Objects.requireNonNullElse(vanillaBiomeProperties.getTemperatureModifier(), vanilla.climateSettings.temperatureModifier()));
      builder.mobSpawnSettings((MobSpawnSettings)Objects.requireNonNullElse(vanillaBiomeProperties.getSpawnSettings(), vanilla.getMobSettings()));
      return builder.specialEffects(effects.build()).generationSettings((new PlainBuilder()).build()).build();
   }

   public static String createBiomeID(ConfigPack pack, RegistryKey biomeID) {
      String var10000 = pack.getID().toLowerCase();
      return var10000 + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
   }
}
