package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.Vector3dm;
import lombok.Generated;

public final class FluidFallingAdjustedMovement {
   public static Vector3dm getFluidFallingAdjustedMovement(@NotNull GrimPlayer player, double gravity, boolean isFalling, Vector3dm velocity) {
      if (player.hasGravity && !player.isSprinting) {
         isFalling = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) ? isFalling : velocity.getY() < 0.0D;
         double newY = isFalling && Math.abs(velocity.getY() - 0.005D) >= 0.003D && Math.abs(velocity.getY() - gravity / 16.0D) < 0.003D ? -0.003D : velocity.getY() - gravity / 16.0D;
         return new Vector3dm(velocity.getX(), newY, velocity.getZ());
      } else {
         return velocity;
      }
   }

   @Generated
   private FluidFallingAdjustedMovement() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
