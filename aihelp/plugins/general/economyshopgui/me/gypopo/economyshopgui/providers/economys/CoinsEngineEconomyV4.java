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
import org.bukkit.OfflinePlayer;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

public class CoinsEngineEconomyV4 implements EconomyProvider {
   private String currency;
   private Currency cur;
   private String friendly;
   private String singular;
   private String plural;
   private boolean decimal;
   private boolean wait = false;
   private PriceFormatter.Format priceFormat;

   public CoinsEngineEconomyV4(String currency) {
      this.currency = currency;
   }

   public void setup(EconomyShopGUI plugin) throws EconomyLoadException {
      if (!plugin.getServer().getPluginManager().isPluginEnabled("CoinsEngine")) {
         throw new EconomyLoadException("Could not find CoinsEngine");
      } else {
         this.registerCurrency();
      }
   }

   private void registerCurrency() throws EconomyLoadException {
      String key;
      if (this.currency != null && !this.currency.isEmpty()) {
         this.cur = CoinsEngineAPI.getCurrency(this.currency);
         if (this.cur == null) {
            throw new EconomyLoadException("Failed to register a CoinsEngine currency named as '" + this.currency + "'");
         }

         key = this.currency;
         SendMessage.infoMessage("Successfully registered currency '" + this.currency + "' from CoinsEngine");
      } else {
         this.cur = (Currency)CoinsEngineAPI.getCurrencies().stream().findFirst().orElse((Object)null);
         if (this.cur == null) {
            throw new EconomyLoadException("Failed to find default currency from CoinsEngine");
         }

         key = this.cur.getName();
         SendMessage.infoMessage("Successfully hooked into CoinsEngine and using default currency: " + this.cur.getName());
      }

      this.formatSingular(key);
      this.formatPlural(key);
      this.formatFriendly(key);
      this.decimal = this.cur.isDecimal();
      this.priceFormat = PriceFormatter.getFormatter("coins-engine", key);
   }

   public double getBalance(OfflinePlayer p) {
      return CoinsEngineAPI.getBalance(p.getUniqueId(), this.cur);
   }

   public void depositBalance(OfflinePlayer p, double amount) {
      CoinsEngineAPI.addBalance(p.getUniqueId(), this.cur, amount);
   }

   public void withdrawBalance(OfflinePlayer p, double amount) {
      CoinsEngineAPI.removeBalance(p.getUniqueId(), this.cur, amount);
   }

   public EcoType getType() {
      return new EcoType(EconomyType.COINS_ENGINE, this.currency);
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

   private void formatSingular(String key) {
      String singular = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.coins-engine:" + key + ".singular", ConfigManager.getConfig().getString("currency-formats.coins-engine.singular", this.cur.getSymbol())));
      String format = ChatUtil.formatColors(this.cur.getFormat().replace("%amount%", !singular.contains("%price%") ? "%price%" : "").replace("%currency_symbol%", singular));
      if (!singular.isEmpty()) {
         if (this.isSymbol(singular)) {
            this.singular = format.isEmpty() ? Lang.SYMBOL_PRICING_FORMAT.get().getLegacy().replace("%symbol%", singular) : format;
         } else {
            this.singular = format.isEmpty() ? Lang.NAMED_PRICING_FORMAT.get().getLegacy().replace("%currency-format%", singular) : format;
         }
      } else {
         this.singular = format.isEmpty() ? Lang.SYMBOL_PRICING_FORMAT.get().getLegacy().replace("%symbol%", Lang.CURRENCYSYMBOL.get().getLegacy()) : format;
      }

   }

   private void formatPlural(String key) {
      String plural = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.coins-engine:" + key + ".plural", ConfigManager.getConfig().getString("currency-formats.coins-engine.plural", this.cur.getSymbol())));
      String format = ChatUtil.formatColors(this.cur.getFormat().replace("%amount%", !plural.contains("%price%") ? "%price%" : "").replace("%currency_symbol%", plural));
      if (!plural.isEmpty()) {
         if (this.isSymbol(plural)) {
            this.plural = format.isEmpty() ? Lang.SYMBOL_PRICING_FORMAT.get().getLegacy().replace("%symbol%", plural) : format;
         } else {
            this.plural = format.isEmpty() ? Lang.NAMED_PRICING_FORMAT.get().getLegacy().replace("%currency-format%", plural) : format;
         }
      } else {
         this.plural = format.isEmpty() ? Lang.SYMBOL_PRICING_FORMAT.get().getLegacy().replace("%symbol%", Lang.CURRENCYSYMBOL.get().getLegacy()) : format;
      }

   }

   private void formatFriendly(String key) {
      this.friendly = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.coins-engine:" + key + ".friendly", ConfigManager.getConfig().getString("currency-formats.coins-engine.friendly", this.cur.getName())));
   }

   private boolean isSymbol(String format) {
      return format.chars().count() == 1L;
   }
}
