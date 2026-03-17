package advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.impl;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import java.util.Optional;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class FlyingMountController extends AbstractMountController {
   private final boolean force;

   public FlyingMountController(Entity var1, Mount var2, boolean var3) {
      super(var1, var2);
      this.force = var3;
   }

   public void updateDriverMovement(MoveController var1, IVisualModel var2) {
      Optional var3 = var2.getMountManager();
      if (!var3.isEmpty()) {
         BehaviorManager var4 = (BehaviorManager)var3.get();
         var1.nullifyFallDistance();
         Vector var5 = var1.getVelocity();
         var1.setVelocity(var5.getX(), 0.0D, var5.getZ());
         if (this.input.isSneak()) {
            if (!var1.isOnGround()) {
               var1.addVelocity(0.0D, (double)(-var1.getSpeed()), 0.0D);
            } else if (!this.force) {
               ((MountManager)var4).dismountDriver();
               var1.move(0.0F, 0.0F, 0.0F, 0.0F);
               return;
            }
         }

         if (this.input.isJump()) {
            var1.addVelocity(0.0D, (double)var1.getSpeed(), 0.0D);
         }

         var1.move(this.input.getSide(), 0.0F, this.input.getFront(), 1.0F);
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
