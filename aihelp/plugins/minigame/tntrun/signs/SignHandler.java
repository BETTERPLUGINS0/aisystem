package tntrun.signs;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;
import tntrun.TNTRun;
import tntrun.messages.Messages;
import tntrun.signs.type.AutoJoinSign;
import tntrun.signs.type.JoinSign;
import tntrun.signs.type.LeaderboardSign;
import tntrun.signs.type.LeaveSign;
import tntrun.signs.type.LobbySign;
import tntrun.signs.type.SignType;
import tntrun.signs.type.VoteSign;
import tntrun.utils.FormattingCodesParser;

public class SignHandler implements Listener {
   private HashMap<String, SignType> signs = new HashMap();
   private TNTRun plugin;

   public SignHandler(TNTRun plugin) {
      this.signs.put("[join]", new JoinSign(plugin));
      this.signs.put("[leave]", new LeaveSign(plugin));
      this.signs.put("[vote]", new VoteSign(plugin));
      this.signs.put("[lobby]", new LobbySign(plugin));
      this.signs.put("[autojoin]", new AutoJoinSign(plugin));
      this.signs.put("[leaderboard]", new LeaderboardSign(plugin));
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onTNTRunSignCreate(SignChangeEvent e) {
      Player player = e.getPlayer();
      if (ChatColor.stripColor(e.getLine(0)).equalsIgnoreCase("[TNTRun]")) {
         if (!player.hasPermission("tntrun.setup")) {
            Messages.sendMessage(player, Messages.nopermission);
            e.setCancelled(true);
            e.getBlock().breakNaturally();
            return;
         }

         String line = e.getLine(1).toLowerCase();
         if (this.signs.containsKey(line)) {
            ((SignType)this.signs.get(line)).handleCreation(e);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onSignClick(PlayerInteractEvent e) {
      if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
         if (e.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign)e.getClickedBlock().getState();
            if (sign.getSide(Side.FRONT).getLine(0).equalsIgnoreCase(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")))) {
               String line = ChatColor.stripColor(sign.getSide(Side.FRONT).getLine(1).toLowerCase());
               if (line.equalsIgnoreCase(ChatColor.stripColor(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.join"))))) {
                  line = "[join]";
               }

               if (this.signs.containsKey(line)) {
                  ((SignType)this.signs.get(line)).handleClick(e);
               }
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onSignDestroy(BlockBreakEvent e) {
      if (e.getBlock().getState() instanceof Sign) {
         Player player = e.getPlayer();
         Sign sign = (Sign)e.getBlock().getState();
         if (sign.getSide(Side.FRONT).getLine(0).equalsIgnoreCase(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")))) {
            if (!player.hasPermission("tntrun.setup")) {
               Messages.sendMessage(player, Messages.nopermission);
               e.setCancelled(true);
               return;
            }

            String line = sign.getSide(Side.FRONT).getLine(1).toLowerCase();
            if (line.equalsIgnoreCase(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.join")))) {
               line = "[join]";
            }

            if (this.signs.containsKey(line)) {
               ((SignType)this.signs.get(line)).handleDestroy(e);
            } else {
               ((SignType)this.signs.get("[leaderboard]")).handleDestroy(e);
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onSignEdit(PlayerSignOpenEvent e) {
      Sign sign = e.getSign();
      if (sign.getSide(Side.FRONT).getLine(0).equalsIgnoreCase(FormattingCodesParser.parseFormattingCodes(this.plugin.getConfig().getString("signs.prefix")))) {
         e.setCancelled(true);
      }

   }
}
