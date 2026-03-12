package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
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
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.NotNull;

public class Compass extends SubCmd {
   private static final String[] compassSub = new String[]{"clear", "set"};

   public Compass(ItemEditCommand cmd) {
      super("compass", cmd, true, true);
   }

   public void reload() {
      super.reload();
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (!(item.getItemMeta() instanceof CompassMeta)) {
         Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
      } else if (args.length == 1) {
         this.onFail(p, alias);
      } else {
         String var6 = args[1].toLowerCase(Locale.ENGLISH);
         byte var7 = -1;
         switch(var6.hashCode()) {
         case 113762:
            if (var6.equals("set")) {
               var7 = 0;
            }
            break;
         case 94746189:
            if (var6.equals("clear")) {
               var7 = 1;
            }
         }

         switch(var7) {
         case 0:
            this.compassSet(p, item, args);
            return;
         case 1:
            this.compassClear(p, item, args);
            return;
         default:
            this.onFail(p, alias);
         }
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], compassSub) : Collections.emptyList();
   }

   private void compassSet(Player p, ItemStack item, String[] args) {
      CompassMeta meta = (CompassMeta)ItemUtils.getMeta(item);
      meta.setLodestoneTracked(false);
      meta.setLodestone(p.getLocation());
      item.setItemMeta(meta);
      this.getLanguageString("set.feedback", (String)null, p, new String[]{"%world%", p.getLocation().getWorld().getName(), "%x%", String.valueOf(p.getLocation().getBlockX()), "%y%", String.valueOf(p.getLocation().getBlockY()), "%z%", String.valueOf(p.getLocation().getBlockZ())});
      this.updateView(p);
   }

   private void compassClear(Player p, ItemStack item, String[] args) {
      CompassMeta meta = (CompassMeta)ItemUtils.getMeta(item);
      meta.setLodestoneTracked(true);
      meta.setLodestone(p.getLocation());
      item.setItemMeta(meta);
      Util.sendMessage(p, (String)this.getLanguageString("clear.feedback", (String)null, p, new String[0]));
      this.updateView(p);
   }
}
