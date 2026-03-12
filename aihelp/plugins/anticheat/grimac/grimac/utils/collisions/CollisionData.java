package ac.grim.grimac.utils.collisions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerStrider;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Attachment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Face;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Part;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Thickness;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Tilt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Type;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.VerticalDirection;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.collisions.blocks.DoorHandler;
import ac.grim.grimac.utils.collisions.blocks.DynamicChest;
import ac.grim.grimac.utils.collisions.blocks.DynamicChorusPlant;
import ac.grim.grimac.utils.collisions.blocks.DynamicStair;
import ac.grim.grimac.utils.collisions.blocks.PistonBaseCollision;
import ac.grim.grimac.utils.collisions.blocks.PistonHeadCollision;
import ac.grim.grimac.utils.collisions.blocks.TrapDoorHandler;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicCollisionFence;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicCollisionPane;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicCollisionWall;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.DynamicCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexOffsetCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.NoCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntityStrider;
import ac.grim.grimac.utils.nmsutil.Materials;
import ac.grim.grimac.utils.viaversion.ViaVersionUtil;
import com.viaversion.viaversion.api.Via;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public enum CollisionData implements CollisionFactory {
   LAVA((player, version, block, x, y, z) -> {
      return (CollisionBox)(MovementTickerStrider.isAbove(player) && player.compensatedEntities.self.getRiding() instanceof PacketEntityStrider && block.getLevel() == 0 ? new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D) : NoCollisionBox.INSTANCE);
   }, new StateType[]{StateTypes.LAVA}),
   BREWING_STAND((player, version, block, x, y, z) -> {
      int base = 0;
      int maxIndex = 3;
      if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         maxIndex = 2;
         base = 1;
      }

      return new ComplexCollisionBox(maxIndex, new SimpleCollisionBox[]{new HexCollisionBox((double)base, 0.0D, (double)base, (double)(16 - base), 2.0D, (double)(16 - base)), new SimpleCollisionBox(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.875D, 0.5625D, false)});
   }, new StateType[]{StateTypes.BREWING_STAND}),
   BAMBOO((player, version, block, x, y, z) -> {
      return (CollisionBox)(version.isOlderThan(ClientVersion.V_1_14) ? NoCollisionBox.INSTANCE : new HexOffsetCollisionBox(block.getType(), 6.5D, 0.0D, 6.5D, 9.5D, 16.0D, 9.5D));
   }, new StateType[]{StateTypes.BAMBOO}),
   COMPOSTER((player, version, block, x, y, z) -> {
      double height = 0.125D;
      if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
         height = 0.25D;
      }

      if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         height = 0.3125D;
      }

      return new ComplexCollisionBox(5, new SimpleCollisionBox[]{new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, height, 1.0D, false), new SimpleCollisionBox(0.0D, height, 0.0D, 0.125D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.875D, height, 0.0D, 1.0D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.0D, height, 0.0D, 1.0D, 1.0D, 0.125D, false), new SimpleCollisionBox(0.0D, height, 0.875D, 1.0D, 1.0D, 1.0D, false)});
   }, new StateType[]{StateTypes.COMPOSTER}),
   RAIL(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 0.0D, false), new StateType[]{StateTypes.RAIL, StateTypes.ACTIVATOR_RAIL, StateTypes.DETECTOR_RAIL, StateTypes.POWERED_RAIL}),
   ANVIL((player, version, datax, x, y, z) -> {
      BlockFace face = datax.getFacing();
      if (!version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         return face != BlockFace.NORTH && face != BlockFace.SOUTH ? new SimpleCollisionBox(0.0D, 0.0D, 0.125D, 1.0D, 1.0D, 0.875D, false) : new SimpleCollisionBox(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D, false);
      } else {
         ComplexCollisionBox complexAnvil = new ComplexCollisionBox(4);
         complexAnvil.add(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D));
         if (face != BlockFace.NORTH && face != BlockFace.SOUTH) {
            complexAnvil.add(new HexCollisionBox(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D));
            complexAnvil.add(new HexCollisionBox(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D));
            complexAnvil.add(new HexCollisionBox(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D));
         } else {
            complexAnvil.add(new HexCollisionBox(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D));
            complexAnvil.add(new HexCollisionBox(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D));
            complexAnvil.add(new HexCollisionBox(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D));
         }

         return complexAnvil;
      }
   }, (StateType[])BlockTags.ANVIL.getStates().toArray(new StateType[0])),
   WALL(new DynamicCollisionWall(), (StateType[])BlockTags.WALLS.getStates().toArray(new StateType[0])),
   SLAB((player, version, datax, x, y, z) -> {
      Type slabType = datax.getTypeData();
      if (slabType == Type.DOUBLE) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else {
         return slabType == Type.BOTTOM ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D, false) : new SimpleCollisionBox(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D, false);
      }
   }, (StateType[])BlockTags.SLABS.getStates().toArray(new StateType[0])),
   SKULL(new SimpleCollisionBox(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D, false), new StateType[]{StateTypes.CREEPER_HEAD, StateTypes.ZOMBIE_HEAD, StateTypes.DRAGON_HEAD, StateTypes.PLAYER_HEAD, StateTypes.SKELETON_SKULL, StateTypes.WITHER_SKELETON_SKULL, StateTypes.HEAVY_CORE}),
   PIGLIN_HEAD(new HexCollisionBox(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D), new StateType[]{StateTypes.PIGLIN_HEAD}),
   WALL_SKULL((player, version, datax, x, y, z) -> {
      SimpleCollisionBox var10000;
      switch(datax.getFacing()) {
      case SOUTH:
         var10000 = new SimpleCollisionBox(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.5D, false);
         break;
      case EAST:
         var10000 = new SimpleCollisionBox(0.0D, 0.25D, 0.25D, 0.5D, 0.75D, 0.75D, false);
         break;
      case WEST:
         var10000 = new SimpleCollisionBox(0.5D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D, false);
         break;
      default:
         var10000 = new SimpleCollisionBox(0.25D, 0.25D, 0.5D, 0.75D, 0.75D, 1.0D, false);
      }

      return var10000;
   }, new StateType[]{StateTypes.CREEPER_WALL_HEAD, StateTypes.DRAGON_WALL_HEAD, StateTypes.PLAYER_WALL_HEAD, StateTypes.ZOMBIE_WALL_HEAD, StateTypes.SKELETON_WALL_SKULL, StateTypes.WITHER_SKELETON_WALL_SKULL}),
   PIGLIN_WALL_HEAD((player, version, datax, x, y, z) -> {
      HexCollisionBox var10000;
      switch(datax.getFacing()) {
      case SOUTH:
         var10000 = new HexCollisionBox(3.0D, 4.0D, 0.0D, 13.0D, 12.0D, 8.0D);
         break;
      case EAST:
         var10000 = new HexCollisionBox(0.0D, 4.0D, 3.0D, 8.0D, 12.0D, 13.0D);
         break;
      case WEST:
         var10000 = new HexCollisionBox(8.0D, 4.0D, 3.0D, 16.0D, 12.0D, 13.0D);
         break;
      default:
         var10000 = new HexCollisionBox(3.0D, 4.0D, 8.0D, 13.0D, 12.0D, 16.0D);
      }

      return var10000;
   }, new StateType[]{StateTypes.PIGLIN_WALL_HEAD}),
   DOOR(new DoorHandler(), (StateType[])BlockTags.DOORS.getStates().toArray(new StateType[0])),
   HOPPER((player, version, datax, x, y, z) -> {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         ComplexCollisionBox hopperBox = new ComplexCollisionBox(7);
         switch(datax.getFacing()) {
         case DOWN:
            hopperBox.add(new HexCollisionBox(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D));
            break;
         case NORTH:
            hopperBox.add(new HexCollisionBox(6.0D, 4.0D, 0.0D, 10.0D, 8.0D, 4.0D));
            break;
         case SOUTH:
            hopperBox.add(new HexCollisionBox(6.0D, 4.0D, 12.0D, 10.0D, 8.0D, 16.0D));
            break;
         case EAST:
            hopperBox.add(new HexCollisionBox(12.0D, 4.0D, 6.0D, 16.0D, 8.0D, 10.0D));
            break;
         case WEST:
            hopperBox.add(new HexCollisionBox(0.0D, 4.0D, 6.0D, 4.0D, 8.0D, 10.0D));
         }

         hopperBox.add(new SimpleCollisionBox(0.0D, 0.625D, 0.0D, 1.0D, 0.6875D, 1.0D, false));
         hopperBox.add(new SimpleCollisionBox(0.0D, 0.6875D, 0.0D, 0.125D, 1.0D, 1.0D, false));
         hopperBox.add(new SimpleCollisionBox(0.125D, 0.6875D, 0.0D, 1.0D, 1.0D, 0.125D, false));
         hopperBox.add(new SimpleCollisionBox(0.125D, 0.6875D, 0.875D, 1.0D, 1.0D, 1.0D, false));
         hopperBox.add(new SimpleCollisionBox(0.25D, 0.25D, 0.25D, 0.75D, 0.625D, 0.75D, false));
         hopperBox.add(new SimpleCollisionBox(0.875D, 0.6875D, 0.125D, 1.0D, 1.0D, 0.875D, false));
         return hopperBox;
      } else {
         double height = 0.625D;
         return new ComplexCollisionBox(5, new SimpleCollisionBox[]{new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, height, 1.0D, false), new SimpleCollisionBox(0.0D, height, 0.0D, 0.125D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.875D, height, 0.0D, 1.0D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.0D, height, 0.0D, 1.0D, 1.0D, 0.125D, false), new SimpleCollisionBox(0.0D, height, 0.875D, 1.0D, 1.0D, 1.0D, false)});
      }
   }, new StateType[]{StateTypes.HOPPER}),
   CAKE((player, version, datax, x, y, z) -> {
      double height = 0.5D;
      if (version.isOlderThan(ClientVersion.V_1_8)) {
         height = 0.4375D;
      }

      double eatenPosition = (double)(1 + datax.getBites() * 2) / 16.0D;
      return new SimpleCollisionBox(eatenPosition, 0.0D, 0.0625D, 0.9375D, height, 0.9375D, false);
   }, new StateType[]{StateTypes.CAKE}),
   COCOA_BEANS((player, version, datax, x, y, z) -> {
      return getCocoa(version, datax.getAge(), datax.getFacing());
   }, new StateType[]{StateTypes.COCOA}),
   STONE_CUTTER((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isOlderThanOrEquals(ClientVersion.V_1_13_2) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D));
   }, new StateType[]{StateTypes.STONECUTTER}),
   CORAL_FAN(NoCollisionBox.INSTANCE, (StateType[])BlockTags.CORALS.getStates().toArray(new StateType[0])),
   RAILS(NoCollisionBox.INSTANCE, (StateType[])BlockTags.RAILS.getStates().toArray(new StateType[0])),
   BANNER(NoCollisionBox.INSTANCE, (StateType[])BlockTags.BANNERS.getStates().toArray(new StateType[0])),
   SMALL_FLOWER(NoCollisionBox.INSTANCE, (StateType[])BlockTags.SMALL_FLOWERS.getStates().toArray(new StateType[0])),
   TALL_FLOWER(NoCollisionBox.INSTANCE, (StateType[])BlockTags.TALL_FLOWERS.getStates().toArray(new StateType[0])),
   SAPLING(NoCollisionBox.INSTANCE, (StateType[])BlockTags.SAPLINGS.getStates().toArray(new StateType[0])),
   BUTTON(NoCollisionBox.INSTANCE, (StateType[])BlockTags.BUTTONS.getStates().toArray(new StateType[0])),
   NO_COLLISION(NoCollisionBox.INSTANCE, new StateType[]{StateTypes.TWISTING_VINES_PLANT, StateTypes.WEEPING_VINES_PLANT, StateTypes.TWISTING_VINES, StateTypes.WEEPING_VINES, StateTypes.CAVE_VINES, StateTypes.CAVE_VINES_PLANT, StateTypes.TALL_SEAGRASS, StateTypes.SEAGRASS, StateTypes.SHORT_GRASS, StateTypes.FERN, StateTypes.NETHER_SPROUTS, StateTypes.DEAD_BUSH, StateTypes.SUGAR_CANE, StateTypes.SWEET_BERRY_BUSH, StateTypes.WARPED_ROOTS, StateTypes.CRIMSON_ROOTS, StateTypes.TORCHFLOWER_CROP, StateTypes.PINK_PETALS, StateTypes.TALL_GRASS, StateTypes.LARGE_FERN, StateTypes.BAMBOO_SAPLING, StateTypes.HANGING_ROOTS, StateTypes.VINE, StateTypes.SMALL_DRIPLEAF, StateTypes.END_PORTAL, StateTypes.LEVER, StateTypes.PUMPKIN_STEM, StateTypes.MELON_STEM, StateTypes.ATTACHED_MELON_STEM, StateTypes.ATTACHED_PUMPKIN_STEM, StateTypes.BEETROOTS, StateTypes.POTATOES, StateTypes.WHEAT, StateTypes.CARROTS, StateTypes.NETHER_WART, StateTypes.MOVING_PISTON, StateTypes.AIR, StateTypes.CAVE_AIR, StateTypes.VOID_AIR, StateTypes.LIGHT, StateTypes.WATER, StateTypes.BUBBLE_COLUMN, StateTypes.FIRE, StateTypes.SOUL_FIRE}),
   KELP(new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D), new StateType[]{StateTypes.KELP}),
   BELL((player, version, datax, x, y, z) -> {
      if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else {
         BlockFace direction = datax.getFacing();
         if (datax.getAttachment() == Attachment.FLOOR) {
            return direction != BlockFace.NORTH && direction != BlockFace.SOUTH ? new HexCollisionBox(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D) : new HexCollisionBox(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
         } else {
            ComplexCollisionBox complex = new ComplexCollisionBox(3, new SimpleCollisionBox[]{new HexCollisionBox(5.0D, 6.0D, 5.0D, 11.0D, 13.0D, 11.0D), new HexCollisionBox(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D)});
            if (datax.getAttachment() == Attachment.CEILING) {
               complex.add(new HexCollisionBox(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D));
            } else if (datax.getAttachment() == Attachment.DOUBLE_WALL) {
               if (direction != BlockFace.NORTH && direction != BlockFace.SOUTH) {
                  complex.add(new HexCollisionBox(0.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
               } else {
                  complex.add(new HexCollisionBox(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 16.0D));
               }
            } else if (direction == BlockFace.NORTH) {
               complex.add(new HexCollisionBox(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 13.0D));
            } else if (direction == BlockFace.SOUTH) {
               complex.add(new HexCollisionBox(7.0D, 13.0D, 3.0D, 9.0D, 15.0D, 16.0D));
            } else if (direction == BlockFace.EAST) {
               complex.add(new HexCollisionBox(3.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
            } else {
               complex.add(new HexCollisionBox(0.0D, 13.0D, 7.0D, 13.0D, 15.0D, 9.0D));
            }

            return complex;
         }
      }
   }, new StateType[]{StateTypes.BELL}),
   SCAFFOLDING((player, version, datax, x, y, z) -> {
      if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else if (player.lastY > (double)(y + 1) - 1.0E-5D && !player.isSneaking) {
         return new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D), new HexCollisionBox(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D), new HexCollisionBox(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D), new HexCollisionBox(0.0D, 0.0D, 14.0D, 2.0D, 16.0D, 16.0D), new HexCollisionBox(14.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D)});
      } else {
         return (CollisionBox)(datax.getDistance() != 0 && datax.isBottom() && player.lastY > (double)y - 1.0E-5D ? new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D) : NoCollisionBox.INSTANCE);
      }
   }, new StateType[]{StateTypes.SCAFFOLDING}),
   LADDER((player, version, datax, x, y, z) -> {
      int width = 3;
      if (version.isOlderThanOrEquals(ClientVersion.V_1_8)) {
         width = 2;
      }

      HexCollisionBox var10000;
      switch(datax.getFacing()) {
      case NORTH:
         var10000 = new HexCollisionBox(0.0D, 0.0D, 16.0D - (double)width, 16.0D, 16.0D, 16.0D);
         break;
      case SOUTH:
         var10000 = new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, (double)width);
         break;
      case EAST:
      default:
         var10000 = new HexCollisionBox(0.0D, 0.0D, 0.0D, (double)width, 16.0D, 16.0D);
         break;
      case WEST:
         var10000 = new HexCollisionBox(16.0D - (double)width, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
      }

      return var10000;
   }, new StateType[]{StateTypes.LADDER}),
   CAMPFIRE((player, version, datax, x, y, z) -> {
      if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
         return (CollisionBox)(datax.isLit() ? NoCollisionBox.INSTANCE : new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D));
      } else {
         return new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
      }
   }, new StateType[]{StateTypes.CAMPFIRE, StateTypes.SOUL_CAMPFIRE}),
   LANTERN((player, version, datax, x, y, z) -> {
      if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else {
         return datax.isHanging() ? new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(5.0D, 1.0D, 5.0D, 11.0D, 8.0D, 11.0D), new HexCollisionBox(6.0D, 8.0D, 6.0D, 10.0D, 10.0D, 10.0D)}) : new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(5.0D, 0.0D, 5.0D, 11.0D, 7.0D, 11.0D), new HexCollisionBox(6.0D, 7.0D, 6.0D, 10.0D, 9.0D, 10.0D)});
      }
   }, (StateType[])BlockTags.LANTERNS.getStates().toArray(new StateType[0])),
   LECTERN((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isOlderThanOrEquals(ClientVersion.V_1_13_2) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), new HexCollisionBox(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D)}));
   }, new StateType[]{StateTypes.LECTERN}),
   HONEY_BLOCK((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isOlderThanOrEquals(ClientVersion.V_1_14_4) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D));
   }, new StateType[]{StateTypes.HONEY_BLOCK}),
   DRAGON_EGG_BLOCK(new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D), new StateType[]{StateTypes.DRAGON_EGG}),
   GRINDSTONE((player, version, datax, x, y, z) -> {
      BlockFace facing = datax.getFacing();
      if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         return facing != BlockFace.NORTH && facing != BlockFace.SOUTH ? new SimpleCollisionBox(0.0D, 0.0D, 0.125D, 1.0D, 1.0D, 0.875D, false) : new SimpleCollisionBox(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D, false);
      } else if (!version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
         Face attachment = datax.getFace();
         if (attachment == Face.FLOOR) {
            return facing != BlockFace.NORTH && facing != BlockFace.SOUTH ? new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(6.0D, 0.0D, 2.0D, 10.0D, 7.0D, 4.0D), new HexCollisionBox(6.0D, 0.0D, 12.0D, 10.0D, 7.0D, 14.0D), new HexCollisionBox(5.0D, 7.0D, 2.0D, 11.0D, 13.0D, 4.0D), new HexCollisionBox(5.0D, 7.0D, 12.0D, 11.0D, 13.0D, 14.0D), new HexCollisionBox(2.0D, 4.0D, 4.0D, 14.0D, 16.0D, 12.0D)}) : new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(2.0D, 0.0D, 6.0D, 4.0D, 7.0D, 10.0D), new HexCollisionBox(12.0D, 0.0D, 6.0D, 14.0D, 7.0D, 10.0D), new HexCollisionBox(2.0D, 7.0D, 5.0D, 4.0D, 13.0D, 11.0D), new HexCollisionBox(12.0D, 7.0D, 5.0D, 14.0D, 13.0D, 11.0D), new HexCollisionBox(4.0D, 4.0D, 2.0D, 12.0D, 16.0D, 14.0D)});
         } else if (attachment == Face.WALL) {
            switch(facing) {
            case NORTH:
               return new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(2.0D, 6.0D, 7.0D, 4.0D, 10.0D, 16.0D), new HexCollisionBox(12.0D, 6.0D, 7.0D, 14.0D, 10.0D, 16.0D), new HexCollisionBox(2.0D, 5.0D, 3.0D, 4.0D, 11.0D, 9.0D), new HexCollisionBox(12.0D, 5.0D, 3.0D, 14.0D, 11.0D, 9.0D), new HexCollisionBox(4.0D, 2.0D, 0.0D, 12.0D, 14.0D, 12.0D)});
            case SOUTH:
               return new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(2.0D, 6.0D, 0.0D, 4.0D, 10.0D, 7.0D), new HexCollisionBox(12.0D, 6.0D, 0.0D, 14.0D, 10.0D, 7.0D), new HexCollisionBox(2.0D, 5.0D, 7.0D, 4.0D, 11.0D, 13.0D), new HexCollisionBox(12.0D, 5.0D, 7.0D, 14.0D, 11.0D, 13.0D), new HexCollisionBox(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 16.0D)});
            case EAST:
               return new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 6.0D, 2.0D, 9.0D, 10.0D, 4.0D), new HexCollisionBox(0.0D, 6.0D, 12.0D, 9.0D, 10.0D, 14.0D), new HexCollisionBox(7.0D, 5.0D, 2.0D, 13.0D, 11.0D, 4.0D), new HexCollisionBox(7.0D, 5.0D, 12.0D, 13.0D, 11.0D, 14.0D), new HexCollisionBox(4.0D, 2.0D, 4.0D, 16.0D, 14.0D, 12.0D)});
            case WEST:
               return new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(7.0D, 6.0D, 2.0D, 16.0D, 10.0D, 4.0D), new HexCollisionBox(7.0D, 6.0D, 12.0D, 16.0D, 10.0D, 14.0D), new HexCollisionBox(3.0D, 5.0D, 2.0D, 9.0D, 11.0D, 4.0D), new HexCollisionBox(3.0D, 5.0D, 12.0D, 9.0D, 11.0D, 14.0D), new HexCollisionBox(0.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D)});
            default:
               return NoCollisionBox.INSTANCE;
            }
         } else {
            return facing != BlockFace.NORTH && facing != BlockFace.SOUTH ? new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(6.0D, 9.0D, 2.0D, 10.0D, 16.0D, 4.0D), new HexCollisionBox(6.0D, 9.0D, 12.0D, 10.0D, 16.0D, 14.0D), new HexCollisionBox(5.0D, 3.0D, 2.0D, 11.0D, 9.0D, 4.0D), new HexCollisionBox(5.0D, 3.0D, 12.0D, 11.0D, 9.0D, 14.0D), new HexCollisionBox(2.0D, 0.0D, 4.0D, 14.0D, 12.0D, 12.0D)}) : new ComplexCollisionBox(5, new SimpleCollisionBox[]{new HexCollisionBox(2.0D, 9.0D, 6.0D, 4.0D, 16.0D, 10.0D), new HexCollisionBox(12.0D, 9.0D, 6.0D, 14.0D, 16.0D, 10.0D), new HexCollisionBox(2.0D, 3.0D, 5.0D, 4.0D, 9.0D, 11.0D), new HexCollisionBox(12.0D, 3.0D, 5.0D, 14.0D, 9.0D, 11.0D), new HexCollisionBox(4.0D, 0.0D, 2.0D, 12.0D, 12.0D, 14.0D)});
         }
      } else {
         ComplexCollisionBox complexAnvil = new ComplexCollisionBox(4);
         complexAnvil.add(new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D));
         if (facing != BlockFace.NORTH && facing != BlockFace.SOUTH) {
            complexAnvil.add(new HexCollisionBox(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D));
            complexAnvil.add(new HexCollisionBox(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D));
            complexAnvil.add(new HexCollisionBox(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D));
         } else {
            complexAnvil.add(new HexCollisionBox(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D));
            complexAnvil.add(new HexCollisionBox(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D));
            complexAnvil.add(new HexCollisionBox(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D));
         }

         return complexAnvil;
      }
   }, new StateType[]{StateTypes.GRINDSTONE}),
   PANE(new DynamicCollisionPane(), (StateType[])Materials.getPanes().toArray(new StateType[0])),
   CHAIN_BLOCK((player, version, datax, x, y, z) -> {
      if (version.isOlderThan(ClientVersion.V_1_16)) {
         return PANE.fetch(player, version, datax, x, y, z);
      } else if (datax.getAxis() == Axis.X) {
         return new HexCollisionBox(0.0D, 6.5D, 6.5D, 16.0D, 9.5D, 9.5D);
      } else {
         return datax.getAxis() == Axis.Y ? new HexCollisionBox(6.5D, 0.0D, 6.5D, 9.5D, 16.0D, 9.5D) : new HexCollisionBox(6.5D, 6.5D, 0.0D, 9.5D, 9.5D, 16.0D);
      }
   }, (StateType[])Materials.getChains().toArray(new StateType[0])),
   CHORUS_PLANT(new DynamicChorusPlant(), new StateType[]{StateTypes.CHORUS_PLANT}),
   FENCE_GATE((player, version, datax, x, y, z) -> {
      if (datax.isOpen()) {
         return NoCollisionBox.INSTANCE;
      } else {
         Object var10000;
         switch(datax.getFacing()) {
         case NORTH:
         case SOUTH:
            var10000 = new SimpleCollisionBox(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D, false);
            break;
         case EAST:
         case WEST:
            var10000 = new SimpleCollisionBox(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D, false);
            break;
         default:
            var10000 = NoCollisionBox.INSTANCE;
         }

         return (CollisionBox)var10000;
      }
   }, (StateType[])BlockTags.FENCE_GATES.getStates().toArray(new StateType[0])),
   FENCE(new DynamicCollisionFence(), (StateType[])BlockTags.FENCES.getStates().toArray(new StateType[0])),
   SNOW((player, version, datax, x, y, z) -> {
      int layers = datax.getLayers();
      if (layers == 1 && version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) || !ViaVersionUtil.isAvailable || !Via.getConfig().isSnowCollisionFix()) {
            return NoCollisionBox.INSTANCE;
         }

         ++layers;
      }

      return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, (double)(layers - 1) * 0.125D, 1.0D);
   }, new StateType[]{StateTypes.SNOW}),
   STAIR(new DynamicStair(), (StateType[])BlockTags.STAIRS.getStates().toArray(new StateType[0])),
   CHEST(new DynamicChest(), (StateType[])Materials.getChests().toArray(new StateType[0])),
   ENDER_CHEST(new SimpleCollisionBox(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D, false), new StateType[]{StateTypes.ENDER_CHEST}),
   ENCHANTING_TABLE(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D, false), new StateType[]{StateTypes.ENCHANTING_TABLE}),
   FRAME((player, version, datax, x, y, z) -> {
      ComplexCollisionBox complexCollisionBox = new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D)});
      if (datax.isEye()) {
         if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            complexCollisionBox.add(new HexCollisionBox(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D));
         } else {
            complexCollisionBox.add(new HexCollisionBox(5.0D, 13.0D, 5.0D, 11.0D, 16.0D, 11.0D));
         }
      }

      return complexCollisionBox;
   }, new StateType[]{StateTypes.END_PORTAL_FRAME}),
   CARPET((player, version, datax, x, y, z) -> {
      return version.isOlderThanOrEquals(ClientVersion.V_1_7_10) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D, false) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D, false);
   }, (StateType[])BlockTags.WOOL_CARPETS.getStates().toArray(new StateType[0])),
   MOSS_CARPET(CARPET, new StateType[]{StateTypes.MOSS_CARPET}),
   PALE_MOSS_CARPET((player, version, datax, x, y, z) -> {
      if (!datax.isBottom()) {
         return NoCollisionBox.INSTANCE;
      } else {
         return (CollisionBox)(version.isOlderThan(ClientVersion.V_1_21_2) ? MOSS_CARPET.fetch(player, version, datax, x, y, z) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D, false));
      }
   }, new StateType[]{StateTypes.PALE_MOSS_CARPET}),
   DAYLIGHT(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D, false), new StateType[]{StateTypes.DAYLIGHT_DETECTOR}),
   FARMLAND((player, version, datax, x, y, z) -> {
      if (version == ClientVersion.V_1_10) {
         return (CollisionBox)(Math.abs(player.y % 1.0D) < 0.001D ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D));
      } else {
         return (CollisionBox)(version.isNewerThanOrEquals(ClientVersion.V_1_10) ? new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true));
      }
   }, new StateType[]{StateTypes.FARMLAND}),
   GRASS_PATH((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isNewerThanOrEquals(ClientVersion.V_1_9) ? new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true));
   }, new StateType[]{StateTypes.DIRT_PATH, StateTypes.GRASS_PATH}),
   LILYPAD((player, version, datax, x, y, z) -> {
      if (player.inVehicle() && player.compensatedEntities.self.getRiding().isBoat && version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         return NoCollisionBox.INSTANCE;
      } else {
         return (CollisionBox)(version.isOlderThan(ClientVersion.V_1_9) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.015625D, 1.0D, false) : new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 1.5D, 15.0D));
      }
   }, new StateType[]{StateTypes.LILY_PAD}),
   BED((player, version, datax, x, y, z) -> {
      if (version.isOlderThan(ClientVersion.V_1_14)) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D, false);
      } else {
         ComplexCollisionBox baseBox = new ComplexCollisionBox(3, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 3.0D, 0.0D, 16.0D, 9.0D, 16.0D)});
         BlockFace facing = datax.getPart() == Part.HEAD ? datax.getFacing() : datax.getFacing().getOppositeFace();
         switch(facing) {
         case NORTH:
            baseBox.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 3.0D, 3.0D, 3.0D));
            baseBox.add(new HexCollisionBox(13.0D, 0.0D, 0.0D, 16.0D, 3.0D, 3.0D));
            break;
         case SOUTH:
            baseBox.add(new HexCollisionBox(0.0D, 0.0D, 13.0D, 3.0D, 3.0D, 16.0D));
            baseBox.add(new HexCollisionBox(13.0D, 0.0D, 13.0D, 16.0D, 3.0D, 16.0D));
            break;
         case EAST:
            baseBox.add(new HexCollisionBox(13.0D, 0.0D, 0.0D, 16.0D, 3.0D, 3.0D));
            baseBox.add(new HexCollisionBox(13.0D, 0.0D, 13.0D, 16.0D, 3.0D, 16.0D));
            break;
         case WEST:
            baseBox.add(new HexCollisionBox(0.0D, 0.0D, 0.0D, 3.0D, 3.0D, 3.0D));
            baseBox.add(new HexCollisionBox(0.0D, 0.0D, 13.0D, 3.0D, 3.0D, 16.0D));
         }

         return baseBox;
      }
   }, (StateType[])BlockTags.BEDS.getStates().toArray(new StateType[0])),
   TRAPDOOR(new TrapDoorHandler(), (StateType[])BlockTags.TRAPDOORS.getStates().toArray(new StateType[0])),
   DIODES(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D, false), new StateType[]{StateTypes.REPEATER, StateTypes.COMPARATOR}),
   STRUCTURE_VOID(new SimpleCollisionBox(0.375D, 0.375D, 0.375D, 0.625D, 0.625D, 0.625D, false), new StateType[]{StateTypes.STRUCTURE_VOID}),
   END_ROD((player, version, datax, x, y, z) -> {
      return getEndRod(version, datax.getFacing());
   }, (StateType[])Materials.getRods().toArray(new StateType[0])),
   CAULDRON((player, version, datax, x, y, z) -> {
      if (version.isNewerThan(ClientVersion.V_1_13_2)) {
         return new ComplexCollisionBox(15, new SimpleCollisionBox[]{new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 0.25D, false), new SimpleCollisionBox(0.0D, 0.0D, 0.75D, 0.125D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.125D, 0.0D, 0.0D, 0.25D, 1.0D, 0.125D, false), new SimpleCollisionBox(0.125D, 0.0D, 0.875D, 0.25D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D, false), new SimpleCollisionBox(0.75D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.875D, 0.0D, 0.125D, 1.0D, 1.0D, 0.25D, false), new SimpleCollisionBox(0.875D, 0.0D, 0.75D, 1.0D, 1.0D, 0.875D, false), new SimpleCollisionBox(0.0D, 0.1875D, 0.25D, 1.0D, 0.25D, 0.75D, false), new SimpleCollisionBox(0.125D, 0.1875D, 0.125D, 0.875D, 0.25D, 0.25D, false), new SimpleCollisionBox(0.125D, 0.1875D, 0.75D, 0.875D, 0.25D, 0.875D, false), new SimpleCollisionBox(0.25D, 0.1875D, 0.0D, 0.75D, 1.0D, 0.125D, false), new SimpleCollisionBox(0.25D, 0.1875D, 0.875D, 0.75D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.0D, 0.25D, 0.25D, 0.125D, 1.0D, 0.75D, false), new SimpleCollisionBox(0.875D, 0.25D, 0.25D, 1.0D, 1.0D, 0.75D, false)});
      } else {
         double height = 0.25D;
         if (version.isOlderThan(ClientVersion.V_1_13)) {
            height = 0.3125D;
         }

         return new ComplexCollisionBox(5, new SimpleCollisionBox[]{new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, height, 1.0D, false), new SimpleCollisionBox(0.0D, height, 0.0D, 0.125D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.875D, height, 0.0D, 1.0D, 1.0D, 1.0D, false), new SimpleCollisionBox(0.0D, height, 0.0D, 1.0D, 1.0D, 0.125D, false), new SimpleCollisionBox(0.0D, height, 0.875D, 1.0D, 1.0D, 1.0D, false)});
      }
   }, (StateType[])BlockTags.CAULDRONS.getStates().toArray(new StateType[0])),
   CACTUS(new SimpleCollisionBox(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D, false), new StateType[]{StateTypes.CACTUS}),
   PISTON_BASE(new PistonBaseCollision(), new StateType[]{StateTypes.PISTON, StateTypes.STICKY_PISTON}),
   PISTON_HEAD(new PistonHeadCollision(), new StateType[]{StateTypes.PISTON_HEAD}),
   SOULSAND(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D, false), new StateType[]{StateTypes.SOUL_SAND}),
   PICKLE((player, version, datax, x, y, z) -> {
      return getPicklesBox(version, datax.getPickles());
   }, new StateType[]{StateTypes.SEA_PICKLE}),
   TURTLEEGG((player, version, datax, x, y, z) -> {
      if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         return getCocoa(version, datax.getEggs(), BlockFace.WEST);
      } else {
         return datax.getEggs() == 1 ? new HexCollisionBox(3.0D, 0.0D, 3.0D, 12.0D, 7.0D, 12.0D) : new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D);
      }
   }, new StateType[]{StateTypes.TURTLE_EGG}),
   CONDUIT((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isOlderThanOrEquals(ClientVersion.V_1_12_2) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : new HexCollisionBox(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D));
   }, new StateType[]{StateTypes.CONDUIT}),
   POT(new HexCollisionBox(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D), (StateType[])BlockTags.FLOWER_POTS.getStates().toArray(new StateType[0])),
   WALL_SIGN((player, version, datax, x, y, z) -> {
      Object var10000;
      switch(datax.getFacing()) {
      case NORTH:
         var10000 = new HexCollisionBox(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D);
         break;
      case SOUTH:
         var10000 = new HexCollisionBox(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D);
         break;
      case EAST:
         var10000 = new HexCollisionBox(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D);
         break;
      case WEST:
         var10000 = new HexCollisionBox(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D);
         break;
      default:
         var10000 = NoCollisionBox.INSTANCE;
      }

      return (CollisionBox)var10000;
   }, (StateType[])BlockTags.WALL_SIGNS.getStates().toArray(new StateType[0])),
   WALL_FAN((player, version, datax, x, y, z) -> {
      HexCollisionBox var10000;
      switch(datax.getFacing()) {
      case NORTH:
         var10000 = new HexCollisionBox(0.0D, 4.0D, 5.0D, 16.0D, 12.0D, 16.0D);
         break;
      case SOUTH:
         var10000 = new HexCollisionBox(0.0D, 4.0D, 0.0D, 16.0D, 12.0D, 11.0D);
         break;
      case EAST:
      default:
         var10000 = new HexCollisionBox(0.0D, 4.0D, 0.0D, 11.0D, 12.0D, 16.0D);
         break;
      case WEST:
         var10000 = new HexCollisionBox(5.0D, 4.0D, 0.0D, 16.0D, 12.0D, 16.0D);
      }

      return var10000;
   }, (StateType[])BlockTags.WALL_CORALS.getStates().toArray(new StateType[0])),
   CORAL_PLANT((player, version, datax, x, y, z) -> {
      return new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);
   }, (StateType[])Stream.concat(Arrays.stream((StateType[])BlockTags.CORAL_PLANTS.getStates().toArray(new StateType[0])), Stream.of(StateTypes.DEAD_HORN_CORAL, StateTypes.DEAD_TUBE_CORAL, StateTypes.DEAD_BRAIN_CORAL, StateTypes.DEAD_BUBBLE_CORAL, StateTypes.DEAD_FIRE_CORAL)).distinct().toArray((x$0) -> {
      return new StateType[x$0];
   })),
   SIGN(new SimpleCollisionBox(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D, false), (StateType[])BlockTags.STANDING_SIGNS.getStates().toArray(new StateType[0])),
   STONE_PRESSURE_PLATE((player, version, datax, x, y, z) -> {
      return datax.isPowered() ? new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D) : new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);
   }, (StateType[])BlockTags.STONE_PRESSURE_PLATES.getStates().toArray(new StateType[0])),
   WOOD_PRESSURE_PLATE((player, version, datax, x, y, z) -> {
      return datax.isPowered() ? new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D) : new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);
   }, (StateType[])BlockTags.WOODEN_PRESSURE_PLATES.getStates().toArray(new StateType[0])),
   OTHER_PRESSURE_PLATE((player, version, datax, x, y, z) -> {
      return datax.getPower() > 0 ? new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D) : new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);
   }, new StateType[]{StateTypes.LIGHT_WEIGHTED_PRESSURE_PLATE, StateTypes.HEAVY_WEIGHTED_PRESSURE_PLATE}),
   TRIPWIRE((player, version, datax, x, y, z) -> {
      return datax.isAttached() ? new HexCollisionBox(0.0D, 1.0D, 0.0D, 16.0D, 2.5D, 16.0D) : new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
   }, new StateType[]{StateTypes.TRIPWIRE}),
   TRIPWIRE_HOOK((player, version, datax, x, y, z) -> {
      HexCollisionBox var10000;
      switch(datax.getFacing()) {
      case NORTH:
         var10000 = new HexCollisionBox(5.0D, 0.0D, 10.0D, 11.0D, 10.0D, 16.0D);
         break;
      case SOUTH:
         var10000 = new HexCollisionBox(5.0D, 0.0D, 0.0D, 11.0D, 10.0D, 6.0D);
         break;
      case EAST:
      default:
         var10000 = new HexCollisionBox(0.0D, 0.0D, 5.0D, 6.0D, 10.0D, 11.0D);
         break;
      case WEST:
         var10000 = new HexCollisionBox(10.0D, 0.0D, 5.0D, 16.0D, 10.0D, 11.0D);
      }

      return var10000;
   }, new StateType[]{StateTypes.TRIPWIRE_HOOK}),
   TORCH(new HexCollisionBox(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D), new StateType[]{StateTypes.TORCH, StateTypes.REDSTONE_TORCH, StateTypes.COPPER_TORCH}),
   WALL_TORCH((player, version, datax, x, y, z) -> {
      HexCollisionBox var10000;
      switch(datax.getFacing()) {
      case NORTH:
         var10000 = new HexCollisionBox(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D);
         break;
      case SOUTH:
         var10000 = new HexCollisionBox(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D);
         break;
      case EAST:
         var10000 = new HexCollisionBox(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D);
         break;
      case WEST:
         var10000 = new HexCollisionBox(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D);
         break;
      default:
         var10000 = new HexCollisionBox(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
      }

      return var10000;
   }, new StateType[]{StateTypes.WALL_TORCH, StateTypes.REDSTONE_WALL_TORCH, StateTypes.COPPER_WALL_TORCH}),
   CANDLE((player, version, datax, x, y, z) -> {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_17)) {
         HexCollisionBox var10000;
         switch(datax.getCandles()) {
         case 1:
            var10000 = new HexCollisionBox(7.0D, 0.0D, 7.0D, 9.0D, 6.0D, 9.0D);
            break;
         case 2:
            var10000 = new HexCollisionBox(5.0D, 0.0D, 6.0D, 11.0D, 6.0D, 9.0D);
            break;
         case 3:
            var10000 = new HexCollisionBox(5.0D, 0.0D, 6.0D, 10.0D, 6.0D, 11.0D);
            break;
         default:
            var10000 = new HexCollisionBox(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 10.0D);
         }

         return var10000;
      } else {
         return getPicklesBox(version, datax.getCandles());
      }
   }, (StateType[])BlockTags.CANDLES.getStates().toArray(new StateType[0])),
   CANDLE_CAKE((player, version, datax, x, y, z) -> {
      SimpleCollisionBox cake = new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
      return (CollisionBox)(version.isOlderThan(ClientVersion.V_1_17) ? cake : new ComplexCollisionBox(2, new SimpleCollisionBox[]{cake, new HexCollisionBox(7.0D, 8.0D, 7.0D, 9.0D, 14.0D, 9.0D)}));
   }, (StateType[])BlockTags.CANDLE_CAKES.getStates().toArray(new StateType[0])),
   SCULK_SENSOR(new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), new StateType[]{StateTypes.SCULK_SENSOR, StateTypes.CALIBRATED_SCULK_SENSOR}),
   DECORATED_POT((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_19_3) ? new HexCollisionBox(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true));
   }, new StateType[]{StateTypes.DECORATED_POT}),
   BIG_DRIPLEAF((player, version, datax, x, y, z) -> {
      Tilt tilt = datax.getTilt();
      if (version.isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
         return tilt == Tilt.FULL ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D, false) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else if (tilt != Tilt.NONE && tilt != Tilt.UNSTABLE) {
         return (CollisionBox)(tilt == Tilt.PARTIAL ? new HexCollisionBox(0.0D, 11.0D, 0.0D, 16.0D, 13.0D, 16.0D) : NoCollisionBox.INSTANCE);
      } else {
         return new HexCollisionBox(0.0D, 11.0D, 0.0D, 16.0D, 15.0D, 16.0D);
      }
   }, new StateType[]{StateTypes.BIG_DRIPLEAF}),
   POINTED_DRIPSTONE((player, version, datax, x, y, z) -> {
      if (version.isOlderThan(ClientVersion.V_1_17)) {
         return getEndRod(version, BlockFace.UP);
      } else {
         HexOffsetCollisionBox box;
         if (datax.getThickness() == Thickness.TIP_MERGE) {
            box = new HexOffsetCollisionBox(datax.getType(), 5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
         } else if (datax.getThickness() == Thickness.TIP) {
            if (datax.getVerticalDirection() == VerticalDirection.DOWN) {
               box = new HexOffsetCollisionBox(datax.getType(), 5.0D, 5.0D, 5.0D, 11.0D, 16.0D, 11.0D);
            } else {
               box = new HexOffsetCollisionBox(datax.getType(), 5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
            }
         } else if (datax.getThickness() == Thickness.FRUSTUM) {
            box = new HexOffsetCollisionBox(datax.getType(), 4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
         } else if (datax.getThickness() == Thickness.MIDDLE) {
            box = new HexOffsetCollisionBox(datax.getType(), 3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
         } else {
            box = new HexOffsetCollisionBox(datax.getType(), 2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
         }

         return box;
      }
   }, new StateType[]{StateTypes.POINTED_DRIPSTONE}),
   POWDER_SNOW((player, version, datax, x, y, z) -> {
      if (version.isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else if (player.fallDistance > 2.5D) {
         return player.getClientVersion() == ClientVersion.V_1_21_4 ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 0.9D, 1.0D, false);
      } else {
         ItemStack boots = player.inventory.getBoots();
         return (CollisionBox)(player.lastY > (double)(y + 1) - 1.0E-5D && boots != null && boots.getType() == ItemTypes.LEATHER_BOOTS && !player.isSneaking && !player.inVehicle() ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : NoCollisionBox.INSTANCE);
      }
   }, new StateType[]{StateTypes.POWDER_SNOW}),
   NETHER_PORTAL((player, version, datax, x, y, z) -> {
      return datax.getAxis() == Axis.X ? new HexCollisionBox(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D) : new HexCollisionBox(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
   }, new StateType[]{StateTypes.NETHER_PORTAL}),
   AZALEA((player, version, datax, x, y, z) -> {
      return new ComplexCollisionBox(2, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D), new HexCollisionBox(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D)});
   }, new StateType[]{StateTypes.AZALEA, StateTypes.FLOWERING_AZALEA}),
   AMETHYST_CLUSTER((player, version, datax, x, y, z) -> {
      return getAmethystBox(version, datax.getFacing(), 7, 3);
   }, new StateType[]{StateTypes.AMETHYST_CLUSTER}),
   SMALL_AMETHYST_BUD((player, version, datax, x, y, z) -> {
      return getAmethystBox(version, datax.getFacing(), 3, 4);
   }, new StateType[]{StateTypes.SMALL_AMETHYST_BUD}),
   MEDIUM_AMETHYST_BUD((player, version, datax, x, y, z) -> {
      return getAmethystBox(version, datax.getFacing(), 4, 3);
   }, new StateType[]{StateTypes.MEDIUM_AMETHYST_BUD}),
   LARGE_AMETHYST_BUD((player, version, datax, x, y, z) -> {
      return getAmethystBox(version, datax.getFacing(), 5, 3);
   }, new StateType[]{StateTypes.LARGE_AMETHYST_BUD}),
   MUD_BLOCK((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isNewerThanOrEquals(ClientVersion.V_1_19) ? new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
   }, new StateType[]{StateTypes.MUD}),
   MANGROVE_PROPAGULE_BLOCK((player, version, datax, x, y, z) -> {
      if (!datax.isHanging()) {
         return new HexCollisionBox(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
      } else {
         HexCollisionBox var10000;
         switch(datax.getAge()) {
         case 0:
            var10000 = new HexCollisionBox(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D);
            break;
         case 1:
            var10000 = new HexCollisionBox(7.0D, 10.0D, 7.0D, 9.0D, 16.0D, 9.0D);
            break;
         case 2:
            var10000 = new HexCollisionBox(7.0D, 7.0D, 7.0D, 9.0D, 16.0D, 9.0D);
            break;
         case 3:
            var10000 = new HexCollisionBox(7.0D, 3.0D, 7.0D, 9.0D, 16.0D, 9.0D);
            break;
         default:
            var10000 = new HexCollisionBox(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
         }

         return var10000;
      }
   }, new StateType[]{StateTypes.MANGROVE_PROPAGULE}),
   SCULK_SHRIKER((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_18_2) ? new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true));
   }, new StateType[]{StateTypes.SCULK_SHRIEKER}),
   SNIFFER_EGG((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isNewerThan(ClientVersion.V_1_19_4) ? new HexCollisionBox(1.0D, 0.0D, 2.0D, 15.0D, 16.0D, 14.0D) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true));
   }, new StateType[]{StateTypes.SNIFFER_EGG}),
   PITCHER_CROP((player, version, datax, x, y, z) -> {
      if (version.isNewerThan(ClientVersion.V_1_19_4)) {
         SimpleCollisionBox COLLISION_SHAPE_BULB = new HexCollisionBox(5.0D, -1.0D, 5.0D, 11.0D, 3.0D, 11.0D);
         SimpleCollisionBox COLLISION_SHAPE_CROP = new HexCollisionBox(3.0D, -1.0D, 3.0D, 13.0D, 5.0D, 13.0D);
         if (datax.getAge() == 0) {
            return COLLISION_SHAPE_BULB;
         } else {
            return (CollisionBox)(datax.getHalf() == Half.LOWER ? COLLISION_SHAPE_CROP : NoCollisionBox.INSTANCE);
         }
      } else {
         return NoCollisionBox.INSTANCE;
      }
   }, new StateType[]{StateTypes.PITCHER_CROP}),
   WALL_HANGING_SIGNS((player, version, datax, x, y, z) -> {
      Object var10000;
      switch(datax.getFacing()) {
      case NORTH:
      case SOUTH:
         var10000 = new HexCollisionBox(0.0D, 14.0D, 6.0D, 16.0D, 16.0D, 10.0D);
         break;
      case EAST:
      case WEST:
         var10000 = new HexCollisionBox(6.0D, 14.0D, 0.0D, 10.0D, 16.0D, 16.0D);
         break;
      default:
         var10000 = NoCollisionBox.INSTANCE;
      }

      return (CollisionBox)var10000;
   }, (StateType[])BlockTags.WALL_HANGING_SIGNS.getStates().toArray(new StateType[0])),
   DRIED_GHAST((player, version, datax, x, y, z) -> {
      if (player.getClientVersion().isNewerThan(ClientVersion.V_1_21_5)) {
         return new HexCollisionBox(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);
      } else if (player.getClientVersion().isNewerThan(ClientVersion.V_1_12_2)) {
         return new ComplexCollisionBox(2, new SimpleCollisionBox[]{new SimpleCollisionBox(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D), new SimpleCollisionBox(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.8125D, 0.8125D)});
      } else {
         return player.getClientVersion().isNewerThan(ClientVersion.V_1_8) ? new SimpleCollisionBox(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.8125D, 0.8125D) : new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      }
   }, new StateType[]{StateTypes.DRIED_GHAST}),
   SHELF((player, version, datax, x, y, z) -> {
      if (version.isOlderThan(ClientVersion.V_1_21_9)) {
         return new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true);
      } else {
         ComplexCollisionBox var10000;
         switch(datax.getFacing()) {
         case NORTH:
            var10000 = new ComplexCollisionBox(3, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 12.0D, 11.0D, 16.0D, 16.0D, 13.0D), new HexCollisionBox(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D), new HexCollisionBox(0.0D, 0.0D, 11.0D, 16.0D, 4.0D, 13.0D)});
            break;
         case SOUTH:
            var10000 = new ComplexCollisionBox(3, new SimpleCollisionBox[]{new HexCollisionBox(0.0D, 12.0D, 3.0D, 16.0D, 16.0D, 5.0D), new HexCollisionBox(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D), new HexCollisionBox(0.0D, 0.0D, 3.0D, 16.0D, 4.0D, 5.0D)});
            break;
         case EAST:
            var10000 = new ComplexCollisionBox(3, new SimpleCollisionBox[]{new HexCollisionBox(3.0D, 12.0D, 0.0D, 5.0D, 16.0D, 16.0D), new HexCollisionBox(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D), new HexCollisionBox(3.0D, 0.0D, 0.0D, 5.0D, 4.0D, 16.0D)});
            break;
         case WEST:
            var10000 = new ComplexCollisionBox(3, new SimpleCollisionBox[]{new HexCollisionBox(11.0D, 12.0D, 0.0D, 13.0D, 16.0D, 16.0D), new HexCollisionBox(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D), new HexCollisionBox(11.0D, 0.0D, 0.0D, 13.0D, 4.0D, 16.0D)});
            break;
         default:
            throw new IllegalStateException("Unexpected value: " + String.valueOf(datax.getFacing()));
         }

         return var10000;
      }
   }, (StateType[])BlockTags.WOODEN_SHELVES.getStates().toArray(new StateType[0])),
   COPPER_GOLEM_STATUE((player, version, datax, x, y, z) -> {
      return (CollisionBox)(version.isOlderThan(ClientVersion.V_1_21_9) ? new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true) : new HexCollisionBox(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D));
   }, (StateType[])BlockTags.COPPER_GOLEM_STATUES.getStates().toArray(new StateType[0])),
   DEFAULT(new SimpleCollisionBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D, true), new StateType[]{StateTypes.STONE});

   private static final Map<StateType, CollisionData> rawLookupMap = new IdentityHashMap();
   public final StateType[] materials;
   public CollisionBox box;
   public CollisionFactory dynamic;

   private CollisionData(CollisionBox param3, StateType... param4) {
      this.box = box;
      Set<StateType> mList = new HashSet(Arrays.asList(states));
      mList.remove((Object)null);
      this.materials = (StateType[])mList.toArray(new StateType[0]);
   }

   private CollisionData(CollisionFactory param3, StateType... param4) {
      this.dynamic = dynamic;
      Set<StateType> mList = new HashSet(Arrays.asList(states));
      mList.remove((Object)null);
      this.materials = (StateType[])mList.toArray(new StateType[0]);
   }

   private static CollisionBox getAmethystBox(ClientVersion version, BlockFace facing, int param_0, int param_1) {
      if (version.isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
         return NoCollisionBox.INSTANCE;
      } else {
         HexCollisionBox var10000;
         switch(facing) {
         case DOWN:
            var10000 = new HexCollisionBox((double)param_1, (double)(16 - param_0), (double)param_1, (double)(16 - param_1), 16.0D, (double)(16 - param_1));
            break;
         case NORTH:
            var10000 = new HexCollisionBox((double)param_1, (double)param_1, (double)(16 - param_0), (double)(16 - param_1), (double)(16 - param_1), 16.0D);
            break;
         case SOUTH:
            var10000 = new HexCollisionBox((double)param_1, (double)param_1, 0.0D, (double)(16 - param_1), (double)(16 - param_1), (double)param_0);
            break;
         case EAST:
            var10000 = new HexCollisionBox(0.0D, (double)param_1, (double)param_1, (double)param_0, (double)(16 - param_1), (double)(16 - param_1));
            break;
         case WEST:
            var10000 = new HexCollisionBox((double)(16 - param_0), (double)param_1, (double)param_1, 16.0D, (double)(16 - param_1), (double)(16 - param_1));
            break;
         default:
            var10000 = new HexCollisionBox((double)param_1, 0.0D, (double)param_1, (double)(16 - param_1), (double)param_0, (double)(16 - param_1));
         }

         return var10000;
      }
   }

   private static CollisionBox getPicklesBox(ClientVersion version, int pickles) {
      if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         return getCocoa(version, pickles, BlockFace.WEST);
      } else {
         Object var10000;
         switch(pickles) {
         case 1:
            var10000 = new HexCollisionBox(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D);
            break;
         case 2:
            var10000 = new HexCollisionBox(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D);
            break;
         case 3:
            var10000 = new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
            break;
         case 4:
            var10000 = new HexCollisionBox(2.0D, 0.0D, 2.0D, 14.0D, 7.0D, 14.0D);
            break;
         default:
            var10000 = NoCollisionBox.INSTANCE;
         }

         return (CollisionBox)var10000;
      }
   }

   public static CollisionBox getCocoa(ClientVersion version, int age, BlockFace direction) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_9_1) && version.isOlderThan(ClientVersion.V_1_11)) {
         age = Math.min(age, 1);
      }

      switch(direction) {
      case EAST:
         switch(age) {
         case 0:
            return new HexCollisionBox(11.0D, 7.0D, 6.0D, 15.0D, 12.0D, 10.0D);
         case 1:
            return new HexCollisionBox(9.0D, 5.0D, 5.0D, 15.0D, 12.0D, 11.0D);
         case 2:
            return new HexCollisionBox(7.0D, 3.0D, 4.0D, 15.0D, 12.0D, 12.0D);
         }
      case WEST:
         switch(age) {
         case 0:
            return new HexCollisionBox(1.0D, 7.0D, 6.0D, 5.0D, 12.0D, 10.0D);
         case 1:
            return new HexCollisionBox(1.0D, 5.0D, 5.0D, 7.0D, 12.0D, 11.0D);
         case 2:
            return new HexCollisionBox(1.0D, 3.0D, 4.0D, 9.0D, 12.0D, 12.0D);
         }
      case NORTH:
         switch(age) {
         case 0:
            return new HexCollisionBox(6.0D, 7.0D, 1.0D, 10.0D, 12.0D, 5.0D);
         case 1:
            return new HexCollisionBox(5.0D, 5.0D, 1.0D, 11.0D, 12.0D, 7.0D);
         case 2:
            return new HexCollisionBox(4.0D, 3.0D, 1.0D, 12.0D, 12.0D, 9.0D);
         }
      case SOUTH:
         switch(age) {
         case 0:
            return new HexCollisionBox(6.0D, 7.0D, 11.0D, 10.0D, 12.0D, 15.0D);
         case 1:
            return new HexCollisionBox(5.0D, 5.0D, 9.0D, 11.0D, 12.0D, 15.0D);
         case 2:
            return new HexCollisionBox(4.0D, 3.0D, 7.0D, 12.0D, 12.0D, 15.0D);
         }
      default:
         return NoCollisionBox.INSTANCE;
      }
   }

   private static CollisionBox getEndRod(ClientVersion version, BlockFace face) {
      if (version.isOlderThan(ClientVersion.V_1_9)) {
         return NoCollisionBox.INSTANCE;
      } else {
         HexCollisionBox var10000;
         switch(face) {
         case NORTH:
         case SOUTH:
            var10000 = new HexCollisionBox(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D);
            break;
         case EAST:
         case WEST:
            var10000 = new HexCollisionBox(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
            break;
         default:
            var10000 = new HexCollisionBox(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
         }

         return var10000;
      }
   }

   public static CollisionData getData(StateType state) {
      return !state.isSolid() && state != StateTypes.LAVA && state != StateTypes.SCAFFOLDING && state != StateTypes.PITCHER_CROP && state != StateTypes.HEAVY_CORE && state != StateTypes.PALE_MOSS_CARPET && !BlockTags.WALL_HANGING_SIGNS.contains(state) && !BlockTags.COPPER_GOLEM_STATUES.contains(state) ? NO_COLLISION : (CollisionData)rawLookupMap.getOrDefault(state, DEFAULT);
   }

   public static CollisionData getRawData(StateType state) {
      return (CollisionData)rawLookupMap.getOrDefault(state, DEFAULT);
   }

   public CollisionBox getMovementCollisionBox(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
      return this.fetch(player, version, block, x, y, z).offset((double)x, (double)y, (double)z);
   }

   public CollisionBox getMovementCollisionBox(GrimPlayer player, ClientVersion version, WrappedBlockState block) {
      return (CollisionBox)(this.box != null ? this.box.copy() : new DynamicCollisionBox(player, version, this.dynamic, block));
   }

   public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
      return (CollisionBox)(this.box != null ? this.box.copy() : new DynamicCollisionBox(player, version, this.dynamic, block));
   }

   // $FF: synthetic method
   private static CollisionData[] $values() {
      return new CollisionData[]{LAVA, BREWING_STAND, BAMBOO, COMPOSTER, RAIL, ANVIL, WALL, SLAB, SKULL, PIGLIN_HEAD, WALL_SKULL, PIGLIN_WALL_HEAD, DOOR, HOPPER, CAKE, COCOA_BEANS, STONE_CUTTER, CORAL_FAN, RAILS, BANNER, SMALL_FLOWER, TALL_FLOWER, SAPLING, BUTTON, NO_COLLISION, KELP, BELL, SCAFFOLDING, LADDER, CAMPFIRE, LANTERN, LECTERN, HONEY_BLOCK, DRAGON_EGG_BLOCK, GRINDSTONE, PANE, CHAIN_BLOCK, CHORUS_PLANT, FENCE_GATE, FENCE, SNOW, STAIR, CHEST, ENDER_CHEST, ENCHANTING_TABLE, FRAME, CARPET, MOSS_CARPET, PALE_MOSS_CARPET, DAYLIGHT, FARMLAND, GRASS_PATH, LILYPAD, BED, TRAPDOOR, DIODES, STRUCTURE_VOID, END_ROD, CAULDRON, CACTUS, PISTON_BASE, PISTON_HEAD, SOULSAND, PICKLE, TURTLEEGG, CONDUIT, POT, WALL_SIGN, WALL_FAN, CORAL_PLANT, SIGN, STONE_PRESSURE_PLATE, WOOD_PRESSURE_PLATE, OTHER_PRESSURE_PLATE, TRIPWIRE, TRIPWIRE_HOOK, TORCH, WALL_TORCH, CANDLE, CANDLE_CAKE, SCULK_SENSOR, DECORATED_POT, BIG_DRIPLEAF, POINTED_DRIPSTONE, POWDER_SNOW, NETHER_PORTAL, AZALEA, AMETHYST_CLUSTER, SMALL_AMETHYST_BUD, MEDIUM_AMETHYST_BUD, LARGE_AMETHYST_BUD, MUD_BLOCK, MANGROVE_PROPAGULE_BLOCK, SCULK_SHRIKER, SNIFFER_EGG, PITCHER_CROP, WALL_HANGING_SIGNS, DRIED_GHAST, SHELF, COPPER_GOLEM_STATUE, DEFAULT};
   }

   static {
      CollisionData[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         CollisionData data = var0[var2];
         StateType[] var4 = data.materials;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            StateType type = var4[var6];
            rawLookupMap.put(type, data);
         }
      }

   }
}
