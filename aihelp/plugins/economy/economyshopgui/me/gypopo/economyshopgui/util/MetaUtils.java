package me.gypopo.economyshopgui.util;

import java.util.List;
import java.util.Map;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.objects.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.ItemMeta;

public interface MetaUtils {
   void update();

   void send(Player var1, Translatable var2);

   void send(Object var1, Translatable var2);

   void send(Translatable var1);

   void sendLegacy(Player var1, String var2);

   void sendLegacy(Object var1, String var2);

   void sendLegacy(String var1);

   void sendTransactionMessage(Player var1, int var2, double var3, ShopItem var5, Transaction.Mode var6, Transaction.Type var7);

   void sendTransactionMessage(Player var1, int var2, Map<EcoType, Double> var3, Map<ShopItem, Integer> var4, Transaction.Type var5);

   Inventory createInventory(InventoryHolder var1, int var2, Translatable var3);

   void setRawDisplayName(ItemMeta var1, String var2);

   void setDisplayName(ItemMeta var1, Translatable var2);

   String getRawDisplayName(ItemMeta var1);

   Translatable getDisplayName(ItemMeta var1);

   void setRawLore(ItemMeta var1, List<String> var2);

   List<String> getRawLore(ItemMeta var1);

   void setLore(ItemMeta var1, List<Translatable> var2);

   void addLore(ItemMeta var1, Translatable var2);

   List<Translatable> getLore(ItemMeta var1);

   void broadcast(Translatable var1);

   void logDevDebugMessage(Translatable var1);

   void logPlayerTransaction(Translatable var1);

   void logPlayerTransaction(String var1);

   void infoMessage(Translatable var1);

   void logDebugMessage(Translatable var1);

   void warnMessage(Translatable var1);

   void errorMessage(Translatable var1);

   void infoLegacyMessage(String var1);

   void logLegacyDebugMessage(String var1);

   void warnLegacyMessage(String var1);

   void errorLegacyMessage(String var1);

   void infoMessage(Object var1, Translatable var2);

   void warnMessage(Object var1, Translatable var2);

   void errorMessage(Object var1, Translatable var2);

   void infoLegacyMessage(Object var1, String var2);

   void warnLegacyMessage(Object var1, String var2);

   void errorLegacyMessage(Object var1, String var2);
}
