package advancedplugins.pm2.cv.api.vehicle.controller.predefined;

import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.enums.EnumSurface;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleFuelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleController;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleControllerProperties;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerSteerInput;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RotorcraftVehicleController extends VehicleController {
   protected double maxPositiveAcceleration;
   protected double maxNegativeAcceleration;
   protected double positiveAcceleration;
   protected double negativeAcceleration;
   protected double deceleration;
   protected double increaseHeightSpeed;
   protected double decreaseHeightSpeed;
   protected int rotationAngle;
   protected float minFuelConsumption;
   protected float maxFuelConsumption;
   protected boolean forward;
   protected boolean backward;
   protected boolean increasingHeight;
   protected boolean decreasingHeight;
   protected boolean rotatingLeft;
   protected boolean rotatingRight;
   protected double currentSpeed;

   public RotorcraftVehicleController(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
      super(var1, var2);
   }

   public void loadProperties() {
      this.maxPositiveAcceleration = this.properties.getDoubleProperty("max-positive-acceleration", 0.95D);
      this.maxNegativeAcceleration = this.properties.getDoubleProperty("max-negative-acceleration", 0.25D);
      this.positiveAcceleration = this.properties.getDoubleProperty("positive-acceleration", 0.055D);
      this.negativeAcceleration = this.properties.getDoubleProperty("negative-acceleration", 0.035D);
      this.deceleration = this.properties.getDoubleProperty("deceleration", 0.04D);
      this.increaseHeightSpeed = this.properties.getDoubleProperty("increase-height-speed", 0.23D);
      this.decreaseHeightSpeed = this.properties.getDoubleProperty("decrease-height-speed", 0.23D);
      this.rotationAngle = this.properties.getIntegerProperty("rotation-angle", 3);
      VehicleFuelConfiguration var1 = this.vehicle.getConfiguration().fuel();
      this.minFuelConsumption = this.properties.getMinFuelConsumptionOverride(var1.getMinConsumption());
      this.maxFuelConsumption = this.properties.getMaxFuelConsumptionOverride(var1.getMaxConsumption());
   }

   public void tick() {
      super.tick();
      boolean var1 = false;
      if ((this.forward || this.backward) && this.hasFuel() && !this.vehicle.containsAnyWithin(EnumSurface.WATER, EnumSurface.LAVA, EnumSurface.SOLID)) {
         if (this.forward && this.positiveAcceleration != 0.0D) {
            this.currentSpeed = Math.min(this.currentSpeed + this.positiveAcceleration, this.maxPositiveAcceleration);
         } else if (this.backward && this.negativeAcceleration != 0.0D) {
            this.currentSpeed = Math.max(this.currentSpeed - this.negativeAcceleration, -this.maxNegativeAcceleration);
         }
      }

      if ((this.increasingHeight || this.decreasingHeight) && this.hasFuel()) {
         if (this.increasingHeight) {
            this.vehicle.addMomentumY(this.increaseHeightSpeed);
         } else {
            this.vehicle.addMomentumY(-this.decreaseHeightSpeed);
         }

         var1 = true;
      }

      this.tickRotation();
      if (this.currentSpeed != 0.0D && this.vehicle.isInTheAir()) {
         float var2 = this.vehicle.getRotation();
         double var3 = -FastMath.sin(FastMath.toRadians((double)var2));
         double var5 = FastMath.cos(FastMath.toRadians((double)var2));
         this.vehicle.addMomentumX(this.currentSpeed * var3);
         this.vehicle.addMomentumZ(this.currentSpeed * var5);
      }

      if (var1) {
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
         double var1 = this.currentSpeed > 0.0D ? this.maxPositiveAcceleration : this.maxNegativeAcceleration;
         double var3 = FastMath.abs(this.currentSpeed) / FastMath.abs(var1);
         float var5 = this.minFuelConsumption + (float)FastMath.round((double)(this.maxFuelConsumption - this.minFuelConsumption) * var3);
         if (var5 > 0.0F) {
            this.vehicle.consumeFuel(var5);
         }

      }
   }

   private void tickRotation() {
      if (this.rotatingLeft || this.rotatingRight) {
         float var1 = this.vehicle.getRotation();
         if (this.currentSpeed >= 0.0D) {
            if (this.rotatingLeft) {
               var1 -= (float)this.rotationAngle;
            } else {
               var1 += (float)this.rotationAngle;
            }
         } else if (this.rotatingLeft) {
            var1 += (float)this.rotationAngle;
         } else {
            var1 -= (float)this.rotationAngle;
         }

         this.vehicle.setRotation(var1);
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
      if (!this.increasingHeight && !this.decreasingHeight) {
         if (this.vehicle.getMomentumX() == 0.0D && this.vehicle.getMomentumZ() == 0.0D) {
            if (this.rotatingLeft) {
               this.vehicle.setState(VehicleState.TURNING_LEFT);
            } else if (this.rotatingRight) {
               this.vehicle.setState(VehicleState.TURNING_RIGHT);
            } else {
               this.vehicle.setState(VehicleState.IDLE);
            }
         } else {
            boolean var3 = this.currentSpeed < 0.0D;
            VehicleState var2 = var3 ? VehicleState.MOVING_BACKWARDS : VehicleState.MOVING;
            if (this.rotatingLeft) {
               var2 = var3 ? VehicleState.MOVING_BACKWARDS_TURNING_LEFT : VehicleState.MOVING_TURNING_LEFT;
            } else if (this.rotatingRight) {
               var2 = var3 ? VehicleState.MOVING_BACKWARDS_TURNING_RIGHT : VehicleState.MOVING_TURNING_RIGHT;
            }

            this.vehicle.setState(var2);
         }
      } else {
         VehicleState var1 = this.increasingHeight ? VehicleState.INCREASING_HEIGHT : VehicleState.DECREASING_HEIGHT;
         if (this.rotatingLeft) {
            var1 = this.increasingHeight ? VehicleState.INCREASING_HEIGHT_TURNING_LEFT : VehicleState.DECREASING_HEIGHT_TURNING_LEFT;
         } else if (this.rotatingRight) {
            var1 = this.increasingHeight ? VehicleState.INCREASING_HEIGHT_TURNING_RIGHT : VehicleState.DECREASING_HEIGHT_TURNING_RIGHT;
         }

         this.vehicle.setState(var1);
      }

   }

   public void process(@NotNull PlayerSteerInput var1) {
      this.forward = var1.forward > 0;
      this.backward = var1.forward < 0;
      this.increasingHeight = var1.jump;
      this.decreasingHeight = var1.unmount;
      this.rotatingLeft = var1.sideways > 0;
      this.rotatingRight = var1.sideways < 0;
   }

   public void standby() {
      this.forward = false;
      this.backward = false;
      this.increasingHeight = false;
      this.decreasingHeight = false;
      this.rotatingLeft = false;
      this.rotatingRight = false;
   }
}
