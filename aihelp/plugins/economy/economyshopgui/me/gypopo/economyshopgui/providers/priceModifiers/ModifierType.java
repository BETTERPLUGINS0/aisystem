package me.gypopo.economyshopgui.providers.priceModifiers;

import me.gypopo.economyshopgui.providers.priceModifiers.seasons.SeasonModifier;

public enum ModifierType {
   SEASON(SeasonModifier.class);

   private final Class<? extends PriceModifier> clazz;

   private ModifierType(Class<? extends PriceModifier> param3) {
      this.clazz = clazz;
   }

   public Class<? extends PriceModifier> getClazz() {
      return this.clazz;
   }

   // $FF: synthetic method
   private static ModifierType[] $values() {
      return new ModifierType[]{SEASON};
   }
}
