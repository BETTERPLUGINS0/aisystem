package advancedplugins.pm2.cv.api.menu;

import advancedplugins.pm2.cv.api.util.Run;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuManager implements Listener {
   private final HashMap<Player, Menu> guiStorage = new HashMap();
   private final HashMap<Player, Menu> lastOpenGuiStorage = new HashMap();

   public MenuManager(JavaPlugin var1) {
      var1.getServer().getPluginManager().registerEvents(this, var1);
   }

   @EventHandler
   public void onPlayerClick(InventoryClickEvent var1) {
      Player var2 = (Player)var1.getWhoClicked();
      Inventory var3 = var1.getClickedInventory();
      if (var3 != null) {
         if (this.getByPlayer(var2) != null) {
            if (!this.getByPlayer(var2).getOptions().contains(Menu.Option.DO_NOT_CANCEL_CLICK)) {
               var1.setCancelled(true);
            }

            Menu var4 = this.getByPlayer(var2);
            if (!(var3 instanceof PlayerInventory)) {
               this.getByPlayer(var2).onClick(var1);
               ClickableItem var5 = (ClickableItem)var4.getItemsStorage().get(var1.getRawSlot());
               if (var5 != null) {
                  var5.getAction().accept(var1);
               }
            }
         }
      }
   }

   @EventHandler
   public void onPlayerDrag(InventoryDragEvent var1) {
      Player var2 = (Player)var1.getWhoClicked();
      if (this.getByPlayer(var2) != null) {
         if (!this.getByPlayer(var2).getOptions().contains(Menu.Option.DO_NOT_CANCEL_CLICK)) {
            var1.setCancelled(true);
         }

         Iterator var3 = var1.getInventorySlots().iterator();

         while(var3.hasNext()) {
            Integer var4 = (Integer)var3.next();
            InventoryClickEvent var5 = new InventoryClickEvent(var1.getView(), SlotType.CONTAINER, var4, ClickType.RIGHT, InventoryAction.PLACE_ONE);
            this.getByPlayer(var2).onClick(var5);
         }

      }
   }

   @EventHandler
   public void onPlayerClose(InventoryCloseEvent var1) {
      Player var2 = (Player)var1.getPlayer();
      if (this.getByPlayer(var2) != null) {
         Menu var3 = this.getByPlayer(var2);
         Run.syncDelayed(() -> {
            var3.onClose(var2);
         });
         this.guiStorage.remove(var2);
         this.getLastOpenGuiStorage().put(var2, var3);
      }
   }

   public Menu getByPlayer(Player var1) {
      return (Menu)this.guiStorage.get(var1);
   }

   public void setByPlayer(Player var1, Menu var2) {
      this.guiStorage.put(var1, var2);
   }

   public HashMap<Player, Menu> getGuiStorage() {
      return this.guiStorage;
   }

   public HashMap<Player, Menu> getLastOpenGuiStorage() {
      return this.lastOpenGuiStorage;
   }
}
