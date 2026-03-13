package com.ryandw11.structure.loottables;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.exceptions.LootTableException;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootContext.Builder;

public class MinecraftLootTable extends LootTable {
   private final org.bukkit.loot.LootTable lootTable;

   public MinecraftLootTable(String nameSpaceString) {
      if (nameSpaceString.startsWith("minecraft:")) {
         this.lootTable = Bukkit.getLootTable(NamespacedKey.minecraft(nameSpaceString.replace("minecraft:", "")));
      } else {
         String[] keys = nameSpaceString.split(":");
         if (keys.length != 2) {
            throw new LootTableException("Specified minecraft loot table not found! (" + nameSpaceString + ")");
         }

         this.lootTable = Bukkit.getLootTable(new NamespacedKey(keys[0], keys[1]));
      }

      if (this.lootTable == null) {
         throw new LootTableException("Specified minecraft loot table not found! (" + nameSpaceString + ")");
      }
   }

   public String getName() {
      return this.lootTable.toString();
   }

   public int getRolls() {
      return 0;
   }

   public void setRolls(int rolls) {
      throw new UnsupportedOperationException("Rolls are not used in this LootTable.");
   }

   public void fillContainerInventory(Inventory inventory, Random random, Location location) {
      LootContext lootContext = (new Builder(location)).build();

      try {
         this.lootTable.fillInventory(inventory, random, lootContext);
      } catch (Exception var6) {
         CustomStructures.getInstance().getLogger().severe("Unable to fill loot table: " + this.getName() + "!");
         CustomStructures.getInstance().getLogger().severe("Does this loot table exist?");
      }

   }

   public void fillFurnaceInventory(FurnaceInventory furnaceInventory, Random random, Location location) {
      LootContext lootContext = (new Builder(location)).build();

      try {
         this.lootTable.fillInventory(furnaceInventory, random, lootContext);
      } catch (Exception var6) {
         CustomStructures.getInstance().getLogger().severe("Unable to fill loot table: " + this.getName() + "!");
         CustomStructures.getInstance().getLogger().severe("Does this loot table exist?");
      }

   }

   public void fillBrewerInventory(BrewerInventory brewerInventory, Random random, Location location) {
      LootContext lootContext = (new Builder(location)).build();

      try {
         this.lootTable.fillInventory(brewerInventory, random, lootContext);
      } catch (Exception var6) {
         CustomStructures.getInstance().getLogger().severe("Unable to fill loot table: " + this.getName() + "!");
         CustomStructures.getInstance().getLogger().severe("Does this loot table exist?");
      }

   }
}
