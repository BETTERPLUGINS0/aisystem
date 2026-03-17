package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.GlobalBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;

public class MountDataImpl implements GlobalBehaviorData, MountData {
   private MountManager mainMountManager;

   public <T extends BehaviorManager<? extends Mount> & MountManager> T getMainMountManager() {
      return (BehaviorManager)this.mainMountManager;
   }

   public <T extends BehaviorManager<? extends Mount> & MountManager> void setMainMountManager(T var1) {
      this.mainMountManager = (MountManager)var1;
   }
}
