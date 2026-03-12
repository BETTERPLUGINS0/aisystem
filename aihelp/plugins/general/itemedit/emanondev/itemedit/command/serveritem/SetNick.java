package emanondev.itemedit.command.serveritem;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.command.ServerItemCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetNick extends SubCmd {
   public SetNick(ServerItemCommand cmd) {
      super("setnick", cmd, false, false);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;

      try {
         if (args.length < 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         if (args.length == 2) {
            ItemEdit.get().getServerStorage().setNick(args[1], (String)null);
         } else {
            StringBuilder builder = new StringBuilder(args[2]);

            for(int i = 3; i < args.length; ++i) {
               builder.append(" ").append(args[i]);
            }

            ItemEdit.get().getServerStorage().setNick(args[1], builder.toString());
         }

         this.sendLanguageString("success", (String)null, p, new String[]{"%id%", args[1].toLowerCase(), "%nick%", ItemEdit.get().getServerStorage().getNick(args[1])});
      } catch (Exception var7) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (!(sender instanceof Player)) {
         return Collections.emptyList();
      } else {
         return args.length == 2 ? CompleteUtility.complete(args[1], (Collection)ItemEdit.get().getServerStorage().getIds()) : Collections.emptyList();
      }
   }
}
