package es.outlook.adriansrj.spigui.menu;

import es.outlook.adriansrj.spigui.SpiGUI;
import es.outlook.adriansrj.spigui.buttons.SGButton;
import es.outlook.adriansrj.spigui.toolbar.SGToolbarBuilder;
import es.outlook.adriansrj.spigui.toolbar.SGToolbarButtonType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public class SGMenu implements InventoryHolder {
   private final JavaPlugin owner;
   private final SpiGUI spiGUI;
   private String name;
   private String tag;
   private int rowsPerPage;
   private final Map<Integer, SGButton> items;
   private final HashSet<Integer> stickiedSlots;
   private int currentPage;
   private boolean blockDefaultInteractions;
   private boolean enableAutomaticPagination;
   private SGToolbarBuilder toolbarBuilder;
   private Consumer<SGMenu> onClose;
   private Consumer<SGMenu> onPageChange;

   public SGMenu(JavaPlugin var1, SpiGUI var2, String var3, int var4, String var5) {
      this.owner = var1;
      this.spiGUI = var2;
      this.name = ChatColor.translateAlternateColorCodes('&', var3);
      this.rowsPerPage = var4;
      this.tag = var5;
      this.items = new HashMap();
      this.stickiedSlots = new HashSet();
      this.currentPage = 0;
   }

   public void setBlockDefaultInteractions(boolean var1) {
      this.blockDefaultInteractions = var1;
   }

   public Boolean areDefaultInteractionsBlocked() {
      return this.blockDefaultInteractions;
   }

   public void setAutomaticPaginationEnabled(boolean var1) {
      this.enableAutomaticPagination = var1;
   }

   public Boolean isAutomaticPaginationEnabled() {
      return this.enableAutomaticPagination;
   }

   public void setToolbarBuilder(SGToolbarBuilder var1) {
      this.toolbarBuilder = var1;
   }

   public SGToolbarBuilder getToolbarBuilder() {
      return this.toolbarBuilder;
   }

   public JavaPlugin getOwner() {
      return this.owner;
   }

   public int getRowsPerPage() {
      return this.rowsPerPage;
   }

   public int getPageSize() {
      return this.rowsPerPage * 9;
   }

   public void setRowsPerPage(int var1) {
      this.rowsPerPage = var1;
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String var1) {
      this.tag = var1;
   }

   public void setName(String var1) {
      this.name = ChatColor.translateAlternateColorCodes('&', var1);
   }

   public void setRawName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void addButton(SGButton var1) {
      if (this.getHighestFilledSlot() == 0 && this.getButton(0) == null) {
         this.setButton(0, var1);
      } else {
         this.setButton(this.getHighestFilledSlot() + 1, var1);
      }
   }

   public void addButtons(SGButton... var1) {
      SGButton[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         SGButton var5 = var2[var4];
         this.addButton(var5);
      }

   }

   public void setButton(int var1, SGButton var2) {
      this.items.put(var1, var2);
   }

   public void setButton(int var1, int var2, SGButton var3) {
      if (var2 >= 0 && var2 <= this.getPageSize()) {
         this.setButton(var1 * this.getPageSize() + var2, var3);
      }
   }

   public void removeButton(int var1) {
      this.items.remove(var1);
   }

   public void removeButton(int var1, int var2) {
      if (var2 >= 0 && var2 <= this.getPageSize()) {
         this.removeButton(var1 * this.getPageSize() + var2);
      }
   }

   public SGButton getButton(int var1) {
      return var1 >= 0 && var1 <= this.getHighestFilledSlot() ? (SGButton)this.items.get(var1) : null;
   }

   public SGButton getButton(int var1, int var2) {
      return var2 >= 0 && var2 <= this.getPageSize() ? this.getButton(var1 * this.getPageSize() + var2) : null;
   }

   public int getCurrentPage() {
      return this.currentPage;
   }

   public void setCurrentPage(int var1) {
      this.currentPage = var1;
      if (this.onPageChange != null) {
         this.onPageChange.accept(this);
      }

   }

   public int getMaxPage() {
      return (int)Math.ceil(((double)this.getHighestFilledSlot() + 1.0D) / (double)this.getPageSize());
   }

   public int getHighestFilledSlot() {
      int var1 = 0;
      Iterator var2 = this.items.keySet().iterator();

      while(var2.hasNext()) {
         int var3 = (Integer)var2.next();
         if (this.items.get(var3) != null && var3 > var1) {
            var1 = var3;
         }
      }

      return var1;
   }

   public boolean nextPage(HumanEntity var1) {
      if (this.currentPage < this.getMaxPage() - 1) {
         ++this.currentPage;
         this.refreshInventory(var1);
         if (this.onPageChange != null) {
            this.onPageChange.accept(this);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean previousPage(HumanEntity var1) {
      if (this.currentPage > 0) {
         --this.currentPage;
         this.refreshInventory(var1);
         if (this.onPageChange != null) {
            this.onPageChange.accept(this);
         }

         return true;
      } else {
         return false;
      }
   }

   public void stickSlot(int var1) {
      if (var1 >= 0 && var1 < this.getPageSize()) {
         this.stickiedSlots.add(var1);
      }
   }

   public void unstickSlot(int var1) {
      this.stickiedSlots.remove(var1);
   }

   public void clearStickiedSlots() {
      this.stickiedSlots.clear();
   }

   public boolean isStickiedSlot(int var1) {
      return var1 >= 0 && var1 < this.getPageSize() ? this.stickiedSlots.contains(var1) : false;
   }

   public void clearAllButStickiedSlots() {
      this.currentPage = 0;
      this.items.entrySet().removeIf((var1) -> {
         return !this.isStickiedSlot((Integer)var1.getKey());
      });
   }

   public Consumer<SGMenu> getOnClose() {
      return this.onClose;
   }

   public void setOnClose(Consumer<SGMenu> var1) {
      this.onClose = var1;
   }

   public Consumer<SGMenu> getOnPageChange() {
      return this.onPageChange;
   }

   public void setOnPageChange(Consumer<SGMenu> var1) {
      this.onPageChange = var1;
   }

   public void refreshInventory(HumanEntity var1) {
      if (var1.getOpenInventory().getTopInventory().getHolder() instanceof SGMenu && var1.getOpenInventory().getTopInventory().getHolder() == this) {
         if (var1.getOpenInventory().getTopInventory().getSize() != this.getPageSize() + (this.getMaxPage() > 0 ? 9 : 0)) {
            var1.openInventory(this.getInventory());
         } else {
            String var2 = this.name.replace("{currentPage}", String.valueOf(this.currentPage + 1)).replace("{maxPage}", String.valueOf(this.getMaxPage()));
            if (!var1.getOpenInventory().getTitle().equals(var2)) {
               var1.openInventory(this.getInventory());
            } else {
               var1.getOpenInventory().getTopInventory().setContents(this.getInventory().getContents());
            }
         }
      }
   }

   public Inventory getInventory() {
      boolean var1 = this.spiGUI.isAutomaticPaginationEnabled();
      if (this.isAutomaticPaginationEnabled() != null) {
         var1 = this.isAutomaticPaginationEnabled();
      }

      boolean var2 = this.getMaxPage() > 0 && var1;
      Inventory var3 = Bukkit.createInventory(this, var2 ? this.getPageSize() + 9 : this.getPageSize(), this.name.replace("{currentPage}", String.valueOf(this.currentPage + 1)).replace("{maxPage}", String.valueOf(this.getMaxPage())));

      for(int var4 = this.currentPage * this.getPageSize(); var4 < (this.currentPage + 1) * this.getPageSize() && var4 <= this.getHighestFilledSlot(); ++var4) {
         if (this.items.containsKey(var4)) {
            var3.setItem(var4 - this.currentPage * this.getPageSize(), ((SGButton)this.items.get(var4)).getIcon());
         }
      }

      Iterator var9 = this.stickiedSlots.iterator();

      int var5;
      while(var9.hasNext()) {
         var5 = (Integer)var9.next();
         var3.setItem(var5, ((SGButton)this.items.get(var5)).getIcon());
      }

      if (var2) {
         SGToolbarBuilder var10 = this.spiGUI.getDefaultToolbarBuilder();
         if (this.getToolbarBuilder() != null) {
            var10 = this.getToolbarBuilder();
         }

         var5 = this.getPageSize();

         for(int var6 = var5; var6 < var5 + 9; ++var6) {
            int var7 = var6 - var5;
            SGButton var8 = var10.buildToolbarButton(var7, this.getCurrentPage(), SGToolbarButtonType.getDefaultForSlot(var7), this);
            var3.setItem(var6, var8 != null ? var8.getIcon() : null);
         }
      }

      return var3;
   }
}
