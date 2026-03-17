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
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
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
import org.bukkit.entity.Player;

public final class OwnerSetProperty extends FieldBackedStandardCartProperty<Set<String>> implements IStringSetProperty {
   public void addOwnerInfo(MessageBuilder message, IProperties properties) {
      if (!properties.hasOwners() && !properties.hasOwnerPermissions()) {
         message.yellow(new Object[]{"Owned by: "}).green(new Object[]{"Everyone"}).white(new Object[]{" (use /train claim)"});
      } else {
         if (properties.hasOwners()) {
            message.yellow(new Object[]{"Owned by: "});
            message.setSeparator(ChatColor.YELLOW, " / ").setIndent(4);
            Iterator var3 = properties.getOwners().iterator();

            while(var3.hasNext()) {
               String owner = (String)var3.next();
               message.white(new Object[]{owner});
            }

            message.clearSeparator().setIndent(0);
         }

         if (properties.hasOwnerPermissions()) {
            StandardProperties.OWNER_PERMISSIONS.addOwnerPermInfo(message, properties);
         }
      }

   }

   @Command("cart owners")
   @CommandDescription("Display the owners set for the cart")
   private void getProperty(CommandSender sender, CartProperties properties) {
      MessageBuilder message = new MessageBuilder();
      this.addOwnerInfo(message, properties);
      message.send(sender);
   }

   @PropertyCheckPermission("owners")
   @Command("cart claim")
   @CommandDescription("Sets the caller as the sole owner of a cart")
   private void setPropertyClaim(Player sender, CartProperties properties) {
      properties.clearOwners();
      properties.setOwner(sender.getName(), true);
      sender.sendMessage(ChatColor.GREEN + "You are now the only owner of this cart!");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("owners")
   @Command("cart owners add <player_names>")
   @CommandDescription("Adds players as owners of a cart")
   private void setPropertyAdd(CommandSender sender, CartProperties properties, @FlagYielding @Argument("player_names") String[] playerNames) {
      if (playerNames != null && playerNames.length > 0) {
         sender.sendMessage(ChatColor.GREEN + "Adding owners to cart: " + StringUtil.combineNames(playerNames));
         String[] var4 = playerNames;
         int var5 = playerNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String playerName = var4[var6];
            properties.setOwner(playerName, true);
         }
      }

      this.getProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("owners")
   @Command("cart owners remove <player_names>")
   @CommandDescription("Removes players as owners of a cart")
   private void setPropertyRemove(CommandSender sender, CartProperties properties, @FlagYielding @Argument("player_names") String[] playerNames) {
      if (playerNames != null && playerNames.length > 0) {
         sender.sendMessage(ChatColor.GREEN + "Removing owners from cart: " + StringUtil.combineNames(playerNames));
         String[] var4 = playerNames;
         int var5 = playerNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String playerName = var4[var6];
            properties.setOwner(playerName, false);
         }
      }

      this.getProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("owners")
   @Command("cart owners set <player_names>")
   @CommandDescription("Discards previous owners and sets players as owners of a cart")
   private void setProperty(CommandSender sender, CartProperties properties, @FlagYielding @Argument("player_names") String[] playerNames) {
      if (playerNames != null && playerNames.length > 0) {
         properties.clearOwners();
         sender.sendMessage(ChatColor.GREEN + "Set new owners of cart: " + StringUtil.combineNames(playerNames));
         String[] var4 = playerNames;
         int var5 = playerNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String playerName = var4[var6];
            properties.setOwner(playerName, true);
         }
      } else {
         this.setPropertyClear(sender, properties);
      }

   }

   @PropertyCheckPermission("owners")
   @Command("cart owners clear")
   @CommandDescription("Clears all owners set for a cart, allowing everyone access")
   private void setPropertyClear(CommandSender sender, CartProperties properties) {
      properties.clearOwners();
      sender.sendMessage(ChatColor.GREEN + "Owners cleared! Everyone can now modify the cart.");
      if (properties.hasOwnerPermissions()) {
         this.getProperty(sender, properties);
      }

   }

