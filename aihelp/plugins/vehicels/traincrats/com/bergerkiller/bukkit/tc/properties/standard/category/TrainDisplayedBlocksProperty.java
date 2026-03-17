package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ITrainProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.TrainDisplayedBlocks;
import com.bergerkiller.bukkit.tc.signactions.SignActionBlockChanger;
import java.util.Optional;
import org.bukkit.command.CommandSender;

public class TrainDisplayedBlocksProperty implements ITrainProperty<TrainDisplayedBlocks> {
   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.BUILD_BLOCKCHANGER.has(sender);
   }

   public TrainDisplayedBlocks getDefault() {
      return TrainDisplayedBlocks.DEFAULT;
   }

   public void set(TrainProperties properties, TrainDisplayedBlocks value) {
      ITrainProperty.super.set((TrainProperties)properties, value);
      if (value != null) {
         MinecartGroup group = properties.getHolder();
         if (group != null) {
            SignActionBlockChanger.setBlocks(group, value);
         }
      }

   }

   public Optional<TrainDisplayedBlocks> readFromConfig(ConfigurationNode config) {
      if (!config.contains("blockTypes") && !config.contains("blockOffset")) {
         return Optional.empty();
      } else {
         String blockTypes = (String)config.getOrDefault("blockTypes", "");
         int blockOffset = (Integer)config.getOrDefault("blockOffset", Integer.MAX_VALUE);
         return Optional.of(TrainDisplayedBlocks.of(blockTypes, blockOffset));
      }
   }

   public void writeToConfig(ConfigurationNode config, Optional<TrainDisplayedBlocks> value) {
      if (value.isPresent()) {
         TrainDisplayedBlocks displayedBlocks = (TrainDisplayedBlocks)value.get();
         config.set("blockTypes", displayedBlocks.getBlockTypesPattern());
         config.set("blockOffset", displayedBlocks.getOffset());
      } else {
         config.remove("blockTypes");
         config.remove("blockOffset");
      }

   }
}
