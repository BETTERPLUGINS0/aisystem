package org.terraform.biome;

import org.jetbrains.annotations.NotNull;
import org.terraform.main.config.TConfig;
import org.terraform.utils.Range;

public enum BiomeClimate {
   HUMID_VEGETATION(Range.between(TConfig.c.CLIMATE_HUMIDVEGETATION_MINTEMP, TConfig.c.CLIMATE_HUMIDVEGETATION_MAXTEMP), Range.between(TConfig.c.CLIMATE_HUMIDVEGETATION_MINMOIST, TConfig.c.CLIMATE_HUMIDVEGETATION_MAXMOIST), 2),
   DRY_VEGETATION(Range.between(TConfig.c.CLIMATE_DRYVEGETATION_MINTEMP, TConfig.c.CLIMATE_DRYVEGETATION_MAXTEMP), Range.between(TConfig.c.CLIMATE_DRYVEGETATION_MINMOIST, TConfig.c.CLIMATE_DRYVEGETATION_MAXMOIST), 1),
   HOT_BARREN(Range.between(TConfig.c.CLIMATE_HOTBARREN_MINTEMP, TConfig.c.CLIMATE_HOTBARREN_MAXTEMP), Range.between(TConfig.c.CLIMATE_HOTBARREN_MINMOIST, TConfig.c.CLIMATE_HOTBARREN_MAXMOIST), 2),
   COLD(Range.between(TConfig.c.CLIMATE_COLD_MINTEMP, TConfig.c.CLIMATE_COLD_MAXTEMP), Range.between(TConfig.c.CLIMATE_COLD_MINMOIST, TConfig.c.CLIMATE_COLD_MAXMOIST), 1),
   SNOWY(Range.between(TConfig.c.CLIMATE_SNOWY_MINTEMP, TConfig.c.CLIMATE_SNOWY_MAXTEMP), Range.between(TConfig.c.CLIMATE_SNOWY_MINMOIST, TConfig.c.CLIMATE_SNOWY_MAXMOIST), 2),
   TRANSITION(Range.between(-4.0D, 4.0D), Range.between(-4.0D, 4.0D), 0);

   final Range<Double> temperatureRange;
   final Range<Double> moistureRange;
   final int priority;

   private BiomeClimate(Range<Double> param3, Range<Double> param4, int param5) {
      this.temperatureRange = temperatureRange;
      this.moistureRange = moistureRange;
      this.priority = priority;
   }

   private static boolean isInRange(double val, @NotNull Range<Double> r) {
      return (Double)r.getMaximum() >= val && (Double)r.getMinimum() <= val;
   }

   @NotNull
   public static BiomeClimate selectClimate(double temp, double moist) {
      BiomeClimate candidate = TRANSITION;
      BiomeClimate[] var5 = values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         BiomeClimate climate = var5[var7];
         if (isInRange(temp, climate.getTemperatureRange()) && isInRange(moist, climate.getMoistureRange()) && candidate.priority < climate.priority) {
            candidate = climate;
         }
      }

      return candidate;
   }

   public Range<Double> getTemperatureRange() {
      return this.temperatureRange;
   }

   public Range<Double> getMoistureRange() {
      return this.moistureRange;
   }

   // $FF: synthetic method
   private static BiomeClimate[] $values() {
      return new BiomeClimate[]{HUMID_VEGETATION, DRY_VEGETATION, HOT_BARREN, COLD, SNOWY, TRANSITION};
   }
}
