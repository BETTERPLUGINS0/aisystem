package advancedplugins.pm2.cv.api.vehicle.configuration.model;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.Validable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.util.reflection.EnumReflection;
import advancedplugins.pm2.cv.api.vehicle.configuration.data.DataParser;
import advancedplugins.pm2.cv.api.vehicle.configuration.data.DataParsers;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleEffectConfiguration implements ConfigurationSectionWritable, Validable {
   @Nullable
   private final Particle particleType;
   private final int particleCount;
   private final float particleDispersion;
   @Nullable
   private final Object particleData;
   @Nullable
   protected final Sound soundType;
   @Nullable
   protected final String soundTypeCustom;
   @Nullable
   protected final SoundCategory soundCategory;
   protected final float soundVolume;
   protected final float soundPitch;

   public static VehicleEffectConfiguration load(@NotNull ConfigurationSection var0) {
      ConfigurationSection var1 = var0.getConfigurationSection("particle");
      ConfigurationSection var2 = var0.getConfigurationSection("sound");
      Particle var3 = null;
      int var4 = 0;
      float var5 = 0.0F;
      Object var6 = null;
      if (var1 != null) {
         var3 = (Particle)ConfigurationUtil.loadEnum(Particle.class, var1, "type", true);
         var4 = var1.getInt("count");
         var5 = (float)var1.getDouble("dispersion");
         ConfigurationSection var7 = var1.getConfigurationSection("data");
         if (var7 != null) {
            DataParser var8 = DataParsers.matchParser(var7);
            if (var8 != null) {
               var6 = var8.parse(var7);
            }
         }
      }

      Sound var13 = null;
      String var14 = null;
      SoundCategory var9 = null;
      float var10 = 0.0F;
      float var11 = 0.0F;
      if (var2 != null) {
         String var12 = var2.getString("type");
         if (var12 != null && (var13 = (Sound)EnumReflection.getOldEnumConstant(Sound.class, var12.trim().toUpperCase())) == null) {
            var14 = var12;
         }

         var9 = (SoundCategory)ConfigurationUtil.loadEnum(SoundCategory.class, var2, "category");
         var10 = (float)var2.getDouble("volume");
         var11 = (float)var2.getDouble("pitch");
         if (var10 < 0.0F) {
            throw new InvalidConfigurationException("volume cannot be negative");
         }
      }

      return new VehicleEffectConfiguration(var3, var4, var5, var6, var13, var14, var9, var10, var11);
   }

   public VehicleEffectConfiguration(@Nullable Particle var1, int var2, float var3, @Nullable Object var4, @Nullable Sound var5, @Nullable String var6, @Nullable SoundCategory var7, float var8, float var9) {
      this.particleType = var1;
      this.particleCount = var2;
      this.particleDispersion = var3;
      this.particleData = var4;
      this.soundType = var5;
      this.soundTypeCustom = var6;
      this.soundCategory = var7;
      this.soundVolume = var8;
      this.soundPitch = var9;
   }

   public boolean isValidParticle() {
      return this.particleType != null && this.particleCount > 0;
   }

   public boolean isValidSound() {
      return (this.soundType != null || StringUtils.isNotBlank(this.soundTypeCustom)) && this.soundVolume > 0.0F;
   }

   public boolean isValid() {
      return this.isValidParticle() || this.isValidSound();
   }

   public void write(@NotNull ConfigurationSection var1) {
      ConfigurationSection var2 = var1.createSection("particle");
      ConfigurationSection var3 = var1.createSection("sound");
      if (this.particleType != null) {
         ConfigurationUtil.writeEnum(this.particleType, var2, "type");
      }

      if (this.particleCount > 0) {
         var2.set("count", this.particleCount);
      }

      if (this.particleDispersion != 0.0F) {
         var2.set("dispersion", this.particleDispersion);
      }

      if (this.particleData != null) {
         DataParser var4 = DataParsers.getParser(this.particleData.getClass());
         if (var4 != null) {
            var4.write(this.particleData, var2.createSection("data"));
         }
      }

      if (this.soundType != null) {
         var3.set("type", this.soundType.name());
      } else if (StringUtils.isNotBlank(this.soundTypeCustom)) {
         var3.set("type", this.soundTypeCustom);
      }

      if (this.soundCategory != null) {
         ConfigurationUtil.writeEnum(this.soundCategory, var3, "category");
      }

      if (this.soundVolume != 0.0F) {
         var3.set("volume", this.soundVolume);
      }

      if (this.soundPitch != 0.0F) {
         var3.set("pitch", this.soundPitch);
      }

   }

   @Nullable
   public Particle getParticleType() {
      return this.particleType;
   }

   public int getParticleCount() {
      return this.particleCount;
   }

   public float getParticleDispersion() {
      return this.particleDispersion;
   }

   @Nullable
   public Object getParticleData() {
      return this.particleData;
   }

   @Nullable
   public Sound getSoundType() {
      return this.soundType;
   }

   @Nullable
   public String getSoundTypeCustom() {
      return this.soundTypeCustom;
   }

   @Nullable
   public SoundCategory getSoundCategory() {
      return this.soundCategory;
   }

   public float getSoundVolume() {
      return this.soundVolume;
   }

   public float getSoundPitch() {
      return this.soundPitch;
   }
}
