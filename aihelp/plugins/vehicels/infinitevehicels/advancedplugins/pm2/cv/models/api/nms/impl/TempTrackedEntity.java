package advancedplugins.pm2.cv.models.api.nms.impl;

import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.TrackedEntity;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import lombok.Generated;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TempTrackedEntity implements TrackedEntity {
   private final Entity entity;
   private final Set<UUID> forcePaired = new HashSet();
   private final Set<UUID> forceHidden = new HashSet();
   private int baseRange = -1;
   private Predicate<Player> playerPredicate;

   public int getEffectiveRange() {
      return this.baseRange;
   }

   public Set<UUID> getTrackedPlayer() {
      return new HashSet();
   }

   public Set<UUID> getTrackedPlayer(Predicate<Player> var1) {
      return new HashSet();
   }

   public void sendPairingData(Player var1) {
   }

   public void broadcastSpawn() {
   }

   public void broadcastRemove() {
   }

   public void addForcedPairing(UUID var1) {
      this.forcePaired.add(var1);
   }

   public void removeForcedPairing(UUID var1) {
      this.forcePaired.remove(var1);
   }

   public void addForcedHidden(UUID var1) {
      this.forceHidden.add(var1);
   }

   public void removeForcedHidden(UUID var1) {
      this.forceHidden.remove(var1);
   }

   @Generated
   public TempTrackedEntity(Entity var1) {
      this.entity = var1;
   }

   @Generated
   public Entity getEntity() {
      return this.entity;
   }

   @Generated
   public Set<UUID> getForcePaired() {
      return this.forcePaired;
   }

   @Generated
   public Set<UUID> getForceHidden() {
      return this.forceHidden;
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
