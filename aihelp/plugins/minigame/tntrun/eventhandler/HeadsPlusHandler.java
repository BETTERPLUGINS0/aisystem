package tntrun.eventhandler;

import io.github.thatsmusic99.headsplus.api.events.HeadPurchaseEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.utils.FormattingCodesParser;

public class HeadsPlusHandler implements Listener {
   private TNTRun plugin;

   public HeadsPlusHandler(TNTRun plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onHeadPurchase(HeadPurchaseEvent e) {
      if (!e.isCancelled()) {
         final Player player = e.getPlayer();
         Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
         if (arena != null) {
            player.closeInventory();
            final ItemStack itemStack = e.getItemStack();
            (new BukkitRunnable() {
               public void run() {
                  for(int i = 0; i < 9; ++i) {
                     if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() == itemStack.getType() && !HeadsPlusHandler.this.isStatsItem(player.getInventory().getItem(i))) {
                        player.getInventory().setHelmet(player.getInventory().getItem(i));
                        player.getInventory().setItem(i, (ItemStack)null);
                        break;
                     }
                  }

                  player.updateInventory();
               }
            }).runTaskLater(this.plugin, 2L);
         }
      }
   }

   private boolean isStatsItem(ItemStack invItem) {
      ItemMeta meta = invItem.getItemMeta();
      if (meta.hasDisplayName()) {
         String name = ChatColor.stripColor(meta.getDisplayName());
         return name.equalsIgnoreCase(ChatColor.stripColor(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("items.stats.name"))));
      } else {
         return false;
      }
   }
}
