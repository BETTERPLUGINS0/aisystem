package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Amount extends SubCmd {
   public Amount(ItemEditCommand cmd) {
      super("amount", cmd, true, true);
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
            item.setAmount(Math.max(0, item.getAmount() + amount));
         } else {
            if (amount > 127 || amount < 1) {
               throw new IllegalArgumentException("Wrong amount number");
            }

            item.setAmount(amount);
         }

         this.updateView(p);
      } catch (Exception var7) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (Collection)Arrays.asList("1", "10", "64", "100", "127")) : Collections.emptyList();
   }
}
