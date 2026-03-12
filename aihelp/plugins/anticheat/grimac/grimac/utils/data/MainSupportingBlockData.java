package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public record MainSupportingBlockData(@Nullable Vector3i blockPos, boolean onGround) {
   public MainSupportingBlockData(@Nullable Vector3i blockPos, boolean onGround) {
      this.blockPos = blockPos;
      this.onGround = onGround;
   }

   @Contract(
      pure = true
   )
   public boolean lastOnGroundAndNoBlock() {
      return this.blockPos == null && this.onGround;
   }

   @Nullable
   public Vector3i blockPos() {
      return this.blockPos;
   }

   public boolean onGround() {
      return this.onGround;
   }
}
