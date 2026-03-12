package emanondev.itemtag.gui;

import emanondev.itemedit.gui.PagedGui;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.activity.Activity;
import emanondev.itemtag.activity.TriggerHandler;
import emanondev.itemtag.activity.TriggerType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TriggerGui implements PagedGui {
   private static final int TRIGGER_SLOTS = 45;
   private final Inventory inventory;
   private final Player target;
   private final ItemStack item;
   private final List<TriggerType> triggers = new ArrayList();
   private int page = 1;

   public TriggerGui(Player target, ItemStack item) {
      String title = this.getLanguageMessage("gui.triggers.title_main", new String[]{"%player_name%", target.getName()});
      this.item = item;
      this.inventory = Bukkit.createInventory(this, 54, title);
      this.target = target;
      this.triggers.addAll(TriggerHandler.getTriggers());
      this.triggers.sort(Comparator.comparing(TriggerType::getId));
   }

   @NotNull
   public Inventory getInventory() {
      return this.inventory;
   }

   public Player getTargetPlayer() {
      return this.target;
   }

   @NotNull
   public ItemTag getPlugin() {
      return ItemTag.get();
   }

   public int getPage() {
      return this.page;
   }

   public void setPage(int page) {
      page = Math.max(1, Math.min(page, this.getMaxPage()));
      if (page != this.page) {
         this.inventory.clear();
         this.page = page;
         this.updateInventory();
      }

   }

   public int getMaxPage() {
      return this.triggers.size() / 45 + (this.triggers.size() % 45 == 0 ? 0 : 1);
   }

   public void onClose(InventoryCloseEvent inventoryCloseEvent) {
   }

   public void onClick(InventoryClickEvent event) {
      if (event.getClickedInventory() == this.inventory) {
         if (this.triggers.size() > 45 * (this.page - 1) + event.getSlot()) {
            TriggerType type = (TriggerType)this.triggers.get(45 * (this.page - 1) + event.getSlot());
            TagItem tagItem = ItemTag.getTagItem(this.getTargetPlayer().getItemInHand());
            Activity activity = TriggerHandler.getTriggerActivity(type, tagItem);
            if (activity != null) {
               this.getTargetPlayer().openInventory((new ActivityGui(activity, this.getTargetPlayer(), this)).getInventory());
            }

         }
      }
   }

   public void onDrag(InventoryDragEvent event) {
   }

   public void onOpen(InventoryOpenEvent event) {
      this.updateInventory();
   }

   private void updateInventory() {
      for(int i = 0; 45 * (this.page - 1) + i < this.triggers.size(); ++i) {
         this.inventory.setItem(i, ((TriggerType)this.triggers.get(45 * (this.page - 1) + i)).getGuiItem(this.getTargetPlayer()));
      }

   }
}
