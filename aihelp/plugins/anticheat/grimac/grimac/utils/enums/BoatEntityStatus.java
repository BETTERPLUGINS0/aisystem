package ac.grim.grimac.utils.enums;

public enum BoatEntityStatus {
   IN_WATER,
   UNDER_WATER,
   UNDER_FLOWING_WATER,
   ON_LAND,
   IN_AIR;

   // $FF: synthetic method
   private static BoatEntityStatus[] $values() {
      return new BoatEntityStatus[]{IN_WATER, UNDER_WATER, UNDER_FLOWING_WATER, ON_LAND, IN_AIR};
   }
}
