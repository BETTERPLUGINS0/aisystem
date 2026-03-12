package ac.grim.grimac.utils.collisions.blocks.connecting;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.East;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.North;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.South;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.West;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class DynamicCollisionPane extends DynamicConnecting implements CollisionFactory {
   private static final CollisionBox[] COLLISION_BOXES = makeShapes(1.0F, 1.0F, 16.0F, 0.0F, 16.0F, true, 1);

   public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
      boolean east;
      boolean north;
      boolean south;
      boolean west;
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         east = block.getEast() != East.FALSE;
         north = block.getNorth() != North.FALSE;
         south = block.getSouth() != South.FALSE;
         west = block.getWest() != West.FALSE;
      } else {
         east = this.connectsTo(player, version, x, y, z, BlockFace.EAST);
         north = this.connectsTo(player, version, x, y, z, BlockFace.NORTH);
         south = this.connectsTo(player, version, x, y, z, BlockFace.SOUTH);
         west = this.connectsTo(player, version, x, y, z, BlockFace.WEST);
      }

      if (!north && !south && !east && !west && (version.isOlderThanOrEquals(ClientVersion.V_1_8) || PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8_8) && version.isNewerThanOrEquals(ClientVersion.V_1_13))) {
         west = true;
         east = true;
         south = true;
         north = true;
      }

      if (version.isNewerThanOrEquals(ClientVersion.V_1_9)) {
         return COLLISION_BOXES[this.getAABBIndex(north, east, south, west)].copy();
      } else {
         ComplexCollisionBox boxes = new ComplexCollisionBox(2);
         if ((!west || !east) && (west || east || north || south)) {
            if (west) {
               boxes.add(new SimpleCollisionBox(0.0D, 0.0D, 0.4375D, 0.5D, 1.0D, 0.5625D));
            } else if (east) {
               boxes.add(new SimpleCollisionBox(0.5D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D));
            }
         } else {
            boxes.add(new SimpleCollisionBox(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D));
         }

         if ((!north || !south) && (west || east || north || south)) {
            if (north) {
               boxes.add(new SimpleCollisionBox(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5D));
            } else if (south) {
               boxes.add(new SimpleCollisionBox(0.4375D, 0.0D, 0.5D, 0.5625D, 1.0D, 1.0D));
            }
         } else {
            boxes.add(new SimpleCollisionBox(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D));
         }

         return boxes;
      }
   }

   public boolean canConnectToGlassBlock() {
      return true;
   }

   public boolean checkCanConnect(GrimPlayer player, WrappedBlockState state, StateType one, StateType two, BlockFace direction) {
      return !BlockTags.GLASS_PANES.contains(one) && one != StateTypes.IRON_BARS && (one != StateTypes.CHAIN || !player.getClientVersion().isOlderThan(ClientVersion.V_1_16)) ? CollisionData.getData(one).getMovementCollisionBox(player, player.getClientVersion(), state, 0, 0, 0).isSideFullBlock(direction) : true;
   }
}
