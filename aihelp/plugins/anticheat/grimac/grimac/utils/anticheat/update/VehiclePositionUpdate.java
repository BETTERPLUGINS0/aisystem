package ac.grim.grimac.utils.anticheat.update;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;

public record VehiclePositionUpdate(Vector3d from, Vector3d to, float xRot, float yRot, boolean onGround, boolean isTeleport) {
   public VehiclePositionUpdate(Vector3d from, Vector3d to, float xRot, float yRot, boolean onGround, boolean isTeleport) {
      this.from = from;
      this.to = to;
      this.xRot = xRot;
      this.yRot = yRot;
      this.onGround = onGround;
      this.isTeleport = isTeleport;
   }

   public Vector3d from() {
      return this.from;
   }

   public Vector3d to() {
      return this.to;
   }

   public float xRot() {
      return this.xRot;
   }

   public float yRot() {
      return this.yRot;
   }

   public boolean onGround() {
      return this.onGround;
   }

   public boolean isTeleport() {
      return this.isTeleport;
   }
}
