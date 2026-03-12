package com.nisovin.shopkeepers.dependencies.citizens;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.util.DataKey;
import net.citizensnpcs.api.util.MemoryDataKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public final class CitizensUtils {
   public static boolean isNPC(Entity entity) {
      Validate.notNull(entity, (String)"entity is null");
      return entity.hasMetadata("NPC");
   }

   private CitizensUtils() {
   }

   public static final class Internal {
      public static DataKey toDataKey(DataContainer dataContainer) {
         Validate.notNull(dataContainer, (String)"dataContainer is null");
         DataKey dataKey = new MemoryDataKey();
         insertIntoDataKey(dataKey, "", dataContainer);
         return dataKey;
      }

      private static void insertIntoDataKey(DataKey dataKey, String parentPath, DataContainer dataContainer) {
         assert dataKey != null && dataContainer != null;

         dataContainer.getValues().forEach((key, value) -> {
            String path = parentPath.isEmpty() ? key : parentPath + "." + key;
            DataContainer valueContainer = DataContainer.of(value);
            if (valueContainer != null) {
               insertIntoDataKey(dataKey, path, valueContainer);
            } else {
               dataKey.setRaw(path, value);
            }

         });
      }

      public static DataContainer toDataContainer(MemoryDataKey dataKey) {
         Validate.notNull(dataKey, (String)"dataKey is null");
         return DataContainer.ofNonNull(Unsafe.assertNonNull(dataKey.getRaw("")));
      }

      public static DataContainer saveNpc(NPC npc) {
         Validate.notNull(npc, (String)"npc is null");
         MemoryDataKey dataKey = new MemoryDataKey();
         npc.save(dataKey);
         return toDataContainer(dataKey);
      }

      public static void loadNpc(NPC npc, DataContainer npcData) {
         Validate.notNull(npc, (String)"npc is null");
         Validate.notNull(npcData, (String)"npcData is null");
         if (npc.getEntity() != null && !npc.despawn()) {
            Log.warning("Failed to despawn Citizens NPC " + npc.getId() + "! Some changes to the NPC's data might have no effect!");
         }

         DataKey npcDataKey = toDataKey(npcData);
         String mobTypeName = npcDataKey.getString("traits.type", EntityType.PLAYER.name());
         EntityType mobType = EntityUtils.parseEntityType(mobTypeName);
         if (mobType == null) {
            Log.warning("Failed to parse Citizens NPC mob type: " + mobTypeName);
         } else {
            npc.setBukkitEntityType(mobType);
         }

         npc.load(npcDataKey);
      }

      private Internal() {
      }
   }
}
