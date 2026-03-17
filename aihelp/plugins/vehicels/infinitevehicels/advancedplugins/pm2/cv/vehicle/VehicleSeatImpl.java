package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.event.VehicleOperatorSetEvent;
import advancedplugins.pm2.cv.api.event.VehiclePassengerSetEvent;
import advancedplugins.pm2.cv.api.interfaces.Tickable;
import advancedplugins.pm2.cv.api.service.EntityService;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleSeat;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleSeatConfiguration;
import advancedplugins.pm2.cv.locator.ArmorStandLocatorHandler;
import advancedplugins.pm2.cv.util.Constants;
import advancedplugins.pm2.cv.util.math.CachedMathUtil;
import advancedplugins.pm2.cv.util.math.TrigonometryUtil;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.geometry.euclidean.twod.Vector2D;
import me.PM2.infinitevehicles.math.util.FastMath;
import me.PM2.infinitevehicles.xseries.XAttribute;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VehicleSeatImpl implements VehicleSeat, Tickable {
   @NotNull
   private final ArmorStandLocatorHandler locationSetterHandler = (ArmorStandLocatorHandler)Objects.requireNonNull((ArmorStandLocatorHandler)InfiniteVehicles.getHandler(ArmorStandLocatorHandler.class));
   @NotNull
   private final VehicleImpl vehicle;
   @NotNull
   private final VehicleSeatConfiguration configuration;
   private boolean main;
   private final int angle;
   private final float hOffset;
   private final float vOffset;
   private ArmorStand bone;
   private double boneMaxHealth;
   private double lastHealthShown;
   @Nullable
   private Entity passenger;
   private boolean passengerWasAllowedFlying;
   private boolean passengerWasFlying;
   private boolean spawned;
   private boolean destroyed;
   private double x;
   private double y;
   private double z;
   private int rotation;

   public VehicleSeatImpl(@NotNull VehicleImpl vehicle, @NotNull VehicleSeatConfiguration configuration) {
      this.vehicle = var1;
      this.configuration = var2;
      Vector3D var3 = var2.getOffset();
      Vector2D var4 = new Vector2D(var3.getX(), var3.getZ());
      this.angle = TrigonometryUtil.calculateAngleBetween(var4, Constants.TOWARD_Z_2D);
      this.hOffset = (float)FastMath.sqrt(var3.getX() * var3.getX() + var3.getZ() * var3.getZ());
      this.vOffset = (float)var3.getY();
   }

   @NotNull
   public VehicleSeatConfiguration getConfiguration() {
      return this.configuration;
   }

   @NotNull
   public Vehicle getVehicle() {
      return this.vehicle;
   }

   public boolean isMain() {
      return this.main;
   }

   public boolean isSpawned() {
      return this.spawned;
   }

   public boolean isDestroyed() {
      return this.destroyed;
   }

   public void spawn() {
      if (!this.spawned) {
         this.recalculateLocation();
         this.spawned = true;
      }
   }

   public void destroy() {
      if (!this.destroyed) {
         this.destroyed = true;
         Run.sync(() -> {
            if (this.getPassenger() != null) {
               this.setPassenger((Entity)null);
            }

            if (this.bone != null) {
               this.bone.remove();
            }

         });
      }
   }

   public void tick() {
      if (this.bone != null && this.boneMaxHealth > 0.0D && Configuration.HEALTH_SHOW_ON_MOUNT_HEALTH_BAR.booleanValue()) {
         double var1 = (double)(this.vehicle.getHealth() / this.vehicle.getMaxHealth()) * this.boneMaxHealth;
         if (Double.compare(var1, this.lastHealthShown) != 0) {
            this.lastHealthShown = var1;
            Run.sync(() -> {
               if (this.bone != null) {
                  this.bone.setHealth(var1);
               }
            });
         }
      }

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
      return (float)this.rotation;
   }

   public void moveToWorld(@NotNull final World world) {
      if (this.bone != null) {
         this.locationSetterHandler.stopHandling(this.bone);
         ArmorStand var10000 = this.bone;
         Objects.requireNonNull(var10000);
         Run.sync(var10000::remove);
      }

      this.recalculateLocation();
      Run.sync(() -> {
         this.renewBone(var1);
      });
   }

   public void onVehicleMove() {
      if (this.spawned && this.bone != null) {
         double var1 = this.x;
         double var3 = this.y;
         double var5 = this.z;
         int var7 = this.rotation;
         this.recalculateLocation();
         if (Double.compare(var1, this.x) != 0 || Double.compare(var3, this.y) != 0 || Double.compare(var5, this.z) != 0 || Double.compare((double)var7, (double)this.rotation) != 0) {
            this.vehicle.getPacketBatcher().track(this.locationSetterHandler.getLocator().writeLocation(this.bone, this.x, this.y, this.z, (float)this.rotation, 0.0F, 10));
         }
      }
   }

   @Nullable
   public Entity getPassenger() {
      return this.passenger;
   }

   public void setPassenger(@Nullable Entity passenger) {
      Preconditions.checkArgument(Bukkit.isPrimaryThread(), "must run from server thread");
      Entity var2 = this.passenger;
      this.passenger = var1;
      if (var2 != null) {
         this.preparePassenger(var2, false);
         if (var2 instanceof Player && this.main) {
            this.vehicle.standBy();
         }
      }

      if (var1 != null) {
         Iterator var3 = this.vehicle.seats.iterator();

         while(var3.hasNext()) {
            VehicleSeatImpl var4 = (VehicleSeatImpl)var3.next();
            if (var4 != this && Objects.equals(var4.passenger, var1)) {
               var4.setPassenger((Entity)null);
            }
         }
      }

      if (this.bone == null && this.spawned && var1 != null) {
         this.renewBone(this.vehicle.getWorld());
      } else if (this.bone != null) {
         this.bone.eject();
         if (var1 != null) {
            this.preparePassenger(var1, true);
            ((EntityService)InfiniteVehicles.getVitalService(EntityService.class)).addPassenger(this.bone, var1);
         }
      }

      if (var1 == null && this.bone != null) {
         this.bone.eject();
         this.bone.remove();
         this.bone = null;
      }

      if (this.getVehicle().getConfiguration().getPlacement().isAddOnExit() && var1 == null && var2 != null && this.getVehicle().hasOwner() && var2.getUniqueId().equals(var2.getUniqueId()) && this.getVehicle().getConfiguration().hasPickupItem()) {
         Run.syncDelayed(() -> {
            this.getVehicle().remove();
         }, 1L);
      }

      (new VehiclePassengerSetEvent(this.vehicle, this, var2, var1)).callEventSynchronously((Consumer)null);
      if (this.main) {
         (new VehicleOperatorSetEvent(this.vehicle, var2, var1)).callEventSynchronously((Consumer)null);
      }

   }

   private void preparePassenger(@NotNull Entity passenger, boolean isPassenger) {
      if (var1 instanceof Player) {
         Player var3 = (Player)var1;
         if (var2) {
            this.passengerWasAllowedFlying = var3.getAllowFlight();
            this.passengerWasFlying = var3.isFlying();
         }
      } else if (!var2) {
         this.passengerWasFlying = false;
         this.passengerWasAllowedFlying = false;
      }

   }

   private void recalculateLocation() {
      this.rotation = TrigonometryUtil.toIntAngle(this.vehicle.getRotation());
      int var1 = this.rotation + this.angle + 90;
      this.y = this.vehicle.getY() + (double)this.vOffset;
      this.x = this.vehicle.getX() + (double)(CachedMathUtil.cos(var1) * this.hOffset);
      this.z = this.vehicle.getZ() + (double)(CachedMathUtil.sin(var1) * this.hOffset);
   }

   private void renewBone(World world) {
      this.recalculateLocation();

      try {
         this.bone = (ArmorStand)var1.spawn(new Location(var1, this.x, this.y, this.z, (float)this.rotation, 0.0F), ArmorStand.class, (var0) -> {
            var0.setVisible(false);
         });
      } catch (NoSuchMethodError var3) {
         this.bone = (ArmorStand)var1.spawnEntity(new Location(var1, this.x, this.y, this.z, (float)this.rotation, 0.0F), EntityType.ARMOR_STAND);
         this.bone.setVisible(false);
      }

      this.boneMaxHealth = ((AttributeInstance)Objects.requireNonNull(this.bone.getAttribute((Attribute)XAttribute.MAX_HEALTH.get()))).getValue();
      this.bone.setVisible(false);
      this.bone.setGravity(false);
      this.bone.setMarker(true);
      this.bone.setBasePlate(false);
      this.bone.setSilent(true);
      this.bone.setSmall(true);
      this.bone.setPersistent(false);
      this.bone.setAI(false);
      this.bone.setRemoveWhenFarAway(false);
      this.bone.setGlowing(true);
      this.locationSetterHandler.handle(this.bone);
      if (this.passenger != null) {
         if (Objects.equals(this.passenger.getWorld(), var1)) {
            this.preparePassenger(this.passenger, true);
            ((EntityService)InfiniteVehicles.getVitalService(EntityService.class)).addPassenger(this.bone, this.passenger);
         } else {
            this.setPassenger((Entity)null);
         }
      }

   }

   @NotNull
   public ArmorStandLocatorHandler getLocationSetterHandler() {
      return this.locationSetterHandler;
   }

   void setMain(final boolean main) {
      this.main = var1;
   }

   public ArmorStand getBone() {
      return this.bone;
   }
}
