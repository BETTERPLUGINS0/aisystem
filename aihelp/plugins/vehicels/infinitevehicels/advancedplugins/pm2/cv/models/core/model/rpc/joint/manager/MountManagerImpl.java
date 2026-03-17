package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.events.ModelDismountEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.events.ModelMountEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.AbstractBehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountController;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountControllerSupplier;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.MountImpl;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MountManagerImpl extends AbstractBehaviorManager<MountImpl> implements MountManager {
   private final Map<String, ?> seats = new LinkedHashMap();
   private final Map<Entity, Mount> passengerSeatMap = new ConcurrentHashMap();
   boolean canDrive;
   boolean canRide;
   private Entity driver;
   private Mount driverJoint;

   public MountManagerImpl(IVisualModel var1, JointActionType<MountImpl> var2) {
      super(var1, var2);
   }

   public void onDestroy() {
      this.dismountDriver();
      this.dismountAll();
   }

   public boolean canDrive() {
      return this.canDrive;
   }

   public boolean canRide() {
      return this.canRide;
   }

   public boolean isControlled() {
      return this.driver != null && this.driverJoint != null;
   }

   public boolean hasPassengers() {
      return !this.passengerSeatMap.isEmpty();
   }

   public boolean hasRiders() {
      return this.driver != null || !this.passengerSeatMap.isEmpty();
   }

   @Nullable
   public <T extends Mount & JointAction> T getDriverJoint() {
      return this.driverJoint;
   }

   public <T extends Mount & JointAction> void setDriverJoint(@Nullable T var1) {
      this.driverJoint = var1;
   }

   public <T extends Mount & JointAction> void registerSeat(T var1) {
      this.getSeats().put(((JointAction)var1).getJoint().getUniqueJointId(), (JointAction)var1);
   }

   public <T extends Mount & JointAction> Map<String, T> getSeats() {
      return this.seats;
   }

   public <T extends Mount & JointAction> Optional<T> getSeat(String var1) {
      return Optional.ofNullable((Mount)this.getSeats().get(var1));
   }

   public <T extends Mount & JointAction> Optional<T> getMount(Entity var1) {
      if (var1 == this.driver) {
         return Optional.ofNullable(this.getDriverJoint());
      } else {
         Mount var2 = (Mount)this.passengerSeatMap.get(var1);
         return Optional.ofNullable(var2);
      }
   }

   public Map<Entity, Mount> getPassengerSeatMap() {
      return ImmutableMap.copyOf(this.passengerSeatMap);
   }

   public boolean mountDriver(Entity var1, MountControllerSupplier var2) {
      return this.mountDriver(var1, var2, (Consumer)null);
   }

   public boolean mountDriver(Entity var1, MountControllerSupplier var2, @Nullable Consumer<MountController> var3) {
      if (this.driverJoint != null && this.driverJoint.getPassengers().isEmpty() && this.canDrive()) {
         ModelMountEvent var4 = new ModelMountEvent(this.visualModel, var1, true, this.driverJoint);
         ModelAPI.callEvent(var4);
         if (var4.isCancelled()) {
            return false;
         } else {
            boolean var5 = this.driverJoint.addPassenger(var1);
            if (var5) {
               this.driver = var1;
               this.registerMountPair(var1, this.driverJoint, var2, var3);
            }

            return var5;
         }
      } else {
         return false;
      }
   }

   public boolean mountPassenger(String var1, Entity var2, MountControllerSupplier var3) {
      return this.mountPassenger((String)var1, var2, var3, (Consumer)null);
   }

   public boolean mountPassenger(String var1, Entity var2, MountControllerSupplier var3, @Nullable Consumer<MountController> var4) {
      return this.canRide() && (Boolean)this.getSeat(var1).map((var4x) -> {
         return this.mountPassenger((Mount)var4x, var2, var3, var4);
      }).orElse(false);
   }

   public boolean mountPassenger(Mount var1, Entity var2, MountControllerSupplier var3) {
      return this.mountPassenger((Mount)var1, var2, var3, (Consumer)null);
   }

   public boolean mountPassenger(Mount var1, Entity var2, MountControllerSupplier var3, @Nullable Consumer<MountController> var4) {
      ModelMountEvent var5 = new ModelMountEvent(this.visualModel, var2, false, var1);
      ModelAPI.callEvent(var5);
      if (var5.isCancelled()) {
         return false;
      } else {
         boolean var6 = var1.addPassenger(var2);
         if (var6) {
            this.passengerSeatMap.put(var2, var1);
            this.registerMountPair(var2, var1, var3, var4);
         }

         return var6;
      }
   }

   public boolean mountAvailable(Entity var1, MountControllerSupplier var2) {
      return this.mountAvailable((Entity)var1, (MountControllerSupplier)var2, (Consumer)null);
   }

   public boolean mountAvailable(Entity var1, MountControllerSupplier var2, @Nullable Consumer<MountController> var3) {
      return this.mountAvailable((Entity)var1, this.seats.keySet(), var2, var3);
   }

   public Set<Entity> mountAvailable(Collection<Entity> var1, MountControllerSupplier var2) {
      return this.mountAvailable((Collection)var1, (MountControllerSupplier)var2, (Consumer)null);
   }

   public Set<Entity> mountAvailable(Collection<Entity> var1, MountControllerSupplier var2, @Nullable Consumer<MountController> var3) {
      return this.mountAvailable((Collection)var1, this.seats.keySet(), var2, var3);
   }

   public boolean mountAvailable(Entity var1, Collection<String> var2, MountControllerSupplier var3) {
      return this.mountAvailable((Entity)var1, var2, var3, (Consumer)null);
   }

   public boolean mountAvailable(Entity var1, Collection<String> var2, MountControllerSupplier var3, @Nullable Consumer<MountController> var4) {
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         Optional var7 = this.getSeat(var6);
         if (var7.isPresent()) {
            JointAction var8 = (JointAction)var7.get();
            if (((Mount)var8).getPassengers().isEmpty()) {
               this.mountPassenger((Mount)var8, var1, var3, var4);
               return true;
            }
         }
      }

      return false;
   }

   public Set<Entity> mountAvailable(Collection<Entity> var1, Collection<String> var2, MountControllerSupplier var3) {
      return this.mountAvailable((Collection)var1, var2, var3, (Consumer)null);
   }

   public Set<Entity> mountAvailable(Collection<Entity> var1, Collection<String> var2, MountControllerSupplier var3, @Nullable Consumer<MountController> var4) {
      HashSet var5 = new HashSet();
      boolean var6 = false;
      Iterator var7 = var1.iterator();

      while(true) {
         label27:
         while(var7.hasNext()) {
            Entity var8 = (Entity)var7.next();
            if (!var6) {
               var6 = true;
               Iterator var9 = var2.iterator();

               while(var9.hasNext()) {
                  String var10 = (String)var9.next();
                  Optional var11 = this.getSeat(var10);
                  if (var11.isPresent()) {
                     JointAction var12 = (JointAction)var11.get();
                     if (((Mount)var12).getPassengers().isEmpty()) {
                        this.mountPassenger((Mount)var12, var8, var3, var4);
                        var6 = false;
                        continue label27;
                     }
                  }
               }
            }

            var5.add(var8);
         }

         return var5;
      }
   }

   public boolean mountLeastOccupied(Entity var1, MountControllerSupplier var2) {
      return this.mountLeastOccupied((Entity)var1, (MountControllerSupplier)var2, (Consumer)null);
   }

   public boolean mountLeastOccupied(Entity var1, MountControllerSupplier var2, @Nullable Consumer<MountController> var3) {
      return this.mountLeastOccupied((Entity)var1, this.seats.keySet(), var2, var3);
   }

   public Set<Entity> mountLeastOccupied(Collection<Entity> var1, MountControllerSupplier var2) {
      return this.mountLeastOccupied((Collection)var1, (MountControllerSupplier)var2, (Consumer)null);
   }

   public Set<Entity> mountLeastOccupied(Collection<Entity> var1, MountControllerSupplier var2, @Nullable Consumer<MountController> var3) {
      return this.mountLeastOccupied((Collection)var1, this.seats.keySet(), var2, var3);
   }

   public boolean mountLeastOccupied(Entity var1, Collection<String> var2, MountControllerSupplier var3) {
      return this.mountLeastOccupied((Entity)var1, var2, var3, (Consumer)null);
   }

   public boolean mountLeastOccupied(Entity var1, Collection<String> var2, MountControllerSupplier var3, @Nullable Consumer<MountController> var4) {
      int var5 = Integer.MAX_VALUE;
      Mount var6 = null;
      Iterator var7 = var2.iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         Optional var9 = this.getSeat(var8);
         if (var9.isPresent()) {
            JointAction var10 = (JointAction)var9.get();
            if (((Mount)var10).canMountMore()) {
               int var11 = ((Mount)var10).getPassengers().size();
               if (var11 == 0) {
                  return this.mountPassenger((Mount)var10, var1, var3, var4);
               }

               if (var5 > var11) {
                  var5 = var11;
                  var6 = (Mount)var10;
               }
            }
         }
      }

      if (var6 != null) {
         return this.mountPassenger(var6, var1, var3, var4);
      } else {
         return false;
      }
   }

   public Set<Entity> mountLeastOccupied(Collection<Entity> var1, Collection<String> var2, MountControllerSupplier var3) {
      return this.mountLeastOccupied((Collection)var1, var2, var3, (Consumer)null);
   }

   public Set<Entity> mountLeastOccupied(Collection<Entity> var1, Collection<String> var2, MountControllerSupplier var3, @Nullable Consumer<MountController> var4) {
      HashSet var5 = new HashSet();
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         Entity var7 = (Entity)var6.next();
         if (!this.mountLeastOccupied(var7, var2, var3, var4)) {
            var5.add(var7);
         }
      }

      return var5;
   }

   public Entity dismountDriver() {
      if (this.driverJoint != null && this.driver != null) {
         ModelDismountEvent var1 = new ModelDismountEvent(this.visualModel, this.driver, true, this.driverJoint);
         ModelAPI.callEvent(var1);
         if (var1.isCancelled()) {
            return null;
         } else {
            this.driverJoint.removePassenger(this.driver);
            this.removeMountPair(this.driver);
            Entity var2 = this.driver;
            this.driver = null;
            return var2;
         }
      } else {
         return null;
      }
   }

   public void dismountPassenger(@NotNull Entity var1) {
      Mount var2 = (Mount)this.passengerSeatMap.remove(var1);
      if (var2 != null) {
         ModelDismountEvent var3 = new ModelDismountEvent(this.visualModel, var1, false, var2);
         ModelAPI.callEvent(var3);
         if (var3.isCancelled()) {
            return;
         }

         var2.removePassenger(var1);
         this.removeMountPair(var1);
      }

   }

   public void dismountRider(@NotNull Entity var1) {
      if (var1 == this.driver) {
         this.dismountDriver();
      } else {
         this.dismountPassenger(var1);
      }

   }

   public Set<Entity> dismountPassengers(String var1) {
      HashSet var2 = new HashSet();
      this.getSeat(var1).ifPresent((var2x) -> {
         Set var3 = ((Mount)var2x).getPassengers();
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            Entity var5 = (Entity)var4.next();
            ModelDismountEvent var6 = new ModelDismountEvent(this.visualModel, var5, false, (Mount)var2x);
            ModelAPI.callEvent(var6);
            if (!var6.isCancelled()) {
               ((Mount)var2x).removePassenger(var5);
               this.removeMountPair(var5);
               var2.add(var5);
            }
         }

      });
      return var2;
   }

   public Set<Entity> dismountAll() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.getSeats().values().iterator();

      while(var2.hasNext()) {
         Mount var3 = (Mount)var2.next();
         JointAction var4 = (JointAction)var3;
         Set var5 = ((Mount)var4).clearPassengers();
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            Entity var7 = (Entity)var6.next();
            ModelDismountEvent var8 = new ModelDismountEvent(this.visualModel, var7, false, (Mount)var4);
            ModelAPI.callEvent(var8);
            if (!var8.isCancelled()) {
               this.passengerSeatMap.remove(var7);
               this.removeMountPair(var7);
               var1.add(var7);
            }
         }
      }

      return var1;
   }

   private void registerMountPair(Entity var1, Mount var2, MountControllerSupplier var3, @Nullable Consumer<MountController> var4) {
      MountController var5 = var3.createController(var1, var2);
      if (var4 != null) {
         var4.accept(var5);
      }

      ModelAPI.getMountPairManager().registerMountedPair(var1, this.visualModel, var5);
      this.visualModel.getModeledEntity().getBase().setCollidableWith(var1, false);
   }

   private void removeMountPair(Entity var1) {
      ModelAPI.getMountPairManager().unregisterMountedPair(var1.getUniqueId());
      this.visualModel.getModeledEntity().getBase().setCollidableWith(var1, true);
   }

   @Generated
   public void setCanDrive(boolean var1) {
      this.canDrive = var1;
   }

   @Generated
   public void setCanRide(boolean var1) {
      this.canRide = var1;
   }

   @Generated
   public Entity getDriver() {
      return this.driver;
   }
}
