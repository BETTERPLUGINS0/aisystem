package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.NoCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class TrapDoorHandler implements CollisionFactory {
   public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
      double var2 = 0.1875D;
      if (block.isOpen()) {
         switch(block.getFacing()) {
         case SOUTH:
            return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, var2, false);
         case NORTH:
            return new SimpleCollisionBox(0.0D, 0.0D, 1.0D - var2, 1.0D, 1.0D, 1.0D, false);
         case EAST:
            return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, var2, 1.0D, 1.0D, false);
         case WEST:
            return new SimpleCollisionBox(1.0D - var2, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, false);
         default:
            return NoCollisionBox.INSTANCE;
         }
      } else {
         return block.getHalf() == Half.BOTTOM ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, var2, 1.0D, false) : new SimpleCollisionBox(0.0D, 1.0D - var2, 0.0D, 1.0D, 1.0D, 1.0D, false);
      }
   }
}
