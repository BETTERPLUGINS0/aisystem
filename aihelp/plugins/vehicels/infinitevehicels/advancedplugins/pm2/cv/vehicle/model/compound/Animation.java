package advancedplugins.pm2.cv.vehicle.model.compound;

import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.AnimationConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.AnimationKeyframeConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.BoneConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.CompoundModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.PartConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.RigConfiguration;
import advancedplugins.pm2.cv.util.ConvertUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Animation {
   final CompoundModel model;
   final RigConfiguration rig;
   final AnimationConfiguration configuration;
   final List<AnimationKeyframeConfiguration> sourceKeyframes = new ArrayList();
   final Animation.Keyframe[] keyframes;
   final int endDelay;
   final Part[] parts;
   final Matrix4f[] firstTransformations;

   Animation(@NotNull AnimationConfiguration configuration, @NotNull CompoundModel model) {
      this.model = var2;
      this.rig = ((CompoundModelConfiguration)var2.getConfiguration()).getRig();
      this.configuration = var1;
      if (this.rig == null) {
         throw new IllegalStateException("rig cannot be null");
      } else {
         this.sourceKeyframes.addAll(var1.getKeyframes());
         this.keyframes = new Animation.Keyframe[FastMath.max(this.sourceKeyframes.size() - 1, 0)];
         if (this.sourceKeyframes.size() > 0) {
            this.endDelay = ((AnimationKeyframeConfiguration)this.sourceKeyframes.get(this.sourceKeyframes.size() - 1)).getDuration();
         } else {
            this.endDelay = 0;
         }

         ArrayList var3 = new ArrayList();
         Iterator var4 = var2.parts.iterator();

         while(var4.hasNext()) {
            Part var5 = (Part)var4.next();
            boolean var6 = this.isAnimated(var5);
            if (var6) {
               var3.add(var5);
            }
         }

         this.parts = (Part[])var3.toArray(new Part[0]);
         this.firstTransformations = new Matrix4f[this.parts.length];
         this.compileKeyframes();
      }
   }

   private void compileKeyframes() {
      RigConfiguration var1 = ((CompoundModelConfiguration)this.model.getConfiguration()).getRig();
      if (var1 != null) {
         int var2;
         for(var2 = 0; var2 < this.keyframes.length; ++var2) {
            this.keyframes[var2] = new Animation.Keyframe(((AnimationKeyframeConfiguration)this.sourceKeyframes.get(var2)).getDuration(), (AnimationKeyframeConfiguration)this.sourceKeyframes.get(var2));
         }

         for(var2 = 0; var2 < this.parts.length; ++var2) {
            Part var3 = this.parts[var2];
            Vector3f var4 = null;
            Quaternionf var5 = null;
            Vector3f var6 = null;
            Vector3f var7 = null;
            Quaternionf var8 = null;
            Vector3f var9 = null;

            for(int var10 = 0; var10 < this.keyframes.length; ++var10) {
               Animation.Keyframe var11 = this.keyframes[var10];
               AnimationKeyframeConfiguration var12 = (AnimationKeyframeConfiguration)this.sourceKeyframes.get(var10);
               Vector3f var13 = this.calculateTranslation(var3, var12);
               Quaternionf var14 = this.calculateRotation(var3, var12);
               Vector3f var15 = this.calculateScale(var3, var12);
               if (var4 == null) {
                  var4 = var13;
               }

               if (var5 == null) {
                  var5 = var14;
               }

               if (var6 == null) {
                  var6 = var15;
               }

               AnimationKeyframeConfiguration var16 = (AnimationKeyframeConfiguration)this.sourceKeyframes.get(var10 + 1);
               Vector3f var17 = this.calculateTranslation(var3, var16);
               Quaternionf var18 = this.calculateRotation(var3, var16);
               Vector3f var19 = this.calculateScale(var3, var16);
               boolean var20 = !Objects.equals(var17, var7);
               boolean var21 = !Objects.equals(var18, var8);
               boolean var22 = !Objects.equals(var19, var9);
               if (var20 || var21 || var22) {
                  if (var11.transformations == null) {
                     var11.transformations = new Matrix4f[this.parts.length];
                  }

                  var11.transformations[var2] = this.composePartTransformation(var17, var18, var19);
               }

               var11.configuration = var16;
               if (var20) {
                  var7 = var17;
               }

               if (var21) {
                  var8 = var18;
               }

               if (var22) {
                  var9 = var19;
               }
            }

            this.firstTransformations[var2] = this.composePartTransformation(var4, var5, var6);
         }

      }
   }

   private Matrix4f composePartTransformation(Vector3f translation, Quaternionf rotation, Vector3f scale) {
      Matrix4f var4 = new Matrix4f();
      var4.translation(var1);
      var4.rotate(var2);
      var4.scale(var3);
      return var4;
   }

   private Vector3f calculateTranslation(Part part, AnimationKeyframeConfiguration keyframe) {
      BoneConfiguration var3 = (BoneConfiguration)Objects.requireNonNull(this.rig.getParent(var1.configuration));
      Vector3D var4 = var1.configuration.getOffset();
      Vector3D var5 = var3.getPivot();
      Matrix4f var6 = new Matrix4f();
      var6.translate(ConvertUtil.toVector3f(this.calculatePivot(var3, var2)));
      var6.rotate(this.calculateRotation(var3, var2));
      Vector3D var7 = RigMath.mulProject((var4 != null ? var4 : Vector3D.ZERO).subtract(var5), var6);
      Iterator var8 = this.rig.getHierarchyUp(var3).iterator();

      while(var8.hasNext()) {
         BoneConfiguration var9 = (BoneConfiguration)var8.next();
         Vector3D var10 = this.getTranslationAt(var9, var2);
         if (var10 != null) {
            var7 = var7.add(var10);
         }
      }

      return ConvertUtil.toVector3f(var7);
   }

   private Quaternionf calculateRotation(Part part, AnimationKeyframeConfiguration keyframe) {
      BoneConfiguration var3 = (BoneConfiguration)Objects.requireNonNull(this.rig.getParent(var1.configuration));
      Quaternionf var4 = this.calculateRotation(var3, var2);
      Vector3D var5 = var1.configuration.getRotation();
      if (var5 != null) {
         RigMath.rotateXYZ(var4, var5);
      }

      return var4;
   }

   private Vector3f calculateScale(Part part, AnimationKeyframeConfiguration keyframe) {
      BoneConfiguration var3 = (BoneConfiguration)Objects.requireNonNull(this.rig.getParent(var1.configuration));
      Vector3D var4 = var1.configuration.getScale();
      if (var4 == null) {
         var4 = new Vector3D(1.0D, 1.0D, 1.0D);
      }

      Iterator var5 = this.rig.getHierarchyUp(var3).iterator();

      while(var5.hasNext()) {
         BoneConfiguration var6 = (BoneConfiguration)var5.next();
         Vector3D var7 = this.getScaleAt(var6, var2);
         if (var7 != null) {
            var4 = new Vector3D(var4.getX() * var7.getX(), var4.getY() * var7.getY(), var4.getZ() * var7.getZ());
         }
      }

      return ConvertUtil.toVector3f(var4);
   }

   private Vector3D calculatePivot(BoneConfiguration bone, AnimationKeyframeConfiguration keyframe) {
      Vector3D var3 = var1.getPivot();
      BoneConfiguration var4 = this.rig.contains(var1) ? this.rig.getParent(var1) : null;
      if (var4 == null) {
         return var3;
      } else {
         Vector3D var5 = var4.getPivot();
         Matrix4f var6 = new Matrix4f();
         var6.translate(ConvertUtil.toVector3f(this.calculatePivot(var4, var2)));
         var6.rotate(this.calculateRotation(var4, var2));
         return RigMath.mulProject(var3.subtract(var5), var6);
      }
   }

   private Quaternionf calculateRotation(BoneConfiguration bone, AnimationKeyframeConfiguration keyframe) {
      Quaternionf var3 = new Quaternionf();
      Iterator var4 = this.rig.getHierarchyUp(var1).iterator();

      while(var4.hasNext()) {
         BoneConfiguration var5 = (BoneConfiguration)var4.next();
         Vector3D var6 = var5.getRotation();
         if (var6 != null) {
            RigMath.rotateXYZ(var3, var6);
         }

         Vector3D var7 = this.getRotationAt(var5, var2);
         if (var7 != null) {
            RigMath.rotateXYZ(var3, var7);
         }
      }

      return var3;
   }

   @Nullable
   private Vector3D getTranslationAt(BoneConfiguration bone, AnimationKeyframeConfiguration keyframe) {
      Vector3D var3 = var2.getTranslation(var1);
      if (var3 != null) {
         return var3;
      } else {
         for(int var4 = this.sourceKeyframes.indexOf(var2) - 1; var4 >= 0; --var4) {
            AnimationKeyframeConfiguration var5 = (AnimationKeyframeConfiguration)this.sourceKeyframes.get(var4);
            if ((var3 = var5.getTranslation(var1)) != null) {
               break;
            }
         }

         return var3;
      }
   }

   @Nullable
   private Vector3D getRotationAt(BoneConfiguration bone, AnimationKeyframeConfiguration keyframe) {
      Vector3D var3 = var2.getRotation(var1);
      if (var3 != null) {
         return var3;
      } else {
         for(int var4 = this.sourceKeyframes.indexOf(var2) - 1; var4 >= 0; --var4) {
            AnimationKeyframeConfiguration var5 = (AnimationKeyframeConfiguration)this.sourceKeyframes.get(var4);
            if ((var3 = var5.getRotation(var1)) != null) {
               break;
            }
         }

         return var3;
      }
   }

   @Nullable
   private Vector3D getScaleAt(BoneConfiguration bone, AnimationKeyframeConfiguration keyframe) {
      Vector3D var3 = var2.getScale(var1);
      if (var3 != null) {
         return var3;
      } else {
         for(int var4 = this.sourceKeyframes.indexOf(var2) - 1; var4 >= 0; --var4) {
            AnimationKeyframeConfiguration var5 = (AnimationKeyframeConfiguration)this.sourceKeyframes.get(var4);
            if ((var3 = var5.getScale(var1)) != null) {
               break;
            }
         }

         return var3;
      }
   }

   private boolean isAnimated(Part part) {
      Iterator var2 = this.sourceKeyframes.iterator();

      AnimationKeyframeConfiguration var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (AnimationKeyframeConfiguration)var2.next();
      } while(!this.isAnimated(var1, var3));

      return true;
   }

   private boolean isAnimated(Part part, AnimationKeyframeConfiguration keyframe) {
      PartConfiguration var3 = var1.configuration;
      BoneConfiguration var4 = this.rig.contains(var3) ? this.rig.getParent(var3) : null;
      return var4 != null && this.isAnimated(var4, var2);
   }

   private boolean isAnimated(BoneConfiguration bone, AnimationKeyframeConfiguration keyframe) {
      if (var2.getTranslation(var1) == null && var2.getRotation(var1) == null && var2.getScale(var1) == null) {
         if (this.rig.contains(var1)) {
            Iterator var3 = this.rig.getAncestors(var1).iterator();

            while(var3.hasNext()) {
               BoneConfiguration var4 = (BoneConfiguration)var3.next();
               if (var2.getTranslation(var4) != null || var2.getRotation(var4) != null || var2.getScale(var4) != null) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public CompoundModel getModel() {
      return this.model;
   }

   public RigConfiguration getRig() {
      return this.rig;
   }

   public AnimationConfiguration getConfiguration() {
      return this.configuration;
   }

   public List<AnimationKeyframeConfiguration> getSourceKeyframes() {
      return this.sourceKeyframes;
   }

   public Animation.Keyframe[] getKeyframes() {
      return this.keyframes;
   }

   public int getEndDelay() {
      return this.endDelay;
   }

   public Part[] getParts() {
      return this.parts;
   }

   public Matrix4f[] getFirstTransformations() {
      return this.firstTransformations;
   }

   public static class Keyframe {
      final int duration;
      Matrix4f[] transformations = null;
      AnimationKeyframeConfiguration configuration;

      public Keyframe(int duration, AnimationKeyframeConfiguration configuration) {
         this.duration = var1;
         this.configuration = var2;
      }

      public int getDuration() {
         return this.duration;
      }

      public Matrix4f[] getTransformations() {
         return this.transformations;
      }

      public AnimationKeyframeConfiguration getConfiguration() {
         return this.configuration;
      }
   }
}
