package ac.grim.grimac.utils.collisions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.East;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Face;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Leaves;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.North;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.South;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Tilt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.West;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicHitboxFence;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicHitboxPane;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicHitboxWall;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexOffsetCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HitBoxFactory;
import ac.grim.grimac.utils.collisions.datatypes.NoCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.OffsetCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.lists.ArrayUtils;
import ac.grim.grimac.utils.nmsutil.Materials;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum HitboxData implements HitBoxFactory {
   VINE((player, item, version, datax, isTargetBlock, x, y, z) -> {
      ComplexCollisionBox boxes = new ComplexCollisionBox(5);
      if (datax.getWest() == West.TRUE) {
         boxes.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D));
      }

      if (datax.getEast() == East.TRUE) {
         boxes.add(new HexCollisionBox(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D));
      }

      if (datax.getNorth() == North.TRUE) {
         boxes.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D));
      }

      if (datax.getSouth() == South.TRUE) {
         boxes.add(new HexCollisionBox(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D));
      }

      if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2) && boxes.size() > 1) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else {
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && datax.isUp()) {
            boxes.add(new HexCollisionBox(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D));
         }

         return boxes;
      }
   }, new StateType[]{StateTypes.VINE}),
   RAILS((player, item, version, datax, isTargetBlock, x, y, z) -> {
      Object var10000;
      switch(datax.getShape()) {
      case ASCENDING_NORTH:
      case ASCENDING_SOUTH:
      case ASCENDING_EAST:
      case ASCENDING_WEST:
         if (version.isOlderThan(ClientVersion.V_1_8)) {
            StateType railType = datax.getType();
            var10000 = railType != StateTypes.ACTIVATOR_RAIL && (railType != StateTypes.POWERED_RAIL || !datax.isPowered()) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D, false) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D, false);
         } else {
            var10000 = version.isOlderThan(ClientVersion.V_1_9) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D, false) : (version.isNewerThanOrEquals(ClientVersion.V_1_9) && version.isOlderThan(ClientVersion.V_1_10) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.15625D, 1.0D, false) : (version.isOlderThan(ClientVersion.V_1_11) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)));
         }
         break;
      default:
         var10000 = new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
      }

      return (CollisionBox)var10000;
   }, (StateType[])BlockTags.RAILS.getStates().toArray(new StateType[0])),
   END_PORTAL((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (version.isOlderThan(ClientVersion.V_1_9)) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
      } else {
         return (CollisionBox)(version.isOlderThan(ClientVersion.V_1_17) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D) : new HexCollisionBox(0.0D, 6.0D, 0.0D, 16.0D, 12.0D, 16.0D));
      }
   }, new StateType[]{StateTypes.END_PORTAL}),
   FENCE_GATE((player, item, version, datax, isTargetBlock, x, y, z) -> {
      boolean isXAxis = datax.getFacing() == BlockFace.WEST || datax.getFacing() == BlockFace.EAST;
      boolean isInWall;
      boolean zPosWall;
      boolean zNegWall;
      if (isXAxis) {
         zPosWall = Materials.isWall(player.compensatedWorld.getBlockType((double)x, (double)y, (double)(z + 1)));
         zNegWall = Materials.isWall(player.compensatedWorld.getBlockType((double)x, (double)y, (double)(z - 1)));
         isInWall = zPosWall || zNegWall;
      } else {
         zPosWall = Materials.isWall(player.compensatedWorld.getBlockType((double)(x + 1), (double)y, (double)z));
         zNegWall = Materials.isWall(player.compensatedWorld.getBlockType((double)(x - 1), (double)y, (double)z));
         isInWall = zPosWall || zNegWall;
      }

      if (isInWall) {
         return isXAxis ? new HexCollisionBox(6.0D, 0.0D, 0.0D, 10.0D, 13.0D, 16.0D) : new HexCollisionBox(0.0D, 0.0D, 6.0D, 16.0D, 13.0D, 10.0D);
      } else {
         return isXAxis ? new HexCollisionBox(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D) : new HexCollisionBox(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
      }
   }, (StateType[])BlockTags.FENCE_GATES.getStates().toArray(new StateType[0])),
   FENCE(new DynamicHitboxFence(), (StateType[])BlockTags.FENCES.getStates().toArray(new StateType[0])),
   PANE(new DynamicHitboxPane(), (StateType[])Materials.getPanes().toArray(new StateType[0])),
   LEVER((player, item, version, datax, isTargetBlock, x, y, z) -> {
      Face face = datax.getFace();
      BlockFace facing = datax.getFacing();
      if (version.isOlderThan(ClientVersion.V_1_13)) {
         double f = 0.1875D;
         switch(face) {
         case WALL:
            switch(facing) {
            case SOUTH:
               return new SimpleCollisionBox(0.5D - f, 0.2D, 0.0D, 0.5D + f, 0.8D, f * 2.0D, false);
            case WEST:
               return new SimpleCollisionBox(1.0D - f * 2.0D, 0.2D, 0.5D - f, 1.0D, 0.8D, 0.5D + f, false);
            case NORTH:
               return new SimpleCollisionBox(0.5D - f, 0.2D, 1.0D - f * 2.0D, 0.5D + f, 0.8D, 1.0D, false);
            case EAST:
               return new SimpleCollisionBox(0.0D, 0.2D, 0.5D - f, f * 2.0D, 0.8D, 0.5D + f, false);
            }
         case CEILING:
            return new SimpleCollisionBox(0.25D, 0.4D, 0.25D, 0.75D, 1.0D, 0.75D, false);
         case FLOOR:
            return new SimpleCollisionBox(0.25D, 0.0D, 0.25D, 0.75D, 0.6D, 0.75D, false);
         }
      }

      SimpleCollisionBox var10000;
      switch(face) {
      case WALL:
         switch(facing) {
         case SOUTH:
            var10000 = new SimpleCollisionBox(0.3125D, 0.25D, 0.0D, 0.6875D, 0.75D, 0.375D, false);
            return var10000;
         case WEST:
            var10000 = new SimpleCollisionBox(0.625D, 0.25D, 0.3125D, 1.0D, 0.75D, 0.6875D, false);
            return var10000;
         case NORTH:
         default:
            var10000 = new SimpleCollisionBox(0.3125D, 0.25D, 0.625D, 0.6875D, 0.75D, 1.0D, false);
            return var10000;
         case EAST:
            var10000 = new SimpleCollisionBox(0.0D, 0.25D, 0.3125D, 0.375D, 0.75D, 0.6875D, false);
            return var10000;
         }
      case FLOOR:
         var10000 = facing != BlockFace.EAST && facing != BlockFace.WEST ? new SimpleCollisionBox(0.3125D, 0.0D, 0.25D, 0.6875D, 0.375D, 0.75D, false) : new SimpleCollisionBox(0.25D, 0.0D, 0.3125D, 0.75D, 0.375D, 0.6875D, false);
         break;
      default:
         var10000 = facing != BlockFace.EAST && facing != BlockFace.WEST ? new SimpleCollisionBox(0.3125D, 0.625D, 0.25D, 0.6875D, 1.0D, 0.75D, false) : new SimpleCollisionBox(0.25D, 0.625D, 0.3125D, 0.75D, 1.0D, 0.6875D, false);
      }

      return var10000;
   }, new StateType[]{StateTypes.LEVER}),
   BUTTON((player, item, version, datax, isTargetBlock, x, y, z) -> {
      Face face = datax.getFace();
      BlockFace facing = datax.getFacing();
      boolean powered = datax.isPowered();
      if (version.isOlderThan(ClientVersion.V_1_13)) {
         double f2 = (double)((float)(datax.isPowered() ? 1 : 2)) / 16.0D;
         switch(face) {
         case WALL:
            switch(facing) {
            case SOUTH:
               return new SimpleCollisionBox(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, f2, false);
            case WEST:
               return new SimpleCollisionBox(1.0D - f2, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D, false);
            case NORTH:
               return new SimpleCollisionBox(0.3125D, 0.375D, 1.0D - f2, 0.6875D, 0.625D, 1.0D, false);
            case EAST:
               return new SimpleCollisionBox(0.0D, 0.375D, 0.3125D, f2, 0.625D, 0.6875D, false);
            }
         case CEILING:
            return new SimpleCollisionBox(0.3125D, 1.0D - f2, 0.375D, 0.6875D, 1.0D, 0.625D, false);
         case FLOOR:
            return new SimpleCollisionBox(0.3125D, 0.0D, 0.375D, 0.6875D, 0.0D + f2, 0.625D, false);
         }
      }

      switch(face) {
      case WALL:
         Object var10000;
         switch(facing) {
         case SOUTH:
            var10000 = powered ? new HexCollisionBox(5.0D, 6.0D, 0.0D, 11.0D, 10.0D, 1.0D) : new HexCollisionBox(5.0D, 6.0D, 0.0D, 11.0D, 10.0D, 2.0D);
            break;
         case WEST:
            var10000 = powered ? new HexCollisionBox(15.0D, 6.0D, 5.0D, 16.0D, 10.0D, 11.0D) : new HexCollisionBox(14.0D, 6.0D, 5.0D, 16.0D, 10.0D, 11.0D);
            break;
         case NORTH:
         case UP:
         case DOWN:
            var10000 = powered ? new HexCollisionBox(5.0D, 6.0D, 15.0D, 11.0D, 10.0D, 16.0D) : new HexCollisionBox(5.0D, 6.0D, 14.0D, 11.0D, 10.0D, 16.0D);
            break;
         case EAST:
            var10000 = powered ? new HexCollisionBox(0.0D, 6.0D, 5.0D, 1.0D, 10.0D, 11.0D) : new HexCollisionBox(0.0D, 6.0D, 5.0D, 2.0D, 10.0D, 11.0D);
            break;
         default:
            var10000 = NoCollisionBox.INSTANCE;
         }

         return (CollisionBox)var10000;
      case CEILING:
         if (player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
            return LEVER.dynamic.fetch(player, item, version, datax, isTargetBlock, x, y, z);
         } else {
            if (facing != BlockFace.EAST && facing != BlockFace.WEST) {
               return powered ? new HexCollisionBox(5.0D, 15.0D, 6.0D, 11.0D, 16.0D, 10.0D) : new HexCollisionBox(5.0D, 14.0D, 6.0D, 11.0D, 16.0D, 10.0D);
            }

            return powered ? new HexCollisionBox(6.0D, 15.0D, 5.0D, 10.0D, 16.0D, 11.0D) : new HexCollisionBox(6.0D, 14.0D, 5.0D, 10.0D, 16.0D, 11.0D);
         }
      case FLOOR:
         if (player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
            return LEVER.dynamic.fetch(player, item, version, datax, isTargetBlock, x, y, z);
         } else {
            if (facing != BlockFace.EAST && facing != BlockFace.WEST) {
               return powered ? new HexCollisionBox(5.0D, 0.0D, 6.0D, 11.0D, 1.0D, 10.0D) : new HexCollisionBox(5.0D, 0.0D, 6.0D, 11.0D, 2.0D, 10.0D);
            }

            return powered ? new HexCollisionBox(6.0D, 0.0D, 5.0D, 10.0D, 1.0D, 11.0D) : new HexCollisionBox(6.0D, 0.0D, 5.0D, 10.0D, 2.0D, 11.0D);
         }
      default:
         throw new IllegalStateException();
      }
   }, (StateType[])BlockTags.BUTTONS.getStates().toArray(new StateType[0])),
   WALL(new DynamicHitboxWall(), (StateType[])BlockTags.WALLS.getStates().toArray(new StateType[0])),
   WALL_SIGN((player, item, version, datax, isTargetBlock, x, y, z) -> {
      Object var10000;
      switch(datax.getFacing()) {
      case SOUTH:
         var10000 = new HexCollisionBox(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D);
         break;
      case WEST:
         var10000 = new HexCollisionBox(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D);
         break;
      case NORTH:
         var10000 = new HexCollisionBox(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D);
         break;
      case EAST:
         var10000 = new HexCollisionBox(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D);
         break;
      default:
         var10000 = NoCollisionBox.INSTANCE;
      }

      return (CollisionBox)var10000;
   }, (StateType[])BlockTags.WALL_SIGNS.getStates().toArray(new StateType[0])),
   CEILING_HANGING_SIGNS((player, item, version, datax, isTargetBlock, x, y, z) -> {
      HexCollisionBox var10000;
      switch(datax.getRotation()) {
      case 0:
      case 8:
         var10000 = new HexCollisionBox(1.0D, 0.0D, 7.0D, 15.0D, 10.0D, 9.0D);
         break;
      case 4:
      case 12:
         var10000 = new HexCollisionBox(7.0D, 0.0D, 1.0D, 9.0D, 10.0D, 15.0D);
         break;
      default:
         var10000 = new HexCollisionBox(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
      }

      return var10000;
   }, (StateType[])BlockTags.CEILING_HANGING_SIGNS.getStates().toArray(new StateType[0])),
   WALL_HANGING_SIGN((player, item, version, datax, isTargetBlock, x, y, z) -> {
      ComplexCollisionBox var10000;
      switch(datax.getFacing()) {
      case SOUTH:
      case NORTH:
         var10000 = new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 14.0D, 6.0D, 16.0D, 16.0D, 10.0D), new HexCollisionBox(1.0D, 0.0D, 7.0D, 15.0D, 10.0D, 9.0D)});
         break;
      default:
         var10000 = new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(6.0D, 14.0D, 0.0D, 10.0D, 16.0D, 16.0D), new HexCollisionBox(7.0D, 0.0D, 1.0D, 9.0D, 10.0D, 15.0D)});
      }

      return var10000;
   }, (StateType[])BlockTags.WALL_HANGING_SIGNS.getStates().toArray(new StateType[0])),
   STANDING_SIGN((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return new HexCollisionBox(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
   }, (StateType[])BlockTags.STANDING_SIGNS.getStates().toArray(new StateType[0])),
   SAPLING(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D), (StateType[])BlockTags.SAPLINGS.getStates().stream().filter((s) -> {
      return s != StateTypes.AZALEA && s != StateTypes.FLOWERING_AZALEA;
   }).toArray((x$0) -> {
      return new StateType[x$0];
   })),
   ROOTS(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D), new StateType[]{StateTypes.WARPED_ROOTS, StateTypes.CRIMSON_ROOTS}),
   BANNER(new HexCollisionBox(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D), new StateType[]{StateTypes.WHITE_BANNER, StateTypes.ORANGE_BANNER, StateTypes.MAGENTA_BANNER, StateTypes.LIGHT_BLUE_BANNER, StateTypes.YELLOW_BANNER, StateTypes.LIME_BANNER, StateTypes.PINK_BANNER, StateTypes.GRAY_BANNER, StateTypes.LIGHT_GRAY_BANNER, StateTypes.CYAN_BANNER, StateTypes.PURPLE_BANNER, StateTypes.BLUE_BANNER, StateTypes.BROWN_BANNER, StateTypes.GREEN_BANNER, StateTypes.RED_BANNER, StateTypes.BLACK_BANNER}),
   WALL_BANNER((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (version.isOlderThan(ClientVersion.V_1_8)) {
         return WALL_SIGN.dynamic.fetch(player, item, version, datax, isTargetBlock, x, y, z);
      } else {
         HexCollisionBox var10000;
         switch(datax.getFacing()) {
         case SOUTH:
            var10000 = new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 12.5D, 2.0D);
            break;
         case WEST:
            var10000 = new HexCollisionBox(14.0D, 0.0D, 0.0D, 16.0D, 12.5D, 16.0D);
            break;
         case NORTH:
            var10000 = new HexCollisionBox(0.0D, 0.0D, 14.0D, 16.0D, 12.5D, 16.0D);
            break;
         case EAST:
            var10000 = new HexCollisionBox(0.0D, 0.0D, 0.0D, 2.0D, 12.5D, 16.0D);
            break;
         default:
            throw new IllegalStateException("Impossible Banner Facing State; Something very wrong is going on");
         }

         return var10000;
      }
   }, new StateType[]{StateTypes.WHITE_WALL_BANNER, StateTypes.ORANGE_WALL_BANNER, StateTypes.MAGENTA_WALL_BANNER, StateTypes.LIGHT_BLUE_WALL_BANNER, StateTypes.YELLOW_WALL_BANNER, StateTypes.LIME_WALL_BANNER, StateTypes.PINK_WALL_BANNER, StateTypes.GRAY_WALL_BANNER, StateTypes.LIGHT_GRAY_WALL_BANNER, StateTypes.CYAN_WALL_BANNER, StateTypes.PURPLE_WALL_BANNER, StateTypes.BLUE_WALL_BANNER, StateTypes.BROWN_WALL_BANNER, StateTypes.GREEN_WALL_BANNER, StateTypes.RED_WALL_BANNER, StateTypes.BLACK_WALL_BANNER}),
   BREWING_STAND((player, item, version, block, isTargetBlock, x, y, z) -> {
      if (version.isOlderThan(ClientVersion.V_1_13)) {
         return (CollisionBox)(isTargetBlock && block.getType() == StateTypes.BREWING_STAND && player.getClientVersion().equals(ClientVersion.V_1_8) ? new ComplexCollisionBox(2, new SimpleCollisionBox[]{new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true)}) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D));
      } else {
         return new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D), new SimpleCollisionBox(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.875D, 0.5625D, false)});
      }
   }, new StateType[]{StateTypes.BREWING_STAND}),
   SMALL_FLOWER((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(player.getClientVersion().isOlderThan(ClientVersion.V_1_13) ? new SimpleCollisionBox(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.625D, 0.6875D) : new OffsetCollisionBox(datax.getType(), 0.3125D, 0.0D, 0.3125D, 0.6875D, 0.625D, 0.6875D));
   }, (StateType[])BlockTags.SMALL_FLOWERS.getStates().toArray(new StateType[0])),
   TALL_FLOWERS(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true), (StateType[])BlockTags.TALL_FLOWERS.getStates().toArray(new StateType[0])),
   FIRE((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_16)) {
         ComplexCollisionBox boxes = new ComplexCollisionBox(5);
         if (datax.getWest() == West.TRUE) {
            boxes.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D));
         }

         if (datax.getEast() == East.TRUE) {
            boxes.add(new HexCollisionBox(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D));
         }

         if (datax.getNorth() == North.TRUE) {
            boxes.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D));
         }

         if (datax.getSouth() == South.TRUE) {
            boxes.add(new HexCollisionBox(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D));
         }

         if (datax.hasProperty(StateValue.UP) && datax.isUp()) {
            boxes.add(new HexCollisionBox(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D));
         }

         return (CollisionBox)(boxes.isNull() ? new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D) : boxes);
      } else {
         return NoCollisionBox.INSTANCE;
      }
   }, (StateType[])BlockTags.FIRE.getStates().toArray(new StateType[0])),
   HONEY_BLOCK(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true), new StateType[]{StateTypes.HONEY_BLOCK}),
   POWDER_SNOW(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true), new StateType[]{StateTypes.POWDER_SNOW}),
   SOUL_SAND(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true), new StateType[]{StateTypes.SOUL_SAND}),
   CACTUS((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isOlderThan(ClientVersion.V_1_13) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D));
   }, new StateType[]{StateTypes.CACTUS}),
   SNOW((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, (double)datax.getLayers() * 0.125D, 1.0D);
   }, new StateType[]{StateTypes.SNOW}),
   LECTERN_BLOCK((player, item, version, datax, isTargetBlock, x, y, z) -> {
      ComplexCollisionBox common = new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), new HexCollisionBox(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D)});
      if (datax.getFacing() == BlockFace.WEST) {
         common.add(new HexCollisionBox(1.0D, 10.0D, 0.0D, 5.333333D, 14.0D, 16.0D));
         common.add(new HexCollisionBox(5.333333D, 12.0D, 0.0D, 9.666667D, 16.0D, 16.0D));
         common.add(new HexCollisionBox(9.666667D, 14.0D, 0.0D, 14.0D, 18.0D, 16.0D));
      } else if (datax.getFacing() == BlockFace.NORTH) {
         common.add(new HexCollisionBox(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D));
         common.add(new HexCollisionBox(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D));
         common.add(new HexCollisionBox(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D));
      } else if (datax.getFacing() == BlockFace.EAST) {
         common.add(new HexCollisionBox(10.666667D, 10.0D, 0.0D, 15.0D, 14.0D, 16.0D));
         common.add(new HexCollisionBox(6.333333D, 12.0D, 0.0D, 10.666667D, 16.0D, 16.0D));
         common.add(new HexCollisionBox(2.0D, 14.0D, 0.0D, 6.333333D, 18.0D, 16.0D));
      } else {
         common.add(new HexCollisionBox(0.0D, 10.0D, 10.666667D, 16.0D, 14.0D, 15.0D));
         common.add(new HexCollisionBox(0.0D, 12.0D, 6.333333D, 16.0D, 16.0D, 10.666667D));
         common.add(new HexCollisionBox(0.0D, 14.0D, 2.0D, 16.0D, 18.0D, 6.333333D));
      }

      return common;
   }, new StateType[]{StateTypes.LECTERN}),
   GLOW_LICHEN_SCULK_VEIN((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (version.isNewerThan(ClientVersion.V_1_16_4)) {
         ComplexCollisionBox box = new ComplexCollisionBox(6);
         if (datax.isUp()) {
            box.add(new HexCollisionBox(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D));
         }

         if (datax.isDown()) {
            box.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D));
         }

         if (datax.getWest() == West.TRUE) {
            box.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D));
         }

         if (datax.getEast() == East.TRUE) {
            box.add(new HexCollisionBox(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D));
         }

         if (datax.getNorth() == North.TRUE) {
            box.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D));
         }

         if (datax.getSouth() == South.TRUE) {
            box.add(new HexCollisionBox(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D));
         }

         return box;
      } else {
         return NoCollisionBox.INSTANCE;
      }
   }, new StateType[]{StateTypes.GLOW_LICHEN, StateTypes.SCULK_VEIN, StateTypes.RESIN_CLUMP}),
   SPORE_BLOSSOM((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_16_4) ? new HexCollisionBox(2.0D, 13.0D, 2.0D, 14.0D, 16.0D, 14.0D) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true));
   }, new StateType[]{StateTypes.SPORE_BLOSSOM}),
   PITCHER_CROP((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (version.isNewerThan(ClientVersion.V_1_19_4)) {
         SimpleCollisionBox FULL_UPPER_SHAPE = new HexCollisionBox(3.0D, 0.0D, 3.0D, 13.0D, 15.0D, 13.0D);
         SimpleCollisionBox FULL_LOWER_SHAPE = new HexCollisionBox(3.0D, -1.0D, 3.0D, 13.0D, 16.0D, 13.0D);
         SimpleCollisionBox COLLISION_SHAPE_BULB = new HexCollisionBox(5.0D, -1.0D, 5.0D, 11.0D, 3.0D, 11.0D);
         new HexCollisionBox(3.0D, -1.0D, 3.0D, 13.0D, 5.0D, 13.0D);
         SimpleCollisionBox[] UPPER_SHAPE_BY_AGE = new SimpleCollisionBox[]{new HexCollisionBox(3.0D, 0.0D, 3.0D, 13.0D, 11.0D, 13.0D), FULL_UPPER_SHAPE};
         SimpleCollisionBox[] LOWER_SHAPE_BY_AGE = new SimpleCollisionBox[]{COLLISION_SHAPE_BULB, new HexCollisionBox(3.0D, -1.0D, 3.0D, 13.0D, 14.0D, 13.0D), FULL_LOWER_SHAPE, FULL_LOWER_SHAPE, FULL_LOWER_SHAPE};
         return datax.getHalf() == Half.UPPER ? UPPER_SHAPE_BY_AGE[Math.min(Math.abs(4 - (datax.getAge() + 1)), UPPER_SHAPE_BY_AGE.length - 1)] : LOWER_SHAPE_BY_AGE[datax.getAge()];
      } else {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      }
   }, new StateType[]{StateTypes.PITCHER_CROP}),
   WHEAT_BEETROOTS((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, (double)((datax.getAge() + 1) * 2), 16.0D);
   }, new StateType[]{StateTypes.WHEAT, StateTypes.BEETROOTS}),
   CARROT_POTATOES((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, (double)(datax.getAge() + 2), 16.0D);
   }, new StateType[]{StateTypes.CARROTS, StateTypes.POTATOES}),
   NETHER_WART((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, (double)(5 + datax.getAge() * 3), 16.0D);
   }, new StateType[]{StateTypes.NETHER_WART}),
   ATTACHED_PUMPKIN_STEM((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (version.isOlderThan(ClientVersion.V_1_13)) {
         return new HexCollisionBox(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
      } else {
         HexCollisionBox var10000;
         switch(datax.getFacing()) {
         case SOUTH:
            var10000 = new HexCollisionBox(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 16.0D);
            break;
         case WEST:
            var10000 = new HexCollisionBox(0.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
            break;
         case NORTH:
            var10000 = new HexCollisionBox(6.0D, 0.0D, 0.0D, 10.0D, 10.0D, 10.0D);
            break;
         default:
            var10000 = new HexCollisionBox(6.0D, 0.0D, 6.0D, 16.0D, 10.0D, 10.0D);
         }

         return var10000;
      }
   }, new StateType[]{StateTypes.ATTACHED_MELON_STEM, StateTypes.ATTACHED_PUMPKIN_STEM}),
   PUMPKIN_STEM((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return new HexCollisionBox(7.0D, 0.0D, 7.0D, 9.0D, (double)(2 * (datax.getAge() + 1)), 9.0D);
   }, new StateType[]{StateTypes.PUMPKIN_STEM, StateTypes.MELON_STEM}),
   COCOA_BEANS((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return CollisionData.getCocoa(version, datax.getAge(), datax.getFacing());
   }, new StateType[]{StateTypes.COCOA}),
   REDSTONE_WIRE(NoCollisionBox.INSTANCE, new StateType[]{StateTypes.REDSTONE_WIRE}),
   SWEET_BERRY((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (datax.getAge() == 0) {
         return new HexCollisionBox(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
      } else {
         return (CollisionBox)(datax.getAge() < 3 ? new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true));
      }
   }, new StateType[]{StateTypes.SWEET_BERRY_BUSH}),
   CORAL_PLANTS(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D), ArrayUtils.combine(BlockTags.CORAL_PLANTS.getStates(), StateTypes.DEAD_TUBE_CORAL, StateTypes.DEAD_BRAIN_CORAL, StateTypes.DEAD_BUBBLE_CORAL, StateTypes.DEAD_FIRE_CORAL, StateTypes.DEAD_HORN_CORAL)),
   CORAL_FAN(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D), new StateType[]{StateTypes.TUBE_CORAL_FAN, StateTypes.BRAIN_CORAL_FAN, StateTypes.BUBBLE_CORAL_FAN, StateTypes.FIRE_CORAL_FAN, StateTypes.HORN_CORAL_FAN, StateTypes.DEAD_TUBE_CORAL_FAN, StateTypes.DEAD_BRAIN_CORAL_FAN, StateTypes.DEAD_BUBBLE_CORAL_FAN, StateTypes.DEAD_FIRE_CORAL_FAN, StateTypes.DEAD_HORN_CORAL_FAN}),
   CORAL_WALL_FAN((player, item, version, datax, isTargetBlock, x, y, z) -> {
      HexCollisionBox var10000;
      switch(datax.getFacing()) {
      case SOUTH:
         var10000 = new HexCollisionBox(0.0D, 4.0D, 0.0D, 16.0D, 12.0D, 11.0D);
         break;
      case WEST:
         var10000 = new HexCollisionBox(5.0D, 4.0D, 0.0D, 16.0D, 12.0D, 16.0D);
         break;
      case NORTH:
         var10000 = new HexCollisionBox(0.0D, 4.0D, 5.0D, 16.0D, 12.0D, 16.0D);
         break;
      case EAST:
         var10000 = new HexCollisionBox(0.0D, 4.0D, 0.0D, 11.0D, 12.0D, 16.0D);
         break;
      default:
         throw new UnsupportedOperationException();
      }

      return var10000;
   }, ArrayUtils.combine(BlockTags.WALL_CORALS.getStates(), StateTypes.DEAD_TUBE_CORAL_WALL_FAN, StateTypes.DEAD_BRAIN_CORAL_WALL_FAN, StateTypes.DEAD_BUBBLE_CORAL_WALL_FAN, StateTypes.DEAD_FIRE_CORAL_WALL_FAN, StateTypes.DEAD_HORN_CORAL_WALL_FAN)),
   TORCHFLOWER_CROP((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return datax.getAge() == 0 ? new HexCollisionBox(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D) : new HexCollisionBox(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
   }, new StateType[]{StateTypes.TORCHFLOWER_CROP}),
   DEAD_BUSH(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D), new StateType[]{StateTypes.DEAD_BUSH}),
   SUGARCANE(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D), new StateType[]{StateTypes.SUGAR_CANE}),
   NETHER_SPROUTS(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), new StateType[]{StateTypes.NETHER_SPROUTS}),
   HANGING_ROOTS(new HexCollisionBox(2.0D, 10.0D, 2.0D, 14.0D, 16.0D, 14.0D), new StateType[]{StateTypes.HANGING_ROOTS}),
   HANGING_MOSS((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isOlderThan(ClientVersion.V_1_21_2) ? HANGING_ROOTS.fetch(player, item, version, datax, isTargetBlock, x, y, z) : new HexCollisionBox(1.0D, datax.isTip() ? 2.0D : 0.0D, 1.0D, 15.0D, 16.0D, 15.0D));
   }, new StateType[]{StateTypes.PALE_HANGING_MOSS}),
   GRASS_FERN((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isOlderThan(ClientVersion.V_1_13) ? new SimpleCollisionBox(0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D) : new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D));
   }, new StateType[]{StateTypes.SHORT_GRASS, StateTypes.FERN}),
   SEA_GRASS(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D), new StateType[]{StateTypes.SEAGRASS}),
   TALL_SEAGRASS(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D), new StateType[]{StateTypes.TALL_SEAGRASS}),
   SMALL_DRIPLEAF(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D), new StateType[]{StateTypes.SMALL_DRIPLEAF}),
   CAVE_VINES(new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D), new StateType[]{StateTypes.CAVE_VINES, StateTypes.CAVE_VINES_PLANT}),
   MUSHROOM(new HexCollisionBox(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D), new StateType[]{StateTypes.BROWN_MUSHROOM, StateTypes.RED_MUSHROOM}),
   FUNGUS(new HexCollisionBox(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D), new StateType[]{StateTypes.CRIMSON_FUNGUS, StateTypes.WARPED_FUNGUS}),
   TWISTING_VINES_BLOCK((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return getVineCollisionBox(version, false, true);
   }, new StateType[]{StateTypes.TWISTING_VINES}),
   WEEPING_VINES_BLOCK((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return getVineCollisionBox(version, true, true);
   }, new StateType[]{StateTypes.WEEPING_VINES}),
   TWISTING_VINES((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return getVineCollisionBox(version, false, false);
   }, new StateType[]{StateTypes.TWISTING_VINES_PLANT}),
   WEEPING_VINES((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return getVineCollisionBox(version, true, false);
   }, new StateType[]{StateTypes.WEEPING_VINES_PLANT}),
   TALL_PLANT(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true), new StateType[]{StateTypes.TALL_GRASS, StateTypes.LARGE_FERN}),
   BAMBOO((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return datax.getLeaves() == Leaves.LARGE ? new HexOffsetCollisionBox(datax.getType(), 3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D) : new HexOffsetCollisionBox(datax.getType(), 5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
   }, new StateType[]{StateTypes.BAMBOO}),
   BAMBOO_SAPLING((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return new HexOffsetCollisionBox(datax.getType(), 4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D);
   }, new StateType[]{StateTypes.BAMBOO_SAPLING}),
   SCAFFOLDING((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (item != StateTypes.SCAFFOLDING && !version.isOlderThan(ClientVersion.V_1_14)) {
         ComplexCollisionBox box = new ComplexCollisionBox(9, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D), new HexCollisionBox(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D), new HexCollisionBox(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D), new HexCollisionBox(0.0D, 0.0D, 14.0D, 2.0D, 16.0D, 16.0D), new HexCollisionBox(14.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D)});
         if (datax.isBottom()) {
            box.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 2.0D, 2.0D, 16.0D));
            box.add(new HexCollisionBox(14.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D));
            box.add(new HexCollisionBox(0.0D, 0.0D, 14.0D, 16.0D, 2.0D, 16.0D));
            box.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 2.0D));
         }

         return box;
      } else {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      }
   }, new StateType[]{StateTypes.SCAFFOLDING}),
   DRIPLEAF((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (version.isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else {
         ComplexCollisionBox box = new ComplexCollisionBox(2);
         if (datax.getFacing() == BlockFace.NORTH) {
            box.add(new HexCollisionBox(5.0D, 0.0D, 9.0D, 11.0D, 15.0D, 15.0D));
         } else if (datax.getFacing() == BlockFace.SOUTH) {
            box.add(new HexCollisionBox(5.0D, 0.0D, 1.0D, 11.0D, 15.0D, 7.0D));
         } else if (datax.getFacing() == BlockFace.EAST) {
            box.add(new HexCollisionBox(1.0D, 0.0D, 5.0D, 7.0D, 15.0D, 11.0D));
         } else {
            box.add(new HexCollisionBox(9.0D, 0.0D, 5.0D, 15.0D, 15.0D, 11.0D));
         }

         if (datax.getTilt() != Tilt.NONE && datax.getTilt() != Tilt.UNSTABLE) {
            if (datax.getTilt() == Tilt.PARTIAL) {
               box.add(new HexCollisionBox(0.0D, 11.0D, 0.0D, 16.0D, 13.0D, 16.0D));
            }
         } else {
            box.add(new HexCollisionBox(0.0D, 11.0D, 0.0D, 16.0D, 15.0D, 16.0D));
         }

         return box;
      }
   }, new StateType[]{StateTypes.BIG_DRIPLEAF}),
   PINK_PETALS_BLOCK((player, item, version, datax, isTargetBlock, x, y, z) -> {
      if (version.isNewerThan(ClientVersion.V_1_20_2)) {
         return getSegmentedHitBox(datax.getFlowerAmount(), datax.getFacing(), 3);
      } else if (version.isNewerThan(ClientVersion.V_1_19_3)) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
      } else {
         return version.isNewerThan(ClientVersion.V_1_12_2) ? CORAL_FAN.box.copy() : GRASS_FERN.dynamic.fetch(player, item, version, datax, isTargetBlock, x, y, z);
      }
   }, new StateType[]{StateTypes.PINK_PETALS}),
   MANGROVE_PROPAGULE((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return datax.isHanging() ? new HexOffsetCollisionBox(datax.getType(), 7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D) : new HexOffsetCollisionBox(datax.getType(), 7.0D, (double)getPropaguleMinHeight(datax.getAge()), 7.0D, 9.0D, 16.0D, 9.0D);
   }, new StateType[]{StateTypes.MANGROVE_PROPAGULE}),
   FROGSPAWN((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_18_2) ? new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 1.5D, 16.0D) : NoCollisionBox.INSTANCE);
   }, new StateType[]{StateTypes.FROGSPAWN}),
   BUSH((player, heldItem, version, block, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_21_4) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D) : GRASS_FERN.dynamic.fetch(player, heldItem, version, block, isTargetBlock, x, y, z));
   }, new StateType[]{StateTypes.BUSH}),
   SHORT_DRY_GRASS((player, heldItem, version, block, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_21_4) ? new SimpleCollisionBox(0.125D, 0.0D, 0.125D, 0.875D, 0.625D, 0.875D) : GRASS_FERN.dynamic.fetch(player, heldItem, version, block, isTargetBlock, x, y, z));
   }, new StateType[]{StateTypes.SHORT_DRY_GRASS}),
   TALL_DRY_GRASS((player, heldItem, version, block, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_21_4) ? new SimpleCollisionBox(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D) : GRASS_FERN.dynamic.fetch(player, heldItem, version, block, isTargetBlock, x, y, z));
   }, new StateType[]{StateTypes.TALL_DRY_GRASS}),
   LEAF_LITTER((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_21_4) ? getSegmentedHitBox(datax.getSegmentAmount(), datax.getFacing(), 1) : new HexCollisionBox(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D));
   }, new StateType[]{StateTypes.LEAF_LITTER}),
   WILDFLOWERS((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_21_4) ? getSegmentedHitBox(datax.getFlowerAmount(), datax.getFacing(), 3) : new HexCollisionBox(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D));
   }, new StateType[]{StateTypes.WILDFLOWERS}),
   CACTUS_FLOWER((player, item, version, datax, isTargetBlock, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_21_4) ? new SimpleCollisionBox(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.75D, 0.9375D) : CORAL_FAN.box.copy());
   }, new StateType[]{StateTypes.CACTUS_FLOWER}),
   SCULK_SHRIEKER(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true), new StateType[]{StateTypes.SCULK_SHRIEKER});

   private static final Map<StateType, HitboxData> lookup = new HashMap();
   private final StateType[] materials;
   private CollisionBox box;
   private HitBoxFactory dynamic;

   private HitboxData(CollisionBox param3, StateType... param4) {
      this.box = box;
      Set<StateType> mList = new HashSet(Arrays.asList(materials));
      mList.remove((Object)null);
      this.materials = (StateType[])mList.toArray(new StateType[0]);
   }

   private HitboxData(HitBoxFactory param3, StateType... param4) {
      this.dynamic = dynamic;
      Set<StateType> mList = new HashSet(Arrays.asList(materials));
      mList.remove((Object)null);
      this.materials = (StateType[])mList.toArray(new StateType[0]);
   }

   public CollisionBox fetch(GrimPlayer player, StateType heldItem, ClientVersion version, WrappedBlockState block, boolean isTargetBlock, int x, int y, int z) {
      return this.box != null ? this.box.copy() : this.dynamic.fetch(player, heldItem, version, block, isTargetBlock, x, y, z);
   }

   public static HitboxData getData(StateType material) {
      return (HitboxData)lookup.get(material);
   }

   public static CollisionBox getBlockHitbox(GrimPlayer player, StateType heldItem, ClientVersion version, WrappedBlockState block, boolean isTargetBlock, int x, int y, int z) {
      HitboxData data = getData(block.getType());
      return data == null ? CollisionData.getRawData(block.getType()).getMovementCollisionBox(player, version, block, x, y, z) : data.fetch(player, heldItem, version, block, isTargetBlock, x, y, z).offset((double)x, (double)y, (double)z);
   }

   private static int getPropaguleMinHeight(int age) {
      int var10000;
      switch(age) {
      case 0:
      case 1:
      case 2:
         var10000 = 13 - age * 3;
         break;
      case 3:
      case 4:
         var10000 = (4 - age) * 3;
         break;
      default:
         throw new IllegalStateException("Impossible Propagule Height");
      }

      return var10000;
   }

   private static CollisionBox getVineCollisionBox(ClientVersion version, boolean isWeeping, boolean isBlock) {
      if (version.isNewerThan(ClientVersion.V_1_15_2)) {
         return isWeeping ? (isBlock ? new SimpleCollisionBox(0.25D, 0.5625D, 0.25D, 0.75D, 1.0D, 0.75D) : new SimpleCollisionBox(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D)) : new SimpleCollisionBox(0.25D, 0.0D, 0.25D, 0.75D, isBlock ? 0.9375D : 1.0D, 0.75D);
      } else {
         return new ComplexCollisionBox(4, new SimpleCollisionBox[]{new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D), new SimpleCollisionBox(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D), new SimpleCollisionBox(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D)});
      }
   }

   private static CollisionBox getSegmentedHitBox(int segments, BlockFace facing, int height) {
      Object var10000;
      switch(segments) {
      case 0:
         var10000 = NoCollisionBox.INSTANCE;
         break;
      case 1:
         switch(facing) {
         case SOUTH:
            var10000 = new SimpleCollisionBox(0.5D, 0.0D, 0.5D, 1.0D, (double)height / 16.0D, 1.0D, false);
            return (CollisionBox)var10000;
         case WEST:
            var10000 = new SimpleCollisionBox(0.5D, 0.0D, 0.0D, 1.0D, (double)height / 16.0D, 0.5D, false);
            return (CollisionBox)var10000;
         case NORTH:
            var10000 = new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 0.5D, (double)height / 16.0D, 0.5D, false);
            return (CollisionBox)var10000;
         case EAST:
            var10000 = new SimpleCollisionBox(0.0D, 0.0D, 0.5D, 0.5D, (double)height / 16.0D, 1.0D, false);
            return (CollisionBox)var10000;
         default:
            throw new IllegalStateException("Unexpected value: " + String.valueOf(facing));
         }
      case 2:
         switch(facing) {
         case SOUTH:
            var10000 = new SimpleCollisionBox(0.5D, 0.0D, 0.0D, 1.0D, (double)height / 16.0D, 1.0D, false);
            return (CollisionBox)var10000;
         case WEST:
            var10000 = new SimpleCollisionBox(0.0D, 0.0D, 0.5D, 1.0D, (double)height / 16.0D, 1.0D, false);
            return (CollisionBox)var10000;
         case NORTH:
            var10000 = new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 0.5D, (double)height / 16.0D, 1.0D, false);
            return (CollisionBox)var10000;
         case EAST:
            var10000 = new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, (double)height / 16.0D, 0.5D, false);
            return (CollisionBox)var10000;
         default:
            throw new IllegalStateException("Unexpected value: " + String.valueOf(facing));
         }
      case 3:
      case 4:
         var10000 = new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, (double)height / 16.0D, 1.0D, false);
         break;
      default:
         throw new IllegalStateException("Unexpected value: " + segments);
      }

      return (CollisionBox)var10000;
   }

   // $FF: synthetic method
   private static HitboxData[] $values() {
      return new HitboxData[]{VINE, RAILS, END_PORTAL, FENCE_GATE, FENCE, PANE, LEVER, BUTTON, WALL, WALL_SIGN, CEILING_HANGING_SIGNS, WALL_HANGING_SIGN, STANDING_SIGN, SAPLING, ROOTS, BANNER, WALL_BANNER, BREWING_STAND, SMALL_FLOWER, TALL_FLOWERS, FIRE, HONEY_BLOCK, POWDER_SNOW, SOUL_SAND, CACTUS, SNOW, LECTERN_BLOCK, GLOW_LICHEN_SCULK_VEIN, SPORE_BLOSSOM, PITCHER_CROP, WHEAT_BEETROOTS, CARROT_POTATOES, NETHER_WART, ATTACHED_PUMPKIN_STEM, PUMPKIN_STEM, COCOA_BEANS, REDSTONE_WIRE, SWEET_BERRY, CORAL_PLANTS, CORAL_FAN, CORAL_WALL_FAN, TORCHFLOWER_CROP, DEAD_BUSH, SUGARCANE, NETHER_SPROUTS, HANGING_ROOTS, HANGING_MOSS, GRASS_FERN, SEA_GRASS, TALL_SEAGRASS, SMALL_DRIPLEAF, CAVE_VINES, MUSHROOM, FUNGUS, TWISTING_VINES_BLOCK, WEEPING_VINES_BLOCK, TWISTING_VINES, WEEPING_VINES, TALL_PLANT, BAMBOO, BAMBOO_SAPLING, SCAFFOLDING, DRIPLEAF, PINK_PETALS_BLOCK, MANGROVE_PROPAGULE, FROGSPAWN, BUSH, SHORT_DRY_GRASS, TALL_DRY_GRASS, LEAF_LITTER, WILDFLOWERS, CACTUS_FLOWER, SCULK_SHRIEKER};
   }

   static {
      HitboxData[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         HitboxData data = var0[var2];
         StateType[] var4 = data.materials;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            StateType type = var4[var6];
            lookup.put(type, data);
         }
      }

   }
}
