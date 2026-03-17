package advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;

public abstract class AbstractJointAction<T extends JointAction> implements JointAction {
   protected final IJoint joint;
   protected final JointActionType<T> type;
   protected final JointBehaviorData data;

   public AbstractJointAction(IJoint var1, JointActionType<T> var2, JointBehaviorData var3) {
      this.joint = var1;
      this.type = var2;
      this.data = var3;
   }

   public IJoint getJoint() {
      return this.joint;
   }

   public JointActionType<T> getType() {
      return this.type;
   }

   public JointBehaviorData getData() {
      return this.data;
   }
}
