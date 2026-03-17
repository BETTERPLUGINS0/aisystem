package advancedplugins.pm2.cv.models.v1_21_R6.entity.controller;

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

   public EntityBodyOrientationManager(EntityInsentient var1) {
      super(var1);
      this.targetEntity = var1;
      this.headRotationUpperBound = (float)var1.gh();
      this.bodyRotationUpperBound = (float)var1.gh();
      this.headRotationLowerBound = -this.headRotationUpperBound;
      this.bodyRotationLowerBound = -this.bodyRotationUpperBound;
   }

   public void tick() {
      this.a();
   }

   public void a() {
      if (!this.getTargetEntity().eM()) {
         if (this.detectMovement()) {
            this.synchronizeBodyToMovement();
         } else if (this.isNotCarryingPassenger()) {
            this.handleStaticOrientation();
         }

      }
   }

   private void synchronizeBodyToMovement() {
      this.targetEntity.br = this.targetEntity.dP();
      this.adjustHeadOrientation();
      this.previousStableHeadOrientation = this.targetEntity.bt;
      this.timeSinceStable = 0;
   }

   private void handleStaticOrientation() {
      float var1 = Math.abs(this.targetEntity.bt - this.previousStableHeadOrientation);
      if (var1 > this.stabilityThreshold) {
         this.resetStabilization();
         this.adjustBodyOrientation();
      } else if (!this.humanoidBehavior) {
         this.progressStabilization();
      }

   }

   private void resetStabilization() {
      this.timeSinceStable = 0;
      this.previousStableHeadOrientation = this.targetEntity.bt;
   }

   private void progressStabilization() {
      ++this.timeSinceStable;
      if (this.timeSinceStable > this.stabilizationDelay) {
         this.smoothHeadAlignment();
      }

   }

   public float getYHeadRot() {
      return this.targetEntity.bt;
   }

   public void setYHeadRot(float var1) {
      this.targetEntity.bt = var1;
   }

   public float getXHeadRot() {
      return this.targetEntity.dR();
   }

   public void setXHeadRot(float var1) {
      this.targetEntity.w(var1);
   }

   public float getYBodyRot() {
      return this.targetEntity.br;
   }

   public void setYBodyRot(float var1) {
      this.targetEntity.br = var1;
   }

   public boolean isHeadClampUneven() {
      return this.asymmetricHeadLimits;
   }

   public void setHeadClampUneven(boolean var1) {
      this.asymmetricHeadLimits = var1;
   }

   public boolean isBodyClampUneven() {
      return this.asymmetricBodyLimits;
   }

   public void setBodyClampUneven(boolean var1) {
      this.asymmetricBodyLimits = var1;
   }

   public float getMaxHeadAngle() {
      return this.headRotationUpperBound;
   }

   public void setMaxHeadAngle(float var1) {
      this.headRotationUpperBound = var1;
   }

   public float getMaxBodyAngle() {
      return this.bodyRotationUpperBound;
   }

   public void setMaxBodyAngle(float var1) {
      this.bodyRotationUpperBound = var1;
   }

   public float getMinHeadAngle() {
      return this.headRotationLowerBound;
   }

   public void setMinHeadAngle(float var1) {
      this.headRotationLowerBound = var1;
   }

   public float getMinBodyAngle() {
      return this.bodyRotationLowerBound;
   }

   public void setMinBodyAngle(float var1) {
      this.bodyRotationLowerBound = var1;
   }

   public boolean isPlayerMode() {
      return this.humanoidBehavior;
   }

   public void setPlayerMode(boolean var1) {
      this.humanoidBehavior = var1;
   }

   public float getStableAngle() {
      return this.stabilityThreshold;
   }

   public void setStableAngle(float var1) {
      this.stabilityThreshold = var1;
   }

   public int getRotationDelay() {
      return this.stabilizationDelay;
   }

   public void setRotationDelay(int var1) {
      this.stabilizationDelay = var1;
   }

   public int getRotationDuration() {
      return this.transitionPeriod;
   }

   public void setRotationDuration(int var1) {
      this.transitionPeriod = var1;
   }

   public void save(SavedData var1) {
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

   public void load(SavedData var1) {
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

   public float a(float var1, float var2, float var3) {
      float var4 = MathUtils.wrapDegree(var2 - var1);
      return var1 + MathUtils.clamp(var4, -var3, var3);
   }

   private void adjustBodyOrientation() {
      float var1 = this.asymmetricBodyLimits ? this.bodyRotationLowerBound : -this.bodyRotationUpperBound;
      this.targetEntity.br = MathUtils.rotateIfNecessary(this.targetEntity.br, this.targetEntity.bt, var1, this.bodyRotationUpperBound);
   }

   private void adjustHeadOrientation() {
      float var1 = this.asymmetricHeadLimits ? this.headRotationLowerBound : -this.headRotationUpperBound;
      this.targetEntity.bt = MathUtils.rotateIfNecessary(this.targetEntity.bt, this.targetEntity.br, var1, this.headRotationUpperBound);
   }

   private void smoothHeadAlignment() {
      float var1 = this.calculateAlignmentProgress();
      float var2 = this.headRotationUpperBound * (1.0F - var1);
      float var3 = this.calculateAdjustedLowerLimit(var1);
      this.targetEntity.br = MathUtils.rotateIfNecessary(this.targetEntity.br, this.targetEntity.bt, var3, var2);
   }

   private float calculateAlignmentProgress() {
      float var1 = (float)(this.timeSinceStable - this.stabilizationDelay);
      float var2 = var1 / (float)this.transitionPeriod;
      return MathUtils.clamp(var2, 0.0F, 1.0F);
   }

   private float calculateAdjustedLowerLimit(float var1) {
      float var2 = this.asymmetricHeadLimits ? this.headRotationLowerBound : -this.headRotationUpperBound;
      return var2 * (1.0F - var1);
   }

   private boolean isNotCarryingPassenger() {
      return !(this.targetEntity.db() instanceof EntityInsentient);
   }

   private boolean detectMovement() {
      double var1 = this.targetEntity.dC() - this.targetEntity.X;
      double var3 = this.targetEntity.dI() - this.targetEntity.Z;
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

   public void setAsymmetricHeadLimits(boolean var1) {
      this.asymmetricHeadLimits = var1;
   }

   public boolean isAsymmetricBodyLimits() {
      return this.asymmetricBodyLimits;
   }

   public void setAsymmetricBodyLimits(boolean var1) {
      this.asymmetricBodyLimits = var1;
   }

   public float getHeadRotationUpperBound() {
      return this.headRotationUpperBound;
   }

   public void setHeadRotationUpperBound(float var1) {
      this.headRotationUpperBound = var1;
   }

   public float getBodyRotationUpperBound() {
      return this.bodyRotationUpperBound;
   }

   public void setBodyRotationUpperBound(float var1) {
      this.bodyRotationUpperBound = var1;
   }

   public float getHeadRotationLowerBound() {
      return this.headRotationLowerBound;
   }

   public void setHeadRotationLowerBound(float var1) {
      this.headRotationLowerBound = var1;
   }

   public float getBodyRotationLowerBound() {
      return this.bodyRotationLowerBound;
   }

   public void setBodyRotationLowerBound(float var1) {
      this.bodyRotationLowerBound = var1;
   }

   public boolean isHumanoidBehavior() {
      return this.humanoidBehavior;
   }

   public void setHumanoidBehavior(boolean var1) {
      this.humanoidBehavior = var1;
   }

   public float getStabilityThreshold() {
      return this.stabilityThreshold;
   }

   public void setStabilityThreshold(float var1) {
      this.stabilityThreshold = var1;
   }

   public int getStabilizationDelay() {
      return this.stabilizationDelay;
   }

   public void setStabilizationDelay(int var1) {
      this.stabilizationDelay = var1;
   }

   public int getTransitionPeriod() {
      return this.transitionPeriod;
   }

   public void setTransitionPeriod(int var1) {
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
   public void setTimeSinceStable(int var1) {
      this.timeSinceStable = var1;
   }

   @Generated
   public void setPreviousStableHeadOrientation(float var1) {
      this.previousStableHeadOrientation = var1;
   }
}
