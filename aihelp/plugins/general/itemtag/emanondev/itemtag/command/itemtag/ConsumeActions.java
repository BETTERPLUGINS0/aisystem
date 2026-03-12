package emanondev.itemtag.command.itemtag;

import emanondev.itemedit.Util;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.actions.ActionHandler;
import emanondev.itemtag.command.ItemTagCommand;
import emanondev.itemtag.command.ListenerSubCmd;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ConsumeActions extends ListenerSubCmd {
   private static final String ACTIONS_KEY;
   private static final String ACTION_COOLDOWN_KEY;
   private static final String ACTION_COOLDOWN_ID_KEY;
   private static final String ACTION_PERMISSION_KEY;
   private static final String TYPE_SEPARATOR = "%%:%%";
   private static final String[] actionsSub;

   public ConsumeActions(ItemTagCommand cmd) {
      super("consumeactions", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      if (args.length == 1) {
         this.onFail(p, alias);
      } else {
         ItemStack item = this.getItemInHand(p);

         try {
            String var6 = args[1].toLowerCase(Locale.ENGLISH);
            byte var7 = -1;
            switch(var6.hashCode()) {
            case -1147867115:
               if (var6.equals("addline")) {
                  var7 = 1;
               }
               break;
            case -934610812:
               if (var6.equals("remove")) {
                  var7 = 2;
               }
               break;
            case -825301562:
               if (var6.equals("cooldownid")) {
                  var7 = 5;
               }
               break;
            case -546109589:
               if (var6.equals("cooldown")) {
                  var7 = 4;
               }
               break;
            case -517618225:
               if (var6.equals("permission")) {
                  var7 = 6;
               }
               break;
            case 96417:
               if (var6.equals("add")) {
                  var7 = 0;
               }
               break;
            case 113762:
               if (var6.equals("set")) {
                  var7 = 3;
               }
               break;
            case 3237038:
               if (var6.equals("info")) {
                  var7 = 7;
               }
            }

            switch(var7) {
            case 0:
               this.add(p, alias, args, item);
               return;
            case 1:
               this.addLine(p, alias, args, item);
               return;
            case 2:
               this.remove(p, alias, args, item);
               return;
            case 3:
               this.set(p, alias, args, item);
               return;
            case 4:
               this.cooldown(p, alias, args, item);
               return;
            case 5:
               this.cooldownId(p, alias, args, item);
               return;
            case 6:
               this.permission(p, alias, args, item);
               return;
            case 7:
               this.info(p, alias, args, item);
               return;
            default:
               this.onFail(p, alias);
            }
         } catch (Exception var8) {
            var8.printStackTrace();
            this.onFail(p, alias);
         }

      }
   }

   private void info(Player sender, String label, String[] args, ItemStack item) {
      TagItem tagItem = ItemTag.getTagItem(item);
      if (!tagItem.hasStringTag(ACTIONS_KEY)) {
         Util.sendMessage(sender, ChatColor.translateAlternateColorCodes('&', "&cThis item has no consume actions binded"));
      } else {
         ArrayList<String> list = new ArrayList();
         list.add("&b&lItemTag Consume Actions Info");
         String permission = tagItem.hasStringTag(ACTION_PERMISSION_KEY) ? tagItem.getString(ACTION_PERMISSION_KEY) : null;
         if (permission != null) {
            list.add("&bTo use this item &e" + permission + "&b permission is required");
         } else {
            list.add("&bTo use this item no permission is required");
         }

         long cooldown = (long)tagItem.getInteger(ACTION_COOLDOWN_KEY, 0);
         String cooldownId = tagItem.getString(ACTION_COOLDOWN_ID_KEY, "default");
         if (cooldown > 0L) {
            list.add("&bUsing this item multiple times apply a cooldown of &e" + cooldown / 1000L + " &bseconds, cooldown ID is &e" + cooldownId);
         }

         list.add("&bExecuted actions are:");
         List<String> actions = tagItem.getStringList(ACTIONS_KEY);
         Iterator var12 = actions.iterator();

         while(var12.hasNext()) {
            String action = (String)var12.next();
            list.add("&b- &6" + action.split("%%:%%")[0] + " &e" + action.split("%%:%%")[1]);
         }

         Util.sendMessage(sender, ChatColor.translateAlternateColorCodes('&', String.join("\n", list)));
      }
   }

   private void permission(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length > 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         String permission = args.length == 2 ? null : args[2].toLowerCase(Locale.ENGLISH);
         if (permission != null) {
            ItemTag.getTagItem(item).setTag(ACTION_PERMISSION_KEY, permission);
            this.sendLanguageString("feedback.actions.permission.set", (String)null, p, new String[]{"%permission%", permission});
         } else {
            ItemTag.getTagItem(item).removeTag(ACTION_PERMISSION_KEY);
            this.sendLanguageString("feedback.actions.permission.removed", (String)null, p, new String[0]);
         }
      } catch (Exception var6) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("permission.params", (String)null, p, new String[0]), this.getLanguageStringList("permission.description", (List)null, p, new String[0])));
      }

   }

   private void cooldownId(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length > 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         String cooldownId = args.length == 2 ? null : args[2].toLowerCase(Locale.ENGLISH);
         if (cooldownId != null) {
            ItemTag.getTagItem(item).setTag(ACTION_COOLDOWN_ID_KEY, cooldownId);
            this.sendLanguageString("feedback.actions.cooldownid.set", (String)null, p, new String[]{"%id%", cooldownId});
         } else {
            ItemTag.getTagItem(item).removeTag(ACTION_COOLDOWN_ID_KEY);
            this.sendLanguageString("feedback.actions.cooldownid.removed", (String)null, p, new String[0]);
         }
      } catch (Exception var6) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("cooldownid.params", (String)null, p, new String[0]), this.getLanguageStringList("cooldownid.description", (List)null, p, new String[0])));
      }

   }

   private void cooldown(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length > 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int cooldownMs = args.length == 2 ? 0 : Integer.parseInt(args[2]);
         if (cooldownMs > 0) {
            ItemTag.getTagItem(item).setTag(ACTION_COOLDOWN_KEY, cooldownMs);
            this.sendLanguageString("feedback.actions.cooldown.set", (String)null, p, new String[]{"%cooldown_ms%", String.valueOf(cooldownMs), "%cooldown_seconds%", String.valueOf(cooldownMs / 1000)});
         } else {
            ItemTag.getTagItem(item).removeTag(ACTION_COOLDOWN_KEY);
            this.sendLanguageString("feedback.actions.cooldown.removed", (String)null, p, new String[0]);
         }
      } catch (Exception var6) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("cooldown.params", (String)null, p, new String[0]), this.getLanguageStringList("cooldown.description", (List)null, p, new String[0])));
      }

   }

   private void set(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length < 4) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int line = Integer.parseInt(args[2]) - 1;
         if (line < 0) {
            throw new IllegalArgumentException();
         }

         ArrayList<String> tmp = new ArrayList(Arrays.asList(args).subList(4, args.length));
         String actionType = args[3].toLowerCase(Locale.ENGLISH);
         String actionInfo = String.join(" ", (CharSequence[])tmp.toArray(new String[0]));

         try {
            ActionHandler.validateActionType(actionType);
         } catch (Exception var12) {
            Util.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&c'&6" + actionType + "&c' is not a valid type\n&cValid types &e" + String.join("&c, &e", ActionHandler.getTypes())));
            return;
         }

         try {
            ActionHandler.validateActionInfo(actionType, actionInfo);
         } catch (Exception var11) {
            Util.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&c'&6" + actionInfo + "&c' is not a valid info for &6" + actionType + "\n" + String.join("\n", ActionHandler.getAction(actionType).getInfo())));
            return;
         }

         String action = actionType + "%%:%%" + actionInfo;
         if (!ItemTag.getTagItem(item).hasStringTag(ACTIONS_KEY)) {
            ItemTag.getTagItem(item).setTag(ACTIONS_KEY, action);
         } else {
            List<String> list = new ArrayList(ItemTag.getTagItem(item).getStringList(ACTIONS_KEY, Collections.emptyList()));
            list.set(line, action);
            ItemTag.getTagItem(item).setTag(ACTIONS_KEY, (List)list);
         }

         this.sendLanguageString("feedback.actions.set", (String)null, p, new String[]{"%line%", String.valueOf(line + 1), "%action%", action.replace("%%:%%", " ")});
      } catch (Exception var13) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("set.params", (String)null, p, new String[0]), this.getLanguageStringList("set.description", (List)null, p, new String[0])));
      }

   }

   private void remove(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length != 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int line = Integer.parseInt(args[2]) - 1;
         if (line < 0) {
            throw new IllegalArgumentException();
         }

         TagItem tagItem = ItemTag.getTagItem(item);
         if (!tagItem.hasStringTag(ACTIONS_KEY)) {
            throw new IllegalArgumentException();
         }

         List<String> list = new ArrayList(tagItem.getStringList(ACTIONS_KEY));
         String action = (String)list.remove(line);
         tagItem.setTag(ACTIONS_KEY, (List)list);
         this.sendLanguageString("feedback.actions.remove", (String)null, p, new String[]{"%line%", String.valueOf(line + 1), "%action%", action.replace("%%:%%", " ")});
      } catch (Exception var9) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("remove.params", (String)null, p, new String[0]), this.getLanguageStringList("remove.description", (List)null, p, new String[0])));
      }

   }

   private void add(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length < 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         ArrayList<String> tmp = new ArrayList(Arrays.asList(args).subList(3, args.length));
         String actionType = args[2].toLowerCase(Locale.ENGLISH);
         String actionInfo = String.join(" ", (CharSequence[])tmp.toArray(new String[0]));

         try {
            ActionHandler.validateActionType(actionType);
         } catch (Exception var12) {
            Util.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&c'&6" + actionType + "&c' is not a valid type\n&cValid types &e" + String.join("&c, &e", ActionHandler.getTypes())));
            return;
         }

         try {
            ActionHandler.validateActionInfo(actionType, actionInfo);
         } catch (Exception var11) {
            Util.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&c'&6" + actionInfo + "&c' is not a valid info for &6" + actionType + "\n" + String.join("\n", ActionHandler.getAction(actionType).getInfo())));
            return;
         }

         String action = actionType + "%%:%%" + actionInfo;
         TagItem tagItem = ItemTag.getTagItem(item);
         if (!tagItem.hasStringListTag(ACTIONS_KEY)) {
            tagItem.setTag(ACTIONS_KEY, Collections.singletonList(action));
         } else {
            List<String> list = new ArrayList(tagItem.getStringList(ACTIONS_KEY));
            list.add(action);
            tagItem.setTag(ACTIONS_KEY, (List)list);
         }

         this.sendLanguageString("feedback.actions.add", (String)null, p, new String[]{"%action%", action.replace("%%:%%", " ")});
      } catch (Exception var13) {
         var13.printStackTrace();
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("add.params", (String)null, p, new String[0]), this.getLanguageStringList("add.description", (List)null, p, new String[0])));
      }

   }

   private void addLine(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length < 4) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int line = Integer.parseInt(args[2]) - 1;
         ArrayList<String> tmp = new ArrayList(Arrays.asList(args).subList(4, args.length));
         String actionType = args[3].toLowerCase(Locale.ENGLISH);
         String actionInfo = String.join(" ", (CharSequence[])tmp.toArray(new String[0]));

         try {
            ActionHandler.validateActionType(actionType);
         } catch (Exception var13) {
            Util.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&c'&6" + actionType + "&c' is not a valid type\n&cValid types &e" + String.join("&c, &e", ActionHandler.getTypes())));
            return;
         }

         try {
            ActionHandler.validateActionInfo(actionType, actionInfo);
         } catch (Exception var12) {
            Util.sendMessage(p, ChatColor.translateAlternateColorCodes('&', "&c'&6" + actionInfo + "&c' is not a valid info for &6" + actionType + "\n" + String.join("\n", ActionHandler.getAction(actionType).getInfo())));
            return;
         }

         String action = actionType + "%%:%%" + actionInfo;
         TagItem tagItem = ItemTag.getTagItem(item);
         if (!tagItem.hasStringTag(ACTIONS_KEY)) {
            tagItem.setTag(ACTIONS_KEY, Collections.singletonList(action));
         } else {
            List<String> list = new ArrayList(tagItem.getStringList(ACTIONS_KEY));
            list.add(line, action);
            tagItem.setTag(ACTIONS_KEY, (List)list);
         }

         this.sendLanguageString("feedback.actions.add", (String)null, p, new String[]{"%action%", action.replace("%%:%%", " ")});
      } catch (Exception var14) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("addline.params", (String)null, p, new String[0]), this.getLanguageStringList("addline.description", (List)null, p, new String[0])));
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      String var3;
      byte var4;
      ArrayList params;
      switch(args.length) {
      case 2:
         return CompleteUtility.complete(args[1], actionsSub);
      case 3:
         if ("add".equalsIgnoreCase(args[1])) {
            return CompleteUtility.complete(args[2], ActionHandler.getTypes());
         }

         return new ArrayList();
      case 4:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case -1147867115:
            if (var3.equals("addline")) {
               var4 = 2;
            }
            break;
         case 96417:
            if (var3.equals("add")) {
               var4 = 0;
            }
            break;
         case 113762:
            if (var3.equals("set")) {
               var4 = 1;
            }
         }

         switch(var4) {
         case 0:
            params = new ArrayList(Arrays.asList(args).subList(3, args.length));
            return ActionHandler.tabComplete(sender, args[2].toLowerCase(Locale.ENGLISH), params);
         case 1:
         case 2:
            return CompleteUtility.complete(args[3], ActionHandler.getTypes());
         }
      default:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case -1147867115:
            if (var3.equals("addline")) {
               var4 = 2;
            }
            break;
         case 96417:
            if (var3.equals("add")) {
               var4 = 0;
            }
            break;
         case 113762:
            if (var3.equals("set")) {
               var4 = 1;
            }
         }

         switch(var4) {
         case 0:
            params = new ArrayList(Arrays.asList(args).subList(3, args.length));
            return ActionHandler.tabComplete(sender, args[2].toLowerCase(Locale.ENGLISH), params);
         case 1:
         case 2:
            params = new ArrayList(Arrays.asList(args).subList(4, args.length));
            return ActionHandler.tabComplete(sender, args[2].toLowerCase(Locale.ENGLISH), params);
         default:
            return Collections.emptyList();
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true
   )
   private void event(PlayerItemConsumeEvent event) {
      TagItem tagItem = ItemTag.getTagItem(event.getItem());
      if (tagItem.hasStringTag(ACTIONS_KEY)) {
         String permission = tagItem.hasStringTag(ACTION_PERMISSION_KEY) ? tagItem.getString(ACTION_PERMISSION_KEY) : null;
         if (permission == null || event.getPlayer().hasPermission(permission)) {
            long cooldown = tagItem.hasIntegerTag(ACTION_COOLDOWN_KEY) ? (long)tagItem.getInteger(ACTION_COOLDOWN_KEY) : 0L;
            if (cooldown > 0L) {
               String cooldownId = tagItem.hasStringTag(ACTION_COOLDOWN_ID_KEY) ? tagItem.getString(ACTION_COOLDOWN_ID_KEY) : "default";
               if (ItemTag.get().getCooldownAPI().hasCooldown(event.getPlayer(), cooldownId)) {
                  event.setCancelled(true);
                  return;
               }

               ItemTag.get().getCooldownAPI().setCooldown(event.getPlayer(), cooldownId, cooldown, TimeUnit.MILLISECONDS);
            }

            Iterator var10 = tagItem.getStringList(ACTIONS_KEY).iterator();

            while(var10.hasNext()) {
               String action = (String)var10.next();

               try {
                  if (!action.isEmpty()) {
                     ActionHandler.handleAction(event.getPlayer(), action.split("%%:%%")[0], action.split("%%:%%")[1]);
                  }
               } catch (Exception var9) {
                  var9.printStackTrace();
               }
            }

         }
      }
   }

   static {
      ACTIONS_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":consume_actions";
      ACTION_COOLDOWN_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":consume_cooldown";
      ACTION_COOLDOWN_ID_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":consume_cooldown_id";
      ACTION_PERMISSION_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":consume_permission";
      actionsSub = new String[]{"add", "addline", "set", "permission", "cooldown", "cooldownid", "remove", "info"};
   }
}
