package advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractKeyframe<T> {
   protected final float[] leftTime = new float[3];
   protected final float[] leftValue = new float[3];
   protected final float[] rightTime = new float[3];
   protected final float[] rightValue = new float[3];
   @NotNull
   protected String interpolation = "";

   public boolean isBezier() {
      return "bezier".equalsIgnoreCase(this.interpolation);
   }

   public void setBezierLeftTime(@Nullable Float var1, @Nullable Float var2, @Nullable Float var3) {
      if (var1 != null) {
         this.leftTime[0] = var1;
      }

      if (var2 != null) {
         this.leftTime[1] = var2;
      }

      if (var3 != null) {
         this.leftTime[2] = var3;
      }

   }

   public void setBezierLeftValue(@Nullable Float var1, @Nullable Float var2, @Nullable Float var3) {
      if (var1 != null) {
         this.leftValue[0] = var1;
      }

      if (var2 != null) {
         this.leftValue[1] = var2;
      }

      if (var3 != null) {
         this.leftValue[2] = var3;
      }

   }

   public void setBezierRightTime(@Nullable Float var1, @Nullable Float var2, @Nullable Float var3) {
      if (var1 != null) {
         this.rightTime[0] = var1;
      }

      if (var2 != null) {
         this.rightTime[1] = var2;
      }

      if (var3 != null) {
         this.rightTime[2] = var3;
      }

   }

   public void setBezierRightValue(@Nullable Float var1, @Nullable Float var2, @Nullable Float var3) {
      if (var1 != null) {
         this.rightValue[0] = var1;
      }

      if (var2 != null) {
         this.rightValue[1] = var2;
      }

      if (var3 != null) {
         this.rightValue[2] = var3;
      }

   }

   public abstract T getValue(int var1, IAnimationProperty var2);

   public float[] getLeftTime() {
      return this.leftTime;
   }

   public float[] getLeftValue() {
      return this.leftValue;
   }

   public float[] getRightTime() {
      return this.rightTime;
   }

   public float[] getRightValue() {
      return this.rightValue;
   }

   @NotNull
   public String getInterpolation() {
      return this.interpolation;
   }

   public void setInterpolation(@NotNull String var1) {
      this.interpolation = var1;
   }
}
