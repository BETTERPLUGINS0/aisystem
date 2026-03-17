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

public class SegmentImpl extends AbstractJointAction<SegmentImpl> implements Segment {
   private Vector3f worldLocation;
   private Vector3f direction;
   private Vector3f up;
   private float yaw;
   private float length;
   private boolean bounded;
   private boolean rollLock;
   private float angleLimit;
   private float extendRate;

   public SegmentImpl(IJoint var1, JointActionType<SegmentImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      this.bounded = (Boolean)var3.get("bounded", true);
      this.rollLock = (Boolean)var3.get("roll_lock", false);
      this.angleLimit = (Float)var3.get("angle_limit", 50.0F);
      this.extendRate = (Float)var3.get("extend_rate", 0.01F);
   }

   public void onApply() {
      this.length = this.joint.getBlueprintJoint().getLocalPosition().length() * this.joint.getVisualModel().getScale().x();
      this.joint.getVisualModel().getScaleCallback().subscribe((var1, var2) -> {
         this.length = this.joint.getBlueprintJoint().getLocalPosition().length() * (float)var2;
      });
      this.worldLocation = new Vector3f(this.calculatePivotLocation());
      this.direction = new Vector3f();
      this.joint.setPivotLocation(this.toLocation(this.worldLocation));
      this.joint.getGlobalPosition().zero();
      this.joint.getTrueGlobalPosition().zero();
      this.segmentCalculation();
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
      this.segmentCalculation();
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
      this.yaw = var4.y + 180.0F;
      Quaternionf var12 = (new Quaternionf()).rotateX(var4.x * 0.017453292F);
      Quaternionf var6 = (new Quaternionf()).rotateZYX(0.0F, -var4.y * 0.017453292F, var4.x * 0.017453292F);
      this.up = (new Vector3f(0.0F, 1.0F, 0.0F)).rotate(var6);
      if (this.rollLock) {
         Quaternionf var7 = var6.invert(new Quaternionf());
         Vector3f var8;
         if (var1 != null) {
            var8 = ((Segment)var1).getUp();
         } else {
            var8 = new Vector3f(0.0F, 1.0F, 0.0F);
            if (this.joint.getParent() != null) {
               var8.rotate(this.joint.getParent().getGlobalLeftRotation());
            }

            var8.rotateY(3.1415927F - this.joint.getVisualModel().getModeledEntity().getYBodyRot() * 0.017453292F);
         }

         var8 = var8.rotate(var7, new Vector3f()).mul(1.0F, 1.0F, 0.0F).normalize().rotate(var6);
         float var9 = this.up.dot(var8);
         float var10 = (float)Math.acos((double)var9);
         if (var9 < 0.999995F) {
            if (var9 < -0.999995F) {
               var12.rotateZ(3.1415927F);
            } else {
               Vector3f var11 = this.up.cross(var8, new Vector3f()).rotate(var7);
               var12.rotateAxis(var10, var11);
            }
         }

         this.up.set(var8);
      }

      Location var13 = this.toLocation(this.worldLocation);
      this.joint.setPivotLocation(var13);
      this.joint.setGlobalLeftRotation(var12);
   }

   public float onUpdateYaw(float var1) {
      return this.yaw;
   }

   private Vector3f calculatePivotLocation() {
      Location var1 = this.joint.getParent() == null ? this.joint.getBaseLocation() : this.joint.getParent().getLocation();
      return new Vector3f((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
   }

   private Location toLocation(Vector3f var1) {
      return this.joint.getBaseLocation().clone().set((double)var1.x, (double)var1.y, (double)var1.z);
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
