package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.FlagYielding;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.IStringSetProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardCartProperty;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class OwnerPermissionSet extends FieldBackedStandardCartProperty<Set<String>> implements IStringSetProperty {
   public void addOwnerPermInfo(MessageBuilder message, IProperties properties) {
      if (properties.hasOwnerPermissions()) {
         message.yellow(new Object[]{"Owned by players with the permissions:"});
         Iterator var3 = properties.getOwnerPermissions().iterator();

         while(var3.hasNext()) {
            String ownerPerm = (String)var3.next();
            message.newLine().yellow(new Object[]{"  - "}).white(new Object[]{ownerPerm});
         }
      } else {
         message.yellow(new Object[]{"No owner permission rules are set."});
      }

   }

   @PropertyCheckPermission("ownerperms")
   @Command("cart owners permission")
   @CommandDescription("Display the owner permissions set for a cart")
   private void getProperty(CommandSender sender, CartProperties properties) {
      MessageBuilder message = new MessageBuilder();
      this.addOwnerPermInfo(message, properties);
      message.send(sender);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("ownerperms")
   @Command("cart owners permission add <permissions>")
   @CommandDescription("Adds permissions players need to access a cart")
   private void setPropertyAdd(CommandSender sender, CartProperties properties, @FlagYielding @Argument("permissions") String[] permissions) {
      if (permissions != null && permissions.length > 0) {
         sender.sendMessage(ChatColor.GREEN + "Adding permission rules to cart: " + StringUtil.combineNames(permissions));
         String[] var4 = permissions;
         int var5 = permissions.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String permission = var4[var6];
            properties.addOwnerPermission(permission);
         }
      }

      this.getProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("ownerperms")
   @Command("cart owners permission remove <permissions>")
   @CommandDescription("Removes permissions players need to access a cart")
   private void setPropertyRemove(CommandSender sender, CartProperties properties, @FlagYielding @Argument("permissions") String[] permissions) {
      if (permissions != null && permissions.length > 0) {
         sender.sendMessage(ChatColor.GREEN + "Removing permission rules from cart: " + StringUtil.combineNames(permissions));
         String[] var4 = permissions;
         int var5 = permissions.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String permission = var4[var6];
            properties.removeOwnerPermission(permission);
         }
      }

      this.getProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("ownerperms")
   @Command("cart owners permission set <permissions>")
   @CommandDescription("Discards previous owner permissions and sets new permissions players need to access a cart")
   private void setProperty(CommandSender sender, CartProperties properties, @FlagYielding @Argument("permissions") String[] permissions) {
      if (permissions != null && permissions.length > 0) {
         properties.clearOwnerPermissions();
         sender.sendMessage(ChatColor.GREEN + "Set new permission rules for cart: " + StringUtil.combineNames(permissions));
         String[] var4 = permissions;
         int var5 = permissions.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String permission = var4[var6];
            properties.addOwnerPermission(permission);
         }
      } else {
         this.setPropertyClear(sender, properties);
      }

   }

   @PropertyCheckPermission("ownerperms")
   @Command("cart owners permission clear")
   @CommandDescription("Clears all owner permissions set for a cart")
   private void setPropertyClear(CommandSender sender, CartProperties properties) {
      properties.clearOwnerPermissions();
      sender.sendMessage(ChatColor.GREEN + "Permission rules cleared.");
   }

   @PropertyCheckPermission("ownerperms")
   @Command("train owners permission")
   @CommandDescription("Display the owner permissions set for a train")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      MessageBuilder message = new MessageBuilder();
      this.addOwnerPermInfo(message, properties);
      message.send(sender);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("ownerperms")
   @Command("train owners permission add <permissions>")
   @CommandDescription("Adds permissions players need to access a cart")
   private void setPropertyAdd(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("permissions") String[] permissions) {
      if (permissions != null && permissions.length > 0) {
         sender.sendMessage(ChatColor.GREEN + "Adding permission rules to train: " + StringUtil.combineNames(permissions));
         String[] var4 = permissions;
         int var5 = permissions.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String permission = var4[var6];
            properties.addOwnerPermission(permission);
         }
      }

      this.getProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("ownerperms")
   @Command("train owners permission remove <permissions>")
   @CommandDescription("Removes permissions players need to access a cart")
   private void setPropertyRemove(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("permissions") String[] permissions) {
      if (permissions != null && permissions.length > 0) {
         sender.sendMessage(ChatColor.GREEN + "Removing permission rules from train: " + StringUtil.combineNames(permissions));
         String[] var4 = permissions;
         int var5 = permissions.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String permission = var4[var6];
            properties.removeOwnerPermission(permission);
         }
      }

      this.getProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("ownerperms")
   @Command("train owners permission set <permissions>")
   @CommandDescription("Discards previous owner permissions and sets new permissions players need to access a train")
   private void setProperty(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("permissions") String[] permissions) {
      if (permissions != null && permissions.length > 0) {
         properties.clearOwnerPermissions();
         sender.sendMessage(ChatColor.GREEN + "Set new permission rules for train: " + StringUtil.combineNames(permissions));
         String[] var4 = permissions;
         int var5 = permissions.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String permission = var4[var6];
            properties.addOwnerPermission(permission);
         }
      } else {
         this.setPropertyClear(sender, properties);
      }

   }

   @PropertyCheckPermission("ownerperms")
   @Command("train owners permission clear")
   @CommandDescription("Clears all owner permissions set for a train")
   private void setPropertyClear(CommandSender sender, TrainProperties properties) {
      properties.clearOwnerPermissions();
      sender.sendMessage(ChatColor.GREEN + "Permission rules cleared.");
   }

   @PropertyParser("setownerperm|ownerperms set")
   public Set<String> parseSet(String input) {
      return input.isEmpty() ? Collections.emptySet() : Collections.singleton(input);
   }

   @PropertyParser("clearownerperm|ownerperms clear")
   public Set<String> parseClear(String input) {
      return Collections.emptySet();
   }

   @PropertyParser(
      value = "addownerperm|ownerperms add",
      processPerCart = true
   )
   public Set<String> parseAdd(PropertyParseContext<Set<String>> context) {
      if (!context.input().isEmpty() && !((Set)context.current()).contains(context.input())) {
         HashSet<String> newPerms = new HashSet((Collection)context.current());
         newPerms.add(context.input());
         return Collections.unmodifiableSet(newPerms);
      } else {
         return (Set)context.current();
      }
   }

   @PropertyParser(
      value = "remownerperm|ownerperm rem|ownerperms remove",
      processPerCart = true
   )
   public Set<String> parseRemove(PropertyParseContext<Set<String>> context) {
      if (!context.input().isEmpty() && ((Set)context.current()).contains(context.input())) {
         HashSet<String> newPerms = new HashSet((Collection)context.current());
         newPerms.remove(context.input());
         return Collections.unmodifiableSet(newPerms);
      } else {
         return (Set)context.current();
      }
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_OWNERS.has(sender);
   }

   public Set<String> getDefault() {
      return Collections.emptySet();
   }

   public String getListedName() {
      return "owner perms";
   }

   public Set<String> getData(FieldBackedProperty.CartInternalData data) {
      return data.ownerPermissions;
   }

   public void setData(FieldBackedProperty.CartInternalData data, Set<String> value) {
      data.ownerPermissions = value;
   }

   public Optional<Set<String>> readFromConfig(ConfigurationNode config) {
      return Util.getConfigStringSetOptional(config, "ownerPermissions");
   }

   public void writeToConfig(ConfigurationNode config, Optional<Set<String>> value) {
      Util.setConfigStringCollectionOptional(config, "ownerPermissions", value);
   }

   public Set<String> get(TrainProperties properties) {
      return FieldBackedProperty.TrainInternalData.get(properties).ownerPermissions.update(properties, this);
   }
}
