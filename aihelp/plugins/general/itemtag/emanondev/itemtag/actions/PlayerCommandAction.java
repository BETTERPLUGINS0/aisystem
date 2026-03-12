package emanondev.itemtag.actions;

import emanondev.itemedit.UtilsString;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemtag.ItemTag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandAction extends Action {
   public PlayerCommandAction() {
      super("command");
   }

   public void validateInfo(String text) {
      if (text.isEmpty()) {
         throw new IllegalStateException();
      }
   }

   public void execute(Player player, String text) {
      text = UtilsString.fix(text, player, true, new String[]{"%player%", player.getName()});
      if (ItemTag.get().getConfig().loadBoolean("actions.player_command.fires_playercommandpreprocessevent", true)) {
         PlayerCommandPreprocessEvent evt = new PlayerCommandPreprocessEvent(player, text);
         Bukkit.getPluginManager().callEvent(evt);
         if (evt.isCancelled()) {
            return;
         }

         text = evt.getMessage();
      }

      Bukkit.dispatchCommand(player, UtilsString.fix(text, player, true, new String[]{"%player%", player.getName()}));
   }

   public List<String> tabComplete(CommandSender sender, List<String> params) {
      return ((String)params.get(params.size() - 1)).startsWith("%") ? CompleteUtility.complete((String)params.get(params.size() - 1), Collections.singletonList("%player%")) : Collections.emptyList();
   }

   public List<String> getInfo() {
      ArrayList<String> list = new ArrayList();
      list.add("&b" + this.getID() + " &e<command>");
      list.add("&e<command> &bcommand executed by player");
      list.add("&b%player% may be used as placeholder for player name");
      list.add("&bN.B. no &e/&b is required, example: '&ehome&b'");
      return list;
   }
}
