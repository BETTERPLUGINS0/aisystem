package com.volmit.iris.engine.framework;

import com.volmit.iris.engine.object.InventorySlotType;
import com.volmit.iris.engine.object.IrisLootReference;
import com.volmit.iris.engine.object.IrisLootTable;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public interface LootProvider {
   void scramble(Inventory inventory, RNG rng);

   void injectTables(KList<IrisLootTable> list, IrisLootReference r, boolean fallback);

   KList<IrisLootTable> getLootTables(RNG rng, Block b);

   void addItems(boolean debug, Inventory inv, RNG rng, KList<IrisLootTable> tables, InventorySlotType slot, World world, int x, int y, int z, int mgf);
}
