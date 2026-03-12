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

public class Update extends SubCmd {
   public Update(ServerItemCommand cmd) {
      super("update", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;

      try {
         if (args.length < 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         if (ItemEdit.get().getServerStorage().getItem(args[1]) == null) {
            throw new IllegalArgumentException();
         }

         ItemEdit.get().getServerStorage().setItem(args[1], this.getItemInHand(p).clone());
      } catch (Exception var6) {
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
