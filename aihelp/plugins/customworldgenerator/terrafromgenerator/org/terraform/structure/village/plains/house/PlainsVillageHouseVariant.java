package org.terraform.structure.village.plains.house;

import java.util.Random;
import org.jetbrains.annotations.NotNull;

public enum PlainsVillageHouseVariant {
   WOODEN,
   CLAY,
   COBBLESTONE;

   public static PlainsVillageHouseVariant roll(@NotNull Random rand) {
      int index = rand.nextInt(values().length);
      return values()[index];
   }

   // $FF: synthetic method
   private static PlainsVillageHouseVariant[] $values() {
      return new PlainsVillageHouseVariant[]{WOODEN, CLAY, COBBLESTONE};
   }
}
