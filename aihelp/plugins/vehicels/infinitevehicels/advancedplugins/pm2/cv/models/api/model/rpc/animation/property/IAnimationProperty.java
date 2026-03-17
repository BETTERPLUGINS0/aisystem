package advancedplugins.pm2.cv.models.api.model.rpc.animation.property;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.BlueprintAnimation;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.KeyframeType;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type.ScriptKeyframe;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public interface IAnimationProperty extends DataIO {
   BlueprintAnimation getBlueprintAnimation();

   boolean update();

   void stop();

   boolean isEnded();

   boolean canReplace();

   String getName();

   boolean containsKeyframe(KeyframeType<?, ?> var1, UUID var2);

   Vector3f getPositionFrame(IJoint var1);

   Vector3f getVelocityFrame(IJoint var1);

   Vector3f getRotationFrame(IJoint var1);

   Vector3f getScaleFrame(IJoint var1);

   List<ScriptKeyframe.Script> getScriptFrame();

   double getLerpInRatio();

   double getLerpOutRatio();

   boolean isFinished();

   IVisualModel getModel();

   double getLerpIn();

   double getLerpOut();

   double getLerpInTime();

   void setLerpInTime(double var1);

   double getLerpOutTime();

   void setLerpOutTime(double var1);

   double getLastTime();

   double getTime();

   double getSpeed();

   void setSpeed(double var1);

   @NotNull
   IAnimationProperty.Phase getPhase();

   BlueprintAnimation.LoopMode getForceLoopMode();

   void setForceLoopMode(BlueprintAnimation.LoopMode var1);

   BlueprintAnimation.LoopMode getLoopMode();

   boolean isOverride();

   boolean isForceOverride();

   void setForceOverride(boolean var1);

   boolean isSkipLastFrame();

   void setSkipLastFrame(boolean var1);

   public static enum Phase {
      LERPIN,
      PLAY,
      LERPOUT;

      private static IAnimationProperty.Phase[] $values() {
         return new IAnimationProperty.Phase[]{LERPIN, PLAY, LERPOUT};
      }

      // $FF: synthetic method
      private static IAnimationProperty.Phase[] $values$() {
         return new IAnimationProperty.Phase[]{LERPIN, PLAY, LERPOUT};
      }
   }
}
