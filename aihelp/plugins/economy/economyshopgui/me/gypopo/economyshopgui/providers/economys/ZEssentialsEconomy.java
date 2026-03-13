package me.gypopo.economyshopgui.providers.economys;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.EconomyProvider;
import me.gypopo.economyshopgui.providers.economys.formatter.PriceFormatter;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.EconomyType;
import me.gypopo.economyshopgui.util.exceptions.EconomyLoadException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class ZEssentialsEconomy implements EconomyProvider {
   private EconomyManager manager;
   private Economy cur;
   private String currency;
   private String friendly;
   private String singular;
   private String plural;
   private final boolean decimal = true;
   private boolean wait = false;
   private PriceFormatter.Format priceFormat;

   public ZEssentialsEconomy(String currency) {
      this.currency = currency;
   }

   public void setup(EconomyShopGUI plugin) throws EconomyLoadException {
      if (plugin.getServer().getPluginManager().isPluginEnabled("zEssentials")) {
         EssentialsPlugin zess = (EssentialsPlugin)Bukkit.getServer().getPluginManager().getPlugin("zEssentials");
         if (zess != null) {
            this.registerCurrency(zess.getEconomyManager());
         } else {
            throw new EconomyLoadException("Failed to hook into zEssentials");
         }
      } else {
         throw new EconomyLoadException("Could not find zEssentials");
      }
   }

   private void registerCurrency(EconomyManager manager) throws EconomyLoadException {
      String key;
      if (this.currency != null && !this.currency.isEmpty()) {
         Optional<Economy> cur = manager.getEconomy(this.currency);
         if (!cur.isPresent()) {
            throw new EconomyLoadException("Failed to register a zEssentials currency named as '" + this.currency + "'");
         }

         this.cur = (Economy)cur.get();
         key = this.currency;
         SendMessage.infoMessage("Successfully registered currency '" + this.currency + "' from zEssentials");
      } else {
         try {
            this.cur = manager.getDefaultEconomy();
            key = this.cur.getName();
            SendMessage.infoMessage("Successfully hooked into zEssentials and using default currency: " + this.cur.getName() + "(" + this.cur.getSymbol() + ")");
         } catch (IndexOutOfBoundsException var4) {
            throw new EconomyLoadException("Failed to find a currency in zEssentials");
         }
      }

      this.manager = manager;
      this.formatSingular(key);
      this.formatPlural(key);
      this.formatFriendly(key);
      this.priceFormat = PriceFormatter.getFormatter("zEssentials", key);
   }

   public double getBalance(OfflinePlayer p) {
      return this.manager.getBalance(p, this.cur).doubleValue();
   }

   public void depositBalance(OfflinePlayer p, double amount) {
      this.manager.deposit(p.getUniqueId(), this.cur, BigDecimal.valueOf(amount));
   }

   public void withdrawBalance(OfflinePlayer p, double amount) {
      this.manager.withdraw(p.getUniqueId(), this.cur, BigDecimal.valueOf(amount));
   }

   public EcoType getType() {
      return new EcoType(EconomyType.ZESSENTIALS, this.currency);
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

   private void formatSingular(String key) {
      String singular = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.zEssentials:" + key + ".singular", ConfigManager.getConfig().getString("currency-formats.zEssentials.singular", this.cur.getSymbol())));
      if (this.isSymbol(singular)) {
         this.singular = this.cur.getFormat().replace("%symbol%", singular);
      } else {
         this.singular = this.cur.getFormat().replace("%currency-format%", singular);
      }

   }

   private void formatPlural(String key) {
      String plural = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.zEssentials:" + key + ".plural", ConfigManager.getConfig().getString("currency-formats.zEssentials.plural", this.cur.getSymbol())));
      if (this.isSymbol(plural)) {
         this.plural = this.cur.getFormat().replace("%symbol%", plural);
      } else {
         this.plural = this.cur.getFormat().replace("%currency-format%", plural);
      }

   }

   private void formatFriendly(String key) {
      this.friendly = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.zEssentials:" + key + ".friendly", ConfigManager.getConfig().getString("currency-formats.zEssentials.friendly", this.cur.getSymbol())));
   }

   private boolean isSymbol(String format) {
      return format.chars().count() == 1L;
   }
}
