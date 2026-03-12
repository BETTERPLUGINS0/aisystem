package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

public class BookAuthor extends SubCmd {
   public BookAuthor(ItemEditCommand cmd) {
      super("bookauthor", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (item.getType() != Material.WRITTEN_BOOK) {
         Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
      } else {
         BookMeta meta = (BookMeta)ItemUtils.getMeta(item);
         if (args.length == 1) {
            meta.setAuthor((String)null);
            item.setItemMeta(meta);
            this.updateView(p);
         } else {
            try {
               StringBuilder name = new StringBuilder(args[1]);

               for(int i = 2; i < args.length; ++i) {
                  name.append(" ").append(args[i]);
               }

               meta.setAuthor(UtilsString.fix((String)name.toString(), (Player)null, true));
               item.setItemMeta(meta);
               this.updateView(p);
            } catch (Exception var9) {
               this.onFail(p, alias);
            }

         }
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.completePlayers(args[1]) : Collections.emptyList();
   }
}
