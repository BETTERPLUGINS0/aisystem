package advancedplugins.pm2.cv.models.v1_21_R10.entity.controller;

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
import advancedplugins.pm2.cv.models.v1_21_R10.ReflectionFieldCatalog;
import advancedplugins.pm2.cv.models.v1_21_R10.network.utils.TripleConsumer;
import java.util.Iterator;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Generated;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Input;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocomotionManager extends MoveControl implements MoveController {
   protected final MoveControl baseMovementControl;
   protected final Queue<Runnable> deferredOperations = new ConcurrentLinkedQueue();
   protected boolean groundContact;
   protected MovementOverride customMovementHandler;

   public LocomotionManager(Mob var1, MoveControl var2) {
      super(var1);
      this.baseMovementControl = var2;
   }

   public boolean hasWanted() {
      return this.baseMovementControl.hasWanted();
   }

   public double getSpeedModifier() {
      return this.baseMovementControl.getSpeedModifier();
   }

   public void setWantedPosition(double var1, double var3, double var5, double var7) {
      this.baseMovementControl.setWantedPosition(var1, var3, var5, var7);
   }

   public void strafe(float var1, float var2) {
      this.baseMovementControl.strafe(var1, var2);
   }

   public void tick() {
      this.groundContact = this.mob.onGround();
      IModelContainer var1 = ModelAPI.getModeledEntity(this.mob.getUUID());
      if (var1 == null) {
         this.executeBasicMovement();
      } else {
         this.processModeledEntityMovement(var1);
         this.executeDeferredOperations();
         this.applyRootMotionTransform(var1);
      }

   }

   private void processModeledEntityMovement(IModelContainer var1) {
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

   private boolean shouldUseDriverControl(BehaviorManager var1) {
      return var1 != null && ((MountManager)var1).isControlled();
   }

   private void setupDriverControlledMovement() {
      this.mob.setOnGround(true);
      this.preventAquaticJumping();
   }

   private void executeDeferredOperations() {
      while(!this.deferredOperations.isEmpty()) {
         ((Runnable)this.deferredOperations.poll()).run();
      }

   }

   private void applyRootMotionTransform(IModelContainer var1) {
      Vector var2 = this.extractEntityVelocity();
      RootMotionDelta var3 = var1.getRootMotionHandler().calculateRootMotion(var2);
      this.applyEntityVelocity(var2);
      if (var3 != null) {
         this.processRootMotion(var3);
      }

   }

   public double getWantedX() {
      return this.baseMovementControl.getWantedX();
   }

   public double getWantedY() {
      return this.baseMovementControl.getWantedY();
   }

   public double getWantedZ() {
      return this.baseMovementControl.getWantedZ();
   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void processDriverInput(T var1) {
      this.mob.setZza(0.0F);
      this.mob.setXxa(0.0F);
      this.processRiderControl(((MountManager)var1).getDriver(), var1.getActiveModel(), ((MountManager)var1).getDriverJoint(), MountController::updateDriverMovement);
   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void updateAllPassengers(IModelContainer var1, T var2) {
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

   private void processModelPassengers(MountManager var1, IVisualModel var2) {
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

   private void processSecondaryDriver(MountManager var1, IVisualModel var2) {
      this.processRiderControl(var1.getDriver(), var2, var1.getDriverJoint(), MountController::updatePassengerMovement);
   }

   private void processRiderControl(Entity var1, IVisualModel var2, Mount var3, TripleConsumer<MountController, MoveController, IVisualModel> var4) {
      MountController var5 = this.fetchController(var1.getUniqueId());
      if (var5 != null) {
         this.initializeRiderInput(var5, var1);
         var4.consume(var5, this, var2);
      }

   }

   private void initializeRiderInput(MountController var1, Entity var2) {
      if (var1.getInput() == null) {
         var1.setInput(this.createInputState(var2));
      }

   }

   private MountController.MountInput createInputState(Entity var1) {
      if (var1 instanceof Player) {
         Player var2 = (Player)var1;
         Input var3 = var2.getCurrentInput();
         return new MountController.MountInput(var3.isForward(), var3.isBackward(), var3.isLeft(), var3.isRight(), var3.isJump(), var3.isSneak(), var3.isSprint());
      } else {
         return new MountController.MountInput();
      }
   }

   protected void executeBasicMovement() {
      this.baseMovementControl.tick();
   }

   private void preventAquaticJumping() {
      if (this.mob.isInWater()) {
         ReflectionUtils.set(this.mob, ReflectionFieldCatalog.JUMP_COOLDOWN, 1);
      }

   }

   public void move(float var1, float var2, float var3, float var4) {
      float var5 = this.getSpeed();
      this.mob.setSpeed(var5 * var4);
      this.mob.setZza(var3);
      this.mob.setYya(var2);
      this.mob.setXxa(var1);
   }

   public void globalMove(float var1, float var2, float var3, float var4) {
      float var5 = this.getSpeed();
      this.mob.setSpeed(var5 * var4);
      Vec3 var6 = this.rotateByEntityYaw(new Vec3((double)var1, (double)var2, (double)var3));
      this.mob.setXxa((float)var6.x);
      this.mob.setYya((float)var6.y);
      this.mob.setZza((float)var6.z);
   }

   private Vec3 rotateByEntityYaw(Vec3 var1) {
      return var1.yRot(-this.mob.getYRot() * 0.017453292F);
   }

   private void applyEntityVelocity(Vector var1) {
      Vec3 var2 = this.rotateByEntityYaw(new Vec3(var1.getX(), var1.getY(), var1.getZ()));
      this.mob.setXxa((float)var2.x);
      this.mob.setYya((float)var2.y);
      this.mob.setZza((float)var2.z);
   }

   private Vector extractEntityVelocity() {
      float var1 = Float.isNaN(this.mob.xxa) ? 0.0F : this.mob.xxa;
      float var2 = Float.isNaN(this.mob.yya) ? 0.0F : this.mob.yya;
      float var3 = Float.isNaN(this.mob.zza) ? 0.0F : this.mob.zza;
      return (new Vector(var1, var2, var3)).rotateAroundY((double)(this.mob.getYRot() * 0.017453292F));
   }

   private void processRootMotion(RootMotionDelta var1) {
      Vector var2 = var1.delta();
      if (!var2.isZero() || !var1.onGround()) {
         this.nullifyFallDistance();
         if (!MathUtils.isSimilar((float)var2.getY(), 0.0F) || !var1.onGround()) {
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
         }

         Vec3 var3 = new Vec3(var2.getX(), var2.getY(), var2.getZ());
         this.mob.move(MoverType.SELF, var3);
      }

   }

   public void jump() {
      this.mob.getJumpControl().jump();
   }

   public void setVelocity(double var1, double var3, double var5) {
      this.mob.setDeltaMovement(var1, var3, var5);
   }

   public void addVelocity(double var1, double var3, double var5) {
      this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(var1, var3, var5));
   }

   public void nullifyFallDistance() {
      this.mob.resetFallDistance();
   }

   public boolean isOnGround() {
      return this.groundContact;
   }

   public boolean isInWater() {
      return this.mob.isInWater();
   }

   public float getSpeed() {
      return (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
   }

   public Vector getVelocity() {
      return CraftVector.toBukkit(this.mob.getDeltaMovement());
   }

   public void queuePostTick(Runnable var1) {
      this.deferredOperations.add(var1);
   }

   private MountController fetchController(UUID var1) {
      return ModelAPI.getMountPairManager().getController(var1);
   }

   @Generated
   public MoveControl getBaseMovementControl() {
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
