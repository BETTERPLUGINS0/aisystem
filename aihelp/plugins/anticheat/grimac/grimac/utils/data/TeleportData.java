package ac.grim.grimac.utils.data;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.math.Vector3dm;
import lombok.Generated;

public class TeleportData {
   private final Vector3d location;
   @Nullable
   private final Vector3d velocity;
   private final RelativeFlag flags;
   private int transaction;
   private int teleportId;

   public void modifyVector(@NotNull GrimPlayer player, Vector3dm vector) {
      boolean isStupidTeleportSystem = player.supportsEndTick();
      if (!isStupidTeleportSystem) {
         if (!this.isRelativeX()) {
            vector.setX(0);
         }

         if (!this.isRelativeY()) {
            vector.setY(0);
            player.lastWasClimbing = 0.0D;
            player.canSwimHop = false;
         }

         if (!this.isRelativeZ()) {
            vector.setZ(0);
         }
      }

      if (this.velocity != null && isStupidTeleportSystem) {
         if (this.isRelativeDeltaX()) {
            vector.setX(vector.getX() + this.velocity.getX());
         } else {
            vector.setX(this.velocity.getX());
         }

         if (this.isRelativeDeltaY()) {
            vector.setY(vector.getY() + this.velocity.getY());
         } else {
            vector.setY(this.velocity.getY());
            player.lastWasClimbing = 0.0D;
            player.canSwimHop = false;
         }

         if (this.isRelativeDeltaZ()) {
            vector.setZ(vector.getZ() + this.velocity.getZ());
         } else {
            vector.setZ(this.velocity.getZ());
         }
      }

   }

   public boolean isRelativeVelocity() {
      return this.isRelativeDeltaX() || this.isRelativeDeltaY() || this.isRelativeDeltaZ();
   }

   public boolean isRelativeDeltaX() {
      return this.flags.has(RelativeFlag.DELTA_X);
   }

   public boolean isRelativeDeltaY() {
      return this.flags.has(RelativeFlag.DELTA_Y);
   }

   public boolean isRelativeDeltaZ() {
      return this.flags.has(RelativeFlag.DELTA_Z);
   }

   public boolean isRelativePos() {
      return this.isRelativeX() || this.isRelativeY() || this.isRelativeZ();
   }

   public boolean isRelativeX() {
      return this.flags.has(RelativeFlag.X.getMask());
   }

   public boolean isRelativeY() {
      return this.flags.has(RelativeFlag.Y.getMask());
   }

   public boolean isRelativeZ() {
      return this.flags.has(RelativeFlag.Z.getMask());
   }

   @Generated
   public TeleportData(Vector3d location, @Nullable Vector3d velocity, RelativeFlag flags, int transaction, int teleportId) {
      this.location = location;
      this.velocity = velocity;
      this.flags = flags;
      this.transaction = transaction;
      this.teleportId = teleportId;
   }

   @Generated
   public Vector3d getLocation() {
      return this.location;
   }

   @Nullable
   @Generated
   public Vector3d getVelocity() {
      return this.velocity;
   }

   @Generated
   public RelativeFlag getFlags() {
      return this.flags;
   }

   @Generated
   public int getTransaction() {
      return this.transaction;
   }

   @Generated
   public int getTeleportId() {
      return this.teleportId;
   }

   @Generated
   public void setTransaction(int transaction) {
      this.transaction = transaction;
   }

   @Generated
   public void setTeleportId(int teleportId) {
      this.teleportId = teleportId;
   }
}
