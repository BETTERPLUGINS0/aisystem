package me.gypopo.economyshopgui;

import java.util.List;
import java.util.Optional;
import me.gypopo.economyshopgui.util.PotionTypes;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface VersionHandler {
   Object emptyList();

   String getLoreAsNBT(ItemStack var1);

   String toNBT(String var1);

   Object getNMSLore(ItemStack var1);

   ItemStack applyNMSLore(ItemStack var1, Object var2);

   void setItemLore(Object var1, String var2, int var3);

   void addItemLore(Object var1, String var2, int var3);

   void addItemLore(Object var1, String var2);

   ItemStack setNBTString(ItemStack var1, String var2, String var3);

   String getNBTString(ItemStack var1, String var2);

   ItemStack setNBTInt(ItemStack var1, String var2, Integer var3);

   Integer getNBTInt(ItemStack var1, String var2);

   boolean isSimilar(ItemStack var1, ItemStack var2, List<String> var3);

   ItemStack setPages(ItemStack var1, String var2);

   String getPages(ItemStack var1);

   ItemStack setDisabledBackButton(ItemStack var1);

   boolean isDisabledBackButton(ItemStack var1);

   double getBuyPrice(ItemStack var1);

   double getSellPrice(ItemStack var1);

   ItemStack addNBTPrices(ItemStack var1, String var2);

   ItemStack setSpawnerType(ItemStack var1, String var2);

   ItemStack setPathToItem(ItemStack var1, String var2);

   ItemStack setItemErrorToItem(ItemStack var1);

   ItemStack setPotionType(ItemStack var1, PotionTypes var2);

   Optional<PotionTypes> getBasePotion(ItemStack var1);

   ItemStack getSpawnerToGive(EntityType var1);

   String getPathToItem(ItemStack var1);

   Boolean hasItemError(ItemStack var1);

   String getSpawnerType(ItemStack var1);

   ItemStack getItemInHand(Player var1);

   String getTitle(Inventory var1);
}
