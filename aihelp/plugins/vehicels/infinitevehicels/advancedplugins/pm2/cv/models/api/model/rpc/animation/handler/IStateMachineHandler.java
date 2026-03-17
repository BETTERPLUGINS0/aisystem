package advancedplugins.pm2.cv.models.api.model.rpc.animation.handler;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import org.jetbrains.annotations.Nullable;

public interface IStateMachineHandler extends AnimationHandler {
   @Nullable
   IAnimationProperty getAnimation(int var1, String var2);

   @Nullable
   IAnimationProperty playAnimation(int var1, String var2, double var3, double var5, double var7, boolean var9);

   boolean playAnimation(int var1, IAnimationProperty var2, boolean var3);

   void refreshState(AnimationHandler.DefaultProperty var1);

   boolean isPlayingAnimation(int var1, String var2);

   void stopAnimation(int var1, String var2);

   void forceStopAnimation(int var1, String var2);

   default String getId() {
      return "state_machine";
   }
}
