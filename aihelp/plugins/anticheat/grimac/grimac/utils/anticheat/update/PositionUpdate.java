package ac.grim.grimac.utils.anticheat.update;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.data.SetBackData;
import ac.grim.grimac.utils.data.TeleportData;
import lombok.Generated;

public final class PositionUpdate {
   private final Vector3d from;
   private final Vector3d to;
   private final boolean onGround;
   private final SetBackData setback;
   private final TeleportData teleportData;
   private boolean isTeleport;

   @Generated
   public PositionUpdate(Vector3d from, Vector3d to, boolean onGround, SetBackData setback, TeleportData teleportData, boolean isTeleport) {
      this.from = from;
      this.to = to;
      this.onGround = onGround;
      this.setback = setback;
      this.teleportData = teleportData;
      this.isTeleport = isTeleport;
   }

   @Generated
   public Vector3d getFrom() {
      return this.from;
   }

   @Generated
   public Vector3d getTo() {
      return this.to;
   }

   @Generated
   public boolean isOnGround() {
      return this.onGround;
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
   public boolean isTeleport() {
      return this.isTeleport;
   }

   @Generated
   public void setTeleport(boolean isTeleport) {
      this.isTeleport = isTeleport;
   }
}
