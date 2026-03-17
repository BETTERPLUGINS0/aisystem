package advancedplugins.pm2.cv.models.api.model.rpc.mount.controller;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import org.bukkit.entity.Entity;

@FunctionalInterface
public interface MountControllerSupplier {
   MountController createController(Entity var1, Mount var2);
}
