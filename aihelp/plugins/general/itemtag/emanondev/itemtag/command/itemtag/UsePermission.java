package emanondev.itemtag.command.itemtag;

import emanondev.itemedit.CooldownAPI;
import emanondev.itemedit.Util;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.command.AbstractCommand;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.command.ListenerSubCmd;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UsePermission extends ListenerSubCmd {
   private static final String USE_KEY;
   private static final String USEMSG_KEY;

   public UsePermission(AbstractCommand cmd) {
      super("usepermission", cmd, true, true);
   }

   public static void setUseKey(@NotNull TagItem item, @Nullable String value) {
      if (value != null && !value.isEmpty()) {
         item.setTag(USE_KEY, value);
      } else {
         item.removeTag(USE_KEY);
      }

   }

   public static void setUseMsgKey(@NotNull TagItem item, @Nullable String value) {
      if (value != null && !value.isEmpty()) {
         item.setTag(USEMSG_KEY, value);
      } else {
         item.removeTag(USEMSG_KEY);
      }

   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      if (args.length == 1) {
         this.onFail(p, alias);
      } else {
         ItemStack item = this.getItemInHand(p);
         TagItem tagItem = ItemTag.getTagItem(item);
         String var7 = args[1].toLowerCase(Locale.ENGLISH);
         byte var8 = -1;
         switch(var7.hashCode()) {
         case -2034700367:
            if (var7.equals("setpermission")) {
               var8 = 0;
            }
            break;
         case -2031029915:
            if (var7.equals("setmessage")) {
               var8 = 1;
            }
         }

         String permission;
         String feedback;
         switch(var8) {
         case 0:
            if (args.length > 3) {
               this.onFail(p, alias);
               return;
            }

            permission = args.length == 2 ? null : args[2].toLowerCase(Locale.ENGLISH);
            setUseKey(tagItem, permission);
            if (permission != null) {
               feedback = this.getLanguageString("setpermission.feedback", (String)null, p, new String[]{"%value%", permission});
            } else {
               feedback = this.getLanguageString("setpermission.feedback-reset", (String)null, p, new String[0]);
            }

            Util.sendMessage(p, feedback);
            return;
         case 1:
            if (args.length == 2) {
               setUseMsgKey(tagItem, (String)null);
               permission = this.getLanguageString("setmessage.feedback-reset", (String)null, p, new String[0]);
               Util.sendMessage(p, permission);
               return;
            }

            permission = UtilsString.fix(String.join(" ", Arrays.asList(args).subList(2, args.length)), (Player)null, true, new String[0]);
            setUseMsgKey(tagItem, permission);
            feedback = this.getLanguageString("setmessage.feedback", (String)null, p, new String[]{"%value%", permission});
            Util.sendMessage(p, feedback);
            return;
         default:
         }
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return args.length == 2 ? CompleteUtility.complete(args[1], new String[]{"setpermission", "setmessage"}) : Collections.emptyList();
   }

   @EventHandler
   private void event(PlayerInteractEvent event) {
      if (event.getAction() != Action.PHYSICAL) {
         TagItem tagItem = ItemTag.getTagItem(event.getItem());
         if (tagItem.hasStringTag(USE_KEY)) {
            String perm = tagItem.getString(USE_KEY);
            if (!event.getPlayer().hasPermission(perm)) {
               event.setUseItemInHand(Result.DENY);
               CooldownAPI cooldownAPI = this.getPlugin().getCooldownAPI();
               String cooldownKey = "useperm_" + perm.replace(".", "_");
               if (!cooldownAPI.hasCooldown(event.getPlayer(), cooldownKey)) {
                  cooldownAPI.setCooldown(event.getPlayer(), cooldownKey, 1L, TimeUnit.SECONDS);
                  if (tagItem.hasStringTag(USEMSG_KEY)) {
                     Util.sendMessage(event.getPlayer(), UtilsString.fix(tagItem.getString(USEMSG_KEY), event.getPlayer(), true, new String[]{"%permission%", perm}));
                  } else {
                     this.getCommand().sendPermissionLackMessage(perm, event.getPlayer());
                  }
               }
            }
         }
      }
   }

   static {
      USE_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":useperm";
      USEMSG_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":usepermmsg";
   }
}
