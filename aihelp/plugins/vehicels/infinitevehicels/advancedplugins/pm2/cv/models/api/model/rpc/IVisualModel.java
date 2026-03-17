package advancedplugins.pm2.cv.models.api.model.rpc;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.LeashManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Leash;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.utils.callback.ExecutionCallback;
import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Color;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

public interface IVisualModel extends DataIO, Tickable {
   IModelContainer getModeledEntity();

   void setModeledEntity(IModelContainer var1);

   ModelBlueprint getBlueprint();

   ModelRenderer getModelRenderer();

   AnimationHandler getAnimationHandler();

   Map<String, IJoint> getJoints();

   Map<JointActionType<?>, BehaviorManager<?>> getBehaviorManagers();

   Map<JointActionType<?>, BehaviorRenderer> getBehaviorRenderers();

   boolean isMainHitbox();

   void setMainHitbox(boolean var1);

   Vector3fc getScale();

   void setScale(double var1);

   Vector3fc getHitboxScale();

   void setHitboxScale(double var1);

   void destroy();

   boolean isDestroyed();

   boolean isRemoved();

   void setRemoved(boolean var1);

   void setAutoRendererInitialization(boolean var1);

   boolean isHitboxVisible();

   void setHitboxVisible(boolean var1);

   boolean isShadowVisible();

   void setShadowVisible(boolean var1);

   void initializeRenderer();

   void generateModel();

   void forceGenerateJoint(String var1, String var2, BlueprintJoint var3);

   void removeJoint(String var1);

   ExecutionCallback<IVisualModel.Scale> getScaleCallback();

   void setCanHurt(boolean var1);

   boolean canHurt();

   Color getDefaultTint();

   void setDefaultTint(Color var1);

   Color getDamageTint();

   void setDamageTint(Color var1);

   boolean wasMarkedHurt();

   boolean isMarkedHurt();

   boolean isGlowing();

   void setGlowing(@Nullable Boolean var1);

   int getGlowColor();

   void setGlowColor(@Nullable Integer var1);

   int getBlockLight();

   void setBlockLight(int var1);

   int getSkyLight();

   void setSkyLight(int var1);

   float getXHeadRot();

   float getYHeadRot();

   boolean isLockPitch();

   void setLockPitch(boolean var1);

   boolean isLockYaw();

   void setLockYaw(boolean var1);

   default Optional<IJoint> getJoint(String jointId) {
      return Optional.ofNullable((IJoint)this.getJoints().get(jointId));
   }

   <T extends JointAction> Optional<BehaviorManager<T>> getBehaviorManager(JointActionType<T> var1);

   Optional<BehaviorRenderer> getBehaviorRenderer(JointActionType<?> var1);

   default <T extends MountManager & BehaviorManager<? extends Mount>> Optional<T> getMountManager() {
      Optional maybe = this.getBehaviorManager(JointBehaviorTypes.MOUNT);
      return maybe.map((behaviorManager) -> {
         return (MountManager)behaviorManager;
      });
   }

   default <T extends LeashManager & BehaviorManager<? extends Leash>> Optional<T> getLeashManager() {
      Optional maybe = this.getBehaviorManager(JointBehaviorTypes.LEASH);
      return maybe.map((behaviorManager) -> {
         return (LeashManager)behaviorManager;
      });
   }

   @FunctionalInterface
   public interface Scale {
      void onScale(IVisualModel var1, double var2);
   }
}
