package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CustomModelData extends SubCmd {
   public CustomModelData(ItemEditCommand cmd) {
      super("custommodeldata", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length != 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int amount = Integer.parseInt(args[1]);
         if (amount < 0) {
            throw new IllegalArgumentException("Wrong model value");
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         meta.setCustomModelData(amount);
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var8) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (Collection)Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9")) : Collections.emptyList();
   }
}
