package me.gypopo.economyshopgui.providers.requirements.items.region;

import me.gypopo.economyshopgui.EconomyShopGUI;
import org.bukkit.entity.Player;

public interface RegionProvider {
   void init(EconomyShopGUI var1, RegionManager var2);

   boolean isReady();

   String getName();

   boolean isLoaded(String var1);

   String getRegionName(String var1);

   boolean isInsideRegion(Player var1, String var2);
}
