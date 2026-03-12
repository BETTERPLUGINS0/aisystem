package ac.grim.grimac.utils.data;

import lombok.Generated;

public class TeleportAcceptData {
   private boolean isTeleport;
   private SetBackData setback;
   private TeleportData teleportData;

   @Generated
   public boolean isTeleport() {
      return this.isTeleport;
   }

   @Generated
   public SetBackData getSetback() {
      return this.setback;
   }

   @Generated
   public TeleportData getTeleportData() {
      return this.teleportData;
   }

   @Generated
   public void setTeleport(boolean isTeleport) {
      this.isTeleport = isTeleport;
   }

   @Generated
   public void setSetback(SetBackData setback) {
      this.setback = setback;
   }

   @Generated
   public void setTeleportData(TeleportData teleportData) {
      this.teleportData = teleportData;
   }
}
