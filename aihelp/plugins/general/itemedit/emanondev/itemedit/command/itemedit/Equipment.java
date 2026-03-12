package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.TagContainer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.jetbrains.annotations.NotNull;

public class Equipment extends SubCmd {
   private final String[] subCommands = new String[]{"slot", "swappable", "allowedentities", "equipsound", "equiponinteract", "dispensable", "damageonhurt", "cameraoverlay", "canshear", "shearsound", "clear", "model"};

   public Equipment(@NotNull ItemEditCommand command) {
      super("equipment", command, true, true);
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
         case -1256402921:
            if (var6.equals("swappable")) {
               var7 = 2;
            }
            break;
         case -986841377:
            if (var6.equals("equipsound")) {
               var7 = 4;
            }
            break;
         case -923840306:
            if (var6.equals("shearsound")) {
               var7 = 10;
            }
            break;
         case -108314863:
            if (var6.equals("canshear")) {
               var7 = 9;
            }
            break;
         case 3533310:
            if (var6.equals("slot")) {
               var7 = 1;
            }
            break;
         case 94746189:
            if (var6.equals("clear")) {
               var7 = 0;
            }
            break;
         case 104069929:
            if (var6.equals("model")) {
               var7 = 11;
            }
            break;
         case 163795298:
            if (var6.equals("dispensable")) {
               var7 = 6;
            }
            break;
         case 190254749:
            if (var6.equals("damageonhurt")) {
               var7 = 7;
            }
            break;
         case 253009291:
            if (var6.equals("cameraoverlay")) {
               var7 = 8;
            }
            break;
         case 931348805:
            if (var6.equals("equiponinteract")) {
               var7 = 5;
            }
            break;
         case 1337598313:
            if (var6.equals("allowedentities")) {
               var7 = 3;
            }
         }

