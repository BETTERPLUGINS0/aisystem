package advancedplugins.pm2.cv.api.item;

import advancedplugins.pm2.cv.api.event.VehicleClickedEvent;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickableItems {
   private static final Map<String, BiConsumer<Player, Vehicle>> clickableEvents = new HashMap();

   public static void init(JavaPlugin var0) {
      var0.getServer().getPluginManager().registerEvents(new ClickableItems.ClickableItemsListener(), var0);
   }

   public static void setClickableItem(ItemStack var0, String var1) {
      ItemMeta var2 = var0.getItemMeta();
      if (var2 == null) {
         var2 = Bukkit.getItemFactory().getItemMeta(var0.getType());
      }

      if (var2 != null) {
         var2.getPersistentDataContainer().set(new NamespacedKey("infinite_vehicles", "clickable_item"), PersistentDataType.STRING, var1);
         var0.setItemMeta(var2);
      }
   }

   public static String getClickableId(ItemStack var0) {
      return var0 != null && var0.getItemMeta() != null ? (String)var0.getItemMeta().getPersistentDataContainer().get(new NamespacedKey("infinite_vehicles", "clickable_item"), PersistentDataType.STRING) : null;
   }

   public static void addClickableEvent(String var0, BiConsumer<Player, Vehicle> var1) {
      clickableEvents.put(var0, var1);
   }

   public static BiConsumer<Player, Vehicle> getClickableEvent(String var0) {
      return (BiConsumer)clickableEvents.get(var0);
   }

   private static final class ClickableItemsListener implements Listener {
      @EventHandler
      public void onVehicleClick(VehicleClickedEvent var1) {
         ItemStack var2 = var1.getPlayer().getInventory().getItemInMainHand();
         String var3 = ClickableItems.getClickableId(var2);
         if (var3 != null) {
            var1.setCancelled(true);
            BiConsumer var4 = ClickableItems.getClickableEvent(var3);
            if (var4 != null) {
               var4.accept(var1.getPlayer(), var1.getVehicle());
            }
         }
      }

      @EventHandler
      public void onPlayerInteract(PlayerInteractEvent var1) {
         Player var2 = var1.getPlayer();
         ItemStack var3 = var1.getItem();
         if (var1.getHand() == EquipmentSlot.HAND && var3 != null) {
            String var4 = ClickableItems.getClickableId(var3);
            if (var4 != null) {
               var1.setUseItemInHand(Result.DENY);
               var1.setCancelled(true);
            }
         }
      }
   }
}
