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
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Rarity extends SubCmd {
   public Rarity(ItemEditCommand cmd) {
      super("rarity", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length > 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         ItemRarity rarity = args.length == 1 ? null : (ItemRarity)Aliases.RARITY.convertAlias(args[1]);
         if (rarity == null && args.length != 1) {
            this.onWrongAlias("wrong-rarity", p, Aliases.RARITY, new String[0]);
            this.onFail(p, alias);
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         meta.setRarity(rarity);
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var8) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (IAliasSet)Aliases.RARITY) : Collections.emptyList();
   }
}
