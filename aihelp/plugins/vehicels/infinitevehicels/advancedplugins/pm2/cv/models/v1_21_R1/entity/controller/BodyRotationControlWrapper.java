package advancedplugins.pm2.cv.models.v1_21_R1.entity.controller;

import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.BodyRotationController;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.control.EntityAIBodyControl;

public class BodyRotationControlWrapper extends EntityAIBodyControl implements BodyRotationController {
   private final EntityInsentient mob;
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

   public BodyRotationControlWrapper(EntityInsentient mob) {
      super(var1);
      this.mob = var1;
      this.maxHeadAngle = (float)var1.ae();
      this.maxBodyAngle = (float)var1.ae();
      this.minHeadAngle = -this.maxHeadAngle;
      this.minBodyAngle = -this.maxBodyAngle;
   }

   public void a() {
      if (!this.getMob().ex()) {
         if (this.isMoving()) {
            this.mob.aY = this.mob.dE();
            this.rotateHeadIfNecessary();
            this.lastStableYHeadRot = this.mob.ba;
            this.headStableTime = 0;
         } else if (this.notCarryingMobPassengers()) {
            if (Math.abs(this.mob.ba - this.lastStableYHeadRot) > this.stableAngle) {
               this.headStableTime = 0;
               this.lastStableYHeadRot = this.mob.ba;
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
      return this.mob.ba;
   }

   public void setYHeadRot(float rot) {
      this.mob.ba = var1;
   }

   public float getXHeadRot() {
      return this.mob.dG();
   }

   public void setXHeadRot(float rot) {
      this.mob.u(var1);
   }

   public float getYBodyRot() {
      return this.mob.aY;
   }

   public void setYBodyRot(float rot) {
      this.mob.aY = var1;
   }

   private void rotateBodyIfNecessary() {
      this.mob.aY = MathUtils.rotateIfNecessary(this.mob.aY, this.mob.ba, this.isBodyClampUneven ? this.minBodyAngle : -this.maxBodyAngle, this.maxBodyAngle);
   }

   private void rotateHeadIfNecessary() {
      this.mob.ba = MathUtils.rotateIfNecessary(this.mob.ba, this.mob.aY, this.isHeadClampUneven ? this.minHeadAngle : -this.maxHeadAngle, this.maxHeadAngle);
   }

   private void rotateHeadTowardsFront() {
      float var1 = (float)(this.headStableTime - this.rotationDelay) / (float)this.rotationDuration;
      float var2 = MathUtils.clamp(var1, 0.0F, 1.0F);
      float var3 = this.maxHeadAngle * (1.0F - var2);
      float var4 = (this.isHeadClampUneven ? this.minHeadAngle : -this.maxHeadAngle) * (1.0F - var2);
      this.mob.aY = MathUtils.rotateIfNecessary(this.mob.aY, this.mob.ba, var4, var3);
   }

   private boolean notCarryingMobPassengers() {
      return !(this.mob.cT() instanceof EntityInsentient);
   }

   private boolean isMoving() {
      double var1 = this.mob.dt() - this.mob.L;
      double var3 = this.mob.dz() - this.mob.N;
      return var1 * var1 + var3 * var3 > 2.500000277905201E-7D;
   }

   public EntityInsentient getMob() {
      return this.mob;
   }

   public boolean isHeadClampUneven() {
      return this.isHeadClampUneven;
   }

   public void setHeadClampUneven(boolean isHeadClampUneven) {
      this.isHeadClampUneven = var1;
   }

   public boolean isBodyClampUneven() {
      return this.isBodyClampUneven;
   }

   public void setBodyClampUneven(boolean isBodyClampUneven) {
      this.isBodyClampUneven = var1;
   }

   public float getMaxHeadAngle() {
      return this.maxHeadAngle;
   }

   public void setMaxHeadAngle(float maxHeadAngle) {
      this.maxHeadAngle = var1;
   }

   public float getMaxBodyAngle() {
      return this.maxBodyAngle;
   }

   public void setMaxBodyAngle(float maxBodyAngle) {
      this.maxBodyAngle = var1;
   }

   public float getMinHeadAngle() {
      return this.minHeadAngle;
   }

   public void setMinHeadAngle(float minHeadAngle) {
      this.minHeadAngle = var1;
   }

   public float getMinBodyAngle() {
      return this.minBodyAngle;
   }

   public void setMinBodyAngle(float minBodyAngle) {
      this.minBodyAngle = var1;
   }

   public boolean isPlayerMode() {
      return this.playerMode;
   }

   public void setPlayerMode(boolean playerMode) {
      this.playerMode = var1;
   }

   public float getStableAngle() {
      return this.stableAngle;
   }

   public void setStableAngle(float stableAngle) {
      this.stableAngle = var1;
   }

   public int getRotationDelay() {
      return this.rotationDelay;
   }

   public void setRotationDelay(int rotationDelay) {
      this.rotationDelay = var1;
   }

   public int getRotationDuration() {
      return this.rotationDuration;
   }

   public void setRotationDuration(int rotationDuration) {
      this.rotationDuration = var1;
   }

   public int getHeadStableTime() {
      return this.headStableTime;
   }

   public void setHeadStableTime(int headStableTime) {
      this.headStableTime = var1;
   }

   public float getLastStableYHeadRot() {
      return this.lastStableYHeadRot;
   }

   public void setLastStableYHeadRot(float lastStableYHeadRot) {
      this.lastStableYHeadRot = var1;
   }
}
