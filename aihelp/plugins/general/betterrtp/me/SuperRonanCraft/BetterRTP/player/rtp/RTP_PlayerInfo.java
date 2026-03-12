package me.SuperRonanCraft.BetterRTP.player.rtp;

public class RTP_PlayerInfo {
   boolean applyDelay;
   boolean applyCooldown;
   boolean checkCooldown;
   boolean takeMoney;
   boolean takeHunger;

   public RTP_PlayerInfo() {
      this(true, true, true, true, true);
   }

   public RTP_PlayerInfo(boolean applyDelay, boolean applyCooldown) {
      this(applyDelay, applyCooldown, true);
   }

   public RTP_PlayerInfo(boolean applyDelay, boolean applyCooldown, boolean checkCooldown) {
      this(applyDelay, applyCooldown, checkCooldown, true, true);
   }

   public RTP_PlayerInfo(boolean applyDelay, boolean applyCooldown, boolean checkCooldown, boolean takeMoney, boolean takeHunger) {
      this.applyDelay = applyDelay;
      this.applyCooldown = applyCooldown;
      this.checkCooldown = checkCooldown;
      this.takeMoney = takeMoney;
      this.takeHunger = takeHunger;
   }

   public boolean isApplyDelay() {
      return this.applyDelay;
   }

   public boolean isApplyCooldown() {
      return this.applyCooldown;
   }

   public boolean isCheckCooldown() {
      return this.checkCooldown;
   }

   public boolean isTakeMoney() {
      return this.takeMoney;
   }

   public boolean isTakeHunger() {
      return this.takeHunger;
   }

   public void setApplyDelay(boolean applyDelay) {
      this.applyDelay = applyDelay;
   }

   public void setApplyCooldown(boolean applyCooldown) {
      this.applyCooldown = applyCooldown;
   }

   public void setCheckCooldown(boolean checkCooldown) {
      this.checkCooldown = checkCooldown;
   }

   public void setTakeMoney(boolean takeMoney) {
      this.takeMoney = takeMoney;
   }

   public void setTakeHunger(boolean takeHunger) {
      this.takeHunger = takeHunger;
   }

   public static enum RTP_PLAYERINFO_FLAG {
      NODELAY,
      NOCOOLDOWN,
      IGNORECOOLDOWN,
      IGNOREMONEY,
      IGNOREHUNGER;

      // $FF: synthetic method
      private static RTP_PlayerInfo.RTP_PLAYERINFO_FLAG[] $values() {
         return new RTP_PlayerInfo.RTP_PLAYERINFO_FLAG[]{NODELAY, NOCOOLDOWN, IGNORECOOLDOWN, IGNOREMONEY, IGNOREHUNGER};
      }
   }
}
