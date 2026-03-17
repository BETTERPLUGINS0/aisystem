package advancedplugins.pm2.cv.models.v1_21_R5.entity.controller;

import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.BodyRotationController;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import lombok.Generated;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class BodyRotationControlWrapper extends BodyRotationControl implements BodyRotationController {
   private final Mob mob;
   private boolean isHeadClampUneven;
   private boolean isBodyClampUneven;
   private float maxHeadAngle;
   private float maxBodyAngle;
   private float minHeadAngle;
   private float minBodyAngle;
   private boolean playerMode;
   private float stableAngle = 15.0F;
   private int rotationDelay = 10;
   private int rotationDuration = 10;
   private int headStableTime;
   private float lastStableYHeadRot;

   public BodyRotationControlWrapper(Mob var1) {
      super(var1);
      this.mob = var1;
      this.maxHeadAngle = (float)var1.getMaxHeadYRot();
      this.maxBodyAngle = (float)var1.getMaxHeadYRot();
      this.minHeadAngle = -this.maxHeadAngle;
      this.minBodyAngle = -this.maxBodyAngle;
   }

   public void clientTick() {
      if (!this.getMob().isDeadOrDying()) {
         if (this.isMoving()) {
            this.mob.yBodyRot = this.mob.getYRot();
            this.rotateHeadIfNecessary();
            this.lastStableYHeadRot = this.mob.yHeadRot;
            this.headStableTime = 0;
         } else if (this.notCarryingMobPassengers()) {
            if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > this.stableAngle) {
               this.headStableTime = 0;
               this.lastStableYHeadRot = this.mob.yHeadRot;
               this.rotateBodyIfNecessary();
            } else if (!this.playerMode) {
               ++this.headStableTime;
               if (this.headStableTime > this.rotationDelay) {
                  this.rotateHeadTowardsFront();
               }
            }
         }
      }

   }

   public float getYHeadRot() {
      return this.mob.yHeadRot;
   }

   public void setYHeadRot(float var1) {
      this.mob.yHeadRot = var1;
   }

   public float getXHeadRot() {
      return this.mob.getXRot();
   }

   public void setXHeadRot(float var1) {
      this.mob.setXRot(var1);
   }

   public float getYBodyRot() {
      return this.mob.yBodyRot;
   }

   public void setYBodyRot(float var1) {
      this.mob.yBodyRot = var1;
   }

   private void rotateBodyIfNecessary() {
      this.mob.yBodyRot = MathUtils.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, this.isBodyClampUneven ? this.minBodyAngle : -this.maxBodyAngle, this.maxBodyAngle);
   }

   private void rotateHeadIfNecessary() {
      this.mob.yHeadRot = MathUtils.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, this.isHeadClampUneven ? this.minHeadAngle : -this.maxHeadAngle, this.maxHeadAngle);
   }

   private void rotateHeadTowardsFront() {
      float var1 = (float)(this.headStableTime - this.rotationDelay) / (float)this.rotationDuration;
      float var2 = MathUtils.clamp(var1, 0.0F, 1.0F);
      float var3 = this.maxHeadAngle * (1.0F - var2);
      float var4 = (this.isHeadClampUneven ? this.minHeadAngle : -this.maxHeadAngle) * (1.0F - var2);
      this.mob.yBodyRot = MathUtils.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, var4, var3);
   }

   private boolean notCarryingMobPassengers() {
      return !(this.mob.getFirstPassenger() instanceof Mob);
   }

   private boolean isMoving() {
      double var1 = this.mob.getX() - this.mob.xo;
      double var3 = this.mob.getZ() - this.mob.zo;
      return var1 * var1 + var3 * var3 > 2.500000277905201E-7D;
   }

   @Generated
   public Mob getMob() {
      return this.mob;
   }

   @Generated
   public boolean isHeadClampUneven() {
      return this.isHeadClampUneven;
   }

   @Generated
   public boolean isBodyClampUneven() {
      return this.isBodyClampUneven;
   }

   @Generated
   public float getMaxHeadAngle() {
      return this.maxHeadAngle;
   }

   @Generated
   public float getMaxBodyAngle() {
      return this.maxBodyAngle;
   }

   @Generated
   public float getMinHeadAngle() {
      return this.minHeadAngle;
   }

   @Generated
   public float getMinBodyAngle() {
      return this.minBodyAngle;
   }

   @Generated
   public boolean isPlayerMode() {
      return this.playerMode;
   }

   @Generated
   public float getStableAngle() {
      return this.stableAngle;
   }

   @Generated
   public int getRotationDelay() {
      return this.rotationDelay;
   }

   @Generated
   public int getRotationDuration() {
      return this.rotationDuration;
   }

   @Generated
   public int getHeadStableTime() {
      return this.headStableTime;
   }

   @Generated
   public float getLastStableYHeadRot() {
      return this.lastStableYHeadRot;
   }

   @Generated
   public void setHeadClampUneven(boolean var1) {
      this.isHeadClampUneven = var1;
   }

   @Generated
   public void setBodyClampUneven(boolean var1) {
      this.isBodyClampUneven = var1;
   }

   @Generated
   public void setMaxHeadAngle(float var1) {
      this.maxHeadAngle = var1;
   }

   @Generated
   public void setMaxBodyAngle(float var1) {
      this.maxBodyAngle = var1;
   }

   @Generated
   public void setMinHeadAngle(float var1) {
      this.minHeadAngle = var1;
   }

   @Generated
   public void setMinBodyAngle(float var1) {
      this.minBodyAngle = var1;
   }

   @Generated
   public void setPlayerMode(boolean var1) {
      this.playerMode = var1;
   }

   @Generated
   public void setStableAngle(float var1) {
      this.stableAngle = var1;
   }

   @Generated
   public void setRotationDelay(int var1) {
      this.rotationDelay = var1;
   }

   @Generated
   public void setRotationDuration(int var1) {
      this.rotationDuration = var1;
   }

   @Generated
   public void setHeadStableTime(int var1) {
      this.headStableTime = var1;
   }

   @Generated
   public void setLastStableYHeadRot(float var1) {
      this.lastStableYHeadRot = var1;
   }
}
