package tntrun.signs.type;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;

public class VoteSign implements SignType {
   private TNTRun plugin;

   public VoteSign(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void handleCreation(SignChangeEvent e) {
      e.setLine(0, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")));
      Messages.sendMessage(e.getPlayer(), Messages.signcreate);
   }

   public void handleClick(PlayerInteractEvent e) {
      Player player = e.getPlayer();
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      if (arena != null) {
         if (arena.getPlayerHandler().vote(player)) {
            Messages.sendMessage(player, Messages.playervotedforstart);
         } else {
            Messages.sendMessage(player, Messages.playeralreadyvotedforstart);
         }

         e.setCancelled(true);
      } else {
         Messages.sendMessage(player, Messages.playernotinarena);
      }

   }

   public void handleDestroy(BlockBreakEvent e) {
      Messages.sendMessage(e.getPlayer(), Messages.signremove);
   }
}
