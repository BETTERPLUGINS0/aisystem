package github.nighter.smartspawner.commands.list.gui.serverselection;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.list.ListSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ServerSelectionHandler implements Listener {
   private final SmartSpawner plugin;
   private final ListSubCommand listSubCommand;

   public ServerSelectionHandler(SmartSpawner plugin, ListSubCommand listSubCommand) {
      this.plugin = plugin;
      this.listSubCommand = listSubCommand;
   }

   @EventHandler
   public void onServerSelectionClick(InventoryClickEvent event) {
      if (event.getInventory().getHolder(false) instanceof ServerSelectionHolder) {
         HumanEntity var3 = event.getWhoClicked();
         if (var3 instanceof Player) {
            Player player = (Player)var3;
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
               ItemMeta meta = clickedItem.getItemMeta();
               if (meta != null && meta.hasDisplayName()) {
                  String serverName = ChatColor.stripColor(meta.getDisplayName());
                  if (serverName != null && !serverName.isEmpty()) {
                     player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
                     this.listSubCommand.openWorldSelectionGUIForServer(player, serverName);
                  }
               }
            }
         }
      }
   }
}
