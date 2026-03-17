package advancedplugins.pm2.cv.models.v1_21_R10.entity.controller;

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
import advancedplugins.pm2.cv.models.v1_21_R10.network.utils.EntityActivityChecker;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.phys.Vec3;

public class GazeDirectionHandler extends LookControl implements LookController {
   private final LookControl baseController;
   private IModelContainer entityModel;
   private MovementOverride directionOverride;

   public GazeDirectionHandler(Mob var1, LookControl var2) {
      super(var1);
      this.baseController = var2;
   }

   public void setLookAt(Vec3 var1) {
      this.baseController.setLookAt(var1);
   }

   public void setLookAt(Entity var1) {
      this.baseController.setLookAt(var1);
   }

   public void setLookAt(Entity var1, float var2, float var3) {
      this.baseController.setLookAt(var1, var2, var3);
   }

   public void setLookAt(double var1, double var3, double var5) {
      this.baseController.setLookAt(var1, var3, var5);
   }

   public void setLookAt(double var1, double var3, double var5, float var7, float var8) {
      this.baseController.setLookAt(var1, var3, var5, var7, var8);
   }

   public void tick() {
      this.initializeModelIfNeeded();
      BehaviorManager var1 = this.retrievePrimaryMountBehavior();
      if (this.shouldUseControlledBehavior(var1)) {
         this.performControlledUpdate(this.getPrimaryMountBehavior());
      } else if (this.directionOverride != null) {
         this.directionOverride.updateDirection(this, this.entityModel);
      } else {
         this.executeStandardBehavior();
      }

   }

   private void initializeModelIfNeeded() {
      if (this.entityModel == null) {
         this.entityModel = ModelAPI.getModeledEntity(this.mob.getUUID());
      }

   }

   private boolean shouldUseControlledBehavior(BehaviorManager var1) {
      return var1 != null && ((MountManager)var1).isControlled();
   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void performControlledUpdate(T var1) {
      org.bukkit.entity.Entity var2 = ((MountManager)var1).getDriver();
      if (var2 == null) {
         this.executeStandardBehavior();
      } else {
         MountController var3 = ModelAPI.getMountPairManager().getController(var2.getUniqueId());
         if (var3 != null) {
            var3.updateDirection(this, var1.getActiveModel());
         } else {
            this.executeStandardBehavior();
         }

      }
   }

   protected void executeStandardBehavior() {
      if (this.checkActivationStatus()) {
         this.baseController.tick();
      }

   }

   public boolean isLookingAtTarget() {
      return this.baseController.isLookingAtTarget();
   }

   public double getWantedX() {
      return this.baseController.getWantedX();
   }

   public double getWantedY() {
      return this.baseController.getWantedY();
   }

   public double getWantedZ() {
      return this.baseController.getWantedZ();
   }

   public void lookAt(double var1, double var3, double var5) {
      Vec3 var7 = new Vec3(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
      Vec3 var8 = new Vec3(var1, var3, var5);
      GazeDirectionHandler.OrientationAngles var9 = this.calculateOrientationAngles(var7, var8);
      this.setPitch(var9.pitch);
      this.setHeadYaw(var9.yaw);
   }

   private GazeDirectionHandler.OrientationAngles calculateOrientationAngles(Vec3 var1, Vec3 var2) {
      double var3 = var2.x - var1.x;
      double var5 = var2.y - var1.y;
      double var7 = var2.z - var1.z;
      double var9 = Math.sqrt(var3 * var3 + var7 * var7);
      float var11 = (float)Math.toDegrees(Math.atan2(-var5, var9));
      float var12 = (float)Math.toDegrees(Math.atan2(-var3, var7));
      return new GazeDirectionHandler.OrientationAngles(var11, var12);
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

   private <T extends BehaviorManager<? extends Mount> & MountManager> T retrievePrimaryMountBehavior() {
      if (this.entityModel == null) {
         return null;
      } else {
         GlobalBehaviorData var1 = this.entityModel.getGlobalBehaviorData(JointBehaviorTypes.MOUNT);
         if (var1 instanceof MountData) {
            MountData var2 = (MountData)var1;
            return var2.getMainMountManager();
         } else {
            return null;
         }
      }
   }

   private <T extends BehaviorManager<? extends Mount> & MountManager> T getPrimaryMountBehavior() {
      return this.retrievePrimaryMountBehavior();
   }

   private boolean checkActivationStatus() {
      return EntityActivityChecker.determineActivityStatus(this.mob);
   }

   @Generated
   public void setEntityModel(IModelContainer var1) {
      this.entityModel = var1;
   }

   @Generated
   public void setDirectionOverride(MovementOverride var1) {
      this.directionOverride = var1;
   }

   private static class OrientationAngles {
      final float pitch;
      final float yaw;

      OrientationAngles(float var1, float var2) {
         this.pitch = var1;
         this.yaw = var2;
      }
   }
}
