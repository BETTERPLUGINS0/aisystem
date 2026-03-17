package advancedplugins.pm2.cv.models.v1_20_R4.entity.controller;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.rootmotion.RootMotionDelta;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.GlobalBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_20_R4.NMSFields;
import java.util.Iterator;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.control.ControllerMove;
import net.minecraft.world.phys.Vec3D;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftVector;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class MoveControlWrapper extends ControllerMove implements MoveController {
   protected final ControllerMove original;
   protected final Queue<Runnable> runnables = new ConcurrentLinkedQueue();
   protected boolean isOnGround;

   public MoveControlWrapper(EntityInsentient mob, ControllerMove control) {
      super(var1);
      this.original = var2;
   }

   public boolean b() {
      return this.original.b();
   }

   public double c() {
      return this.original.c();
   }

   public void a(double x, double y, double z, double speed) {
      this.original.a(var1, var3, var5, var7);
   }

   public void a(float forward, float sideways) {
      this.original.a(var1, var2);
   }

   public void a() {
      this.isOnGround = this.d.aC();
      IModelContainer var1 = ModelAPI.getModeledEntity(this.d.cw());
      if (var1 == null) {
         this.defaultTick();
      } else {
         GlobalBehaviorData var2 = var1.getMountData();
         BehaviorManager var3 = var2 == null ? null : var2.getMainMountManager();
         if (var3 != null && ((MountManager)var3).isControlled()) {
            this.d.c(true);
            this.disableWaterJumping();
            this.driverTick(var3);
         } else {
            this.defaultTick();
         }

         this.passengerTick(var1, var3);

         while(!this.runnables.isEmpty()) {
            ((Runnable)this.runnables.poll()).run();
         }

         Vector var4 = this.toVector();
         RootMotionDelta var5 = var1.getRootMotionHandler().calculateRootMotion(var4);
         this.fromVector(var4);
         if (var5 != null) {
            Vector var6 = var5.delta();
            if (!var6.isZero() || !var5.onGround()) {
               this.nullifyFallDistance();
               if (!MathUtils.isSimilar((float)var6.getY(), 0.0F) || !var5.onGround()) {
                  this.d.g(this.d.dp().d(1.0D, 0.0D, 1.0D));
               }

               this.d.a(EnumMoveType.a, new Vec3D(var6.getX(), var6.getY(), var6.getZ()));
            }
         }
      }

   }

   public double d() {
      return this.original.d();
   }

   public double e() {
      return this.original.e();
   }

   public double f() {
      return this.original.f();
   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void driverTick(T manager) {
      this.d.A(0.0F);
      this.d.C(0.0F);
      this.updateRider(((MountManager)var1).getDriver(), var1.getActiveModel(), ((MountManager)var1).getDriverJoint(), MountController::updateDriverMovement);
   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void passengerTick(IModelContainer modelContainer, T manager) {
      Iterator var3 = var1.getModels().values().iterator();

      while(var3.hasNext()) {
         IVisualModel var4 = (IVisualModel)var3.next();
         var4.getMountManager().ifPresent((var3x) -> {
            Iterator var4x = ((MountManager)var3x).getSeats().values().iterator();

            while(var4x.hasNext()) {
               JointAction var5 = (JointAction)var4x.next();
               Iterator var6 = ((Mount)var5).getPassengers().iterator();

               while(var6.hasNext()) {
                  Entity var7 = (Entity)var6.next();
                  this.updateRider(var7, var4, (Mount)var5, MountController::updatePassengerMovement);
               }
            }

            if (var3x != var2 && ((MountManager)var3x).isControlled()) {
               this.updateRider(((MountManager)var3x).getDriver(), var4, ((MountManager)var3x).getDriverJoint(), MountController::updatePassengerMovement);
            }

         });
      }

   }

   private void updateRider(Entity entity, IVisualModel visualModel, Mount mountJoint, TriConsumer<MountController, MoveController, IVisualModel> updateMethod) {
      MountController var5 = this.getController(var1.getUniqueId());
      if (var5 != null) {
         if (var5.getInput() == null) {
            var5.setInput(new MountController.MountInput());
         }

         var4.accept(var5, this, var2);
      }

   }

   protected void defaultTick() {
      this.original.a();
   }

   private void disableWaterJumping() {
      if (this.d.aZ()) {
         ReflectionUtils.set(this.d, NMSFields.LIVING_ENTITY_noJumpDelay, 1);
      }

   }

   public void move(float side, float up, float front, float speedModifier) {
      float var5 = this.getSpeed();
      this.d.w(var5 * var4);
      this.d.A(var3);
      this.d.B(var2);
      this.d.C(var1);
   }

   public void globalMove(float x, float y, float z, float speedModifier) {
      float var5 = this.getSpeed();
      this.d.w(var5 * var4);
      Vec3D var6 = (new Vec3D((double)var1, (double)var2, (double)var3)).b(-this.d.dC() * 0.017453292F);
      this.d.C((float)var6.c);
      this.d.B((float)var6.d);
      this.d.A((float)var6.e);
   }

   private void fromVector(Vector vector) {
      Vec3D var2 = (new Vec3D(var1.getX(), var1.getY(), var1.getZ())).b(-this.d.dC() * 0.017453292F);
      this.d.C((float)var2.c);
      this.d.B((float)var2.d);
      this.d.A((float)var2.e);
   }

   private Vector toVector() {
      return (new Vector(Float.isNaN(this.d.bk) ? 0.0F : this.d.bk, Float.isNaN(this.d.bl) ? 0.0F : this.d.bl, Float.isNaN(this.d.bm) ? 0.0F : this.d.bm)).rotateAroundY((double)(this.d.dC() * 0.017453292F));
   }

   public void jump() {
      this.d.M().a();
   }

   public void setVelocity(double x, double y, double z) {
      this.d.o(var1, var3, var5);
   }

   public void addVelocity(double x, double y, double z) {
      this.d.g(this.d.dp().b(var1, var3, var5));
   }

   public void nullifyFallDistance() {
      this.d.n();
   }

   public boolean isOnGround() {
      return this.isOnGround;
   }

   public boolean isInWater() {
      return this.d.aZ();
   }

   public float getSpeed() {
      return (float)this.d.b(GenericAttributes.m);
   }

   public Vector getVelocity() {
      return CraftVector.toBukkit(this.d.dp());
   }

   public void queuePostTick(Runnable runnable) {
      this.runnables.add(var1);
   }

   private MountController getController(UUID uuid) {
      return ModelAPI.getMountPairManager().getController(var1);
   }
}
