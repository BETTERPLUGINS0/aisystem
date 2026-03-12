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
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;

public class RepairCost extends SubCmd {
   public RepairCost(ItemEditCommand cmd) {
      super("repaircost", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length > 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         if (!sender.hasPermission(this.getPermission() + ".without_durability") && item.getType().getMaxDurability() <= 1) {
            this.getCommand().sendPermissionLackMessage(this.getPermission() + ".without_durability", sender);
            return;
         }

         Repairable meta = (Repairable)ItemUtils.getMeta(item);
         meta.setRepairCost(Integer.parseInt(args[1]));
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var7) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (Collection)Arrays.asList("0", "1", "3", "7", "30", "40")) : Collections.emptyList();
   }
}
