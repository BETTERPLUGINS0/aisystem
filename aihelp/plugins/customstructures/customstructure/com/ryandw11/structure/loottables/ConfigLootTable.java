package com.ryandw11.structure.loottables;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.exceptions.LootTableException;
import com.ryandw11.structure.loottables.customitems.CustomLootItem;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigLootTable extends LootTable {
   private final String name;
   private int rolls;
   public FileConfiguration lootTablesFC;

   public ConfigLootTable(String name) {
      this.loadFile(name);
      this.name = name;
      if (!this.lootTablesFC.contains("Rolls")) {
         throw new LootTableException("Invalid loot table format! Cannot find global 'Rolls' setting.");
      } else {
         this.rolls = this.lootTablesFC.getInt("Rolls");
         this.loadItems();
      }
   }

   public String getName() {
      return this.name;
   }

   public int getRolls() {
      return this.rolls;
   }

   public void setRolls(int rolls) {
      this.rolls = rolls;
   }

   private void loadItems() {
      if (!this.lootTablesFC.contains("Items")) {
         throw new LootTableException("Invalid LootTable format! The 'Items' section is required!");
      } else {
         Iterator var1 = this.lootTablesFC.getConfigurationSection("Items").getKeys(false).iterator();

         while(var1.hasNext()) {
            String itemID = (String)var1.next();
            ConfigurationSection itemSection = this.lootTablesFC.getConfigurationSection("Items." + itemID);

            assert itemSection != null;

            String type = itemSection.getString("Type");
            if (type == null) {
               type = "Standard";
            }

            type = type.toUpperCase();
            int weight = itemSection.getInt("Weight", 1);
            String amount = itemSection.getString("Amount", "1");
            if (type.equalsIgnoreCase("STANDARD")) {
               this.handleStandardItem(itemID, itemSection);
            } else if (type.equalsIgnoreCase("CUSTOM")) {
               try {
                  CustomLootItem customLootItem = new CustomLootItem(this, itemID, weight, amount);
                  customLootItem.constructItem(itemSection);
                  this.randomCollection.add((double)weight, customLootItem);
               } catch (LootTableException var12) {
               }
            } else {
               Class<? extends ConfigLootItem> itemClass = CustomStructures.getInstance().getLootTableHandler().getLootItemClassByName(type);
               if (itemClass == null) {
                  throw new LootTableException(String.format("Unable to find custom loot item type %s!", type));
               }

               try {
                  Constructor<? extends ConfigLootItem> item = itemClass.getConstructor(LootTable.class, String.class, Integer.TYPE, String.class);
                  ConfigLootItem itemInst = (ConfigLootItem)item.newInstance(this, itemID, weight, amount);
                  itemInst.constructItem(itemSection);
                  this.randomCollection.add((double)weight, itemInst);
               } catch (NoSuchMethodException var10) {
                  throw new LootTableException(String.format("Unable to construct custom loot item type %s! Does the required constructor exist?", type));
               } catch (InvocationTargetException | IllegalAccessException | InstantiationException | IllegalArgumentException var11) {
                  throw new LootTableException(String.format("Unable to construct custom loot item type %s! Does the constructor have the correct access level?", type));
               }
            }
         }

      }
   }

   private void handleStandardItem(String itemID, ConfigurationSection section) {
      this.validateItem(itemID, section);
      String customName = section.getString("Name");
      String material = (String)Objects.requireNonNull(section.getString("Material"));
      String amount = (String)Objects.requireNonNull(section.getString("Amount"));
      int weight = section.getInt("Weight");
      Map<String, String> enchants = new HashMap();
      ConfigurationSection enchantMents = section.getConfigurationSection("Enchantments");
      if (enchantMents != null) {
         Iterator var9 = enchantMents.getKeys(false).iterator();

         while(var9.hasNext()) {
            String enchantName = (String)var9.next();
            String level = (String)Objects.requireNonNull(section.getString("Enchantments." + enchantName));
            enchants.put(enchantName, level);
         }
      }

      List<String> lore = section.getStringList("Lore");
      this.randomCollection.add((double)weight, new StandardLootItem(customName, material, amount, weight, lore, enchants));
   }

   private void validateItem(String itemID, ConfigurationSection item) {
      if (item == null) {
         throw new LootTableException("Invalid file format for loot table!");
      } else if (!item.contains("Amount")) {
         throw new LootTableException("Invalid file format for loot table! Cannot find 'Amount' setting for item: " + itemID);
      } else if (!item.contains("Weight")) {
         throw new LootTableException("Invalid file format for loot table! Cannot find 'Weight' setting for item: " + itemID);
      } else if (!item.contains("Type")) {
         throw new LootTableException("Invalid file format for loot table! Cannot find 'Type' setting for item: " + itemID);
      } else if (!item.isInt("Weight")) {
         throw new LootTableException("Invalid file format for loot table! 'Weight' is not an integer for item: " + itemID);
      }
   }

   private void loadFile(String name) {
      String var10002 = String.valueOf(CustomStructures.plugin.getDataFolder());
      File lootTablesfile = new File(var10002 + "/lootTables/" + name + ".yml");
      if (!lootTablesfile.exists()) {
         throw new LootTableException("Cannot find the following loot table file: " + name);
      } else {
         this.lootTablesFC = YamlConfiguration.loadConfiguration(lootTablesfile);

         try {
            this.lootTablesFC.load(lootTablesfile);
         } catch (InvalidConfigurationException | IOException var4) {
            throw new LootTableException("Invalid LootTable Configuration! Please view the guide on the wiki for more information.");
         }
      }
   }
}