   @Command("train owners")
   @CommandDescription("Display the owners set for carts of the train")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      MessageBuilder message = new MessageBuilder();
      this.addOwnerInfo(message, properties);
      message.send(sender);
   }

   @PropertyCheckPermission("owners")
   @Command("train claim")
   @CommandDescription("Sets the caller as the sole owner of a train")
   private void setPropertyClaim(Player sender, TrainProperties properties) {
      properties.clearOwners();
      properties.setOwner(sender.getName(), true);
      sender.sendMessage(ChatColor.GREEN + "You are now the only owner of this train!");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("owners")
   @Command("train owners add <player_names>")
   @CommandDescription("Adds players as owners of all carts of a train")
   private void setPropertyAdd(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("player_names") String[] playerNames) {
      if (playerNames != null && playerNames.length > 0) {
         sender.sendMessage(ChatColor.GREEN + "Adding owners to train: " + StringUtil.combineNames(playerNames));
         String[] var4 = playerNames;
         int var5 = playerNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String playerName = var4[var6];
            properties.setOwner(playerName, true);
         }
      }

      this.getProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("owners")
   @Command("train owners remove <player_names>")
   @CommandDescription("Removes players as owners of all carts of a train")
   private void setPropertyRemove(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("player_names") String[] playerNames) {
      if (playerNames != null && playerNames.length > 0) {
         sender.sendMessage(ChatColor.GREEN + "Removing owners from train: " + StringUtil.combineNames(playerNames));
         String[] var4 = playerNames;
         int var5 = playerNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String playerName = var4[var6];
            properties.setOwner(playerName, false);
         }
      }

      this.getProperty(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("owners")
   @Command("train owners set <player_names>")
   @CommandDescription("Discards previous owners and sets players as owners of all carts of a train")
   private void setProperty(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("player_names") String[] playerNames) {
      if (playerNames != null && playerNames.length > 0) {
         properties.clearOwners();
         sender.sendMessage(ChatColor.GREEN + "Set new owners of train: " + StringUtil.combineNames(playerNames));
         String[] var4 = playerNames;
         int var5 = playerNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String playerName = var4[var6];
            properties.setOwner(playerName, true);
         }
      } else {
         this.setPropertyClear(sender, properties);
      }

   }

   @PropertyCheckPermission("owners")
   @Command("train owners clear")
   @CommandDescription("Clears all owners set for a train, allowing everyone access")
   private void setPropertyClear(CommandSender sender, TrainProperties properties) {
      properties.clearOwners();
      sender.sendMessage(ChatColor.GREEN + "Owners cleared! Everyone can now modify the train.");
      if (properties.hasOwnerPermissions()) {
         this.getProperty(sender, properties);
      }

   }

   @PropertyParser("setowner|owners set")
   public Set<String> parseSet(String input) {
      return input.isEmpty() ? Collections.emptySet() : Collections.singleton(input.toLowerCase());
   }

   @PropertyParser("clearowner|clearowners|owners clear")
   public Set<String> parseClear(String input) {
      return Collections.emptySet();
   }

   @PropertyParser(
      value = "addowner|owners add",
      processPerCart = true
   )
   public Set<String> parseAdd(PropertyParseContext<Set<String>> context) {
      String name_lc = context.input().toLowerCase();
      if (!name_lc.isEmpty() && !((Set)context.current()).contains(name_lc)) {
         HashSet<String> newPerms = new HashSet((Collection)context.current());
         newPerms.add(name_lc);
         return Collections.unmodifiableSet(newPerms);
      } else {
         return (Set)context.current();
      }
   }

   @PropertyParser(
      value = "remowner|owners rem|owners remove",
      processPerCart = true
   )
   public Set<String> parseRemove(PropertyParseContext<Set<String>> context) {
      String name_lc = context.input().toLowerCase();
      if (!name_lc.isEmpty() && ((Set)context.current()).contains(name_lc)) {
         HashSet<String> newPerms = new HashSet((Collection)context.current());
         newPerms.remove(name_lc);
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
      return "owners";
   }

   public Set<String> getData(FieldBackedProperty.CartInternalData data) {
      return data.owners;
   }

   public void setData(FieldBackedProperty.CartInternalData data, Set<String> value) {
      data.owners = value;
   }

   public Optional<Set<String>> readFromConfig(ConfigurationNode config) {
      return Util.getConfigStringSetOptional(config, "owners");
   }

   public void writeToConfig(ConfigurationNode config, Optional<Set<String>> value) {
      Util.setConfigStringCollectionOptional(config, "owners", value);
   }

   public Set<String> get(TrainProperties properties) {
      return FieldBackedProperty.TrainInternalData.get(properties).owners.update(properties, this);
   }
}
