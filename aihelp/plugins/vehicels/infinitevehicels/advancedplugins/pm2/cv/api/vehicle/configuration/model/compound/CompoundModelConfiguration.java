package advancedplugins.pm2.cv.api.vehicle.configuration.model.compound;

import advancedplugins.pm2.cv.api.enums.EnumVehicleModelType;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleHitBoxConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleParticleConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleProjectileShooterConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleSeatConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleSoundConfiguration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompoundModelConfiguration extends VehicleModelConfiguration {
   @NotNull
   private final Set<PartConfiguration> parts;
   @NotNull
   private final Set<BoneConfiguration> bones;
   @Nullable
   private RigConfiguration rig;
   @NotNull
   protected final Set<AnimationConfiguration> animations;
   @Nullable
   private String modelID;
   protected final int modelInterpolation;

   public CompoundModelConfiguration(@NotNull String var1, @NotNull VehicleHitBoxConfiguration var2, @NotNull Collection<VehicleSeatConfiguration> var3, @Nullable Collection<VehicleProjectileShooterConfiguration> var4, @NotNull Collection<PartConfiguration> var5, @Nullable Collection<BoneConfiguration> var6, @Nullable RigConfiguration var7, @Nullable Collection<AnimationConfiguration> var8, @Nullable Collection<VehicleParticleConfiguration> var9, @Nullable Collection<VehicleSoundConfiguration> var10, int var11) {
      super(var1, var2, var3, var4, var9, var10);
      this.parts = ConcurrentHashMap.newKeySet();
      this.parts.addAll(var5);
      this.bones = ConcurrentHashMap.newKeySet();
      this.animations = ConcurrentHashMap.newKeySet();
      if (var6 != null) {
         this.bones.addAll(var6);
      }

      if (var8 != null) {
         this.animations.addAll(var8);
      }

      this.rig = var7;
      this.modelID = null;
      this.modelInterpolation = var11;
   }

   public CompoundModelConfiguration(@NotNull ConfigurationSection var1) {
      super(var1);
      this.modelID = var1.getString("model-id");
      this.parts = ConcurrentHashMap.newKeySet();
      Iterator var2 = ConfigurationUtil.getConfigurationSectionsAfter(var1, "parts", false).iterator();

      while(true) {
         ConfigurationSection var3;
         while(var2.hasNext()) {
            var3 = (ConfigurationSection)var2.next();
            if (var3 != null && !var3.getKeys(false).isEmpty() && IDeyed.loadId(var3, (String)null) == null) {
               Iterator var4 = var3.getKeys(false).iterator();

               while(var4.hasNext()) {
                  String var5 = (String)var4.next();
                  if (var3.isConfigurationSection(var5)) {
                     ConfigurationSection var6 = var3.getConfigurationSection(var5);
                     if (var6 != null) {
                        this.parts.add(PartConfiguration.load(var6));
                     }
                  }
               }
            } else {
               try {
                  this.parts.add(PartConfiguration.load(var3));
               } catch (InvalidConfigurationException var9) {
                  var9.printStackTrace();
               }
            }
         }

         if (this.parts.size() == 0 && this.modelID == null) {
            throw new InvalidConfigurationException("at least one valid part is required");
         }

         this.bones = ConcurrentHashMap.newKeySet();
         var2 = ConfigurationUtil.getConfigurationSectionsAfter(var1, "bones", false).iterator();

         while(var2.hasNext()) {
            var3 = (ConfigurationSection)var2.next();

            try {
               this.bones.add(BoneConfiguration.load(var3));
            } catch (InvalidConfigurationException var8) {
               var8.printStackTrace();
            }
         }

         ConfigurationSection var10 = var1.getConfigurationSection("rig");
         this.rig = var10 != null ? RigConfiguration.load(var10, this) : null;
         this.animations = ConcurrentHashMap.newKeySet();
         Iterator var11 = ConfigurationUtil.getConfigurationSectionsAfter(var1, "animations", false).iterator();

         while(var11.hasNext()) {
            ConfigurationSection var12 = (ConfigurationSection)var11.next();

            try {
               this.animations.add(AnimationConfiguration.load(var12));
            } catch (InvalidConfigurationException var7) {
               var7.printStackTrace();
            }
         }

         this.modelInterpolation = var1.getInt("model-interpolation");
         return;
      }
   }

   @NotNull
   public EnumVehicleModelType getType() {
      return EnumVehicleModelType.COMPOUND;
   }

   @NotNull
   public Set<PartConfiguration> getParts() {
      return this.parts;
   }

   @Nullable
   public PartConfiguration getPartByIdentifier(@NotNull UUID var1) {
      Iterator var2 = this.parts.iterator();

      PartConfiguration var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (PartConfiguration)var2.next();
      } while(!Objects.equals(var3.getIdentifier(), var1));

      return var3;
   }

   @NotNull
   public Set<BoneConfiguration> getBones() {
      return this.bones;
   }

   @Nullable
   public BoneConfiguration getBoneByIdentifier(@NotNull UUID var1) {
      Iterator var2 = this.bones.iterator();

      BoneConfiguration var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (BoneConfiguration)var2.next();
      } while(!Objects.equals(var3.getIdentifier(), var1));

      return var3;
   }

   @Nullable
   public RigConfiguration getRig() {
      return this.rig;
   }

   @NotNull
   public Set<AnimationConfiguration> getAnimations() {
      return this.animations;
   }

   @Nullable
   public AnimationConfiguration getAnimationByState(@NotNull VehicleState var1) {
      Iterator var2 = this.animations.iterator();

      AnimationConfiguration var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (AnimationConfiguration)var2.next();
      } while(!var3.appliesTo(var1));

      return var3;
   }

   @Nullable
   public AnimationConfiguration getAnimationAllStates() {
      Iterator var1 = this.animations.iterator();

      AnimationConfiguration var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (AnimationConfiguration)var1.next();
      } while(var2.getStatesToApply().size() != 0);

      return var2;
   }

   @Nullable
   public String getModelID() {
      return this.modelID;
   }

   public void setModelID(@Nullable String var1) {
      this.modelID = var1;
   }

   public void write(@NotNull ConfigurationSection var1) {
      super.write(var1);
      boolean var2 = this.modelID != null;
      if (!var2) {
         ConfigurationUtil.writeConfigurationSectionWritables(var1.createSection("parts"), this.parts, "part-");
         ConfigurationUtil.writeConfigurationSectionWritables(var1.createSection("bones"), this.bones, "bone-");
      }

      if (this.rig != null && !var2) {
         this.rig.write(var1.createSection("rig"));
      }

      if (!var2) {
         ConfigurationUtil.writeConfigurationSectionWritables(var1.createSection("animations"), this.animations, "animation-");
      }

   }

   public static CompoundModelConfiguration.CompoundModelConfigurationBuilder builder() {
      return new CompoundModelConfiguration.CompoundModelConfigurationBuilder();
   }

   public int getModelInterpolation() {
      return this.modelInterpolation;
   }

   public void setRig(@Nullable RigConfiguration var1) {
      this.rig = var1;
   }

   public static class CompoundModelConfigurationBuilder {
      private String id;
      private VehicleHitBoxConfiguration hitBox;
      private ArrayList<VehicleSeatConfiguration> seats;
      private Collection<VehicleProjectileShooterConfiguration> projectileShooters;
      private ArrayList<PartConfiguration> parts;
      private ArrayList<BoneConfiguration> bones;
      private RigConfiguration rig;
      private ArrayList<AnimationConfiguration> animations;
      private ArrayList<VehicleParticleConfiguration> particles;
      private ArrayList<VehicleSoundConfiguration> sounds;
      private int modelInterpolation;

      CompoundModelConfigurationBuilder() {
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder id(@NotNull String var1) {
         this.id = var1;
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder hitBox(@NotNull VehicleHitBoxConfiguration var1) {
         this.hitBox = var1;
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder seat(VehicleSeatConfiguration var1) {
         if (this.seats == null) {
            this.seats = new ArrayList();
         }

         this.seats.add(var1);
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder seats(Collection<? extends VehicleSeatConfiguration> var1) {
         if (var1 == null) {
            throw new NullPointerException("seats cannot be null");
         } else {
            if (this.seats == null) {
               this.seats = new ArrayList();
            }

            this.seats.addAll(var1);
            return this;
         }
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder clearSeats() {
         if (this.seats != null) {
            this.seats.clear();
         }

         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder projectileShooters(@Nullable Collection<VehicleProjectileShooterConfiguration> var1) {
         this.projectileShooters = var1;
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder part(PartConfiguration var1) {
         if (this.parts == null) {
            this.parts = new ArrayList();
         }

         this.parts.add(var1);
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder parts(Collection<? extends PartConfiguration> var1) {
         if (var1 == null) {
            throw new NullPointerException("parts cannot be null");
         } else {
            if (this.parts == null) {
               this.parts = new ArrayList();
            }

            this.parts.addAll(var1);
            return this;
         }
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder clearParts() {
         if (this.parts != null) {
            this.parts.clear();
         }

         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder bone(BoneConfiguration var1) {
         if (this.bones == null) {
            this.bones = new ArrayList();
         }

         this.bones.add(var1);
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder bones(Collection<? extends BoneConfiguration> var1) {
         if (var1 == null) {
            throw new NullPointerException("bones cannot be null");
         } else {
            if (this.bones == null) {
               this.bones = new ArrayList();
            }

            this.bones.addAll(var1);
            return this;
         }
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder clearBones() {
         if (this.bones != null) {
            this.bones.clear();
         }

         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder rig(@Nullable RigConfiguration var1) {
         this.rig = var1;
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder animation(AnimationConfiguration var1) {
         if (this.animations == null) {
            this.animations = new ArrayList();
         }

         this.animations.add(var1);
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder animations(Collection<? extends AnimationConfiguration> var1) {
         if (var1 == null) {
            throw new NullPointerException("animations cannot be null");
         } else {
            if (this.animations == null) {
               this.animations = new ArrayList();
            }

            this.animations.addAll(var1);
            return this;
         }
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder clearAnimations() {
         if (this.animations != null) {
            this.animations.clear();
         }

         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder particle(VehicleParticleConfiguration var1) {
         if (this.particles == null) {
            this.particles = new ArrayList();
         }

         this.particles.add(var1);
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder particles(Collection<? extends VehicleParticleConfiguration> var1) {
         if (var1 == null) {
            throw new NullPointerException("particles cannot be null");
         } else {
            if (this.particles == null) {
               this.particles = new ArrayList();
            }

            this.particles.addAll(var1);
            return this;
         }
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder clearParticles() {
         if (this.particles != null) {
            this.particles.clear();
         }

         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder sound(VehicleSoundConfiguration var1) {
         if (this.sounds == null) {
            this.sounds = new ArrayList();
         }

         this.sounds.add(var1);
         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder sounds(Collection<? extends VehicleSoundConfiguration> var1) {
         if (var1 == null) {
            throw new NullPointerException("sounds cannot be null");
         } else {
            if (this.sounds == null) {
               this.sounds = new ArrayList();
            }

            this.sounds.addAll(var1);
            return this;
         }
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder clearSounds() {
         if (this.sounds != null) {
            this.sounds.clear();
         }

         return this;
      }

      public CompoundModelConfiguration.CompoundModelConfigurationBuilder modelInterpolation(int var1) {
         this.modelInterpolation = var1;
         return this;
      }

      public CompoundModelConfiguration build() {
         List var1;
         switch(this.seats == null ? 0 : this.seats.size()) {
         case 0:
            var1 = Collections.emptyList();
            break;
         case 1:
            var1 = Collections.singletonList((VehicleSeatConfiguration)this.seats.get(0));
            break;
         default:
            var1 = Collections.unmodifiableList(new ArrayList(this.seats));
         }

         List var2;
         switch(this.parts == null ? 0 : this.parts.size()) {
         case 0:
            var2 = Collections.emptyList();
            break;
         case 1:
            var2 = Collections.singletonList((PartConfiguration)this.parts.get(0));
            break;
         default:
            var2 = Collections.unmodifiableList(new ArrayList(this.parts));
         }

         List var3;
         switch(this.bones == null ? 0 : this.bones.size()) {
         case 0:
            var3 = Collections.emptyList();
            break;
         case 1:
            var3 = Collections.singletonList((BoneConfiguration)this.bones.get(0));
            break;
         default:
            var3 = Collections.unmodifiableList(new ArrayList(this.bones));
         }

         List var4;
         switch(this.animations == null ? 0 : this.animations.size()) {
         case 0:
            var4 = Collections.emptyList();
            break;
         case 1:
            var4 = Collections.singletonList((AnimationConfiguration)this.animations.get(0));
            break;
         default:
            var4 = Collections.unmodifiableList(new ArrayList(this.animations));
         }

         List var5;
         switch(this.particles == null ? 0 : this.particles.size()) {
         case 0:
            var5 = Collections.emptyList();
            break;
         case 1:
            var5 = Collections.singletonList((VehicleParticleConfiguration)this.particles.get(0));
            break;
         default:
            var5 = Collections.unmodifiableList(new ArrayList(this.particles));
         }

         List var6;
         switch(this.sounds == null ? 0 : this.sounds.size()) {
         case 0:
            var6 = Collections.emptyList();
            break;
         case 1:
            var6 = Collections.singletonList((VehicleSoundConfiguration)this.sounds.get(0));
            break;
         default:
            var6 = Collections.unmodifiableList(new ArrayList(this.sounds));
         }

         return new CompoundModelConfiguration(this.id, this.hitBox, var1, this.projectileShooters, var2, var3, this.rig, var4, var5, var6, this.modelInterpolation);
      }

      public String toString() {
         String var10000 = this.id;
         return "CompoundModelConfiguration.CompoundModelConfigurationBuilder(id=" + var10000 + ", hitBox=" + String.valueOf(this.hitBox) + ", seats=" + String.valueOf(this.seats) + ", projectileShooters=" + String.valueOf(this.projectileShooters) + ", parts=" + String.valueOf(this.parts) + ", bones=" + String.valueOf(this.bones) + ", rig=" + String.valueOf(this.rig) + ", animations=" + String.valueOf(this.animations) + ", particles=" + String.valueOf(this.particles) + ", sounds=" + String.valueOf(this.sounds) + ", modelInterpolation=" + this.modelInterpolation + ")";
      }
   }
}
