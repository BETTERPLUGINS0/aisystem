package com.bergerkiller.bukkit.tc.rails.logic;

import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailLogicGround extends RailLogic {
   public static final RailLogicGround INSTANCE = new RailLogicGround();

   private RailLogicGround() {
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
      }

   }

   public void onGravity(MinecartMember<?> member, double gravityFactorSquared) {
      CommonMinecart<?> e = (CommonMinecart)member.getEntity();
      e.vel.y.subtract(gravityFactorSquared * this.getGravityMultiplier(member));
   }

   public double getForwardVelocity(MinecartMember<?> member) {
      CommonEntity<?> e = member.getEntity();
      BlockFace direction = member.getDirection();
      double vel = 0.0D;
      vel += e.vel.getX() * FaceUtil.cos(direction);
      vel += e.vel.getZ() * FaceUtil.sin(direction);
      return vel;
   }

   public void setForwardVelocity(MinecartMember<?> member, double force) {
      CommonEntity<?> e = member.getEntity();
      if (e.vel.getY() > 0.0D) {
         Vector vel = e.vel.vector();
         MathUtil.setVectorLength(vel, force);
         e.vel.set(vel);
      } else {
         e.vel.set(force * FaceUtil.cos(member.getDirection()), e.vel.getY(), force * FaceUtil.sin(member.getDirection()));
      }

   }

   public void onUpdateOrientation(MinecartMember<?> member, Quaternion orientation) {
      CommonMinecart<?> entity = (CommonMinecart)member.getEntity();
      double movedX = entity.getMovedX();
      double movedZ = entity.getMovedZ();
      float oldyaw = entity.loc.getYaw();
      float newyaw = oldyaw;
      float newpitch = entity.loc.getPitch();
      boolean upsideDown = newpitch <= -91.0F || newpitch >= 91.0F;
      if (Math.abs(movedX) > 0.01D || Math.abs(movedZ) > 0.01D) {
         newyaw = MathUtil.getLookAtYaw(movedX, movedZ);
      }

      if (upsideDown) {
         newpitch = MathUtil.wrapAngle(newpitch + 180.0F);
      }

      if ((double)Math.abs(newpitch) > 0.1D) {
         newpitch = (float)((double)newpitch * 0.1D);
      } else {
         newpitch = 0.0F;
      }

      if (upsideDown) {
         newpitch += 180.0F;
      }

      member.setRotationWrap(newyaw, newpitch);
   }

   public BlockFace getMovementDirection(BlockFace endDirection) {
      return endDirection;
   }

   public boolean hasVerticalMovement() {
      return true;
   }

   public void onPreMove(MinecartMember<?> member) {
      if (!member.isMovementControlled()) {
         ((CommonMinecart)member.getEntity()).vel.multiply(((CommonMinecart)member.getEntity()).getDerailedVelocityMod());
      }

   }
}
