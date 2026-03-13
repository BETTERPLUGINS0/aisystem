package me.gypopo.economyshopgui.objects.inventorys;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.CalculateAmount;
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
import org.bukkit.inventory.ItemStack;

public class StandBrowser extends ShopInventory {
   private final Inventory inv;
   private final StandBrowser.Listen listener = new StandBrowser.Listen();
   private final EconomyShopGUI plugin;
   private final ArrayList<Stand> stands;
   private int page = 1;
   private final int allPages;

   public StandBrowser(EconomyShopGUI plugin, Player p) {
      this.stands = (ArrayList)plugin.getStandProvider().getStands().stream().sorted(Comparator.comparingInt(Stand::getId)).collect(Collectors.toCollection(ArrayList::new));
      int total = this.stands.size();
      this.allPages = (int)Math.ceil((double)total / 45.0D);
      this.inv = plugin.getMetaUtils().createInventory(this, CalculateAmount.getSlots(total), Lang.SHOP_STAND_BROWSE_INVENTORY_TITLE.get());
      this.plugin = plugin;
      this.buildPane();
      this.openPage(1);
      Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
      p.openInventory(this.inv);
   }

   private void buildPane() {
      this.inv.setItem(this.inv.getSize() - 5, (new ItemBuilder(XMaterial.COMPASS.parseItem())).withDisplayName(Lang.CURRENT_PAGE.get() + " " + this.page + "/" + this.allPages).build());
      this.inv.setItem(this.inv.getSize() - 6, (new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem())).withDisplayName(Lang.PREVIOUS_PAGE.get()).build());
      this.inv.setItem(this.inv.getSize() - 4, (new ItemBuilder(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE.parseItem())).withDisplayName(Lang.NEXT_PAGE.get()).build());
   }

   private void openPage(int page) {
      int index = page * 45 - 45;

      for(int i = index; i < index + Math.min(this.stands.size() - index, 45); ++i) {
         Stand stand = (Stand)this.stands.get(i);
         this.inv.addItem(new ItemStack[]{(new ItemBuilder(XMaterial.NETHER_STAR.parseItem())).withDisplayName(Lang.SHOP_STAND_INFO_ITEM_NAME.get()).withLore(Lang.SHOP_STAND_INFO_ITEM_LORE.get().replace("%id%", String.valueOf(stand.getId())).replace("%location%", stand.getLoc().toString()).replace("%item%", stand.getItem()).replace("%type%", stand.getType().name()).replace("%loaded%", String.valueOf(stand.isLoaded()))).build()});
      }

      this.page = page;
   }

   private void quit() {
      InventoryClickEvent.getHandlerList().unregister(this.listener);
   }

   private class Listen implements Listener {
      private Listen() {
      }

      @EventHandler
      public void onInventoryClick(InventoryClickEvent e) {
         if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof StandBrowser && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            e.setCancelled(true);
            Player p = (Player)e.getWhoClicked();
            if (e.getRawSlot() == StandBrowser.this.inv.getSize() - 6) {
               if (StandBrowser.this.page > 1) {
                  StandBrowser.this.inv.clear();
                  StandBrowser.this.openPage(StandBrowser.this.page - 1);
                  StandBrowser.this.buildPane();
               }
            } else if (e.getRawSlot() == StandBrowser.this.inv.getSize() - 4) {
               if (StandBrowser.this.page < StandBrowser.this.allPages) {
                  StandBrowser.this.inv.clear();
                  StandBrowser.this.openPage(StandBrowser.this.page + 1);
                  StandBrowser.this.buildPane();
               }
            } else {
               Stand stand = (Stand)StandBrowser.this.stands.get(StandBrowser.this.page * 45 - 45 + e.getRawSlot());
               if (stand != null) {
                  if (!PermissionsCache.hasPermission(p, "EconomyShopGUI.eshop.shopstands.edit")) {
                     SendMessage.chatToPlayer(p, Lang.NO_PERMISSIONS.get());
                     return;
                  }

                  new StandManagement(StandBrowser.this.plugin, p, (Stand)StandBrowser.this.stands.get(StandBrowser.this.page * 45 - 45 + e.getRawSlot()));
                  StandBrowser.this.quit();
               }
            }

         }
      }

      @EventHandler
      public void onInventoryClose(InventoryCloseEvent e) {
         if (e.getInventory().getHolder() instanceof StandBrowser) {
            StandBrowser.this.quit();
         }

      }

      // $FF: synthetic method
      Listen(Object x1) {
         this();
      }
   }
}
