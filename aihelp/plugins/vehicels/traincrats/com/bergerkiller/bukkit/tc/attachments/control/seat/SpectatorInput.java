package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundPlayerRotationPacketHandle;
import org.bukkit.util.Vector;

class SpectatorInput {
   private AttachmentViewer player;
   private int blindTicks = 0;
   private float yawLimit = 360.0F;
   private boolean enabled = false;
   private final Object deltaRotationLock = new Object();
   private SpectatorInput.YawPitch deltaRotation;
   private final Quaternion absOrientation;

   SpectatorInput() {
      this.deltaRotation = SpectatorInput.YawPitch.ZERO;
      this.absOrientation = new Quaternion();
   }

   public void start(AttachmentViewer player, float yawLimit) {
      this.player = player;
      this.blindTicks = CommonUtil.getServerTicks() + 5;
      this.yawLimit = yawLimit;
      this.deltaRotation = SpectatorInput.YawPitch.ZERO;
      this.absOrientation.setIdentity();
      this.enabled = true;
      this.sendRotation(0.0F, 0.0F);
   }

   public void startLocked() {
      this.enabled = false;
   }

   public void addInputRotation(SpectatorInput.YawPitch rotation) {
      synchronized(this.deltaRotationLock) {
         this.deltaRotation = SpectatorInput.YawPitch.add(this.deltaRotation, rotation);
      }
   }

   public void stop(Matrix4x4 currentEyeTransform) {
      if (this.player != null) {
         FirstPersonView.HeadRotation headRot = FirstPersonView.HeadRotation.compute(currentEyeTransform);
         headRot = headRot.ensureLevel();
         this.sendRotation(headRot.pitch, headRot.yaw);
      }

      this.player = null;
      this.blindTicks = 0;
      this.deltaRotation = SpectatorInput.YawPitch.ZERO;
      this.enabled = false;
   }

   public boolean isStarted() {
      return this.player != null;
   }

   public void applyTo(Matrix4x4 eyeTransform) {
      Vector pos = eyeTransform.toVector();
      Quaternion rot = eyeTransform.getRotation();
      this.applyTo(rot);
      eyeTransform.setIdentity();
      eyeTransform.translate(pos);
      eyeTransform.rotate(rot);
   }

   public void applyTo(Quaternion eyeRotation) {
      if (this.enabled) {
         SpectatorInput.YawPitch accumulatedRotation;
         synchronized(this.deltaRotationLock) {
            accumulatedRotation = this.deltaRotation;
            this.deltaRotation = SpectatorInput.YawPitch.ZERO;
         }

         if (accumulatedRotation.yaw != 0.0F || accumulatedRotation.pitch != 0.0F) {
            SpectatorInput.RelativeOrientationCalc calc = new SpectatorInput.RelativeOrientationCalc(this, accumulatedRotation, eyeRotation);
            this.absOrientation.setTo(calc.calculate());
            if (isUpsideDown(this.absOrientation)) {
               this.absOrientation.rotateZFlip();
            }
         }
      }

      eyeRotation.multiply(this.absOrientation);
   }

   public void update() {
      if (this.player != null) {
         if (this.blindTicks != 0) {
            if (CommonUtil.getServerTicks() < this.blindTicks) {
               return;
            }

            this.blindTicks = 0;
         }

      }
   }

   private void sendRotation(float pitch, float yaw) {
      this.player.send((PacketHandle)ClientboundPlayerRotationPacketHandle.createAbsolute(yaw, pitch));
   }

   private static boolean isUpsideDown(Quaternion q) {
      return 1.0D + 2.0D * (-q.getX() * q.getX() - q.getZ() * q.getZ()) < 0.0D;
   }

   public static final class YawPitch {
      public static final SpectatorInput.YawPitch ZERO = new SpectatorInput.YawPitch(0.0F, 0.0F);
      public final float yaw;
      public final float pitch;

      public YawPitch(float yaw, float pitch) {
         this.yaw = yaw;
         this.pitch = pitch;
      }

      public static SpectatorInput.YawPitch add(SpectatorInput.YawPitch a, SpectatorInput.YawPitch b) {
         return new SpectatorInput.YawPitch(a.yaw + b.yaw, a.pitch + b.pitch);
      }

