package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Attribute extends SubCmd {
   private static final String[] attributeSub = new String[]{"add", "remove"};

   public Attribute(@NotNull ItemEditCommand cmd) {
      super("attribute", cmd, true, true);
   }

   public void reload() {
      super.reload();
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (args.length == 1) {
         this.onFail(p, alias);
      } else {
         String var6 = args[1].toLowerCase(Locale.ENGLISH);
         byte var7 = -1;
         switch(var6.hashCode()) {
         case -934610812:
            if (var6.equals("remove")) {
               var7 = 1;
            }
            break;
         case 96417:
            if (var6.equals("add")) {
               var7 = 0;
            }
         }

         switch(var7) {
         case 0:
            this.attributeAdd(p, item, alias, args);
            return;
         case 1:
            this.attributeRemove(p, item, alias, args);
            return;
         default:
            this.onFail(p, alias);
         }
      }
   }

   private void attributeAdd(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length < 4 || args.length > 6) {
            throw new IllegalArgumentException("Wrong param number");
         }

         org.bukkit.attribute.Attribute attr = (org.bukkit.attribute.Attribute)Aliases.ATTRIBUTE.convertAlias(args[2]);
         if (attr == null) {
            this.onWrongAlias("wrong-attribute", p, Aliases.ATTRIBUTE, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "add");
            return;
         }

         double amount = Double.parseDouble(args[3]);
         Operation op;
         if (args.length > 4) {
            op = (Operation)Aliases.OPERATIONS.convertAlias(args[4]);
         } else {
            op = Operation.ADD_NUMBER;
         }

         if (op == null) {
            this.onWrongAlias("wrong-operation", p, Aliases.OPERATIONS, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "add");
            return;
         }

         String equip = null;
         if (args.length > 5) {
            if (VersionUtils.isVersionAfter(1, 21)) {
               equip = ((EquipmentSlotGroup)Aliases.EQUIPMENT_SLOTGROUPS.convertAlias(args[5])).toString();
               if (equip == null) {
                  this.onWrongAlias("wrong-equipment", p, Aliases.EQUIPMENT_SLOTGROUPS, new String[0]);
                  this.sendFailFeedbackForSub(p, alias, "add");
                  return;
               }
            } else {
               equip = ((EquipmentSlot)Aliases.EQUIPMENT_SLOTS.convertAlias(args[5])).toString();
               if (equip == null) {
                  this.onWrongAlias("wrong-equipment", p, Aliases.EQUIPMENT_SLOTS, new String[0]);
                  this.sendFailFeedbackForSub(p, alias, "add");
                  return;
               }
            }
         }

         ItemMeta itemMeta = ItemUtils.getMeta(item);
         itemMeta.addAttributeModifier(attr, ItemUtils.createAttributeModifier(amount, op, equip));
         item.setItemMeta(itemMeta);
         this.updateView(p);
      } catch (Exception var11) {
         var11.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "add");
      }

   }

   private void attributeRemove(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         org.bukkit.attribute.Attribute attr = (org.bukkit.attribute.Attribute)Aliases.ATTRIBUTE.convertAlias(args[2]);
         EquipmentSlot equip = (EquipmentSlot)Aliases.EQUIPMENT_SLOTS.convertAlias(args[2]);
         if (attr == null && equip == null) {
            this.onWrongAlias("wrong-attribute", p, Aliases.ATTRIBUTE, new String[0]);
            this.onWrongAlias("wrong-equipment", p, Aliases.EQUIPMENT_SLOTS, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "remove");
            return;
         }

         ItemMeta itemMeta = ItemUtils.getMeta(item);
         if (attr != null) {
            itemMeta.removeAttributeModifier(attr);
         }

         if (equip != null) {
            itemMeta.removeAttributeModifier(equip);
         }

         item.setItemMeta(itemMeta);
         this.updateView(p);
      } catch (Exception var8) {
         this.sendFailFeedbackForSub(p, alias, "remove");
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (args.length == 2) {
         return CompleteUtility.complete(args[1], attributeSub);
      } else {
         if (args[1].equalsIgnoreCase("add")) {
            if (args.length == 3) {
               return CompleteUtility.complete(args[2], (IAliasSet)Aliases.ATTRIBUTE);
            }

            if (args.length == 5) {
               return CompleteUtility.complete(args[4], (IAliasSet)Aliases.OPERATIONS);
            }

            if (args.length == 6) {
               if (VersionUtils.isVersionAfter(1, 21)) {
                  return CompleteUtility.complete(args[5], (IAliasSet)Aliases.EQUIPMENT_SLOTGROUPS);
               }

               return CompleteUtility.complete(args[5], (IAliasSet)Aliases.EQUIPMENT_SLOTS);
            }
         } else if (args[1].equalsIgnoreCase("remove") && args.length == 3) {
            List<String> l = CompleteUtility.complete(args[2], (IAliasSet)Aliases.ATTRIBUTE);
            l.addAll(CompleteUtility.complete(args[2], (IAliasSet)Aliases.EQUIPMENT_SLOTS));
            return l;
         }

         return Collections.emptyList();
      }
   }
}
