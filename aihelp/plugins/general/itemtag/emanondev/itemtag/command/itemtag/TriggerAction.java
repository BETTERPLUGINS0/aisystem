package emanondev.itemtag.command.itemtag;

import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.activity.Activity;
import emanondev.itemtag.activity.ActivityManager;
import emanondev.itemtag.activity.TriggerHandler;
import emanondev.itemtag.activity.TriggerManager;
import emanondev.itemtag.activity.TriggerType;
import emanondev.itemtag.command.ItemTagCommand;
import emanondev.itemtag.gui.TriggerGui;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class TriggerAction extends SubCmd {
   public TriggerAction(@NotNull ItemTagCommand cmd) {
      super("trigger", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String label, String[] args) {
      Player player = (Player)sender;
      if (args.length == 1) {
         player.openInventory((new TriggerGui(player, player.getItemInHand())).getInventory());
      } else {
         String var5 = args[1].toLowerCase(Locale.ENGLISH);
         byte var6 = -1;
         switch(var5.hashCode()) {
         case -2140852082:
            if (var5.equals("setmaxuses")) {
               var6 = 5;
            }
            break;
         case -1335458389:
            if (var5.equals("delete")) {
               var6 = 1;
            }
            break;
         case -445604480:
            if (var5.equals("consumeatusesend")) {
               var6 = 4;
            }
            break;
         case 113762:
            if (var5.equals("set")) {
               var6 = 0;
            }
            break;
         case 1355209603:
            if (var5.equals("cooldownamount")) {
               var6 = 7;
            }
            break;
         case 1836142251:
            if (var5.equals("visualcooldown")) {
               var6 = 6;
            }
            break;
         case 1985980206:
            if (var5.equals("setuses")) {
               var6 = 2;
            }
            break;
         case 2056279791:
            if (var5.equals("setallowedslots")) {
               var6 = 3;
            }
         }

         switch(var6) {
         case 0:
            this.set(player, label, args);
            return;
         case 1:
            this.delete(player, label, args);
            return;
         case 2:
            this.setuses(player, label, args);
            return;
         case 3:
            this.setallowedslots(player, label, args);
            return;
         case 4:
            this.consumeatusesend(player, label, args);
            return;
         case 5:
            this.maxuses(player, label, args);
            return;
         case 6:
            this.visualcooldown(player, label, args);
            return;
         case 7:
            this.cooldownamount(player, label, args);
            return;
         default:
            this.help(player, label, args);
         }
      }
   }

   private void cooldownamount(Player player, String label, String[] args) {
      if (args.length == 4 || args.length == 5) {
         TriggerType type = TriggerManager.getTriggerType(args[2]);
         if (type != null) {
            boolean seconds = args.length == 5 && args[4].equalsIgnoreCase("s");

            int amount;
            try {
               amount = Integer.parseInt(args[3]);
            } catch (Exception var8) {
               return;
            }

            amount = Math.max(0, amount * (seconds ? 1000 : 1));
            TriggerHandler.setCooldownAmountMs(type, ItemTag.getTagItem(this.getItemInHand(player)), (long)amount);
         }
      }
   }

   private void visualcooldown(Player player, String label, String[] args) {
      if (args.length == 3) {
         Boolean value = (Boolean)Aliases.BOOLEAN.convertAlias(args[2]);
         if (value != null) {
            TriggerHandler.setVisualCooldown(ItemTag.getTagItem(this.getItemInHand(player)), value);
         }
      }
   }

   private void maxuses(Player player, String label, String[] args) {
      if (args.length == 3) {
         int amount;
         try {
            amount = Integer.parseInt(args[2]);
         } catch (Exception var6) {
            return;
         }

         TriggerHandler.setMaxUses(ItemTag.getTagItem(this.getItemInHand(player)), amount);
      }
   }

   private void consumeatusesend(Player player, String label, String[] args) {
      if (args.length == 3) {
         Boolean value = (Boolean)Aliases.BOOLEAN.convertAlias(args[2]);
         if (value != null) {
            TriggerHandler.setConsumeAtUsesEnd(ItemTag.getTagItem(this.getItemInHand(player)), value);
         }
      }
   }

   private void setallowedslots(Player player, String label, String[] args) {
      EnumSet<EquipmentSlot> set = EnumSet.noneOf(EquipmentSlot.class);
      TriggerType type = TriggerManager.getTriggerType(args[2]);
      if (type != null) {
         for(int i = 3; i < args.length; ++i) {
            EquipmentSlot value = (EquipmentSlot)Aliases.EQUIPMENT_SLOTS.convertAlias(args[i]);
            if (value == null) {
               return;
            }

            set.add(value);
         }

         TriggerHandler.setAllowedSlot(type, ItemTag.getTagItem(this.getItemInHand(player)), set);
      }
   }

   private void setuses(Player player, String label, String[] args) {
      if (args.length == 3) {
         int amount;
         try {
            amount = Integer.parseInt(args[2]);
         } catch (Exception var6) {
            return;
         }

         TriggerHandler.setMaxUses(ItemTag.getTagItem(this.getItemInHand(player)), amount);
      }
   }

   private void set(Player player, String label, String[] args) {
      if (args.length == 4) {
         TriggerType type = TriggerManager.getTriggerType(args[2]);
         if (type != null) {
            Activity activity = ActivityManager.getActivity(args[3]);
            if (activity != null) {
               TriggerHandler.setTriggerActivity(type, ItemTag.getTagItem(this.getItemInHand(player)), activity);
            }
         }
      }
   }

   private void delete(Player player, String label, String[] args) {
      if (args.length == 4) {
         TriggerType type = TriggerManager.getTriggerType(args[2]);
         if (type != null) {
            TagItem tag = ItemTag.getTagItem(this.getItemInHand(player));
            if (TriggerHandler.hasTrigger(type, tag)) {
               TriggerHandler.setTriggerActivity(type, tag, (Activity)null);
            }
         }
      }
   }

   private void help(Player player, String label, String[] args) {
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      String var3;
      byte var4;
      switch(args.length) {
      case 2:
         return CompleteUtility.complete(args[1], new String[]{"set", "delete", "setuses", "setallowedslots", "consumeatusesend", "setmaxuses", "visualcooldown", "cooldownamount"});
      case 3:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case -2140852082:
            if (var3.equals("setmaxuses")) {
               var4 = 5;
            }
            break;
         case -1335458389:
            if (var3.equals("delete")) {
               var4 = 1;
            }
            break;
         case -445604480:
            if (var3.equals("consumeatusesend")) {
               var4 = 6;
            }
            break;
         case 113762:
            if (var3.equals("set")) {
               var4 = 0;
            }
            break;
         case 1355209603:
            if (var3.equals("cooldownamount")) {
               var4 = 3;
            }
            break;
         case 1836142251:
            if (var3.equals("visualcooldown")) {
               var4 = 7;
            }
            break;
         case 1985980206:
            if (var3.equals("setuses")) {
               var4 = 4;
            }
            break;
         case 2056279791:
            if (var3.equals("setallowedslots")) {
               var4 = 2;
            }
         }

         switch(var4) {
         case 0:
         case 1:
         case 2:
         case 3:
            return CompleteUtility.complete(args[2], TriggerManager.getTriggerTypeIds());
         case 4:
         case 5:
            return CompleteUtility.complete(args[2], new String[]{"-1", "1", "50"});
         case 6:
         case 7:
            return CompleteUtility.complete(args[2], Aliases.BOOLEAN);
         default:
            return Collections.emptyList();
         }
      case 4:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case 113762:
            if (var3.equals("set")) {
               var4 = 0;
            }
            break;
         case 2056279791:
            if (var3.equals("setallowedslots")) {
               var4 = 1;
            }
         }

         switch(var4) {
         case 0:
            return CompleteUtility.complete(args[3], ActivityManager.getActivityIds());
         case 1:
            return CompleteUtility.complete(args[2], Aliases.EQUIPMENT_SLOTS);
         default:
            return Collections.emptyList();
         }
      default:
         return args[1].equalsIgnoreCase("setallowedslots") ? CompleteUtility.complete(args[2], Aliases.EQUIPMENT_SLOTS) : Collections.emptyList();
      }
   }
}
