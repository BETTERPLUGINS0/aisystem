package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
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
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.jetbrains.annotations.NotNull;

public class BookType extends SubCmd {
   public BookType(@NotNull ItemEditCommand cmd) {
      super("booktype", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (item.getType() != Material.WRITTEN_BOOK) {
         Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
      } else {
         try {
            BookMeta itemMeta = (BookMeta)ItemUtils.getMeta(item);
            if (args.length == 1) {
               itemMeta.setGeneration((Generation)null);
               item.setItemMeta(itemMeta);
               this.updateView(p);
               return;
            }

            if (args.length != 2) {
               throw new IllegalArgumentException();
            }

            Generation type = (Generation)Aliases.BOOK_TYPE.convertAlias(args[1]);
            if (type == null) {
               this.onWrongAlias("wrong-generation", p, Aliases.BOOK_TYPE, new String[0]);
               this.onFail(p, alias);
               return;
            }

            itemMeta.setGeneration(type);
            item.setItemMeta(itemMeta);
            this.updateView(p);
         } catch (Exception var8) {
            this.onFail(p, alias);
         }

      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (IAliasSet)Aliases.BOOK_TYPE) : Collections.emptyList();
   }
}
