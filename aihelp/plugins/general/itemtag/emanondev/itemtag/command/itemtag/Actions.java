package emanondev.itemtag.command.itemtag;

import emanondev.itemedit.Util;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.VersionUtils;
import emanondev.itemedit.utility.InventoryUtils.ExcessMode;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.actions.ActionHandler;
import emanondev.itemtag.actions.ActionsUtility;
import emanondev.itemtag.command.ItemTagCommand;
import emanondev.itemtag.command.ListenerSubCmd;
import emanondev.itemtag.gui.ActionsGui;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Actions extends ListenerSubCmd {
   private static final String[] actionsSub = new String[]{"add", "addline", "set", "permission", "cooldown", "cooldownmsg", "cooldownmsgtype", "cooldownid", "uses", "maxuses", "remove", "info", "consume", "visualcooldown", "displayuses"};

   public Actions(ItemTagCommand cmd) {
      super("actions", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (args.length == 1) {
         p.openInventory((new ActionsGui(p, item, alias, this.getName())).getInventory());
      } else {
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
                  var7 = 10;
               }
               break;
            case -546109589:
               if (var6.equals("cooldown")) {
                  var7 = 7;
               }
               break;
            case -517618225:
               if (var6.equals("permission")) {
                  var7 = 11;
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
                  var7 = 14;
               }
               break;
            case 3599308:
               if (var6.equals("uses")) {
                  var7 = 4;
               }
               break;
            case 185459766:
               if (var6.equals("cooldownmsg")) {
                  var7 = 8;
               }
               break;
            case 845102896:
               if (var6.equals("maxuses")) {
                  var7 = 5;
               }
               break;
            case 951516156:
               if (var6.equals("consume")) {
                  var7 = 6;
               }
               break;
            case 1286301808:
               if (var6.equals("cooldownmsgtype")) {
                  var7 = 9;
               }
               break;
            case 1715327886:
               if (var6.equals("displayuses")) {
                  var7 = 13;
               }
               break;
            case 1836142251:
               if (var6.equals("visualcooldown")) {
                  var7 = 12;
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
               this.uses(p, alias, args, item);
               return;
            case 5:
               this.maxUses(p, alias, args, item);
               return;
            case 6:
               this.consume(p, alias, args, item);
               return;
            case 7:
               this.cooldown(p, alias, args, item);
               return;
            case 8:
               this.cooldownMsg(p, alias, args, item);
               return;
            case 9:
               this.cooldownMsgType(p, alias, args, item);
               return;
            case 10:
               this.cooldownId(p, alias, args, item);
               return;
            case 11:
               this.permission(p, alias, args, item);
               return;
            case 12:
               this.visualCooldown(p, alias, args, item);
               return;
            case 13:
               this.displayUses(p, alias, args, item);
            case 14:
               p.openInventory((new ActionsGui(p, item, alias, this.getName())).getInventory());
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

   private void displayUses(Player p, String label, String[] args, ItemStack item) {
      TagItem tagItem = ItemTag.getTagItem(item);
      boolean value = args.length >= 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !ActionsUtility.getDisplayUses(tagItem);
      ActionsUtility.setDisplayUses(tagItem, value);
      ActionsUtility.updateUsesDisplay(item);
      if (value) {
         this.sendLanguageString("displayuses.feedback.set", "", p, new String[0]);
         ActionsUtility.updateUsesDisplay(item);
      } else {
         this.sendLanguageString("displayuses.feedback.unset", "", p, new String[0]);
         ActionsUtility.updateUsesDisplay(item);
      }

   }

   private void visualCooldown(Player sender, String label, String[] args, ItemStack item) {
      TagItem tagItem = ItemTag.getTagItem(item);
      boolean value = args.length >= 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !ActionsUtility.getVisualCooldown(tagItem);
      ActionsUtility.setVisualCooldown(tagItem, value);
      if (value) {
         this.sendLanguageString("visualcooldown.feedback.set", "", sender, new String[0]);
      } else {
         this.sendLanguageString("visualcooldown.feedback.unset", "", sender, new String[0]);
      }

   }

   private void consume(Player sender, String label, String[] args, ItemStack item) {
      TagItem tagItem = ItemTag.getTagItem(item);
      boolean value = args.length >= 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !ActionsUtility.getConsume(tagItem);
      ActionsUtility.setConsume(tagItem, value);
      if (value) {
         this.sendLanguageString("consume.feedback.set", "", sender, new String[0]);
      } else {
         this.sendLanguageString("consume.feedback.unset", "", sender, new String[0]);
      }

   }

   private void permission(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length > 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         String permission = args.length == 2 ? null : args[2].toLowerCase(Locale.ENGLISH);
         TagItem tagItem = ItemTag.getTagItem(item);
         ActionsUtility.setPermission(tagItem, permission);
         if (permission != null) {
            this.sendLanguageString("permission.feedback.set", "", p, new String[]{"%permission%", permission});
         } else {
            this.sendLanguageString("permission.feedback.removed", "", p, new String[0]);
         }
      } catch (Exception var7) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("permission.params", (String)null, p, new String[0]), this.getLanguageStringList("permission.description", (List)null, p, new String[0])));
      }

   }

   private void cooldownId(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length > 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         String cooldownId = args.length == 2 ? null : args[2].toLowerCase(Locale.ENGLISH);
         TagItem tagItem = ItemTag.getTagItem(item);
         ActionsUtility.setCooldownId(tagItem, cooldownId);
         if (cooldownId != null) {
            this.sendLanguageString("cooldownid.feedback.set", "", p, new String[]{"%id%", cooldownId});
         } else {
            this.sendLanguageString("cooldownid.feedback.removed", "", p, new String[0]);
         }
      } catch (Exception var7) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("cooldownid.params", (String)null, p, new String[0]), this.getLanguageStringList("cooldownid.description", (List)null, p, new String[0])));
      }

   }

   private void cooldownMsg(Player p, String label, String[] args, ItemStack item) {
      try {
         String cooldownMsg = args.length == 2 ? null : String.join(" ", (CharSequence[])Arrays.copyOfRange(args, 2, args.length));
         TagItem tagItem = ItemTag.getTagItem(item);
         ActionsUtility.setCooldownMsg(tagItem, cooldownMsg);
         if (cooldownMsg != null) {
            this.sendLanguageString("cooldownmsg.feedback.set", "", p, new String[]{"%msg%", cooldownMsg});
         } else {
            this.sendLanguageString("cooldownmsg.feedback.removed", "", p, new String[0]);
         }
      } catch (Exception var7) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("cooldownmsg.params", (String)null, p, new String[0]), this.getLanguageStringList("cooldownmsg.description", (List)null, p, new String[0])));
      }

   }

   private void cooldownMsgType(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length != 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         String type = args[2].toLowerCase(Locale.ENGLISH);
         byte var7 = -1;
         switch(type.hashCode()) {
         case 3052376:
            if (type.equals("chat")) {
               var7 = 1;
            }
            break;
         case 198298141:
            if (type.equals("actionbar")) {
               var7 = 0;
            }
         }

         switch(var7) {
         case 0:
         case 1:
            TagItem tagItem = ItemTag.getTagItem(item);
            ActionsUtility.setCooldownMsgType(tagItem, type);
            this.sendLanguageString("cooldownmsgtype.feedback.set", "", p, new String[]{"%type%", type});
            break;
         default:
            throw new IllegalArgumentException("Wrong type");
         }
      } catch (Exception var8) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("cooldownmsgtype.params", (String)null, p, new String[0]), this.getLanguageStringList("cooldownmsgtype.description", (List)null, p, new String[0])));
      }

   }

   private void cooldown(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length > 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int cooldownMs = args.length == 2 ? 0 : Integer.parseInt(args[2]);
         TagItem tagItem = ItemTag.getTagItem(item);
         ActionsUtility.setCooldownMs(tagItem, cooldownMs);
         if (cooldownMs > 0) {
            this.sendLanguageString("cooldown.feedback.set", "", p, new String[]{"%cooldown_ms%", String.valueOf(cooldownMs), "%cooldown_seconds%", String.valueOf(cooldownMs / 1000)});
         } else {
            this.sendLanguageString("cooldown.feedback.removed", "", p, new String[0]);
         }
      } catch (Exception var7) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("cooldown.params", (String)null, p, new String[0]), this.getLanguageStringList("cooldown.description", (List)null, p, new String[0])));
      }

   }

   private void maxUses(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length > 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int uses = args.length == 2 ? 1 : Integer.parseInt(args[2]);
         if (uses == 0) {
            throw new IllegalArgumentException();
         }

         TagItem tagItem = ItemTag.getTagItem(item);
         ActionsUtility.setMaxUses(tagItem, uses);
         if (ActionsUtility.getDisplayUses(tagItem)) {
            ActionsUtility.updateUsesDisplay(item);
         }

         if (uses < 0) {
            this.sendLanguageString("maxuses.feedback.unlimited", "", p, new String[0]);
         } else {
            this.sendLanguageString("maxuses.feedback.set", "", p, new String[]{"%uses%", String.valueOf(uses)});
         }
      } catch (Exception var7) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("maxuses.params", (String)null, p, new String[0]), this.getLanguageStringList("maxuses.description", (List)null, p, new String[0])));
      }

   }

   private void uses(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length > 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int uses = args.length == 2 ? 1 : Integer.parseInt(args[2]);
         if (uses == 0) {
            throw new IllegalArgumentException();
         }

         TagItem tagItem = ItemTag.getTagItem(item);
         ActionsUtility.setUses(tagItem, uses);
         if (ActionsUtility.getDisplayUses(tagItem)) {
            ActionsUtility.updateUsesDisplay(item);
         }

         if (uses < 0) {
            this.sendLanguageString("uses.feedback.unlimited", "", p, new String[0]);
         } else {
            this.sendLanguageString("uses.feedback.set", "", p, new String[]{"%uses%", String.valueOf(uses)});
         }
      } catch (Exception var7) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("uses.params", (String)null, p, new String[0]), this.getLanguageStringList("uses.description", (List)null, p, new String[0])));
      }

   }

   private void invalidAction(Player p, String actionError) {
      String msg = this.getLanguageString("invalid-action.message", "", p, new String[]{"%error%", actionError});
      if (msg != null && !msg.isEmpty()) {
         StringBuilder hover = (new StringBuilder(this.getLanguageString("invalid-action.hover-pre", "", p, new String[0]))).append("\n");
         String color1 = this.getLanguageString("invalid-action.first_color", "", p, new String[0]);
         String color2 = this.getLanguageString("invalid-action.second_color", "", p, new String[0]);
         boolean color = true;
         int counter = 0;
         Iterator var9 = ActionHandler.getTypes().iterator();

         while(var9.hasNext()) {
            String actionType = (String)var9.next();
            counter += actionType.length() + 1;
            hover.append(color ? color1 : color2).append(actionType);
            color = !color;
            if (counter > 30) {
               counter = 0;
               hover.append("\n");
            } else {
               hover.append(" ");
            }
         }

         Util.sendMessage(p, (new ComponentBuilder(msg)).event(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder(hover.toString())).create())).create());
      }
   }

   private void invalidActionInfo(Player p, String actionType, String actionInfo) {
      String msg = this.getLanguageString("invalid-actioninfo.message", "", p, new String[]{"%error%", actionInfo, "%action%", actionType});
      if (msg != null && !msg.isEmpty()) {
         Util.sendMessage(p, (new ComponentBuilder(msg)).event(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder(String.join("\n", UtilsString.fix(ActionHandler.getAction(actionType).getInfo(), p, true, new String[0])))).create())).create());
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

         String actionType = args[3].toLowerCase(Locale.ENGLISH);
         String actionInfo = String.join(" ", Arrays.asList(args).subList(4, args.length));
         String originalAction = String.join(" ", Arrays.asList(args).subList(3, args.length));

         try {
            ActionHandler.validateActionType(actionType);
         } catch (Exception var13) {
            this.invalidAction(p, actionType);
            return;
         }

         try {
            ActionHandler.validateActionInfo(actionType, actionInfo);
         } catch (Exception var12) {
            this.invalidActionInfo(p, actionType, actionInfo);
            return;
         }

         String action = actionType + "%%:%%" + actionInfo;
         TagItem tagItem = ItemTag.getTagItem(item);
         if (!ActionsUtility.hasActions(tagItem)) {
            ActionsUtility.setActions(tagItem, Collections.singletonList(action));
         } else {
            List<String> list = new ArrayList(ActionsUtility.getActions(tagItem));
            list.set(line, action);
            ActionsUtility.setActions(tagItem, list);
         }

         this.sendLanguageString("set.feedback", "", p, new String[]{"%line%", String.valueOf(line + 1), "%action%", originalAction});
      } catch (Exception var14) {
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
         if (!ActionsUtility.hasActions(tagItem)) {
            throw new IllegalArgumentException();
         }

         List<String> list = new ArrayList(ActionsUtility.getActions(tagItem));
         String action = (String)list.remove(line);
         ActionsUtility.setActions(tagItem, list);
         this.sendLanguageString("remove.feedback", "", p, new String[]{"%line%", String.valueOf(line + 1), "%action%", action.split("%%:%%")[0]});
      } catch (Exception var9) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("remove.params", (String)null, p, new String[0]), this.getLanguageStringList("remove.description", (List)null, p, new String[0])));
      }

   }

   private void add(Player p, String label, String[] args, ItemStack item) {
      try {
         if (args.length < 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         String actionType = args[2].toLowerCase(Locale.ENGLISH);
         String actionInfo = String.join(" ", Arrays.asList(args).subList(3, args.length));
         String originalAction = String.join(" ", Arrays.asList(args).subList(2, args.length));

         try {
            ActionHandler.validateActionType(actionType);
         } catch (Exception var12) {
            this.invalidAction(p, actionType);
            return;
         }

         try {
            ActionHandler.validateActionInfo(actionType, actionInfo);
         } catch (Exception var11) {
            this.invalidActionInfo(p, actionType, actionInfo);
            return;
         }

         actionInfo = ActionHandler.fixActionInfo(actionType, actionInfo);
         String action = actionType + "%%:%%" + actionInfo;
         TagItem tagItem = ItemTag.getTagItem(item);
         if (!ActionsUtility.hasActions(tagItem)) {
            ActionsUtility.setActions(tagItem, Collections.singletonList(action));
         } else {
            List<String> list = new ArrayList(ActionsUtility.getActions(tagItem));
            list.add(action);
            ActionsUtility.setActions(tagItem, list);
         }

         this.sendLanguageString("add.feedback", "", p, new String[]{"%action%", originalAction});
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
         String actionType = args[3].toLowerCase(Locale.ENGLISH);
         String actionInfo = String.join(" ", Arrays.asList(args).subList(4, args.length));
         String originalAction = String.join(" ", Arrays.asList(args).subList(3, args.length));

         try {
            ActionHandler.validateActionType(actionType);
         } catch (Exception var13) {
            this.invalidAction(p, actionType);
            return;
         }

         try {
            ActionHandler.validateActionInfo(actionType, actionInfo);
         } catch (Exception var12) {
            this.invalidActionInfo(p, actionType, actionInfo);
            return;
         }

         String action = actionType + "%%:%%" + actionInfo;
         TagItem tagItem = ItemTag.getTagItem(item);
         if (!ActionsUtility.hasActions(tagItem)) {
            ActionsUtility.setActions(tagItem, Collections.singletonList(action));
         } else {
            List<String> list = new ArrayList(ActionsUtility.getActions(tagItem));
            list.add(line, action);
            ActionsUtility.setActions(tagItem, list);
         }

         this.sendLanguageString("addline.feedback", "", p, new String[]{"%action%", originalAction, "%line%", String.valueOf(line + 1)});
      } catch (Exception var14) {
         Util.sendMessage(p, this.craftFailFeedback(label, this.getLanguageString("addline.params", (String)null, p, new String[0]), this.getLanguageStringList("addline.description", (List)null, p, new String[0])));
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      String var3;
      byte var4;
      switch(args.length) {
      case 2:
         return CompleteUtility.complete(args[1], actionsSub);
      case 3:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case 96417:
            if (var3.equals("add")) {
               var4 = 0;
            }
            break;
         case 3599308:
            if (var3.equals("uses")) {
               var4 = 1;
            }
            break;
         case 845102896:
            if (var3.equals("maxuses")) {
               var4 = 2;
            }
            break;
         case 951516156:
            if (var3.equals("consume")) {
               var4 = 4;
            }
            break;
         case 1286301808:
            if (var3.equals("cooldownmsgtype")) {
               var4 = 6;
            }
            break;
         case 1715327886:
            if (var3.equals("displayuses")) {
               var4 = 5;
            }
            break;
         case 1836142251:
            if (var3.equals("visualcooldown")) {
               var4 = 3;
            }
         }

         switch(var4) {
         case 0:
            return CompleteUtility.complete(args[2], ActionHandler.getTypes());
         case 1:
         case 2:
            return CompleteUtility.complete(args[2], Arrays.asList("-1", "1", "5", "10"));
         case 3:
         case 4:
         case 5:
            return CompleteUtility.complete(args[2], Aliases.BOOLEAN);
         case 6:
            return CompleteUtility.complete(args[2], VersionUtils.isVersionAfter(1, 11) ? Arrays.asList("chat", "actionbar") : Collections.singletonList("chat"));
         default:
            return Collections.emptyList();
         }
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
            return ActionHandler.tabComplete(sender, args[2].toLowerCase(Locale.ENGLISH), new ArrayList(Arrays.asList(args).subList(3, args.length)));
         case 1:
         case 2:
            return CompleteUtility.complete(args[3], ActionHandler.getTypes());
         default:
            return Collections.emptyList();
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
            return ActionHandler.tabComplete(sender, args[2].toLowerCase(Locale.ENGLISH), new ArrayList(Arrays.asList(args).subList(3, args.length)));
         case 1:
         case 2:
            return ActionHandler.tabComplete(sender, args[3].toLowerCase(Locale.ENGLISH), new ArrayList(Arrays.asList(args).subList(4, args.length)));
         default:
            return Collections.emptyList();
         }
      }
   }

   @EventHandler
   private void event(PlayerInteractEvent event) {
      switch(event.getAction()) {
      case RIGHT_CLICK_AIR:
      case RIGHT_CLICK_BLOCK:
         ItemStack item = event.getItem();
         TagItem tagItem = ItemTag.getTagItem(item);
         if (!ActionsUtility.hasActions(tagItem)) {
            return;
         } else {
            String permission = ActionsUtility.getPermission(tagItem);
            if (permission != null && !event.getPlayer().hasPermission(permission)) {
               return;
            } else {
               long cooldown = (long)ActionsUtility.getCooldownMs(tagItem);
               String type;
               if (cooldown > 0L) {
                  String cooldownId = ActionsUtility.getCooldownId(tagItem);
                  if (ItemTag.get().getCooldownAPI().hasCooldown(event.getPlayer(), cooldownId)) {
                     String msg = ActionsUtility.getCooldownMsg(tagItem);
                     if (msg == null) {
                        return;
                     }

                     type = ActionsUtility.getCooldownMsgType(tagItem);
                     msg = UtilsString.fix(msg, event.getPlayer(), true, new String[0]);
                     byte var11 = -1;
                     switch(type.hashCode()) {
                     case 3052376:
                        if (type.equals("chat")) {
                           var11 = 0;
                        }
                        break;
                     case 198298141:
                        if (type.equals("actionbar")) {
                           var11 = 1;
                        }
                     }

                     switch(var11) {
                     case 0:
                        Util.sendMessage(event.getPlayer(), msg);
                        break;
                     case 1:
                        if (VersionUtils.isVersionAfter(1, 11, 2)) {
                           event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
                        } else {
                           ItemTag.get().log("Invalid action cooldown message type &e" + type + "&f not available on this server version");
                        }
                        break;
                     default:
                        ItemTag.get().log("Invalid action cooldown message type &e" + type);
                     }

                     return;
                  }

                  ItemTag.get().getCooldownAPI().setCooldown(event.getPlayer(), cooldownId, cooldown, TimeUnit.MILLISECONDS);
                  if (ActionsUtility.getVisualCooldown(tagItem)) {
                     event.getPlayer().setCooldown(item.getType(), (int)(cooldown / 50L));
                  }
               }

               int uses = ActionsUtility.getUses(tagItem);
               if (uses == 0) {
                  return;
               } else {
                  Iterator var8 = ActionsUtility.getActions(tagItem).iterator();

                  while(var8.hasNext()) {
                     type = (String)var8.next();

                     try {
                        if (!type.isEmpty()) {
                           ActionHandler.handleAction(event.getPlayer(), type.split("%%:%%")[0], type.split("%%:%%")[1]);
                        }
                     } catch (Exception var14) {
                        var14.printStackTrace();
                     }
                  }

                  event.setCancelled(true);
                  if (uses > 0) {
                     ItemStack clone;
                     if (uses == 1 && ActionsUtility.getConsume(tagItem)) {
                        if (event.getItem().getAmount() == 1) {
                           try {
                              if (event.getHand() == EquipmentSlot.HAND) {
                                 event.getPlayer().getInventory().setItemInMainHand((ItemStack)null);
                              } else {
                                 event.getPlayer().getInventory().setItemInOffHand((ItemStack)null);
                              }
                           } catch (Error var13) {
                              event.getPlayer().getInventory().setItemInHand((ItemStack)null);
                           }
                        } else {
                           clone = event.getItem().clone();
                           clone.setAmount(clone.getAmount() - 1);

                           try {
                              if (event.getHand() == EquipmentSlot.HAND) {
                                 event.getPlayer().getInventory().setItemInMainHand(clone);
                              } else {
                                 event.getPlayer().getInventory().setItemInOffHand(clone);
                              }
                           } catch (Error var12) {
                              event.getPlayer().getInventory().setItemInHand(clone);
                           }
                        }
                     } else {
                        try {
                           if (event.getItem().getAmount() == 1) {
                              ActionsUtility.setUses(tagItem, uses - 1);
                              if (ActionsUtility.getDisplayUses(tagItem)) {
                                 ActionsUtility.updateUsesDisplay(item);
                              }
                           } else {
                              clone = event.getItem();
                              clone.setAmount(clone.getAmount() - 1);
                              if (event.getHand() == EquipmentSlot.HAND) {
                                 event.getPlayer().getInventory().setItemInMainHand(clone);
                              } else {
                                 event.getPlayer().getInventory().setItemInOffHand(clone);
                              }

                              ActionsUtility.setUses(ItemTag.getTagItem(clone), uses - 1);
                              if (ActionsUtility.getDisplayUses(tagItem)) {
                                 ActionsUtility.updateUsesDisplay(clone);
                              }

                              InventoryUtils.giveAmount(event.getPlayer(), clone, 1, ExcessMode.DROP_EXCESS);
                           }
                        } catch (Throwable var15) {
                           if (event.getItem().getAmount() == 1) {
                              ActionsUtility.setUses(tagItem, uses - 1);
                           } else {
                              ItemStack clone = event.getItem();
                              clone.setAmount(clone.getAmount() - 1);
                              event.getPlayer().getInventory().setItemInHand(clone);
                              ActionsUtility.setUses(ItemTag.getTagItem(clone), uses - 1);
                              InventoryUtils.giveAmount(event.getPlayer(), clone, 1, ExcessMode.DROP_EXCESS);
                           }
                        }
                     }
                  }
               }
            }
         }
      default:
      }
   }
}
