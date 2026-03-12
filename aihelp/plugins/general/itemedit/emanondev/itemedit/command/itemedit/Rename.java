package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.Util;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Rename extends SubCmd {
   private final Map<UUID, String> copies = new HashMap();
   private int lengthLimit = this.getPlugin().getConfig().getInt("blocked.rename-length-limit", 120);

   public Rename(@NotNull ItemEditCommand cmd) {
      super("rename", cmd, true, true);
   }

   public void reload() {
      super.reload();
      this.lengthLimit = this.getPlugin().getConfig().getInt("blocked.rename-length-limit", 120);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (Util.isAllowedRenameItem(sender, item.getType())) {
         ItemMeta itemMeta = ItemUtils.getMeta(item);
         if (args.length == 1) {
            itemMeta.setDisplayName(" ");
            item.setItemMeta(itemMeta);
            this.updateView(p);
         } else if (args.length == 2 && args[1].equalsIgnoreCase("-clear")) {
            itemMeta.setDisplayName((String)null);
            item.setItemMeta(itemMeta);
            this.updateView(p);
         } else if (args.length == 2 && args[1].equalsIgnoreCase("-copy")) {
            this.copies.put(p.getUniqueId(), itemMeta.getDisplayName());
            this.updateView(p);
         } else if (args.length == 2 && args[1].equalsIgnoreCase("-paste")) {
            itemMeta.setDisplayName((String)this.copies.get(p.getUniqueId()));
            item.setItemMeta(itemMeta);
            this.updateView(p);
         } else {
            StringBuilder bname = new StringBuilder(args[1]);

            for(int i = 2; i < args.length; ++i) {
               bname.append(" ").append(args[i]);
            }

            String name = Util.formatText(p, bname.toString(), this.getPermission());
            if (!Util.hasBannedWords(p, name)) {
               if (!this.allowedLengthLimit(p, ChatColor.stripColor(name))) {
                  Util.sendMessage(p, (String)ItemEdit.get().getLanguageConfig(p).loadMessage("blocked-by-rename-length-limit", "", (Player)null, true, "%limit%", String.valueOf(this.lengthLimit)));
               } else {
                  itemMeta.setDisplayName(name);
                  item.setItemMeta(itemMeta);
                  this.updateView(p);
               }
            }
         }
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (!(sender instanceof Player)) {
         return Collections.emptyList();
      } else if (args.length != 2) {
         return Collections.emptyList();
      } else {
         ItemStack item = this.getItemInHand((Player)sender);
         if (item != null && item.hasItemMeta()) {
            ItemMeta meta = ItemUtils.getMeta(item);
            if (meta.hasDisplayName()) {
               return CompleteUtility.complete(args[1], meta.getDisplayName().replace('§', '&'), "-clear", "-paste", "-copy");
            }
         }

         return Collections.emptyList();
      }
   }

   private boolean allowedLengthLimit(Player who, String text) {
      if (this.lengthLimit >= 0 && !who.hasPermission("itemedit.bypass.rename_length_limit")) {
         return text.length() <= this.lengthLimit;
      } else {
         return true;
      }
   }
}
