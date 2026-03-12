package ac.grim.grimac.utils.change;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public record BlockModification(WrappedBlockState oldBlockContents, WrappedBlockState newBlockContents, Vector3i location, int tick, BlockModification.Cause cause) {
   public BlockModification(WrappedBlockState oldBlockContents, WrappedBlockState newBlockContents, Vector3i location, int tick, BlockModification.Cause cause) {
      this.oldBlockContents = oldBlockContents;
      this.newBlockContents = newBlockContents;
      this.location = location;
      this.tick = tick;
      this.cause = cause;
   }

   @NotNull
   public String toString() {
      return String.format("BlockModification{location=%s, old=%s, new=%s, tick=%d, cause=%s}", this.location, this.oldBlockContents, this.newBlockContents, this.tick, this.cause);
   }

   public WrappedBlockState oldBlockContents() {
      return this.oldBlockContents;
   }

   public WrappedBlockState newBlockContents() {
      return this.newBlockContents;
   }

   public Vector3i location() {
      return this.location;
   }

   public int tick() {
      return this.tick;
   }

   public BlockModification.Cause cause() {
      return this.cause;
   }

   public static enum Cause {
      START_DIGGING,
      APPLY_BLOCK_CHANGES,
      HANDLE_NETTY_SYNC_TRANSACTION,
      OTHER;

      // $FF: synthetic method
      private static BlockModification.Cause[] $values() {
         return new BlockModification.Cause[]{START_DIGGING, APPLY_BLOCK_CHANGES, HANDLE_NETTY_SYNC_TRANSACTION, OTHER};
      }
   }
}
