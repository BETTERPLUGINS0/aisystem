package advancedplugins.pm2.cv.models.api.model.rpc.joint.manager;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;

public interface BehaviorManager<T extends JointAction> {
   IVisualModel getActiveModel();

   JointActionType<T> getType();

   default void onCreate() {
   }

   default void onDestroy() {
   }

   default void preJointTick() {
   }

   default void postJointTick() {
   }

   default void preScriptTick() {
   }

   default void postScriptTick() {
   }

   default void preJointRender() {
   }

   default void postJointRenderer() {
   }
}
