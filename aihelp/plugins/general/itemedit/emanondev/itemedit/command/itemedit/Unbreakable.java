package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Unbreakable extends SubCmd {
   public Unbreakable(ItemEditCommand cmd) {
      super("unbreakable", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (args.length > 2) {
         this.onFail(p, alias);
      } else {
         try {
            ItemMeta meta = ItemUtils.getMeta(item);
            boolean value = args.length == 2 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[1]) : !ItemUtils.isUnbreakable(meta);
            ItemUtils.setUnbreakable(meta, value);
            item.setItemMeta(meta);
            this.updateView(p);
         } catch (Exception var8) {
            this.onFail(p, alias);
         }

      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (IAliasSet)Aliases.BOOLEAN) : Collections.emptyList();
   }
}
