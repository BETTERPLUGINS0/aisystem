package com.ryandw11.structure.loottables;

import com.ryandw11.structure.utils.NumberStylizer;
import org.bukkit.configuration.ConfigurationSection;

public abstract class ConfigLootItem implements LootItem {
   private final LootTable lootTable;
   private final String itemID;
   private final int weight;
   private final String amount;

   public ConfigLootItem(LootTable lootTable, String itemID, int weight, String amount) {
      this.lootTable = lootTable;
      this.itemID = itemID;
      this.weight = weight;
      this.amount = amount;
   }

   public abstract void constructItem(ConfigurationSection var1);

   public final LootTable getLootTable() {
      return this.lootTable;
   }

   public final String getItemID() {
      return this.itemID;
   }

   public final int getWeight() {
      return this.weight;
   }

   public final int getAmount() {
      return NumberStylizer.getStylizedInt(this.amount);
   }

   public final String getRawAmount() {
      return this.amount;
   }
}
