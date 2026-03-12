package me.gypopo.economyshopgui.providers.priceModifiers;

import java.util.ArrayList;
import java.util.Iterator;
import me.gypopo.economyshopgui.providers.priceModifiers.seasons.SeasonModifier;

public class Modifiers extends ArrayList<PriceModifier> {
   public PriceModifier getByType(ModifierType type) {
      Iterator var2 = this.iterator();

      PriceModifier modifier;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         modifier = (PriceModifier)var2.next();
      } while(modifier.getClass() != type.getClazz());

      return modifier;
   }

   public boolean hasType(ModifierType type) {
      Iterator var2 = this.iterator();

      PriceModifier modifier;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         modifier = (PriceModifier)var2.next();
      } while(modifier.getClass() != type.getClazz());

      return true;
   }

   public boolean removeType(ModifierType type) {
      Iterator var2 = this.iterator();

      PriceModifier modifier;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         modifier = (PriceModifier)var2.next();
      } while(modifier.getClass() != type.getClazz());

      return super.remove(modifier);
   }

   public boolean hasSeasonModifier(String world) {
      Iterator var2 = this.iterator();

      PriceModifier modifier;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         modifier = (PriceModifier)var2.next();
      } while(!(modifier instanceof SeasonModifier) || !((SeasonModifier)modifier).getWorld().equals(world));

      return true;
   }
}
