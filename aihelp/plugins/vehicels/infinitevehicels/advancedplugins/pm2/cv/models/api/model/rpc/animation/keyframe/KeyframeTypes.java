package advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.IPriorityHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.IStateMachineHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.interpolator.KeyframeInterpolator;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.interpolator.PrePostInterpolator;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.interpolator.ScriptInterpolator;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.ScriptKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.VectorKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.script.ScriptReader;
import advancedplugins.pm2.cv.models.api.utils.StepFlag;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public final class KeyframeTypes {
   public static final KeyframeType<VectorKeyframe, Vector3f> POSITION = KeyframeType.Builder.of("position", VectorKeyframe::new).interpolator((var0) -> {
      return new PrePostInterpolator((var0x, var1, var2, var3) -> {
         return standard(var0x, var1, var2, var3, StepFlag.POSITION);
      }, (var0x, var1) -> {
         markStep(var0x, var1, StepFlag.POSITION);
      }, KeyframeTypes::derive);
   }).registerJointUpdater(IPriorityHandler.class, (var0, var1, var2) -> {
      IAnimationProperty var3 = (IAnimationProperty)var2[0];
      Vector3f var4 = var1.getCachedPosition();
      Vector3f var5;
      if (var1.isRootJoint()) {
         if (var3.getPhase() == IAnimationProperty.Phase.PLAY) {
            var5 = var3.getVelocityFrame(var1);
            if (var5 == null) {
               var5 = new Vector3f();
            }

            if (!var3.isOverride()) {
               var4.add(var5);
            } else {
               var4.set(var5);
            }

            Vector3f var6 = var3.getPositionFrame(var1);
            var1.setJointOnGround(var6 == null || MathUtils.isSimilar(var6.y, 0.0F));
         }
      } else {
         var5 = var3.getPositionFrame(var1);
         if (!var3.isOverride()) {
            if (var5 == null) {
               var5 = new Vector3f();
            }

            switch(var3.getPhase()) {
            case LERPOUT:
               var5 = MathUtils.lerp(var5, new Vector3f(), var3.getLerpOutRatio());
               break;
            case LERPIN:
               var5 = MathUtils.lerp(new Vector3f(), var5, var3.getLerpInRatio());
            }

            var4.add(var5);
         } else if (var5 != null) {
            switch(var3.getPhase()) {
            case PLAY:
               var4.set(var5);
               break;
            case LERPOUT:
               var4.set(MathUtils.lerp(var5, var4, var3.getLerpOutRatio()));
               break;
            case LERPIN:
               var4.set(MathUtils.lerp(var4, var5, var3.getLerpInRatio()));
            }
         }
      }

   }).registerJointUpdater(IStateMachineHandler.class, (var0, var1, var2) -> {
      IAnimationProperty var3 = (IAnimationProperty)var2[0];
      if (var3 != null) {
         Vector3f var4;
         Vector3f var5;
         Vector3f var6;
         if (var1.isRootJoint()) {
            var4 = var1.getCachedPosition();
            if (var3.getPhase() == IAnimationProperty.Phase.PLAY) {
               var5 = var3.getVelocityFrame(var1);
               if (var5 == null) {
                  var5 = new Vector3f();
               }

               if (!var3.isOverride()) {
                  var4.add(var5);
               } else {
                  var4.set(var5);
               }

               var6 = var3.getPositionFrame(var1);
               var1.setJointOnGround(var6 == null || MathUtils.isSimilar(var6.y, 0.0F));
            }
         } else {
            var4 = new Vector3f(var1.getCachedPosition());
            var5 = var3.getPositionFrame(var1);
            if (var5 != null) {
               if (!var3.isOverride()) {
                  var4.add(var5);
               } else {
                  var4.set(var5);
               }
            }

            switch(var3.getPhase()) {
            case PLAY:
               var1.setCachedPosition(var4);
               return;
            case LERPOUT:
               var1.setCachedPosition(MathUtils.lerp(var4, var1.getCachedPosition(), var3.getLerpOutRatio()));
               return;
            default:
               IAnimationProperty var7 = (IAnimationProperty)var2[1];
               if (var7 == null) {
                  var1.setCachedPosition(MathUtils.lerp(var1.getCachedPosition(), var4, var3.getLerpInRatio()));
                  return;
               }

               var6 = new Vector3f(var1.getCachedPosition());
               Vector3f var8 = var7.getPositionFrame(var1);
               if (var8 != null) {
                  if (!var7.isOverride()) {
                     var6.add(var8);
                  } else {
                     var6.set(var8);
                  }
               }

               var1.setCachedPosition(MathUtils.lerp(var6, var4, var3.getLerpInRatio()));
            }
         }
      }

   }).build();
   public static final KeyframeType<VectorKeyframe, Vector3f> ROTATION = KeyframeType.Builder.of("rotation", VectorKeyframe::new).interpolator((var0) -> {
      return new PrePostInterpolator((var0x, var1, var2, var3) -> {
         return standard(var0x, var1, var2, var3, StepFlag.ROTATION);
      }, (var0x, var1) -> {
         markStep(var0x, var1, StepFlag.ROTATION);
      }, KeyframeTypes::derive);
   }).registerJointUpdater(IPriorityHandler.class, (var0, var1, var2) -> {
      IAnimationProperty var3 = (IAnimationProperty)var2[0];
      Vector3f var4 = var1.getCachedLeftRotation();
      Vector3f var5 = var3.getRotationFrame(var1);
      if (!var3.isOverride()) {
         if (var5 == null) {
            var5 = new Vector3f();
         }

         switch(var3.getPhase()) {
         case LERPOUT:
            var5 = MathUtils.slerp(var5, new Vector3f(), var3.getLerpOutRatio());
            break;
         case LERPIN:
            var5 = MathUtils.slerp(new Vector3f(), var5, var3.getLerpInRatio());
         }

         var4.add(var5);
      } else if (var5 != null) {
         switch(var3.getPhase()) {
         case PLAY:
            var4.set(var5);
            break;
         case LERPOUT:
            var4.set(MathUtils.slerp(var5, var4, var3.getLerpOutRatio()));
            break;
         case LERPIN:
            var4.set(MathUtils.slerp(var4, var5, var3.getLerpInRatio()));
         }
      }

   }).registerJointUpdater(IStateMachineHandler.class, (var0, var1, var2) -> {
      IAnimationProperty var3 = (IAnimationProperty)var2[0];
      if (var3 != null) {
         Vector3f var4 = new Vector3f(var1.getCachedLeftRotation());
         Vector3f var5 = var3.getRotationFrame(var1);
         if (var5 != null) {
            if (!var3.isOverride()) {
               var4.add(var5);
            } else {
               var4.set(var5);
            }
         }

         switch(var3.getPhase()) {
         case PLAY:
            var1.setCachedLeftRotation(var4);
            return;
         case LERPOUT:
            var1.setCachedLeftRotation(MathUtils.slerp(var4, var1.getCachedLeftRotation(), var3.getLerpOutRatio()));
            return;
         default:
            IAnimationProperty var6 = (IAnimationProperty)var2[1];
            if (var6 == null) {
               var1.setCachedLeftRotation(MathUtils.slerp(var1.getCachedLeftRotation(), var4, var3.getLerpInRatio()));
            } else {
               Vector3f var7 = new Vector3f(var1.getCachedLeftRotation());
               Vector3f var8 = var6.getRotationFrame(var1);
               if (var8 != null) {
                  if (!var6.isOverride()) {
                     var7.add(var8);
                  } else {
                     var7.set(var8);
                  }
               }

               var1.setCachedLeftRotation(MathUtils.slerp(var7, var4, var3.getLerpInRatio()));
            }
         }
      }

   }).build();
   public static final KeyframeType<VectorKeyframe, Vector3f> SCALE = KeyframeType.Builder.of("scale", VectorKeyframe::new).interpolator((var0) -> {
      return new PrePostInterpolator((var0x, var1, var2, var3) -> {
         return standard(var0x, var1, var2, var3, StepFlag.SCALE);
      }, (var0x, var1) -> {
         markStep(var0x, var1, StepFlag.SCALE);
      }, KeyframeTypes::derive);
   }).registerJointUpdater(IPriorityHandler.class, (var0, var1, var2) -> {
      IAnimationProperty var3 = (IAnimationProperty)var2[0];
      Vector3f var4 = var1.getCachedScale();
      Vector3f var5 = var3.getScaleFrame(var1);
      if (!var3.isOverride()) {
         if (var5 == null) {
            var5 = new Vector3f(1.0F);
         }

         switch(var3.getPhase()) {
         case LERPOUT:
            var5 = MathUtils.lerp(var5, new Vector3f(1.0F), var3.getLerpOutRatio());
            break;
         case LERPIN:
            var5 = MathUtils.lerp(new Vector3f(1.0F), var5, var3.getLerpInRatio());
         }

         var4.mul(var5);
      } else if (var5 != null) {
         switch(var3.getPhase()) {
         case PLAY:
            var4.set(var5);
            break;
         case LERPOUT:
            var4.set(MathUtils.lerp(var5, var4, var3.getLerpOutRatio()));
            break;
         case LERPIN:
            var4.set(MathUtils.lerp(var4, var5, var3.getLerpInRatio()));
         }
      }

   }).registerJointUpdater(IStateMachineHandler.class, (var0, var1, var2) -> {
      IAnimationProperty var3 = (IAnimationProperty)var2[0];
      if (var3 != null) {
         Vector3f var4 = new Vector3f(var1.getCachedScale());
         Vector3f var5 = var3.getScaleFrame(var1);
         if (var5 != null) {
            if (!var3.isOverride()) {
               var4.mul(var5);
            } else {
               var4.set(var5);
            }
         }

         switch(var3.getPhase()) {
         case PLAY:
            var1.setCachedScale(var4);
            return;
         case LERPOUT:
            var1.setCachedScale(MathUtils.lerp(var4, var1.getCachedScale(), var3.getLerpOutRatio()));
            return;
         default:
            IAnimationProperty var6 = (IAnimationProperty)var2[1];
            if (var6 == null) {
               var1.setCachedScale(MathUtils.lerp(var1.getCachedScale(), var4, var3.getLerpInRatio()));
            } else {
               Vector3f var7 = new Vector3f(var1.getCachedScale());
               Vector3f var8 = var6.getScaleFrame(var1);
               if (var8 != null) {
                  if (!var6.isOverride()) {
                     var7.mul(var8);
                  } else {
                     var7.set(var8);
                  }
               }

               var1.setCachedScale(MathUtils.lerp(var7, var4, var3.getLerpInRatio()));
            }
         }
      }

   }).build();
   public static final KeyframeType<ScriptKeyframe, List<ScriptKeyframe.Script>> SCRIPT = KeyframeType.Builder.of("script", ScriptKeyframe::new).interpolator((var0) -> {
      return new ScriptInterpolator(ArrayList::new, List::addAll);
   }).registerModelUpdater(IPriorityHandler.class, KeyframeTypes::standardScript).registerModelUpdater(IStateMachineHandler.class, KeyframeTypes::standardScript).global().build();

   private static Vector3f standard(KeyframeInterpolator.Context<VectorKeyframe, Vector3f> var0, Vector3f var1, Vector3f var2, float var3, StepFlag var4) {
      String var5 = ((VectorKeyframe)var0.interpolator.get(var0.prevKey)).getInterpolation();
      if (var5.equals("step")) {
         var0.joint.markStep(var4);
         return var1;
      } else {
         VectorKeyframe var6 = (VectorKeyframe)var0.interpolator.get(var0.nextKey);
         if (var6.isDiscontinuous() && var0.property.getTime() + var0.property.getSpeed() * 0.05D > (double)var0.nextKey) {
            var0.joint.markStep(var4);
         }

         String var7 = var6.getInterpolation();
         if (!var5.equals("catmullrom") && !var7.equals("catmullrom")) {
            return var1.lerp(var2, var3, new Vector3f());
         } else {
            float var8 = var0.interpolator.getHigherKey(var0.nextKey);
            float var9 = var0.interpolator.getLowerKey(var0.prevKey);
            VectorKeyframe var10 = (VectorKeyframe)var0.interpolator.get(var8);
            VectorKeyframe var11 = (VectorKeyframe)var0.interpolator.get(var9);
            return MathUtils.smoothLerp(var11.getValue(0, var0.property), var1, var2, var10.getValue(0, var0.property), var3);
         }
      }
   }

   private static void markStep(KeyframeInterpolator.Context<VectorKeyframe, Vector3f> var0, VectorKeyframe var1, StepFlag var2) {
      if ("step".equals(var1.getInterpolation()) || var1.isDiscontinuous()) {
         var0.joint.markStep(var2);
      }

   }

   private static Vector3f derive(@Nullable Vector3f var0, @Nullable Vector3f var1) {
      return var0 != null && var1 != null ? var1.sub(var0, new Vector3f()) : new Vector3f();
   }

   private static void standardScript(AnimationHandler var0, IVisualModel var1, Object... var2) {
      IAnimationProperty var3 = (IAnimationProperty)var2[0];
      List var4 = var3.getScriptFrame();
      if (var4 != null && !var4.isEmpty()) {
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            ScriptKeyframe.Script var6 = (ScriptKeyframe.Script)var5.next();
            ModelAPI.getAPI();
            ScriptReader var7 = (ScriptReader)ModelAPI.getScriptReaderArchive().get(var6.reader());
            if (var7 != null) {
               var7.read(var3, var6.script());
            }
         }
      }

   }
}
