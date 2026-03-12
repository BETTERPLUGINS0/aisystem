package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class PistonHeadCollision implements CollisionFactory {
   public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
      double longAmount = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && block.isShort() ? 0.0D : 4.0D;
      if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2) || PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
         longAmount = 4.0D;
      }

      if (version.isOlderThan(ClientVersion.V_1_9) || PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9)) {
         longAmount = 0.0D;
      }

      ComplexCollisionBox var10000;
      switch(block.getFacing()) {
      case UP:
         var10000 = new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D), new HexCollisionBox(6.0D, 0.0D - longAmount, 6.0D, 10.0D, 12.0D, 10.0D)});
         break;
      case NORTH:
         var10000 = new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 4.0D), new HexCollisionBox(6.0D, 6.0D, 4.0D, 10.0D, 10.0D, 16.0D + longAmount)});
         break;
      case SOUTH:
         var10000 = version.isOlderThanOrEquals(ClientVersion.V_1_8) ? new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 0.0D, 12.0D, 16.0D, 16.0D, 16.0D), new HexCollisionBox(4.0D, 6.0D, 0.0D, 12.0D, 10.0D, 12.0D)}) : new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 0.0D, 12.0D, 16.0D, 16.0D, 16.0D), new HexCollisionBox(6.0D, 6.0D, 0.0D - longAmount, 10.0D, 10.0D, 12.0D)});
         break;
      case WEST:
         var10000 = version.isOlderThanOrEquals(ClientVersion.V_1_8) ? new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 0.0D, 0.0D, 4.0D, 16.0D, 16.0D), new HexCollisionBox(6.0D, 4.0D, 4.0D, 10.0D, 12.0D, 16.0D)}) : new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 0.0D, 0.0D, 4.0D, 16.0D, 16.0D), new HexCollisionBox(4.0D, 6.0D, 6.0D, 16.0D + longAmount, 10.0D, 10.0D)});
         break;
      case EAST:
         var10000 = new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(12.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D), new HexCollisionBox(0.0D - longAmount, 6.0D, 4.0D, 12.0D, 10.0D, 12.0D)});
         break;
      default:
         var10000 = new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), new HexCollisionBox(6.0D, 4.0D, 6.0D, 10.0D, 16.0D + longAmount, 10.0D)});
      }

      return var10000;
   }
}
