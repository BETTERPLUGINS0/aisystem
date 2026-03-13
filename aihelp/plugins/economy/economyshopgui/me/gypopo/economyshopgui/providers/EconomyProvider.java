package me.gypopo.economyshopgui.providers;

import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.exceptions.EconomyLoadException;
import org.bukkit.OfflinePlayer;

public interface EconomyProvider {
   void setup(EconomyShopGUI var1) throws EconomyLoadException;

   double getBalance(OfflinePlayer var1);

   void depositBalance(OfflinePlayer var1, double var2);

   void withdrawBalance(OfflinePlayer var1, double var2);

   EcoType getType();

   String getPlural();

   String getSingular();

   String getFriendly();

   boolean isDecimal();

   String formatPrice(double var1);
}
