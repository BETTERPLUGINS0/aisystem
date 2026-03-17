package advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;

public interface GlobalBehaviorData {
   <T extends BehaviorManager<? extends Mount> & MountManager> T getMainMountManager();

   <T extends BehaviorManager<? extends Mount> & MountManager> void setMainMountManager(T var1);
}
