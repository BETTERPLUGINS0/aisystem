package advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.impl;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import java.util.Optional;
import org.bukkit.entity.Entity;

public class WalkingMountController extends AbstractMountController {
   private final boolean force;

   public WalkingMountController(Entity var1, Mount var2, boolean var3) {
      super(var1, var2);
      this.force = var3;
   }

   public void updateDriverMovement(MoveController var1, IVisualModel var2) {
      Optional var3 = var2.getMountManager();
      if (!var3.isEmpty()) {
         BehaviorManager var4 = (BehaviorManager)var3.get();
         if (!this.force && this.input.isSneak()) {
            ((MountManager)var4).dismountDriver();
            var1.move(0.0F, 0.0F, 0.0F, 0.0F);
         } else {
            var1.move(this.input.getSide(), 0.0F, this.input.getFront(), 1.0F);
            if (this.input.isJump() && (var1.isOnGround() || var1.isInWater())) {
               var1.jump();
            }
         }
      }

   }

   public void updatePassengerMovement(MoveController var1, IVisualModel var2) {
      Optional var3 = var2.getMountManager();
      if (!var3.isEmpty()) {
         BehaviorManager var4 = (BehaviorManager)var3.get();
         if (!this.force && this.input.isSneak()) {
            ((MountManager)var4).dismountRider(this.entity);
         }
      }

   }
}
