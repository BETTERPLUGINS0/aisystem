package advancedplugins.pm2.cv.models.v1_21_R5.entity.controller;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.GlobalBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.LookController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MovementOverride;
import io.papermc.paper.entity.activation.ActivationRange;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.phys.Vec3;

public class LookControlWrapper extends LookControl implements LookController {
   private final LookControl original;
   private IModelContainer modelContainer;
   private MovementOverride movementOverride;

   public LookControlWrapper(Mob var1, LookControl var2) {
      super(var1);
      this.original = var2;
   }

   public void setLookAt(Vec3 var1) {
      this.original.setLookAt(var1);
   }

   public void setLookAt(Entity var1) {
      this.original.setLookAt(var1);
   }

   public void setLookAt(Entity var1, float var2, float var3) {
      this.original.setLookAt(var1, var2, var3);
   }

   public void setLookAt(double var1, double var3, double var5) {
      this.original.setLookAt(var1, var3, var5);
   }

   public void setLookAt(double var1, double var3, double var5, float var7, float var8) {
      this.original.setLookAt(var1, var3, var5, var7, var8);
   }

   public void tick() {
      if (this.modelContainer == null) {
         this.modelContainer = ModelAPI.getModeledEntity(this.mob.getUUID());
      }

      BehaviorManager var1 = this.getMainManager();
      if (var1 != null && ((MountManager)var1).isControlled()) {
         this.controlledTick(this.getMainManager());
      } else if (this.movementOverride != null) {
         this.movementOverride.updateDirection(this, this.modelContainer);
      } else {
         this.defaultTick();
      }

   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void controlledTick(T var1) {
      org.bukkit.entity.Entity var2 = ((MountManager)var1).getDriver();
      if (var2 != null) {
         MountController var3 = ModelAPI.getMountPairManager().getController(var2.getUniqueId());
         if (var3 != null) {
            var3.updateDirection(this, var1.getActiveModel());
            return;
         }
      }

      this.defaultTick();
   }

   protected void defaultTick() {
      if (this.isActive()) {
         this.original.tick();
      }

   }

   public boolean isLookingAtTarget() {
      return this.original.isLookingAtTarget();
   }

   public double getWantedX() {
      return this.original.getWantedX();
   }

   public double getWantedY() {
      return this.original.getWantedY();
   }

   public double getWantedZ() {
      return this.original.getWantedZ();
   }

   public void lookAt(double var1, double var3, double var5) {
      double var7 = var1 - this.mob.getX();
      double var9 = var3 - this.mob.getEyeY();
      double var11 = var5 - this.mob.getZ();
      double var13 = Math.sqrt(var7 * var7 + var11 * var11);
      float var15 = (float)Math.toDegrees(Math.atan2(-var9, var13));
      float var16 = (float)Math.toDegrees(Math.atan2(-var7, var11));
      this.setPitch(var15);
      this.setHeadYaw(var16);
   }

   public void setPitch(float var1) {
      this.mob.setXRot(var1);
   }

   public void setHeadYaw(float var1) {
      this.mob.setYRot(var1);
      this.mob.yHeadRot = var1;
   }

   public void setBodyYaw(float var1) {
      this.mob.yBodyRot = var1;
   }

   private <T extends BehaviorManager<? extends Mount> & MountManager> T getMainManager() {
      if (this.modelContainer == null) {
         return null;
      } else {
         GlobalBehaviorData var1 = this.modelContainer.getGlobalBehaviorData(JointBehaviorTypes.MOUNT);
         if (var1 instanceof MountData) {
            MountData var2 = (MountData)var1;
            return var2.getMainMountManager();
         } else {
            return null;
         }
      }
   }

   private boolean isActive() {
      return ActivationRange.checkIfActive(this.mob);
   }

   @Generated
   public void setModelContainer(IModelContainer var1) {
      this.modelContainer = var1;
   }

   @Generated
   public void setMovementOverride(MovementOverride var1) {
      this.movementOverride = var1;
   }
}
