package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.Hitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.AbstractJointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox;
import advancedplugins.pm2.cv.models.api.nms.entity.HitboxEntity;
import advancedplugins.pm2.cv.models.api.utils.config.DebugToggle;
import advancedplugins.pm2.cv.models.api.utils.math.OrientedBoundingBox;
import advancedplugins.pm2.cv.models.api.utils.ticker.DualTicker;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class SubHitboxImpl extends AbstractJointAction<SubHitboxImpl> implements SubHitbox {
   private final boolean isOBB;
   private final Vector3f fixedDimension = new Vector3f();
   private final Vector3f origin = new Vector3f();
   private final int hitboxId;
   private final Vector3f dimension = new Vector3f();
   private final Vector3f location = new Vector3f();
   private final Quaternionf rotation = new Quaternionf();
   private final Map<UUID, Entity> boundEntities = Maps.newConcurrentMap();
   private float yaw;
   private HitboxEntity hitboxEntity;
   private float damageMultiplier = 1.0F;

   public SubHitboxImpl(IJoint var1, JointActionType<SubHitboxImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      Hitbox var4 = (Hitbox)var3.get("dimension");
      if (var4 == null) {
         throw new RuntimeException("Unable to retrieve sub-hitbox dimension of joint " + var1.getUniqueJointId());
      } else {
         this.hitboxId = ModelAPI.getEntityHandler().getNextEntityId();
         this.isOBB = (Boolean)var3.get("obb", false);
         Vector3f var5 = var1.getBlueprintJoint().getGlobalPosition();
         this.origin.set((Vector3fc)var3.get("origin", var5)).sub(var5);
         if (this.isOBB) {
            this.fixedDimension.set(var4.getWidth(), var4.getHeight(), var4.getDepth());
         } else {
            this.fixedDimension.set(var4.getMaxWidth(), var4.getHeight(), var4.getMaxWidth());
         }

      }
   }

   public void onApply() {
      Location var1 = this.joint.calculatePivotLocation();
      this.removeOld();
      this.hitboxEntity = ModelAPI.getEntityHandler().createHitbox(var1, this.joint, this);
      this.joint.getBlueprintJoint().getLocalPosition().rotateY((180.0F - this.joint.getYaw()) * 0.017453292F, this.location).add((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
      ModelAPI.getInteractionTracker().setEntityRelay(this.hitboxId, this.hitboxEntity.getEntityId());
   }

   public void onRemove() {
      ModelAPI.getInteractionTracker().removeEntityRelay(this.hitboxId);
      this.removeOld();
      DualTicker.queueSyncTask(() -> {
         this.boundEntities.forEach((var0, var1) -> {
            var1.remove();
         });
      });
   }

   public void postGlobalCalculation() {
      Vector3f var1 = new Vector3f(this.origin);
      Vector3f var2 = this.joint.getGlobalPosition();
      Quaternionf var3 = this.joint.getGlobalLeftRotation();
      Vector3f var4 = this.joint.getGlobalScale();
      var2.add(var1.mul(var4).rotate(var3), this.location);
      this.rotation.set(var3);
      var4.mul(this.fixedDimension, this.dimension);
   }

   public void onFinalize() {
      this.dimension.mul((float)this.joint.getBlueprintJoint().getScale()).mul(this.joint.getVisualModel().getScale());
      this.location.mul(this.joint.getVisualModel().getScale());
      Location var1 = this.joint.calculatePivotLocation();
      this.yaw = 180.0F - this.joint.getYaw();
      this.location.rotateY(this.yaw * 0.017453292F).add((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
      if (!this.isOBB) {
         this.location.sub(0.0F, this.dimension.y * 0.5F, 0.0F);
      }

      if (this.hitboxEntity != null) {
         this.hitboxEntity.queueLocation(this.location);
         if (DebugToggle.isDebugging(DebugToggle.SHOW_OBB) && this.isOBB) {
            OrientedBoundingBox var2 = this.hitboxEntity.getOrientedBoundingBox();
            if (var2 != null) {
               var2.visualize(var1.getWorld());
            }
         }
      }

   }

   public void addBoundEntity(Entity var1) {
      this.getBoundEntities().put(var1.getUniqueId(), var1);
      ModelAPI.getEntityHandler().forceDespawn(var1);
      ModelAPI.setRenderCanceled(var1.getEntityId(), true);
   }

   public void removeBoundEntity(Entity var1) {
      this.getBoundEntities().remove(var1.getUniqueId(), var1);
      ModelAPI.setRenderCanceled(var1.getEntityId(), false);
      ModelAPI.getEntityHandler().forceSpawn(var1);
   }

   private void removeOld() {
      if (this.hitboxEntity != null) {
         this.hitboxEntity.markRemoved();
      }

   }

   @Generated
   public boolean isOBB() {
      return this.isOBB;
   }

   @Generated
   public Vector3f getFixedDimension() {
      return this.fixedDimension;
   }

   @Generated
   public Vector3f getOrigin() {
      return this.origin;
   }

   @Generated
   public int getHitboxId() {
      return this.hitboxId;
   }

   @Generated
   public Vector3f getDimension() {
      return this.dimension;
   }

   @Generated
   public Vector3f getLocation() {
      return this.location;
   }

   @Generated
   public Quaternionf getRotation() {
      return this.rotation;
   }

   @Generated
   public Map<UUID, Entity> getBoundEntities() {
      return this.boundEntities;
   }

   @Generated
   public float getYaw() {
      return this.yaw;
   }

   @Generated
   public HitboxEntity getHitboxEntity() {
      return this.hitboxEntity;
   }

   @Generated
   public float getDamageMultiplier() {
      return this.damageMultiplier;
   }

   @Generated
   public void setDamageMultiplier(float var1) {
      this.damageMultiplier = var1;
   }
}
