package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.MainSupportingBlockData;
import com.google.common.util.concurrent.AtomicDouble;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Generated;

public final class MainSupportingBlockPosFinder {
   public static MainSupportingBlockData findMainSupportingBlockPos(GrimPlayer player, MainSupportingBlockData lastSupportingBlock, Vector3d lastMovement, SimpleCollisionBox maxPose, boolean isOnGround) {
      if (!isOnGround) {
         return new MainSupportingBlockData((Vector3i)null, false);
      } else {
         SimpleCollisionBox slightlyBelowPlayer = new SimpleCollisionBox(maxPose.minX, maxPose.minY - 1.0E-6D, maxPose.minZ, maxPose.maxX, maxPose.minY, maxPose.maxZ);
         Vector3i supportingBlock = findSupportingBlock(player, slightlyBelowPlayer);
         if (supportingBlock == null && !lastSupportingBlock.lastOnGroundAndNoBlock()) {
            if (lastMovement != null) {
               SimpleCollisionBox aabb2 = slightlyBelowPlayer.offset(-lastMovement.x, 0.0D, -lastMovement.z);
               return new MainSupportingBlockData(findSupportingBlock(player, aabb2), true);
            } else {
               return new MainSupportingBlockData((Vector3i)null, true);
            }
         } else {
            return new MainSupportingBlockData(supportingBlock, true);
         }
      }
   }

   @Nullable
   private static Vector3i findSupportingBlock(@NotNull GrimPlayer player, @NotNull SimpleCollisionBox searchBox) {
      Vector3d playerPos = new Vector3d(player.x, player.y, player.z);
      AtomicReference<Vector3i> bestBlockPos = new AtomicReference();
      AtomicDouble blockPosDistance = new AtomicDouble(Double.MAX_VALUE);
      Collisions.forEachCollisionBox(player, searchBox, (pos) -> {
         Vector3i blockPos = pos.toVector3i();
         Vector3d blockPosAsVector3d = new Vector3d((double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D);
         double distance = playerPos.distanceSquared(blockPosAsVector3d);
         if (distance < blockPosDistance.get() || distance == blockPosDistance.get() && (bestBlockPos.get() == null || firstHasPriorityOverSecond(blockPos, (Vector3i)bestBlockPos.get()))) {
            bestBlockPos.set(blockPos);
            blockPosDistance.set(distance);
         }

      });
      return (Vector3i)bestBlockPos.get();
   }

   private static boolean firstHasPriorityOverSecond(@NotNull Vector3i first, @NotNull Vector3i second) {
      if (first.getY() < second.getY()) {
         return true;
      } else {
         double sumX = (double)(second.getX() - first.getX());
         double sumY = (double)(second.getZ() - first.getZ());
         double horizontalSumTotal = sumX + sumY;
         if (horizontalSumTotal == 0.0D) {
            return sumX < 0.0D;
         } else {
            return horizontalSumTotal < 0.0D;
         }
      }
   }

   @Generated
   private MainSupportingBlockPosFinder() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
