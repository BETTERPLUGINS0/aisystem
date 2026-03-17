package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.FlagYielding;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorCondition;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.IStringSetProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.PropertySelectorCondition;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardCartProperty;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class TagSetProperty extends FieldBackedStandardCartProperty<Set<String>> implements IStringSetProperty {
   @CommandTargetTrain
   @Command("train tags")
   @CommandDescription("Displays the tags set for the carts of a train")
   private void getTrainTags(CommandSender sender, TrainProperties properties) {
      if (properties.hasTags()) {
         sender.sendMessage(ChatColor.YELLOW + "Train has tags: " + ChatColor.WHITE + StringUtil.combineNames(properties.getTags()));
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Train has tags: " + ChatColor.RED + "None");
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("tags")
   @Command("train tags clear")
   @CommandDescription("Clears the previous tags for a train")
   private void setCartTags(CommandSender sender, TrainProperties properties) {
      this.setTrainTags(sender, properties, (String[])null);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("tags")
   @Command("train tags set [tags]")
   @CommandDescription("Clears the previous tags and sets new tags for carts of the train")
   private void setTrainTags(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("tags") String[] tags) {
      if (tags != null && tags.length > 0) {
         properties.setTags(tags);
         sender.sendMessage(ChatColor.GREEN + "Train tags set to: " + ChatColor.WHITE + StringUtil.combineNames(properties.getTags()));
      } else {
         properties.setTags();
         sender.sendMessage(ChatColor.GREEN + "Tags of train have been cleared.");
      }

   }

   @Command("cart tags")
   @CommandDescription("Displays the tags set for a cart")
   private void getCartTags(CommandSender sender, CartProperties properties) {
      if (properties.hasTags()) {
         sender.sendMessage(ChatColor.YELLOW + "Cart has tags: " + ChatColor.WHITE + StringUtil.combineNames(properties.getTags()));
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Cart has tags: " + ChatColor.RED + "None");
      }

   }

   @PropertyCheckPermission("tags")
   @Command("cart tags clear")
   @CommandDescription("Clears the previous tags for a cart")
   private void setCartTags(CommandSender sender, CartProperties properties) {
      this.setCartTags(sender, properties, (String[])null);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("tags")
   @Command("cart tags set [tags]")
   @CommandDescription("Clears the previous tags and sets new tags for a cart")
   private void setCartTags(CommandSender sender, CartProperties properties, @FlagYielding @Argument("tags") String[] tags) {
      if (tags != null && tags.length > 0) {
         properties.setTags(tags);
         sender.sendMessage(ChatColor.GREEN + "Cart tags set to: " + ChatColor.WHITE + StringUtil.combineNames(properties.getTags()));
      } else {
         properties.setTags();
         sender.sendMessage(ChatColor.GREEN + "Tags of cart have been cleared.");
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("tags")
   @Command("train tags add|add_many <tags>")
   @CommandDescription("Adds one or more tags to the train")
   private void addTrainSingleTag(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("tags") String[] tags) {
      if (tags != null && tags.length > 0) {
         properties.addTags(tags);
         sender.sendMessage(ChatColor.GREEN + "Added tags: " + ChatColor.WHITE + StringUtil.combineNames(tags));
      }

      this.getTrainTags(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("tags")
   @Command("train tags remove|remove_many <tags>")
   @CommandDescription("Removes one or more tags from the train")
   private void removeTrainSingleTag(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("tags") String[] tags) {
      if (tags != null && tags.length > 0) {
         properties.removeTags(tags);
         sender.sendMessage(ChatColor.GREEN + "Removed tags: " + ChatColor.WHITE + StringUtil.combineNames(tags));
      }

      this.getTrainTags(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("tags")
   @Command("cart tags add|add_many <tags>")
   @CommandDescription("Adds one or more tags to the cart")
   private void addCartSingleTag(CommandSender sender, CartProperties properties, @FlagYielding @Argument("tags") String[] tags) {
      if (tags != null && tags.length > 0) {
         properties.addTags(tags);
         sender.sendMessage(ChatColor.GREEN + "Added tags: " + ChatColor.WHITE + StringUtil.combineNames(tags));
      }

      this.getCartTags(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("tags")
   @Command("cart tags remove|remove_many <tags>")
   @CommandDescription("Removes one or more tags from the cart")
   private void removeCartSingleTag(CommandSender sender, CartProperties properties, @FlagYielding @Argument("tags") String[] tags) {
      if (tags != null && tags.length > 0) {
         properties.removeTags(tags);
         sender.sendMessage(ChatColor.GREEN + "Removed tags: " + ChatColor.WHITE + StringUtil.combineNames(tags));
      }

      this.getCartTags(sender, properties);
   }

   @PropertyParser("settag|tags set")
   public Set<String> parse(String input) {
      return Collections.singleton(input);
   }

   @PropertyParser(
      value = "addtag|tags add",
      processPerCart = true
   )
   public Set<String> parseAddTag(PropertyParseContext<Set<String>> context) {
      if (context.input().isEmpty()) {
         return (Set)context.current();
      } else if (((Set)context.current()).isEmpty()) {
         return Collections.singleton(context.input());
      } else if (((Set)context.current()).contains(context.input())) {
         return (Set)context.current();
      } else {
         HashSet<String> newTags = new HashSet((Collection)context.current());
         newTags.add(context.input());
         return Collections.unmodifiableSet(newTags);
      }
   }

   @PropertyParser(
      value = "remtag|removetag|tags remove",
      processPerCart = true
   )
   public Set<String> parseRemoveTag(PropertyParseContext<Set<String>> context) {
      if (!context.input().isEmpty() && ((Set)context.current()).contains(context.input())) {
         if (((Set)context.current()).size() == 1) {
            return Collections.emptySet();
         } else {
            HashSet<String> newTags = new HashSet((Collection)context.current());
            newTags.remove(context.input());
            return Collections.unmodifiableSet(newTags);
         }
      } else {
         return (Set)context.current();
      }
   }

   @PropertySelectorCondition("tag")
   public boolean selectorMatchesAnyTag(TrainProperties properties, SelectorCondition condition) {
      return condition.matchesAnyText((Collection)this.get(properties));
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_TAGS.has(sender);
   }

   public Set<String> getDefault() {
      return Collections.emptySet();
   }

   public String getListedName() {
      return "tags";
   }

   public Set<String> getData(FieldBackedProperty.CartInternalData data) {
      return data.tags;
   }

   public void setData(FieldBackedProperty.CartInternalData data, Set<String> value) {
      data.tags = value;
   }

   public Optional<Set<String>> readFromConfig(ConfigurationNode config) {
      return Util.getConfigStringSetOptional(config, "tags");
   }

   public void writeToConfig(ConfigurationNode config, Optional<Set<String>> value) {
      Util.setConfigStringCollectionOptional(config, "tags", value);
   }

   public Set<String> get(TrainProperties properties) {
      return FieldBackedProperty.TrainInternalData.get(properties).tags.update(properties, this);
   }
}
