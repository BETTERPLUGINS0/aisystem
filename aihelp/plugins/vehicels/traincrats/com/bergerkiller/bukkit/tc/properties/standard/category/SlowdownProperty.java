package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardTrainProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.SlowdownMode;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class SlowdownProperty extends FieldBackedStandardTrainProperty<Set<SlowdownMode>> {
   private final Set<SlowdownMode> ALL = Collections.unmodifiableSet(EnumSet.allOf(SlowdownMode.class));
   private final Set<SlowdownMode> NONE = Collections.unmodifiableSet(EnumSet.noneOf(SlowdownMode.class));

   public void appendSlowdownInfo(MessageBuilder message, TrainProperties properties) {
      message.yellow(new Object[]{"Slow down over time: "});
      if (properties.isSlowingDownAll()) {
         message.green(new Object[]{"Yes (All)"});
      } else if (properties.isSlowingDownNone()) {
         message.red(new Object[]{"No (None)"});
      } else {
         message.setSeparator(", ");
         SlowdownMode[] var3 = SlowdownMode.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SlowdownMode mode = var3[var5];
            if (properties.isSlowingDown(mode)) {
               message.green(new Object[]{mode.getKey() + "[Yes]"});
            } else {
               message.red(new Object[]{mode.getKey() + "[No]"});
            }
         }

         message.clearSeparator();
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("slowdown")
   @Command("train slowdown <mode> <enabled>")
   @CommandDescription("Sets whether trains slow down and speed up due to a particular type of slow-down mode")
   private void trainSetSlowdownMode(CommandSender sender, TrainProperties properties, @Argument("mode") SlowdownMode mode, @Argument("enabled") boolean enabled) {
      properties.setSlowingDown(mode, enabled);
      this.trainGetSlowdownMode(sender, properties, mode);
   }

   @Command("train slowdown <mode>")
   @CommandDescription("Gets whether trains slow down and speed up for a particular slow-down mode")
   private void trainGetSlowdownMode(CommandSender sender, TrainProperties properties, @Argument("mode") SlowdownMode mode) {
      sender.sendMessage(ChatColor.YELLOW + "Train slows down over time due to " + ChatColor.BLUE + mode.getKey() + ChatColor.YELLOW + ": " + (properties.isSlowingDown(mode) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
   }

   @CommandTargetTrain
   @PropertyCheckPermission("slowdown")
   @Command("train slowdown all|true|enable|enabled")
   @CommandDescription("Enables all default modes of slowing down")
   private void trainSetSlowdownAll(CommandSender sender, TrainProperties properties) {
      properties.setSlowingDown(true);
      this.trainGetSlowdownModes(sender, properties);
   }

   @CommandTargetTrain
   @PropertyCheckPermission("slowdown")
   @Command("train slowdown none|false|disable|disabled")
   @CommandDescription("Disables all default modes of slowing down")
   private void trainSetSlowdownNone(CommandSender sender, TrainProperties properties) {
      properties.setSlowingDown(false);
      this.trainGetSlowdownModes(sender, properties);
   }

   @Command("train slowdown")
   @CommandDescription("Gets what types of slow-down are enabled for a train")
   private void trainGetSlowdownModes(CommandSender sender, TrainProperties properties) {
      MessageBuilder message = new MessageBuilder();
      this.appendSlowdownInfo(message, properties);
      message.send(sender);
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_SLOWDOWN.has(sender);
   }

   private Set<SlowdownMode> wrapAndOptimize(Set<SlowdownMode> result) {
      if (result.isEmpty()) {
         return this.NONE;
      } else {
         return result.size() == this.ALL.size() ? this.ALL : Collections.unmodifiableSet(result);
      }
   }

   @PropertyParser("slowdown")
   public Set<SlowdownMode> parseSlowdownAll(PropertyParseContext<Set<SlowdownMode>> context) {
      if (context.input().equalsIgnoreCase("all")) {
         return this.ALL;
      } else if (context.input().equalsIgnoreCase("none")) {
         return this.NONE;
      } else {
         return context.inputBoolean() ? this.ALL : this.NONE;
      }
   }

   @PropertyParser("slowfriction")
   public Set<SlowdownMode> parseSlowdownFriction(PropertyParseContext<Set<SlowdownMode>> context) {
      EnumSet<SlowdownMode> values = EnumSet.noneOf(SlowdownMode.class);
      values.addAll((Collection)context.current());
      LogicUtil.addOrRemove(values, SlowdownMode.FRICTION, context.inputBoolean());
      return this.wrapAndOptimize(values);
   }

   @PropertyParser("slowgravity")
   public Set<SlowdownMode> parseSlowdownGravity(PropertyParseContext<Set<SlowdownMode>> context) {
      EnumSet<SlowdownMode> values = EnumSet.noneOf(SlowdownMode.class);
      values.addAll((Collection)context.current());
      LogicUtil.addOrRemove(values, SlowdownMode.GRAVITY, context.inputBoolean());
      return this.wrapAndOptimize(values);
   }

   public Set<SlowdownMode> getDefault() {
      return this.ALL;
   }

   public Set<SlowdownMode> getData(FieldBackedProperty.TrainInternalData data) {
      return data.slowdown;
   }

   public void setData(FieldBackedProperty.TrainInternalData data, Set<SlowdownMode> value) {
      data.slowdown = value;
   }

   public Optional<Set<SlowdownMode>> readFromConfig(ConfigurationNode config) {
      if (!config.contains("slowDown")) {
         return Optional.empty();
      } else if (!config.isNode("slowDown")) {
         return Optional.of((Boolean)config.get("slowDown", true) ? this.ALL : this.NONE);
      } else {
         EnumSet<SlowdownMode> modes = EnumSet.noneOf(SlowdownMode.class);
         ConfigurationNode slowDownNode = config.getNode("slowDown");
         SlowdownMode[] var4 = SlowdownMode.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            SlowdownMode mode = var4[var6];
            if (slowDownNode.contains(mode.getKey()) && (Boolean)slowDownNode.get(mode.getKey(), true)) {
               modes.add(mode);
            }
         }

         return Optional.of(this.wrapAndOptimize(modes));
      }
   }

   public void writeToConfig(ConfigurationNode config, Optional<Set<SlowdownMode>> value) {
      if (value.isPresent()) {
         Set<SlowdownMode> modes = (Set)value.get();
         if (modes.isEmpty()) {
            config.set("slowDown", false);
         } else if (modes.equals(this.ALL)) {
            config.set("slowDown", true);
         } else {
            ConfigurationNode slowDownNode = config.getNode("slowDown");
            slowDownNode.clear();
            SlowdownMode[] var5 = SlowdownMode.values();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               SlowdownMode mode = var5[var7];
               slowDownNode.set(mode.getKey(), modes.contains(mode));
            }
         }
      } else {
         config.remove("slowDown");
      }

   }
}
