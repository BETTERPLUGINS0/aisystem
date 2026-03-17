package advancedplugins.pm2.cv.vehicle.model.compound.animPlayer;

import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.AnimationKeyframeConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.compound.CompoundModelConfiguration;
import advancedplugins.pm2.cv.api.vehicle.model.AnimationPlayer;
import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import advancedplugins.pm2.cv.fake.display.FakeDisplayHandle;
import advancedplugins.pm2.cv.vehicle.model.compound.Animation;
import advancedplugins.pm2.cv.vehicle.model.compound.CompoundModel;
import advancedplugins.pm2.cv.vehicle.model.compound.Part;
import java.util.UUID;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BlockBenchAnimationPlayer implements AnimationPlayer {
   private final CompoundModel model;
   private final Animation animation;
   private int lastFrameIndex = -1;

   public BlockBenchAnimationPlayer(CompoundModel model, Animation animation) {
      this.model = var1;
      this.animation = var2;
   }

   public void start() {
      this.model.getMetadataLinker().begin();
      Part[] var1 = this.animation.getParts();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Part var4 = var1[var3];
         Animation.Keyframe var5 = this.animation.getKeyframes()[0];
         this.applyBlockBenchTransform(var4, var5.getConfiguration(), true);
      }

      this.model.getMetadataLinker().complete();
      this.lastFrameIndex = 0;
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
      this.lastFrameIndex = -1;
   }

   public void playFrame(int frameIndex) {
      if (var1 >= this.animation.getKeyframes().length) {
         var1 = this.animation.getKeyframes().length - 1;
      }

      Animation.Keyframe var2 = this.animation.getKeyframes()[var1];
      this.model.getMetadataLinker().begin();
      Part[] var3 = this.animation.getParts();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Part var6 = var3[var5];
         UUID var7 = this.animation.getRig().getParent(var6.getConfiguration()).getIdentifier();
         Quaternionf var8 = (Quaternionf)var2.getConfiguration().getBlockBenchRotations().get(var7);
         if (var8 != null) {
         }

         this.applyBlockBenchTransform(var6, var2.getConfiguration(), false);
      }

      this.model.getMetadataLinker().complete();
   }

   public void updateProgress(int frameIndex, float progress) {
   }

   private void applyBlockBenchTransform(Part part, AnimationKeyframeConfiguration config, boolean isFirst) {
      this.applyBlockBenchTransform(var1, var2, var3, false);
   }

   private void applyBlockBenchTransform(Part part, AnimationKeyframeConfiguration config, boolean isFirst, boolean isLooping) {
      UUID var5 = this.animation.getRig().getParent(var1.getConfiguration()).getIdentifier();
      if (!var3 || ((CompoundModelConfiguration)var1.getModel().getConfiguration()).getModelID() == null) {
         Quaternionf var6 = (Quaternionf)var2.getBlockBenchRotations().get(var5);
         Vector3f var7 = (Vector3f)var2.getBlockBenchPositions().get(var5);
         Vector3f var8 = (Vector3f)var2.getBlockBenchScales().get(var5);
         if (var6 != null || var7 != null || var8 != null) {
            Transformation var9 = this.buildTransformation(var6, var7, var8);
            int var10 = this.calculateInterpolationDuration(var3, var4);
            ((FakeDisplayHandle)var1.getDisplay().handle).applyProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DURATION, var10);
            ((FakeDisplayHandle)var1.getDisplay().handle).applyProperty(EnumDisplayProperty.TRANSFORM_INTERPOLATION_DELAY, 5);
            ((FakeDisplayHandle)var1.getDisplay().handle).applyProperty(EnumDisplayProperty.TRANSFORMATION, var9);
            var1.getDisplay().sendTransformation(true);
         }
      }
   }

   private Transformation buildTransformation(Quaternionf rotation, Vector3f position, Vector3f scale) {
      Transformation var4 = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1.0F, 1.0F, 1.0F), new Quaternionf());
      if (var1 != null) {
         Quaternionf var5 = (new Quaternionf(var1)).rotateY(3.1415927F);
         var4.getLeftRotation().set(var5);
      }

      if (var3 != null) {
         var4.getScale().set(var3);
      }

      if (var2 != null) {
         Vector3f var6 = new Vector3f(var2);
         var6.add(0.0F, ((CompoundModelConfiguration)this.model.getConfiguration()).getModelOffset() - 0.01F, 0.0F);
         var4.getTranslation().set(var6);
      }

      return var4;
   }

   private int calculateInterpolationDuration(boolean isFirst, boolean isLooping) {
      if (var1) {
         return 0;
      } else {
         return this.model.getAnimationState() != VehicleState.IDLE ? ((CompoundModelConfiguration)this.model.getConfiguration()).getModelInterpolation() : 3;
      }
   }
}
