package me.gypopo.economyshopgui.objects.shops;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.objects.DisplayItem;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.ShopPageItems;
import me.gypopo.economyshopgui.objects.mappings.ClickAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface ShopSection {
   String getSection();

   Collection<DisplayItem> getDisplayItems();

   DisplayItem getDisplayItem(String var1);

   boolean isActionItem(String var1, String var2);

   Collection<ShopItem> getShopItems();

   ShopItem getShopItem(String var1);

   ShopPageItems getShopPageItems(int var1);

   int getPages();

   boolean isSubSection();

   boolean isHidden();

   boolean isCloseMenu();

   ShopType getType();

   List<String> getItemLocs();

   List<String> getShopItemLocs();

   ClickAction getClickAction(ClickType var1);

   int getPageForShopItem(String var1);

   void openShopSection(Player var1, boolean var2);

   void openShopSection(Player var1, boolean var2, String var3);

   void openShopSection(Player var1, int var2, boolean var3);

   void openShopSection(Player var1, int var2, boolean var3, @Nullable String var4);

   void reloadItem(String var1);

   ItemStack updateItem(Player var1, ShopItem var2);
}
