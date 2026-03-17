package advancedplugins.pm2.cv.models.v1_21_R4.entity;

import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.TrackedEntity;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R4.NMSFields;
import advancedplugins.pm2.cv.models.v1_21_R4.NMSMethods;
import advancedplugins.pm2.cv.models.v1_21_R4.network.utils.NetworkUtils;
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
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrackedEntityImpl implements TrackedEntity {
   private final Entity entity;
   private final Supplier<EntityTracker> trackedEntitySupplier;
   private final Set<UUID> forcedPairing = Sets.newConcurrentHashSet();
   private final Set<UUID> forcedRemove = Sets.newConcurrentHashSet();
   private Predicate<Player> playerPredicate;
   @NotNull
   private EntityTracker lastTrackedEntity;

   public TrackedEntityImpl(Entity entity, Supplier<EntityTracker> trackedEntitySupplier, @NotNull EntityTracker lastTrackedEntity) {
      this.playerPredicate = DEFAULT_PREDICATE;
      this.entity = var1;
      this.trackedEntitySupplier = var2;
      this.lastTrackedEntity = var3;
   }

   public int getBaseRange() {
      Integer var1 = (Integer)ReflectionUtils.get(this.getTrackedEntity(), NMSFields.TRACKED_ENTITY_range);
      if (var1 == null) {
         throw new NullPointerException(String.format("Unable to retrieve base range of entity with UUID %s.", this.entity.getUniqueId()));
      } else {
         return var1;
      }
   }

   public void setBaseRange(int range) {
      ReflectionUtils.set(this.getTrackedEntity(), NMSFields.TRACKED_ENTITY_range, var1);
   }

   public int getEffectiveRange() {
      Integer var1 = (Integer)ReflectionUtils.call(this.getTrackedEntity(), NMSMethods.TRACKED_ENTITY_getEffectiveRange);
      if (var1 == null) {
         throw new NullPointerException(String.format("Unable to retrieve range of entity with UUID %s.", this.entity.getUniqueId()));
      } else {
         return var1;
      }
   }

   public Set<UUID> getTrackedPlayer() {
      HashSet var1 = new HashSet(this.forcedPairing);
      Iterator var2 = this.getTrackedEntity().f.iterator();

      while(var2.hasNext()) {
         ServerPlayerConnection var3 = (ServerPlayerConnection)var2.next();
         Player var4 = Bukkit.getPlayer(var3.o().cG());
         if (var4 != null && this.playerPredicate.test(var4) && !this.forcedRemove.contains(var4.getUniqueId())) {
            var1.add(var4.getUniqueId());
         }
      }

      return var1;
   }

   public Set<UUID> getTrackedPlayer(Predicate<Player> predicate) {
      HashSet var2 = new HashSet(this.forcedPairing);
      Iterator var3 = this.getTrackedEntity().f.iterator();

      while(var3.hasNext()) {
         ServerPlayerConnection var4 = (ServerPlayerConnection)var3.next();
         Player var5 = Bukkit.getPlayer(var4.o().cG());
         if (var5 != null && var1.test(var5) && this.playerPredicate.test(var5) && !this.forcedRemove.contains(var5.getUniqueId())) {
            var2.add(var5.getUniqueId());
         }
      }

      return var2;
   }

   public void sendPairingData(Player player) {
      if (var1 != null) {
         EntityPlayer var2 = ((CraftPlayer)var1).getHandle();
         this.getTrackedEntity().b.a(var2, (var1x) -> {
            NetworkUtils.send(var1.getUniqueId(), var1x);
         });
      }

   }

   public void broadcastSpawn() {
      Iterator var1 = this.getTrackedPlayer().iterator();

      while(var1.hasNext()) {
         UUID var2 = (UUID)var1.next();
         this.sendPairingData(Bukkit.getPlayer(var2));
      }

   }

   public void broadcastRemove() {
      this.getTrackedEntity().a();
   }

   public void addForcedPairing(UUID uuid) {
      this.forcedPairing.add(var1);
      this.removeForcedHidden(var1);
   }

   public void removeForcedPairing(UUID uuid) {
      this.forcedPairing.remove(var1);
   }

   public void addForcedHidden(UUID uuid) {
      this.forcedRemove.add(var1);
      this.removeForcedPairing(var1);
   }

   public void removeForcedHidden(UUID uuid) {
      this.forcedRemove.remove(var1);
   }

   @NotNull
   private EntityTracker getTrackedEntity() {
      EntityTracker var1 = (EntityTracker)this.trackedEntitySupplier.get();
      if (var1 != null && this.lastTrackedEntity != var1) {
         this.syncInstance(var1);
         this.lastTrackedEntity = var1;
      }

      return this.lastTrackedEntity;
   }

   private void syncInstance(EntityTracker target) {
      Integer var2 = (Integer)ReflectionUtils.get(this.lastTrackedEntity, NMSFields.TRACKED_ENTITY_range);
      if (var2 != null) {
         ReflectionUtils.set(var1, NMSFields.TRACKED_ENTITY_range, var2);
      }

   }

   @Generated
   public Entity getEntity() {
      return this.entity;
   }

   @Generated
   public Supplier<EntityTracker> getTrackedEntitySupplier() {
      return this.trackedEntitySupplier;
   }

   @Generated
   public Set<UUID> getForcedPairing() {
      return this.forcedPairing;
   }

   @Generated
   public Set<UUID> getForcedRemove() {
      return this.forcedRemove;
   }

   @Generated
   public Predicate<Player> getPlayerPredicate() {
      return this.playerPredicate;
   }

   @NotNull
   @Generated
   public EntityTracker getLastTrackedEntity() {
      return this.lastTrackedEntity;
   }

   @Generated
   public void setPlayerPredicate(Predicate<Player> playerPredicate) {
      this.playerPredicate = var1;
   }

   @Generated
   public void setLastTrackedEntity(@NotNull EntityTracker lastTrackedEntity) {
      this.lastTrackedEntity = var1;
   }
}
