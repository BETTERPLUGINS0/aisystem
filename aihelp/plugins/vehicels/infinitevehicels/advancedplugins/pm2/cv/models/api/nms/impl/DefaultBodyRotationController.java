package advancedplugins.pm2.cv.models.api.nms.impl;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.BodyRotationController;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;

public class DefaultBodyRotationController implements BodyRotationController {
   private final BaseEntity<?> entity;
   private float xHeadRot;
   private float yHeadRot;
   private float yBodyRot;
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

   public DefaultBodyRotationController(BaseEntity<?> var1) {
      this.entity = var1;
      this.xHeadRot = var1.getXHeadRot();
      this.yHeadRot = var1.getYHeadRot();
      this.yBodyRot = var1.getYBodyRot();
      this.maxHeadAngle = 75.0F;
      this.maxBodyAngle = 75.0F;
      this.minHeadAngle = -this.maxHeadAngle;
      this.minBodyAngle = -this.maxBodyAngle;
   }

   public void tick() {
      this.xHeadRot = this.entity.getXHeadRot();
      this.yHeadRot = this.entity.getYHeadRot();
      if (this.entity.isWalking()) {
         this.yBodyRot = this.entity.getYRot();
         this.rotateHeadIfNecessary();
         this.lastStableYHeadRot = this.yHeadRot;
         this.headStableTime = 0;
      } else if (this.notCarryingMobPassengers()) {
         if (Math.abs(this.yHeadRot - this.lastStableYHeadRot) > this.stableAngle) {
            this.headStableTime = 0;
            this.lastStableYHeadRot = this.yHeadRot;
            this.rotateBodyIfNecessary();
         } else if (!this.playerMode) {
            ++this.headStableTime;
            if (this.headStableTime > this.rotationDelay) {
               this.rotateHeadTowardsFront();
            }
         }
      }

   }

   private void rotateBodyIfNecessary() {
      this.yBodyRot = MathUtils.rotateIfNecessary(this.yBodyRot, this.yHeadRot, this.isBodyClampUneven ? this.minBodyAngle : -this.maxBodyAngle, this.maxBodyAngle);
   }

   private void rotateHeadIfNecessary() {
      this.yHeadRot = MathUtils.rotateIfNecessary(this.yHeadRot, this.yBodyRot, this.isHeadClampUneven ? this.minHeadAngle : -this.maxHeadAngle, this.maxHeadAngle);
   }

   private void rotateHeadTowardsFront() {
      float var1 = (float)(this.headStableTime - this.rotationDelay) / (float)this.rotationDuration;
      float var2 = MathUtils.clamp(var1, 0.0F, 1.0F);
      float var3 = this.maxHeadAngle * (1.0F - var2);
      float var4 = (this.isHeadClampUneven ? this.minHeadAngle : -this.maxHeadAngle) * (1.0F - var2);
      this.yBodyRot = MathUtils.rotateIfNecessary(this.yBodyRot, this.yHeadRot, var4, var3);
   }

   private boolean notCarryingMobPassengers() {
      Iterator var1 = this.entity.getPassengers().iterator();

      while(var1.hasNext()) {
         Entity var2 = (Entity)var1.next();
         if (var2 instanceof Mob) {
            return false;
         }
      }

      return true;
   }

   @Generated
   public BaseEntity<?> getEntity() {
      return this.entity;
   }

   @Generated
   public float getXHeadRot() {
      return this.xHeadRot;
   }

   @Generated
   public float getYHeadRot() {
      return this.yHeadRot;
   }

   @Generated
   public float getYBodyRot() {
      return this.yBodyRot;
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
   public void setXHeadRot(float var1) {
      this.xHeadRot = var1;
   }

   @Generated
   public void setYHeadRot(float var1) {
      this.yHeadRot = var1;
   }

   @Generated
   public void setYBodyRot(float var1) {
      this.yBodyRot = var1;
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
