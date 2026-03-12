package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.util.Collections;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class SkullOwner extends SubCmd {
   public SkullOwner(ItemEditCommand cmd) {
      super("skullowner", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      ItemMeta rawMeta = ItemUtils.getMeta(item);
      if (!(rawMeta instanceof SkullMeta)) {
         Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
      } else if (VersionUtils.isVersionUpTo(1, 12) && item.getDurability() != 3) {
         Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
      } else {
         SkullMeta meta = (SkullMeta)rawMeta;
         meta.setOwner((String)null);
         if (args.length == 1) {
            item.setItemMeta(meta);
            this.updateView(p);
         } else {
            try {
               StringBuilder name = new StringBuilder(args[1]);

               for(int i = 2; i < args.length; ++i) {
                  name.append(" ").append(args[i]);
               }

               name = new StringBuilder(ChatColor.translateAlternateColorCodes('&', name.toString()));
               meta.setOwner(name.toString());
               item.setItemMeta(meta);
               this.updateView(p);
            } catch (Exception var10) {
               this.onFail(p, alias);
            }

         }
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.completePlayers(args[1]) : Collections.emptyList();
   }
}
