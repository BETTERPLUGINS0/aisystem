package com.dfsek.terra.bukkit.util;

import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BukkitUtils {
   private static final Logger logger = LoggerFactory.getLogger(BukkitUtils.class);

   public static boolean isLiquid(BlockData blockState) {
      Material material = blockState.getMaterial();
      return material == Material.WATER || material == Material.LAVA;
   }

   public static EntityType getEntityType(String id) {
      String entityID;
      if (!id.contains(":")) {
         entityID = "minecraft:" + id.toLowerCase();
         logger.warn("Translating " + id + " to " + entityID + ". In 1.20.3 entity parsing was reworked. You are advised to perform this rename in your config backs as this translation will be removed in the next major version of Terra.");
      }

      if (!id.startsWith("minecraft:")) {
         throw new IllegalArgumentException("Invalid entity identifier " + id);
      } else {
         entityID = id.toUpperCase(Locale.ROOT).substring(10);
         BukkitEntityType var10000 = new BukkitEntityType;
         byte var3 = -1;
         switch(entityID.hashCode()) {
         case -402794606:
            if (entityID.equals("END_CRYSTAL")) {
               var3 = 0;
            }
            break;
         case 2041798783:
            if (entityID.equals("ENDER_CRYSTAL")) {
               var3 = 1;
            }
         }

         org.bukkit.entity.EntityType var10002;
         switch(var3) {
         case 0:
            var10002 = org.bukkit.entity.EntityType.END_CRYSTAL;
            break;
         case 1:
            throw new IllegalArgumentException("Invalid entity identifier " + id);
         default:
            var10002 = org.bukkit.entity.EntityType.valueOf(entityID);
         }

         var10000.<init>(var10002);
         return var10000;
      }
   }
}