         switch(var7) {
         case 0:
            this.equipmentClear(p, item, alias, args);
            return;
         case 1:
            this.equipmentSlot(p, item, alias, args);
            return;
         case 2:
            this.equipmentSwappable(p, item, alias, args);
            return;
         case 3:
            this.equipmentAllowedEntities(p, item, alias, args);
            return;
         case 4:
            this.equipmentEquipSound(p, item, alias, args);
            return;
         case 5:
            this.equipmentEquipOnInteract(p, item, alias, args);
            return;
         case 6:
            this.equipmentDispensable(p, item, alias, args);
            return;
         case 7:
            this.equipmentDamageOnHurt(p, item, alias, args);
            return;
         case 8:
            this.equipmentCameraOverlay(p, item, alias, args);
            return;
         case 9:
            this.equipmentCanShear(p, item, alias, args);
            return;
         case 10:
            this.equipmentShearSound(p, item, alias, args);
            return;
         case 11:
            this.equipmentModel(p, item, alias, args);
            return;
         default:
            this.onFail(p, alias);
         }
      }
   }

   private void equipmentModel(Player p, ItemStack item, @NotNull String alias, String[] args) {
      try {
         if (args.length == 2) {
            ItemMeta meta = ItemUtils.getMeta(item);
            if (!meta.hasEquippable()) {
               this.sendCustomFeedbackForSub(p, "model", "feedback-reset", new String[0]);
               return;
            }

            EquippableComponent comp = meta.getEquippable();
            comp.setModel((NamespacedKey)null);
            meta.setEquippable(comp);
            item.setItemMeta(meta);
            this.updateView(p);
            this.sendCustomFeedbackForSub(p, "model", "feedback-reset", new String[0]);
            return;
         }

         if (args.length != 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         String[] rawKey = args[2].toLowerCase(Locale.ENGLISH).split(":");
         NamespacedKey key = rawKey.length == 1 ? new NamespacedKey("minecraft", rawKey[0]) : new NamespacedKey(rawKey[0], rawKey[1]);
         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         comp.setModel(key);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         this.updateView(p);
         this.sendFeedbackForSub(p, "model", new String[]{"%key%", key.toString()});
      } catch (Throwable var9) {
         var9.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "model");
      }

   }

   private void equipmentShearSound(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "shearsound");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         Sound value = args.length == 2 ? null : (Sound)Aliases.SOUND.convertAlias(args[2]);
         if (args.length == 3 && value == null) {
            this.onWrongAlias("wrong-sound", p, Aliases.SOUND, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "shearsound");
            return;
         }

         comp.setShearingSound(value);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         item.setItemMeta(meta);
         if (value != null) {
            this.sendFeedbackForSub(p, "shearsound", new String[]{"%value%", args[2]});
         } else {
            this.sendCustomFeedbackForSub(p, "shearsound", "feedback-reset", new String[0]);
         }
      } catch (NoSuchMethodError var8) {
         this.sendCustomFeedbackForSub(p, "canshear", "unsupported-version", new String[]{"%value%", Bukkit.getVersion()});
      } catch (Throwable var9) {
         var9.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "shearsound");
      }

   }

   private void equipmentCanShear(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "canshear");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         Boolean value = args.length == 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !comp.isCanBeSheared();
         if (args.length == 3 && value == null) {
            this.onWrongAlias("wrong-boolean", p, Aliases.BOOLEAN, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "canshear");
            return;
         }

         comp.setCanBeSheared(value);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         this.sendFeedbackForSub(p, "canshear", new String[]{"%value%", String.valueOf(value)});
      } catch (NoSuchMethodError var8) {
         this.sendCustomFeedbackForSub(p, "canshear", "unsupported-version", new String[]{"%value%", Bukkit.getVersion()});
      } catch (Throwable var9) {
         var9.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "canshear");
      }

   }

   private void equipmentCameraOverlay(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "cameraoverlay");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         String rawkey = args.length == 2 ? null : args[2];
         NamespacedKey key = rawkey == null ? null : NamespacedKey.fromString(rawkey);
         if (args.length == 3 && key == null) {
            this.sendCustomFeedbackForSub(p, "cameraoverlay", "invalid-namespacedkey", new String[]{"%value%", args[2]});
            return;
         }

         comp.setCameraOverlay(key);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         if (key != null) {
            this.sendFeedbackForSub(p, "cameraoverlay", new String[]{"%value%", key.toString()});
         } else {
            this.sendCustomFeedbackForSub(p, "cameraoverlay", "feedback-reset", new String[0]);
         }
      } catch (Throwable var9) {
         var9.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "cameraoverlay");
      }

   }

   private void equipmentDamageOnHurt(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "damageonhurt");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         Boolean value = args.length == 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !comp.isDamageOnHurt();
         if (args.length == 3 && value == null) {
            this.onWrongAlias("wrong-boolean", p, Aliases.BOOLEAN, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "damageonhurt");
            return;
         }

         comp.setDamageOnHurt(value);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         this.sendFeedbackForSub(p, "damageonhurt", new String[]{"%value%", String.valueOf(value)});
         if (value) {
            if (meta.hasMaxStackSize()) {
               if (meta.getMaxStackSize() <= 1) {
                  return;
               }
            } else if (item.getType().getMaxStackSize() <= 1) {
               return;
            }

            String msg = this.getLanguageString("damageonhurt.warning-maxstacksize", (String)null, p, new String[0]);
            if (msg != null && !msg.isEmpty()) {
               ComponentBuilder compMsg = (new ComponentBuilder(msg)).event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + alias + " maxstacksize 1"));
               Util.sendMessage(p, (BaseComponent[])compMsg.create());
            }
         }
      } catch (Throwable var10) {
         var10.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "damageonhurt");
      }

   }

   private void equipmentDispensable(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "dispensable");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         Boolean value = args.length == 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !comp.isDamageOnHurt();
         if (args.length == 3 && value == null) {
            this.onWrongAlias("wrong-boolean", p, Aliases.BOOLEAN, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "dispensable");
            return;
         }

         comp.setDamageOnHurt(value);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         this.sendFeedbackForSub(p, "dispensable", new String[]{"%value%", String.valueOf(value)});
      } catch (Throwable var8) {
         var8.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "dispensable");
      }

   }

   private void equipmentEquipOnInteract(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "equiponinteract");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         Boolean value = args.length == 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !comp.isEquipOnInteract();
         if (args.length == 3 && value == null) {
            this.onWrongAlias("wrong-boolean", p, Aliases.BOOLEAN, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "equiponinteract");
            return;
         }

         comp.setEquipOnInteract(value);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         this.sendFeedbackForSub(p, "equiponinteract", new String[]{"%value%", String.valueOf(value)});
      } catch (NoSuchMethodError var8) {
         this.sendCustomFeedbackForSub(p, "canshear", "unsupported-version", new String[]{"%value%", Bukkit.getVersion()});
      } catch (Throwable var9) {
         var9.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "equiponinteract");
      }

   }

   private void equipmentSwappable(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "swappable");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         Boolean value = args.length == 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !comp.isSwappable();
         if (args.length == 3 && value == null) {
            this.onWrongAlias("wrong-boolean", p, Aliases.BOOLEAN, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "swappable");
            return;
         }

         comp.setSwappable(value);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         this.sendFeedbackForSub(p, "swappable", new String[]{"%value%", String.valueOf(value)});
      } catch (Throwable var8) {
         var8.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "swappable");
      }

   }

   private void equipmentAllowedEntities(Player p, ItemStack item, String alias, String[] args) {
      try {
         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         if (args.length == 2) {
            comp.setAllowedEntities((EntityType)null);
            meta.setEquippable(comp);
            item.setItemMeta(meta);
            this.sendCustomFeedbackForSub(p, "allowedentities", "feedback-reset", new String[0]);
            return;
         }

         Set<EntityType> types = new HashSet();
         String[] var8 = (String[])Arrays.copyOfRange(args, 2, args.length);
         int var9 = var8.length;
         int var10 = 0;

         while(true) {
            if (var10 >= var9) {
               comp.setAllowedEntities(types);
               meta.setEquippable(comp);
               item.setItemMeta(meta);
               this.sendFeedbackForSub(p, "allowedentities", new String[]{"%value%", (String)types.stream().map(Enum::name).collect(Collectors.joining(", "))});
               break;
            }

            String arg = var8[var10];
            EntityType entity = (EntityType)Aliases.ENTITY_TYPE.convertAlias(arg);
            if (entity != null && entity.isAlive()) {
               types.add(entity);
            } else {
               TagContainer<EntityType> tag = (TagContainer)Aliases.ENTITY_GROUPS.convertAlias(arg);
               if (tag == null || !tag.getValues().stream().anyMatch(EntityType::isAlive)) {
                  this.onWrongAlias("wrong-entitytype", p, Aliases.ENTITY_TYPE, new String[0]);
                  this.onWrongAlias("wrong-entitygroup", p, Aliases.ENTITY_GROUPS, new String[0]);
                  this.sendCustomFeedbackForSub(p, "allowedentities", "invalid-type", new String[]{"%value%", arg});
                  return;
               }

               types.addAll((Collection)tag.getValues().stream().filter(EntityType::isAlive).collect(Collectors.toList()));
            }

            ++var10;
         }
      } catch (Throwable var14) {
         var14.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "allowedentities");
      }

   }

   private void equipmentEquipSound(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2 && args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "equipsound");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         Sound value = args.length == 2 ? null : (Sound)Aliases.SOUND.convertAlias(args[2]);
         if (args.length == 3 && value == null) {
            this.onWrongAlias("wrong-sound", p, Aliases.SOUND, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "equipsound");
            return;
         }

         comp.setEquipSound(value);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         if (value != null) {
            this.sendFeedbackForSub(p, "equipsound", new String[]{"%value%", args[2]});
         } else {
            this.sendCustomFeedbackForSub(p, "equipsound", "feedback-reset", new String[0]);
         }
      } catch (Throwable var8) {
         var8.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "equipsound");
      }

   }

   private void equipmentSlot(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            this.sendFailFeedbackForSub(p, alias, "slot");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         EquippableComponent comp = meta.getEquippable();
         EquipmentSlot slot = (EquipmentSlot)Aliases.EQUIPMENT_SLOTS.convertAlias(args[2]);
         if (slot == null) {
            this.onWrongAlias("wrong-slot", p, Aliases.SOUND, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "slot");
            return;
         }

         comp.setSlot(slot);
         meta.setEquippable(comp);
         item.setItemMeta(meta);
         this.sendFeedbackForSub(p, "slot", new String[]{"%value%", args[2]});
      } catch (Throwable var8) {
         var8.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "slot");
      }

   }

   private void equipmentClear(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 2) {
            this.sendFailFeedbackForSub(p, alias, "clear");
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         meta.setEquippable((EquippableComponent)null);
         item.setItemMeta(meta);
         this.sendFeedbackForSub(p, "clear", new String[0]);
      } catch (Throwable var6) {
         var6.printStackTrace();
         this.sendFailFeedbackForSub(p, alias, "clear");
      }

   }

   public void onFail(@NotNull CommandSender target, @NotNull String alias) {
      Util.sendMessage(target, (new ComponentBuilder(this.getLanguageString("help-header", "", target, new String[0]))).create());
      String[] var3 = this.subCommands;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String sub = var3[var5];
         Util.sendMessage(target, (new ComponentBuilder(ChatColor.DARK_GREEN + "/" + alias + " " + this.getName() + ChatColor.GREEN + " " + sub + " " + this.getLanguageString(sub + ".params", "", target, new String[0]))).event(new ClickEvent(Action.SUGGEST_COMMAND, "/" + alias + " " + this.getName() + " " + sub + " ")).event(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(String.join("\n", this.getLanguageStringList(sub + ".description", (List)null, target, new String[0])))})).create());
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      switch(args.length) {
      case 2:
         return CompleteUtility.complete(args[1], this.subCommands);
      case 3:
         String var3 = args[1].toLowerCase(Locale.ENGLISH);
         byte var4 = -1;
         switch(var3.hashCode()) {
         case -1256402921:
            if (var3.equals("swappable")) {
               var4 = 1;
            }
            break;
         case -986841377:
            if (var3.equals("equipsound")) {
               var4 = 6;
            }
            break;
         case -923840306:
            if (var3.equals("shearsound")) {
               var4 = 7;
            }
            break;
         case -108314863:
            if (var3.equals("canshear")) {
               var4 = 5;
            }
            break;
         case 3533310:
            if (var3.equals("slot")) {
               var4 = 0;
            }
            break;
         case 163795298:
            if (var3.equals("dispensable")) {
               var4 = 2;
            }
            break;
         case 190254749:
            if (var3.equals("damageonhurt")) {
               var4 = 3;
            }
            break;
         case 253009291:
            if (var3.equals("cameraoverlay")) {
               var4 = 9;
            }
            break;
         case 931348805:
            if (var3.equals("equiponinteract")) {
               var4 = 4;
            }
            break;
         case 1337598313:
            if (var3.equals("allowedentities")) {
               var4 = 8;
            }
         }

         switch(var4) {
         case 0:
            return CompleteUtility.complete(args[2], (IAliasSet)Aliases.EQUIPMENT_SLOTS);
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
            return CompleteUtility.complete(args[2], (IAliasSet)Aliases.BOOLEAN);
         case 6:
         case 7:
            return CompleteUtility.complete(args[2], (IAliasSet)Aliases.SOUND);
         case 8:
            List<String> res = CompleteUtility.complete(args[2], (IAliasSet)Aliases.ENTITY_TYPE, (Predicate)(EntityType::isAlive));
            res.addAll(CompleteUtility.complete(args[2], (IAliasSet)Aliases.ENTITY_GROUPS, (Predicate)((tag) -> {
               return tag.getValues().stream().anyMatch(EntityType::isAlive);
            })));
            return res;
         case 9:
            return CompleteUtility.complete(args[2], "minecraft:misc/pumpkinblur");
         }
      default:
         if (args[1].equalsIgnoreCase("allowedentities")) {
            List<String> res = CompleteUtility.complete(args[args.length - 1], (IAliasSet)Aliases.ENTITY_TYPE, (Predicate)(EntityType::isAlive));
            res.addAll(CompleteUtility.complete(args[args.length - 1], (IAliasSet)Aliases.ENTITY_GROUPS, (Predicate)((tag) -> {
               return tag.getValues().stream().anyMatch(EntityType::isAlive);
            })));
            return res;
         } else {
            return Collections.emptyList();
         }
      }
   }
}
