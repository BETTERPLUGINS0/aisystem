package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("An inventory slot type is used to represent a type of slot for items to fit into in any given inventory.")
public enum InventorySlotType {
   @Desc("Typically the one you want to go with. Storage represnents most slots in inventories.")
   STORAGE,
   @Desc("Used for the fuel slot in Furnaces, Blast furnaces, smokers etc.")
   FUEL,
   @Desc("Used for the cook slot in furnaces")
   FURNACE,
   @Desc("Used for the cook slot in blast furnaces")
   BLAST_FURNACE,
   @Desc("Used for the cook slot in smokers")
   SMOKER;

   // $FF: synthetic method
   private static InventorySlotType[] $values() {
      return new InventorySlotType[]{STORAGE, FUEL, FURNACE, BLAST_FURNACE, SMOKER};
   }
}
