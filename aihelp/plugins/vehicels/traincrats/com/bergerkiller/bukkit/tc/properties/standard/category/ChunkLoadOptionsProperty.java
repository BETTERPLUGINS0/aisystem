package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Flag;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorCondition;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.PropertySelectorCondition;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardTrainProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.ChunkLoadOptions;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class ChunkLoadOptionsProperty extends FieldBackedStandardTrainProperty<ChunkLoadOptions> {
   @CommandTargetTrain
   @PropertyCheckPermission("keeploaded")
   @Command("train keepchunksloaded|keeploaded|loadchunks <mode>")
   @CommandDescription("Sets whether the train keeps chunks loaded, how and optionally with what radius")
   private void commandSetProperty(CommandSender sender, TrainProperties properties, @Argument("mode") ChunkLoadOptions.Mode mode, @Flag("radius") Integer radius) {
      ChunkLoadOptions options = properties.getChunkLoadOptions();
      options = options.withMode(mode);
      if (radius != null) {
         int radInt = radius;
         if (radInt > TCConfig.maxKeepChunksLoadedRadius) {
            sender.sendMessage(ChatColor.RED + "Radius " + radInt + " is too big (max: " + TCConfig.maxKeepChunksLoadedRadius + ")");
            radInt = TCConfig.maxKeepChunksLoadedRadius;
         }

         options = options.withRadius(radInt);
      }

      properties.setChunkLoadOptions(options);
      this.commandGetProperty(sender, properties);
   }

   @Command("train keepchunksloaded|keeploaded|loadchunks")
   @CommandDescription("Gets the chunk loader configuration of the train")
   private void commandGetProperty(CommandSender sender, TrainProperties properties) {
      ChunkLoadOptions options = properties.getChunkLoadOptions();
      int rad = Math.min(TCConfig.maxKeepChunksLoadedRadius, options.radius()) * 2 + 1;
      String radInfo = options.keepLoaded() ? "" + ChatColor.WHITE + " (" + rad + " x " + rad + " chunks)" : "";
      sender.sendMessage(ChatColor.YELLOW + "Train keeps nearby chunks loaded: " + Localization.boolStr(options.keepLoaded()) + radInfo);
      if (options.keepLoaded()) {
         switch(options.mode()) {
         case FULL:
            sender.sendMessage(ChatColor.YELLOW + "The loaded chunks will simulate entities and redstone");
            break;
         case REDSTONE:
            sender.sendMessage(ChatColor.YELLOW + "The loaded chunks will only simulate redstone, not entities");
            break;
         case MINIMAL:
            sender.sendMessage(ChatColor.YELLOW + "The loaded chunks will " + ChatColor.RED + "not" + ChatColor.YELLOW + " simulate redstone and entities");
         }
      }

   }

   @PropertyParser("keepchunksloaded|keeploaded|keepcloaded|loadchunks")
   public ChunkLoadOptions parseChunkLoadOptions(PropertyParseContext<ChunkLoadOptions> context) {
      ChunkLoadOptions options = (ChunkLoadOptions)context.current();
      String[] var3 = context.input().split(" ");
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String word = var3[var5];
         Optional<ChunkLoadOptions.Mode> newMode = ChunkLoadOptions.Mode.fromName(word);
         if (newMode.isPresent()) {
            options = options.withMode((ChunkLoadOptions.Mode)newMode.get());
         } else {
            Integer radius = ParseUtil.parseInt(word, (Integer)null);
            if (radius != null) {
               options = options.withRadius(radius);
            }
         }
      }

      return options;
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_KEEPCHUNKSLOADED.has(sender);
   }

   @PropertySelectorCondition("keepchunksloaded")
   public boolean selectorMatchesKeepChunksLoaded(TrainProperties properties, SelectorCondition condition) {
      return condition.matchesBoolean(properties.isKeepingChunksLoaded());
   }

   public ChunkLoadOptions getDefault() {
      return ChunkLoadOptions.DEFAULT;
   }

   public ChunkLoadOptions getData(FieldBackedProperty.TrainInternalData data) {
      return data.chunkLoadOptions;
   }

   public void setData(FieldBackedProperty.TrainInternalData data, ChunkLoadOptions value) {
      data.chunkLoadOptions = value;
   }

   public Optional<ChunkLoadOptions> readFromConfig(ConfigurationNode config) {
      if (config.contains("keepChunksLoaded")) {
         if (config.isNode("keepChunksLoaded")) {
            ConfigurationNode node = config.getNode("keepChunksLoaded");
            ChunkLoadOptions.Mode mode = (ChunkLoadOptions.Mode)ChunkLoadOptions.Mode.fromName((String)node.get("mode", "disabled")).orElse(ChunkLoadOptions.Mode.DISABLED);
            int radius = Math.min(TCConfig.maxKeepChunksLoadedRadius, (Integer)node.get("radius", 2));
            return Optional.of(ChunkLoadOptions.of(mode, radius));
         } else {
            return Optional.of((Boolean)config.get("keepChunksLoaded", false) ? ChunkLoadOptions.LEGACY_TRUE : ChunkLoadOptions.LEGACY_FALSE);
         }
      } else {
         return Optional.empty();
      }
   }

   public void writeToConfig(ConfigurationNode config, Optional<ChunkLoadOptions> value) {
      if (value.isPresent()) {
         ChunkLoadOptions options = (ChunkLoadOptions)value.get();
         if (options.equals(ChunkLoadOptions.LEGACY_TRUE)) {
            config.set("keepChunksLoaded", true);
         } else if (options.equals(ChunkLoadOptions.LEGACY_FALSE)) {
            config.set("keepChunksLoaded", false);
         } else {
            ConfigurationNode node = config.getNode("keepChunksLoaded");
            node.set("mode", options.mode().getNames().get(0));
            node.set("radius", options.radius());
         }
      } else {
         config.remove("keepChunksLoaded");
      }

   }

   public void onConfigurationChanged(TrainProperties properties) {
      super.onConfigurationChanged(properties);
      this.updateState(properties, (ChunkLoadOptions)this.get(properties));
   }

   public void set(TrainProperties properties, ChunkLoadOptions value) {
      super.set(properties, value);
      this.updateState(properties, value);
   }

   private void updateState(TrainProperties properties, ChunkLoadOptions options) {
      if (options.keepLoaded()) {
         properties.restore().thenAccept((result) -> {
            if (result) {
               MinecartGroup group = properties.getHolder();
               if (group != null) {
                  group.keepChunksLoaded(group.getProperties().getChunkLoadOptions().mode());
               }
            }

         });
      } else {
         MinecartGroup group = properties.getHolder();
         if (group != null) {
            group.keepChunksLoaded(options.mode());
         }
      }

   }
}
