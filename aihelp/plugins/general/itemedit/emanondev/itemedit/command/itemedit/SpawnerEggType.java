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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;

public class SpawnerEggType extends SubCmd {
   public SpawnerEggType(ItemEditCommand cmd) {
      super("spawnereggtype", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      ItemMeta rawMeta = ItemUtils.getMeta(item);
      if (!(rawMeta instanceof SpawnEggMeta)) {
         Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
      } else {
         SpawnEggMeta meta = (SpawnEggMeta)rawMeta;

         try {
            if (args.length != 2) {
               throw new IllegalArgumentException();
            }

            EntityType type = (EntityType)Aliases.EGG_TYPE.convertAlias(args[1]);
            if (type == null) {
               this.onWrongAlias("wrong-entity", p, Aliases.EGG_TYPE, new String[0]);
               this.onFail(p, alias);
               return;
            }

            meta.setSpawnedType(type);
            item.setItemMeta(meta);
            this.updateView(p);
         } catch (Exception var9) {
            this.onFail(p, alias);
         }

      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (IAliasSet)Aliases.EGG_TYPE) : Collections.emptyList();
   }
}
