package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.math.Vector3dm;

public record HitData(Vector3i position, Vector3dm blockHitLocation, BlockFace closestDirection, WrappedBlockState state) {
   public HitData(Vector3i position, Vector3dm blockHitLocation, BlockFace closestDirection, WrappedBlockState state) {
      this.position = position;
      this.blockHitLocation = blockHitLocation;
      this.closestDirection = closestDirection;
      this.state = state;
   }

   public Vector3d getRelativeBlockHitLocation() {
      return new Vector3d(this.blockHitLocation.getX() - (double)this.position.getX(), this.blockHitLocation.getY() - (double)this.position.getY(), this.blockHitLocation.getZ() - (double)this.position.getZ());
   }

   public Vector3i position() {
      return this.position;
   }

   public Vector3dm blockHitLocation() {
      return this.blockHitLocation;
   }

   public BlockFace closestDirection() {
      return this.closestDirection;
   }

   public WrappedBlockState state() {
      return this.state;
   }
}
