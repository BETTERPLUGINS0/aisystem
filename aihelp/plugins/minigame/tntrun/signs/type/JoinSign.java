package tntrun.signs.type;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;
import tntrun.utils.Utils;

public class JoinSign implements SignType {
   private TNTRun plugin;

   public JoinSign(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void handleCreation(SignChangeEvent e) {
      String arenaname = ChatColor.stripColor(FormattingCodesParser.parseFormattingCodes(e.getLine(2)));
      Arena arena = this.plugin.amanager.getArenaByName(arenaname);
      if (arena != null) {
         this.plugin.getSignEditor().createJoinSign(e.getBlock(), arenaname);
         Messages.sendMessage(e.getPlayer(), Messages.signcreate);
      } else {
         Messages.sendMessage(e.getPlayer(), Messages.arenanotexist.replace("{ARENA}", arenaname));
         e.setCancelled(true);
         e.getBlock().breakNaturally();
      }

   }

   public void handleClick(PlayerInteractEvent e) {
      Player player = e.getPlayer();
      Sign sign = (Sign)e.getClickedBlock().getState();
      Arena arena = this.plugin.amanager.getArenaByName(ChatColor.stripColor(sign.getSide(Side.FRONT).getLine(2)));
      if (arena == null) {
         Messages.sendMessage(player, Messages.arenanotexist);
         e.getClickedBlock().breakNaturally();
      } else {
         if (!arena.getStatusManager().isArenaRunning()) {
            if (arena.getPlayerHandler().checkJoin(player)) {
               arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoothers);
               this.plugin.getSignEditor().addSign(e.getClickedBlock(), arena.getArenaName());
            }

            e.setCancelled(true);
         } else if (this.plugin.getConfig().getBoolean("signs.allowspectate") && arena.getPlayerHandler().canSpectate(player)) {
            arena.getPlayerHandler().spectatePlayer(player, Messages.playerjoinedasspectator, "");
            if (Utils.debug()) {
               Logger var10000 = this.plugin.getLogger();
               String var10001 = player.getName();
               var10000.info("Player " + var10001 + " joined arena " + arena.getArenaName() + " as a spectator");
            }
         }

      }
   }

   public void handleDestroy(BlockBreakEvent e) {
      Block block = e.getBlock();
      Sign sign = (Sign)block.getState();
      this.plugin.getSignEditor().removeSign(block, ChatColor.stripColor(sign.getSide(Side.FRONT).getLine(2)));
      Messages.sendMessage(e.getPlayer(), Messages.signremove);
   }
}
