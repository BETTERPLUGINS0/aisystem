package emanondev.itemedit.command.itemedit;

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
import org.jetbrains.annotations.NotNull;

public class Type extends SubCmd {
   public Type(ItemEditCommand cmd) {
      super("type", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length != 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         Material mat = Material.valueOf(args[1].toUpperCase());
         if (mat == Material.AIR) {
            throw new IllegalArgumentException();
         }

         item.setType(mat);
         this.updateView(p);
      } catch (Exception var7) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], Material.class, ItemUtils::isItem) : Collections.emptyList();
   }
}
