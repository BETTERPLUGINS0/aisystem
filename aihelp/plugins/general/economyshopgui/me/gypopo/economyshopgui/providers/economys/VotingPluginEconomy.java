package me.gypopo.economyshopgui.providers.economys;

import com.bencodez.votingplugin.VotingPluginMain;
import com.bencodez.votingplugin.user.VotingPluginUser;
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

public class VotingPluginEconomy implements EconomyProvider {
   private String friendly;
   private String singular;
   private String plural;
   private final boolean decimal = false;
   private PriceFormatter.Format priceFormat;

   public void setup(EconomyShopGUI plugin) throws EconomyLoadException {
      if (plugin.getServer().getPluginManager().getPlugin("VotingPlugin") != null) {
         SendMessage.infoMessage("Successfully hooked into VotingPlugin");
         this.formatSingular();
         this.formatPlural();
         this.friendly = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.voting-plugin.friendly", "Points"));
         this.priceFormat = PriceFormatter.getFormatter("voting-plugin", (String)null);
      } else {
         throw new EconomyLoadException("Could not find VotingPlugin");
      }
   }

   public double getBalance(OfflinePlayer p) {
      VotingPluginUser user = VotingPluginMain.plugin.getUser(p.getUniqueId());
      return (double)user.getPoints();
   }

   public void depositBalance(OfflinePlayer p, double amount) {
      VotingPluginUser user = VotingPluginMain.plugin.getUser(p.getUniqueId());
      user.setPoints((int)this.getBalance(p) + (int)Math.round(amount));
   }

   public void withdrawBalance(OfflinePlayer p, double amount) {
      VotingPluginUser user = VotingPluginMain.plugin.getUser(p.getUniqueId());
      user.removePoints((int)Math.round(amount));
   }

   public EcoType getType() {
      return new EcoType(EconomyType.VOTING_PLUGIN);
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
      String singular = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.voting-plugin.singular", "Point"));
      if (this.isSymbol(singular)) {
         this.singular = Lang.SYMBOL_PRICING_FORMAT.get().replace("%symbol%", singular).getLegacy();
      } else {
         this.singular = Lang.NAMED_PRICING_FORMAT.get().replace("%currency-format%", singular).getLegacy();
      }

   }

   private void formatPlural() {
      String plural = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.voting-plugin.plural", "Points"));
      if (this.isSymbol(plural)) {
         this.plural = Lang.SYMBOL_PRICING_FORMAT.get().replace("%symbol%", plural).getLegacy();
      } else {
         this.plural = Lang.NAMED_PRICING_FORMAT.get().replace("%currency-format%", plural).getLegacy();
      }

   }

   private boolean isSymbol(String format) {
      return format.chars().count() == 1L;
   }
}
