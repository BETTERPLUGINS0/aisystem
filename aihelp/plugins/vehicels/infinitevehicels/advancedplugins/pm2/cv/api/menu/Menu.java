package advancedplugins.pm2.cv.api.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Menu {
   public Map<Integer, ClickableItem> itemsStorage = new HashMap();
   public List<Menu.Option> options = new ArrayList();

   public abstract void open(Player var1);

   public abstract void onOpen(Player var1);

   public abstract void onClose(Player var1);

   public abstract Inventory getInventory();

   public abstract void setItem(int var1, ItemStack var2);

   public abstract void setItem(int var1, ItemStack var2, @NonNull Consumer<InventoryClickEvent> var3);

   public abstract ClickableItem getItem(int var1);

   public abstract void removeItem(int var1);

   public void onClick(InventoryClickEvent var1) {
   }

   public void addOption(Menu.Option var1) {
      this.options.add(var1);
   }

   public void removeOption(Menu.Option var1) {
      this.options.remove(var1);
   }

   public Map<Integer, ClickableItem> getItemsStorage() {
      return this.itemsStorage;
   }

   public List<Menu.Option> getOptions() {
      return this.options;
   }

   public static enum Option {
      DO_NOT_CANCEL_CLICK;

      // $FF: synthetic method
      private static Menu.Option[] $values() {
         return new Menu.Option[]{DO_NOT_CANCEL_CLICK};
      }
   }
}
