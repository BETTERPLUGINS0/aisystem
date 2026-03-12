package emanondev.itemtag.command.itemtag;

import emanondev.itemedit.Util;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemtag.activity.ActionManager;
import emanondev.itemtag.activity.Activity;
import emanondev.itemtag.activity.ActivityManager;
import emanondev.itemtag.activity.ConditionManager;
import emanondev.itemtag.activity.ConditionType;
import emanondev.itemtag.command.ItemTagCommand;
import emanondev.itemtag.gui.ActivityGui;
import emanondev.itemtag.gui.TriggerGui;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActivitySubCommand extends SubCmd {
   public ActivitySubCommand(@NotNull ItemTagCommand cmd) {
      super("activity", cmd, true, false);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String label, String[] args) {
      Player player = (Player)sender;
      if (args.length == 1) {
         this.help(player, label, args);
      } else {
         String var5 = args[1].toLowerCase(Locale.ENGLISH);
         byte var6 = -1;
         switch(var5.hashCode()) {
         case -2035413680:
            if (var5.equals("setnoconsumesaction")) {
               var6 = 20;
            }
            break;
         case -1383198689:
            if (var5.equals("removealternativeaction")) {
               var6 = 17;
            }
            break;
         case -1369979903:
            if (var5.equals("setalternativeaction")) {
               var6 = 16;
            }
            break;
         case -1352294148:
            if (var5.equals("create")) {
               var6 = 0;
            }
            break;
         case -1335458389:
            if (var5.equals("delete")) {
               var6 = 1;
            }
            break;
         case -1325891847:
            if (var5.equals("setcondition")) {
               var6 = 8;
            }
            break;
         case -934594754:
            if (var5.equals("rename")) {
               var6 = 3;
            }
            break;
         case -753678057:
            if (var5.equals("removecondition")) {
               var6 = 9;
            }
            break;
         case -502945150:
            if (var5.equals("insertcondition")) {
               var6 = 7;
            }
            break;
         case -468253766:
            if (var5.equals("removeaction")) {
               var6 = 13;
            }
            break;
         case -418675289:
            if (var5.equals("insertnoconsumesaction")) {
               var6 = 19;
            }
            break;
         case 3417674:
            if (var5.equals("open")) {
               var6 = 4;
            }
            break;
         case 94756189:
            if (var5.equals("clone")) {
               var6 = 2;
            }
            break;
         case 386021911:
            if (var5.equals("addaction")) {
               var6 = 10;
            }
            break;
         case 596559218:
            if (var5.equals("removenoconsumesaction")) {
               var6 = 21;
            }
            break;
         case 974579992:
            if (var5.equals("setaction")) {
               var6 = 12;
            }
            break;
         case 1099131791:
            if (var5.equals("addnoconsumesaction")) {
               var6 = 18;
            }
            break;
         case 1139249711:
            if (var5.equals("insertaction")) {
               var6 = 11;
            }
            break;
         case 1291786650:
            if (var5.equals("addcondition")) {
               var6 = 6;
            }
            break;
         case 1311649186:
            if (var5.equals("addalternativeaction")) {
               var6 = 14;
            }
            break;
         case 1356906073:
            if (var5.equals("setconsumes")) {
               var6 = 5;
            }
            break;
         case 1504269962:
            if (var5.equals("insertalternativeaction")) {
               var6 = 15;
            }
         }

         switch(var6) {
         case 0:
            this.create(player, label, args);
            return;
         case 1:
            this.delete(player, label, args);
            return;
         case 2:
            this.clone(player, label, args);
            return;
         case 3:
            this.rename(player, label, args);
            return;
         case 4:
            this.open(player, label, args);
            return;
         case 5:
            this.setconsumes(player, label, args);
            return;
         case 6:
            this.addcondition(player, label, args);
            return;
         case 7:
            this.insertcondition(player, label, args);
            return;
         case 8:
            this.setcondition(player, label, args);
            return;
         case 9:
            this.removecondition(player, label, args);
            return;
         case 10:
            this.addaction(player, label, args);
            return;
         case 11:
            this.insertaction(player, label, args);
            return;
         case 12:
            this.setaction(player, label, args);
            return;
         case 13:
            this.removeaction(player, label, args);
            return;
         case 14:
            this.addalternativeaction(player, label, args);
            return;
         case 15:
            this.insertalternativeaction(player, label, args);
            return;
         case 16:
            this.setalternativeaction(player, label, args);
            return;
         case 17:
            this.removealternativeaction(player, label, args);
            return;
         case 18:
            this.addnoconsumesaction(player, label, args);
            return;
         case 19:
            this.insertnoconsumesaction(player, label, args);
            return;
         case 20:
            this.setnoconsumesaction(player, label, args);
            return;
         case 21:
            this.removenoconsumesaction(player, label, args);
            return;
         default:
         }
      }
   }

   private void help(Player player, String label, String[] args) {
   }

   private void create(Player player, String label, String[] args) {
      if (args.length != 3) {
         Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("create.params", (String)null, player, new String[0]), this.getLanguageStringList("create.description", (List)null, player, new String[0])));
      } else {
         Activity activity = ActivityManager.getActivity(args[2]);
         if (activity != null) {
            this.sendLanguageString("feedback.already_used_activity_id", (String)null, player, new String[]{"%id%", args[2]});
         } else {
            activity = new Activity(args[2]);
            ActivityManager.registerActivity(activity);
            this.sendLanguageString("create.feedback", (String)null, player, new String[]{"%id%", args[2]});
         }
      }
   }

   private void rename(Player player, String label, String[] args) {
      if (args.length != 4) {
         Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("rename.params", (String)null, player, new String[0]), this.getLanguageStringList("rename.description", (List)null, player, new String[0])));
      } else {
         Activity activity = ActivityManager.getActivity(args[2]);
         if (activity == null) {
            this.sendLanguageString("feedback.invalid_activity_id", (String)null, player, new String[]{"%id%", args[2]});
         } else {
            Activity newActivity = ActivityManager.getActivity(args[3]);
            if (newActivity != null) {
               this.sendLanguageString("feedback.already_used_activity_id", (String)null, player, new String[]{"%id%", args[3]});
            } else {
               ActivityManager.rename(activity, args[3]);
               this.sendLanguageString("rename.feedback", (String)null, player, new String[]{"%id%", args[2], "%new_id%", args[3]});
            }
         }
      }
   }

   private void open(Player player, String label, String[] args) {
      if (args.length != 3) {
         Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("open.params", (String)null, player, new String[0]), this.getLanguageStringList("open.description", (List)null, player, new String[0])));
      } else {
         Activity activity = ActivityManager.getActivity(args[2]);
         if (activity == null) {
            this.sendLanguageString("feedback.invalid_activity_id", (String)null, player, new String[]{"%id%", args[2]});
         } else {
            player.openInventory((new ActivityGui(activity, player, (TriggerGui)null)).getInventory());
         }
      }
   }

   private void delete(Player player, String label, String[] args) {
      if (args.length != 3) {
         Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("delete.params", (String)null, player, new String[0]), this.getLanguageStringList("delete.description", (List)null, player, new String[0])));
      } else {
         Activity activity = ActivityManager.getActivity(args[2]);
         if (activity == null) {
            this.sendLanguageString("feedback.invalid_activity_id", (String)null, player, new String[]{"%id%", args[2]});
         } else {
            ActivityManager.deleteActivity(activity);
            this.sendLanguageString("delete.feedback", (String)null, player, new String[]{"%id%", args[2]});
         }
      }
   }

   private void clone(Player player, String label, String[] args) {
      if (args.length != 4) {
         Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("clone.params", (String)null, player, new String[0]), this.getLanguageStringList("clone.description", (List)null, player, new String[0])));
      } else {
         Activity activity = ActivityManager.getActivity(args[2]);
         if (activity == null) {
            this.sendLanguageString("feedback.invalid_activity_id", (String)null, player, new String[]{"%id%", args[2]});
         } else {
            Activity newActivity = ActivityManager.getActivity(args[3]);
            if (newActivity != null) {
               this.sendLanguageString("feedback.already_used_activity_id", (String)null, player, new String[]{"%id%", args[3]});
            } else {
               ActivityManager.clone(activity, args[3]);
               this.sendLanguageString("clone.feedback", (String)null, player, new String[]{"%id%", args[2], "%clone_id%", args[3]});
            }
         }
      }
   }

   private void setconsumes(Player player, String label, String[] args) {
      if (args.length != 4) {
         Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("setconsumes.params", (String)null, player, new String[0]), this.getLanguageStringList("setconsumes.description", (List)null, player, new String[0])));
      } else {
         Activity activity = ActivityManager.getActivity(args[2]);
         if (activity == null) {
            this.sendLanguageString("feedback.invalid_activity_id", (String)null, player, new String[]{"%id%", args[2]});
         } else {
            int amount;
            try {
               amount = Integer.parseInt(args[3]);
            } catch (Exception var7) {
               Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("setconsumes.params", (String)null, player, new String[0]), this.getLanguageStringList("setconsumes.description", (List)null, player, new String[0])));
               return;
            }

            amount = Math.max(0, amount);
            activity.setConsumes(amount);
            this.sendLanguageString("setconsumes.feedback", (String)null, player, new String[]{"%id%", args[2], "%amount%", String.valueOf(amount)});
         }
      }
   }

   private void addcondition(Player player, String label, String[] args) {
      if (args.length < 4) {
         Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("addcondition.params", (String)null, player, new String[0]), this.getLanguageStringList("addcondition.description", (List)null, player, new String[0])));
      } else {
         Activity activity = ActivityManager.getActivity(args[2]);
         if (activity == null) {
            this.sendLanguageString("feedback.invalid_activity_id", (String)null, player, new String[]{"%id%", args[2]});
         } else {
            ConditionType type = ConditionManager.getConditionType(args[3]);
            if (type == null) {
               Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("addcondition.params", (String)null, player, new String[0]), this.getLanguageStringList("addcondition.description", (List)null, player, new String[0])));
            } else {
               StringBuilder b = new StringBuilder(args[3]);

               for(int i = 4; i < args.length; ++i) {
                  b.append(" ").append(args[i]);
               }

               ConditionType.Condition condition;
               try {
                  condition = ConditionManager.read(b.toString());
               } catch (Exception var9) {
                  Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("addcondition.params", (String)null, player, new String[0]), this.getLanguageStringList("addcondition.description", (List)null, player, new String[0])));
                  return;
               }

               if (condition == null) {
                  Util.sendMessage(player, this.craftFailFeedback(label, this.getLanguageString("addcondition.params", (String)null, player, new String[0]), this.getLanguageStringList("addcondition.description", (List)null, player, new String[0])));
               } else {
                  activity.addCondition(condition);
                  this.sendLanguageString("addcondition.feedback", (String)null, player, new String[]{"%id%", args[2], "%condition%", condition.toString()});
               }
            }
         }
      }
   }

   private void insertcondition(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void setcondition(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void removecondition(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void addaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void insertaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void setaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void removeaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void addalternativeaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void insertalternativeaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void setalternativeaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void removealternativeaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void addnoconsumesaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void insertnoconsumesaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void setnoconsumesaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   private void removenoconsumesaction(Player player, String label, String[] args) {
      player.sendMessage("Not implemented yet, use the file! plugins/ItemTag/activity/config.yml");
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      String var3;
      byte var4;
      switch(args.length) {
      case 2:
         return CompleteUtility.complete(args[1], new String[]{"create", "delete", "clone", "rename", "open", "setconsumes", "addcondition", "insertcondition", "setcondition", "removecondition", "addaction", "insertaction", "setaction", "removeaction", "addalternativeaction", "insertalternativeaction", "setalternativeaction", "removealternativeaction", "addnoconsumesaction", "insertnoconsumesaction", "setnoconsumesaction", "removenoconsumesaction"});
      case 3:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case -2035413680:
            if (var3.equals("setnoconsumesaction")) {
               var4 = 19;
            }
            break;
         case -1383198689:
            if (var3.equals("removealternativeaction")) {
               var4 = 16;
            }
            break;
         case -1369979903:
            if (var3.equals("setalternativeaction")) {
               var4 = 15;
            }
            break;
         case -1335458389:
            if (var3.equals("delete")) {
               var4 = 0;
            }
            break;
         case -1325891847:
            if (var3.equals("setcondition")) {
               var4 = 7;
            }
            break;
         case -934594754:
            if (var3.equals("rename")) {
               var4 = 2;
            }
            break;
         case -753678057:
            if (var3.equals("removecondition")) {
               var4 = 8;
            }
            break;
         case -502945150:
            if (var3.equals("insertcondition")) {
               var4 = 6;
            }
            break;
         case -468253766:
            if (var3.equals("removeaction")) {
               var4 = 12;
            }
            break;
         case -418675289:
            if (var3.equals("insertnoconsumesaction")) {
               var4 = 18;
            }
            break;
         case 3417674:
            if (var3.equals("open")) {
               var4 = 3;
            }
            break;
         case 94756189:
            if (var3.equals("clone")) {
               var4 = 1;
            }
            break;
         case 386021911:
            if (var3.equals("addaction")) {
               var4 = 9;
            }
            break;
         case 596559218:
            if (var3.equals("removenoconsumesaction")) {
               var4 = 20;
            }
            break;
         case 974579992:
            if (var3.equals("setaction")) {
               var4 = 11;
            }
            break;
         case 1099131791:
            if (var3.equals("addnoconsumesaction")) {
               var4 = 17;
            }
            break;
         case 1139249711:
            if (var3.equals("insertaction")) {
               var4 = 10;
            }
            break;
         case 1291786650:
            if (var3.equals("addcondition")) {
               var4 = 5;
            }
            break;
         case 1311649186:
            if (var3.equals("addalternativeaction")) {
               var4 = 13;
            }
            break;
         case 1356906073:
            if (var3.equals("setconsumes")) {
               var4 = 4;
            }
            break;
         case 1504269962:
            if (var3.equals("insertalternativeaction")) {
               var4 = 14;
            }
         }

         switch(var4) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
            return CompleteUtility.complete(args[1], ActivityManager.getActivityIds());
         default:
            return Collections.emptyList();
         }
      case 4:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case -2035413680:
            if (var3.equals("setnoconsumesaction")) {
               var4 = 11;
            }
            break;
         case -1383198689:
            if (var3.equals("removealternativeaction")) {
               var4 = 9;
            }
            break;
         case -1369979903:
            if (var3.equals("setalternativeaction")) {
               var4 = 8;
            }
            break;
         case -1325891847:
            if (var3.equals("setcondition")) {
               var4 = 2;
            }
            break;
         case -753678057:
            if (var3.equals("removecondition")) {
               var4 = 3;
            }
            break;
         case -502945150:
            if (var3.equals("insertcondition")) {
               var4 = 1;
            }
            break;
         case -468253766:
            if (var3.equals("removeaction")) {
               var4 = 6;
            }
            break;
         case -418675289:
            if (var3.equals("insertnoconsumesaction")) {
               var4 = 10;
            }
            break;
         case 386021911:
            if (var3.equals("addaction")) {
               var4 = 14;
            }
            break;
         case 596559218:
            if (var3.equals("removenoconsumesaction")) {
               var4 = 12;
            }
            break;
         case 974579992:
            if (var3.equals("setaction")) {
               var4 = 5;
            }
            break;
         case 1099131791:
            if (var3.equals("addnoconsumesaction")) {
               var4 = 16;
            }
            break;
         case 1139249711:
            if (var3.equals("insertaction")) {
               var4 = 4;
            }
            break;
         case 1291786650:
            if (var3.equals("addcondition")) {
               var4 = 13;
            }
            break;
         case 1311649186:
            if (var3.equals("addalternativeaction")) {
               var4 = 15;
            }
            break;
         case 1356906073:
            if (var3.equals("setconsumes")) {
               var4 = 0;
            }
            break;
         case 1504269962:
            if (var3.equals("insertalternativeaction")) {
               var4 = 7;
            }
         }

         switch(var4) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
            return Collections.emptyList();
         case 13:
            return CompleteUtility.complete(args[3], ConditionManager.getConditionTypeIds());
         case 14:
         case 15:
         case 16:
            return CompleteUtility.complete(args[3], ActionManager.getActionTypeIds());
         default:
            return Collections.emptyList();
         }
      case 5:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case -2035413680:
            if (var3.equals("setnoconsumesaction")) {
               var4 = 10;
            }
            break;
         case -1383198689:
            if (var3.equals("removealternativeaction")) {
               var4 = 8;
            }
            break;
         case -1369979903:
            if (var3.equals("setalternativeaction")) {
               var4 = 7;
            }
            break;
         case -1325891847:
            if (var3.equals("setcondition")) {
               var4 = 1;
            }
            break;
         case -753678057:
            if (var3.equals("removecondition")) {
               var4 = 2;
            }
            break;
         case -502945150:
            if (var3.equals("insertcondition")) {
               var4 = 0;
            }
            break;
         case -468253766:
            if (var3.equals("removeaction")) {
               var4 = 5;
            }
            break;
         case -418675289:
            if (var3.equals("insertnoconsumesaction")) {
               var4 = 9;
            }
            break;
         case 596559218:
            if (var3.equals("removenoconsumesaction")) {
               var4 = 11;
            }
            break;
         case 974579992:
            if (var3.equals("setaction")) {
               var4 = 4;
            }
            break;
         case 1139249711:
            if (var3.equals("insertaction")) {
               var4 = 3;
            }
            break;
         case 1504269962:
            if (var3.equals("insertalternativeaction")) {
               var4 = 6;
            }
         }

         switch(var4) {
         case 0:
         case 1:
         case 2:
            return CompleteUtility.complete(args[4], ConditionManager.getConditionTypeIds());
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
            return CompleteUtility.complete(args[4], ActionManager.getActionTypeIds());
         default:
            return Collections.emptyList();
         }
      default:
         return Collections.emptyList();
      }
   }
}
