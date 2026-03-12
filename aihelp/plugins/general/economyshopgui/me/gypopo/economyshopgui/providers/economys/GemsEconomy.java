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
import me.xanium.gemseconomy.api.GemsEconomyAPI;
import me.xanium.gemseconomy.currency.Currency;
import org.bukkit.OfflinePlayer;

public class GemsEconomy implements EconomyProvider {
   private GemsEconomyAPI gems;
   private String currency;
   private Currency cur;
   private String friendly;
   private String singular;
   private String plural;
   private boolean decimal;
   private PriceFormatter.Format priceFormat;

   public GemsEconomy(String currency) {
      this.currency = currency;
   }

   public void setup(EconomyShopGUI plugin) throws EconomyLoadException {
      if (plugin.getServer().getPluginManager().isPluginEnabled("GemsEconomy")) {
         this.gems = new GemsEconomyAPI();
         if (this.gems != null) {
            this.registerCurrency();
         } else {
            throw new EconomyLoadException("Failed to hook into GemsEconomy");
         }
      } else {
         throw new EconomyLoadException("Could not find GemsEconomy");
      }
   }

   private void registerCurrency() throws EconomyLoadException {
      if (this.currency != null && !this.currency.isEmpty()) {
         this.cur = this.gems.getCurrency(this.currency);
         if (this.cur == null) {
            throw new EconomyLoadException("Failed to register a GemsEconomy currency named as '" + this.currency + "'");
         }

         SendMessage.infoMessage("Successfully registered currency '" + this.currency + "' from GemsEconomy");
      } else {
         this.cur = this.gems.plugin.getCurrencyManager().getDefaultCurrency();
         if (this.cur == null) {
            throw new EconomyLoadException("Failed to find default currency from GemsEconomy");
         }

         this.currency = this.cur.getSingular();
         SendMessage.infoMessage("Successfully hooked into GemsEconomy and using default currency: " + this.currency + "(" + this.cur.getPlural() + ")");
      }

      this.formatSingular();
      this.formatPlural();
      this.formatFriendly();
      this.priceFormat = PriceFormatter.getFormatter("gems-economy", this.currency);
      this.decimal = this.cur.isDecimalSupported();
   }

   public double getBalance(OfflinePlayer p) {
      return this.gems.getBalance(p.getUniqueId(), this.cur);
   }

   public void depositBalance(OfflinePlayer p, double amount) {
      this.gems.deposit(p.getUniqueId(), !this.decimal ? (double)Math.round(amount) : amount, this.cur);
   }

   public void withdrawBalance(OfflinePlayer p, double amount) {
      this.gems.withdraw(p.getUniqueId(), !this.decimal ? (double)Math.round(amount) : amount, this.cur);
   }

   public EcoType getType() {
      return new EcoType(EconomyType.GEMS_ECONOMY, this.currency);
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
      String singular = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.gems-economy:" + this.currency + ".singular", ConfigManager.getConfig().getString("currency-formats.gems-economy.singular", this.cur.getSingular())));
      if (this.isSymbol(singular)) {
         this.singular = Lang.SYMBOL_PRICING_FORMAT.get().replace("%symbol%", singular).getLegacy();
      } else {
         this.singular = Lang.NAMED_PRICING_FORMAT.get().replace("%currency-format%", singular).getLegacy();
      }

   }

   private void formatPlural() {
      String plural = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.gems-economy:" + this.currency + ".plural", ConfigManager.getConfig().getString("currency-formats.gems-economy.plural", this.cur.getPlural())));
      if (this.isSymbol(plural)) {
         this.plural = Lang.SYMBOL_PRICING_FORMAT.get().replace("%symbol%", plural).getLegacy();
      } else {
         this.plural = Lang.NAMED_PRICING_FORMAT.get().replace("%currency-format%", plural).getLegacy();
      }

   }

   private void formatFriendly() {
      this.friendly = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.gems-economy:" + this.currency + ".friendly", ConfigManager.getConfig().getString("currency-formats.gems-economy.friendly", this.cur.getPlural())));
   }

   private boolean isSymbol(String format) {
      return format.chars().count() == 1L;
   }
}
