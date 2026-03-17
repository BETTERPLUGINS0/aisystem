package advancedplugins.pm2.cv.models.api.nms.impl;

import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.TrackedEntity;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DummyTrackedEntity implements TrackedEntity {
   private final Set<UUID> tracked = Sets.newConcurrentHashSet();
   private final Set<UUID> forcedPairing = Sets.newConcurrentHashSet();
   private final Set<UUID> forcedRemove = Sets.newConcurrentHashSet();
   private int baseRange = 64;
   private Predicate<Player> playerPredicate;

   public DummyTrackedEntity() {
      this.playerPredicate = DEFAULT_PREDICATE;
   }

   public void detectPlayers(@NotNull Location var1) {
      if (var1.getWorld() == null) {
         throw new IllegalArgumentException("World of location cannot be null.");
      } else {
         int var2 = this.baseRange * this.baseRange;
         this.tracked.clear();
         Iterator var3 = Bukkit.getOnlinePlayers().iterator();

         while(var3.hasNext()) {
            Player var4 = (Player)var3.next();
            Location var5 = var4.getLocation();
            if (var5.getWorld() == var1.getWorld() && var5.distanceSquared(var1) <= (double)var2) {
               this.tracked.add(var4.getUniqueId());
            }
         }

      }
   }

   public int getEffectiveRange() {
      return this.baseRange;
   }

   public Set<UUID> getTrackedPlayer() {
      HashSet var1 = new HashSet(this.forcedPairing);
      Iterator var2 = this.tracked.iterator();

      while(var2.hasNext()) {
         UUID var3 = (UUID)var2.next();
         Player var4 = Bukkit.getPlayer(var3);
         if (var4 != null && this.playerPredicate.test(var4) && !this.forcedRemove.contains(var3)) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Set<UUID> getTrackedPlayer(Predicate<Player> var1) {
      HashSet var2 = new HashSet(this.forcedPairing);
      Iterator var3 = this.tracked.iterator();

      while(var3.hasNext()) {
         UUID var4 = (UUID)var3.next();
         Player var5 = Bukkit.getPlayer(var4);
         if (var1.test(var5) && this.playerPredicate.test(var5) && !this.forcedRemove.contains(var4)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public void addForcedPairing(UUID var1) {
      this.forcedPairing.add(var1);
      this.removeForcedHidden(var1);
   }

   public void removeForcedPairing(UUID var1) {
      this.forcedPairing.remove(var1);
   }

   public void addForcedHidden(UUID var1) {
      this.forcedRemove.add(var1);
      this.removeForcedPairing(var1);
   }

   public void removeForcedHidden(UUID var1) {
      this.forcedRemove.remove(var1);
   }

   public Entity getEntity() {
      return null;
   }

   public void sendPairingData(Player var1) {
   }

   public void broadcastSpawn() {
   }

   public void broadcastRemove() {
   }

   @Generated
   public Set<UUID> getTracked() {
      return this.tracked;
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
   public int getBaseRange() {
      return this.baseRange;
   }

   @Generated
   public Predicate<Player> getPlayerPredicate() {
      return this.playerPredicate;
   }

   @Generated
   public void setBaseRange(int var1) {
      this.baseRange = var1;
   }

   @Generated
   public void setPlayerPredicate(Predicate<Player> var1) {
      this.playerPredicate = var1;
   }
}
