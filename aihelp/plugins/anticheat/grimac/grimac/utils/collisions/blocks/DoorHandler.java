package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Hinge;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.NoCollisionBox;

public class DoorHandler implements CollisionFactory {
   protected static final CollisionBox SOUTH_AABB = new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
   protected static final CollisionBox NORTH_AABB = new HexCollisionBox(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
   protected static final CollisionBox WEST_AABB = new HexCollisionBox(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
   protected static final CollisionBox EAST_AABB = new HexCollisionBox(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);

   public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
      Object var10000;
      switch(this.fetchDirection(player, version, block, x, y, z)) {
      case NORTH:
         var10000 = NORTH_AABB.copy();
         break;
      case SOUTH:
         var10000 = SOUTH_AABB.copy();
         break;
      case EAST:
         var10000 = EAST_AABB.copy();
         break;
      case WEST:
         var10000 = WEST_AABB.copy();
         break;
      default:
         var10000 = NoCollisionBox.INSTANCE;
      }

      return (CollisionBox)var10000;
   }

   public BlockFace fetchDirection(GrimPlayer player, ClientVersion version, WrappedBlockState door, int x, int y, int z) {
      BlockFace facingDirection;
      boolean isClosed;
      boolean isRightHinge;
      if (!PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2) && !version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         facingDirection = door.getFacing();
         isClosed = !door.isOpen();
         isRightHinge = door.getHinge() == Hinge.RIGHT;
      } else {
         WrappedBlockState above;
         if (door.getHalf() == Half.LOWER) {
            above = player.compensatedWorld.getBlock(x, y + 1, z);
            facingDirection = door.getFacing();
            isClosed = !door.isOpen();
            if (above.getType() == door.getType()) {
               isRightHinge = above.getHinge() == Hinge.RIGHT;
            } else {
               isRightHinge = false;
            }
         } else {
            above = player.compensatedWorld.getBlock(x, y - 1, z);
            if (above.getType() == door.getType() && above.getHalf() == Half.LOWER) {
               isClosed = !above.isOpen();
               facingDirection = above.getFacing();
               isRightHinge = door.getHinge() == Hinge.RIGHT;
            } else {
               facingDirection = BlockFace.EAST;
               isClosed = true;
               isRightHinge = false;
            }
         }
      }

      BlockFace var10000;
      switch(facingDirection) {
      case NORTH:
         var10000 = isClosed ? BlockFace.NORTH : (isRightHinge ? BlockFace.WEST : BlockFace.EAST);
         break;
      case SOUTH:
         var10000 = isClosed ? BlockFace.SOUTH : (isRightHinge ? BlockFace.EAST : BlockFace.WEST);
         break;
      case EAST:
      default:
         var10000 = isClosed ? BlockFace.EAST : (isRightHinge ? BlockFace.NORTH : BlockFace.SOUTH);
         break;
      case WEST:
         var10000 = isClosed ? BlockFace.WEST : (isRightHinge ? BlockFace.SOUTH : BlockFace.NORTH);
      }

      return var10000;
   }
}
