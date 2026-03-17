package advancedplugins.pm2.cv.models.v1_21_R5.entity.controller;

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
import advancedplugins.pm2.cv.models.v1_21_R5.NMSFields;
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
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Input;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MoveControlWrapper extends MoveControl implements MoveController {
   protected final MoveControl original;
   protected final Queue<Runnable> runnables = new ConcurrentLinkedQueue();
   protected boolean isOnGround;
   protected MovementOverride movementOverride;

   public MoveControlWrapper(Mob var1, MoveControl var2) {
      super(var1);
      this.original = var2;
   }

   public boolean hasWanted() {
      return this.original.hasWanted();
   }

   public double getSpeedModifier() {
      return this.original.getSpeedModifier();
   }

   public void setWantedPosition(double var1, double var3, double var5, double var7) {
      this.original.setWantedPosition(var1, var3, var5, var7);
   }

   public void strafe(float var1, float var2) {
      this.original.strafe(var1, var2);
   }

   public void tick() {
      this.isOnGround = this.mob.onGround();
      IModelContainer var1 = ModelAPI.getModeledEntity(this.mob.getUUID());
      if (var1 == null) {
         this.defaultTick();
      } else {
         GlobalBehaviorData var2 = var1.getMountData();
         BehaviorManager var3 = var2 == null ? null : ((MountData)var2).getMainMountManager();
         if (var3 != null && ((MountManager)var3).isControlled()) {
            this.mob.setOnGround(true);
            this.disableWaterJumping();
            this.driverTick(var2.getMainMountManager());
         } else if (this.movementOverride != null) {
            this.movementOverride.updateMovement(this, var1);
         } else {
            this.defaultTick();
         }

         this.passengerTick(var1, var2.getMainMountManager());

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
                  this.mob.setDeltaMovement(this.mob.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
               }

               this.mob.move(MoverType.SELF, new Vec3(var6.getX(), var6.getY(), var6.getZ()));
            }
         }
      }

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

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void driverTick(T var1) {
      this.mob.setZza(0.0F);
      this.mob.setXxa(0.0F);
      this.updateRider(((MountManager)var1).getDriver(), var1.getActiveModel(), ((MountManager)var1).getDriverJoint(), MountController::updateDriverMovement);
   }

   protected <T extends BehaviorManager<? extends Mount> & MountManager> void passengerTick(IModelContainer var1, T var2) {
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

   private void updateRider(Entity var1, IVisualModel var2, Mount var3, TriConsumer<MountController, MoveController, IVisualModel> var4) {
      MountController var5 = this.getController(var1.getUniqueId());
      if (var5 != null) {
         if (var5.getInput() == null) {
            if (var1 instanceof Player) {
               Player var6 = (Player)var1;
               Input var7 = var6.getCurrentInput();
               var5.setInput(new MountController.MountInput(var7.isForward(), var7.isBackward(), var7.isLeft(), var7.isRight(), var7.isJump(), var7.isSneak(), var7.isSprint()));
            } else {
               var5.setInput(new MountController.MountInput());
            }
         }

         var4.accept(var5, this, var2);
      }

   }

   protected void defaultTick() {
      this.original.tick();
   }

   private void disableWaterJumping() {
      if (this.mob.isInWater()) {
         ReflectionUtils.set(this.mob, NMSFields.LIVING_ENTITY_noJumpDelay, 1);
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
      Vec3 var6 = (new Vec3((double)var1, (double)var2, (double)var3)).yRot(-this.mob.getYRot() * 0.017453292F);
      this.mob.setXxa((float)var6.x);
      this.mob.setYya((float)var6.y);
      this.mob.setZza((float)var6.z);
   }

   private void fromVector(Vector var1) {
      Vec3 var2 = (new Vec3(var1.getX(), var1.getY(), var1.getZ())).yRot(-this.mob.getYRot() * 0.017453292F);
      this.mob.setXxa((float)var2.x);
      this.mob.setYya((float)var2.y);
      this.mob.setZza((float)var2.z);
   }

   private Vector toVector() {
      return (new Vector(Float.isNaN(this.mob.xxa) ? 0.0F : this.mob.xxa, Float.isNaN(this.mob.yya) ? 0.0F : this.mob.yya, Float.isNaN(this.mob.zza) ? 0.0F : this.mob.zza)).rotateAroundY((double)(this.mob.getYRot() * 0.017453292F));
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
      return this.isOnGround;
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
      this.runnables.add(var1);
   }

   private MountController getController(UUID var1) {
      return ModelAPI.getMountPairManager().getController(var1);
   }

   @Generated
   public MoveControl getOriginal() {
      return this.original;
   }

   @Generated
   public Queue<Runnable> getRunnables() {
      return this.runnables;
   }

   @Generated
   public MovementOverride getMovementOverride() {
      return this.movementOverride;
   }
}
