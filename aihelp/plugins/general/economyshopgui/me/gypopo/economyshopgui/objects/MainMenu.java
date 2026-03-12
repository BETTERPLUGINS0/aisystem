package me.gypopo.economyshopgui.objects;

import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.providers.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MainMenu extends ShopInventory {
   private final Inventory inv;
   private final EconomyShopGUI plugin = EconomyShopGUI.getInstance();

   public MainMenu(Player p) {
      User user = UserManager.getUser(p);
      this.inv = this.plugin.getMetaUtils().createInventory(this, this.plugin.resizeGUI && user.isBedrock() ? 54 : this.plugin.mainMenuSize, Lang.INVENTORY_MAIN_SHOP_TITLE.get());
      this.init();
      this.plugin.navBar.addMainMenuNavBar(this.inv, p);
      this.open(p, user);
   }

   private void open(Player player, User user) {
      user.setOpenNewGUI(true);
      player.openInventory(this.inv);
   }

   private void init() {
      this.plugin.getSectionItems().forEach((slot, item) -> {
         this.inv.setItem(slot, item);
      });
      if (this.plugin.createItem.fillItem != null) {
         int menuSize = this.plugin.mainMenuSize;

         for(int i = 0; i < (this.plugin.navBar.isEnableMainNav() ? menuSize - 9 : menuSize); ++i) {
            if (this.inv.getItem(i) == null) {
               this.inv.setItem(i, this.plugin.createItem.fillItem);
            }
         }
      }

   }

   public Inventory getInventory() {
      return this.inv;
   }
}
