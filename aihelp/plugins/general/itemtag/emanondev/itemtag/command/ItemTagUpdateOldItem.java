package emanondev.itemtag.command;

import emanondev.itemedit.Util;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.actions.ActionsUtility;
import emanondev.itemtag.command.itemtag.SecurityUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemTagUpdateOldItem implements TabExecutor {
   private final ItemTag plugin = ItemTag.get();
   private final String permission = "itemtag.itemtagupdateolditem";

   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      return Collections.emptyList();
   }

   public void sendPermissionLackMessage(@NotNull String permission, CommandSender sender) {
      Util.sendMessage(sender, this.plugin.getLanguageConfig(sender).loadMessage("lack-permission", "&cYou lack of permission %permission%", sender instanceof Player ? (Player)sender : null, true, new String[]{"%permission%", permission}));
   }

   public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (!sender.hasPermission(this.permission)) {
         this.sendPermissionLackMessage(this.permission, sender);
         return true;
      } else if (!(sender instanceof Player)) {
         this.sendPlayerOnly(sender);
         return false;
      } else {
         Player player = (Player)sender;
         ItemStack item = ItemUtils.getHandItem(player);
         if (ItemUtils.isAirOrNull(item)) {
            this.sendNoItemInHand(sender);
            return false;
         } else {
            TagItem tagItem = ItemTag.getTagItem(item);
            if (!ActionsUtility.hasActions(tagItem)) {
               Util.sendMessage(sender, ChatColor.RED + "[ItemTag] This item doesn't require any update");
               return true;
            } else {
               List<String> actions = new ArrayList(ActionsUtility.getActions(tagItem));
               boolean updating = false;

               for(int i = 0; i < actions.size(); ++i) {
                  String action = (String)actions.get(i);
                  String prefix = null;
                  if (action.startsWith("commandasop%%:%%") && !action.startsWith("commandasop%%:%%-pin")) {
                     prefix = "commandasop%%:%%";
                  } else if (action.startsWith("servercommand%%:%%") && !action.startsWith("servercommand%%:%%-pin")) {
                     prefix = "servercommand%%:%%";
                  }

                  if (prefix != null) {
                     Util.sendMessage(sender, ChatColor.GREEN + "[ItemTag] Updated Action " + ChatColor.YELLOW + prefix.substring(0, prefix.length() - 5) + " " + action.substring(prefix.length()));
                     actions.set(i, prefix + "-pin" + SecurityUtil.generateControlKey(action.substring(prefix.length())) + " " + action.substring(prefix.length()));
                     updating = true;
                  }
               }

               if (updating) {
                  ActionsUtility.setActions(tagItem, actions);
                  ItemUtils.setHandItem(player, tagItem.getItem());
                  Util.sendMessage(sender, ChatColor.GREEN + "[ItemTag] Item was updated");
                  return true;
               } else {
                  Util.sendMessage(sender, ChatColor.RED + "[ItemTag] This item doesn't require any update");
                  return true;
               }
            }
         }
      }
   }

   public void sendPlayerOnly(CommandSender sender) {
      Util.sendMessage(sender, this.plugin.getLanguageConfig(sender).loadMessage("player-only", "&cCommand for Players only", sender instanceof Player ? (Player)sender : null, true, new String[0]));
   }

   public void sendNoItemInHand(CommandSender sender) {
      Util.sendMessage(sender, this.plugin.getLanguageConfig(sender).loadMessage("no-item-on-hand", "&cYou need to hold an item in hand", sender instanceof Player ? (Player)sender : null, true, new String[0]));
   }
}
