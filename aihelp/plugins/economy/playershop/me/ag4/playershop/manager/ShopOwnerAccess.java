package me.ag4.playershop.manager;

import me.ag4.playershop.Utils;
import me.ag4.playershop.api.InventoryAPI;
import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.files.Lang;
import me.ag4.playershop.hooks.ProtocolLibAPI;
import me.ag4.playershop.objects.Shop;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopOwnerAccess {
   public ShopOwnerAccess(Player player, Shop shop) {
      InventoryAPI inventoryAPI = new InventoryAPI(Utils.hex("&8&l" + shop.getUsername() + " &c&lSHOP"), 27);
      if (!shop.getInventory().isEmpty()) {
         Inventory inventory = shop.getInventory();
         inventoryAPI.getInventory().setContents(inventory.getContents());
      }

      PlayerShopAPI.addBuySection(inventoryAPI.getInventory(), shop.getPrice());
      inventoryAPI.onClickInventory((e) -> {
         int slot = e.getSlot();
         ItemStack currentItem = e.getCurrentItem();
         boolean continueExecution = true;
         if (Utils.isTopInventory(e.getRawSlot(), e.getView()) && PlayerShopAPI.buySection(slot)) {
            e.setCancelled(true);
            continueExecution = false;
         }

         if (continueExecution && currentItem == null) {
            continueExecution = false;
         }

         if (continueExecution && !Utils.isTopInventory(e.getRawSlot(), e.getView())) {
            if (Utils.isInventoryFull(e.getInventory())) {
               player.sendMessage(Utils.hex(Lang.GUI_INVENTORY_IS_FULL.toString()));
               e.setCancelled(true);
               continueExecution = false;
            }

            if (continueExecution && PlayerShopAPI.isItemInBlacklist(currentItem)) {
               player.sendMessage(Lang.Error_Item_BlackListed.toString());
               e.setCancelled(true);
               continueExecution = false;
            }

            if (continueExecution) {
               Utils.addItemManually(e.getInventory(), currentItem);
               shop.setInventory(e.getInventory());
               player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 2.0F);
               e.setCurrentItem((ItemStack)null);
            }

            continueExecution = false;
         }

         if (continueExecution && !shop.itemExist(slot)) {
            player.sendMessage(Lang.GUI_Item_Not_Exist_Default.toString());
            e.getInventory().setItem(slot, (ItemStack)null);
            e.setCancelled(true);
            continueExecution = false;
         }

         if (continueExecution && Utils.isInventoryFull(player.getInventory())) {
            player.sendMessage(Utils.hex(Lang.GUI_INVENTORY_IS_FULL.toString()));
            e.setCancelled(true);
            continueExecution = false;
         }

         if (continueExecution) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 2.0F);
            Utils.addItemManually(player.getInventory(), currentItem);
            shop.removeItem(slot);
            e.setCurrentItem((ItemStack)null);
         }

      });
      inventoryAPI.onClose(() -> {
         ProtocolLibAPI.chestCloseAnimation(player, shop.getLocation());
         shop.updateHolo();
      });
      player.openInventory(inventoryAPI.getInventory());
   }
}
