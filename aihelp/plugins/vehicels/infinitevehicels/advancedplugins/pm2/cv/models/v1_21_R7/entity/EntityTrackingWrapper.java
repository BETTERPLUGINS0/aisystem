package advancedplugins.pm2.cv.models.v1_21_R7.entity;

import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.TrackedEntity;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R7.ReflectionFieldCatalog;
import advancedplugins.pm2.cv.models.v1_21_R7.ReflectionMethodCatalog;
import advancedplugins.pm2.cv.models.v1_21_R7.network.utils.PacketTransmissionUtility;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityTrackingWrapper implements TrackedEntity {
   private final Entity observedEntity;
   private final Supplier<EntityTracker> trackingDataProvider;
   private final Set<UUID> explicitlyTracked = Sets.newConcurrentHashSet();
   private final Set<UUID> explicitlyHidden = Sets.newConcurrentHashSet();
   private Predicate<Player> visibilityFilter = (var0) -> {
      return true;
   };
   @NotNull
   private EntityTracker cachedTracker;

   public EntityTrackingWrapper(Entity var1, Supplier<EntityTracker> var2, @NotNull EntityTracker var3) {
      this.observedEntity = var1;
      this.trackingDataProvider = var2;
      this.cachedTracker = var3;
   }

   public int getBaseRange() {
      Integer var1 = (Integer)ReflectionUtils.get(this.getCurrentTracker(), ReflectionFieldCatalog.TRACKING_DISTANCE);
      if (var1 == null) {
         throw new NullPointerException(String.format("Unable to retrieve base range of entity with UUID %s.", this.observedEntity.getUniqueId()));
      } else {
         return var1;
      }
   }

   public void setBaseRange(int var1) {
      ReflectionUtils.set(this.getCurrentTracker(), ReflectionFieldCatalog.TRACKING_DISTANCE, var1);
   }

   public int getEffectiveRange() {
      Integer var1 = (Integer)ReflectionUtils.call(this.getCurrentTracker(), ReflectionMethodCatalog.CALCULATE_EFFECTIVE_RANGE);
      if (var1 == null) {
         throw new NullPointerException(String.format("Unable to retrieve range of entity with UUID %s.", this.observedEntity.getUniqueId()));
      } else {
         return var1;
      }
   }

   public Set<UUID> getTrackedPlayer() {
      return this.collectVisiblePlayers((var0) -> {
         return true;
      });
   }

   public Set<UUID> getTrackedPlayer(Predicate<Player> var1) {
      return this.collectVisiblePlayers(var1);
   }

   private Set<UUID> collectVisiblePlayers(Predicate<Player> var1) {
      HashSet var2 = new HashSet(this.explicitlyTracked);
      if (this.visibilityFilter == null) {
         this.visibilityFilter = (var0) -> {
            return true;
         };
      }

      Iterator var3 = this.getCurrentTracker().f.iterator();

      while(var3.hasNext()) {
         ServerPlayerConnection var4 = (ServerPlayerConnection)var3.next();
         Player var5 = Bukkit.getPlayer(var4.o().cT());
         if (this.isPlayerVisible(var5, var1)) {
            var2.add(var5.getUniqueId());
         }
      }

      return var2;
   }

   private boolean isPlayerVisible(Player var1, Predicate<Player> var2) {
      return var1 != null && var2.test(var1) && this.visibilityFilter.test(var1) && !this.explicitlyHidden.contains(var1.getUniqueId());
   }

   public void sendPairingData(Player var1) {
      if (var1 != null) {
         EntityPlayer var2 = ((CraftPlayer)var1).getHandle();
         this.getCurrentTracker().b.a(var2, (var1x) -> {
            PacketTransmissionUtility.transmitToPlayer(var1.getUniqueId(), var1x);
         });
      }
   }

   public void broadcastSpawn() {
      Iterator var1 = this.getTrackedPlayer().iterator();

      while(var1.hasNext()) {
         UUID var2 = (UUID)var1.next();
         Player var3 = Bukkit.getPlayer(var2);
         if (var3 != null) {
            this.sendPairingData(var3);
         }
      }

   }

   public void broadcastRemove() {
      this.getCurrentTracker().a();
   }

   public void addForcedPairing(UUID var1) {
      this.explicitlyTracked.add(var1);
      this.removeForcedHidden(var1);
   }

   public void removeForcedPairing(UUID var1) {
      this.explicitlyTracked.remove(var1);
   }

   public void addForcedHidden(UUID var1) {
      this.explicitlyHidden.add(var1);
      this.removeForcedPairing(var1);
   }

   public void removeForcedHidden(UUID var1) {
      this.explicitlyHidden.remove(var1);
   }

   @NotNull
   public Predicate<Player> getPlayerPredicate() {
      return this.visibilityFilter;
   }

   @NotNull
   private EntityTracker getCurrentTracker() {
      EntityTracker var1 = (EntityTracker)this.trackingDataProvider.get();
      if (var1 != null && this.cachedTracker != var1) {
         this.synchronizeTrackerData(var1);
         this.cachedTracker = var1;
      }

      return this.cachedTracker;
   }

   private void synchronizeTrackerData(EntityTracker var1) {
      Integer var2 = (Integer)ReflectionUtils.get(this.cachedTracker, ReflectionFieldCatalog.TRACKING_DISTANCE);
      if (var2 != null) {
         ReflectionUtils.set(var1, ReflectionFieldCatalog.TRACKING_DISTANCE, var2);
      }

   }

   public Entity getEntity() {
      return this.observedEntity;
   }

   public Entity getObservedEntity() {
      return this.observedEntity;
   }

   public void setPlayerPredicate(Predicate<Player> var1) {
      this.visibilityFilter = var1;
   }

   @Generated
   public Supplier<EntityTracker> getTrackingDataProvider() {
      return this.trackingDataProvider;
   }

   @Generated
   public Set<UUID> getExplicitlyTracked() {
      return this.explicitlyTracked;
   }

   @Generated
   public Set<UUID> getExplicitlyHidden() {
      return this.explicitlyHidden;
   }

   @Generated
   public Predicate<Player> getVisibilityFilter() {
      return this.visibilityFilter;
   }

   @NotNull
   @Generated
   public EntityTracker getCachedTracker() {
      return this.cachedTracker;
   }

   @Generated
   public void setVisibilityFilter(Predicate<Player> var1) {
      this.visibilityFilter = var1;
   }

   @Generated
   public void setCachedTracker(@NotNull EntityTracker var1) {
      this.cachedTracker = var1;
   }
}
