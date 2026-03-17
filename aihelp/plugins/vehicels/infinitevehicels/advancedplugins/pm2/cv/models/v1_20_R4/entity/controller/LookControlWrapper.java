package advancedplugins.pm2.cv.models.v1_20_R4.entity.controller;

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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.control.ControllerLook;
import net.minecraft.world.phys.Vec3D;
import org.spigotmc.ActivationRange;

public class LookControlWrapper extends ControllerLook implements LookController {
   private final ControllerLook original;
   private IModelContainer modelContainer;

   public LookControlWrapper(EntityInsentient mob, ControllerLook control) {
      super(var1);
      this.original = var2;
   }

   public void a(Vec3D var0) {
      this.original.a(var1);
   }

   public void a(Entity var0) {
      this.original.a(var1);
   }

   public void a(Entity var0, float var1, float var2) {
      this.original.a(var1, var2, var3);
   }

   public void a(double var0, double var2, double var4) {
      this.original.a(var1, var3, var5);
   }

   public void a(double var0, double var2, double var4, float var6, float var7) {
      this.original.a(var1, var3, var5, var7, var8);
   }

   public void a() {
      if (this.modelContainer == null) {
         this.modelContainer = ModelAPI.getModeledEntity(this.a.cw());
      }

      BehaviorManager var1 = this.getMainManager();
      if (var1 != null && ((MountManager)var1).isControlled()) {
         this.controlledTick(this.getMainManager());
      } else {
         this.defaultTick();
      }

   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void controlledTick(T manager) {
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
         this.original.a();
      }

   }

   public boolean d() {
      return this.original.d();
   }

   public double e() {
      return this.original.e();
   }

   public double f() {
      return this.original.f();
   }

   public double g() {
      return this.original.g();
   }

   public void lookAt(double x, double y, double z) {
      double var7 = var1 - this.a.dr();
      double var9 = var3 - this.a.dv();
      double var11 = var5 - this.a.dx();
      double var13 = Math.sqrt(var7 * var7 + var11 * var11);
      float var15 = (float)Math.toDegrees(Math.atan2(-var9, var13));
      float var16 = (float)Math.toDegrees(Math.atan2(-var7, var11));
      this.setPitch(var15);
      this.setHeadYaw(var16);
   }

   public void setPitch(float pitch) {
      this.a.s(var1);
   }

   public void setHeadYaw(float yaw) {
      this.a.r(var1);
      this.a.aW = var1;
   }

   public void setBodyYaw(float yaw) {
      this.a.aU = var1;
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
      return ActivationRange.checkIfActive(this.a);
   }
}
