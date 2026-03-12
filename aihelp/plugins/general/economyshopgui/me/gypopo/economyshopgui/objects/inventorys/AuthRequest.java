package me.gypopo.economyshopgui.objects.inventorys;

import java.util.function.Consumer;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.CalculateAmount;
import me.gypopo.economyshopgui.objects.ShopInventory;
import me.gypopo.economyshopgui.util.ItemBuilder;
import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class AuthRequest extends ShopInventory {
   private final Inventory inv = Bukkit.createInventory(this, 36, "§8§lAuthorization required");
   private final AuthRequest.Listen listener = new AuthRequest.Listen();
   private final EconomyShopGUI plugin = EconomyShopGUI.getInstance();
   private final Consumer<AuthRequest.AuthMethod> onComplete;

   public AuthRequest(Player p, Consumer<AuthRequest.AuthMethod> onComplete) {
      this.onComplete = onComplete;
      this.buildPane();
      Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
      p.openInventory(this.inv);
   }

   private void buildPane() {
      this.inv.setItem(12, (new ItemBuilder(XMaterial.PAPER.parseItem())).withDisplayName("§a§lTemporary authorization").withLore(CalculateAmount.splitLongString("§cWhen using the temporary authorization method, the shop layout will be uploaded §c§lanomalously §r§cbut will only be available for 5 days only and removed after.")).build());
      this.inv.setItem(14, (new ItemBuilder(XMaterial.PAPER.parseItem())).withDisplayName("§9§lAuthorization via discord").withLore(CalculateAmount.splitLongString("§cWhen authorizing using discord, the shop layout will be linked to your discord account. \n §cThis allows you to update your existing layout and will be available until you remove the layout yourself.")).build());
      this.inv.setItem(22, (new ItemBuilder(XMaterial.NETHER_STAR.parseItem())).withDisplayName("§c§lCancel upload process").withEnchantGlint().build());
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
         if (e.getClickedInventory() != null && e.getClickedInventory() == AuthRequest.this.inv && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() != AuthRequest.this.plugin.createItem.fillItem.getType()) {
               AuthRequest.AuthMethod method = null;
               if (e.getRawSlot() == 12) {
                  method = AuthRequest.AuthMethod.TEMP;
               } else if (e.getRawSlot() == 14) {
                  method = AuthRequest.AuthMethod.DISCORD;
               }

               AuthRequest.this.quit();
               e.getWhoClicked().closeInventory();
               AuthRequest.this.onComplete.accept(method);
            }
         }
      }

      @EventHandler
      public void onInventoryClose(InventoryCloseEvent e) {
         if (e.getInventory() == AuthRequest.this.inv) {
            AuthRequest.this.quit();
         }

      }

      // $FF: synthetic method
      Listen(Object x1) {
         this();
      }
   }

   public static enum AuthMethod {
      DISCORD,
      TEMP;

      // $FF: synthetic method
      private static AuthRequest.AuthMethod[] $values() {
         return new AuthRequest.AuthMethod[]{DISCORD, TEMP};
      }
   }
}
