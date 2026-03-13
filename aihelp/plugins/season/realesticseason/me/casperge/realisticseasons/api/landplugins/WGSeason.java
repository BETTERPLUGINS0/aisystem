package me.casperge.realisticseasons.api.landplugins;

import me.casperge.realisticseasons.season.Season;

public enum WGSeason {
   SUMMER,
   WINTER,
   FALL,
   SPRING;

   public static Season getRSSeason(WGSeason var0) {
      switch(var0.ordinal()) {
      case 0:
         return Season.SUMMER;
      case 1:
         return Season.WINTER;
      case 2:
         return Season.FALL;
      case 3:
         return Season.SPRING;
      default:
         return null;
      }
   }

   // $FF: synthetic method
   private static WGSeason[] $values() {
      return new WGSeason[]{SUMMER, WINTER, FALL, SPRING};
   }
}
