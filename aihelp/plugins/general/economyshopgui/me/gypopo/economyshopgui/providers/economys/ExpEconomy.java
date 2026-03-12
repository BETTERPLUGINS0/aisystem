package me.gypopo.economyshopgui.providers.economys;

import java.util.Objects;
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
import org.bukkit.entity.Player;

public class ExpEconomy implements EconomyProvider {
   private String friendly;
   private String singular;
   private String plural;
   private final boolean decimal = false;
   private PriceFormatter.Format priceFormat;

   public void setup(EconomyShopGUI plugin) throws EconomyLoadException {
      this.formatSingular();
      this.formatPlural();
      this.friendly = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.exp.friendly", Lang.XP_CURRENCY_NAME_PLURAL.get().getLegacy()).replace("%translations-exp-currency-name-plural%", Lang.XP_CURRENCY_NAME_PLURAL.get().getLegacy()));
      this.priceFormat = PriceFormatter.getFormatter("exp", (String)null);
      SendMessage.infoMessage("Successfully enabled EXP points economy");
   }

   public double getBalance(OfflinePlayer p) {
      return (double)this.getTotalExperience((Player)p);
   }

   public void depositBalance(OfflinePlayer p, double amount) {
      this.setTotalExperience((Player)p, this.getTotalExperience((Player)p) + (int)Math.round(amount));
   }

   public void withdrawBalance(OfflinePlayer p, double amount) {
      this.setTotalExperience((Player)p, this.getTotalExperience((Player)p) - (int)Math.round(amount));
   }

   public EcoType getType() {
      return new EcoType(EconomyType.EXP);
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
      return false;
   }

   public String formatPrice(double price) {
      return this.priceFormat.format(this, price);
   }

   private void formatSingular() {
      String singular = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.exp.singular", Lang.XP_CURRENCY_NAME_SINGULAR.get().getLegacy()).replace("%translations-exp-currency-name-singular%", Lang.XP_CURRENCY_NAME_SINGULAR.get().getLegacy()));
      if (!singular.isEmpty()) {
         if (this.isSymbol(singular)) {
            this.singular = Lang.SYMBOL_PRICING_FORMAT.get().getLegacy().replace("%symbol%", singular);
         } else {
            this.singular = Lang.NAMED_PRICING_FORMAT.get().getLegacy().replace("%currency-format%", singular);
         }
      } else {
         this.singular = Lang.CURRENCYSYMBOL.get().getLegacy();
      }

   }

   private void formatPlural() {
      String plural = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.exp.plural", Lang.XP_CURRENCY_NAME_PLURAL.get().getLegacy()).replace("%translations-exp-currency-name-plural%", Lang.XP_CURRENCY_NAME_PLURAL.get().getLegacy()));
      if (!plural.isEmpty()) {
         if (this.isSymbol(plural)) {
            this.plural = Lang.SYMBOL_PRICING_FORMAT.get().getLegacy().replace("%symbol%", plural);
         } else {
            this.plural = Lang.NAMED_PRICING_FORMAT.get().getLegacy().replace("%currency-format%", plural);
         }
      } else {
         this.plural = Lang.CURRENCYSYMBOL.get().getLegacy();
      }

   }

   private boolean isSymbol(String format) {
      return format.chars().count() == 1L;
   }

   private void setTotalExperience(Player player, int exp) {
      player.setExp(0.0F);
      player.setLevel(0);
      player.setTotalExperience(0);
      int amount = exp;

      while(amount > 0) {
         int expToLevel = this.getExpAtLevel(player);
         amount -= expToLevel;
         if (amount >= 0) {
            player.giveExp(expToLevel);
         } else {
            amount += expToLevel;
            player.giveExp(amount);
            amount = 0;
         }
      }

   }

   private int getExpAtLevel(Player player) {
      return this.getExpAtLevel(player.getLevel());
   }

   private int getExpAtLevel(int level) {
      if (level <= 15) {
         return 2 * level + 7;
      } else {
         return level >= 16 && level <= 30 ? 5 * level - 38 : 9 * level - 158;
      }
   }

   private int getExpToLevel(int level) {
      int currentLevel = 0;

      int exp;
      for(exp = 0; currentLevel < level; ++currentLevel) {
         exp += this.getExpAtLevel(currentLevel);
      }

      if (exp < 0) {
         exp = Integer.MAX_VALUE;
      }

      return exp;
   }

   private int getTotalExperience(Player player) {
      int exp = Math.round((float)this.getExpAtLevel(player) * player.getExp());

      for(int currentLevel = player.getLevel(); currentLevel > 0; exp += this.getExpAtLevel(currentLevel)) {
         --currentLevel;
      }

      if (exp < 0) {
         exp = Integer.MAX_VALUE;
      }

      return exp;
   }

   private int getExpUntilNextLevel(Player player) {
      int exp = Math.round((float)this.getExpAtLevel(player) * player.getExp());
      int nextLevel = player.getLevel();
      return this.getExpAtLevel(nextLevel) - exp;
   }
}
