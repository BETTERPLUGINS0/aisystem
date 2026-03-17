package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.bases.mutable.LocationAbstract;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.Collections;
import java.util.List;
import org.bukkit.util.Vector;

public class WheelTrackerMember {
   public static final double MIN_WHEEL_DISTANCE = 1.0E-5D;
   private final MinecartMember<?> _owner;
   private final WheelTrackerMember.Wheel _front;
   private final WheelTrackerMember.Wheel _back;
   private Quaternion _orientation_last = null;
   private Vector _asyncPosition = null;
   private Vector _position = null;
   private double _centripetalForce = 0.0D;
   private double _bankingRoll = 0.0D;

   public WheelTrackerMember(MinecartMember<?> owner) {
      this._owner = owner;
      this._front = new WheelTrackerMember.Wheel(owner, true);
      this._back = new WheelTrackerMember.Wheel(owner, false);
   }

   public MinecartMember<?> getOwner() {
      return this._owner;
   }

   public WheelTrackerMember.Wheel front() {
      return this._front;
   }

   public WheelTrackerMember.Wheel back() {
      return this._back;
   }

   public WheelTrackerMember.Wheel other(WheelTrackerMember.Wheel wheel) {
      return this._front == wheel ? this._back : this._front;
   }

   public WheelTrackerMember.Wheel movingForwards() {
      return this._owner.isOrientationInverted() ? this._back : this._front;
   }

   public WheelTrackerMember.Wheel movingBackwards() {
      return this._owner.isOrientationInverted() ? this._front : this._back;
   }

   public boolean hasWheelDistance() {
      return this._front.getDistance() > 1.0E-5D || this._back.getDistance() > 1.0E-5D;
   }

   public Quaternion getLastOrientation() {
      if (this._orientation_last == null) {
         this._orientation_last = this._owner.getOrientation();
      }

      return this._orientation_last;
   }

   public Vector getPosition() {
      if (this._position == null) {
         if (!CommonUtil.isMainThread()) {
            Vector p = this._asyncPosition;
            if (p == null) {
               p = ((CommonMinecart)this._owner.getEntity()).loc.vector();
            }

            return p;
         }

         double diff = this.back().getDistance() - this.front().getDistance();
         this._position = new Vector();
         this._position.add(this.front().getPosition());
         this._position.add(this.back().getPosition());
         if (diff != 0.0D) {
            Vector dir = this.front().getPosition().clone().subtract(this.back().getPosition());
            double n = MathUtil.getNormalizationFactor(dir);
            if (n < 1.0E10D) {
               dir.multiply(n * diff);
            } else {
               dir = this.getOwner().getOrientationForward().multiply(diff);
            }

            this._position.add(dir);
         }

         this._position.multiply(0.5D);
         this._position.add(((CommonMinecart)this._owner.getEntity()).loc.vector());
         this._asyncPosition = this._position;
      }

      return this._position;
   }

   public double getBankingRoll() {
      return this._bankingRoll;
   }

   public void startTeleport() {
      this._front._invalid = true;
      this._back._invalid = true;
      this._position = null;
   }

