package emanondev.itemedit.command.itemstorage;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.command.ItemStorageCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.InventoryUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Get extends SubCmd {
   public Get(ItemStorageCommand cmd) {
      super("get", cmd, true, false);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;

      try {
         if (args.length != 2 && args.length != 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int amount = 1;
         if (args.length == 3) {
            amount = Integer.parseInt(args[2]);
         }

         if (amount < 1) {
            throw new IllegalArgumentException("Wrong amount number");
         }

         ItemStack item = ItemEdit.get().getPlayerStorage().getItem(p, args[1]);
         int given = InventoryUtils.giveAmount(p, item, amount, InventoryUtils.ExcessMode.DELETE_EXCESS);
         if (given == 0) {
            this.sendLanguageString("no-inventory-space", (String)null, p, new String[0]);
         } else {
            this.sendLanguageString("success", (String)null, p, new String[]{"%id%", args[1].toLowerCase(Locale.ENGLISH), "%amount%", String.valueOf(given)});
         }
      } catch (Exception var8) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (!(sender instanceof Player)) {
         return new ArrayList();
      } else {
         switch(args.length) {
         case 2:
            return CompleteUtility.complete(args[1], (Collection)ItemEdit.get().getPlayerStorage().getIds((Player)sender));
         case 3:
            return CompleteUtility.complete(args[2], (Collection)Arrays.asList("1", "10", "64", "576", "2304"));
         default:
            return Collections.emptyList();
         }
      }
   }
}
