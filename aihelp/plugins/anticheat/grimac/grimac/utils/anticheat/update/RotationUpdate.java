package ac.grim.grimac.utils.anticheat.update;

import ac.grim.grimac.checks.impl.aim.processor.AimProcessor;
import ac.grim.grimac.utils.data.HeadRotation;
import lombok.Generated;

public final class RotationUpdate {
   private HeadRotation from;
   private HeadRotation to;
   private AimProcessor processor;
   private float deltaYRot;
   private float deltaXRot;
   private boolean isCinematic;
   private double sensitivityX;
   private double sensitivityY;

   public RotationUpdate(HeadRotation from, HeadRotation to, float deltaXRot, float deltaYRot) {
      this.from = from;
      this.to = to;
      this.deltaXRot = deltaXRot;
      this.deltaYRot = deltaYRot;
   }

   public float getDeltaXRotABS() {
      return Math.abs(this.deltaXRot);
   }

   public float getDeltaYRotABS() {
      return Math.abs(this.deltaYRot);
   }

   @Generated
   public HeadRotation getFrom() {
      return this.from;
   }

   @Generated
   public HeadRotation getTo() {
      return this.to;
   }

   @Generated
   public AimProcessor getProcessor() {
      return this.processor;
   }

   @Generated
   public float getDeltaYRot() {
      return this.deltaYRot;
   }

   @Generated
   public float getDeltaXRot() {
      return this.deltaXRot;
   }

   @Generated
   public boolean isCinematic() {
      return this.isCinematic;
   }

   @Generated
   public double getSensitivityX() {
      return this.sensitivityX;
   }

   @Generated
   public double getSensitivityY() {
      return this.sensitivityY;
   }

   @Generated
   public void setFrom(HeadRotation from) {
      this.from = from;
   }

   @Generated
   public void setTo(HeadRotation to) {
      this.to = to;
   }

   @Generated
   public void setProcessor(AimProcessor processor) {
      this.processor = processor;
   }

   @Generated
   public void setDeltaYRot(float deltaYRot) {
      this.deltaYRot = deltaYRot;
   }

   @Generated
   public void setDeltaXRot(float deltaXRot) {
      this.deltaXRot = deltaXRot;
   }

   @Generated
   public void setCinematic(boolean isCinematic) {
      this.isCinematic = isCinematic;
   }

   @Generated
   public void setSensitivityX(double sensitivityX) {
      this.sensitivityX = sensitivityX;
   }

   @Generated
   public void setSensitivityY(double sensitivityY) {
      this.sensitivityY = sensitivityY;
   }
}
