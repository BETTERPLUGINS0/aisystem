package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Type;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;

public class DynamicChest implements CollisionFactory {
   public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState chest, int x, int y, int z) {
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         if (chest.getTypeData() == Type.SINGLE) {
            return new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
         } else if ((chest.getFacing() != BlockFace.SOUTH || chest.getTypeData() != Type.RIGHT) && (chest.getFacing() != BlockFace.NORTH || chest.getTypeData() != Type.LEFT)) {
            if (chest.getFacing() == BlockFace.SOUTH && chest.getTypeData() == Type.LEFT || chest.getFacing() == BlockFace.NORTH && chest.getTypeData() == Type.RIGHT) {
               return new HexCollisionBox(0.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
            } else {
               return (chest.getFacing() != BlockFace.WEST || chest.getTypeData() != Type.RIGHT) && (chest.getFacing() != BlockFace.EAST || chest.getTypeData() != Type.LEFT) ? new HexCollisionBox(1.0D, 0.0D, 0.0D, 15.0D, 14.0D, 15.0D) : new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 16.0D);
            }
         } else {
            return new HexCollisionBox(1.0D, 0.0D, 1.0D, 16.0D, 14.0D, 15.0D);
         }
      } else {
         WrappedBlockState westState;
         WrappedBlockState eastState;
         if (chest.getFacing() != BlockFace.EAST && chest.getFacing() != BlockFace.WEST) {
            westState = player.compensatedWorld.getBlock(x, y, z - 1);
            if (westState.getType() == chest.getType()) {
               return new HexCollisionBox(1.0D, 0.0D, 0.0D, 15.0D, 14.0D, 15.0D);
            }

            eastState = player.compensatedWorld.getBlock(x, y, z + 1);
            if (eastState.getType() == chest.getType()) {
               return new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 16.0D);
            }
         } else {
            westState = player.compensatedWorld.getBlock(x - 1, y, z);
            if (westState.getType() == chest.getType()) {
               return new HexCollisionBox(0.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
            }

            eastState = player.compensatedWorld.getBlock(x + 1, y, z);
            if (eastState.getType() == chest.getType()) {
               return new HexCollisionBox(1.0D, 0.0D, 1.0D, 16.0D, 14.0D, 15.0D);
            }
         }

         return new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
      }
   }
}
