package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class MaxDurability extends SubCmd {
   public MaxDurability(ItemEditCommand cmd) {
      super("maxdurability", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length != 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         if (!(meta instanceof Damageable)) {
            Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
            return;
         }

         int amount = Integer.parseInt(args[1]);
         Damageable damageable = (Damageable)meta;
         damageable.setMaxDamage(amount);
         item.setItemMeta(meta);
      } catch (Exception var9) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (args.length == 2 && sender instanceof Player) {
         ItemStack item = this.getItemInHand((Player)sender);
         if (item != null && item.getType().getMaxDurability() > 1) {
            int max = item.getType().getMaxDurability();
            return CompleteUtility.complete(args[1], "1", String.valueOf(max), String.valueOf(max / 2), String.valueOf(max / 4), String.valueOf(max / 4 * 3));
         }
      }

      return Collections.emptyList();
   }
}
