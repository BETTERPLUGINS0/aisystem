package me.gypopo.economyshopgui.objects.inventorys;

import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ShopInventory;
import me.gypopo.economyshopgui.objects.stands.Stand;
import me.gypopo.economyshopgui.util.ItemBuilder;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class StandManagement extends ShopInventory {
   private final Inventory inv;
   private final StandManagement.Listen listener = new StandManagement.Listen();
   private final EconomyShopGUI plugin;
   private final Stand stand;

   public StandManagement(EconomyShopGUI plugin, Player p, Stand stand) {
      this.inv = plugin.getMetaUtils().createInventory(this, 9, Lang.SHOP_STANDS_MANAGEMENT_INVENTORY_TITLE.get());
      this.plugin = plugin;
      this.stand = stand;
      this.buildPane();
      Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
      p.openInventory(this.inv);
   }

   private void buildPane() {
      this.inv.setItem(0, (new ItemBuilder(XMaterial.NETHER_STAR.parseItem())).withDisplayName(Lang.SHOP_STAND_INFO_ITEM_NAME.get()).withLore(Lang.SHOP_STAND_INFO_ITEM_LORE.get().replace("%id%", String.valueOf(this.stand.getId())).replace("%location%", this.stand.getLoc().toString()).replace("%item%", this.stand.getItem()).replace("%type%", this.stand.getType().name()).replace("%loaded%", String.valueOf(this.stand.isLoaded()))).build());
      this.inv.setItem(7, (new ItemBuilder(Material.REDSTONE)).withDisplayName(Lang.FORCE_RELOAD_STAND_ITEM_NAME.get()).withLore(Lang.FORCE_RELOAD_STAND_ITEM_LORE.get()).build());
      this.inv.setItem(8, (new ItemBuilder(XMaterial.BARRIER.parseItem())).withDisplayName(Lang.DESTROY_STAND_ITEM_NAME.get()).withLore(Lang.DESTROY_STAND_ITEM_LORE.get()).build());
      this.fillEmpty();
   }

   private void fillEmpty() {
      if (this.plugin.createItem.fillItem != null) {
         for(int i = 0; i < this.inv.getSize(); ++i) {
            if (this.inv.getItem(i) == null) {
               this.inv.setItem(i, this.plugin.createItem.fillItem);
            }
         }

      }
   }

   private void quit() {
      InventoryClickEvent.getHandlerList().unregister(this.listener);
   }

   private class Listen implements Listener {
      private Listen() {
      }

      @EventHandler
      public void onInventoryClick(InventoryClickEvent e) {
         if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof StandManagement && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            e.setCancelled(true);
            Player p = (Player)e.getWhoClicked();
            if (e.getRawSlot() == 7) {
               if (StandManagement.this.plugin.getStandProvider().reloadStand(StandManagement.this.stand)) {
                  SendMessage.chatToPlayer(p, Lang.RELOADED_SHOP_STAND.get().replace("%id%", String.valueOf(StandManagement.this.stand.getId())));
               }
            } else {
               if (e.getRawSlot() != 8) {
                  return;
               }

               if (!PermissionsCache.hasPermission(p, "EconomyShopGUI.eshop.shopstands.destroy")) {
                  SendMessage.chatToPlayer(p, Lang.NO_PERMISSIONS.get());
                  return;
               }

               StandManagement.this.plugin.getStandProvider().destroy(StandManagement.this.stand);
               SendMessage.chatToPlayer(p, Lang.REMOVED_SHOP_STAND.get().replace("%location%", StandManagement.this.stand.getLoc().toString()));
            }

            StandManagement.this.quit();
            e.getWhoClicked().closeInventory();
         }
      }

      @EventHandler
      public void onInventoryClose(InventoryCloseEvent e) {
         if (e.getInventory().getHolder() instanceof StandManagement) {
            StandManagement.this.quit();
         }

      }

      // $FF: synthetic method
      Listen(Object x1) {
         this();
      }
   }
}
