package advancedplugins.pm2.cv.api.configuration;

import advancedplugins.pm2.cv.api.InfiniteVehiclesPluginBase;
import advancedplugins.pm2.cv.api.util.reflection.EnumReflection;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import me.PM2.infinitevehicles.xseries.XSound;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Configuration {
   public static final Configuration.Entry RENDER_DELAY = new Configuration.Entry("render.delay", 1);
   public static final Configuration.Entry RENDER_FAR_AWAY_CHUNKS = new Configuration.Entry("render.far-away-chunks", 5);
   public static final Configuration.Entry OWNERSHIP_ONLY_OWNER = new Configuration.Entry("ownership.only-owner-can-operate", false);
   public static final Configuration.Entry HEALTH_SHOW_ON_MOUNT_HEALTH_BAR = new Configuration.Entry("health.show-on-mount-health-bar", true);
   public static final Configuration.Entry FUEL_ENABLE = new Configuration.Entry("fuel-system.enable", true);
   public static final Configuration.Entry FUEL_BYPASS_CREATIVE = new Configuration.Entry("fuel-system.bypass-creative-mode", true);
   public static final Configuration.Entry PICKUP_ENABLE = new Configuration.Entry("pickup-system.enable", true);
   public static final Configuration.Entry PICKUP_ONLY_OWNER = new Configuration.Entry("pickup-system.only-owner-can-pickup", true);
   public static final Configuration.Entry PICKUP_PARTICLE_ENABLE = new Configuration.Entry("pickup-system.particle.enable", true);
   public static final Configuration.Entry PICKUP_PARTICLE_TYPE;
   public static final Configuration.Entry PICKUP_PARTICLE_DISPERSION;
   public static final Configuration.Entry PICKUP_PARTICLE_AMOUNT;
   public static final Configuration.Entry PICKUP_SOUND_ENABLE;
   public static final Configuration.Entry PICKUP_SOUND_TYPE;
   public static final Configuration.Entry PICKUP_SOUND_VOLUME;
   public static final Configuration.Entry PICKUP_SOUND_PITCH;
   public static final Configuration.Entry COMPATIBILITY_WEAPON_MECHANICS;

   public static void load(@NotNull InfiniteVehiclesPluginBase var0) {
      File var1 = new File(var0.getDataFolder(), "BaseConfiguration.yml");
      if (!var1.exists()) {
         var1.getParentFile().mkdirs();

         try {
            Files.createFile(var1.toPath());
         } catch (IOException var10) {
            throw new IllegalStateException("couldn't generate base configuration file", var10);
         }
      }

      YamlConfiguration var2 = YamlConfiguration.loadConfiguration(var1);
      Field[] var3 = Configuration.class.getFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field var6 = var3[var5];
         if (Configuration.Entry.class.isAssignableFrom(var6.getType())) {
            try {
               ((Configuration.Entry)var6.get((Object)null)).load(var2);
            } catch (IllegalAccessException var9) {
               var9.printStackTrace();
            }
         }
      }

      try {
         var2.save(var1);
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   static {
      PICKUP_PARTICLE_TYPE = new Configuration.Entry("pickup-system.particle.type", Particle.CLOUD.name());
      PICKUP_PARTICLE_DISPERSION = new Configuration.Entry("pickup-system.particle.dispersion", 1.5F);
      PICKUP_PARTICLE_AMOUNT = new Configuration.Entry("pickup-system.particle.amount", 20);
      PICKUP_SOUND_ENABLE = new Configuration.Entry("pickup-system.sound.enable", true);
      PICKUP_SOUND_TYPE = new Configuration.Entry("pickup-system.sound.type", XSound.ENTITY_CHICKEN_EGG.name());
      PICKUP_SOUND_VOLUME = new Configuration.Entry("pickup-system.sound.volume", 1.5F);
      PICKUP_SOUND_PITCH = new Configuration.Entry("pickup-system.sound.pitch", 1.0F);
      COMPATIBILITY_WEAPON_MECHANICS = new Configuration.Entry("compatibility.weapon-mechanics", false);
   }

   public static class Entry {
      @NotNull
      private final String key;
      @NotNull
      private final Object defaultValue;
      @Nullable
      private Object value;

      private Entry(@NotNull String var1, @NotNull Object var2) {
         this.key = var1;
         this.defaultValue = var2;
         this.value = var2;
      }

      @NotNull
      public Object value() {
         return this.value != null ? this.value : this.defaultValue;
      }

      @NotNull
      public <T> T value(@NotNull Class<T> var1) {
         if (!this.compatible(var1)) {
            throw new IllegalArgumentException("type mismatch");
         } else {
            return var1.cast(this.value != null ? this.value : this.defaultValue);
         }
      }

      public boolean booleanValue() {
         return (Boolean)this.value(Boolean.class);
      }

      @NotNull
      public Number numericValue() {
         return (Number)this.value(Number.class);
      }

      public int intValue() {
         return this.numericValue().intValue();
      }

      public int intValueClamp(int var1, int var2) {
         return Math.max(var1, Math.min(var2, this.intValue()));
      }

      public long longValue() {
         return this.numericValue().longValue();
      }

      public long longValueClamp(long var1, long var3) {
         return Math.max(var1, Math.min(var3, this.longValue()));
      }

      public double doubleValue() {
         return this.numericValue().doubleValue();
      }

      public double doubleValueClamp(double var1, double var3) {
         return Math.max(var1, Math.min(var3, this.doubleValue()));
      }

      public float floatValue() {
         return this.numericValue().floatValue();
      }

      public float floatValueClamp(float var1, float var2) {
         return Math.max(var1, Math.min(var2, this.floatValue()));
      }

      @NotNull
      public String stringValue() {
         return (String)this.value(String.class);
      }

      @Nullable
      public <T extends Enum<T>> T enumValue(Class<T> var1) {
         return EnumReflection.getEnumConstant(var1, this.stringValue());
      }

      private void load(@NotNull ConfigurationSection var1) {
         this.value = var1.get(this.key);
         if (this.value == null || !this.compatible(this.value.getClass())) {
            this.value = this.defaultValue;
            var1.set(this.key, this.defaultValue);
         }

      }

      private boolean compatible(@NotNull Class<?> var1) {
         return this.defaultValue.getClass().isAssignableFrom(var1) || this.defaultValue instanceof Number && Number.class.isAssignableFrom(var1);
      }
   }
}
