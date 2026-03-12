package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import java.util.UUID;

public class CooldownData {
   private final UUID uuid;
   private Long time;

   public CooldownData(UUID uuid, Long time) {
      this.uuid = uuid;
      this.time = time;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public Long getTime() {
      return this.time;
   }

   public void setTime(Long time) {
      this.time = time;
   }
}
