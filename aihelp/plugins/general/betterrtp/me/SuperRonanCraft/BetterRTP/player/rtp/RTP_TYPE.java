package me.SuperRonanCraft.BetterRTP.player.rtp;

public enum RTP_TYPE {
   COMMAND,
   FORCED,
   RESPAWN,
   JOIN,
   TEST,
   ADDON,
   ADDON_PORTAL,
   ADDON_MAGICSTICK;

   // $FF: synthetic method
   private static RTP_TYPE[] $values() {
      return new RTP_TYPE[]{COMMAND, FORCED, RESPAWN, JOIN, TEST, ADDON, ADDON_PORTAL, ADDON_MAGICSTICK};
   }
}
