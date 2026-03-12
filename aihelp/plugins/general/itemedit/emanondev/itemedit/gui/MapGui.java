package emanondev.itemedit.gui;

import emanondev.itemedit.APlugin;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.gui.button.Button;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapGui implements Gui {
   private final HashMap<Integer, Button> buttons = new HashMap();
   private final Inventory inv;
   private final Player targetPlayer;
   private final APlugin plugin;

   public MapGui(@NotNull APlugin plugin, @NotNull Player target, @Nullable String title, int rows) {
      this.plugin = plugin;
      this.targetPlayer = target;
      this.inv = Bukkit.createInventory(this, rows * 9, UtilsString.fix(title == null ? "" : title, target, true));
   }

   public void onClose(InventoryCloseEvent event) {
   }

   public void onClick(InventoryClickEvent event) {
      if (this.buttons.containsKey(event.getSlot()) && ((Button)this.buttons.get(event.getSlot())).onClick(event)) {
         this.updateInventory();
      }

   }

   public void registerButton(int slot, Button button) {
      this.buttons.put(slot, button);
   }

   public void updateInventory() {
      this.buttons.forEach((slot, button) -> {
         if (slot >= 0 && slot < this.getInventory().getSize()) {
            this.getInventory().setItem(slot, button.getItem());
         }
      });
   }

   public void onDrag(InventoryDragEvent event) {
   }

   public void onOpen(InventoryOpenEvent event) {
      this.updateInventory();
   }

   @NotNull
   public Inventory getInventory() {
      return this.inv;
   }

   @NotNull
   public Player getTargetPlayer() {
      return this.targetPlayer;
   }

   @NotNull
   public APlugin getPlugin() {
      return this.plugin;
   }

   public void openGui() {
      if (this.targetPlayer.isValid()) {
         this.targetPlayer.openInventory(this.inv);
      }

   }
}