   public void update() {
      this._orientation_last = this._owner.getOrientation();
      this._position = null;
      this._front.update();
      this._back.update();
      Vector dir = this.front().getPosition().clone().subtract(this.back().getPosition());
      Vector fwd_a;
      if (dir.lengthSquared() < 1.0E-4D) {
         fwd_a = this.front().getForward();
         Vector fwd_b = this.back().getForward();
         if (fwd_a.dot(fwd_b) > 0.0D) {
            dir = fwd_a.clone().add(fwd_b);
         } else {
            Vector a = this._owner.getOrientationForward();
            if (a.dot(FaceUtil.faceToVector(this._owner.getDirection())) >= 0.0D) {
               dir = fwd_a;
            } else {
               dir = fwd_b;
            }
         }
      }

      fwd_a = this.front().getUp().clone().add(this.back().getUp());
      if (fwd_a.lengthSquared() < 1.0E-4D) {
         fwd_a = this._owner.getOrientation().upVector();
      }

      Quaternion new_orientation = Quaternion.fromLookDirection(dir, fwd_a);
      TrainProperties props = this._owner.isUnloaded() ? null : this._owner.getGroup().getProperties();
      if (props != null && props.getBankingStrength() != 0.0D) {
         Quaternion q = Quaternion.divide(new_orientation, this.getLastOrientation());
         double centripetalForceStep = q.forwardVector().getX();
         if (MathUtil.isHeadingTo(this._owner.getDirection(), new_orientation.forwardVector())) {
            centripetalForceStep = -centripetalForceStep;
         }

         centripetalForceStep *= this._owner.getRealSpeedLimited();
         if (props.getBankingSmoothness() == 0.0D) {
            this._centripetalForce = centripetalForceStep;
         } else {
            this._centripetalForce += centripetalForceStep;
            this._centripetalForce *= 1.0D - 1.0D / props.getBankingSmoothness();
         }

         double angle = (double)MathUtil.atan2(this._centripetalForce, 1.0D / props.getBankingStrength());
         if (props.getBankingSmoothness() == 0.0D) {
            this._bankingRoll = angle;
         } else {
            this._bankingRoll += 1.0D / props.getBankingSmoothness() * (angle - this._bankingRoll);
            if (Math.abs(this._bankingRoll) < 0.01D) {
               this._bankingRoll = 0.0D;
            }
         }
      } else {
         this._bankingRoll = 0.0D;
      }

      if (this._owner.isUnloaded()) {
         this._owner.setOrientation(new_orientation);
      } else {
         this._owner.getRailLogic().onUpdateOrientation(this._owner, new_orientation);
      }

   }

   public static class Wheel {
      private final MinecartMember<?> member;
      private final boolean _front;
      private double _distance = 0.0D;
      private final Vector _displayPosition = new Vector();
      private final Vector _position = new Vector();
      private final Vector _forward = new Vector();
      private final Vector _up = new Vector();
      private boolean _invalid = true;
      private boolean _displayInvalid = true;
      private boolean _oriented;
      private final RailPath.Position _railPosition = new RailPath.Position();

      public Wheel(MinecartMember<?> member, boolean front) {
         this.member = member;
         this._front = front;
      }

      public void setDistance(double distance) {
         if (this._distance != distance) {
            this._distance = distance;
            this._invalid = true;
         }

      }

      public double getDistance() {
         return this._distance;
      }

      public double getEdgeDistance() {
         double edgeDistance = 0.5D * (double)((CommonMinecart)this.member.getEntity()).getWidth();
         edgeDistance -= this._distance;
         return edgeDistance;
      }

      public Vector getPosition() {
         if (this._invalid) {
            this.update();
         }

         return this._position;
      }

      public Vector getAbsolutePosition() {
         return this.getPosition().clone().add(((CommonMinecart)this.member.getEntity()).loc.vector());
      }

      public Vector getDisplayPosition() {
         if (this._displayInvalid) {
            if (this._invalid) {
               this.update();
            }

            LocationAbstract loc = ((CommonMinecart)this.member.getEntity()).loc;
            this._displayPosition.setX(loc.getX() + this._position.getX());
            this._displayPosition.setY(loc.getY() + this._position.getY());
            this._displayPosition.setZ(loc.getZ() + this._position.getZ());
            this._displayInvalid = false;
         }

         return this._displayPosition;
      }

      public Matrix4x4 getAbsoluteTransform() {
         Matrix4x4 result = new Matrix4x4();
         this.getAbsoluteTransform(result);
         return result;
      }

      public void getAbsoluteTransform(Matrix4x4 target) {
         target.setIdentity();
         target.translate(this.getDisplayPosition());
         target.rotate(Quaternion.fromLookDirection(this._forward.clone(), this._up.clone()));
      }

      public Vector getUp() {
         if (this._invalid) {
            this.update();
         }

         return this._up;
      }

      public Vector getForward() {
         if (this._invalid) {
            this.update();
         }

         return this._forward;
      }

