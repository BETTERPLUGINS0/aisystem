package advancedplugins.pm2.cv.models.v1_21_R3.entity.controller;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.rootmotion.RootMotionDelta;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.GlobalBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MovementOverride;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R3.ReflectionFieldCatalog;
import advancedplugins.pm2.cv.models.v1_21_R3.network.utils.TripleConsumer;
import java.util.Iterator;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Generated;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.control.ControllerMove;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Input;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocomotionManager extends ControllerMove implements MoveController {
   protected final ControllerMove baseMovementControl;
   protected final Queue<Runnable> deferredOperations = new ConcurrentLinkedQueue();
   protected boolean groundContact;
   protected MovementOverride customMovementHandler;

   public LocomotionManager(EntityInsentient mob, ControllerMove control) {
      super(var1);
      this.baseMovementControl = var2;
   }

   public boolean b() {
      return this.baseMovementControl.b();
   }

   public double c() {
      return this.baseMovementControl.c();
   }

   public void a(double x, double y, double z, double speed) {
      this.baseMovementControl.a(var1, var3, var5, var7);
   }

   public void a(float forward, float sideways) {
      this.baseMovementControl.a(var1, var2);
   }

   public void a() {
      this.groundContact = this.d.aJ();
      IModelContainer var1 = ModelAPI.getModeledEntity(this.d.cG());
      if (var1 == null) {
         this.executeBasicMovement();
      } else {
         this.processModeledEntityMovement(var1);
         this.executeDeferredOperations();
         this.applyRootMotionTransform(var1);
      }

   }

   private void processModeledEntityMovement(IModelContainer modeledEntity) {
      GlobalBehaviorData var2 = var1.getMountData();
      BehaviorManager var3 = var2 == null ? null : ((MountData)var2).getMainMountManager();
      if (this.shouldUseDriverControl(var3)) {
         this.setupDriverControlledMovement();
         this.processDriverInput(var2.getMainMountManager());
      } else if (this.customMovementHandler != null) {
         this.customMovementHandler.updateMovement(this, var1);
      } else {
         this.executeBasicMovement();
      }

      this.updateAllPassengers(var1, var2.getMainMountManager());
   }

   private boolean shouldUseDriverControl(BehaviorManager mount) {
      return var1 != null && ((MountManager)var1).isControlled();
   }

   private void setupDriverControlledMovement() {
      this.d.d(true);
      this.preventAquaticJumping();
   }

   private void executeDeferredOperations() {
      while(!this.deferredOperations.isEmpty()) {
         ((Runnable)this.deferredOperations.poll()).run();
      }

   }

   private void applyRootMotionTransform(IModelContainer modeledEntity) {
      Vector var2 = this.extractEntityVelocity();
      RootMotionDelta var3 = var1.getRootMotionHandler().calculateRootMotion(var2);
      this.applyEntityVelocity(var2);
      if (var3 != null) {
         this.processRootMotion(var3);
      }

   }

   public double d() {
      return this.baseMovementControl.d();
   }

   public double e() {
      return this.baseMovementControl.e();
   }

   public double f() {
      return this.baseMovementControl.f();
   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void processDriverInput(T mountManager) {
      this.d.G(0.0F);
      this.d.I(0.0F);
      this.processRiderControl(((MountManager)var1).getDriver(), var1.getActiveModel(), ((MountManager)var1).getDriverJoint(), MountController::updateDriverMovement);
   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void updateAllPassengers(IModelContainer modeledEntity, T primaryMount) {
      Iterator var3 = var1.getModels().values().iterator();

      while(var3.hasNext()) {
         IVisualModel var4 = (IVisualModel)var3.next();
         var4.getMountManager().ifPresent((var3x) -> {
            this.processModelPassengers((MountManager)var3x, var4);
            if (var3x != var2 && ((MountManager)var3x).isControlled()) {
               this.processSecondaryDriver((MountManager)var3x, var4);
            }

         });
      }

   }

   private void processModelPassengers(MountManager mountSystem, IVisualModel model) {
      Iterator var3 = var1.getSeats().values().iterator();

      while(var3.hasNext()) {
         Mount var4 = (Mount)var3.next();
         JointAction var5 = (JointAction)var4;
         Iterator var6 = ((Mount)var5).getPassengers().iterator();

         while(var6.hasNext()) {
            Entity var7 = (Entity)var6.next();
            this.processRiderControl(var7, var2, (Mount)var5, MountController::updatePassengerMovement);
         }
      }

   }

   private void processSecondaryDriver(MountManager mountSystem, IVisualModel model) {
      this.processRiderControl(var1.getDriver(), var2, var1.getDriverJoint(), MountController::updatePassengerMovement);
   }

   private void processRiderControl(Entity rider, IVisualModel model, Mount mount, TripleConsumer<MountController, MoveController, IVisualModel> handler) {
      MountController var5 = this.fetchController(var1.getUniqueId());
      if (var5 != null) {
         this.initializeRiderInput(var5, var1);
         var4.consume(var5, this, var2);
      }

   }

   private void initializeRiderInput(MountController controller, Entity rider) {
      if (var1.getInput() == null) {
         var1.setInput(this.createInputState(var2));
      }

   }

   private MountController.MountInput createInputState(Entity rider) {
      if (var1 instanceof Player) {
         Player var2 = (Player)var1;
         Input var3 = var2.getCurrentInput();
         return new MountController.MountInput(var3.isForward(), var3.isBackward(), var3.isLeft(), var3.isRight(), var3.isJump(), var3.isSneak(), var3.isSprint());
      } else {
         return new MountController.MountInput();
      }
   }

   protected void executeBasicMovement() {
      this.baseMovementControl.a();
   }

   private void preventAquaticJumping() {
      if (this.d.bj()) {
         ReflectionUtils.set(this.d, ReflectionFieldCatalog.JUMP_COOLDOWN, 1);
      }

   }

   public void move(float lateral, float vertical, float forward, float speedScale) {
      float var5 = this.getSpeed();
      this.d.C(var5 * var4);
      this.d.G(var3);
      this.d.H(var2);
      this.d.I(var1);
   }

   public void globalMove(float x, float y, float z, float speedScale) {
      float var5 = this.getSpeed();
      this.d.C(var5 * var4);
      Vec3D var6 = this.rotateByEntityYaw(new Vec3D((double)var1, (double)var2, (double)var3));
      this.d.I((float)var6.d);
      this.d.H((float)var6.e);
      this.d.G((float)var6.f);
   }

   private Vec3D rotateByEntityYaw(Vec3D vector) {
      return var1.b(-this.d.dL() * 0.017453292F);
   }

   private void applyEntityVelocity(Vector velocity) {
      Vec3D var2 = this.rotateByEntityYaw(new Vec3D(var1.getX(), var1.getY(), var1.getZ()));
      this.d.I((float)var2.d);
      this.d.H((float)var2.e);
      this.d.G((float)var2.f);
   }

   private Vector extractEntityVelocity() {
      float var1 = Float.isNaN(this.d.bn) ? 0.0F : this.d.bn;
      float var2 = Float.isNaN(this.d.bo) ? 0.0F : this.d.bo;
      float var3 = Float.isNaN(this.d.bp) ? 0.0F : this.d.bp;
      return (new Vector(var1, var2, var3)).rotateAroundY((double)(this.d.dL() * 0.017453292F));
   }

   private void processRootMotion(RootMotionDelta motion) {
      Vector var2 = var1.delta();
      if (!var2.isZero() || !var1.onGround()) {
         this.nullifyFallDistance();
         if (!MathUtils.isSimilar((float)var2.getY(), 0.0F) || !var1.onGround()) {
            this.d.i(this.d.dy().d(1.0D, 0.0D, 1.0D));
         }

         Vec3D var3 = new Vec3D(var2.getX(), var2.getY(), var2.getZ());
         this.d.a(EnumMoveType.a, var3);
      }

   }

   public void jump() {
      this.d.O().a();
   }

   public void setVelocity(double x, double y, double z) {
      this.d.n(var1, var3, var5);
   }

   public void addVelocity(double x, double y, double z) {
      this.d.i(this.d.dy().b(var1, var3, var5));
   }

   public void nullifyFallDistance() {
      this.d.k();
   }

   public boolean isOnGround() {
      return this.groundContact;
   }

   public boolean isInWater() {
      return this.d.bj();
   }

   public float getSpeed() {
      return (float)this.d.h(GenericAttributes.v);
   }

   public Vector getVelocity() {
      return CraftVector.toBukkit(this.d.dy());
   }

   public void queuePostTick(Runnable runnable) {
      this.deferredOperations.add(var1);
   }

   private MountController fetchController(UUID uuid) {
      return ModelAPI.getMountPairManager().getController(var1);
   }

   @Generated
   public ControllerMove getBaseMovementControl() {
      return this.baseMovementControl;
   }

   @Generated
   public Queue<Runnable> getDeferredOperations() {
      return this.deferredOperations;
   }

   @Generated
   public boolean isGroundContact() {
      return this.groundContact;
   }

   @Generated
   public MovementOverride getCustomMovementHandler() {
      return this.customMovementHandler;
   }
}
