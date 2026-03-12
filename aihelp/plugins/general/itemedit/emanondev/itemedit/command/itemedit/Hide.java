package emanondev.itemedit.command.itemedit;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Hide extends SubCmd {
   public Hide(ItemEditCommand cmd) {
      super("hide", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length != 3 && args.length != 2) {
            throw new IllegalArgumentException("Wrong param number");
         }

         ItemMeta itemMeta = ItemUtils.getMeta(item);
         ItemFlag flag = (ItemFlag)Aliases.FLAG_TYPE.convertAlias(args[1]);
         if (flag == null) {
            this.onWrongAlias("wrong-flag", p, Aliases.FLAG_TYPE, new String[0]);
            this.onFail(p, alias);
            return;
         }

         boolean add = args.length == 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !itemMeta.hasItemFlag(flag);
         this.handleFlagChange(add, flag, item, itemMeta);
         if (add) {
            itemMeta.addItemFlags(new ItemFlag[]{flag});
         } else {
            itemMeta.removeItemFlags(new ItemFlag[]{flag});
         }

         item.setItemMeta(itemMeta);
         this.updateView(p);
      } catch (Exception var9) {
         this.onFail(p, alias);
      }

   }

   private void handleFlagChange(boolean put, ItemFlag flag, ItemStack item, ItemMeta meta) {
      if (VersionUtils.hasPaperAPI() && VersionUtils.isVersionAfter(1, 20, 5) && ItemEdit.get().getConfig().loadBoolean("itemedit.paper_hide_fix", true)) {
         if (flag == ItemFlag.HIDE_ATTRIBUTES) {
            if (put) {
               if (meta.getAttributeModifiers() == null) {
                  EquipmentSlot[] var11 = EquipmentSlot.values();
                  int var12 = var11.length;

                  for(int var13 = 0; var13 < var12; ++var13) {
                     EquipmentSlot slot = var11[var13];
                     Multimap var10000 = item.getType().getDefaultAttributeModifiers(slot);
                     Objects.requireNonNull(meta);
                     var10000.forEach(meta::addAttributeModifier);
                  }

               }
            } else {
               Multimap<org.bukkit.attribute.Attribute, AttributeModifier> mods = meta.getAttributeModifiers();
               if (mods != null) {
                  HashMultimap<org.bukkit.attribute.Attribute, AttributeModifier> mods2 = HashMultimap.create();
                  EquipmentSlot[] var7 = EquipmentSlot.values();
                  int var8 = var7.length;

                  int var9;
                  EquipmentSlot slot;
                  for(var9 = 0; var9 < var8; ++var9) {
                     slot = var7[var9];
                     mods2.putAll(item.getType().getDefaultAttributeModifiers(slot));
                  }

                  if (mods.equals(mods2)) {
                     var7 = EquipmentSlot.values();
                     var8 = var7.length;

                     for(var9 = 0; var9 < var8; ++var9) {
                        slot = var7[var9];
                        meta.removeAttributeModifier(slot);
                     }
                  }

               }
            }
         }
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (args.length == 2) {
         return CompleteUtility.complete(args[1], (IAliasSet)Aliases.FLAG_TYPE);
      } else {
         return args.length == 3 ? CompleteUtility.complete(args[2], (IAliasSet)Aliases.BOOLEAN) : Collections.emptyList();
      }
   }
}
