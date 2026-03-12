package ac.grim.grimac.predictionengine.blockeffects.impl;

import ac.grim.grimac.player.GrimPlayer;
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
import java.util.Set;

public class BlockEffectsResolverV1_21_4 implements BlockEffectsResolver {
   public static final BlockEffectsResolver INSTANCE = new BlockEffectsResolverV1_21_4();

   public void applyEffectsFromBlocks(GrimPlayer player, List<GrimPlayer.Movement> movements) {
      LongSet visitedBlocks = player.visitedBlocks;
      Iterator var4 = movements.iterator();

      while(var4.hasNext()) {
         GrimPlayer.Movement movement = (GrimPlayer.Movement)var4.next();
         Vector3d from = movement.from();
         Vector3d to = movement.to();
         SimpleCollisionBox boundingBox = GetBoundingBox.getCollisionBoxForPlayer(player, to.x, to.y, to.z).expand(-9.999999747378752E-6D);
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
         Vector3d boxMinPosition = boundingBox.min().toVector3d();
         Vector3d subtractedMinPosition = boxMinPosition.subtract(direction);
         BlockEffectsResolverV1_21_2.addCollisionsAlongTravel(traversedBlocks, subtractedMinPosition, boxMinPosition, boundingBox);
         Iterator var8 = initialBlocks.iterator();

         while(var8.hasNext()) {
            Vector3i blockPos = (Vector3i)var8.next();
            traversedBlocks.add(blockPos);
         }

         return traversedBlocks;
      }
   }
}
