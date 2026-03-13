package me.casperge.realisticseasons.temperature;

public enum TempEffect {
   COLD_SLOWNESS,
   COLD_HUNGER,
   COLD_FREEZING,
   HEAT_NO_HEALING,
   HEAT_SLOWNESS,
   HEAT_FIRE,
   BOOSTS,
   HYDRATED;

   // $FF: synthetic method
   private static TempEffect[] $values() {
      return new TempEffect[]{COLD_SLOWNESS, COLD_HUNGER, COLD_FREEZING, HEAT_NO_HEALING, HEAT_SLOWNESS, HEAT_FIRE, BOOSTS, HYDRATED};
   }
}
