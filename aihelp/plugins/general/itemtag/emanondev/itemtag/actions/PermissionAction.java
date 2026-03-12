package emanondev.itemtag.actions;

import emanondev.itemedit.utility.CompleteUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionAction extends Action {
   public PermissionAction() {
      super("permission");
   }

   public void validateInfo(String text) {
      if (text.isEmpty()) {
         throw new IllegalStateException();
      } else {
         String[] args = text.split(" ");
         if (args.length < 3) {
            throw new IllegalStateException();
         } else {
            ActionHandler.validateActionType(args[1]);
            ActionHandler.validateActionInfo(args[1], text.substring(args[0].length() + args[1].length() + 2));
         }
      }
   }

   public void execute(Player player, String text) {
      String[] args = text.split(" ");
      if (args[0].startsWith("-")) {
         if (player.hasPermission(args[0].substring(1))) {
            return;
         }
      } else if (!player.hasPermission(args[0])) {
         return;
      }

      ActionHandler.handleAction(player, args[1], text.substring(args[0].length() + args[1].length() + 2));
   }

   public List<String> tabComplete(CommandSender sender, List<String> params) {
      switch(params.size()) {
      case 1:
         return Collections.emptyList();
      case 2:
         return CompleteUtility.complete((String)params.get(1), ActionHandler.getTypes());
      default:
         Action sub = ActionHandler.getAction((String)params.get(1));
         if (sub == null) {
            return Collections.emptyList();
         } else {
            params.remove(0);
            params.remove(0);
            return sub.tabComplete(sender, params);
         }
      }
   }

   public String fixActionInfo(String actionInfo) {
      String[] args = actionInfo.split(" ");
      return args[0] + " " + args[1] + " " + ActionHandler.getAction(args[1]).fixActionInfo(actionInfo.substring(args[0].length() + args[1].length() + 2));
   }

   public List<String> getInfo() {
      ArrayList<String> list = new ArrayList();
      list.add("&b" + this.getID() + " &e<permission> <action>");
      list.add("&e<permission> &bpermission required or &e-permission &bfor reversed check");
      list.add("&e<action> &baction to execute, example: '&esound entity_bat_hurt 1 1&b'");
      return list;
   }
}
