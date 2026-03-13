package tntrun.signs.type;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;

public class LeaveSign implements SignType {
   private TNTRun plugin;

   public LeaveSign(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void handleCreation(SignChangeEvent e) {
      e.setLine(0, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")));
      Messages.sendMessage(e.getPlayer(), Messages.signcreate);
   }

   public void handleClick(PlayerInteractEvent e) {
      Arena arena = this.plugin.amanager.getPlayerArena(e.getPlayer().getName());
      if (arena != null) {
         arena.getPlayerHandler().leavePlayer(e.getPlayer(), Messages.playerlefttoplayer, Messages.playerlefttoothers);
         e.setCancelled(true);
      } else {
         Messages.sendMessage(e.getPlayer(), Messages.playernotinarena);
      }

   }

   public void handleDestroy(BlockBreakEvent e) {
      Messages.sendMessage(e.getPlayer(), Messages.signremove);
   }
}
