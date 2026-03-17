package advancedplugins.pm2.cv.api.vehicle.configuration.model;

import advancedplugins.pm2.cv.api.enums.EnumVehicleModelType;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.util.reflection.EnumReflection;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.CompoundModelConfiguration;
import com.google.common.base.Preconditions;
import gnu.trove.set.hash.THashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class VehicleModelConfiguration implements IDeyed, ConfigurationSectionWritable {
   @NotNull
   protected final String id;
   @NotNull
   protected final VehicleHitBoxConfiguration hitBox;
   @NotNull
   protected final Set<VehicleSeatConfiguration> seats;
   @NotNull
   protected final Set<VehicleProjectileShooterConfiguration> projectileShooters;
   @NotNull
   protected final Set<VehicleParticleConfiguration> particles;
   @NotNull
   protected final Set<VehicleSoundConfiguration> sounds;
   @Nullable
   protected final String modelID;
   protected float modelOffset;

   public static VehicleModelConfiguration load(ConfigurationSection var0) {
      EnumVehicleModelType var1 = (EnumVehicleModelType)EnumReflection.getEnumConstant(EnumVehicleModelType.class, var0.getString("type", ""));
      if (var1 == null) {
         throw new InvalidConfigurationException("unknown model type");
      } else if (var1 == EnumVehicleModelType.COMPOUND) {
         return new CompoundModelConfiguration(var0);
      } else {
         throw new IllegalStateException(var1.name());
      }
   }

   protected VehicleModelConfiguration(@NotNull String var1, @NotNull VehicleHitBoxConfiguration var2, @NotNull Collection<VehicleSeatConfiguration> var3, @Nullable Collection<VehicleProjectileShooterConfiguration> var4, @Nullable Collection<VehicleParticleConfiguration> var5, @Nullable Collection<VehicleSoundConfiguration> var6) {
      Preconditions.checkArgument(var3.size() > 0, "at least one seat is required");
      this.id = IDeyed.idCheck(var1.toLowerCase());
      this.hitBox = var2;
      this.seats = new HashSet(var3);
      this.projectileShooters = new HashSet();
      this.particles = new THashSet();
      this.sounds = new THashSet();
      if (var5 != null) {
         this.particles.addAll(var5);
      }

      if (var6 != null) {
         this.sounds.addAll(var6);
      }

      if (var4 != null) {
         this.projectileShooters.addAll(var4.stream().filter(Objects::nonNull).toList());
      }

      this.modelID = null;
      this.modelOffset = 1.3F;
   }

   protected VehicleModelConfiguration(@NotNull ConfigurationSection var1) {
      this.id = IDeyed.loadId(var1);
      ConfigurationSection var2 = var1.getConfigurationSection("hitbox");
      if (var2 == null) {
         throw new InvalidConfigurationException("vehicles require a hitbox");
      } else {
         this.hitBox = VehicleHitBoxConfiguration.load(var2);
         this.seats = new HashSet();
         Iterator var3 = ConfigurationUtil.getConfigurationSectionsAfter(var1, "seats", false).iterator();

         ConfigurationSection var4;
         while(var3.hasNext()) {
            var4 = (ConfigurationSection)var3.next();
            this.seats.add(VehicleSeatConfiguration.load(var4));
         }

         if (this.seats.isEmpty()) {
            throw new InvalidConfigurationException("vehicles require at least one seat");
         } else {
            this.projectileShooters = new HashSet();
            var3 = ConfigurationUtil.getConfigurationSectionsAfter(var1, "projectile-shooters", false).iterator();

            while(var3.hasNext()) {
               var4 = (ConfigurationSection)var3.next();
               this.projectileShooters.add(VehicleProjectileShooterConfiguration.load(var4));
            }

            this.particles = new THashSet();
            var3 = ConfigurationUtil.getConfigurationSectionsAfter(var1, "particles", false).iterator();

            while(var3.hasNext()) {
               var4 = (ConfigurationSection)var3.next();
               this.particles.add(VehicleParticleConfiguration.load(var4));
            }

            this.sounds = new THashSet();
            var3 = ConfigurationUtil.getConfigurationSectionsAfter(var1, "sounds", false).iterator();

            while(var3.hasNext()) {
               var4 = (ConfigurationSection)var3.next();
               this.sounds.add(VehicleSoundConfiguration.load(var4));
            }

            this.modelID = var1.getString("model-id");
            this.modelOffset = Float.parseFloat(var1.getString("model-offset", "1.3"));
         }
      }
   }

   @NotNull
   public String getId() {
      return this.id;
   }

   @NotNull
   public abstract EnumVehicleModelType getType();

   @NotNull
   public Set<VehicleSeatConfiguration> getSeats() {
      return Collections.unmodifiableSet(this.seats);
   }

   @NotNull
   public Set<VehicleProjectileShooterConfiguration> getProjectileShooters() {
      return Collections.unmodifiableSet(this.projectileShooters);
   }

   @NotNull
   public Set<VehicleParticleConfiguration> getParticles() {
      return Collections.unmodifiableSet(this.particles);
   }

   @NotNull
   public Set<VehicleSoundConfiguration> getSounds() {
      return Collections.unmodifiableSet(this.sounds);
   }

   public void write(@NotNull ConfigurationSection var1) {
      IDeyed.writeId((IDeyed)this, var1);
      var1.set("type", this.getType().name());
      var1.set("model-id", this.getModelID());
      var1.set("model-offset", this.getModelOffset());
      this.hitBox.write(var1.createSection("hitbox"));
      ConfigurationUtil.writeConfigurationSectionWritables(var1.createSection("seats"), this.seats, "seat-");
      ConfigurationUtil.writeConfigurationSectionWritables(var1.createSection("particles"), this.particles, "particle-");
      ConfigurationUtil.writeConfigurationSectionWritables(var1.createSection("sounds"), this.sounds, "sound-");
      ConfigurationUtil.writeConfigurationSectionWritables(var1.createSection("projectile-shooters"), this.projectileShooters, "projectile-");
   }

   @Nullable
   public String getModelID() {
      return this.modelID;
   }

   @NotNull
   public VehicleHitBoxConfiguration getHitBox() {
      return this.hitBox;
   }

   public void setModelOffset(float var1) {
      this.modelOffset = var1;
   }

   public float getModelOffset() {
      return this.modelOffset;
   }
}
