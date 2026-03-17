package advancedplugins.pm2.cv.models.api.model.rpc.mount.controller;

import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.impl.FlyingMountController;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.impl.WalkingMountController;

public class MountControllerTypes {
   public static final MountControllerType WALKING = new MountControllerType((var0, var1) -> {
      return new WalkingMountController(var0, var1, false);
   });
   public static final MountControllerType WALKING_FORCE = new MountControllerType((var0, var1) -> {
      return new WalkingMountController(var0, var1, true);
   });
   public static final MountControllerType FLYING = new MountControllerType((var0, var1) -> {
      return new FlyingMountController(var0, var1, false);
   });
   public static final MountControllerType FLYING_FORCE = new MountControllerType((var0, var1) -> {
      return new FlyingMountController(var0, var1, true);
   });
}
