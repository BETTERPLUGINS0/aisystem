package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ToolTipStyle extends SubCmd {
   public ToolTipStyle(ItemEditCommand cmd) {
      super("tooltipstyle", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length != 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         String value = args[1].toLowerCase(Locale.ENGLISH);
         if (value.equals("clear")) {
            meta.setTooltipStyle((NamespacedKey)null);
            item.setItemMeta(meta);
            return;
         }

         String pre;
         String post;
         if (!value.contains(":")) {
            pre = "minecraft";
            post = value;
         } else {
            pre = value.split(":")[0];
            post = value.split(":")[1];
         }

         meta.setTooltipStyle(new NamespacedKey(pre, post));
         item.setItemMeta(meta);
      } catch (Exception var10) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], "clear") : Collections.emptyList();
   }
}
