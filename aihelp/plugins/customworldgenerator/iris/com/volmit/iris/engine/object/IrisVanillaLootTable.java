package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import java.io.File;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootContext.Builder;

public class IrisVanillaLootTable extends IrisLootTable {
   private final LootTable lootTable;

   public String getName() {
      return "Vanilla " + String.valueOf(this.lootTable.getKey());
   }

   public int getRarity() {
      return 0;
   }

   public int getMaxPicked() {
      return 0;
   }

   public int getMinPicked() {
      return 0;
   }

   public int getMaxTries() {
      return 0;
   }

   public KList<IrisLoot> getLoot() {
      return new KList();
   }

   public KList<ItemStack> getLoot(boolean debug, RNG rng, InventorySlotType slot, World world, int x, int y, int z) {
      return new KList(this.lootTable.populateLoot(var2, (new Builder(new Location(var4, (double)var5, (double)var6, (double)var7))).build()));
   }

   public String getFolderName() {
      throw new UnsupportedOperationException("VanillaLootTables do not have a folder name");
   }

   public String getTypeName() {
      throw new UnsupportedOperationException("VanillaLootTables do not have a type name");
   }

   public File getLoadFile() {
      throw new UnsupportedOperationException("VanillaLootTables do not have a load file");
   }

   public IrisData getLoader() {
      throw new UnsupportedOperationException("VanillaLootTables do not have a loader");
   }

   public KList<String> getPreprocessors() {
      return new KList();
   }

   @Generated
   public IrisVanillaLootTable(final LootTable lootTable) {
      this.lootTable = var1;
   }

   @Generated
   public LootTable getLootTable() {
      return this.lootTable;
   }

   @Generated
   public String toString() {
      return "IrisVanillaLootTable(lootTable=" + String.valueOf(this.getLootTable()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisVanillaLootTable)) {
         return false;
      } else {
         IrisVanillaLootTable var2 = (IrisVanillaLootTable)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            LootTable var3 = this.getLootTable();
            LootTable var4 = var2.getLootTable();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisVanillaLootTable;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      LootTable var3 = this.getLootTable();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
