package advancedplugins.pm2.cv.models.v1_21_R3.entity.controller;

import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.BodyRotationController;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.control.EntityAIBodyControl;

public class EntityBodyOrientationManager extends EntityAIBodyControl implements BodyRotationController {
   private final EntityInsentient targetEntity;
   private boolean asymmetricHeadLimits;
   private boolean asymmetricBodyLimits;
   private float headRotationUpperBound;
   private float bodyRotationUpperBound;
   private float headRotationLowerBound;
   private float bodyRotationLowerBound;
   private boolean humanoidBehavior;
   private float stabilityThreshold = 15.0F;
   private int stabilizationDelay = 10;
   private int transitionPeriod = 10;
   private int timeSinceStable;
   private float previousStableHeadOrientation;

   public EntityBodyOrientationManager(EntityInsentient mob) {
      super(var1);
      this.targetEntity = var1;
      this.headRotationUpperBound = (float)var1.af();
      this.bodyRotationUpperBound = (float)var1.af();
      this.headRotationLowerBound = -this.headRotationUpperBound;
      this.bodyRotationLowerBound = -this.bodyRotationUpperBound;
   }

   public void tick() {
      this.a();
   }

   public void a() {
      if (!this.getTargetEntity().eE()) {
         if (this.detectMovement()) {
            this.synchronizeBodyToMovement();
         } else if (this.isNotCarryingPassenger()) {
            this.handleStaticOrientation();
         }

      }
   }

   private void synchronizeBodyToMovement() {
      this.targetEntity.aX = this.targetEntity.dL();
      this.adjustHeadOrientation();
      this.previousStableHeadOrientation = this.targetEntity.aZ;
      this.timeSinceStable = 0;
   }

   private void handleStaticOrientation() {
      float var1 = Math.abs(this.targetEntity.aZ - this.previousStableHeadOrientation);
      if (var1 > this.stabilityThreshold) {
         this.resetStabilization();
         this.adjustBodyOrientation();
      } else if (!this.humanoidBehavior) {
         this.progressStabilization();
      }

   }

   private void resetStabilization() {
      this.timeSinceStable = 0;
      this.previousStableHeadOrientation = this.targetEntity.aZ;
   }

   private void progressStabilization() {
      ++this.timeSinceStable;
      if (this.timeSinceStable > this.stabilizationDelay) {
         this.smoothHeadAlignment();
      }

   }

   public float getYHeadRot() {
      return this.targetEntity.aZ;
   }

   public void setYHeadRot(float rot) {
      this.targetEntity.aZ = var1;
   }

   public float getXHeadRot() {
      return this.targetEntity.dN();
   }

   public void setXHeadRot(float rot) {
      this.targetEntity.w(var1);
   }

   public float getYBodyRot() {
      return this.targetEntity.aX;
   }

   public void setYBodyRot(float rot) {
      this.targetEntity.aX = var1;
   }

   public boolean isHeadClampUneven() {
      return this.asymmetricHeadLimits;
   }

   public void setHeadClampUneven(boolean uneven) {
      this.asymmetricHeadLimits = var1;
   }

   public boolean isBodyClampUneven() {
      return this.asymmetricBodyLimits;
   }

   public void setBodyClampUneven(boolean uneven) {
      this.asymmetricBodyLimits = var1;
   }

   public float getMaxHeadAngle() {
      return this.headRotationUpperBound;
   }

   public void setMaxHeadAngle(float angle) {
      this.headRotationUpperBound = var1;
   }

   public float getMaxBodyAngle() {
      return this.bodyRotationUpperBound;
   }

   public void setMaxBodyAngle(float angle) {
      this.bodyRotationUpperBound = var1;
   }

   public float getMinHeadAngle() {
      return this.headRotationLowerBound;
   }

   public void setMinHeadAngle(float angle) {
      this.headRotationLowerBound = var1;
   }

   public float getMinBodyAngle() {
      return this.bodyRotationLowerBound;
   }

   public void setMinBodyAngle(float angle) {
      this.bodyRotationLowerBound = var1;
   }

   public boolean isPlayerMode() {
      return this.humanoidBehavior;
   }

   public void setPlayerMode(boolean playerMode) {
      this.humanoidBehavior = var1;
   }

   public float getStableAngle() {
      return this.stabilityThreshold;
   }

   public void setStableAngle(float angle) {
      this.stabilityThreshold = var1;
   }

   public int getRotationDelay() {
      return this.stabilizationDelay;
   }

   public void setRotationDelay(int delay) {
      this.stabilizationDelay = var1;
   }

   public int getRotationDuration() {
      return this.transitionPeriod;
   }

   public void setRotationDuration(int duration) {
      this.transitionPeriod = var1;
   }

   public void save(SavedData data) {
      var1.putBoolean("asymmetricHeadLimits", this.asymmetricHeadLimits);
      var1.putBoolean("asymmetricBodyLimits", this.asymmetricBodyLimits);
      var1.putFloat("headRotationUpperBound", this.headRotationUpperBound);
      var1.putFloat("bodyRotationUpperBound", this.bodyRotationUpperBound);
      var1.putFloat("headRotationLowerBound", this.headRotationLowerBound);
      var1.putFloat("bodyRotationLowerBound", this.bodyRotationLowerBound);
      var1.putBoolean("humanoidBehavior", this.humanoidBehavior);
      var1.putFloat("stabilityThreshold", this.stabilityThreshold);
      var1.putInt("stabilizationDelay", this.stabilizationDelay);
      var1.putInt("transitionPeriod", this.transitionPeriod);
   }

