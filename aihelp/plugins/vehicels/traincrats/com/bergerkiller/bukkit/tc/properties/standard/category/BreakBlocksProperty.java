package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.FlagYielding;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardCartProperty;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public final class BreakBlocksProperty extends FieldBackedStandardCartProperty<Set<Material>> {
   @Command("cart breakblocks|break")
   @CommandDescription("Displays what block types are broken by the cart")
   private void getProperty(CommandSender sender, CartProperties properties) {
      Collection<Material> types = properties.getBlockBreakTypes();
      sender.sendMessage(ChatColor.YELLOW + "This cart breaks: " + ChatColor.WHITE + StringUtil.combineNames(types));
   }

   @Command("train breakblocks|break")
   @CommandDescription("Displays what block types are broken by the train")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      Set<Material> types = new HashSet();
      Iterator var4 = properties.iterator();

      while(var4.hasNext()) {
         CartProperties cprop = (CartProperties)var4.next();
         types.addAll(cprop.getBlockBreakTypes());
      }

      sender.sendMessage(ChatColor.YELLOW + "This train breaks: " + ChatColor.WHITE + StringUtil.combineNames(types));
   }

   @CommandTargetTrain
   @PropertyCheckPermission("breakblocks")
   @Command("cart breakblocks|break clear")
   @CommandDescription("Clears the list of blocks broken by the cart, disabling it")
   private void setPropertyClear(CommandSender sender, CartProperties properties) {
      properties.clearBlockBreakTypes();
      sender.sendMessage(ChatColor.YELLOW + "Block break types have been cleared!");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("breakblocks")
   @Command("train breakblocks|break clear")
   @CommandDescription("Clears the list of blocks broken by the train, disabling it")
   private void setPropertyClear(CommandSender sender, TrainProperties properties) {
      Iterator var3 = properties.iterator();

      while(var3.hasNext()) {
         CartProperties cProp = (CartProperties)var3.next();
         cProp.clearBlockBreakTypes();
      }

      sender.sendMessage(ChatColor.YELLOW + "Train block break types have been cleared!");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("breakblocks")
   @Command("cart breakblocks|break <block_types>")
   @CommandDescription("Sets the list of blocks broken by the cart")
   private void setProperty(CommandSender sender, CartProperties properties, @FlagYielding @Argument("block_types") String[] args) {
      boolean anyblock = Permission.PROPERTY_BREAKBLOCKS_ADMIN.has(sender);
      boolean asBreak = true;
      boolean lastIsBool = ParseUtil.isBool(args[args.length - 1]);
      if (lastIsBool) {
         asBreak = ParseUtil.parseBool(args[args.length - 1]);
      }

      int count = lastIsBool ? args.length - 1 : args.length;
      Set<Material> mats = new HashSet();

      for(int i = 0; i < count; ++i) {
         Material mat = ParseUtil.parseMaterial(args[i], (Material)null);
         if (mat != null) {
            if (!anyblock && !TrainCarts.canBreak(mat)) {
               sender.sendMessage(ChatColor.RED + "You are not allowed to make this cart break '" + mat.toString() + "'!");
            } else {
               mats.add(mat);
            }
         }
      }

      if (mats.isEmpty()) {
         sender.sendMessage(ChatColor.RED + "Failed to find possible and allowed block types in the list given.");
      } else {
         if (asBreak) {
            properties.update(this, (blocks) -> {
               HashSet<Material> new_blocks = new HashSet(blocks);
               new_blocks.addAll(mats);
               return new_blocks;
            });
            sender.sendMessage(ChatColor.YELLOW + "This cart can now (also) break: " + ChatColor.WHITE + StringUtil.combineNames(mats));
         } else {
            properties.update(this, (blocks) -> {
               HashSet<Material> new_blocks = new HashSet(blocks);
               new_blocks.removeAll(mats);
               return new_blocks;
            });
            sender.sendMessage(ChatColor.YELLOW + "This cart can no longer break: " + ChatColor.WHITE + StringUtil.combineNames(mats));
         }

      }
   }

   @CommandTargetTrain
   @PropertyCheckPermission("breakblocks")
   @Command("train breakblocks|break <block_types>")
   @CommandDescription("Sets the list of blocks broken by the train")
   private void setProperty(CommandSender sender, TrainProperties properties, @FlagYielding @Argument("block_types") String[] args) {
      boolean anyblock = Permission.PROPERTY_BREAKBLOCKS_ADMIN.has(sender);
      boolean asBreak = true;
      boolean lastIsBool = ParseUtil.isBool(args[args.length - 1]);
      if (lastIsBool) {
         asBreak = ParseUtil.parseBool(args[args.length - 1]);
      }

      int count = lastIsBool ? args.length - 1 : args.length;
      Set<Material> mats = new HashSet();

      for(int i = 0; i < count; ++i) {
         Material mat = ParseUtil.parseMaterial(args[i], (Material)null);
         if (mat != null) {
            if (!anyblock && !TrainCarts.canBreak(mat)) {
               sender.sendMessage(ChatColor.RED + "You are not allowed to make this train break '" + mat.toString() + "'!");
            } else {
               mats.add(mat);
            }
         }
      }

      if (mats.isEmpty()) {
         sender.sendMessage(ChatColor.RED + "Failed to find possible and allowed block types in the list given.");
      } else {
         Iterator var11;
         CartProperties cprop;
         if (asBreak) {
            var11 = properties.iterator();

            while(var11.hasNext()) {
               cprop = (CartProperties)var11.next();
               cprop.update(this, (blocks) -> {
                  HashSet<Material> new_blocks = new HashSet(blocks);
                  new_blocks.addAll(mats);
                  return new_blocks;
               });
            }

            sender.sendMessage(ChatColor.YELLOW + "This cart can now (also) break: " + ChatColor.WHITE + StringUtil.combineNames(mats));
         } else {
            var11 = properties.iterator();

            while(var11.hasNext()) {
               cprop = (CartProperties)var11.next();
               cprop.update(this, (blocks) -> {
                  HashSet<Material> new_blocks = new HashSet(blocks);
                  new_blocks.removeAll(mats);
                  return new_blocks;
               });
            }

            sender.sendMessage(ChatColor.YELLOW + "This cart can no longer break: " + ChatColor.WHITE + StringUtil.combineNames(mats));
         }

      }
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_BREAKBLOCKS_NORMAL.has(sender) || Permission.PROPERTY_BREAKBLOCKS_ADMIN.has(sender);
   }

   public Set<Material> getDefault() {
      return Collections.emptySet();
   }

   public Set<Material> getData(FieldBackedProperty.CartInternalData data) {
      return data.blockBreakTypes;
   }

   public void setData(FieldBackedProperty.CartInternalData data, Set<Material> value) {
      data.blockBreakTypes = value;
   }

   public Optional<Set<Material>> readFromConfig(ConfigurationNode config) {
      return config.contains("blockBreakTypes") ? Optional.of(Collections.unmodifiableSet((Set)config.getList("blockBreakTypes", String.class).stream().map((name) -> {
         return ParseUtil.parseMaterial(name, (Material)null);
      }).filter((m) -> {
         return m != null;
      }).collect(Collectors.toSet()))) : Optional.empty();
   }

   public void writeToConfig(ConfigurationNode config, Optional<Set<Material>> value) {
      if (value.isPresent()) {
         config.set("blockBreakTypes", ((Set)value.get()).stream().map(Enum::toString).collect(Collectors.toList()));
      } else {
         config.remove("blockBreakTypes");
      }

   }
}
