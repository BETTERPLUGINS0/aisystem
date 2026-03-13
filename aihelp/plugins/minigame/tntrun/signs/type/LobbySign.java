package tntrun.signs.type;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tntrun.TNTRun;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;

public class LobbySign implements SignType {
   private TNTRun plugin;

   public LobbySign(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void handleCreation(SignChangeEvent e) {
      e.setLine(0, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")));
      Messages.sendMessage(e.getPlayer(), Messages.signcreate);
   }

   public void handleClick(PlayerInteractEvent e) {
      this.plugin.getGlobalLobby().joinLobby(e.getPlayer());
      e.setCancelled(true);
   }

   public void handleDestroy(BlockBreakEvent e) {
      Messages.sendMessage(e.getPlayer(), Messages.signremove);
   }
}
