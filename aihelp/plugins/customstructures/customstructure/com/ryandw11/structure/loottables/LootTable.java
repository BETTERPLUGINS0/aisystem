package com.ryandw11.structure.loottables;

import com.ryandw11.structure.schematic.LootTableReplacer;
import com.ryandw11.structure.utils.RandomCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class LootTable {
   protected RandomCollection<LootItem> randomCollection = new RandomCollection();
   protected List<LootTableType> types = new ArrayList();

   public abstract String getName();

   public abstract int getRolls();

   public abstract void setRolls(int var1);

   public void fillContainerInventory(Inventory inventory, Random random, Location location) {
      LootTableReplacer.replaceChestContent(this, random, inventory);
   }

   public void fillFurnaceInventory(FurnaceInventory furnaceInventory, Random random, Location location) {
      LootTableReplacer.replaceFurnaceContent(this, furnaceInventory);
   }

   public void fillBrewerInventory(BrewerInventory brewerInventory, Random random, Location location) {
      LootTableReplacer.replaceBrewerContent(this, brewerInventory);
   }

   public ItemStack getRandomWeightedItem() {
      return ((LootItem)this.randomCollection.next()).getItemStack();
   }

   public List<LootItem> getItems() {
      return this.randomCollection.toList();
   }

   protected final void addLootItem(double weight, @NotNull LootItem lootItem) {
      this.randomCollection.add(weight, lootItem);
   }

   public final List<LootTableType> getTypes() {
      return this.types;
   }

   public final void setTypes(List<LootTableType> types) {
      this.types = types;
   }

   public final void addType(LootTableType type) {
      this.types.add(type);
   }
}
