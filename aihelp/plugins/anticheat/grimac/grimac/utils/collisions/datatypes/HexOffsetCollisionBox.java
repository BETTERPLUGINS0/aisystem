package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;

public class HexOffsetCollisionBox extends OffsetCollisionBox {
   public HexOffsetCollisionBox(StateType block, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
      super(block, minX / 16.0D, minY / 16.0D, minZ / 16.0D, maxX / 16.0D, maxY / 16.0D, maxZ / 16.0D);
   }
}
