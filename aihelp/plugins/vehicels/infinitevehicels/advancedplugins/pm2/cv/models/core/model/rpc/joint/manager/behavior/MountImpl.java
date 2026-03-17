package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.AbstractJointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.joml.Vector3f;

public class MountImpl extends AbstractJointAction<MountImpl> implements Mount {
   private final boolean driver;
   private final Vector3f location = new Vector3f();
   private final Vector3f globalLocation = new Vector3f();
   private final Set<Entity> passengers = Sets.newConcurrentHashSet();

   public MountImpl(IJoint var1, JointActionType<MountImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      this.driver = (Boolean)var3.get("driver", false);
   }

   public void onApply() {
      this.joint.getVisualModel().getMountManager().ifPresent((var1x) -> {
         if (this.driver) {
            ((MountManager)var1x).setDriverJoint(this);
         } else {
            ((MountManager)var1x).registerSeat(this);
         }

      });
      this.joint.getBlueprintJoint().getLocalPosition().rotateY((180.0F - this.joint.getYaw()) * 0.017453292F, this.location);
      Location var1 = this.joint.calculatePivotLocation();
      this.globalLocation.set(this.location).add((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
   }

   public void onFinalize() {
      this.joint.getGlobalPosition().rotateY((180.0F - this.joint.getYaw()) * 0.017453292F, this.location);
      Location var1 = this.joint.calculatePivotLocation();
      this.globalLocation.set(this.location).add((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
      this.passengers.removeIf((var0) -> {
         return ModelAPI.getEntityHandler().isRemoved(var0);
      });
   }

   public boolean addPassenger(Entity var1) {
      if (this.canMountMore()) {
         this.passengers.add(var1);
         return true;
      } else {
         return false;
      }
   }

   public boolean addPassengers(Collection<Entity> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Entity var3 = (Entity)var2.next();
         if (!this.canMountMore()) {
            return false;
         }

         this.passengers.add(var3);
      }

      return true;
   }

   public void removePassenger(Entity var1) {
      this.passengers.remove(var1);
   }

   public void removePassengers(Collection<Entity> var1) {
      this.passengers.removeAll(var1);
   }

   public Set<Entity> clearPassengers() {
      HashSet var1 = new HashSet(this.passengers);
      this.passengers.clear();
      return var1;
   }

   public Set<Entity> getPassengers() {
      return ImmutableSet.copyOf(this.passengers);
   }

   public boolean canMountMore() {
      return !this.isDriver() || this.passengers.isEmpty();
   }

   @Generated
   public boolean isDriver() {
      return this.driver;
   }

   @Generated
   public Vector3f getLocation() {
      return this.location;
   }

   @Generated
   public Vector3f getGlobalLocation() {
      return this.globalLocation;
   }
}
