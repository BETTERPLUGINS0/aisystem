package advancedplugins.pm2.cv.api.vehicle.controller.predefined;

import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.enums.EnumSurface;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleFuelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleControllerProperties;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerSteerInput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FixedWingVehicleController extends VehicleController {
   protected static final Collection<EnumSurface> SURFACE_SCAN_IGNORE;
   protected double gravityCompensation;
   protected boolean canTakeOffFromLandOnly;
   protected double takeOffSpeed;
   protected double stallSpeed;
   protected double increaseHeightMinSpeed;
   protected double increaseHeightMaxSpeed;
   protected double decreaseHeightMinSpeed;
   protected double decreaseHeightMaxSpeed;
   protected double maxFlyingPositiveAcceleration;
   protected double maxTakingOffPositiveAcceleration;
   protected double maxNegativeAcceleration;
   protected double positiveAccelerationFlying;
   protected double positiveAccelerationOnSolid;
   protected double positiveAccelerationOnDusty;
   protected double positiveAccelerationOnSnowy;
   protected double positiveAccelerationOnSlippery;
   protected double positiveAccelerationOnWater;
   protected double positiveAccelerationOnLava;
   protected double negativeAccelerationFlying;
   protected double negativeAccelerationOnSolid;
   protected double negativeAccelerationOnDusty;
   protected double negativeAccelerationOnSnowy;
   protected double negativeAccelerationOnSlippery;
   protected double negativeAccelerationOnWater;
   protected double negativeAccelerationOnLava;
   protected double deceleration;
   protected int turningAngleMin;
   protected int turningAngleMax;
   protected float minFuelConsumption;
   protected float maxFuelConsumption;
   protected boolean forward;
   protected boolean backward;
   protected boolean increasingHeight;
   protected boolean decreasingHeight;
   protected boolean turningLeft;
   protected boolean turningRight;
   protected double currentSpeed;
   protected int currentTurningAngle;

   public FixedWingVehicleController(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
      super(var1, var2);
   }

   public void loadProperties() {
      this.gravityCompensation = this.vehicle.getConfiguration().physics().getGravityAcceleration();
      this.canTakeOffFromLandOnly = this.properties.getBooleanProperty("take-off-from-land-only", true);
      this.takeOffSpeed = this.properties.getDoubleProperty("take-off-speed", 0.3D);
      this.stallSpeed = this.properties.getDoubleProperty("stall-speed", 0.3D);
      this.increaseHeightMinSpeed = this.properties.getDoubleProperty("increase-height-min-speed", 0.13D);
      this.increaseHeightMaxSpeed = this.properties.getDoubleProperty("increase-height-max-speed", 0.13D);
      this.decreaseHeightMinSpeed = this.properties.getDoubleProperty("decrease-height-min-speed", 0.13D);
      this.decreaseHeightMaxSpeed = this.properties.getDoubleProperty("decrease-height-max-speed", 0.13D);
      this.maxFlyingPositiveAcceleration = this.properties.getDoubleProperty("max-flying-positive-acceleration", 0.6D);
      this.maxTakingOffPositiveAcceleration = this.properties.getDoubleProperty("max-land-positive-acceleration", 0.3D);
      this.maxNegativeAcceleration = this.properties.getDoubleProperty("max-negative-acceleration", 0.05D);
      this.positiveAccelerationFlying = this.properties.getDoubleProperty("positive-acceleration-flying", 0.015D);
      this.positiveAccelerationOnSolid = this.properties.getDoubleProperty("positive-acceleration-on-solid", 0.01D);
      this.positiveAccelerationOnDusty = this.properties.getDoubleProperty("positive-acceleration-on-dusty", 0.012D);
      this.positiveAccelerationOnSnowy = this.properties.getDoubleProperty("positive-acceleration-on-snowy", 0.012D);
      this.positiveAccelerationOnSlippery = this.properties.getDoubleProperty("positive-acceleration-on-slippery", 0.013D);
      this.positiveAccelerationOnWater = this.properties.getDoubleProperty("positive-acceleration-on-water", 0.0D);
      this.positiveAccelerationOnLava = this.properties.getDoubleProperty("positive-acceleration-on-lava", 0.0D);
      this.negativeAccelerationFlying = this.properties.getDoubleProperty("negative-acceleration-flying", 0.01D);
      this.negativeAccelerationOnSolid = this.properties.getDoubleProperty("negative-acceleration-on-solid", 0.01D);
      this.negativeAccelerationOnDusty = this.properties.getDoubleProperty("negative-acceleration-on-dusty", 0.009D);
      this.negativeAccelerationOnSnowy = this.properties.getDoubleProperty("negative-acceleration-on-snowy", 0.008D);
      this.negativeAccelerationOnSlippery = this.properties.getDoubleProperty("negative-acceleration-on-slippery", 0.008D);
      this.negativeAccelerationOnWater = this.properties.getDoubleProperty("negative-acceleration-on-water", 0.0D);
      this.negativeAccelerationOnLava = this.properties.getDoubleProperty("negative-acceleration-on-lava", 0.0D);
      this.deceleration = this.properties.getDoubleProperty("deceleration", 0.008D);
      this.turningAngleMin = this.properties.getIntegerProperty("turning-angle-min", 1);
      this.turningAngleMax = this.properties.getIntegerProperty("turning-angle-max", 3);
      VehicleFuelConfiguration var1 = this.vehicle.getConfiguration().fuel();
      this.minFuelConsumption = this.properties.getMinFuelConsumptionOverride(var1.getMinConsumption());
      this.maxFuelConsumption = this.properties.getMaxFuelConsumptionOverride(var1.getMaxConsumption());
   }

   public void tick() {
      super.tick();
      Map var1 = null;
      boolean var2 = false;
      if (this.currentSpeed > 0.0D) {
         this.vehicle.addMomentumY(this.gravityCompensation * (this.currentSpeed / this.maxFlyingPositiveAcceleration));
      }

      double var3;
      double var5;
      double var7;
      if ((this.increasingHeight || this.decreasingHeight) && this.currentSpeed >= this.takeOffSpeed && this.hasFuel()) {
         var3 = this.currentSpeed > 0.0D ? this.maxFlyingPositiveAcceleration : this.maxNegativeAcceleration;
         var5 = FastMath.abs(this.currentSpeed) / FastMath.abs(var3);
         var7 = 0.0D;
         if (this.increasingHeight && this.currentSpeed >= this.stallSpeed) {
            var7 = this.increaseHeightMinSpeed + (this.increaseHeightMaxSpeed - this.increaseHeightMinSpeed) * var5;
         } else if (this.decreasingHeight) {
            double var9 = this.decreaseHeightMinSpeed + (this.decreaseHeightMaxSpeed - this.decreaseHeightMinSpeed) * var5;
            var7 = -var9;
         }

         if (var7 != 0.0D && this.vehicle.containsAnyWithin(EnumSurface.WATER, EnumSurface.LAVA, EnumSurface.SOLID)) {
            var7 = 0.0D;
         }

         this.vehicle.addMomentumY(var7);
         var2 = true;
      }

      var3 = this.currentSpeed;
      if ((this.forward || this.backward) && this.hasFuel()) {
         var1 = this.vehicle.getCurrentSurface(SURFACE_SCAN_IGNORE);
         var5 = this.calculateAcceleration(var1, this.forward);
         if (var5 != 0.0D && this.vehicle.containsAnyWithin(EnumSurface.WATER, EnumSurface.LAVA, EnumSurface.SOLID)) {
            var5 = 0.0D;
         }

         if (this.forward && var5 != 0.0D) {
            var7 = this.isInTheAir(var1) ? this.maxFlyingPositiveAcceleration : this.maxTakingOffPositiveAcceleration;
            this.currentSpeed = Math.min(this.currentSpeed + var5, var7);
         } else if (this.backward && var5 != 0.0D && !this.isInTheAir(var1)) {
            this.currentSpeed = Math.max(this.currentSpeed - var5, -this.maxNegativeAcceleration);
         }
      }

      if (var3 < this.takeOffSpeed && this.currentSpeed >= this.takeOffSpeed && this.canTakeOffFrom(var1 != null ? var1 : (var1 = this.vehicle.getCurrentSurface(SURFACE_SCAN_IGNORE)))) {
         this.vehicle.addMomentumY(this.increaseHeightMinSpeed);
      }

      this.tickTurning();
      if (this.currentSpeed != 0.0D) {
         if (var1 == null) {
            var1 = this.vehicle.getCurrentSurface(SURFACE_SCAN_IGNORE);
         }

         if (this.isInTheAir(var1) || this.canTakeOffFrom(var1)) {
            float var11 = this.vehicle.getRotation();
            if (this.currentTurningAngle != 0) {
               if (this.currentSpeed > 0.0D) {
                  if (this.turningLeft) {
                     var11 -= (float)this.currentTurningAngle;
                  } else {
                     var11 += (float)this.currentTurningAngle;
                  }
               } else if (this.turningLeft) {
                  var11 += (float)this.currentTurningAngle;
               } else {
                  var11 -= (float)this.currentTurningAngle;
               }

               this.vehicle.setRotation(var11);
            }

            double var6 = -FastMath.sin(FastMath.toRadians((double)var11));
            double var8 = FastMath.cos(FastMath.toRadians((double)var11));
            this.vehicle.addMomentumX(this.currentSpeed * var6);
            this.vehicle.addMomentumZ(this.currentSpeed * var8);
            var2 = true;
         }
      }

      if (var2) {
         this.tickFuelConsumption();
      }

      this.tickDeceleration();
      this.tickState();
   }

   private boolean hasFuel() {
      if (this.maxFuelConsumption <= 0.0F) {
         return true;
      } else {
         float var1 = this.vehicle.getFuelLevel();
         boolean var2 = !Configuration.FUEL_ENABLE.booleanValue();
         boolean var3 = Configuration.FUEL_BYPASS_CREATIVE.booleanValue();
         Entity var4 = this.vehicle.getOperator();
         return var2 || var1 > 0.0F || var3 && var4 instanceof Player && ((Player)var4).getGameMode() == GameMode.CREATIVE;
      }
   }

   private void tickFuelConsumption() {
      if (this.currentSpeed != 0.0D && !(this.minFuelConsumption > this.maxFuelConsumption) && !((double)this.maxFuelConsumption <= 0.0D)) {
         double var1 = this.currentSpeed > 0.0D ? this.maxFlyingPositiveAcceleration : this.maxNegativeAcceleration;
         double var3 = FastMath.abs(this.currentSpeed) / FastMath.abs(var1);
         float var5 = this.minFuelConsumption + (float)FastMath.round((double)(this.maxFuelConsumption - this.minFuelConsumption) * var3);
         if (var5 > 0.0F) {
            this.vehicle.consumeFuel(var5);
         }

      }
   }

   private void tickTurning() {
      if (!this.turningLeft && !this.turningRight) {
         this.currentTurningAngle = 0;
      } else {
         double var1 = this.currentSpeed > 0.0D ? this.maxFlyingPositiveAcceleration : this.maxNegativeAcceleration;
         double var3 = FastMath.abs(this.currentSpeed) / FastMath.abs(var1);
         int var5 = this.turningAngleMin + (int)FastMath.round((double)(this.turningAngleMax - this.turningAngleMin) * var3);
         this.currentTurningAngle = Math.max(var5, 0);
      }

   }

   private void tickDeceleration() {
      if (this.currentSpeed > 0.0D) {
         this.currentSpeed = Math.max(0.0D, this.currentSpeed - this.deceleration);
      } else if (this.currentSpeed < 0.0D) {
         this.currentSpeed = Math.min(0.0D, this.currentSpeed + this.deceleration);
      }

   }

   private void tickState() {
      if (this.vehicle.getMomentumX() == 0.0D && this.vehicle.getMomentumZ() == 0.0D) {
         if (this.turningLeft) {
            this.vehicle.setState(VehicleState.TURNING_LEFT);
         } else if (this.turningRight) {
            this.vehicle.setState(VehicleState.TURNING_RIGHT);
         } else {
            this.vehicle.setState(VehicleState.IDLE);
         }
      } else {
         boolean var1 = this.currentSpeed < 0.0D;
         VehicleState var2 = var1 ? VehicleState.MOVING_BACKWARDS : VehicleState.MOVING;
         if (this.turningLeft) {
            var2 = var1 ? VehicleState.MOVING_BACKWARDS_TURNING_LEFT : VehicleState.MOVING_TURNING_LEFT;
         } else if (this.turningRight) {
            var2 = var1 ? VehicleState.MOVING_BACKWARDS_TURNING_RIGHT : VehicleState.MOVING_TURNING_RIGHT;
         }

         this.vehicle.setState(var2);
      }

   }

   protected boolean canTakeOffFrom(@NotNull Map<EnumSurface, Double> var1) {
      return var1.containsKey(EnumSurface.SOLID) || var1.containsKey(EnumSurface.DUSTY) || var1.containsKey(EnumSurface.SNOWY) || var1.containsKey(EnumSurface.SLIPPERY) || var1.containsKey(EnumSurface.WATER) && !this.canTakeOffFromLandOnly || var1.containsKey(EnumSurface.LAVA) && !this.canTakeOffFromLandOnly;
   }

   protected boolean isInTheAir(@NotNull Map<EnumSurface, Double> var1) {
      return var1.size() == 0 || var1.size() == 1 && var1.containsKey(EnumSurface.EMPTY);
   }

   protected double calculateAcceleration(@NotNull Map<EnumSurface, Double> var1, boolean var2) {
      double var3 = 0.0D;
      if (this.isInTheAir(var1)) {
         return var2 ? this.positiveAccelerationFlying : this.negativeAccelerationFlying;
      } else {
         Entry var6;
         for(Iterator var5 = var1.entrySet().iterator(); var5.hasNext(); var3 += this.getAccelerationOn((EnumSurface)var6.getKey(), var2) * (Double)var6.getValue()) {
            var6 = (Entry)var5.next();
         }

         return var3;
      }
   }

   protected double getAccelerationOn(@NotNull EnumSurface var1, boolean var2) {
      switch(var1) {
      case SOLID:
         return var2 ? this.positiveAccelerationOnSolid : this.negativeAccelerationOnSolid;
      case DUSTY:
         return var2 ? this.positiveAccelerationOnDusty : this.negativeAccelerationOnDusty;
      case SNOWY:
         return var2 ? this.positiveAccelerationOnSnowy : this.negativeAccelerationOnSnowy;
      case SLIPPERY:
         return var2 ? this.positiveAccelerationOnSlippery : this.negativeAccelerationOnSlippery;
      case WATER:
         if (!this.canTakeOffFromLandOnly) {
            return var2 ? this.positiveAccelerationOnWater : this.negativeAccelerationOnWater;
         }
         break;
      case LAVA:
         if (!this.canTakeOffFromLandOnly) {
            return var2 ? this.positiveAccelerationOnLava : this.negativeAccelerationOnLava;
         }
      }

      return 0.0D;
   }

   public void process(@NotNull PlayerSteerInput var1) {
      this.forward = var1.forward > 0;
      this.backward = var1.forward < 0;
      this.increasingHeight = var1.jump;
      this.decreasingHeight = var1.unmount;
      this.turningLeft = var1.sideways > 0;
      this.turningRight = var1.sideways < 0;
   }

   public void standby() {
      this.forward = false;
      this.backward = false;
      this.increasingHeight = false;
      this.decreasingHeight = false;
      this.turningLeft = false;
      this.turningRight = false;
   }

   static {
      SURFACE_SCAN_IGNORE = Arrays.asList(EnumSurface.EMPTY, EnumSurface.UNKNOWN);
   }
}
