package advancedplugins.pm2.cv.models.api.model.rpc.joint.manager;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import java.util.Optional;
import lombok.Generated;

public abstract class AbstractBehaviorManager<T extends JointAction> implements BehaviorManager<T> {
   protected final IVisualModel visualModel;
   protected final JointActionType<T> type;

   protected Optional<T> getJointBehavior(String var1) {
      Optional var2 = this.getActiveModel().getJoint(var1);
      return var2.isEmpty() ? Optional.empty() : ((IJoint)var2.get()).getJointAction(this.getType());
   }

   public IVisualModel getActiveModel() {
      return this.visualModel;
   }

   public JointActionType<T> getType() {
      return this.type;
   }

   @Generated
   public IVisualModel getVisualModel() {
      return this.visualModel;
   }

   @Generated
   public AbstractBehaviorManager(IVisualModel var1, JointActionType<T> var2) {
      this.visualModel = var1;
      this.type = var2;
   }
}
