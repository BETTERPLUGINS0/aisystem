package com.ryandw11.structure.schematic;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.LootPopulateEvent;
import com.ryandw11.structure.loottables.LootTable;
import com.ryandw11.structure.loottables.LootTableType;
import com.ryandw11.structure.structure.Structure;
import com.ryandw11.structure.utils.RandomCollection;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LootTableReplacer {
   private LootTableReplacer() {
   }

   protected static void replaceContainerContent(Structure structure, Location location) {
      BlockState blockState = location.getBlock().getState();
      Container container = (Container)blockState;
      Inventory containerInventory = container.getInventory();
      Block block = location.getBlock();
      LootTableType blockType = LootTableType.valueOf(block.getType());
      int numberOfFills = 1;
      boolean explictLoottableDefined = false;
      LootTable lootTable = null;
      if (containerInventory.getItem(0) != null) {
         ItemStack paper = containerInventory.getItem(0);
         if (paper.getType() == Material.PAPER && paper.hasItemMeta() && paper.getItemMeta().hasDisplayName() && paper.getItemMeta().getDisplayName().contains("%${") && paper.getItemMeta().getDisplayName().contains("}$%")) {
            String name = paper.getItemMeta().getDisplayName().replace("%${", "").replace("}$%", "");
            lootTable = CustomStructures.getInstance().getLootTableHandler().getLootTableByName(name);
            numberOfFills = Math.min(20, paper.getAmount());
            containerInventory.clear();
            explictLoottableDefined = true;
         }
      }

      if (lootTable == null) {
         if (structure.getLootTables().isEmpty()) {
            return;
         }

         RandomCollection<LootTable> tables = structure.getLootTables(blockType);
         if (tables == null) {
            return;
         }

         lootTable = (LootTable)tables.next();
      }

      Random random = new Random();
      LootPopulateEvent event = new LootPopulateEvent(structure, location, lootTable);
      Bukkit.getServer().getPluginManager().callEvent(event);
      if (!event.isCanceled()) {
         for(int i = 0; i < numberOfFills; ++i) {
            if ((lootTable.getTypes().contains(blockType) || explictLoottableDefined) && containerInventory instanceof FurnaceInventory) {
               lootTable.fillFurnaceInventory((FurnaceInventory)containerInventory, random, container.getLocation());
            } else if ((lootTable.getTypes().contains(blockType) || explictLoottableDefined) && containerInventory instanceof BrewerInventory) {
               lootTable.fillBrewerInventory((BrewerInventory)containerInventory, random, container.getLocation());
            } else if (lootTable.getTypes().contains(blockType) || explictLoottableDefined) {
               lootTable.fillContainerInventory(containerInventory, random, container.getLocation());
            }
         }

      }
   }

   public static void replaceChestContent(LootTable lootTable, Random random, Inventory containerInventory) {
      for(int roll = 0; roll < lootTable.getRolls(); ++roll) {
         ItemStack[] containerContent = containerInventory.getContents();
         ItemStack randomItem = lootTable.getRandomWeightedItem();

         for(int j = 0; j < randomItem.getAmount(); ++j) {
            boolean done = false;
            int attemps = 0;

            while(!done) {
               int randomPos = random.nextInt(containerContent.length);
               ItemStack randomPosItem = containerInventory.getItem(randomPos);
               ItemStack randomItemCopy;
               if (randomPosItem != null) {
                  if (isSameItem(randomPosItem, randomItem) && randomPosItem.getAmount() < randomItem.getMaxStackSize()) {
                     randomItemCopy = randomItem.clone();
                     int newAmount = randomPosItem.getAmount() + 1;
                     randomItemCopy.setAmount(newAmount);
                     containerContent[randomPos] = randomItemCopy;
                     containerInventory.setContents(containerContent);
                     done = true;
                  }
               } else {
                  randomItemCopy = randomItem.clone();
                  randomItemCopy.setAmount(1);
                  containerContent[randomPos] = randomItemCopy;
                  containerInventory.setContents(containerContent);
                  done = true;
               }

               ++attemps;
               if (attemps >= containerContent.length) {
                  done = true;
               }
            }
         }
      }

   }

   public static void replaceBrewerContent(LootTable lootTable, BrewerInventory containerInventory) {
      for(int roll = 0; roll < lootTable.getRolls(); ++roll) {
         ItemStack item = lootTable.getRandomWeightedItem();
         ItemStack ingredient = containerInventory.getIngredient();
         ItemStack fuel = containerInventory.getFuel();
         if (ingredient != null && !ingredient.equals(item)) {
            if (fuel == null || fuel.equals(item)) {
               containerInventory.setFuel(item);
            }
         } else {
            containerInventory.setIngredient(item);
         }
      }

   }

   public static void replaceFurnaceContent(LootTable lootTable, FurnaceInventory containerInventory) {
      for(int roll = 0; roll < lootTable.getRolls(); ++roll) {
         ItemStack item = lootTable.getRandomWeightedItem();
         ItemStack result = containerInventory.getResult();
         ItemStack fuel = containerInventory.getFuel();
         ItemStack smelting = containerInventory.getSmelting();
         if (result != null && !result.equals(item)) {
            if (fuel != null && !fuel.equals(item)) {
               if (smelting == null || smelting.equals(item)) {
                  containerInventory.setSmelting(item);
               }
            } else {
               containerInventory.setFuel(item);
            }
         } else {
            containerInventory.setResult(item);
         }
      }

   }

   private static boolean isSameItem(ItemStack randomPosItem, ItemStack randomItem) {
      ItemMeta randomPosItemMeta = randomPosItem.getItemMeta();
      ItemMeta randomItemMeta = randomItem.getItemMeta();
      return randomPosItem.getType().equals(randomItem.getType()) && randomPosItemMeta.equals(randomItemMeta);
   }
}
