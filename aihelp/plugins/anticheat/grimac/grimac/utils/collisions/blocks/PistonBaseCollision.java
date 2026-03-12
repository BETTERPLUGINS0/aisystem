package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class PistonBaseCollision implements CollisionFactory {
   public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
      if (!block.isExtended()) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else {
         HexCollisionBox var10000;
         switch(block.getFacing()) {
         case UP:
            var10000 = new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
            break;
         case NORTH:
            var10000 = new HexCollisionBox(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 16.0D);
            break;
         case SOUTH:
            var10000 = new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 12.0D);
            break;
         case WEST:
            var10000 = new HexCollisionBox(4.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
            break;
         case EAST:
            var10000 = new HexCollisionBox(0.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
            break;
         default:
            var10000 = new HexCollisionBox(0.0D, 4.0D, 0.0D, 16.0D, 16.0D, 16.0D);
         }

         return var10000;
      }
   }
}
