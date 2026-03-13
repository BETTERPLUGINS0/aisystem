package tntrun.signs.type;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tntrun.TNTRun;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;

public class AutoJoinSign implements SignType {
   private TNTRun plugin;

   public AutoJoinSign(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void handleCreation(SignChangeEvent e) {
      e.setLine(0, FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")));
      Messages.sendMessage(e.getPlayer(), Messages.signcreate);
   }

   public void handleClick(PlayerInteractEvent e) {
      Sign sign = (Sign)e.getClickedBlock().getState();
      String type = ChatColor.stripColor(sign.getSide(Side.FRONT).getLine(2));
      this.plugin.getMenus().autoJoin(e.getPlayer(), type);
      e.setCancelled(true);
   }

   public void handleDestroy(BlockBreakEvent e) {
      Messages.sendMessage(e.getPlayer(), Messages.signremove);
   }
}
