package ac.grim.grimac.predictionengine.blockeffects.impl;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.blockeffects.BlockCollisions;
import ac.grim.grimac.predictionengine.blockeffects.BlockEffectsResolver;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.fastutil.longs.LongSet;
import ac.grim.grimac.shaded.fastutil.objects.ObjectLinkedOpenHashSet;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BlockEffectsResolverV1_21_2 implements BlockEffectsResolver {
   public static BlockEffectsResolver INSTANCE = new BlockEffectsResolverV1_21_2();

   public void applyEffectsFromBlocks(GrimPlayer player, List<GrimPlayer.Movement> movements) {
      LongSet visitedBlocks = player.visitedBlocks;
      SimpleCollisionBox boundingBox = (player.inVehicle() ? GetBoundingBox.getCollisionBoxForPlayer(player, player.x, player.y, player.z) : player.boundingBox.copy()).expand(-9.999999747378752E-6D);
      Iterator var5 = movements.iterator();

      while(var5.hasNext()) {
         GrimPlayer.Movement movement = (GrimPlayer.Movement)var5.next();
         Vector3d from = movement.from();
         Vector3d to = movement.to();
         Iterator var9 = boxTraverseBlocks(from, to, boundingBox).iterator();

         while(var9.hasNext()) {
            Vector3i blockPos = (Vector3i)var9.next();
            WrappedBlockState blockState = player.compensatedWorld.getBlock(blockPos);
            StateType blockType = blockState.getType();
            if (!blockType.isAir() && visitedBlocks.add(GrimMath.asLong(blockPos))) {
               Collisions.onInsideBlock(player, blockType, blockState, blockPos.x, blockPos.y, blockPos.z, true);
            }
         }
      }

      visitedBlocks.clear();
   }

   private static Iterable<Vector3i> boxTraverseBlocks(Vector3d start, Vector3d end, SimpleCollisionBox boundingBox) {
      Vector3d direction = end.subtract(start);
      Iterable<Vector3i> initialBlocks = SimpleCollisionBox.betweenClosed(boundingBox);
      if (direction.lengthSquared() < (double)GrimMath.square(0.99999F)) {
         return initialBlocks;
      } else {
         Set<Vector3i> traversedBlocks = new ObjectLinkedOpenHashSet();
         Vector3d normalizedDirection = direction.normalize().multiply(1.0E-7D);
         Vector3d boxMinPosition = boundingBox.min().toVector3d().add(normalizedDirection);
         Vector3d subtractedMinPosition = boundingBox.min().toVector3d().subtract(direction).subtract(normalizedDirection);
         addCollisionsAlongTravel(traversedBlocks, subtractedMinPosition, boxMinPosition, boundingBox);
         Iterator var9 = initialBlocks.iterator();

         while(var9.hasNext()) {
            Vector3i blockPos = (Vector3i)var9.next();
            traversedBlocks.add(blockPos);
         }

         return traversedBlocks;
      }
   }

   public static void addCollisionsAlongTravel(Set<Vector3i> output, Vector3d start, Vector3d end, SimpleCollisionBox boundingBox) {
      Vector3d direction = end.subtract(start);
      int currentX = GrimMath.floor(start.x);
      int currentY = GrimMath.floor(start.y);
      int currentZ = GrimMath.floor(start.z);
      int stepX = GrimMath.sign(direction.x);
      int stepY = GrimMath.sign(direction.y);
      int stepZ = GrimMath.sign(direction.z);
      double tMaxX = stepX == 0 ? Double.MAX_VALUE : (double)stepX / direction.x;
      double tMaxY = stepY == 0 ? Double.MAX_VALUE : (double)stepY / direction.y;
      double tMaxZ = stepZ == 0 ? Double.MAX_VALUE : (double)stepZ / direction.z;
      double tDeltaX = tMaxX * (stepX > 0 ? 1.0D - GrimMath.frac(start.x) : GrimMath.frac(start.x));
      double tDeltaY = tMaxY * (stepY > 0 ? 1.0D - GrimMath.frac(start.y) : GrimMath.frac(start.y));
      double tDeltaZ = tMaxZ * (stepZ > 0 ? 1.0D - GrimMath.frac(start.z) : GrimMath.frac(start.z));
      int var23 = 0;

      while(tDeltaX <= 1.0D || tDeltaY <= 1.0D || tDeltaZ <= 1.0D) {
         if (tDeltaX < tDeltaY) {
            if (tDeltaX < tDeltaZ) {
               currentX += stepX;
               tDeltaX += tMaxX;
            } else {
               currentZ += stepZ;
               tDeltaZ += tMaxZ;
            }
         } else if (tDeltaY < tDeltaZ) {
            currentY += stepY;
            tDeltaY += tMaxY;
         } else {
            currentZ += stepZ;
            tDeltaZ += tMaxZ;
         }

         if (var23++ > 16) {
            break;
         }

         Optional<Vector3d> collisionPoint = BlockCollisions.clip((double)currentX, (double)currentY, (double)currentZ, (double)(currentX + 1), (double)(currentY + 1), (double)(currentZ + 1), start, end);
         if (!collisionPoint.isEmpty()) {
            Vector3d collisionVec = (Vector3d)collisionPoint.get();
            double clampedX = GrimMath.clamp(collisionVec.x, (double)((float)currentX + 1.0E-5F), (double)currentX + 1.0D - 9.999999747378752E-6D);
            double clampedY = GrimMath.clamp(collisionVec.y, (double)((float)currentY + 1.0E-5F), (double)currentY + 1.0D - 9.999999747378752E-6D);
            double clampedZ = GrimMath.clamp(collisionVec.z, (double)((float)currentZ + 1.0E-5F), (double)currentZ + 1.0D - 9.999999747378752E-6D);
            int endX = GrimMath.floor(clampedX + boundingBox.getXSize());
            int endY = GrimMath.floor(clampedY + boundingBox.getYSize());
            int endZ = GrimMath.floor(clampedZ + boundingBox.getZSize());

            for(int x = currentX; x <= endX; ++x) {
               for(int y = currentY; y <= endY; ++y) {
                  for(int z = currentZ; z <= endZ; ++z) {
                     output.add(new Vector3i(x, y, z));
                  }
               }
            }
         }
      }

   }
}
