package advancedplugins.pm2.cv.models.api.model.rpc;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.rootmotion.RootMotionHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.GlobalBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountData;
import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IModelContainer extends DataIO, Tickable {
   BaseEntity<?> getBase();

   AnimationLODHandler getAnimationLodHandler();

   IPosition getPosition();

   void destroy();

   boolean isInitialized();

   boolean isDestroyed();

   void markRemoved();

   void restore();

   void queuePostInitTask(Runnable var1);

   boolean isBaseEntityVisible();

   void setBaseEntityVisible(boolean var1);

   void markHurt();

   int getHurtTick();

   boolean shouldBeSaved();

   void setSaved(boolean var1);

   boolean isModelRotationLocked();

   void setModelRotationLocked(boolean var1);

   boolean isGlowing();

   int getGlowColor();

   float getYHeadRot();

   float getXHeadRot();

   float getYBodyRot();

   Optional<IVisualModel> addModel(@NotNull IVisualModel var1, boolean var2);

   Optional<IVisualModel> removeModel(String var1);

   Optional<IVisualModel> getModel(@Nullable String var1);

   Map<String, IVisualModel> getModels();

   <T extends JointAction> GlobalBehaviorData getOrCreateGlobalBehaviorData(JointActionType<T> var1, Supplier<GlobalBehaviorData> var2);

   <T extends JointAction> GlobalBehaviorData getGlobalBehaviorData(JointActionType<T> var1);

   <T extends JointAction> GlobalBehaviorData removeGlobalBehaviorData(JointActionType<T> var1);

   Map<JointActionType<?>, GlobalBehaviorData> getAllGlobalBehaviorData(JointActionType<?> var1);

   <T extends GlobalBehaviorData & MountData> T getMountData();

   RootMotionHandler getRootMotionHandler();

   UUID registerTickTask(IModelContainer.Phase var1, Function<IModelContainer, Boolean> var2);

   UUID registerTickTask(IModelContainer.Phase var1, Consumer<IModelContainer> var2);

   void removeTickTask(UUID var1);

   void runTickTasks(IModelContainer.Phase var1);

   default void registerSelf() {
      ModelAPI.getAPI().getModelUpdaters().registerModeledEntity(this.getBase(), this).ifPresent(IModelContainer::destroy);
      this.getBase().registerData();
   }

   public static enum Phase {
      PRE_DATA_SYNC,
      PRE_MODEL_TICK,
      PRE_MODEL_RENDER,
      POST_MODEL_RENDER;

      private static IModelContainer.Phase[] $values() {
         return new IModelContainer.Phase[]{PRE_DATA_SYNC, PRE_MODEL_TICK, PRE_MODEL_RENDER, POST_MODEL_RENDER};
      }

      // $FF: synthetic method
      private static IModelContainer.Phase[] $values$() {
         return new IModelContainer.Phase[]{PRE_DATA_SYNC, PRE_MODEL_TICK, PRE_MODEL_RENDER, POST_MODEL_RENDER};
      }
   }
}
