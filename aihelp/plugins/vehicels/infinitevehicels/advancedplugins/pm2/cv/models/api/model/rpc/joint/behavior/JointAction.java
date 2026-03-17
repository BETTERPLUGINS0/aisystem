package advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import org.jetbrains.annotations.Nullable;

public interface JointAction extends DataIO {
   IJoint getJoint();

   JointActionType<?> getType();

   JointBehaviorData getData();

   default void onApply() {
   }

   default void onRemove() {
   }

   default void onParentSwap(@Nullable IJoint parent) {
   }

   default void preAnimation() {
   }

   default void onAnimation() {
   }

   default void postAnimation() {
   }

   default void preGlobalCalculation() {
   }

   default void onGlobalCalculation() {
   }

   default void postGlobalCalculation() {
   }

   default float onUpdateYaw(float yaw) {
      return yaw;
   }

   default void preChildCalculation() {
   }

   default void postChildCalculation() {
   }

   default void onFinalize() {
   }

   default void preRender() {
   }

   default void onRender() {
   }

   default void postRender() {
   }

   default boolean isHidden() {
      return false;
   }

   default void save(SavedData data) {
   }

   default void load(SavedData data) {
   }
}
