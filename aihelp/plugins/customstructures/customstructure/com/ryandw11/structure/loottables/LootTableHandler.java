package com.ryandw11.structure.loottables;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.exceptions.LootTableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootTableHandler {
   private final Map<String, LootTable> lootTables = new HashMap();
   private final Map<String, Class<? extends ConfigLootItem>> lootItems = new HashMap();

   public boolean addLootTable(LootTable lootTable) {
      if (this.lootTables.containsKey(lootTable.getName())) {
         return false;
      } else {
         this.lootTables.put(lootTable.getName(), lootTable);
         return true;
      }
   }

   public boolean addLootItem(String typeName, Class<? extends ConfigLootItem> lootItemClass) {
      if (this.lootItems.containsKey(typeName.toUpperCase())) {
         return false;
      } else {
         this.lootItems.put(typeName.toUpperCase(), lootItemClass);
         return true;
      }
   }

   public LootTable getLootTableByName(String lootTableName) {
      if (!this.lootTables.containsKey(lootTableName)) {
         try {
            if (lootTableName.contains(":")) {
               this.lootTables.put(lootTableName, new MinecraftLootTable(lootTableName));
            } else {
               this.lootTables.put(lootTableName, new ConfigLootTable(lootTableName));
            }
         } catch (LootTableException var3) {
            CustomStructures.getInstance().getLogger().severe("There seems to be a problem with the '" + lootTableName + "' loot table:");
            CustomStructures.getInstance().getLogger().severe(var3.getMessage());
         }
      }

      return (LootTable)this.lootTables.get(lootTableName);
   }

   public Map<String, LootTable> getLootTables() {
      return Collections.unmodifiableMap(this.lootTables);
   }

   public List<String> getLootTablesNames() {
      return new ArrayList(this.lootTables.keySet());
   }

   public Class<? extends ConfigLootItem> getLootItemClassByName(String typeName) {
      return (Class)this.lootItems.get(typeName);
   }

   public Map<String, Class<? extends ConfigLootItem>> getLootItems() {
      return Collections.unmodifiableMap(this.lootItems);
   }
}
