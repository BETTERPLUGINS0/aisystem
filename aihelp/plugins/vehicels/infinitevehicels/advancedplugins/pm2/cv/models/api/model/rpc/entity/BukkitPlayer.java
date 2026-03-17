package advancedplugins.pm2.cv.models.api.model.rpc.entity;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.BukkitEntityData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BukkitPlayer extends BukkitEntity {
   public BukkitPlayer(Player var1) {
      super(var1);
   }

   protected BukkitEntityData createEntityData(Entity var1) {
      return new BukkitPlayer.BukkitPlayerData(var1);
   }

   public boolean isWalking() {
      return ((BukkitPlayer.BukkitPlayerData)this.getData()).getWalkTick() > 0;
   }

   public boolean isJumping() {
      return ((BukkitPlayer.BukkitPlayerData)this.getData()).getJumpTick() > 0;
   }

   public boolean isFlying() {
      return ((BukkitPlayer.BukkitPlayerData)this.getData()).isFlying;
   }

   public static class BukkitPlayerData extends BukkitEntityData {
      private int walkTick;
      private int jumpTick;
      private boolean isFlying;

      public BukkitPlayerData(Entity var1) {
         super(var1);
      }

      public void syncUpdate() {
         super.syncUpdate();
         if (this.walkTick > 0) {
            --this.walkTick;
         }

         if (this.jumpTick > 0 && this.entity.isOnGround()) {
            --this.jumpTick;
         }

         this.isFlying = ((Player)this.entity).isFlying();
      }

      public int getWalkTick() {
         return this.walkTick;
      }

      public void setWalkTick(int var1) {
         this.walkTick = var1;
      }

      public int getJumpTick() {
         return this.jumpTick;
      }

      public void setJumpTick(int var1) {
         this.jumpTick = var1;
      }
   }
}
