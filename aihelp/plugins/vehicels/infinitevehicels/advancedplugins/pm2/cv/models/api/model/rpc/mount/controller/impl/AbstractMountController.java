package advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.impl;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.LookController;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class AbstractMountController implements MountController {
   protected final Entity entity;
   protected final Mount mount;
   protected MountController.MountInput input;
   protected boolean canDamageMount;
   protected boolean canInteractMount;

   public AbstractMountController(Entity var1, Mount var2) {
      this.entity = var1;
      this.mount = var2;
   }

   public boolean canDamageMount() {
      return this.canDamageMount;
   }

   public boolean canInteractMount() {
      return this.canInteractMount;
   }

   public void updateDirection(LookController var1, IVisualModel var2) {
      Location var3 = this.getEntity().getLocation();
      var1.setHeadYaw(var3.getYaw());
      var1.setPitch(var3.getPitch() * 0.5F);
   }

   public Entity getEntity() {
      return this.entity;
   }

   public Mount getMount() {
      return this.mount;
   }

   public MountController.MountInput getInput() {
      return this.input;
   }

   public void setInput(MountController.MountInput var1) {
      this.input = var1;
   }

   public boolean isCanDamageMount() {
      return this.canDamageMount;
   }

   public void setCanDamageMount(boolean var1) {
      this.canDamageMount = var1;
   }

   public boolean isCanInteractMount() {
      return this.canInteractMount;
   }

   public void setCanInteractMount(boolean var1) {
      this.canInteractMount = var1;
   }
}
