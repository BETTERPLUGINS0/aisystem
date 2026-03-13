package me.gypopo.economyshopgui.providers.priceModifiers.seasons;

import java.util.Objects;
import me.gypopo.economyshopgui.providers.priceModifiers.PriceModifier;
import org.bukkit.entity.Player;

public class SeasonModifier extends PriceModifier {
   private final int modifier;
   private final String world;

   public SeasonModifier(String world, int modifier) {
      this.world = world;
      this.modifier = modifier;
   }

   public String getWorld() {
      return this.world;
   }

   public double modify(Player player, double original) {
      return !player.getWorld().getName().equals(this.world) ? original : original + original / 100.0D * (double)this.modifier;
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (object != null && this.getClass() == object.getClass()) {
         SeasonModifier modifier = (SeasonModifier)object;
         return this.modifier == modifier.modifier && this.world.equals(modifier.world);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.modifier, this.world});
   }
}
