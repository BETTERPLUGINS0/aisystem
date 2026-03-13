package com.nisovin.shopkeepers.shopobjects;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

public final class ShopkeeperMetadata {
   public static final String SHOPKEEPER_METADATA_KEY = "shopkeeper";

   public static void apply(Entity entity) {
      Validate.notNull(entity, (String)"entity is null");
      entity.setMetadata("shopkeeper", new FixedMetadataValue(ShopkeepersPlugin.getInstance(), true));
   }

   public static void remove(Entity entity) {
      Validate.notNull(entity, (String)"entity is null");
      entity.removeMetadata("shopkeeper", ShopkeepersPlugin.getInstance());
   }

   public static boolean isTagged(Entity entity) {
      Validate.notNull(entity, (String)"entity is null");
      return entity.hasMetadata("shopkeeper");
   }

   public static void apply(Block block) {
      Validate.notNull(block, (String)"block is null");
      block.setMetadata("shopkeeper", new FixedMetadataValue(ShopkeepersPlugin.getInstance(), true));
   }

   public static void remove(Block block) {
      Validate.notNull(block, (String)"block is null");
      block.removeMetadata("shopkeeper", ShopkeepersPlugin.getInstance());
   }

   public static boolean isTagged(Block block) {
      Validate.notNull(block, (String)"block is null");
      return block.hasMetadata("shopkeeper");
   }

   private ShopkeeperMetadata() {
   }
}
