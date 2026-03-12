package ac.grim.grimac.utils.blockplace;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.ItemTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.collisions.AxisUtil;
import ac.grim.grimac.utils.latency.CompensatedWorld;
import ac.grim.grimac.utils.nmsutil.Materials;
import lombok.Generated;

public final class ConsumesBlockPlace {
   public static boolean consumesPlace(@NotNull GrimPlayer player, @NotNull WrappedBlockState state, @NotNull BlockPlace place) {
      if (state.getType() == StateTypes.BELL) {
         return goodBellHit(state, place);
      } else if (BlockTags.CANDLE_CAKES.contains(state.getType())) {
         WrappedBlockState cake = StateTypes.CAKE.createBlockState(CompensatedWorld.blockVersion);
         cake.setBites(1);
         player.compensatedWorld.updateBlock(place.position, cake);
         return true;
      } else if (state.getType() == StateTypes.CAKE) {
         if (state.getBites() == 0 && BlockTags.CANDLES.contains(place.material)) {
            player.compensatedWorld.updateBlock(place.position, StateTypes.CANDLE_CAKE.createBlockState(CompensatedWorld.blockVersion));
            return true;
         } else if (player.gamemode != GameMode.CREATIVE && player.food >= 20) {
            return false;
         } else {
            if (state.getBites() != 6) {
               state.setBites(state.getBites() + 1);
               player.compensatedWorld.updateBlock(place.position, state);
            } else {
               player.compensatedWorld.updateBlock(place.position, StateTypes.AIR.createBlockState(CompensatedWorld.blockVersion));
            }

            return true;
         }
      } else if (state.getType() != StateTypes.CAVE_VINES && state.getType() != StateTypes.CAVE_VINES_PLANT) {
         if (state.getType() == StateTypes.SWEET_BERRY_BUSH) {
            if (state.getAge() != 3 && place.itemStack.getType() == ItemTypes.BONE_MEAL) {
               return false;
            } else if (state.getAge() > 1) {
               state.setAge(1);
               player.compensatedWorld.updateBlock(place.position, state);
               return true;
            } else {
               return false;
            }
         } else if (state.getType() == StateTypes.TNT && (place.itemStack.getType() == ItemTypes.FIRE_CHARGE || place.itemStack.getType() == ItemTypes.FLINT_AND_STEEL)) {
            player.compensatedWorld.updateBlock(place.position, StateTypes.AIR.createBlockState(CompensatedWorld.blockVersion));
            return true;
         } else if (state.getType() == StateTypes.RESPAWN_ANCHOR) {
            if (place.itemStack.getType() == ItemTypes.GLOWSTONE) {
               return true;
            } else {
               return !place.isBlock && player.inventory.getOffHand().getType() == ItemTypes.GLOWSTONE;
            }
         } else if (state.getType() != StateTypes.COMMAND_BLOCK && state.getType() != StateTypes.CHAIN_COMMAND_BLOCK && state.getType() != StateTypes.REPEATING_COMMAND_BLOCK && state.getType() != StateTypes.JIGSAW && state.getType() != StateTypes.STRUCTURE_BLOCK) {
            if (state.getType() == StateTypes.COMPOSTER) {
               if (Materials.isCompostable(place.itemStack.getType()) && state.getLevel() < 8) {
                  return true;
               } else {
                  return state.getLevel() == 8;
               }
            } else if (state.getType() == StateTypes.JUKEBOX) {
               return state.isHasRecord();
            } else if (state.getType() == StateTypes.LECTERN) {
               return state.isHasBook() ? true : ItemTags.LECTERN_BOOKS.contains(place.itemStack.getType());
            } else {
               return false;
            }
         } else {
            return player.canPlaceGameMasterBlocks();
         }
      } else if (state.isBerries()) {
         state.setBerries(false);
         player.compensatedWorld.updateBlock(place.position, state);
         return true;
      } else {
         return false;
      }
   }

   private static boolean goodBellHit(@NotNull WrappedBlockState bell, @NotNull BlockPlace place) {
      BlockFace direction = place.getFace();
      return place.hitData != null && isProperHit(bell, direction, place.hitData.getRelativeBlockHitLocation().getY());
   }

   private static boolean isProperHit(@NotNull WrappedBlockState bell, @NotNull BlockFace direction, double p_49742_) {
      boolean var10000;
      label39: {
         if (direction != BlockFace.UP && direction != BlockFace.DOWN && !(p_49742_ > 0.8123999834060669D)) {
            switch(bell.getAttachment()) {
            case FLOOR:
               if (AxisUtil.isSameAxis(bell.getFacing(), direction)) {
                  break label39;
               }
               break;
            case SINGLE_WALL:
            case DOUBLE_WALL:
               if (!AxisUtil.isSameAxis(bell.getFacing(), direction)) {
                  break label39;
               }
               break;
            case CEILING:
               break label39;
            default:
               throw new IncompatibleClassChangeError();
            }
         }

         var10000 = false;
         return var10000;
      }

      var10000 = true;
      return var10000;
   }

   @Generated
   private ConsumesBlockPlace() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
