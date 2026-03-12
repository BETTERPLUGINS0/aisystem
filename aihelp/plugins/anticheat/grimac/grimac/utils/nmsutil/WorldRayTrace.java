package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.HitboxData;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.HitData;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import lombok.Generated;

public final class WorldRayTrace {
   public static HitData getNearestBlockHitResult(GrimPlayer player, StateType heldItem, boolean sourcesHaveHitbox, boolean fluidPlacement, boolean itemUsePlacement) {
      Vector3d startingPos = new Vector3d(player.x, player.y + player.getEyeHeight(), player.z);
      Vector3dm startingVec = new Vector3dm(startingPos.getX(), startingPos.getY(), startingPos.getZ());
      Ray trace = new Ray(player, startingPos.getX(), startingPos.getY(), startingPos.getZ(), player.yaw, player.pitch);
      double distance = itemUsePlacement && player.getClientVersion().isOlderThan(ClientVersion.V_1_20_5) ? 5.0D : player.compensatedEntities.self.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
      Vector3dm endVec = trace.getPointAtDistance(distance);
      Vector3d endPos = new Vector3d(endVec.getX(), endVec.getY(), endVec.getZ());
      return traverseBlocks(player, startingPos, endPos, (block, vector3i) -> {
         if (fluidPlacement && player.getClientVersion().isOlderThan(ClientVersion.V_1_13) && CollisionData.getData(block.getType()).getMovementCollisionBox(player, player.getClientVersion(), block, vector3i.getX(), vector3i.getY(), vector3i.getZ()).isNull()) {
            return null;
         } else {
            CollisionBox data = HitboxData.getBlockHitbox(player, heldItem, player.getClientVersion(), block, false, vector3i.getX(), vector3i.getY(), vector3i.getZ());
            List<SimpleCollisionBox> boxes = new ArrayList();
            data.downCast((List)boxes);
            double bestHitResult = Double.MAX_VALUE;
            Vector3dm bestHitLoc = null;
            BlockFace bestFace = null;
            Iterator var16 = boxes.iterator();

            while(var16.hasNext()) {
               SimpleCollisionBox box = (SimpleCollisionBox)var16.next();
               Pair<Vector3dm, BlockFace> interceptx = ReachUtils.calculateIntercept(box, trace.getOrigin(), trace.getPointAtDistance(distance));
               if (interceptx.first() != null) {
                  Vector3dm hitLoc = (Vector3dm)interceptx.first();
                  if (hitLoc.distanceSquared(startingVec) < bestHitResult) {
                     bestHitResult = hitLoc.distanceSquared(startingVec);
                     bestHitLoc = hitLoc;
                     bestFace = (BlockFace)interceptx.second();
                  }
               }
            }

            if (bestHitLoc != null) {
               return new HitData(vector3i, bestHitLoc, bestFace, block);
            } else {
               if (sourcesHaveHitbox && (player.compensatedWorld.isWaterSourceBlock(vector3i.getX(), vector3i.getY(), vector3i.getZ()) || player.compensatedWorld.getLavaFluidLevelAt(vector3i.getX(), vector3i.getY(), vector3i.getZ()) == 0.8888888955116272D)) {
                  double waterHeight = player.getClientVersion().isOlderThan(ClientVersion.V_1_13) ? 1.0D : player.compensatedWorld.getFluidLevelAt(vector3i.getX(), vector3i.getY(), vector3i.getZ());
                  SimpleCollisionBox boxx = new SimpleCollisionBox((double)vector3i.getX(), (double)vector3i.getY(), (double)vector3i.getZ(), (double)(vector3i.getX() + 1), (double)vector3i.getY() + waterHeight, (double)(vector3i.getZ() + 1));
                  Pair<Vector3dm, BlockFace> intercept = ReachUtils.calculateIntercept(boxx, trace.getOrigin(), trace.getPointAtDistance(distance));
                  if (intercept.first() != null) {
                     return new HitData(vector3i, (Vector3dm)intercept.first(), (BlockFace)intercept.second(), block);
                  }
               }

               return null;
            }
         }
      });
   }

   public static HitData traverseBlocks(GrimPlayer player, Vector3d start, Vector3d end, BiFunction<WrappedBlockState, Vector3i, HitData> predicate) {
      double endX = GrimMath.lerp(-1.0E-7D, end.x, start.x);
      double endY = GrimMath.lerp(-1.0E-7D, end.y, start.y);
      double endZ = GrimMath.lerp(-1.0E-7D, end.z, start.z);
      double startX = GrimMath.lerp(-1.0E-7D, start.x, end.x);
      double startY = GrimMath.lerp(-1.0E-7D, start.y, end.y);
      double startZ = GrimMath.lerp(-1.0E-7D, start.z, end.z);
      int floorStartX = GrimMath.floor(startX);
      int floorStartY = GrimMath.floor(startY);
      int floorStartZ = GrimMath.floor(startZ);
      if (start.equals(end)) {
         return null;
      } else {
         WrappedBlockState state = player.compensatedWorld.getBlock(floorStartX, floorStartY, floorStartZ);
         HitData apply = (HitData)predicate.apply(state, new Vector3i(floorStartX, floorStartY, floorStartZ));
         if (apply != null) {
            return apply;
         } else {
            double xDiff = endX - startX;
            double yDiff = endY - startY;
            double zDiff = endZ - startZ;
            double xSign = Math.signum(xDiff);
            double ySign = Math.signum(yDiff);
            double zSign = Math.signum(zDiff);
            double posXInverse = xSign == 0.0D ? Double.MAX_VALUE : xSign / xDiff;
            double posYInverse = ySign == 0.0D ? Double.MAX_VALUE : ySign / yDiff;
            double posZInverse = zSign == 0.0D ? Double.MAX_VALUE : zSign / zDiff;
            double d12 = posXInverse * (xSign > 0.0D ? 1.0D - GrimMath.frac(startX) : GrimMath.frac(startX));
            double d13 = posYInverse * (ySign > 0.0D ? 1.0D - GrimMath.frac(startY) : GrimMath.frac(startY));
            double d14 = posZInverse * (zSign > 0.0D ? 1.0D - GrimMath.frac(startZ) : GrimMath.frac(startZ));

            do {
               if (!(d12 <= 1.0D) && !(d13 <= 1.0D) && !(d14 <= 1.0D)) {
                  return null;
               }

               if (d12 < d13) {
                  if (d12 < d14) {
                     floorStartX = (int)((double)floorStartX + xSign);
                     d12 += posXInverse;
                  } else {
                     floorStartZ = (int)((double)floorStartZ + zSign);
                     d14 += posZInverse;
                  }
               } else if (d13 < d14) {
                  floorStartY = (int)((double)floorStartY + ySign);
                  d13 += posYInverse;
               } else {
                  floorStartZ = (int)((double)floorStartZ + zSign);
                  d14 += posZInverse;
               }

               state = player.compensatedWorld.getBlock(floorStartX, floorStartY, floorStartZ);
               apply = (HitData)predicate.apply(state, new Vector3i(floorStartX, floorStartY, floorStartZ));
            } while(apply == null);

            return apply;
         }
      }
   }

   @Generated
   private WorldRayTrace() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
