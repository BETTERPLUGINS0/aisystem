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

public class GroundVehicleController extends VehicleController {
   protected static final Collection<EnumSurface> SURFACE_SCAN_IGNORE;
   protected double maxPositiveAcceleration;
   protected double maxNegativeAcceleration;
   protected double positiveAccelerationOnSolid;
   protected double positiveAccelerationOnDusty;
   protected double positiveAccelerationOnSnowy;
   protected double positiveAccelerationOnSlippery;
   protected double negativeAccelerationOnSolid;
   protected double negativeAccelerationOnDusty;
   protected double negativeAccelerationOnSnowy;
   protected double negativeAccelerationOnSlippery;
   protected double deceleration;
   protected int turningAngleMin;
   protected int turningAngleMax;
   protected float minFuelConsumption;
   protected float maxFuelConsumption;
   protected boolean forward;
   protected boolean backward;
   protected boolean turningLeft;
   protected boolean turningRight;
   protected double currentSpeed;
   protected int currentTurningAngle;

   public GroundVehicleController(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
      super(var1, var2);
   }

   public void loadProperties() {
      this.maxPositiveAcceleration = this.properties.getDoubleProperty("max-positive-acceleration", 0.45D);
      this.maxNegativeAcceleration = this.properties.getDoubleProperty("max-negative-acceleration", 0.2D);
      this.positiveAccelerationOnSolid = this.properties.getDoubleProperty("positive-acceleration-on-solid", 0.025D);
      this.positiveAccelerationOnDusty = this.properties.getDoubleProperty("positive-acceleration-on-dusty", 0.022D);
      this.positiveAccelerationOnSnowy = this.properties.getDoubleProperty("positive-acceleration-on-snowy", 0.02D);
      this.positiveAccelerationOnSlippery = this.properties.getDoubleProperty("positive-acceleration-on-slippery", 0.02D);
      this.negativeAccelerationOnSolid = this.properties.getDoubleProperty("negative-acceleration-on-solid", 0.025D);
      this.negativeAccelerationOnDusty = this.properties.getDoubleProperty("negative-acceleration-on-dusty", 0.022D);
      this.negativeAccelerationOnSnowy = this.properties.getDoubleProperty("negative-acceleration-on-snowy", 0.02D);
      this.negativeAccelerationOnSlippery = this.properties.getDoubleProperty("negative-acceleration-on-slippery", 0.02D);
      this.deceleration = this.properties.getDoubleProperty("deceleration", 0.015D);
      this.turningAngleMin = this.properties.getIntegerProperty("turning-angle-min", 1);
      this.turningAngleMax = this.properties.getIntegerProperty("turning-angle-max", 4);
      VehicleFuelConfiguration var1 = this.vehicle.getConfiguration().fuel();
      this.minFuelConsumption = this.properties.getMinFuelConsumptionOverride(var1.getMinConsumption());
      this.maxFuelConsumption = this.properties.getMaxFuelConsumptionOverride(var1.getMaxConsumption());
   }

