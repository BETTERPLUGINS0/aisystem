package advancedplugins.pm2.cv.api.menu;

import java.util.function.Consumer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public record ClickableItem(ItemStack itemStack, Consumer<InventoryClickEvent> action) {
   public ClickableItem(ItemStack itemStack, Consumer<InventoryClickEvent> action) {
      this.itemStack = var1;
      this.action = var2;
   }

   public Consumer<InventoryClickEvent> getAction() {
      return this.action;
   }

   public ItemStack itemStack() {
      return this.itemStack;
   }

   public Consumer<InventoryClickEvent> action() {
      return this.action;
   }
}
