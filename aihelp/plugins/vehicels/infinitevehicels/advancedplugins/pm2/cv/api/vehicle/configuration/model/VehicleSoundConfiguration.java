package advancedplugins.pm2.cv.api.vehicle.configuration.model;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.Validable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import com.google.common.base.Preconditions;
import gnu.trove.set.hash.THashSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleSoundConfiguration implements ConfigurationSectionWritable, Validable {
   @Nullable
   protected final Sound type;
   @Nullable
   protected final String typeCustom;
   @Nullable
   protected final SoundCategory category;
   protected final int delay;
   protected final float volume;
   protected final float pitch;
   protected final boolean global;
   private final Set<String> statesToApply;

   public static VehicleSoundConfiguration load(@NotNull ConfigurationSection var0) {
      String var1 = var0.getString("type");
      String var3 = null;
      if (StringUtils.isBlank(var1)) {
         throw new InvalidConfigurationException("sound type must be set");
      } else {
         Sound var2;
         try {
            var2 = (Sound)Sound.class.getField(var1.trim().toUpperCase()).get((Object)null);
         } catch (Exception var12) {
            var2 = null;
         }

         if (var2 == null) {
            var3 = var1;
         }

         SoundCategory var4 = (SoundCategory)ConfigurationUtil.loadEnum(SoundCategory.class, var0, "category");
         int var5 = var0.getInt("delay");
         float var6 = (float)var0.getDouble("volume");
         float var7 = (float)var0.getDouble("pitch");
         boolean var8 = var0.getBoolean("global");
         if (var5 < 0) {
            throw new InvalidConfigurationException("delay cannot be negative");
         } else if (var6 <= 0.0F) {
            throw new InvalidConfigurationException("volume must be greater than 0");
         } else {
            THashSet var9 = new THashSet();
            String var10 = var0.getString("states-to-apply");
            if (StringUtils.isNotBlank(var10)) {
               String[] var11 = var10.split(",");
               Stream var10000 = Arrays.stream(var11).filter(StringUtils::isNotBlank).map((var0x) -> {
                  return var0x.toLowerCase().trim();
               });
               Objects.requireNonNull(var9);
               var10000.forEach(var9::add);
            }

            return new VehicleSoundConfiguration(var2, var3, var4, var5, var6, var7, var8, var9);
         }
      }
   }

   public boolean appliesTo(@NotNull VehicleState var1) {
      Iterator var2 = this.statesToApply.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (String)var2.next();
      } while(!var1.getName().equalsIgnoreCase(var3));

      return true;
   }

   public VehicleSoundConfiguration(@Nullable Sound var1, @Nullable String var2, @Nullable SoundCategory var3, int var4, float var5, float var6, boolean var7, @Nullable Collection<String> var8) {
      this.statesToApply = new THashSet();
      Preconditions.checkArgument(var1 != null || StringUtils.isNotBlank(var2), "either a valid type or a valid typeCustom must be provided");
      if (var1 == null) {
         Preconditions.checkArgument(StringUtils.isNotBlank(var2), "typeCustom cannot be blank");
      }

      Preconditions.checkArgument(var4 >= 0, "delay cannot be negative");
      Preconditions.checkArgument(var5 > 0.0F, "volume must be > 0");
      this.type = var1;
      this.typeCustom = var2;
      this.category = var3;
      this.delay = var4;
      this.volume = var5;
      this.pitch = var6;
      this.global = var7;
      if (var8 != null) {
         this.statesToApply.addAll(var8);
      }

   }

   public VehicleSoundConfiguration(@NotNull Sound var1, @Nullable SoundCategory var2, int var3, float var4, float var5, boolean var6, @Nullable Collection<String> var7) {
      this(var1, (String)null, var2, var3, var4, var5, var6, var7);
   }

   public VehicleSoundConfiguration(@NotNull String var1, @Nullable SoundCategory var2, int var3, float var4, float var5, boolean var6, @Nullable Collection<String> var7) {
      this((Sound)null, var1, var2, var3, var4, var5, var6, var7);
   }

   public boolean isValid() {
      return this.type != null || StringUtils.isNotBlank(this.typeCustom);
   }

   public boolean applies(@NotNull VehicleState var1) {
      return this.statesToApply.contains(var1.getName());
   }

   public void write(@NotNull ConfigurationSection var1) {
      if (this.type != null) {
         var1.set("type", this.type.name());
      } else if (StringUtils.isNotBlank(this.typeCustom)) {
         var1.set("type", this.typeCustom);
      }

      if (this.category != null) {
         ConfigurationUtil.writeEnum(this.category, var1, "category");
      }

      var1.set("delay", this.delay);
      var1.set("volume", this.volume);
      var1.set("pitch", this.pitch);
      var1.set("global", this.global);
      if (this.statesToApply.size() > 0) {
         var1.set("states-to-apply", String.join(",", this.statesToApply));
      } else {
         var1.set("states-to-apply", (Object)null);
      }

   }

   public static VehicleSoundConfiguration.VehicleSoundConfigurationBuilder builder() {
      return new VehicleSoundConfiguration.VehicleSoundConfigurationBuilder();
   }

   public static VehicleSoundConfiguration.VehicleSoundConfigurationBuilder builderCustomType() {
      return new VehicleSoundConfiguration.VehicleSoundConfigurationBuilder();
   }

   @Nullable
   public Sound getType() {
      return this.type;
   }

   @Nullable
   public String getTypeCustom() {
      return this.typeCustom;
   }

   @Nullable
   public SoundCategory getCategory() {
      return this.category;
   }

   public int getDelay() {
      return this.delay;
   }

   public float getVolume() {
      return this.volume;
   }

   public float getPitch() {
      return this.pitch;
   }

   public boolean isGlobal() {
      return this.global;
   }

   public Set<String> getStatesToApply() {
      return this.statesToApply;
   }

   public static class VehicleSoundConfigurationBuilder {
      private Sound type;
      private SoundCategory category;
      private int delay;
      private float volume;
      private float pitch;
      private boolean global;
      private Collection<String> statesToApply;
      private String typeCustom;

      VehicleSoundConfigurationBuilder() {
      }

      public VehicleSoundConfiguration.VehicleSoundConfigurationBuilder type(@NotNull Sound var1) {
         this.type = var1;
         return this;
      }

      public VehicleSoundConfiguration.VehicleSoundConfigurationBuilder category(@Nullable SoundCategory var1) {
         this.category = var1;
         return this;
      }

      public VehicleSoundConfiguration.VehicleSoundConfigurationBuilder delay(int var1) {
         this.delay = var1;
         return this;
      }

      public VehicleSoundConfiguration.VehicleSoundConfigurationBuilder volume(float var1) {
         this.volume = var1;
         return this;
      }

      public VehicleSoundConfiguration.VehicleSoundConfigurationBuilder pitch(float var1) {
         this.pitch = var1;
         return this;
      }

      public VehicleSoundConfiguration.VehicleSoundConfigurationBuilder global(boolean var1) {
         this.global = var1;
         return this;
      }

      public VehicleSoundConfiguration.VehicleSoundConfigurationBuilder statesToApply(@Nullable Collection<String> var1) {
         this.statesToApply = var1;
         return this;
      }

      public VehicleSoundConfiguration build() {
         return new VehicleSoundConfiguration(this.type, this.category, this.delay, this.volume, this.pitch, this.global, this.statesToApply);
      }

      public String toString() {
         String var10000 = String.valueOf(this.type);
         return "VehicleSoundConfiguration.VehicleSoundConfigurationBuilder(type=" + var10000 + ", category=" + String.valueOf(this.category) + ", delay=" + this.delay + ", volume=" + this.volume + ", pitch=" + this.pitch + ", global=" + this.global + ", statesToApply=" + String.valueOf(this.statesToApply) + ")";
      }

      public VehicleSoundConfiguration.VehicleSoundConfigurationBuilder typeCustom(@NotNull String var1) {
         this.typeCustom = var1;
         return this;
      }
   }
}
