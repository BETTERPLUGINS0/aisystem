package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.AbstractJointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Segment;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.Optional;
import lombok.Generated;
import org.bukkit.Location;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TailImpl extends AbstractJointAction<TailImpl> implements Segment {
   private Vector3f worldLocation;
   private Vector3f direction;
   private Vector3f up;
   private float length;
   private boolean bounded;
   private boolean rollLock;
   private float angleLimit;
   private float extendRate;

   public TailImpl(IJoint var1, JointActionType<TailImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      this.bounded = (Boolean)var3.get("bounded", true);
      this.rollLock = (Boolean)var3.get("roll_lock", true);
      this.angleLimit = (Float)var3.get("angle_limit", 50.0F);
      this.extendRate = (Float)var3.get("extend_rate", 0.25F);
   }

   public void onApply() {
      this.length = this.joint.getBlueprintJoint().getLocalPosition().length();
      this.worldLocation = new Vector3f(this.calculatePivotLocation());
      this.direction = new Vector3f();
      this.joint.getGlobalPosition().zero();
      this.joint.getTrueGlobalPosition().zero();
      this.segmentNoPivot();
   }

   private <T extends Segment & JointAction> T getPivotData() {
      if (this.joint.getParent() != null) {
         Optional var1 = this.joint.getParent().getJointAction(JointBehaviorTypes.SEGMENT);
         if (var1.isPresent()) {
            return (Segment)var1.get();
         }

         var1 = this.joint.getParent().getJointAction(JointBehaviorTypes.TAIL);
         if (var1.isPresent()) {
            return (Segment)var1.get();
         }
      }

      return null;
   }

   public void onGlobalCalculation() {
      this.segmentNoPivot();
   }

   protected void segmentNoPivot() {
      this.joint.getGlobalPosition().zero();
      this.segmentCalculation();
      Location var1 = this.joint.calculatePivotLocation();
      float var2 = this.getPivotYaw() * 0.017453292F - 3.1415927F;
      this.worldLocation.sub((float)var1.getX(), (float)var1.getY(), (float)var1.getZ(), this.joint.getGlobalPosition()).rotateY(var2);
      this.joint.getTrueGlobalPosition().set(this.joint.getGlobalPosition());
      this.joint.getGlobalLeftRotation().premul((new Quaternionf()).rotateY(var2));
      this.joint.getTrueGlobalLeftRotation().set(this.joint.getGlobalLeftRotation());
   }

   protected void segmentCalculation() {
      JointAction var1 = (JointAction)this.getPivotData();
      Vector3f var2 = this.calculatePivotLocation();
      Vector3f var3 = this.worldLocation.sub(var2, new Vector3f());
      if ((double)var3.lengthSquared() <= 1.0E-5D) {
         var3.set(0.0F, 0.0F, -1.0F);
      }

      var3.normalize(this.direction);
      Vector3f var4;
      if (this.bounded) {
         if (var1 != null) {
            var4 = ((Segment)var1).getDirection();
         } else {
            var4 = new Vector3f(0.0F, 0.0F, 1.0F);
            if (this.joint.getParent() != null) {
               var4.rotate(this.joint.getParent().getGlobalLeftRotation());
            }

            var4.rotateY(3.1415927F - this.joint.getVisualModel().getModeledEntity().getYBodyRot() * 0.017453292F);
         }

         float var5 = (float)Math.acos((double)var4.dot(this.direction)) * 57.29578F;
         var4.lerp(this.direction, var5 > this.angleLimit ? this.angleLimit / var5 * (1.0F - this.extendRate) : 1.0F - this.extendRate, this.direction).normalize();
      }

      this.direction.mul(this.length, var3);
      var2.add(var3, this.worldLocation);
      var4 = MathUtils.toPitchYaw(this.direction);
      Quaternionf var11 = (new Quaternionf()).rotateZYX(0.0F, -var4.y * 0.017453292F, var4.x * 0.017453292F);
      this.up = (new Vector3f(0.0F, 1.0F, 0.0F)).rotate(var11);
      if (this.rollLock) {
         Quaternionf var6 = var11.invert(new Quaternionf());
         Vector3f var7;
         if (var1 != null) {
            var7 = ((Segment)var1).getUp();
         } else {
            var7 = new Vector3f(0.0F, 1.0F, 0.0F);
            if (this.joint.getParent() != null) {
               var7.rotate(this.joint.getParent().getGlobalLeftRotation());
            }

            var7.rotateY(3.1415927F - this.joint.getVisualModel().getModeledEntity().getYBodyRot() * 0.017453292F);
         }

         var7 = var7.rotate(var6, new Vector3f()).mul(1.0F, 1.0F, 0.0F).normalize().rotate(var11);
         float var8 = this.up.dot(var7);
         float var9 = (float)Math.acos((double)var8);
         if (var8 < 0.999995F) {
            if (var8 < -0.999995F) {
               var11.rotateZ(3.1415927F);
            } else {
               Vector3f var10 = this.up.cross(var7, new Vector3f()).rotate(var6);
               var11.rotateAxis(var9, var10);
            }
         }

         this.up.set(var7);
      }

      this.joint.setGlobalLeftRotation(var11);
   }

   private Vector3f calculatePivotLocation() {
      Location var1 = this.joint.getParent() == null ? this.joint.getBaseLocation() : this.joint.getParent().getLocationUnsafe();
      return new Vector3f((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
   }

   private Location toLocation(Vector3f var1) {
      return this.joint.getBaseLocation().clone().set((double)var1.x, (double)var1.y, (double)var1.z);
   }

   private float getPivotYaw() {
      return this.joint.getPivot() != null ? this.joint.getPivot().getYaw() : this.joint.getVisualModel().getModeledEntity().getYBodyRot();
   }

   @Generated
   public Vector3f getWorldLocation() {
      return this.worldLocation;
   }

   @Generated
   public Vector3f getDirection() {
      return this.direction;
   }

   @Generated
   public Vector3f getUp() {
      return this.up;
   }

   @Generated
   public void setBounded(boolean var1) {
      this.bounded = var1;
   }

   @Generated
   public boolean isBounded() {
      return this.bounded;
   }

   @Generated
   public void setRollLock(boolean var1) {
      this.rollLock = var1;
   }

   @Generated
   public boolean isRollLock() {
      return this.rollLock;
   }

   @Generated
   public void setAngleLimit(float var1) {
      this.angleLimit = var1;
   }

   @Generated
   public float getAngleLimit() {
      return this.angleLimit;
   }

   @Generated
   public void setExtendRate(float var1) {
      this.extendRate = var1;
   }

   @Generated
   public float getExtendRate() {
      return this.extendRate;
   }
}