   public void tick() {
      super.tick();
      Map var1 = null;
      if ((this.forward || this.backward) && this.hasFuel()) {
         var1 = this.vehicle.getCurrentSurface(SURFACE_SCAN_IGNORE);
         double var2 = this.calculateAcceleration(var1, this.forward);
         if (var2 != 0.0D && this.vehicle.containsAnyWithin(EnumSurface.WATER, EnumSurface.LAVA, EnumSurface.SOLID)) {
            var2 = 0.0D;
         }

         if (this.forward && var2 != 0.0D) {
            this.currentSpeed = Math.min(this.currentSpeed + var2, this.maxPositiveAcceleration);
         } else if (this.backward && var2 != 0.0D) {
            this.currentSpeed = Math.max(this.currentSpeed - var2, -this.maxNegativeAcceleration);
         }
      }

      this.tickTurning();
      if (this.currentSpeed != 0.0D && this.isOnLand(var1 != null ? var1 : this.vehicle.getCurrentSurface(SURFACE_SCAN_IGNORE))) {
         float var7 = this.vehicle.getRotation();
         if (this.currentTurningAngle != 0) {
            if (this.currentSpeed > 0.0D) {
               if (this.turningLeft) {
                  var7 -= (float)this.currentTurningAngle;
               } else {
                  var7 += (float)this.currentTurningAngle;
               }
            } else if (this.turningLeft) {
               var7 += (float)this.currentTurningAngle;
            } else {
               var7 -= (float)this.currentTurningAngle;
            }

            this.vehicle.setRotation(var7);
         }

         double var3 = -FastMath.sin(FastMath.toRadians((double)var7));
         double var5 = FastMath.cos(FastMath.toRadians((double)var7));
         this.vehicle.addMomentumX(this.currentSpeed * var3);
         this.vehicle.addMomentumZ(this.currentSpeed * var5);
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

   private void tickTurning() {
      if (!this.turningLeft && !this.turningRight) {
         this.currentTurningAngle = 0;
      } else {
         double var1 = this.currentSpeed > 0.0D ? this.maxPositiveAcceleration : this.maxNegativeAcceleration;
         double var3 = FastMath.abs(this.currentSpeed) / FastMath.abs(var1);
         int var5 = this.turningAngleMin + (int)FastMath.round((double)(this.turningAngleMax - this.turningAngleMin) * var3);
         this.currentTurningAngle = Math.max(var5, 0);
      }

   }

   private void tickFuelConsumption() {
      if (this.currentSpeed != 0.0D && !(this.minFuelConsumption > this.maxFuelConsumption) && !((double)this.maxFuelConsumption <= 0.0D)) {
         double var1 = this.currentSpeed > 0.0D ? this.maxPositiveAcceleration : this.maxNegativeAcceleration;
         double var3 = FastMath.abs(this.currentSpeed) / FastMath.abs(var1);
         float var5 = this.minFuelConsumption + (float)FastMath.round((double)(this.maxFuelConsumption - this.minFuelConsumption) * var3);
         if (var5 > 0.0F) {
            this.vehicle.consumeFuel(var5);
         }

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

   protected boolean isOnLand(@NotNull Map<EnumSurface, Double> var1) {
      return var1.containsKey(EnumSurface.SOLID) || var1.containsKey(EnumSurface.DUSTY) || var1.containsKey(EnumSurface.SNOWY) || var1.containsKey(EnumSurface.SLIPPERY);
   }

   protected double calculateAcceleration(@NotNull Map<EnumSurface, Double> var1, boolean var2) {
      double var3 = 0.0D;

      Entry var6;
      for(Iterator var5 = var1.entrySet().iterator(); var5.hasNext(); var3 += this.getAccelerationOn((EnumSurface)var6.getKey(), var2) * (Double)var6.getValue()) {
         var6 = (Entry)var5.next();
      }

      return var3;
   }

   protected double getAccelerationOn(@NotNull EnumSurface var1, boolean var2) {
      double var10000;
      switch(var1) {
      case SOLID:
         var10000 = var2 ? this.positiveAccelerationOnSolid : this.negativeAccelerationOnSolid;
         break;
      case DUSTY:
         var10000 = var2 ? this.positiveAccelerationOnDusty : this.negativeAccelerationOnDusty;
         break;
      case SNOWY:
         var10000 = var2 ? this.positiveAccelerationOnSnowy : this.negativeAccelerationOnSnowy;
         break;
      case SLIPPERY:
         var10000 = var2 ? this.positiveAccelerationOnSlippery : this.negativeAccelerationOnSlippery;
         break;
      default:
         var10000 = 0.0D;
      }

      return var10000;
   }

   public void process(@NotNull PlayerSteerInput var1) {
      this.forward = var1.forward > 0;
      this.backward = var1.forward < 0;
      this.turningLeft = var1.sideways > 0;
      this.turningRight = var1.sideways < 0;
   }

   public void standby() {
      this.forward = false;
      this.backward = false;
      this.turningLeft = false;
      this.turningRight = false;
   }

   static {
      SURFACE_SCAN_IGNORE = Arrays.asList(EnumSurface.EMPTY, EnumSurface.UNKNOWN);
   }
}
