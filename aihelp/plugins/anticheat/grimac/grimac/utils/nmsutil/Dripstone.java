package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Thickness;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.VerticalDirection;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import lombok.Generated;

public final class Dripstone {
   public static void update(@NotNull GrimPlayer player, @NotNull WrappedBlockState toPlace, int x, int y, int z, boolean secondaryUse) {
      VerticalDirection primaryDirection = toPlace.getVerticalDirection();
      VerticalDirection opposite = toPlace.getVerticalDirection() == VerticalDirection.UP ? VerticalDirection.DOWN : VerticalDirection.UP;
      WrappedBlockState typePlacingOn = player.compensatedWorld.getBlock(x, y + (primaryDirection == VerticalDirection.UP ? 1 : -1), z);
      Thickness dripThick;
      if (isPointedDripstoneWithDirection(typePlacingOn, opposite)) {
         dripThick = secondaryUse && typePlacingOn.getThickness() != Thickness.TIP_MERGE ? Thickness.TIP : Thickness.TIP_MERGE;
         toPlace.setThickness(dripThick);
      } else if (!isPointedDripstoneWithDirection(typePlacingOn, primaryDirection)) {
         toPlace.setThickness(Thickness.TIP);
      } else {
         dripThick = typePlacingOn.getThickness();
         if (dripThick != Thickness.TIP && dripThick != Thickness.TIP_MERGE) {
            WrappedBlockState oppositeData = player.compensatedWorld.getBlock(x, y + (opposite == VerticalDirection.UP ? 1 : -1), z);
            Thickness toSetThick = !isPointedDripstoneWithDirection(oppositeData, primaryDirection) ? Thickness.BASE : Thickness.MIDDLE;
            toPlace.setThickness(toSetThick);
         } else {
            toPlace.setThickness(Thickness.FRUSTUM);
         }
      }

   }

   private static boolean isPointedDripstoneWithDirection(@NotNull WrappedBlockState unknown, VerticalDirection direction) {
      return unknown.getType() == StateTypes.POINTED_DRIPSTONE && unknown.getVerticalDirection() == direction;
   }

   @Generated
   private Dripstone() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
