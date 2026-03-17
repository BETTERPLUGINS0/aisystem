package advancedplugins.pm2.cv.models.api.model.rpc.joint.manager;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;

public interface MountData {
   <T extends BehaviorManager<? extends Mount> & MountManager> T getMainMountManager();

   <T extends BehaviorManager<? extends Mount> & MountManager> void setMainMountManager(T var1);
}
