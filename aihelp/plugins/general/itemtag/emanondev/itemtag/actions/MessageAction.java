package emanondev.itemtag.actions;

import emanondev.itemedit.UtilsString;
import emanondev.itemedit.utility.CompleteUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageAction extends Action {
   public MessageAction() {
      super("message");
   }

   public void validateInfo(String text) {
      if (text.isEmpty()) {
         throw new IllegalStateException();
      }
   }

   public void execute(Player player, String text) {
      text = UtilsString.fix(text, player, true, new String[]{"%player%", player.getName()});
      player.sendMessage(text);
   }

   public List<String> tabComplete(CommandSender sender, List<String> params) {
      return ((String)params.get(params.size() - 1)).startsWith("%") ? CompleteUtility.complete((String)params.get(params.size() - 1), Collections.singletonList("%player%")) : Collections.emptyList();
   }

   public List<String> getInfo() {
      ArrayList<String> list = new ArrayList();
      list.add("&b" + this.getID() + " &e<message>");
      list.add("&e<message>&b message sent to player");
      list.add("&b%player% may be used as placeholder for player name");
      return list;
   }
}
