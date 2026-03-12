package me.SuperRonanCraft.BetterRTP.player.rtp;

import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;

public enum RTP_ERROR_REQUEST_REASON {
   IS_RTPING(MessagesCore.ALREADY),
   NO_PERMISSION(MessagesCore.NOPERMISSION_WORLD),
   WORLD_DISABLED(MessagesCore.DISABLED_WORLD),
   COOLDOWN(MessagesCore.COOLDOWN),
   PRICE_ECONOMY(MessagesCore.FAILED_PRICE),
   PRICE_HUNGER(MessagesCore.FAILED_HUNGER);

   private final MessagesCore msg;

   private RTP_ERROR_REQUEST_REASON(MessagesCore msg) {
      this.msg = msg;
   }

   public MessagesCore getMsg() {
      return this.msg;
   }

   // $FF: synthetic method
   private static RTP_ERROR_REQUEST_REASON[] $values() {
      return new RTP_ERROR_REQUEST_REASON[]{IS_RTPING, NO_PERMISSION, WORLD_DISABLED, COOLDOWN, PRICE_ECONOMY, PRICE_HUNGER};
   }
}
