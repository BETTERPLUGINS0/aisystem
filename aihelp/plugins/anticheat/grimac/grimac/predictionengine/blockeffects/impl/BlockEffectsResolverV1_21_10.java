package ac.grim.grimac.predictionengine.blockeffects.impl;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.blockeffects.BlockCollisions;
import ac.grim.grimac.predictionengine.blockeffects.BlockEffectsResolver;
import ac.grim.grimac.predictionengine.blockeffects.BlockStepVisitor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.fastutil.longs.LongOpenHashSet;
import ac.grim.grimac.shaded.fastutil.longs.LongSet;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockEffectsResolverV1_21_10 implements BlockEffectsResolver {
   public static final BlockEffectsResolver INSTANCE = new BlockEffectsResolverV1_21_10();

   public void applyEffectsFromBlocks(GrimPlayer player, List<GrimPlayer.Movement> movements) {
      LongSet visitedBlocks = player.visitedBlocks;
      Iterator var4 = movements.iterator();

      while(var4.hasNext()) {
         GrimPlayer.Movement movement = (GrimPlayer.Movement)var4.next();
         Vector3d from = movement.from();
         Vector3d to = movement.to().subtract(movement.from());
         int iterationCount = 16;
         if (movement.axisIndependant() && to.lengthSquared() > 0.0D) {
            UnmodifiableIterator var9 = BlockCollisions.axisStepOrder(movement.axisDependentOriginalMovement()).iterator();

            while(var9.hasNext()) {
               Collisions.Axis axis = (Collisions.Axis)var9.next();
               double value = axis.get(to);
               if (value != 0.0D) {
                  Vector3d vector = BlockCollisions.relative(from, axis.getPositive(), value);
                  iterationCount -= checkInsideBlocks(player, from, vector, visitedBlocks, iterationCount);
                  from = vector;
               }
            }
         } else {
            iterationCount -= checkInsideBlocks(player, movement.from(), movement.to(), visitedBlocks, 16);
         }

         if (iterationCount <= 0) {
            checkInsideBlocks(player, movement.to(), movement.to(), visitedBlocks, 1);
         }
      }

      visitedBlocks.clear();
   }

   public static int checkInsideBlocks(GrimPlayer player, Vector3d from, Vector3d to, LongSet visitedBlocks, int count) {
      SimpleCollisionBox boundingBox = GetBoundingBox.getCollisionBoxForPlayer(player, to.x, to.y, to.z).expand(-9.999999747378752E-6D);
      boolean isFarEnough = from.distanceSquared(to) > GrimMath.square(0.9999900000002526D);
      AtomicInteger blockCount = new AtomicInteger();
      forEachBlockIntersectedBetween(from, to, boundingBox, (blockPos, localCount) -> {
         if (localCount >= count) {
            return false;
         } else {
            blockCount.set(localCount);
            WrappedBlockState blockState = player.compensatedWorld.getBlock(blockPos);
            StateType blockType = blockState.getType();
            if (blockType.isAir()) {
               return true;
            } else {
               if (visitedBlocks.add(GrimMath.asLong(blockPos))) {
                  boolean shouldApply = isFarEnough || boundingBox.intersects(blockPos);
                  Collisions.onInsideBlock(player, blockType, blockState, blockPos.x, blockPos.y, blockPos.z, shouldApply);
               }

               return true;
            }
         }
      });
      return blockCount.get() + 1;
   }

   public static boolean forEachBlockIntersectedBetween(Vector3d start, Vector3d end, SimpleCollisionBox boundingBox, BlockStepVisitor visitor) {
      Vector3d direction = end.subtract(start);
      if (direction.lengthSquared() < (double)GrimMath.square(1.0E-5F)) {
         Iterator var9 = SimpleCollisionBox.betweenClosed(boundingBox).iterator();

         Vector3i blockPos;
         do {
            if (!var9.hasNext()) {
               return true;
            }

            blockPos = (Vector3i)var9.next();
         } while(visitor.visit(blockPos, 0));

         return false;
      } else {
         LongSet alreadyVisited = new LongOpenHashSet();
         Iterator var6 = SimpleCollisionBox.betweenCornersInDirection(boundingBox.move(direction.multiply(-1.0D)), direction).iterator();

         while(var6.hasNext()) {
            Vector3i blockPos = (Vector3i)var6.next();
            if (!visitor.visit(blockPos, 0)) {
               return false;
            }

            alreadyVisited.add(GrimMath.asLong(blockPos));
         }

         int iterationCount = addCollisionsAlongTravel(alreadyVisited, direction, boundingBox, visitor);
         if (iterationCount < 0) {
            return false;
         } else {
            Iterator var12 = SimpleCollisionBox.betweenCornersInDirection(boundingBox, direction).iterator();

            Vector3i blockPos;
            do {
               if (!var12.hasNext()) {
                  return true;
               }

               blockPos = (Vector3i)var12.next();
            } while(!alreadyVisited.add(GrimMath.asLong(blockPos)) || visitor.visit(blockPos, iterationCount + 1));

            return false;
         }
      }
   }

   public static int addCollisionsAlongTravel(LongSet alreadyVisited, Vector3d direction, SimpleCollisionBox boundingBox, BlockStepVisitor visitor) {
      double sizeX = boundingBox.getXSize();
      double sizeY = boundingBox.getYSize();
      double sizeZ = boundingBox.getZSize();
      Vector3i furthestCorner = BlockCollisions.getFurthestCorner(direction);
      Vector3d center = boundingBox.getCenter();
      Vector3d end = new Vector3d(center.x + sizeX * 0.5D * (double)furthestCorner.getX(), center.y + sizeY * 0.5D * (double)furthestCorner.getY(), center.z + sizeZ * 0.5D * (double)furthestCorner.getZ());
      Vector3d start = end.subtract(direction);
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
      int iterationCount = 0;

      while(true) {
         Optional collisionPoint;
         do {
            if (!(tDeltaX <= 1.0D) && !(tDeltaY <= 1.0D) && !(tDeltaZ <= 1.0D)) {
               return iterationCount;
            }

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

            collisionPoint = BlockCollisions.clip((double)currentX, (double)currentY, (double)currentZ, (double)(currentX + 1), (double)(currentY + 1), (double)(currentZ + 1), start, end);
         } while(collisionPoint.isEmpty());

         ++iterationCount;
         Vector3d collisionVec = (Vector3d)collisionPoint.get();
         double clampedX = GrimMath.clamp(collisionVec.x, (double)((float)currentX + 1.0E-5F), (double)currentX + 1.0D - 9.999999747378752E-6D);
         double clampedY = GrimMath.clamp(collisionVec.y, (double)((float)currentY + 1.0E-5F), (double)currentY + 1.0D - 9.999999747378752E-6D);
         double clampedZ = GrimMath.clamp(collisionVec.z, (double)((float)currentZ + 1.0E-5F), (double)currentZ + 1.0D - 9.999999747378752E-6D);
         int endX = GrimMath.floor(clampedX - sizeX * (double)furthestCorner.getX());
         int endY = GrimMath.floor(clampedY - sizeY * (double)furthestCorner.getY());
         int endZ = GrimMath.floor(clampedZ - sizeZ * (double)furthestCorner.getZ());
         int copyIterationCount = iterationCount;
         Iterator var45 = SimpleCollisionBox.betweenCornersInDirection(currentX, currentY, currentZ, endX, endY, endZ, direction).iterator();

         while(var45.hasNext()) {
            Vector3i blockPos = (Vector3i)var45.next();
            if (alreadyVisited.add(GrimMath.asLong(blockPos)) && !visitor.visit(blockPos, copyIterationCount)) {
               return -1;
            }
         }
      }
   }
}
