package me.gypopo.economyshopgui.providers.economys;

import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.EconomyProvider;
import me.gypopo.economyshopgui.providers.economys.formatter.PriceFormatter;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.EconomyType;
import me.gypopo.economyshopgui.util.exceptions.EconomyLoadException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEconomy implements EconomyProvider {
   private Economy economy;
   private String friendly;
   private String singular;
   private String plural;
   private boolean decimal;
   private PriceFormatter.Format priceFormat;

   public void setup(EconomyShopGUI plugin) throws EconomyLoadException {
      if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
         throw new EconomyLoadException("Could not find Vault");
      } else {
         RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
         if (rsp == null) {
            throw new EconomyLoadException("No supported economy provider for Vault detected, please install a plugin such as Essentials");
         } else {
            this.economy = (Economy)rsp.getProvider();
            this.formatSingular();
            this.formatPlural();
            this.decimal = this.economy.fractionalDigits() == -1 || this.economy.fractionalDigits() > 0;
            this.friendly = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.vault.friendly", "money"));
            this.priceFormat = PriceFormatter.getFormatter("vault", (String)null);
            SendMessage.infoMessage("Successfully hooked into Vault and using " + this.economy.getName() + " as economy provider");
         }
      }
   }

   public double getBalance(OfflinePlayer p) {
      return this.economy.getBalance(p);
   }

   public void depositBalance(OfflinePlayer p, double amount) {
      this.economy.depositPlayer(p, amount);
   }

   public void withdrawBalance(OfflinePlayer p, double amount) {
      this.economy.withdrawPlayer(p, amount);
   }

   public EcoType getType() {
      return new EcoType(EconomyType.VAULT);
   }

   public String getPlural() {
      return this.plural;
   }

   public String getSingular() {
      return this.singular;
   }

   public String getFriendly() {
      return this.friendly;
   }

   public boolean isDecimal() {
      return this.decimal;
   }

   public String formatPrice(double price) {
      return this.priceFormat.format(this, price);
   }

   private void formatSingular() {
      String singular = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.vault.singular", this.economy.currencyNameSingular()));
      if (!singular.isEmpty()) {
         if (!singular.contains("%currency-symbol%")) {
            if (this.isSymbol(singular)) {
               this.singular = Lang.SYMBOL_PRICING_FORMAT.get().replace("%symbol%", singular).getLegacy();
            } else {
               this.singular = Lang.NAMED_PRICING_FORMAT.get().replace("%currency-format%", singular).getLegacy();
            }
         } else {
            this.singular = Lang.CURRENCYSYMBOL.get().getLegacy();
         }
      } else {
         this.singular = Lang.CURRENCYSYMBOL.get().getLegacy();
      }

   }

   private void formatPlural() {
      String plural = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.vault.plural", this.economy.currencyNamePlural()));
      if (!plural.isEmpty()) {
         if (!plural.contains("%currency-symbol%")) {
            if (this.isSymbol(plural)) {
               this.plural = Lang.SYMBOL_PRICING_FORMAT.get().replace("%symbol%", plural).getLegacy();
            } else {
               this.plural = Lang.NAMED_PRICING_FORMAT.get().replace("%currency-format%", plural).getLegacy();
            }
         } else {
            this.plural = Lang.CURRENCYSYMBOL.get().getLegacy();
         }
      } else {
         this.plural = Lang.CURRENCYSYMBOL.get().getLegacy();
      }

   }

   private boolean isSymbol(String format) {
      return format.chars().count() == 1L;
   }
}
