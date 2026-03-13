package tntrun.signs.type;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import tntrun.TNTRun;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;

public class LeaderboardSign implements SignType {
   private TNTRun plugin;

   public LeaderboardSign(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void handleCreation(final SignChangeEvent e) {
      if (!this.plugin.useStats()) {
         Messages.sendMessage(e.getPlayer(), Messages.statsdisabled);
         e.setCancelled(true);
         e.getBlock().breakNaturally();
      } else {
         e.setLine(0, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")));
         this.plugin.getSignEditor().addLeaderboardSign(e.getBlock());
         Messages.sendMessage(e.getPlayer(), Messages.signcreate);
         (new BukkitRunnable() {
            public void run() {
               LeaderboardSign.this.plugin.getSignEditor().modifyLeaderboardSign(e.getBlock());
            }
         }).runTask(this.plugin);
      }
   }

   public void handleClick(PlayerInteractEvent e) {
      e.setCancelled(true);
   }

   public void handleDestroy(BlockBreakEvent e) {
      this.plugin.getSignEditor().removeLeaderboardSign(e.getBlock());
      Messages.sendMessage(e.getPlayer(), Messages.signremove);
   }
}