      public void update() {
         this._invalid = false;
         this._displayInvalid = true;
         List rails;
         if (this.member.isUnloaded()) {
            rails = Collections.emptyList();
         } else {
            rails = this.member.getGroup().getRailTracker().getRailInformation();
         }

         int railIndex = -1;
         RailTracker.TrackedRail rail;
         if (!this.member.isDerailed()) {
            for(int i = 0; i < rails.size(); ++i) {
               rail = (RailTracker.TrackedRail)rails.get(i);
               if (rail == this.member.getRailTracker().getRail()) {
                  railIndex = i;
                  break;
               }
            }
         }

         if (railIndex == -1) {
            Quaternion orientation = this.member.getOrientation();
            Util.setVector(this._up, orientation.upVector());
            Util.setVector(this._forward, orientation.forwardVector());
            Util.setVector(this._position, this._forward);
            this._position.multiply(this._distance);
            if (!this._front) {
               this._position.multiply(-1.0D);
            }

         } else {
            RailPath.Position position = this._railPosition;
            position.setLocation(((CommonMinecart)this.member.getEntity()).loc);
            position.setMotion(this.member.getRailTracker().getMotionVector());
            rail = (RailTracker.TrackedRail)rails.get(railIndex);
            rail.getPath().move(position, rail.state.railBlock(), 0.0D);
            double initial_position_error = position.distanceSquared(((CommonMinecart)this.member.getEntity()).loc);
            if (initial_position_error > 1.0E-5D) {
               RailPath.Position next_position;
               RailTracker.TrackedRail next_rail;
               double next_initial_error;
               if (railIndex > 0) {
                  next_position = new RailPath.Position();
                  next_position.setLocation(((CommonMinecart)this.member.getEntity()).loc);
                  next_position.setMotion(this.member.getRailTracker().getMotionVector());
                  next_rail = (RailTracker.TrackedRail)rails.get(railIndex - 1);
                  next_rail.getPath().move(next_position, next_rail.state.railBlock(), 0.0D);
                  next_initial_error = next_position.distanceSquared(((CommonMinecart)this.member.getEntity()).loc);
                  if (next_initial_error < initial_position_error) {
                     next_position.copyTo(position);
                     initial_position_error = next_initial_error;
                     --railIndex;
                  }
               }

               if (railIndex < rails.size() - 1) {
                  next_position = new RailPath.Position();
                  next_position.setLocation(((CommonMinecart)this.member.getEntity()).loc);
                  next_position.setMotion(this.member.getRailTracker().getMotionVector());
                  next_rail = (RailTracker.TrackedRail)rails.get(railIndex + 1);
                  next_rail.getPath().move(next_position, next_rail.state.railBlock(), 0.0D);
                  next_initial_error = next_position.distanceSquared(((CommonMinecart)this.member.getEntity()).loc);
                  if (next_initial_error < initial_position_error) {
                     next_position.copyTo(position);
                     ++railIndex;
                  }
               }
            }

            int order = -1;
            double dot = position.motDot(this.member.getOrientationForward());
            boolean oriented = dot > 0.0D;
            if (dot >= -1.0E-4D && dot <= 1.0E-4D) {
               oriented = this._oriented;
            }

            this._oriented = oriented;
            if (oriented ^ this._front) {
               position.motX = -position.motX;
               position.motY = -position.motY;
               position.motZ = -position.motZ;
               position.reverse = true;
               order = 1;
            }

            if (this._distance > 1.0E-5D) {
               double remainingDistance = this._distance;

               for(int index = railIndex; index >= 0 && index < rails.size() && remainingDistance >= 1.0E-4D; index += order) {
                  RailTracker.TrackedRail rail = (RailTracker.TrackedRail)rails.get(index);
                  RailPath path = rail.getPath();
                  remainingDistance -= path.move(position, rail.state.railBlock(), remainingDistance);
               }

               position.posX += position.motX * remainingDistance;
               position.posY += position.motY * remainingDistance;
               position.posZ += position.motZ * remainingDistance;
            }

            this._position.setX(position.posX - ((CommonMinecart)this.member.getEntity()).loc.getX());
            this._position.setY(position.posY - ((CommonMinecart)this.member.getEntity()).loc.getY());
            this._position.setZ(position.posZ - ((CommonMinecart)this.member.getEntity()).loc.getZ());
            Quaternion orientation = position.getWheelOrientation();
            Util.setVector(this._up, orientation.upVector());
            Util.setVector(this._forward, orientation.forwardVector());
            if (position.motDot(this._forward) < 0.0D) {
               this._forward.multiply(-1.0D);
            }

            if (!this._front) {
               this._forward.multiply(-1.0D);
            }

            if (TCConfig.wheelTrackerDebugEnabled) {
               Util.spawnBubble(position.toLocation(((CommonMinecart)this.member.getEntity()).getWorld()));
            }

         }
      }
   }
}
