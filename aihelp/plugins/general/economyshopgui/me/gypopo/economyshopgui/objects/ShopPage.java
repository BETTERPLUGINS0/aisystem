package me.gypopo.economyshopgui.objects;

import java.util.Map;
import java.util.Objects;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Translatable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ShopPage extends ShopInventory {
   private final int size;
   private final int page;
   private final int allPages;
   private final boolean disabledBackButton;
   private final String rootSection;
   private final String section;
   private final Inventory inventory;

   public int getAllPages() {
      return this.allPages;
   }

   public boolean isDisabledBackButton() {
      return this.disabledBackButton;
   }

   public String getSection() {
      return this.section;
   }

   public String getItem(int slot) {
      return EconomyShopGUI.getInstance().getSection(this.section).getShopPageItems(this.page).getItem(slot);
   }

   public ShopPage(Map<Integer, ItemStack> items, ItemStack fillItem, Player player, Translatable title, String section, int page, int allPages, boolean disabledBackButton, boolean navBar, int size, @Nullable String rootSection) {
      this.page = page;
      this.allPages = allPages;
      this.section = section;
      this.disabledBackButton = disabledBackButton;
      this.size = size;
      this.rootSection = rootSection;
      this.inventory = EconomyShopGUI.getInstance().getMetaUtils().createInventory(this, size, title);
      this.init(player, items, fillItem, navBar);
   }

   private void init(Player p, Map<Integer, ItemStack> items, ItemStack fillItem, boolean navBar) {
      Inventory var10001 = this.inventory;
      Objects.requireNonNull(var10001);
      items.forEach(var10001::setItem);
      if (fillItem != null) {
         for(int i = 0; i < (navBar ? this.size - 9 : this.size); ++i) {
            if (!items.containsKey(i)) {
               this.inventory.setItem(i, fillItem);
            }
         }
      }

      EconomyShopGUI.getInstance().navBar.addShopsNavBar(this.inventory, p, this.section, this.page, this.allPages);
   }

   public int getPage() {
      return this.page;
   }

   public int getSize() {
      return this.size;
   }

   public boolean isSubSection() {
      return this.rootSection != null;
   }

   public String getRootSection() {
      return this.rootSection;
   }

   public Inventory getInventory() {
      return this.inventory;
   }
}
