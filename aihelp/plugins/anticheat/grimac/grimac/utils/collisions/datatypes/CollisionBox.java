package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import java.util.List;

public interface CollisionBox {
   CollisionBox union(SimpleCollisionBox var1);

   boolean isCollided(SimpleCollisionBox var1);

   boolean isIntersected(SimpleCollisionBox var1);

   CollisionBox copy();

   CollisionBox offset(double var1, double var3, double var5);

   void downCast(List<SimpleCollisionBox> var1);

   int downCast(SimpleCollisionBox[] var1);

   boolean isNull();

   boolean isFullBlock();

   default boolean isSideFullBlock(BlockFace axis) {
      return this.isFullBlock();
   }
}
