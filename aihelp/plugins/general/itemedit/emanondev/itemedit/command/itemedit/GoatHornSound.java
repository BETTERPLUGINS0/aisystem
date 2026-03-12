package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.MusicInstrument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.jetbrains.annotations.NotNull;

public class GoatHornSound extends SubCmd {
   public GoatHornSound(ItemEditCommand cmd) {
      super("goathornsound", cmd, true, true);
      MusicInstrument.values();
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player player = (Player)sender;
      ItemStack item = this.getItemInHand(player);
      if (!(item.getItemMeta() instanceof MusicInstrumentMeta)) {
         Util.sendMessage(player, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
      } else {
         try {
            if (args.length != 2) {
               throw new IllegalArgumentException("Wrong param number");
            }

            MusicInstrumentMeta meta = (MusicInstrumentMeta)ItemUtils.getMeta(item);
            MusicInstrument type = (MusicInstrument)Aliases.GOAT_HORN_SOUND.convertAlias(args[1]);
            if (type == null) {
               this.onWrongAlias("wrong-sound", player, Aliases.GOAT_HORN_SOUND, new String[0]);
               this.onFail(player, alias);
               return;
            }

            meta.setInstrument(type);
            item.setItemMeta(meta);
            this.updateView(player);
         } catch (Exception var8) {
            this.onFail(player, alias);
         }

      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (IAliasSet)Aliases.GOAT_HORN_SOUND) : Collections.emptyList();
   }
}