      public static SpectatorInput.YawPitch subtract(SpectatorInput.YawPitch a, SpectatorInput.YawPitch b) {
         return new SpectatorInput.YawPitch(a.yaw - b.yaw, a.pitch - b.pitch);
      }
   }

   private static class RelativeOrientationCalc {
      private static final int MAX_INTERPOLATION_ROUNDS = 20;
      public final Quaternion base;
      public final double basePitch;
      public final double baseYaw;
      public final double deltaPitch;
      public final double deltaYaw;
      public final double maxForwardZ;

      public RelativeOrientationCalc(SpectatorInput input, SpectatorInput.YawPitch inputRotation, Quaternion eyeRotation) {
         Quaternion current = Quaternion.multiply(eyeRotation, input.absOrientation);
         Vector eyePYR = current.getYawPitchRoll();
         this.basePitch = eyePYR.getX();
         this.baseYaw = -eyePYR.getY();
         this.deltaPitch = (double)inputRotation.pitch;
         this.deltaYaw = SpectatorInput.isUpsideDown(current) ? (double)inputRotation.yaw : (double)(-inputRotation.yaw);
         this.base = Quaternion.divide(input.absOrientation, current);
         this.maxForwardZ = input.yawLimit >= 180.0F ? 1.0D : Math.cos(Math.toRadians((double)input.yawLimit));
      }

      public Quaternion calculate() {
         Quaternion fullRotation = this.createRotation(this.deltaPitch, this.deltaYaw);
         if (this.isValidRotation(fullRotation)) {
            return fullRotation;
         } else {
            Quaternion tmp = new Quaternion();
            boolean canDoFullPitch = this.testRotation(tmp, this.deltaPitch, 0.0D);
            boolean canDoFullYaw = this.testRotation(tmp, 0.0D, this.deltaYaw);
            if (canDoFullPitch == canDoFullYaw) {
               return this.calcUsingSlerp(fullRotation);
            } else {
               double t0 = 0.0D;
               double t1 = 1.0D;
               Quaternion result;
               int n;
               double th;
               if (canDoFullYaw) {
                  result = this.createRotation(0.0D, this.deltaYaw);

                  for(n = 0; n < 20; ++n) {
                     th = 0.5D * (t0 + t1);
                     if (this.testRotation(tmp, th * this.deltaPitch, this.deltaYaw)) {
                        t0 = th;
                        result.setTo(tmp);
                     } else {
                        t1 = th;
                     }
                  }
               } else {
                  result = this.createRotation(this.deltaPitch, 0.0D);

                  for(n = 0; n < 20; ++n) {
                     th = 0.5D * (t0 + t1);
                     if (this.testRotation(tmp, this.deltaPitch, th * this.deltaYaw)) {
                        t0 = th;
                        result.setTo(tmp);
                     } else {
                        t1 = th;
                     }
                  }
               }

               return result;
            }
         }
      }

      private Quaternion calcUsingSlerp(Quaternion fullRotation) {
         Quaternion startRotation = this.createRotation(0.0D, 0.0D);
         double t0 = 0.0D;
         double t1 = 1.0D;
         Quaternion result = startRotation;

         for(int n = 0; n < 20; ++n) {
            double th = 0.5D * (t0 + t1);
            Quaternion qh = Quaternion.slerp(startRotation, fullRotation, th);
            if (this.isValidRotation(qh)) {
               t0 = th;
               result = qh;
            } else {
               t1 = th;
            }
         }

         return result;
      }

      private boolean testRotation(Quaternion tmp, double deltaPitch, double deltaYaw) {
         tmp.setTo(this.base);
         tmp.rotateY(this.baseYaw + deltaYaw);
         tmp.rotateX(this.basePitch + deltaPitch);
         return this.isValidRotation(tmp);
      }

      private Quaternion createRotation(double deltaPitch, double deltaYaw) {
         Quaternion result = new Quaternion();
         this.testRotation(result, deltaPitch, deltaYaw);
         return result;
      }

      private boolean isValidRotation(Quaternion rotation) {
         if (SpectatorInput.isUpsideDown(rotation)) {
            return false;
         } else {
            if (this.maxForwardZ != 1.0D) {
               Vector forward = rotation.forwardVector().setY(0.0D).normalize();
               if (forward.getZ() < this.maxForwardZ) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
