package ac.grim.grimac.utils.collisions.blocks.connecting;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.NoCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.nmsutil.Materials;

public class DynamicConnecting {
   public static CollisionBox[] makeShapes(float p_196408_1_, float p_196408_2_, float p_196408_3_, float p_196408_4_, float p_196408_5_, boolean includeCenter, int additionalMaxIndex) {
      float middleMin = 8.0F - p_196408_1_;
      float middleMax = 8.0F + p_196408_1_;
      float f2 = 8.0F - p_196408_2_;
      float f3 = 8.0F + p_196408_2_;
      SimpleCollisionBox up = new HexCollisionBox((double)middleMin, 0.0D, (double)middleMin, (double)middleMax, (double)p_196408_3_, (double)middleMax);
      SimpleCollisionBox voxelshape1 = new HexCollisionBox((double)f2, (double)p_196408_4_, 0.0D, (double)f3, (double)p_196408_5_, (double)f3);
      SimpleCollisionBox voxelshape2 = new HexCollisionBox((double)f2, (double)p_196408_4_, (double)f2, (double)f3, (double)p_196408_5_, 16.0D);
      SimpleCollisionBox voxelshape3 = new HexCollisionBox(0.0D, (double)p_196408_4_, (double)f2, (double)f3, (double)p_196408_5_, (double)f3);
      SimpleCollisionBox voxelshape4 = new HexCollisionBox((double)f2, (double)p_196408_4_, (double)f2, 16.0D, (double)p_196408_5_, (double)f3);
      ComplexCollisionBox voxelshape5 = new ComplexCollisionBox(2 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape1, voxelshape4});
      ComplexCollisionBox voxelshape6 = new ComplexCollisionBox(2 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape2, voxelshape3});
      CollisionBox[] avoxelshape = new CollisionBox[]{NoCollisionBox.INSTANCE, voxelshape2, voxelshape3, voxelshape6, voxelshape1, new ComplexCollisionBox(2 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape2, voxelshape1}), new ComplexCollisionBox(2 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape3, voxelshape1}), new ComplexCollisionBox(3 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape2, voxelshape3, voxelshape1}), voxelshape4, new ComplexCollisionBox(2 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape2, voxelshape4}), new ComplexCollisionBox(2 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape3, voxelshape4}), new ComplexCollisionBox(3 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape2, voxelshape3, voxelshape4}), voxelshape5, new ComplexCollisionBox(3 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape2, voxelshape1, voxelshape4}), new ComplexCollisionBox(3 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape3, voxelshape1, voxelshape4}), new ComplexCollisionBox(4 + additionalMaxIndex, new SimpleCollisionBox[]{voxelshape1, voxelshape2, voxelshape3, voxelshape4})};
      if (includeCenter) {
         for(int i = 0; i < 16; ++i) {
            avoxelshape[i] = avoxelshape[i].union(up);
         }
      }

      return avoxelshape;
   }

   public boolean connectsTo(GrimPlayer player, ClientVersion v, int currX, int currY, int currZ, BlockFace direction) {
      WrappedBlockState targetBlock = player.compensatedWorld.getBlock(currX + direction.getModX(), currY + direction.getModY(), currZ + direction.getModZ());
      WrappedBlockState currBlock = player.compensatedWorld.getBlock(currX, currY, currZ);
      StateType target = targetBlock.getType();
      StateType fence = currBlock.getType();
      if (!BlockTags.FENCES.contains(target) && this.isBlacklisted(target, fence, v)) {
         return false;
      } else if (target == StateTypes.TNT) {
         return v.isNewerThanOrEquals(ClientVersion.V_1_12);
      } else if (target == StateTypes.BARRIER) {
         return player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10) || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_11_1);
      } else if (BlockTags.STAIRS.contains(target)) {
         if (!v.isOlderThan(ClientVersion.V_1_12) && (!PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_11) || !v.isNewerThanOrEquals(ClientVersion.V_1_13))) {
            return targetBlock.getFacing().getOppositeFace() == direction;
         } else {
            return false;
         }
      } else if (this.canConnectToGate(fence) && BlockTags.FENCE_GATES.contains(target)) {
         if (v.isOlderThanOrEquals(ClientVersion.V_1_11_1)) {
            return true;
         } else {
            BlockFace f1 = targetBlock.getFacing();
            BlockFace f2 = f1.getOppositeFace();
            return direction != f1 && direction != f2;
         }
      } else {
         return fence == target ? true : this.checkCanConnect(player, targetBlock, target, fence, direction);
      }
   }

   boolean isBlacklisted(StateType m, StateType fence, ClientVersion clientVersion) {
      if (BlockTags.LEAVES.contains(m)) {
         return clientVersion.isNewerThan(ClientVersion.V_1_8) || !Materials.isGlassPane(fence);
      } else if (BlockTags.SHULKER_BOXES.contains(m)) {
         return true;
      } else if (BlockTags.TRAPDOORS.contains(m)) {
         return true;
      } else {
         return m == StateTypes.ENCHANTING_TABLE || m == StateTypes.FARMLAND || m == StateTypes.CARVED_PUMPKIN || m == StateTypes.JACK_O_LANTERN || m == StateTypes.PUMPKIN || m == StateTypes.MELON || m == StateTypes.BEACON || BlockTags.CAULDRONS.contains(m) || m == StateTypes.GLOWSTONE || m == StateTypes.SEA_LANTERN || m == StateTypes.ICE || m == StateTypes.PISTON || m == StateTypes.STICKY_PISTON || m == StateTypes.PISTON_HEAD || !this.canConnectToGlassBlock() && BlockTags.GLASS_BLOCKS.contains(m);
      }
   }

   protected int getAABBIndex(boolean north, boolean east, boolean south, boolean west) {
      int i = 0;
      if (north) {
         i |= 4;
      }

      if (east) {
         i |= 8;
      }

      if (south) {
         i |= 1;
      }

      if (west) {
         i |= 2;
      }

      return i;
   }

   public boolean checkCanConnect(GrimPlayer player, WrappedBlockState state, StateType one, StateType two, BlockFace direction) {
      return false;
   }

   public boolean canConnectToGlassBlock() {
      return false;
   }

   public boolean canConnectToGate(StateType fence) {
      return !Materials.isGlassPane(fence);
   }
}
