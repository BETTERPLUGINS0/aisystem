package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Glow extends SubCmd {
   public Glow(ItemEditCommand cmd) {
      super("glow", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length > 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         Boolean value = args.length == 1 ? (meta.hasEnchantmentGlintOverride() ? !meta.getEnchantmentGlintOverride() : Boolean.TRUE) : (Boolean)Aliases.BOOLEAN.convertAlias(args[1]);
         meta.setEnchantmentGlintOverride(value);
         item.setItemMeta(meta);
      } catch (Exception var8) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (args.length == 2) {
         List<String> list = CompleteUtility.complete(args[1], (IAliasSet)Aliases.BOOLEAN);
         if ("default".startsWith(args[1].toLowerCase(Locale.ENGLISH))) {
            list.add("default");
         }

         return list;
      } else {
         return Collections.emptyList();
      }
   }
}
