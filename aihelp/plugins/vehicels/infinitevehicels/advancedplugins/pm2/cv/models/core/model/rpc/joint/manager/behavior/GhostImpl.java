package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.JointItems;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.AbstractJointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Ghost;

public class GhostImpl extends AbstractJointAction<GhostImpl> implements Ghost {
   public GhostImpl(IJoint var1, JointActionType<GhostImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
   }

   public void onApply() {
      this.joint.setRenderer(true);
      this.joint.clearModel();
   }

   public JointItems getModel() {
      return this.joint.getModels();
   }
}
