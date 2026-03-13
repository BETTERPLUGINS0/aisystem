package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("A loot mode is used to describe what to do with the existing loot layers before adding this loot. Using ADD will simply add this table to the building list of tables (i.e. add dimension tables, region tables then biome tables). By using clear or replace, you remove the parent tables before and add just your tables.")
public enum IrisLootMode {
   @Desc("Add to the existing parent loot tables")
   ADD,
   @Desc("Clear all loot tables then add this table")
   CLEAR,
   @Desc("Replace all loot tables with this table (same as clear)")
   REPLACE,
   @Desc("Only use when there was no loot table defined by an object")
   FALLBACK;

   // $FF: synthetic method
   private static IrisLootMode[] $values() {
      return new IrisLootMode[]{ADD, CLEAR, REPLACE, FALLBACK};
   }
}
