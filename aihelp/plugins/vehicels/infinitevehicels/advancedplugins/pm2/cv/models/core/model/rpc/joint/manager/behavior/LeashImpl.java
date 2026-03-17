package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.AbstractJointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.LeashManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Leash;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.joml.Vector3f;

public class LeashImpl extends AbstractJointAction<LeashImpl> implements Leash {
   private final boolean mainLeash;
   private final int id;
   private final Vector3f location = new Vector3f();
   private Entity connectedEntity;
   private Leash connectedLeash;

   public LeashImpl(IJoint var1, JointActionType<LeashImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      this.mainLeash = (Boolean)var3.get("main", false);
      this.id = ModelAPI.getEntityHandler().getNextEntityId();
   }

   public void onApply() {
      this.joint.getVisualModel().getLeashManager().ifPresent((var1x) -> {
         ((LeashManager)var1x).registerLeash(this);
      });
      Location var1 = this.joint.calculatePivotLocation();
      this.joint.getBlueprintJoint().getLocalPosition().rotateY((180.0F - this.joint.getYaw()) * 0.017453292F, this.location).add((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
   }

   public void onFinalize() {
      Location var1 = this.joint.calculatePivotLocation();
      this.joint.getGlobalPosition().rotateY((180.0F - this.joint.getYaw()) * 0.017453292F, this.location).add((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
   }

   public void connect(Entity var1) {
      this.connectedEntity = var1;
   }

   public <T extends Leash & JointAction> void connect(T var1) {
      if (var1 != this) {
         this.connectedLeash = var1;
      }

   }

   public void disconnect() {
      this.connectedEntity = null;
      this.connectedLeash = null;
   }

   public <T extends Leash & JointAction> T getConnectedLeash() {
      return this.connectedLeash;
   }

   @Generated
   public boolean isMainLeash() {
      return this.mainLeash;
   }

   @Generated
   public int getId() {
      return this.id;
   }

   @Generated
   public Vector3f getLocation() {
      return this.location;
   }

   @Generated
   public Entity getConnectedEntity() {
      return this.connectedEntity;
   }
}
