package advancedplugins.pm2.cv.models.api.model.rpc.mount.controller;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import java.util.function.BiFunction;
import org.bukkit.entity.Entity;

public class MountControllerType implements MountControllerSupplier {
   private final BiFunction<Entity, Mount, MountController> controllerConstructor;

   public MountControllerType(BiFunction<Entity, Mount, MountController> var1) {
      this.controllerConstructor = var1;
   }

   public MountController createController(Entity var1, Mount var2) {
      return (MountController)this.controllerConstructor.apply(var1, var2);
   }
}
