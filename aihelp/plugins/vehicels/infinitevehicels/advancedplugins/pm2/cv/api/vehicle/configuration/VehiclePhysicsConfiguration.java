package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.util.Constants;
import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class VehiclePhysicsConfiguration implements ConfigurationSectionWritable {
   public static final VehiclePhysicsConfiguration DEFAULTS = new VehiclePhysicsConfiguration(false, 3.8D, 0.15D, 0.015D, 0.2D, 0.2D, 0.6D, 0.555D, 0.5D, 0.2D, 0.3D, 0.5D, 0.9D, 1);
   private final boolean floats;
   private final double gravityMaximum;
   private final double gravityAcceleration;
   private final double airFriction;
   private final double frictionOnUnknown;
   private final double frictionOnSolid;
   private final double frictionOnDusty;
   private final double frictionOnSnowy;
   private final double frictionOnSlippery;
   private final double frictionOnWater;
   private final double frictionOnLava;
   private final double frictionThroughWater;
   private final double frictionThroughLava;
   private final int blockClimbCapacity;

   public static VehiclePhysicsConfiguration load(ConfigurationSection var0) {
      boolean var1 = var0.getBoolean("floats");
      double var2 = checkGravityValue(var0.getDouble(Constants.Key.GRAVITY_MAXIMUM), "gravity maximum");
      double var4 = checkGravityValue(var0.getDouble(Constants.Key.GRAVITY_ACCELERATION), "gravity acceleration");
      double var6 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_AIR) / 100.0D, "air friction");
      double var8 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_ON_UNKNOWN) / 100.0D, "friction on unknown");
      double var10 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_ON_SOLID) / 100.0D, "friction on solid");
      double var12 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_ON_DUSTY) / 100.0D, "friction on dusty");
      double var14 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_ON_SNOWY) / 100.0D, "friction on snowy");
      double var16 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_ON_SLIPPERY) / 100.0D, "friction on slippery");
      double var18 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_ON_WATER) / 100.0D, "friction on water");
      double var20 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_ON_LAVA) / 100.0D, "friction on lava");
      double var22 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_THROUGH_WATER) / 100.0D, "friction through water");
      double var24 = checkFrictionValue(var0.getDouble(Constants.Key.FRICTION_THROUGH_LAVA) / 100.0D, "friction through lava");
      int var26 = var0.getInt(Constants.Key.BLOCK_CLIMB_CAPACITY);
      return new VehiclePhysicsConfiguration(var1, var2, var4, var6, var8, var10, var12, var14, var16, var18, var20, var22, var24, var26);
   }

   private static double checkValue(double var0, String var2) {
      if (var0 > 0.0D) {
         return var0;
      } else {
         throw new InvalidConfigurationException(var2 + " must be > 0");
      }
   }

   private static double checkGravityValue(double var0, String var2) {
      if (var0 >= 0.0D) {
         return var0;
      } else {
         throw new InvalidConfigurationException(var2 + " cannot be negative");
      }
   }

   private static double checkFrictionValue(double var0, String var2) {
      if (var0 > 0.0D && var0 <= 1.0D) {
         return var0;
      } else {
         throw new InvalidConfigurationException(var2 + " must be > 0 and less or equal to 1.0");
      }
   }

   public VehiclePhysicsConfiguration(boolean var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, double var24, int var26) {
      Preconditions.checkArgument(var2 >= 0.0D, "gravityMaximum cannot be negative");
      Preconditions.checkArgument(var4 >= 0.0D, "gravityAcceleration cannot be negative");
      Preconditions.checkArgument(var6 > 0.0D, "airFriction must be > 0");
      Preconditions.checkArgument(var8 > 0.0D, "frictionOnUnknown must be > 0");
      Preconditions.checkArgument(var10 > 0.0D, "frictionOnSolid must be > 0");
      Preconditions.checkArgument(var12 > 0.0D, "frictionOnDusty must be > 0");
      Preconditions.checkArgument(var14 > 0.0D, "frictionOnSnowy must be > 0");
      Preconditions.checkArgument(var16 > 0.0D, "frictionOnSlippery must be > 0");
      Preconditions.checkArgument(var18 > 0.0D, "frictionOnWater must be > 0");
      Preconditions.checkArgument(var20 > 0.0D, "frictionOnLava must be > 0");
      Preconditions.checkArgument(var22 > 0.0D, "frictionThroughWater must be > 0");
      Preconditions.checkArgument(var24 > 0.0D, "frictionThroughLava must be > 0");
      this.floats = var1;
      this.gravityMaximum = var2;
      this.gravityAcceleration = var4;
      this.airFriction = var6;
      this.frictionOnUnknown = var8;
      this.frictionOnSolid = var10;
      this.frictionOnDusty = var12;
      this.frictionOnSnowy = var14;
      this.frictionOnSlippery = var16;
      this.frictionOnWater = var18;
      this.frictionOnLava = var20;
      this.frictionThroughWater = var22;
      this.frictionThroughLava = var24;
      this.blockClimbCapacity = var26;
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("floats", this.floats);
      var1.set(Constants.Key.GRAVITY_MAXIMUM, this.gravityMaximum);
      var1.set(Constants.Key.GRAVITY_ACCELERATION, this.gravityAcceleration);
      var1.set(Constants.Key.FRICTION_AIR, this.airFriction * 100.0D);
      var1.set(Constants.Key.FRICTION_ON_UNKNOWN, this.frictionOnUnknown * 100.0D);
      var1.set(Constants.Key.FRICTION_ON_SOLID, this.frictionOnSolid * 100.0D);
      var1.set(Constants.Key.FRICTION_ON_DUSTY, this.frictionOnDusty * 100.0D);
      var1.set(Constants.Key.FRICTION_ON_SNOWY, this.frictionOnSnowy * 100.0D);
      var1.set(Constants.Key.FRICTION_ON_SLIPPERY, this.frictionOnSlippery * 100.0D);
      var1.set(Constants.Key.FRICTION_ON_WATER, this.frictionOnWater * 100.0D);
      var1.set(Constants.Key.FRICTION_ON_LAVA, this.frictionOnLava * 100.0D);
      var1.set(Constants.Key.FRICTION_THROUGH_WATER, this.frictionThroughWater * 100.0D);
      var1.set(Constants.Key.FRICTION_THROUGH_LAVA, this.frictionThroughLava * 100.0D);
      var1.set(Constants.Key.BLOCK_CLIMB_CAPACITY, this.blockClimbCapacity);
   }

   public static VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder builder() {
      return new VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder();
   }

   public boolean isFloats() {
      return this.floats;
   }

   public double getGravityMaximum() {
      return this.gravityMaximum;
   }

   public double getGravityAcceleration() {
      return this.gravityAcceleration;
   }

   public double getAirFriction() {
      return this.airFriction;
   }

   public double getFrictionOnUnknown() {
      return this.frictionOnUnknown;
   }

   public double getFrictionOnSolid() {
      return this.frictionOnSolid;
   }

   public double getFrictionOnDusty() {
      return this.frictionOnDusty;
   }

   public double getFrictionOnSnowy() {
      return this.frictionOnSnowy;
   }

   public double getFrictionOnSlippery() {
      return this.frictionOnSlippery;
   }

   public double getFrictionOnWater() {
      return this.frictionOnWater;
   }

   public double getFrictionOnLava() {
      return this.frictionOnLava;
   }

   public double getFrictionThroughWater() {
      return this.frictionThroughWater;
   }

   public double getFrictionThroughLava() {
      return this.frictionThroughLava;
   }

   public int getBlockClimbCapacity() {
      return this.blockClimbCapacity;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof VehiclePhysicsConfiguration)) {
         return false;
      } else {
         VehiclePhysicsConfiguration var2 = (VehiclePhysicsConfiguration)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isFloats() != var2.isFloats()) {
            return false;
         } else if (Double.compare(this.getGravityMaximum(), var2.getGravityMaximum()) != 0) {
            return false;
         } else if (Double.compare(this.getGravityAcceleration(), var2.getGravityAcceleration()) != 0) {
            return false;
         } else if (Double.compare(this.getAirFriction(), var2.getAirFriction()) != 0) {
            return false;
         } else if (Double.compare(this.getFrictionOnUnknown(), var2.getFrictionOnUnknown()) != 0) {
            return false;
         } else if (Double.compare(this.getFrictionOnSolid(), var2.getFrictionOnSolid()) != 0) {
            return false;
         } else if (Double.compare(this.getFrictionOnDusty(), var2.getFrictionOnDusty()) != 0) {
            return false;
         } else if (Double.compare(this.getFrictionOnSnowy(), var2.getFrictionOnSnowy()) != 0) {
            return false;
         } else if (Double.compare(this.getFrictionOnSlippery(), var2.getFrictionOnSlippery()) != 0) {
            return false;
         } else if (Double.compare(this.getFrictionOnWater(), var2.getFrictionOnWater()) != 0) {
            return false;
         } else if (Double.compare(this.getFrictionOnLava(), var2.getFrictionOnLava()) != 0) {
            return false;
         } else if (Double.compare(this.getFrictionThroughWater(), var2.getFrictionThroughWater()) != 0) {
            return false;
         } else if (Double.compare(this.getFrictionThroughLava(), var2.getFrictionThroughLava()) != 0) {
            return false;
         } else {
            return this.getBlockClimbCapacity() == var2.getBlockClimbCapacity();
         }
      }
   }

   protected boolean canEqual(Object var1) {
      return var1 instanceof VehiclePhysicsConfiguration;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var27 = var2 * 59 + (this.isFloats() ? 79 : 97);
      long var3 = Double.doubleToLongBits(this.getGravityMaximum());
      var27 = var27 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getGravityAcceleration());
      var27 = var27 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getAirFriction());
      var27 = var27 * 59 + (int)(var7 >>> 32 ^ var7);
      long var9 = Double.doubleToLongBits(this.getFrictionOnUnknown());
      var27 = var27 * 59 + (int)(var9 >>> 32 ^ var9);
      long var11 = Double.doubleToLongBits(this.getFrictionOnSolid());
      var27 = var27 * 59 + (int)(var11 >>> 32 ^ var11);
      long var13 = Double.doubleToLongBits(this.getFrictionOnDusty());
      var27 = var27 * 59 + (int)(var13 >>> 32 ^ var13);
      long var15 = Double.doubleToLongBits(this.getFrictionOnSnowy());
      var27 = var27 * 59 + (int)(var15 >>> 32 ^ var15);
      long var17 = Double.doubleToLongBits(this.getFrictionOnSlippery());
      var27 = var27 * 59 + (int)(var17 >>> 32 ^ var17);
      long var19 = Double.doubleToLongBits(this.getFrictionOnWater());
      var27 = var27 * 59 + (int)(var19 >>> 32 ^ var19);
      long var21 = Double.doubleToLongBits(this.getFrictionOnLava());
      var27 = var27 * 59 + (int)(var21 >>> 32 ^ var21);
      long var23 = Double.doubleToLongBits(this.getFrictionThroughWater());
      var27 = var27 * 59 + (int)(var23 >>> 32 ^ var23);
      long var25 = Double.doubleToLongBits(this.getFrictionThroughLava());
      var27 = var27 * 59 + (int)(var25 >>> 32 ^ var25);
      var27 = var27 * 59 + this.getBlockClimbCapacity();
      return var27;
   }

   public static class VehiclePhysicsConfigurationBuilder {
      private boolean floats;
      private double gravityMaximum;
      private double gravityAcceleration;
      private double airFriction;
      private double frictionOnUnknown;
      private double frictionOnSolid;
      private double frictionOnDusty;
      private double frictionOnSnowy;
      private double frictionOnSlippery;
      private double frictionOnWater;
      private double frictionOnLava;
      private double frictionThroughWater;
      private double frictionThroughLava;
      private int blockClimbCapacity;

      VehiclePhysicsConfigurationBuilder() {
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder floats(boolean var1) {
         this.floats = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder gravityMaximum(double var1) {
         this.gravityMaximum = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder gravityAcceleration(double var1) {
         this.gravityAcceleration = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder airFriction(double var1) {
         this.airFriction = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder frictionOnUnknown(double var1) {
         this.frictionOnUnknown = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder frictionOnSolid(double var1) {
         this.frictionOnSolid = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder frictionOnDusty(double var1) {
         this.frictionOnDusty = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder frictionOnSnowy(double var1) {
         this.frictionOnSnowy = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder frictionOnSlippery(double var1) {
         this.frictionOnSlippery = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder frictionOnWater(double var1) {
         this.frictionOnWater = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder frictionOnLava(double var1) {
         this.frictionOnLava = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder frictionThroughWater(double var1) {
         this.frictionThroughWater = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder frictionThroughLava(double var1) {
         this.frictionThroughLava = var1;
         return this;
      }

      public VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder blockClimbCapacity(int var1) {
         this.blockClimbCapacity = var1;
         return this;
      }

      public VehiclePhysicsConfiguration build() {
         return new VehiclePhysicsConfiguration(this.floats, this.gravityMaximum, this.gravityAcceleration, this.airFriction, this.frictionOnUnknown, this.frictionOnSolid, this.frictionOnDusty, this.frictionOnSnowy, this.frictionOnSlippery, this.frictionOnWater, this.frictionOnLava, this.frictionThroughWater, this.frictionThroughLava, this.blockClimbCapacity);
      }

      public String toString() {
         return "VehiclePhysicsConfiguration.VehiclePhysicsConfigurationBuilder(floats=" + this.floats + ", gravityMaximum=" + this.gravityMaximum + ", gravityAcceleration=" + this.gravityAcceleration + ", airFriction=" + this.airFriction + ", frictionOnUnknown=" + this.frictionOnUnknown + ", frictionOnSolid=" + this.frictionOnSolid + ", frictionOnDusty=" + this.frictionOnDusty + ", frictionOnSnowy=" + this.frictionOnSnowy + ", frictionOnSlippery=" + this.frictionOnSlippery + ", frictionOnWater=" + this.frictionOnWater + ", frictionOnLava=" + this.frictionOnLava + ", frictionThroughWater=" + this.frictionThroughWater + ", frictionThroughLava=" + this.frictionThroughLava + ", blockClimbCapacity=" + this.blockClimbCapacity + ")";
      }
   }
}
