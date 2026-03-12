package emanondev.itemtag.gui;

import emanondev.itemedit.gui.Gui;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.activity.Activity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActivityGui implements Gui {
   private final Inventory inventory;
   private final Player target;
   private final Activity activity;
   private final TriggerGui parent;

   public ActivityGui(@NotNull Activity activity, @NotNull Player target, @Nullable TriggerGui parent) {
      String title = this.getLanguageMessage("gui.activity.title_main", new String[]{"%player_name%", target.getName(), "%activity_id%", activity.getId()});
      this.inventory = Bukkit.createInventory(this, 54, title);
      this.target = target;
      this.activity = activity;
      this.parent = parent;
   }

   public void onClose(InventoryCloseEvent inventoryCloseEvent) {
   }

   public void onClick(InventoryClickEvent inventoryClickEvent) {
   }

   public void onDrag(InventoryDragEvent inventoryDragEvent) {
   }

   public void onOpen(InventoryOpenEvent inventoryOpenEvent) {
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
}