   public void load(SavedData data) {
      this.asymmetricHeadLimits = var1.getBoolean("asymmetricHeadLimits");
      this.asymmetricBodyLimits = var1.getBoolean("asymmetricBodyLimits");
      this.headRotationUpperBound = var1.getFloat("headRotationUpperBound");
      this.bodyRotationUpperBound = var1.getFloat("bodyRotationUpperBound");
      this.headRotationLowerBound = var1.getFloat("headRotationLowerBound");
      this.bodyRotationLowerBound = var1.getFloat("bodyRotationLowerBound");
      this.humanoidBehavior = var1.getBoolean("humanoidBehavior");
      this.stabilityThreshold = var1.getFloat("stabilityThreshold");
      this.stabilizationDelay = var1.getInt("stabilizationDelay");
      this.transitionPeriod = var1.getInt("transitionPeriod");
   }

   public Optional<SavedData> save() {
      SavedData var1 = new SavedData();
      this.save(var1);
      return Optional.of(var1);
   }

   public float a(float from, float to, float maxDelta) {
      float var4 = MathUtils.wrapDegree(var2 - var1);
      return var1 + MathUtils.clamp(var4, -var3, var3);
   }

   private void adjustBodyOrientation() {
      float var1 = this.asymmetricBodyLimits ? this.bodyRotationLowerBound : -this.bodyRotationUpperBound;
      this.targetEntity.aX = MathUtils.rotateIfNecessary(this.targetEntity.aX, this.targetEntity.aZ, var1, this.bodyRotationUpperBound);
   }

   private void adjustHeadOrientation() {
      float var1 = this.asymmetricHeadLimits ? this.headRotationLowerBound : -this.headRotationUpperBound;
      this.targetEntity.aZ = MathUtils.rotateIfNecessary(this.targetEntity.aZ, this.targetEntity.aX, var1, this.headRotationUpperBound);
   }

   private void smoothHeadAlignment() {
      float var1 = this.calculateAlignmentProgress();
      float var2 = this.headRotationUpperBound * (1.0F - var1);
      float var3 = this.calculateAdjustedLowerLimit(var1);
      this.targetEntity.aX = MathUtils.rotateIfNecessary(this.targetEntity.aX, this.targetEntity.aZ, var3, var2);
   }

   private float calculateAlignmentProgress() {
      float var1 = (float)(this.timeSinceStable - this.stabilizationDelay);
      float var2 = var1 / (float)this.transitionPeriod;
      return MathUtils.clamp(var2, 0.0F, 1.0F);
   }

   private float calculateAdjustedLowerLimit(float progress) {
      float var2 = this.asymmetricHeadLimits ? this.headRotationLowerBound : -this.headRotationUpperBound;
      return var2 * (1.0F - var1);
   }

   private boolean isNotCarryingPassenger() {
      return !(this.targetEntity.cZ() instanceof EntityInsentient);
   }

   private boolean detectMovement() {
      double var1 = this.targetEntity.dA() - this.targetEntity.K;
      double var3 = this.targetEntity.dG() - this.targetEntity.M;
      double var5 = var1 * var1 + var3 * var3;
      return var5 > 2.500000277905201E-7D;
   }

   public EntityInsentient getMob() {
      return this.targetEntity;
   }

   public EntityInsentient getTargetEntity() {
      return this.targetEntity;
   }

   public boolean isAsymmetricHeadLimits() {
      return this.asymmetricHeadLimits;
   }

   public void setAsymmetricHeadLimits(boolean asymmetricHeadLimits) {
      this.asymmetricHeadLimits = var1;
   }

   public boolean isAsymmetricBodyLimits() {
      return this.asymmetricBodyLimits;
   }

   public void setAsymmetricBodyLimits(boolean asymmetricBodyLimits) {
      this.asymmetricBodyLimits = var1;
   }

   public float getHeadRotationUpperBound() {
      return this.headRotationUpperBound;
   }

   public void setHeadRotationUpperBound(float headRotationUpperBound) {
      this.headRotationUpperBound = var1;
   }

   public float getBodyRotationUpperBound() {
      return this.bodyRotationUpperBound;
   }

   public void setBodyRotationUpperBound(float bodyRotationUpperBound) {
      this.bodyRotationUpperBound = var1;
   }

   public float getHeadRotationLowerBound() {
      return this.headRotationLowerBound;
   }

   public void setHeadRotationLowerBound(float headRotationLowerBound) {
      this.headRotationLowerBound = var1;
   }

   public float getBodyRotationLowerBound() {
      return this.bodyRotationLowerBound;
   }

   public void setBodyRotationLowerBound(float bodyRotationLowerBound) {
      this.bodyRotationLowerBound = var1;
   }

   public boolean isHumanoidBehavior() {
      return this.humanoidBehavior;
   }

   public void setHumanoidBehavior(boolean humanoidBehavior) {
      this.humanoidBehavior = var1;
   }

   public float getStabilityThreshold() {
      return this.stabilityThreshold;
   }

   public void setStabilityThreshold(float stabilityThreshold) {
      this.stabilityThreshold = var1;
   }

   public int getStabilizationDelay() {
      return this.stabilizationDelay;
   }

   public void setStabilizationDelay(int stabilizationDelay) {
      this.stabilizationDelay = var1;
   }

   public int getTransitionPeriod() {
      return this.transitionPeriod;
   }

   public void setTransitionPeriod(int transitionPeriod) {
      this.transitionPeriod = var1;
   }

   @Generated
   public int getTimeSinceStable() {
      return this.timeSinceStable;
   }

   @Generated
   public float getPreviousStableHeadOrientation() {
      return this.previousStableHeadOrientation;
   }

   @Generated
   public void setTimeSinceStable(int timeSinceStable) {
      this.timeSinceStable = var1;
   }

   @Generated
   public void setPreviousStableHeadOrientation(float previousStableHeadOrientation) {
      this.previousStableHeadOrientation = var1;
   }
}
