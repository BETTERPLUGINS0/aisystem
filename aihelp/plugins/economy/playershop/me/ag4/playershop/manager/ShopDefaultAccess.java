package me.ag4.playershop.manager;

import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.Utils;
import me.ag4.playershop.api.DataUtils;
import me.ag4.playershop.api.InventoryAPI;
import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.files.Lang;
import me.ag4.playershop.hooks.ProtocolLibAPI;
import me.ag4.playershop.objects.Shop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopDefaultAccess {
   public ShopDefaultAccess(Player player, Shop shop) {
      InventoryAPI inventoryAPI = new InventoryAPI(Utils.hex("&8&l" + shop.getUsername() + " &c&lSHOP"), 27);
      Economy econ = PlayerShop.getInstance().getEcon();
      if (!shop.getInventory().isEmpty()) {
         Inventory inventory = shop.getInventory();
         inventoryAPI.getInventory().setContents(inventory.getContents());
      }

      PlayerShopAPI.addBuySection(inventoryAPI.getInventory(), shop.getPrice());
      inventoryAPI.onClickInventory((e) -> {
         ItemStack currentItem = e.getCurrentItem();
         int slot = e.getSlot();
         boolean continueExecution = true;
         if (Utils.isTopInventory(e.getRawSlot(), e.getView()) && PlayerShopAPI.buySection(slot)) {
            e.setCancelled(true);
            continueExecution = false;
         }

         if (continueExecution && e.getCurrentItem() == null) {
            continueExecution = false;
         }

         if (continueExecution && !Utils.isTopInventory(e.getRawSlot(), e.getView())) {
            e.setCancelled(true);
            continueExecution = false;
         }

         if (continueExecution && !shop.itemExist(slot)) {
            player.sendMessage(Lang.GUI_Item_Not_Exist_Default.toString());
            e.getInventory().setItem(slot, (ItemStack)null);
            e.setCancelled(true);
            continueExecution = false;
         }

         if (continueExecution && econ.getBalance(player) < shop.getPrice()) {
            player.sendMessage(Lang.GUI_Error_Money.toString());
            e.setCancelled(true);
            continueExecution = false;
         }

         if (continueExecution && Utils.isInventoryFull(player.getInventory())) {
            player.sendMessage(Utils.hex(Lang.GUI_INVENTORY_IS_FULL.toString()));
            e.setCancelled(true);
            continueExecution = false;
         }

         if (continueExecution) {
            OfflinePlayer owner = shop.getPlayer();
            player.sendMessage(Lang.GUI_Item_Buy.toString().replace("{price}", shop.getPrice().makeConcatWithConstants<invokedynamic>(shop.getPrice())));
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 2.0F);
            econ.withdrawPlayer(player, shop.getPrice());
            econ.depositPlayer(owner, shop.getPrice());
            shop.removeItem(slot);
            player.getInventory().addItem(new ItemStack[]{currentItem});
            e.setCurrentItem((ItemStack)null);
            double purchaseAmount = DataUtils.purchase.get(shop.getOwner()) != null ? (Double)DataUtils.purchase.get(shop.getOwner()) : 0.0D;
            DataUtils.purchase.put(shop.getOwner(), purchaseAmount + shop.getPrice());
         }

      });
      inventoryAPI.onClose(() -> {
         ProtocolLibAPI.chestCloseAnimation(player, shop.getLocation());
         shop.updateHolo();
      });
      player.openInventory(inventoryAPI.getInventory());
   }
}
