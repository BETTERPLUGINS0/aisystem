package advancedplugins.pm2.cv.vehicle.model.compound.animPlayer;

import advancedplugins.pm2.cv.api.enums.EnumInterpolationMode;
import advancedplugins.pm2.cv.api.vehicle.model.AnimationPlayer;
import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import advancedplugins.pm2.cv.vehicle.model.compound.Animation;
import advancedplugins.pm2.cv.vehicle.model.compound.CompoundModel;
import advancedplugins.pm2.cv.vehicle.model.compound.Part;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MatrixAnimationPlayer implements AnimationPlayer {
   private final CompoundModel model;
   private final Animation animation;
   private final boolean useInterpolation;

   public MatrixAnimationPlayer(CompoundModel model, Animation animation, EnumInterpolationMode interpolationMode) {
      this.model = var1;
      this.animation = var2;
      this.useInterpolation = var3 == EnumInterpolationMode.SMOOTH;
   }

   public void start() {
      this.model.getMetadataLinker().begin();

      for(int var1 = 0; var1 < this.animation.getParts().length; ++var1) {
         Part var2 = this.animation.getParts()[var1];
         Matrix4f var3 = this.animation.getFirstTransformations()[var1];
         var2.getDisplay().setProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DELAY, 0, false, false);
         var2.getDisplay().setProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DURATION, 0, false, true);
         var2.getDisplay().setTransformation(var3, true, false);
      }

      this.model.getMetadataLinker().complete();
   }

   public void stop() {
      this.model.getMetadataLinker().begin();
      Part[] var1 = this.animation.getParts();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Part var4 = var1[var3];
         var4.applyInitialTransformation();
      }

      this.model.getMetadataLinker().complete();
   }

   public void playFrame(int frameIndex) {
      int var3;
      if (var1 != 0) {
         --var1;
         if (var1 + 1 > this.animation.getKeyframes().length) {
            var1 = this.animation.getKeyframes().length - 1;
         }

         Animation.Keyframe var7 = this.animation.getKeyframes()[var1];
         this.model.getMetadataLinker().begin();

         for(var3 = 0; var3 < this.animation.getParts().length; ++var3) {
            Part var8 = this.animation.getParts()[var3];
            Matrix4f var9 = var7.getTransformations() != null ? var7.getTransformations()[var3] : null;
            if (var9 != null) {
               int var6 = this.useInterpolation ? Math.min(var7.getDuration(), 3) : 0;
               var8.getDisplay().setProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DELAY, 0, false, false);
               var8.getDisplay().setProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DURATION, var6, false, false);
               var8.getDisplay().setTransformation(var9, false, false);
            }
         }

         this.model.getMetadataLinker().complete();
      } else {
         this.model.getMetadataLinker().begin();
         Part[] var2 = this.animation.getParts();
         var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Part var5 = var2[var4];
            var5.applyInitialTransformation();
         }

         this.model.getMetadataLinker().complete();
      }
   }

   public void updateProgress(int frameIndex, float progress) {
      if (this.useInterpolation) {
         --var1;
         if (var1 >= 0) {
            Animation.Keyframe var3 = this.animation.getKeyframes()[var1];
            Animation.Keyframe var4 = var1 + 1 < this.animation.getKeyframes().length ? this.animation.getKeyframes()[var1 + 1] : var3;
            if (var3 != var4) {
               float var5 = this.easeInOutCubic(var2);
               this.model.getMetadataLinker().begin();

               for(int var6 = 0; var6 < this.animation.getParts().length; ++var6) {
                  Part var7 = this.animation.getParts()[var6];
                  Matrix4f var8 = var3.getTransformations() != null ? var3.getTransformations()[var6] : null;
                  Matrix4f var9 = var4.getTransformations() != null ? var4.getTransformations()[var6] : null;
                  if (var8 != null && var9 != null) {
                     Matrix4f var10 = this.interpolateMatrix(var8, var9, var5);
                     var7.getDisplay().setProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DELAY, 0, false, false);
                     var7.getDisplay().setProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DURATION, 1, false, false);
                     var7.getDisplay().setTransformation(var10, true, true);
                  }
               }

               this.model.getMetadataLinker().complete();
            }
         }
      }
   }

   private Matrix4f interpolateMatrix(Matrix4f from, Matrix4f to, float t) {
      Vector3f var4 = new Vector3f();
      Vector3f var5 = new Vector3f();
      Quaternionf var6 = new Quaternionf();
      Quaternionf var7 = new Quaternionf();
      Vector3f var8 = new Vector3f();
      Vector3f var9 = new Vector3f();
      var1.getTranslation(var4);
      var1.getUnnormalizedRotation(var6);
      var1.getScale(var8);
      var2.getTranslation(var5);
      var2.getUnnormalizedRotation(var7);
      var2.getScale(var9);
      Vector3f var10 = var4.lerp(var5, var3, new Vector3f());
      Quaternionf var11 = var6.slerp(var7, var3, new Quaternionf());
      Vector3f var12 = var8.lerp(var9, var3, new Vector3f());
      return (new Matrix4f()).translationRotateScale(var10, var11, var12);
   }

   private float easeInOutCubic(float t) {
      return var1 < 0.5F ? 4.0F * var1 * var1 * var1 : 1.0F - (float)Math.pow((double)(-2.0F * var1 + 2.0F), 3.0D) / 2.0F;
   }
}
