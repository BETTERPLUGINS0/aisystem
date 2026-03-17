package advancedplugins.pm2.cv.api.vehicle.configuration.model;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.Validable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.data.DataParser;
import advancedplugins.pm2.cv.api.vehicle.configuration.data.DataParsers;
import com.google.common.base.Preconditions;
import gnu.trove.set.hash.THashSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.xseries.particles.XParticle;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleParticleConfiguration implements ConfigurationSectionWritable, Validable {
   @NotNull
   private final Particle type;
   @NotNull
   private final Vector3D offset;
   private final int delay;
   private final int count;
   private final float dispersion;
   @Nullable
   private final Object data;
   private final Set<String> statesToApply = new THashSet();

   public static VehicleParticleConfiguration load(@NotNull ConfigurationSection var0) {
      String var1 = var0.getString("type");
      XParticle var2 = var1 != null ? (XParticle)XParticle.of(var1.toUpperCase()).orElse((Object)null) : null;
      if (var2 == null) {
         throw new InvalidConfigurationException("invalid particle type: " + var0.getString("type"));
      } else {
         Particle var3 = var2.get();
         Vector3D var4 = (Vector3D)ConfigurationUtil.loadLibraryObject(Vector3D.class, var0, "offset");
         if (var4 == null) {
            throw new InvalidConfigurationException("particle requires offset to be set");
         } else {
            int var5 = var0.getInt("delay");
            int var6 = var0.getInt("count");
            float var7 = (float)var0.getDouble("dispersion");
            Object var8 = null;
            ConfigurationSection var9 = var0.getConfigurationSection("data");
            if (var9 != null) {
               DataParser var10 = DataParsers.matchParser(var9);
               if (var10 != null) {
                  var8 = var10.parse(var9);
               }
            }

            THashSet var13 = new THashSet();
            String var11 = var0.getString("states-to-apply");
            if (StringUtils.isNotBlank(var11)) {
               String[] var12 = var11.split(",");
               Stream var10000 = Arrays.stream(var12).filter(StringUtils::isNotBlank).map((var0x) -> {
                  return var0x.toLowerCase().trim();
               });
               Objects.requireNonNull(var13);
               var10000.forEach(var13::add);
            }

            return new VehicleParticleConfiguration(var3, var4, var5, var6, var7, var8, var13);
         }
      }
   }

   public VehicleParticleConfiguration(@NotNull Particle var1, @NotNull Vector3D var2, int var3, int var4, float var5, @Nullable Object var6, @Nullable Collection<String> var7) {
      Preconditions.checkArgument(var3 >= 0, "delay cannot be negative");
      Preconditions.checkArgument(var4 > 0, "count must be > 0");
      Preconditions.checkArgument(var5 >= 0.0F, "dispersion cannot be negative");
      this.type = var1;
      this.offset = var2;
      this.delay = var3;
      this.count = var4;
      this.dispersion = var5;
      this.data = var6;
      if (var7 != null) {
         this.statesToApply.addAll(var7);
      }

   }

   public boolean isValid() {
      Class var1 = this.type.getDataType();
      return var1 == Void.class || this.data != null && var1.isAssignableFrom(this.data.getClass());
   }

   @Nullable
   public <T> T getDataAs(Class<T> var1) {
      return var1.cast(this.data);
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

   public void write(@NotNull ConfigurationSection var1) {
      ConfigurationUtil.writeEnum(this.type, var1, "type");
      ConfigurationUtil.writeLibraryObject(Vector3D.class, this.offset, var1.createSection("offset"));
      var1.set("delay", this.delay);
      var1.set("count", this.count);
      var1.set("dispersion", this.dispersion);
      if (this.data != null) {
         DataParser var2 = DataParsers.getParser(this.data.getClass());
         if (var2 != null) {
            var2.write(this.data, var1.createSection("data"));
         }
      }

      if (this.statesToApply.size() > 0) {
         var1.set("states-to-apply", String.join(",", this.statesToApply));
      } else {
         var1.set("states-to-apply", (Object)null);
      }

   }

   public static VehicleParticleConfiguration.VehicleParticleConfigurationBuilder builder() {
      return new VehicleParticleConfiguration.VehicleParticleConfigurationBuilder();
   }

   @NotNull
   public Particle getType() {
      return this.type;
   }

   @NotNull
   public Vector3D getOffset() {
      return this.offset;
   }

   public int getDelay() {
      return this.delay;
   }

   public int getCount() {
      return this.count;
   }

   public float getDispersion() {
      return this.dispersion;
   }

   @Nullable
   public Object getData() {
      return this.data;
   }

   public Set<String> getStatesToApply() {
      return this.statesToApply;
   }

   public static class VehicleParticleConfigurationBuilder {
      private Particle type;
      private Vector3D offset;
      private int delay;
      private int count;
      private float dispersion;
      private Object data;
      private Collection<String> statesToApply;

      VehicleParticleConfigurationBuilder() {
      }

      public VehicleParticleConfiguration.VehicleParticleConfigurationBuilder type(@NotNull Particle var1) {
         this.type = var1;
         return this;
      }

      public VehicleParticleConfiguration.VehicleParticleConfigurationBuilder offset(@NotNull Vector3D var1) {
         this.offset = var1;
         return this;
      }

      public VehicleParticleConfiguration.VehicleParticleConfigurationBuilder delay(int var1) {
         this.delay = var1;
         return this;
      }

      public VehicleParticleConfiguration.VehicleParticleConfigurationBuilder count(int var1) {
         this.count = var1;
         return this;
      }

      public VehicleParticleConfiguration.VehicleParticleConfigurationBuilder dispersion(float var1) {
         this.dispersion = var1;
         return this;
      }

      public VehicleParticleConfiguration.VehicleParticleConfigurationBuilder data(@Nullable Object var1) {
         this.data = var1;
         return this;
      }

      public VehicleParticleConfiguration.VehicleParticleConfigurationBuilder statesToApply(@Nullable Collection<String> var1) {
         this.statesToApply = var1;
         return this;
      }

      public VehicleParticleConfiguration build() {
         return new VehicleParticleConfiguration(this.type, this.offset, this.delay, this.count, this.dispersion, this.data, this.statesToApply);
      }

      public String toString() {
         String var10000 = String.valueOf(this.type);
         return "VehicleParticleConfiguration.VehicleParticleConfigurationBuilder(type=" + var10000 + ", offset=" + String.valueOf(this.offset) + ", delay=" + this.delay + ", count=" + this.count + ", dispersion=" + this.dispersion + ", data=" + String.valueOf(this.data) + ", statesToApply=" + String.valueOf(this.statesToApply) + ")";
      }
   }
}
