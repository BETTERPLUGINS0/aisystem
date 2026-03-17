package advancedplugins.pm2.cv.api.vehicle.configuration.model.compound;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.util.MathUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AnimationKeyframeConfiguration implements ConfigurationSectionWritable {
   private final int duration;
   private final Map<UUID, Vector3D> translations = new HashMap();
   private final Map<UUID, Vector3D> rotations = new HashMap();
   private final Map<UUID, Vector3D> scales = new HashMap();
   private Map<UUID, Quaternionf> blockBenchRotations;
   private Map<UUID, Vector3f> blockBenchPositions;
   private Map<UUID, Vector3f> blockBenchScales;

   public static AnimationKeyframeConfiguration load(@NotNull ConfigurationSection var0) {
      int var1 = var0.getInt("duration");
      if (var1 < 0) {
         throw new InvalidConfigurationException("duration cannot be negative");
      } else {
         Map var2 = loadVectors(var0, "translations", false);
         Map var3 = loadVectors(var0, "rotations", true);
         Map var4 = loadVectors(var0, "scales", false);
         if (var2.isEmpty() && var3.isEmpty() && var4.isEmpty()) {
            throw new InvalidConfigurationException("at least one transformation is required");
         } else {
            return new AnimationKeyframeConfiguration(var1, var2, var3, var4);
         }
      }
   }

   private static Map<UUID, Vector3D> loadVectors(ConfigurationSection var0, String var1, boolean var2) {
      HashMap var3 = new HashMap();
      Iterator var4 = ConfigurationUtil.getConfigurationSectionsAfter(var0, var1, false).iterator();

      while(var4.hasNext()) {
         ConfigurationSection var5 = (ConfigurationSection)var4.next();
         UUID var6 = (UUID)ConfigurationUtil.loadLibrarySingleEntryObject(UUID.class, var5, "identifier");
         if (var6 != null) {
            ConfigurationSection var7 = var5.getConfigurationSection("value");
            if (var7 != null) {
               Vector3D var8 = (Vector3D)ConfigurationUtil.loadLibraryObject(Vector3D.class, var7);
               if (var8 != null) {
                  var3.put(var6, var2 ? MathUtil.toRadians(var8) : var8);
               }
            }
         }
      }

      return var3;
   }

   public AnimationKeyframeConfiguration(int var1, @Nullable Map<UUID, Vector3D> var2, @Nullable Map<UUID, Vector3D> var3, @Nullable Map<UUID, Vector3D> var4) {
      this.duration = var1;
      if (var2 != null) {
         this.translations.putAll(var2);
      }

      if (var3 != null) {
         this.rotations.putAll(var3);
      }

      if (var4 != null) {
         this.scales.putAll(var4);
      }

      this.blockBenchRotations = new HashMap();
      this.blockBenchPositions = new HashMap();
      this.blockBenchScales = new HashMap();
   }

   public AnimationKeyframeConfiguration setBlockBenchScales(Map<UUID, Vector3f> var1) {
      this.blockBenchScales = var1;
      return this;
   }

   public AnimationKeyframeConfiguration setBlockBenchRotations(Map<UUID, Quaternionf> var1) {
      this.blockBenchRotations = var1;
      return this;
   }

   public AnimationKeyframeConfiguration setBlockBenchPositions(Map<UUID, Vector3f> var1) {
      this.blockBenchPositions = var1;
      return this;
   }

   public Map<UUID, Vector3D> getTranslations() {
      return Collections.unmodifiableMap(this.translations);
   }

   @Nullable
   public Vector3D getTranslation(@NotNull UUID var1) {
      return (Vector3D)this.translations.get(var1);
   }

   @Nullable
   public Vector3D getTranslation(@NotNull BoneConfiguration var1) {
      return this.getTranslation(var1.getIdentifier());
   }

   public Map<UUID, Vector3D> getRotations() {
      return Collections.unmodifiableMap(this.rotations);
   }

   @Nullable
   public Vector3D getRotation(@NotNull UUID var1) {
      return (Vector3D)this.rotations.get(var1);
   }

   @Nullable
   public Vector3D getRotation(@NotNull BoneConfiguration var1) {
      return this.getRotation(var1.getIdentifier());
   }

   public Map<UUID, Vector3D> getScales() {
      return Collections.unmodifiableMap(this.scales);
   }

   @Nullable
   public Vector3D getScale(@NotNull UUID var1) {
      return (Vector3D)this.scales.get(var1);
   }

   @Nullable
   public Vector3D getScale(@NotNull BoneConfiguration var1) {
      return this.getScale(var1.getIdentifier());
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("duration", this.duration);
      this.writeVectors(this.translations, var1, "translations", false);
      this.writeVectors(this.rotations, var1, "rotations", true);
      this.writeVectors(this.scales, var1, "scales", false);
   }

   private void writeVectors(Map<UUID, Vector3D> var1, ConfigurationSection var2, String var3, boolean var4) {
      if (var1.size() == 0) {
         var2.set(var3, (Object)null);
      } else {
         ConfigurationSection var5 = var2.createSection(var3);
         int var6 = 0;
         Iterator var7 = var1.entrySet().iterator();

         while(var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            int var10001 = var6++;
            ConfigurationSection var9 = var5.createSection("v-" + var10001);
            ConfigurationUtil.writeSingleEntryLibraryObject(UUID.class, (UUID)var8.getKey(), var9, "identifier");
            ConfigurationUtil.writeLibraryObject(Vector3D.class, var4 ? MathUtil.toDegrees((Vector3D)var8.getValue()) : (Vector3D)var8.getValue(), var9.createSection("value"));
         }

      }
   }

   public static AnimationKeyframeConfiguration.AnimationKeyframeConfigurationBuilder builder() {
      return new AnimationKeyframeConfiguration.AnimationKeyframeConfigurationBuilder();
   }

   public String toString() {
      int var10000 = this.getDuration();
      return "AnimationKeyframeConfiguration(duration=" + var10000 + ", translations=" + String.valueOf(this.getTranslations()) + ", rotations=" + String.valueOf(this.getRotations()) + ", scales=" + String.valueOf(this.getScales()) + ", blockBenchRotations=" + String.valueOf(this.getBlockBenchRotations()) + ", blockBenchPositions=" + String.valueOf(this.getBlockBenchPositions()) + ", blockBenchScales=" + String.valueOf(this.getBlockBenchScales()) + ")";
   }

   public int getDuration() {
      return this.duration;
   }

   public Map<UUID, Quaternionf> getBlockBenchRotations() {
      return this.blockBenchRotations;
   }

   public Map<UUID, Vector3f> getBlockBenchPositions() {
      return this.blockBenchPositions;
   }

   public Map<UUID, Vector3f> getBlockBenchScales() {
      return this.blockBenchScales;
   }

   public static class AnimationKeyframeConfigurationBuilder {
      private int duration;
      private Map<UUID, Vector3D> translations;
      private Map<UUID, Vector3D> rotations;
      private Map<UUID, Vector3D> scales;

      AnimationKeyframeConfigurationBuilder() {
      }

      public AnimationKeyframeConfiguration.AnimationKeyframeConfigurationBuilder duration(int var1) {
         this.duration = var1;
         return this;
      }

      public AnimationKeyframeConfiguration.AnimationKeyframeConfigurationBuilder translations(@Nullable Map<UUID, Vector3D> var1) {
         this.translations = var1;
         return this;
      }

      public AnimationKeyframeConfiguration.AnimationKeyframeConfigurationBuilder rotations(@Nullable Map<UUID, Vector3D> var1) {
         this.rotations = var1;
         return this;
      }

      public AnimationKeyframeConfiguration.AnimationKeyframeConfigurationBuilder scales(@Nullable Map<UUID, Vector3D> var1) {
         this.scales = var1;
         return this;
      }

      public AnimationKeyframeConfiguration build() {
         return new AnimationKeyframeConfiguration(this.duration, this.translations, this.rotations, this.scales);
      }

      public String toString() {
         int var10000 = this.duration;
         return "AnimationKeyframeConfiguration.AnimationKeyframeConfigurationBuilder(duration=" + var10000 + ", translations=" + String.valueOf(this.translations) + ", rotations=" + String.valueOf(this.rotations) + ", scales=" + String.valueOf(this.scales) + ")";
      }
   }
}
