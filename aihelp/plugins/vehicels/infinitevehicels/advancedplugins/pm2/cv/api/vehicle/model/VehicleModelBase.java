package advancedplugins.pm2.cv.api.vehicle.model;

import advancedplugins.pm2.cv.api.interfaces.Tickable;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleSeat;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleParticleConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleSoundConfiguration;
import gnu.trove.set.hash.THashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.geometry.euclidean.twod.Vector2D;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class VehicleModelBase<C extends VehicleModelConfiguration> implements VehicleModel<C> {
   @NotNull
   protected final Vehicle vehicle;
   @NotNull
   protected final C configuration;
   @NotNull
   protected World world;
   protected double x;
   protected double y;
   protected double z;
   protected float rotation;
   protected boolean spawned;
   @Nullable
   protected VehicleState state;
   protected final Set<VehicleModelBase.ParticleTicker> particleTickers = new THashSet();
   protected final Set<VehicleModelBase.SoundTicker> soundTickers = new THashSet();

   protected VehicleModelBase(@NotNull Vehicle var1, @NotNull C var2, @NotNull World var3, double var4, double var6, double var8) {
      this.vehicle = var1;
      this.configuration = var2;
      this.world = var3;
      this.x = var4;
      this.y = var6;
      this.z = var8;
      Iterator var10 = var2.getParticles().iterator();

      while(var10.hasNext()) {
         VehicleParticleConfiguration var11 = (VehicleParticleConfiguration)var10.next();
         if (var11.isValid()) {
            this.particleTickers.add(new VehicleModelBase.ParticleTicker(this, var11));
         }
      }

      var10 = var2.getSounds().iterator();

      while(var10.hasNext()) {
         VehicleSoundConfiguration var12 = (VehicleSoundConfiguration)var10.next();
         if (var12.isValid()) {
            this.soundTickers.add(new VehicleModelBase.SoundTicker(this, var12));
         }
      }

   }

   @NotNull
   public C getConfiguration() {
      return this.configuration;
   }

   public boolean isSpawned() {
      return this.spawned;
   }

   public void tick() {
      if (this.spawned) {
         this.particleTickers.forEach(VehicleModelBase.ParticleTicker::tick);
         this.soundTickers.forEach(VehicleModelBase.SoundTicker::tick);
      }
   }

   public void setState(@Nullable VehicleState var1) {
      if (!Objects.equals(this.state, var1)) {
         this.state = var1;
         this.onStateChanged();
      }

   }

   protected abstract void onStateChanged();

   @NotNull
   public Location getLocation() {
      return new Location(this.world, this.x, this.y, this.z, this.rotation, 0.0F);
   }

   @NotNull
   public World getWorld() {
      return this.world;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public float getRotation() {
      return this.rotation;
   }

   public void setLocationAndRotation(double var1, double var3, double var5, float var7) {
      this.setLocationAndRotation(var1, var3, var5, var7, false);
   }

   public void setLocationAndRotation(double var1, double var3, double var5, float var7, boolean var8) {
      var7 %= 360.0F;
      boolean var9 = Double.compare(var1, this.x) != 0 || Double.compare(var3, this.y) != 0 || Double.compare(var5, this.z) != 0;
      boolean var10 = Float.compare(this.rotation, var7) != 0;
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.rotation = var7;
      if (var9 && var10) {
         this.onLocationAndRotationChanged(var8);
      } else if (var9) {
         this.onLocationChanged(var8);
      } else {
         this.onRotationChanged();
      }

      if (var9 || var10) {
         this.relocateParticleTickers();
      }

   }

   public void setLocation(double var1, double var3, double var5) {
      this.setLocation(var1, var3, var5, false);
   }

   public void setLocation(double var1, double var3, double var5, boolean var7) {
      if (Double.compare(var1, this.x) != 0 || Double.compare(var3, this.y) != 0 || Double.compare(var5, this.z) != 0) {
         this.x = var1;
         this.y = var3;
         this.z = var5;
         this.onLocationChanged(var7);
         this.relocateParticleTickers();
      }

   }

   public void setRotation(float var1) {
      var1 %= 360.0F;
      if (Float.compare(this.rotation, var1) != 0) {
         this.rotation = var1;
         this.onRotationChanged();
         this.relocateParticleTickers();
      }

   }

   protected abstract void onLocationAndRotationChanged(boolean var1);

   protected abstract void onLocationChanged();

   protected abstract void onLocationChanged(boolean var1);

   protected abstract void onLocationChanged(boolean var1, boolean var2);

   protected abstract void onRotationChanged(boolean var1);

   protected abstract void onRotationChanged();

   protected void relocateParticleTickers() {
      this.particleTickers.forEach(VehicleModelBase.ParticleTicker::updateLocation);
   }

   protected static class ParticleTicker implements Tickable {
      @NotNull
      protected final VehicleModelBase<?> model;
      @NotNull
      protected final VehicleParticleConfiguration configuration;
      protected final int m;
      protected final float h;
      protected final float v;
      protected double x;
      protected double y;
      protected double z;
      protected int wait;
      protected boolean firstTick;

      public ParticleTicker(@NotNull VehicleModelBase<?> var1, @NotNull VehicleParticleConfiguration var2) {
         this.model = var1;
         this.configuration = var2;
         Vector3D var3 = var2.getOffset();
         this.v = (float)var3.getY();
         this.h = (float)FastMath.sqrt(var3.getX() * var3.getX() + var3.getZ() * var3.getZ());
         this.m = this.m(new Vector2D(var3.getX(), var3.getZ()), new Vector2D(0.0D, 1.0D));
      }

      public void tick() {
         if (this.firstTick) {
            this.firstTick = false;
            this.updateLocation();
         }

         if (this.wait > 0) {
            --this.wait;
         } else if (this.configuration.getStatesToApply().size() <= 0 || this.model.state != null && this.configuration.appliesTo(this.model.state)) {
            int var1 = this.configuration.getCount();
            int var2 = this.configuration.getDelay();
            float var3 = this.configuration.getDispersion();
            this.model.getWorld().spawnParticle(this.configuration.getType(), new Location(this.model.getWorld(), this.x, this.y, this.z), var1, (double)var3, (double)var3, (double)var3, 0.0D, this.configuration.getData());
            if (var2 > 0) {
               this.wait += var2;
            }

         }
      }

      protected void updateLocation() {
         int var1 = (int)FastMath.floor((double)this.model.getRotation()) + this.m + 90;
         this.y = this.model.y + (double)this.v;
         this.x = this.model.x + FastMath.cos(FastMath.toRadians((double)var1)) * (double)this.h;
         this.z = this.model.z + FastMath.sin(FastMath.toRadians((double)var1)) * (double)this.h;
      }

      protected int m(@NotNull Vector2D var1, @NotNull Vector2D var2) {
         double var3 = var1.getX() * var2.getY() - var1.getY() * var2.getX();
         double var5 = var1.getX() * var2.getX() + var1.getY() * var2.getY();
         return (int)FastMath.toDegrees(FastMath.atan2(var3, var5));
      }
   }

   protected static class SoundTicker implements Tickable {
      @NotNull
      protected final VehicleModelBase<?> model;
      @NotNull
      protected final VehicleSoundConfiguration configuration;
      protected int wait;

      public SoundTicker(@NotNull VehicleModelBase<?> var1, @NotNull VehicleSoundConfiguration var2) {
         this.model = var1;
         this.configuration = var2;
      }

      public void tick() {
         if (this.wait > 0) {
            --this.wait;
         } else if (this.configuration.isValid()) {
            if (this.configuration.getStatesToApply().size() <= 0 || this.model.state != null && this.configuration.appliesTo(this.model.state)) {
               Location var1 = this.model.getLocation();
               if (this.configuration.isGlobal()) {
                  Run.sync(() -> {
                     this.playGlobally(var1);
                  });
               } else {
                  this.playToPassengers(var1);
               }

            }
         }
      }

      protected void playGlobally(@NotNull Location var1) {
         World var2 = var1.getWorld();
         if (var2 != null) {
            SoundCategory var3 = this.configuration.getCategory();
            Sound var4 = this.configuration.getType();
            String var5 = this.configuration.getTypeCustom();
            float var6 = this.configuration.getVolume();
            float var7 = this.configuration.getPitch();
            if (var4 != null) {
               if (var3 != null) {
                  var2.playSound(var1, var4, var3, var6, var7);
               } else {
                  var2.playSound(var1, var4, var6, var7);
               }
            } else if (StringUtils.isNotBlank(var5)) {
               try {
                  if (var3 != null) {
                     var2.playSound(var1, var5, var3, var6, var7);
                  } else {
                     var2.playSound(var1, var5, var6, var7);
                  }
               } catch (Exception var9) {
               }
            }

         }
      }

      protected void playToPassengers(@NotNull Location var1) {
         SoundCategory var2 = this.configuration.getCategory();
         Sound var3 = this.configuration.getType();
         String var4 = this.configuration.getTypeCustom();
         float var5 = this.configuration.getVolume();
         float var6 = this.configuration.getPitch();
         Iterator var7 = this.model.vehicle.getSeats().iterator();

         while(var7.hasNext()) {
            VehicleSeat var8 = (VehicleSeat)var7.next();
            if (var8.getPassenger() instanceof Player) {
               Player var9 = (Player)var8.getPassenger();
               if (var3 != null) {
                  if (var2 != null) {
                     var9.playSound(var1, var3, var2, var5, var6);
                  } else {
                     var9.playSound(var1, var3, var5, var6);
                  }
               } else if (StringUtils.isNotBlank(var4)) {
                  if (var2 != null) {
                     var9.playSound(var1, var4, var2, var5, var6);
                  } else {
                     var9.playSound(var1, var4, var5, var6);
                  }
               }
            }
         }

      }
   }
}
