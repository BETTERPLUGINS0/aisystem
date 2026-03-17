package advancedplugins.pm2.cv.models.api.model.rpc.entity.data;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.CullType;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.Dummy;
import advancedplugins.pm2.cv.models.api.nms.impl.DummyTrackedEntity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class DummyEntityData<T> extends AbstractEntityData {
   protected final Dummy<T> dummy;
   protected final DummyTrackedEntity tracked;
   protected final Set<UUID> syncTracking = new HashSet();
   protected final Map<UUID, CullType> asyncTracking = Maps.newConcurrentMap();
   protected final Queue<UUID> startTrackingQueue = new ConcurrentLinkedQueue();
   protected final Set<UUID> startTracking = new HashSet();
   protected final Queue<UUID> stopTrackingQueue = new ConcurrentLinkedQueue();
   protected final Set<UUID> stopTracking = new HashSet();
   protected Location location;

   public DummyEntityData(Dummy<T> var1) {
      this.dummy = var1;
      this.tracked = new DummyTrackedEntity();
      this.syncUpdate();
      this.asyncUpdate();
   }

   public void asyncUpdate() {
      UUID var1;
      while(!this.startTrackingQueue.isEmpty()) {
         var1 = (UUID)this.startTrackingQueue.poll();
         this.startTracking.add(var1);
         this.asyncTracking.put(var1, CullType.NO_CULL);
      }

      while(!this.stopTrackingQueue.isEmpty()) {
         var1 = (UUID)this.stopTrackingQueue.poll();
         this.stopTracking.add(var1);
         if (this.asyncTracking.get(var1) != CullType.CULLED) {
            this.asyncTracking.remove(var1);
         }
      }

   }

   public void syncUpdate() {
      if (this.dummy.isDetectingPlayers() && this.location != null) {
         this.tracked.detectPlayers(this.location);
      }

      Set var1 = this.tracked.getTrackedPlayer((var1x) -> {
         return this.asyncTracking.get(var1x.getUniqueId()) != CullType.CULLED;
      });
      HashSet var2 = new HashSet(this.syncTracking);
      var2.addAll(var1);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         UUID var4 = (UUID)var3.next();
         if (!this.syncTracking.contains(var4)) {
            this.startTrackingQueue.add(var4);
         } else if (!var1.contains(var4)) {
            this.stopTrackingQueue.add(var4);
         }
      }

      this.syncTracking.clear();
      this.syncTracking.addAll(var1);
   }

   public void cullUpdate() {
   }

   public void cleanup() {
      this.startTracking.clear();
      this.stopTracking.clear();
   }

   public void destroy() {
   }

   public boolean isDataValid() {
      return this.dummy.isAlive();
   }

   public List<Entity> getPassengers() {
      return List.of();
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
      return true;
   }

   public int getRenderRadius() {
      return this.tracked.getBaseRange();
   }

   public void setRenderRadius(int var1) {
      this.tracked.setBaseRange(var1);
   }

   public DummyTrackedEntity getTracked() {
      return this.tracked;
   }

   public Location getLocation() {
      return this.location;
   }

   public void setLocation(Location var1) {
      this.location = var1;
   }
}
