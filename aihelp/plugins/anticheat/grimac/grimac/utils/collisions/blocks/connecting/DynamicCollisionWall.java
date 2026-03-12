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
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class DynamicCollisionWall extends DynamicConnecting implements CollisionFactory {
   private static final CollisionBox[] COLLISION_BOXES = makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, false, 1);
   private static final boolean isNewServer;

   /** @deprecated */
   @Deprecated
   public CollisionBox fetchRegularBox(GrimPlayer player, WrappedBlockState state, ClientVersion version, int x, int y, int z) {
      int up = false;
      int east = 0;
      int west = 0;
      int south = 0;
      int north = 0;
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_12_2)) {
         boolean sixteen = PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_16);
         if (state.getNorth() != North.NONE) {
            north += state.getNorth() != North.LOW && !sixteen ? 2 : 1;
         }

         if (state.getEast() != East.NONE) {
            east += state.getEast() != East.LOW && !sixteen ? 2 : 1;
         }

         if (state.getSouth() != South.NONE) {
            south += state.getSouth() != South.LOW && !sixteen ? 2 : 1;
         }

         if (state.getWest() != West.NONE) {
            west += state.getWest() != West.LOW && !sixteen ? 2 : 1;
         }

         if (state.isUp()) {
            up = true;
         }
      } else {
         north = this.connectsTo(player, version, x, y, z, BlockFace.NORTH) ? 1 : 0;
         south = this.connectsTo(player, version, x, y, z, BlockFace.SOUTH) ? 1 : 0;
         west = this.connectsTo(player, version, x, y, z, BlockFace.WEST) ? 1 : 0;
         east = this.connectsTo(player, version, x, y, z, BlockFace.EAST) ? 1 : 0;
         up = true;
      }

      if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         ComplexCollisionBox box = new ComplexCollisionBox(5);
         if (up) {
            box.add(new HexCollisionBox(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D));
         }

         if (north == 1) {
            box.add(new HexCollisionBox(5.0D, 0.0D, 0.0D, 11.0D, 14.0D, 11.0D));
         } else if (north == 2) {
            box.add(new HexCollisionBox(5.0D, 0.0D, 0.0D, 11.0D, 16.0D, 11.0D));
         }

         if (south == 1) {
            box.add(new HexCollisionBox(5.0D, 0.0D, 5.0D, 11.0D, 14.0D, 16.0D));
         } else if (south == 2) {
            box.add(new HexCollisionBox(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 16.0D));
         }

         if (west == 1) {
            box.add(new HexCollisionBox(0.0D, 0.0D, 5.0D, 11.0D, 14.0D, 11.0D));
         } else if (west == 2) {
            box.add(new HexCollisionBox(0.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D));
         }

         if (east == 1) {
            box.add(new HexCollisionBox(5.0D, 0.0D, 5.0D, 16.0D, 14.0D, 11.0D));
         } else if (east == 2) {
            box.add(new HexCollisionBox(5.0D, 0.0D, 5.0D, 16.0D, 16.0D, 11.0D));
         }

         return box;
      } else {
         float f = 0.25F;
         float f1 = 0.75F;
         float f2 = 0.25F;
         float f3 = 0.75F;
         if (north == 1) {
            f2 = 0.0F;
         }

         if (south == 1) {
            f3 = 1.0F;
         }

         if (west == 1) {
            f = 0.0F;
         }

         if (east == 1) {
            f1 = 1.0F;
         }

         if (north == 1 && south == 1 && west != 0 && east != 0) {
            f = 0.3125F;
            f1 = 0.6875F;
         } else if (north != 1 && south != 1 && west == 0 && east == 0) {
            f2 = 0.3125F;
            f3 = 0.6875F;
         }

         return new SimpleCollisionBox((double)f, 0.0D, (double)f2, (double)f1, 1.0D, (double)f3);
      }
   }

   public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
      boolean isNewClient = version.isNewerThan(ClientVersion.V_1_12_2);
      boolean north;
      boolean south;
      boolean west;
      boolean east;
      if (isNewServer && isNewClient) {
         north = block.getNorth() != North.NONE;
         south = block.getSouth() != South.NONE;
         west = block.getWest() != West.NONE;
         east = block.getEast() != East.NONE;
         return block.isUp() ? COLLISION_BOXES[this.getAABBIndex(north, east, south, west)].copy().union(new HexCollisionBox(4.0D, 0.0D, 4.0D, 12.0D, 24.0D, 12.0D)) : COLLISION_BOXES[this.getAABBIndex(north, east, south, west)].copy();
      } else {
         north = isNewServer ? block.getNorth() != North.NONE : this.connectsTo(player, version, x, y, z, BlockFace.NORTH);
         south = isNewServer ? block.getSouth() != South.NONE : this.connectsTo(player, version, x, y, z, BlockFace.SOUTH);
         west = isNewServer ? block.getWest() != West.NONE : this.connectsTo(player, version, x, y, z, BlockFace.WEST);
         east = isNewServer ? block.getEast() != East.NONE : this.connectsTo(player, version, x, y, z, BlockFace.EAST);
         if (!isNewServer && isNewClient) {
            boolean up = this.connectsTo(player, version, x, y, z, BlockFace.UP);
            if (!up) {
               WrappedBlockState currBlock = player.compensatedWorld.getBlock(x, y, z);
               StateType currType = currBlock.getType();
               boolean selfNorth = currType == player.compensatedWorld.getBlock(x, y, z + 1).getType();
               boolean selfSouth = currType == player.compensatedWorld.getBlock(x, y, z - 1).getType();
               boolean selfWest = currType == player.compensatedWorld.getBlock(x - 1, y, z).getType();
               boolean selfEast = currType == player.compensatedWorld.getBlock(x + 1, y, z).getType();
               up = (!selfNorth || !selfSouth || selfWest || selfEast) && (!selfWest || !selfEast || selfNorth || selfSouth);
               return up ? COLLISION_BOXES[this.getAABBIndex(north, east, south, west)].copy().union(new HexCollisionBox(4.0D, 0.0D, 4.0D, 12.0D, 24.0D, 12.0D)) : COLLISION_BOXES[this.getAABBIndex(north, east, south, west)].copy();
            }
         }

         float f = 0.25F;
         float f1 = 0.75F;
         float f2 = 0.25F;
         float f3 = 0.75F;
         if (north) {
            f2 = 0.0F;
         }

         if (south) {
            f3 = 1.0F;
         }

         if (west) {
            f = 0.0F;
         }

         if (east) {
            f1 = 1.0F;
         }

         if (north && south && !west && !east) {
            f = 0.3125F;
            f1 = 0.6875F;
         } else if (!north && !south && west && east) {
            f2 = 0.3125F;
            f3 = 0.6875F;
         }

         return new SimpleCollisionBox((double)f, 0.0D, (double)f2, (double)f1, 1.5D, (double)f3);
      }
   }

   public boolean checkCanConnect(GrimPlayer player, WrappedBlockState state, StateType one, StateType two, BlockFace direction) {
      return BlockTags.WALLS.contains(one) || CollisionData.getData(one).getMovementCollisionBox(player, player.getClientVersion(), state, 0, 0, 0).isSideFullBlock(direction);
   }

   static {
      isNewServer = PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_12_2);
   }
}
