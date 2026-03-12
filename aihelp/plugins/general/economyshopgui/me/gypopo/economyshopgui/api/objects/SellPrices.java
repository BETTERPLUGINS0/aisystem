package me.gypopo.economyshopgui.api.objects;

import java.util.Map;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.EcoType;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class SellPrices {
   private final OfflinePlayer player;
   private final Map<ShopItem, Integer> items;
   private final Map<EcoType, Double> prices;

   public SellPrices(OfflinePlayer player, Map<ShopItem, Integer> items, Map<EcoType, Double> prices) {
      this.player = player;
      this.items = items;
      this.prices = prices;
   }

   public OfflinePlayer getPlayer() {
      return this.player;
   }

   public Map<ShopItem, Integer> getItems() {
      return this.items;
   }

   public Map<EcoType, Double> getPrices() {
      return this.prices;
   }

   public double getPrice(@NotNull EcoType ecoType) {
      return (Double)this.prices.getOrDefault(ecoType, -1.0D);
   }

   public boolean isEmpty() {
      return this.items.isEmpty();
   }

   public SellPrices updateLimits() {
      return this;
   }
}
