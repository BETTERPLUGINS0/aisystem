package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Shape;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.nmsutil.Materials;
import java.util.stream.IntStream;

public class DynamicStair implements CollisionFactory {
   protected static final SimpleCollisionBox TOP_AABB = new HexCollisionBox(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
   protected static final SimpleCollisionBox BOTTOM_AABB = new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
   protected static final SimpleCollisionBox OCTET_NNN = new HexCollisionBox(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);
   protected static final SimpleCollisionBox OCTET_NNP = new HexCollisionBox(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);
   protected static final SimpleCollisionBox OCTET_NPN = new HexCollisionBox(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D);
   protected static final SimpleCollisionBox OCTET_NPP = new HexCollisionBox(0.0D, 8.0D, 8.0D, 8.0D, 16.0D, 16.0D);
   protected static final SimpleCollisionBox OCTET_PNN = new HexCollisionBox(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
   protected static final SimpleCollisionBox OCTET_PNP = new HexCollisionBox(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
   protected static final SimpleCollisionBox OCTET_PPN = new HexCollisionBox(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
   protected static final SimpleCollisionBox OCTET_PPP = new HexCollisionBox(8.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);
   protected static final CollisionBox[] TOP_SHAPES;
   protected static final CollisionBox[] BOTTOM_SHAPES;
   private static final int[] SHAPE_BY_STATE;

   public static DynamicStair.EnumShape getStairsShape(GrimPlayer player, WrappedBlockState originalStairs, int x, int y, int z) {
      BlockFace facing = originalStairs.getFacing();
      WrappedBlockState offsetOne = player.compensatedWorld.getBlock(x + facing.getModX(), y + facing.getModY(), z + facing.getModZ());
      if (Materials.isStairs(offsetOne.getType()) && originalStairs.getHalf() == offsetOne.getHalf()) {
         BlockFace enumfacing1 = offsetOne.getFacing();
         if (isDifferentAxis(facing, enumfacing1) && canTakeShape(player, originalStairs, x + enumfacing1.getOppositeFace().getModX(), y + enumfacing1.getOppositeFace().getModY(), z + enumfacing1.getOppositeFace().getModZ())) {
            if (enumfacing1 == rotateYCCW(facing)) {
               return DynamicStair.EnumShape.OUTER_LEFT;
            }

            return DynamicStair.EnumShape.OUTER_RIGHT;
         }
      }

      WrappedBlockState offsetTwo = player.compensatedWorld.getBlock(x + facing.getOppositeFace().getModX(), y + facing.getOppositeFace().getModY(), z + facing.getOppositeFace().getModZ());
      if (Materials.isStairs(offsetTwo.getType()) && originalStairs.getHalf() == offsetTwo.getHalf()) {
         BlockFace enumfacing2 = offsetTwo.getFacing();
         if (isDifferentAxis(facing, enumfacing2) && canTakeShape(player, originalStairs, x + enumfacing2.getModX(), y + enumfacing2.getModY(), z + enumfacing2.getModZ())) {
            if (enumfacing2 == rotateYCCW(facing)) {
               return DynamicStair.EnumShape.INNER_LEFT;
            }

            return DynamicStair.EnumShape.INNER_RIGHT;
         }
      }

      return DynamicStair.EnumShape.STRAIGHT;
   }

   private static boolean canTakeShape(GrimPlayer player, WrappedBlockState stairOne, int x, int y, int z) {
      WrappedBlockState otherStair = player.compensatedWorld.getBlock(x, y, z);
      return !BlockTags.STAIRS.contains(otherStair.getType()) || stairOne.getFacing() != otherStair.getFacing() || stairOne.getHalf() != otherStair.getHalf();
   }

   private static boolean isDifferentAxis(BlockFace faceOne, BlockFace faceTwo) {
      return faceOne.getOppositeFace() != faceTwo && faceOne != faceTwo;
   }

   private static BlockFace rotateYCCW(BlockFace face) {
      BlockFace var10000;
      switch(face) {
      case EAST:
         var10000 = BlockFace.NORTH;
         break;
      case SOUTH:
         var10000 = BlockFace.EAST;
         break;
      case WEST:
         var10000 = BlockFace.SOUTH;
         break;
      default:
         var10000 = BlockFace.WEST;
      }

      return var10000;
   }

   private static CollisionBox[] makeShapes(SimpleCollisionBox p_199779_0_, SimpleCollisionBox p_199779_1_, SimpleCollisionBox p_199779_2_, SimpleCollisionBox p_199779_3_, SimpleCollisionBox p_199779_4_) {
      return (CollisionBox[])IntStream.range(0, 16).mapToObj((p_199780_5_) -> {
         return makeStairShape(p_199780_5_, p_199779_0_, p_199779_1_, p_199779_2_, p_199779_3_, p_199779_4_);
      }).toArray((x$0) -> {
         return new CollisionBox[x$0];
      });
   }

   private static CollisionBox makeStairShape(int p_199781_0_, SimpleCollisionBox p_199781_1_, SimpleCollisionBox p_199781_2_, SimpleCollisionBox p_199781_3_, SimpleCollisionBox p_199781_4_, SimpleCollisionBox p_199781_5_) {
      ComplexCollisionBox voxelshape = new ComplexCollisionBox(5, new SimpleCollisionBox[]{p_199781_1_});
      if ((p_199781_0_ & 1) != 0) {
         voxelshape.add(p_199781_2_);
      }

      if ((p_199781_0_ & 2) != 0) {
         voxelshape.add(p_199781_3_);
      }

      if ((p_199781_0_ & 4) != 0) {
         voxelshape.add(p_199781_4_);
      }

      if ((p_199781_0_ & 8) != 0) {
         voxelshape.add(p_199781_5_);
      }

      return voxelshape;
   }

   public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
      int shapeOrdinal;
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         shapeOrdinal = this.toEnumShape(block.getShape()).ordinal();
      } else {
         DynamicStair.EnumShape shape = getStairsShape(player, block, x, y, z);
         shapeOrdinal = shape.ordinal();
      }

      return (block.getHalf() == Half.BOTTOM ? BOTTOM_SHAPES : TOP_SHAPES)[SHAPE_BY_STATE[this.getShapeIndex(block, shapeOrdinal)]].copy();
   }

   private int getShapeIndex(WrappedBlockState state, int shapeOrdinal) {
      return shapeOrdinal * 4 + this.directionToValue(state.getFacing());
   }

   private int directionToValue(BlockFace face) {
      byte var10000;
      switch(face) {
      case EAST:
         var10000 = 3;
         break;
      case SOUTH:
         var10000 = 0;
         break;
      case WEST:
         var10000 = 1;
         break;
      case NORTH:
         var10000 = 2;
         break;
      default:
         var10000 = -1;
      }

      return var10000;
   }

   private DynamicStair.EnumShape toEnumShape(Shape shape) {
      DynamicStair.EnumShape var10000;
      switch(shape) {
      case INNER_LEFT:
         var10000 = DynamicStair.EnumShape.INNER_LEFT;
         break;
      case INNER_RIGHT:
         var10000 = DynamicStair.EnumShape.INNER_RIGHT;
         break;
      case OUTER_LEFT:
         var10000 = DynamicStair.EnumShape.OUTER_LEFT;
         break;
      case OUTER_RIGHT:
         var10000 = DynamicStair.EnumShape.OUTER_RIGHT;
         break;
      default:
         var10000 = DynamicStair.EnumShape.STRAIGHT;
      }

      return var10000;
   }

   static {
      TOP_SHAPES = makeShapes(TOP_AABB, OCTET_NNN, OCTET_PNN, OCTET_NNP, OCTET_PNP);
      BOTTOM_SHAPES = makeShapes(BOTTOM_AABB, OCTET_NPN, OCTET_PPN, OCTET_NPP, OCTET_PPP);
      SHAPE_BY_STATE = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};
   }

   static enum EnumShape {
      STRAIGHT,
      INNER_LEFT,
      INNER_RIGHT,
      OUTER_LEFT,
      OUTER_RIGHT;

      // $FF: synthetic method
      private static DynamicStair.EnumShape[] $values() {
         return new DynamicStair.EnumShape[]{STRAIGHT, INNER_LEFT, INNER_RIGHT, OUTER_LEFT, OUTER_RIGHT};
      }
   }
}
