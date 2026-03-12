package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemModel extends SubCmd {
   public ItemModel(ItemEditCommand cmd) {
      super("itemmodel", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length == 1) {
            ItemMeta meta = ItemUtils.getMeta(item);
            meta.setItemModel((NamespacedKey)null);
            item.setItemMeta(meta);
            this.updateView(p);
            return;
         }

         if (args.length != 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         String[] rawKey = args[1].toLowerCase(Locale.ENGLISH).split(":");
         NamespacedKey key = rawKey.length == 1 ? new NamespacedKey("minecraft", rawKey[0]) : new NamespacedKey(rawKey[0], rawKey[1]);
         ItemMeta meta = ItemUtils.getMeta(item);
         meta.setItemModel(key);
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var9) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], (Iterable)Registry.ITEM.stream().collect(Collectors.toList()), args[1].contains(":") ? (type) -> {
         return type.getKey().toString();
      } : (type) -> {
         return type.getKey().getKey();
      }) : Collections.emptyList();
   }
}
