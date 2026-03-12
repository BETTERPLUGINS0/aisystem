package me.gypopo.economyshopgui.providers.priceModifiers.seasons;

import java.util.Locale;

public enum SeasonType {
   SPRING,
   FALL,
   SUMMER,
   WINTER,
   DISABLED;

   public static SeasonType getFromString(String name) {
      try {
         return valueOf(name.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public static SeasonType get(me.casperge.realisticseasons.season.Season season) {
      try {
         return valueOf(season.name());
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   // $FF: synthetic method
   private static SeasonType[] $values() {
      return new SeasonType[]{SPRING, FALL, SUMMER, WINTER, DISABLED};
   }
}
