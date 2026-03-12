package emanondev.itemedit.command.itemstorage;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.command.ItemStorageCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Update extends SubCmd {
   public Update(ItemStorageCommand cmd) {
      super("update", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;

      try {
         if (args.length != 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         if (ItemEdit.get().getPlayerStorage().getItem(p, args[1]) == null) {
            throw new IllegalArgumentException();
         }

         ItemEdit.get().getPlayerStorage().setItem(p, args[1], this.getItemInHand(p).clone());
         this.sendLanguageString("success", (String)null, p, new String[]{"%id%", args[1].toLowerCase(Locale.ENGLISH)});
      } catch (Exception var6) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (!(sender instanceof Player)) {
         return Collections.emptyList();
      } else {
         return args.length == 2 ? CompleteUtility.complete(args[1], (Collection)ItemEdit.get().getPlayerStorage().getIds((Player)sender)) : Collections.emptyList();
      }
   }
}
