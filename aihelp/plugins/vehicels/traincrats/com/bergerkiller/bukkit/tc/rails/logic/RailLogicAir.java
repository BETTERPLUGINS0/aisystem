package com.bergerkiller.bukkit.tc.rails.logic;

import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.standard.type.SlowdownMode;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailLogicAir extends RailLogic {
   public static final RailLogicAir INSTANCE = new RailLogicAir();

   private RailLogicAir() {
      super(BlockFace.SELF);
   }

   public double getGravityMultiplier(MinecartMember<?> member) {
      return 0.04D;
   }

   public void onSpacingUpdate(MinecartMember<?> member, Vector velocity, Vector factor) {
      double motLen = velocity.length();
      if (motLen > 0.01D) {
         double f = motLen / ((CommonMinecart)member.getEntity()).getMaxSpeed();
         velocity.setX(velocity.getX() + f * factor.getX() * TCConfig.cartDistanceForcer);
         velocity.setZ(velocity.getZ() + f * factor.getZ() * TCConfig.cartDistanceForcer);
         if (member.isMovingVerticalOnly() || member != member.getGroup().head() && member != member.getGroup().head()) {
            velocity.setY(velocity.getY() + f * factor.getY() * TCConfig.cartDistanceForcer);
         }
      }

   }

   public void onUpdateOrientation(MinecartMember<?> member, Quaternion orientation) {
      CommonMinecart<?> entity = (CommonMinecart)member.getEntity();
      Vector forward = new Vector(entity.getMovedX(), entity.getMovedY(), entity.getMovedZ());
      if (member.getGroup().size() > 1) {
         boolean has_delta = false;
         double dx = 0.0D;
         double dy = 0.0D;
         double dz = 0.0D;
         MinecartMember m;
         if (member != member.getGroup().head()) {
            m = member.getNeighbour(-1);
            if (m.isDerailed()) {
               dx += ((CommonMinecart)m.getEntity()).loc.getX() - ((CommonMinecart)member.getEntity()).loc.getX();
               dy += ((CommonMinecart)m.getEntity()).loc.getY() - ((CommonMinecart)member.getEntity()).loc.getY();
               dz += ((CommonMinecart)m.getEntity()).loc.getZ() - ((CommonMinecart)member.getEntity()).loc.getZ();
               has_delta = true;
            }
         }

         if (member != member.getGroup().tail()) {
            m = member.getNeighbour(1);
            if (m.isDerailed()) {
               dx += ((CommonMinecart)member.getEntity()).loc.getX() - ((CommonMinecart)m.getEntity()).loc.getX();
               dy += ((CommonMinecart)member.getEntity()).loc.getY() - ((CommonMinecart)m.getEntity()).loc.getY();
               dz += ((CommonMinecart)member.getEntity()).loc.getZ() - ((CommonMinecart)m.getEntity()).loc.getZ();
               has_delta = true;
            }
         }

         if (has_delta) {
            forward.setX(dx);
            forward.setY(dy);
            forward.setZ(dz);
         }
      } else if (!member.getGroup().getProperties().isSlowingDown(SlowdownMode.GRAVITY)) {
         forward.multiply(0.0D);
      }

      if (forward.lengthSquared() <= 1.0E-8D) {
         member.setOrientation(orientation);
      } else {
         if (forward.dot(orientation.forwardVector()) < 0.0D) {
            forward.multiply(-1.0D);
         }

         member.setOrientation(Quaternion.fromLookDirection(forward, orientation.upVector()));
      }

   }

   public void onUpdateOrientation_old(MinecartMember<?> member, Quaternion orientation) {
      CommonMinecart<?> entity = (CommonMinecart)member.getEntity();
      boolean upsideDown = MathUtil.getAngleDifference(entity.loc.getPitch(), 180.0F) < 89.0F;
      float newYaw = ((CommonMinecart)member.getEntity()).loc.getYaw();
      float newPitch = ((CommonMinecart)member.getEntity()).loc.getPitch();
      if (member.getGroup().size() <= 1) {
         double movedX = entity.getMovedX();
         double movedY = entity.getMovedY();
         double movedZ = entity.getMovedZ();
         boolean movedXZ = Math.abs(movedX) > 0.001D || Math.abs(movedZ) > 0.001D;
         if (Math.abs(movedX) > 0.01D || Math.abs(movedZ) > 0.01D) {
            newYaw = MathUtil.getLookAtYaw(movedX, movedZ);
         }

         if (movedXZ && Math.abs(movedY) > 0.001D) {
            newPitch = MathUtil.clamp(-MathUtil.getLookAtPitch(-movedX, -movedY, -movedZ), 89.9F);
            if (upsideDown) {
               newPitch += 180.0F;
            }
         }
      } else {
         int n = 0;
         double dx = 0.0D;
         double dy = 0.0D;
         double dz = 0.0D;
         MinecartMember m;
         if (member != member.getGroup().head()) {
            m = member.getNeighbour(-1);
            dx += ((CommonMinecart)m.getEntity()).loc.getX() - ((CommonMinecart)member.getEntity()).loc.getX();
            dy += ((CommonMinecart)m.getEntity()).loc.getY() - ((CommonMinecart)member.getEntity()).loc.getY();
            dz += ((CommonMinecart)m.getEntity()).loc.getZ() - ((CommonMinecart)member.getEntity()).loc.getZ();
            ++n;
         }

         if (member != member.getGroup().tail()) {
            m = member.getNeighbour(1);
            dx += ((CommonMinecart)member.getEntity()).loc.getX() - ((CommonMinecart)m.getEntity()).loc.getX();
            dy += ((CommonMinecart)member.getEntity()).loc.getY() - ((CommonMinecart)m.getEntity()).loc.getY();
            dz += ((CommonMinecart)member.getEntity()).loc.getZ() - ((CommonMinecart)m.getEntity()).loc.getZ();
            ++n;
         }

         dx /= (double)n;
         dy /= (double)n;
         dz /= (double)n;
         if (MathUtil.lengthSquared(new double[]{dx, dz}) < 1.0E-4D) {
            if (MathUtil.getAngleDifference(newPitch, 90.0F) < MathUtil.getAngleDifference(newPitch, -90.0F)) {
               newPitch = 90.0F;
            } else {
               newPitch = -90.0F;
            }
         } else {
            newYaw = MathUtil.getLookAtYaw(dx, dz);
            newPitch = MathUtil.getLookAtPitch(dx, dy, dz);
         }

         if (upsideDown) {
            newPitch += 180.0F;
         }
      }

      member.setRotationWrap(newYaw, newPitch);
   }

   public BlockFace getMovementDirection(BlockFace endDirection) {
      return endDirection;
   }

   public double getForwardVelocity(MinecartMember<?> member) {
      CommonEntity<?> e = member.getEntity();
      if (((CommonMinecart)member.getEntity()).vel.xz.lengthSquared() == 0.0D) {
         double dot = e.vel.getY() * (double)member.getDirection().getModY();
         return MathUtil.invert(e.vel.length(), dot < 0.0D);
      } else {
         return e.vel.length();
      }
   }

   public void setForwardVelocity(MinecartMember<?> member, double force) {
      if (member.isMovementControlled()) {
         super.setForwardVelocity(member, force);
      } else {
         Vector vel;
         if (((CommonMinecart)member.getEntity()).vel.xz.lengthSquared() == 0.0D) {
            vel = ((CommonMinecart)member.getEntity()).vel.vector();
            MathUtil.setVectorLength(vel, force);
            ((CommonMinecart)member.getEntity()).vel.set(vel);
         } else {
            vel = ((CommonMinecart)member.getEntity()).vel.vector();
            MathUtil.setVectorLength(vel, force);
            ((CommonMinecart)member.getEntity()).vel.set(vel);
         }
      }

   }

   public boolean hasVerticalMovement() {
      return true;
   }

   public void onPreMove(MinecartMember<?> member) {
      CommonMinecart<?> entity = (CommonMinecart)member.getEntity();
      TrainProperties trainProp = member.getGroup().getProperties();
      if (!member.isMovementControlled() && trainProp.isSlowingDown(SlowdownMode.FRICTION)) {
         Vector flyingMod = entity.getFlyingVelocityMod();
         if (member.getGroup().getUpdateStepCount() > 1) {
            double factor = member.getGroup().getUpdateSpeedFactor();
            flyingMod = new Vector(Math.pow(flyingMod.getX(), factor), Math.pow(flyingMod.getY(), factor), Math.pow(flyingMod.getZ(), factor));
         }

         entity.vel.multiply(flyingMod);
      }

   }
}
