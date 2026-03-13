package tntrun.eventhandler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;

public class ShopHandler implements Listener {
   private TNTRun plugin;

   public ShopHandler(TNTRun plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onClick(InventoryClickEvent e) {
      Inventory inv = e.getClickedInventory();
      if (inv != null) {
         Player p = (Player)e.getWhoClicked();
         if (this.plugin.isGlobalShop() && inv.equals(this.plugin.getShop().getInv(p.getName()))) {
            e.setCancelled(true);
            if (e.getRawSlot() != this.plugin.getShop().getInvsize() - 1) {
               Arena arena = this.plugin.amanager.getPlayerArena(p.getName());
               if (arena != null) {
                  if (e.getSlot() == e.getRawSlot() && e.getCurrentItem() != null) {
                     ItemStack current = e.getCurrentItem();
                     if (current.hasItemMeta() && current.getItemMeta().hasDisplayName()) {
                        FileConfiguration cfg = this.plugin.getShop().getShopFiles().getShopConfiguration();
                        int kit = (Integer)this.plugin.getShop().getItemSlot().get(e.getSlot());
                        if (cfg.getInt(kit + ".items.1.amount") <= 0) {
                           Messages.sendMessage(p, Messages.shopnostock);
                           return;
                        }

                        String permission = cfg.getString(kit + ".permission");
                        if (!p.hasPermission(permission) && !p.hasPermission("tntrun.shop")) {
                           p.closeInventory();
                           Messages.sendMessage(p, Messages.nopermission);
                           this.plugin.getSound().ITEM_SELECT(p);
                           return;
                        }

                        String title = current.getItemMeta().getDisplayName();
                        if (this.plugin.getShop().validatePurchase(p, kit, title)) {
                           this.plugin.getShop().giveItem(e.getSlot(), p, title);
                        }
                     }
                  }

               }
            }
         }
      }
   }
}
