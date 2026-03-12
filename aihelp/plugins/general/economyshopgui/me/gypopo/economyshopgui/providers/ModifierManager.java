package me.gypopo.economyshopgui.providers;

import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.priceModifiers.seasons.SeasonProvider;
import me.gypopo.economyshopgui.util.exceptions.ModifierLoadException;

public class ModifierManager {
   private final EconomyShopGUI plugin;
   private static SeasonProvider seasonProvider;

   public ModifierManager(EconomyShopGUI plugin) {
      this.plugin = plugin;
      this.loadModifiers();
   }

   private void loadModifiers() {
      this.loadSeasonModifier();
   }

   private void loadSeasonModifier() {
      if (ConfigManager.getConfig().getBoolean("enable-seasonal-pricing", false)) {
         try {
            SendMessage.infoMessage("Loading season modifiers...");
            seasonProvider = new SeasonProvider(this.plugin);
         } catch (ModifierLoadException var2) {
            SendMessage.errorMessage("Failed to load SeasonModifiers from config for reason: " + var2.getMessage());
         }
      }

   }

   public boolean hasSeasonalPrices() {
      return seasonProvider != null;
   }

   public void reloadModifiers() {
      if (this.hasSeasonalPrices()) {
         seasonProvider.reloadModifiers();
      } else {
         this.loadSeasonModifier();
      }

   }
}
