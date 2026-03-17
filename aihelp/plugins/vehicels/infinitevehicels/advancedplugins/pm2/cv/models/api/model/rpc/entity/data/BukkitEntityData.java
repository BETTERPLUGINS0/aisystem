package advancedplugins.pm2.cv.models.api.model.rpc.entity.data;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.CullType;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.MountPairManager;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.TrackedEntity;
import advancedplugins.pm2.cv.models.api.nms.impl.TempTrackedEntity;
import advancedplugins.pm2.cv.models.api.utils.data.QueuedAtomic;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class BukkitEntityData extends AbstractEntityData {
   protected final EntityHandler entityHandler = ModelAPI.getEntityHandler();
   protected final Entity entity;
   protected final QueuedAtomic<Boolean> isEntityValid = (new QueuedAtomic.Builder()).value(() -> {
      return false;
   }).build();
   protected final QueuedAtomic<Boolean> isDataValid = (new QueuedAtomic.Builder()).value(() -> {
      return false;
   }).build();
   protected final QueuedAtomic<Boolean> isForcedAlive = (new QueuedAtomic.Builder()).value(() -> {
      return true;
   }).build();
   protected final QueuedAtomic<Location> location = (new QueuedAtomic.Builder()).passer((var0, var1x) -> {
      var1x.set(((Location)var0.get()).clone());
   }).build();
   protected final QueuedAtomic<List<Entity>> passengers = (new QueuedAtomic.Builder()).value(ArrayList::new).setter((var0, var1x) -> {
      ((List)var1x.get()).clear();
      ((List)var1x.get()).addAll(var0);
   }).passer((var0, var1x) -> {
      ((List)var1x.get()).clear();
      ((List)var1x.get()).addAll((Collection)var0.get());
   }).build();
   protected final Set<UUID> syncTracking = new HashSet();
   protected final Map<UUID, CullType> asyncTracking = Maps.newConcurrentMap();
   protected final Queue<Queue<UUID>> startTrackingQueue = new ConcurrentLinkedQueue();
   protected final Set<UUID> startTracking = new HashSet();
   protected final Queue<Queue<UUID>> stopTrackingQueue = new ConcurrentLinkedQueue();
   protected final Set<UUID> stopTracking = new HashSet();
   protected TrackedEntity tracked;
   protected int lastCulled;
   protected int syncTick;
   protected boolean wasValid;
   protected int culledCount;

   public BukkitEntityData(Entity var1) {
      this.entity = var1;
      this.tracked = this.entityHandler.wrapTrackedEntity(var1);
      this.syncUpdate();
      this.asyncUpdate();
   }

   public void asyncUpdate() {
      this.isEntityValid.pass();
      this.isDataValid.pass();
      this.isForcedAlive.pass();
      this.location.pass();
      this.passengers.pass();

      while(true) {
         Queue var1;
         UUID var2;
         do {
            do {
               if (this.startTrackingQueue.isEmpty() && this.stopTrackingQueue.isEmpty()) {
                  return;
               }

               if (!this.startTrackingQueue.isEmpty()) {
                  var1 = (Queue)this.startTrackingQueue.poll();
                  if (var1 != null) {
                     while(!var1.isEmpty()) {
                        var2 = (UUID)var1.poll();
                        this.startTracking.add(var2);
                        this.stopTracking.remove(var2);
                        this.put(var2, CullType.NO_CULL);
                     }
                  }
               }
            } while(this.stopTrackingQueue.isEmpty());

            var1 = (Queue)this.stopTrackingQueue.poll();
         } while(var1 == null);

         while(!var1.isEmpty()) {
            var2 = (UUID)var1.poll();
            this.stopTracking.add(var2);
            this.startTracking.remove(var2);
            if (this.asyncTracking.get(var2) != CullType.CULLED) {
               this.remove(var2);
            }
         }
      }
   }

   public void syncUpdate() {
      ++this.syncTick;
      boolean var1 = this.entity.isValid();
      this.wasValid |= var1 || this.syncTick > 20;
      var1 |= !this.wasValid;
      this.isEntityValid.set(var1);
      this.isDataValid.set(var1 || !this.entityHandler.isRemoved(this.entity));
      if (this.isForcedAlive()) {
         this.entityHandler.setDeathTick(this.entity, 0);
      } else if (!this.isEntityValid()) {
         this.entityHandler.setDeathTick(this.entity, 20);
      }

      this.location.set(this.entity.getLocation());
      this.passengers.set(this.entity.getPassengers());
      Set var2 = this.getTracked().getTrackedPlayer((var1x) -> {
         return this.asyncTracking.get(var1x.getUniqueId()) != CullType.CULLED;
      });
      HashSet var3 = new HashSet(this.syncTracking);
      ConcurrentLinkedQueue var4 = new ConcurrentLinkedQueue();
      ConcurrentLinkedQueue var5 = new ConcurrentLinkedQueue();
      var3.addAll(var2);
      Iterator var6 = var3.iterator();

      while(var6.hasNext()) {
         UUID var7 = (UUID)var6.next();
         if (!this.syncTracking.contains(var7)) {
            var4.add(var7);
         } else if (!var2.contains(var7)) {
            var5.add(var7);
         }
      }

      this.syncTracking.clear();
      this.syncTracking.addAll(var2);
      this.startTrackingQueue.add(var4);
      this.stopTrackingQueue.add(var5);
   }

   public void cullUpdate() {
      this.updateCulledPlayer();
   }

   public void cleanup() {
      this.startTracking.clear();
      this.stopTracking.clear();
   }

   public void destroy() {
      this.startTrackingQueue.forEach(Collection::clear);
      this.startTrackingQueue.clear();
      this.stopTrackingQueue.forEach(Collection::clear);
      this.stopTrackingQueue.clear();
   }

   private void updateCulledPlayer() {
      if (--this.lastCulled <= 0) {
         this.lastCulled = this.cullInterval();
         MountPairManager var1 = ModelAPI.getMountPairManager();
         Location var2 = this.getLocation();
         BoundingBox var3 = this.getCullHitbox() == null ? this.entity.getBoundingBox() : this.getCullHitbox().createBoundingBox(var2.toVector());
         Iterator var4 = this.asyncTracking.keySet().iterator();

         while(true) {
            while(true) {
               Player var5;
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  UUID var6 = (UUID)var4.next();
                  var5 = Bukkit.getPlayer(var6);
               } while(var5 == null);

               IVisualModel var10 = var1.getMountedPair(var5.getUniqueId());
               if (var10 != null && var10.getModeledEntity().getBase().getData() == this) {
                  this.put(var5.getUniqueId(), CullType.NO_CULL);
               } else if (var2.getWorld() != var5.getWorld()) {
                  this.put(var5.getUniqueId(), CullType.CULLED);
               } else {
                  Location var7 = ((IEntityData.CullCameraOverride)PLAYER_EXECUTION_CALLBACK.invoker()).calculate(this, var5, var5.getEyeLocation());
                  if (this.verticalCull()) {
                     double var8 = Math.max(var7.getY() - var3.getMaxY(), var3.getMinY() - var7.getY());
                     if (var8 > this.verticalCullDistance()) {
                        this.put(var5.getUniqueId(), this.verticalCullType());
                        continue;
                     }
                  }

                  Vector var11 = var2.clone().subtract(var7).toVector();
                  if (this.blockedCull() && !this.entity.isGlowing() && !this.isModelGlowing() && var11.lengthSquared() > this.blockedCullIgnoreRadius() && this.entityHandler.shouldCull(var5, var7, this.entity, var3)) {
                     this.put(var5.getUniqueId(), this.blockedCullType());
                  } else {
                     if (this.backCull()) {
                        if (var3.contains(var7.toVector())) {
                           this.put(var5.getUniqueId(), CullType.NO_CULL);
                           continue;
                        }

                        if (var11.lengthSquared() > this.backCullIgnoreRadius() && var7.getDirection().dot(var11.normalize()) <= this.backCullAngle()) {
                           this.put(var5.getUniqueId(), this.backCullType());
                           continue;
                        }
                     }

                     this.put(var5.getUniqueId(), CullType.NO_CULL);
                  }
               }
            }
         }
      }
   }

   public boolean isDataValid() {
      return (Boolean)this.isDataValid.get();
   }

   public boolean isEntityValid() {
      return (Boolean)this.isEntityValid.get();
   }

   public boolean isForcedAlive() {
      return (Boolean)this.isForcedAlive.get();
   }

   public void setForcedAlive(boolean var1) {
      this.isForcedAlive.set(var1);
   }

   public Location getLocation() {
      return ((Location)this.location.get()).clone();
   }

   public List<Entity> getPassengers() {
      return (List)this.passengers.get();
   }

   public Set<UUID> getStartTracking() {
      return ImmutableSet.copyOf(this.startTracking);
   }

   public Map<UUID, CullType> getTracking() {
      return ImmutableMap.copyOf(this.asyncTracking);
   }

   public Set<UUID> getStopTracking() {
      return ImmutableSet.copyOf(this.stopTracking);
   }

   public boolean hasTracking() {
      return !this.asyncTracking.isEmpty() && this.culledCount != this.asyncTracking.size();
   }

   public TrackedEntity getTracked() {
      TrackedEntity var1 = this.tracked;
      if (var1 instanceof TempTrackedEntity) {
         TempTrackedEntity var2 = (TempTrackedEntity)var1;
         var1 = this.entityHandler.wrapTrackedEntity(this.entity);
         if (!(var1 instanceof TempTrackedEntity)) {
            if (var2.getBaseRange() != -1) {
               var1.setBaseRange(var2.getBaseRange());
            }

            var1.setPlayerPredicate(var2.getPlayerPredicate());
            Iterator var3 = var2.getForcePaired().iterator();

            UUID var4;
            while(var3.hasNext()) {
               var4 = (UUID)var3.next();
               var1.addForcedPairing(var4);
            }

            var3 = var2.getForceHidden().iterator();

            while(var3.hasNext()) {
               var4 = (UUID)var3.next();
               var1.addForcedHidden(var4);
            }

            this.tracked = var1;
         }
      }

      return this.tracked;
   }

   private CullType put(UUID var1, CullType var2) {
      CullType var3 = (CullType)this.asyncTracking.put(var1, var2);
      if (var3 == CullType.CULLED) {
         --this.culledCount;
      }

      if (var2 == CullType.CULLED) {
         ++this.culledCount;
      }

      return var3;
   }

   private CullType remove(UUID var1) {
      CullType var2 = (CullType)this.asyncTracking.remove(var1);
      if (var2 == CullType.CULLED) {
         --this.culledCount;
      }

      return var2;
   }
}
