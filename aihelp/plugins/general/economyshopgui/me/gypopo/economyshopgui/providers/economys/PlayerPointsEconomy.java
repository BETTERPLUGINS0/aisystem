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
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.OfflinePlayer;

public class PlayerPointsEconomy implements EconomyProvider {
   private PlayerPointsAPI pp;
   private String friendly;
   private String singular;
   private String plural;
   private final boolean decimal = false;
   private PriceFormatter.Format priceFormat;

   public void setup(EconomyShopGUI plugin) throws EconomyLoadException {
      if (plugin.getServer().getPluginManager().isPluginEnabled("PlayerPoints")) {
         this.pp = PlayerPoints.getInstance().getAPI();
         if (this.pp == null) {
            throw new EconomyLoadException("Failed to hook into PlayerPoints economy");
         } else {
            SendMessage.infoMessage("Successfully hooked into PlayerPoints");
            this.formatSingular();
            this.formatPlural();
            this.friendly = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.player-points.friendly", "Points"));
            this.priceFormat = PriceFormatter.getFormatter("player-points", (String)null);
         }
      } else {
         throw new EconomyLoadException("Could not find PlayerPoints");
      }
   }

   public double getBalance(OfflinePlayer p) {
      return (double)this.pp.look(p.getUniqueId());
   }

   public void depositBalance(OfflinePlayer p, double amount) {
      this.pp.give(p.getUniqueId(), (int)Math.round(amount));
   }

   public void withdrawBalance(OfflinePlayer p, double amount) {
      this.pp.take(p.getUniqueId(), (int)Math.round(amount));
   }

   public EcoType getType() {
      return new EcoType(EconomyType.PLAYER_POINTS);
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
      String singular = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.player-points.singular", "Point"));
      if (this.isSymbol(singular)) {
         this.singular = Lang.SYMBOL_PRICING_FORMAT.get().replace("%symbol%", singular).getLegacy();
      } else {
         this.singular = Lang.NAMED_PRICING_FORMAT.get().replace("%currency-format%", singular).getLegacy();
      }

   }

   private void formatPlural() {
      String plural = ChatUtil.formatColors(ConfigManager.getConfig().getString("currency-formats.player-points.plural", "Points"));
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
