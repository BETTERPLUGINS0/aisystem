package me.gypopo.economyshopgui.providers.economys;

import java.util.Objects;
import java.util.Optional;
import me.TechsCode.UltraEconomy.UltraEconomyAPI;
import me.TechsCode.UltraEconomy.objects.Account;
import me.TechsCode.UltraEconomy.objects.Currency;
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

public class UltraEconomy implements EconomyProvider {
   private UltraEconomyAPI ue;
   private Currency cur;
   private String currency;
   private String friendly;
   private String singular;
   private String plural;
   private final boolean decimal = true;
   private PriceFormatter.Format priceFormat;

   public UltraEconomy(String currency) {
      this.currency = currency;
   }

   public void setup(EconomyShopGUI plugin) throws EconomyLoadException {
      if (plugin.getServer().getPluginManager().isPluginEnabled("UltraEconomy")) {
         this.ue = me.TechsCode.UltraEconomy.UltraEconomy.getAPI();
         if (this.ue != null) {
            this.registerCurrency();
         } else {
            throw new EconomyLoadException("Failed to hook into UltraEconomy");
         }
      } else {
         throw new EconomyLoadException("Could not find UltraEconomy");
      }
   }

   private void registerCurrency() throws EconomyLoadException {
      if (this.currency != null && !this.currency.isEmpty()) {
         Optional<Currency> cur = this.ue.getCurrencies().name(this.currency);
         if (!cur.isPresent()) {
            throw new EconomyLoadException("Failed to register a UltraEconomy currency named as '" + this.currency + "'");
         }

         this.cur = (Currency)cur.get();
         SendMessage.infoMessage("Successfully registered currency '" + this.currency + "' from UltraEconomy");
      } else {
         try {
            this.cur = (Currency)this.ue.getCurrencies().get(0);
            this.currency = this.cur.getKey();
            SendMessage.infoMessage("Successfully hooked into UltraEconomy and using default currency: " + this.currency + "(" + this.cur.getName() + ")");
         } catch (IndexOutOfBoundsException var2) {
            throw new EconomyLoadException("Failed to find a currency in UltraEconomy");
         }
      }

      this.formatSingular();
      this.formatPlural();
      this.formatFriendly();
      this.priceFormat = PriceFormatter.getFormatter("ultra-economy", this.currency);
   }

   public double getBalance(OfflinePlayer p) {
      Optional<Account> acc = this.ue.getAccounts().uuid(p.getUniqueId());
      if (acc.isPresent()) {
         return ((Account)acc.get()).getBalance(this.cur).getOnHand();
      } else {
         SendMessage.warnMessage("UltraEconomy Failed to retrieve the account from player '" + p.getName() + "'");
         return 0.0D;
      }
   }

   public void depositBalance(OfflinePlayer p, double amount) {
      Optional<Account> acc = this.ue.getAccounts().uuid(p.getUniqueId());
      if (acc.isPresent()) {
         ((Account)acc.get()).getBalance(this.cur).addHand((double)((float)amount));
      } else {
         SendMessage.warnMessage("UltraEconomy Failed to retrieve the account from player '" + p.getName() + "'");
      }

   }

   public void withdrawBalance(OfflinePlayer p, double amount) {
      Optional<Account> acc = this.ue.getAccounts().uuid(p.getUniqueId());
      if (acc.isPresent()) {
         ((Account)acc.get()).getBalance(this.cur).removeHand((double)((float)amount));
      } else {
         SendMessage.warnMessage("UltraEconomy Failed to retrieve the account from player '" + p.getName() + "'");
      }

   }

   public EcoType getType() {
      return new EcoType(EconomyType.ULTRA_ECONOMY, this.currency);
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
      Objects.requireNonNull(this);
      return true;
   }

   public String formatPrice(double price) {
      return this.priceFormat.format(this, price);
   }

   private void formatSingular() {
      String singular = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.ultra-economy:" + this.currency + ".singular", ConfigManager.getConfig().getString("currency-formats.ultra-economy.singular", this.cur.getFormat().getSingularFormat().replace("{Amount} ", "").replace(" {Amount}", "").replace("{Amount}", ""))));
      if (this.isSymbol(singular)) {
         this.singular = Lang.SYMBOL_PRICING_FORMAT.get().replace("%symbol%", singular).getLegacy();
      } else {
         this.singular = Lang.NAMED_PRICING_FORMAT.get().replace("%currency-format%", singular).getLegacy();
      }

   }

   private void formatPlural() {
      String plural = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.ultra-economy:" + this.currency + ".plural", ConfigManager.getConfig().getString("currency-formats.ultra-economy.plural", this.cur.getFormat().getPluralFormat().replace("{Amount} ", "").replace(" {Amount}", "").replace("{Amount}", ""))));
      if (this.isSymbol(plural)) {
         this.plural = Lang.SYMBOL_PRICING_FORMAT.get().replace("%symbol%", plural).getLegacy();
      } else {
         this.plural = Lang.NAMED_PRICING_FORMAT.get().replace("%currency-format%", plural).getLegacy();
      }

   }

   private void formatFriendly() {
      this.friendly = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.ultra-economy:" + this.currency + ".friendly", ConfigManager.getConfig().getString("currency-formats.ultra-economy.friendly", this.cur.getFormat().getPluralFormat().replace("{Amount} ", "").replace(" {Amount}", "").replace("{Amount}", ""))));
   }

   private boolean isSymbol(String format) {
      return format.chars().count() == 1L;
   }
}
