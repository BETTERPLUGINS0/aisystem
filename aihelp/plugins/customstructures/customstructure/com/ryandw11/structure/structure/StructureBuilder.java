package com.ryandw11.structure.structure;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.CustomStructureAddon;
import com.ryandw11.structure.api.structaddon.StructureSection;
import com.ryandw11.structure.api.structaddon.StructureSectionProvider;
import com.ryandw11.structure.exceptions.StructureConfigurationException;
import com.ryandw11.structure.loottables.LootTable;
import com.ryandw11.structure.loottables.LootTableType;
import com.ryandw11.structure.structure.properties.AdvancedSubSchematics;
import com.ryandw11.structure.structure.properties.BottomSpaceFill;
import com.ryandw11.structure.structure.properties.MaskProperty;
import com.ryandw11.structure.structure.properties.StructureLimitations;
import com.ryandw11.structure.structure.properties.StructureLocation;
import com.ryandw11.structure.structure.properties.StructureProperties;
import com.ryandw11.structure.structure.properties.SubSchematics;
import com.ryandw11.structure.utils.RandomCollection;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class StructureBuilder {
   private FileConfiguration config;
   private final CustomStructures plugin;
   protected String name;
   protected String schematic;
   protected int probabilityNumerator;
   protected int probabilityDenominator;
   protected int priority;
   protected String compiledSchematic;
   protected boolean isCompiled;
   protected StructureLocation structureLocation;
   protected StructureProperties structureProperties;
   protected StructureLimitations structureLimitations;
   protected MaskProperty sourceMaskProperty;
   protected MaskProperty targetMaskProperty;
   protected SubSchematics subSchematics;
   protected AdvancedSubSchematics advancedSubSchematics;
   protected BottomSpaceFill bottomSpaceFill;
   protected Map<LootTableType, RandomCollection<LootTable>> lootTables;
   protected List<StructureSection> structureSections;
   protected double baseRotation;

   public StructureBuilder(String name, String schematic) {
      this(name, schematic, (List)(new ArrayList()));
   }

   public StructureBuilder(String name, String schematic, List<StructureSection> sections) {
      this.isCompiled = false;
      this.plugin = CustomStructures.getInstance();
      this.name = name;
      this.schematic = schematic;
      this.priority = 100;
      this.baseRotation = 0.0D;
      this.lootTables = new HashMap();
      this.structureSections = sections;
   }

   public StructureBuilder(String name, String schematic, StructureSection... sections) {
      this.isCompiled = false;
      this.plugin = CustomStructures.getInstance();
      this.name = name;
      this.schematic = schematic;
      this.priority = 100;
      this.baseRotation = 0.0D;
      this.lootTables = new HashMap();
      this.structureSections = Arrays.asList(sections);
   }

   public StructureBuilder(String name, File file) {
      this.isCompiled = false;
      if (!file.exists()) {
         throw new RuntimeException("Cannot build structure: That file does not exist!");
      } else {
         this.config = YamlConfiguration.loadConfiguration(file);
         this.plugin = CustomStructures.getInstance();
         this.name = name;
         this.structureSections = new ArrayList();
         this.checkValidity();
         this.schematic = this.config.getString("Schematic");
         this.probabilityNumerator = this.config.getInt("Probability.Numerator");
         this.probabilityDenominator = this.config.getInt("Probability.Denominator");
         this.priority = this.config.contains("Priority") ? this.config.getInt("Priority") : 100;
         this.baseRotation = 0.0D;
         if (this.config.contains("CompiledSchematic")) {
            String var10003 = String.valueOf(CustomStructures.getInstance().getDataFolder());
            this.isCompiled = (new File(var10003 + "/schematics/" + (String)Objects.requireNonNull(this.config.getString("CompiledSchematic")))).exists();
            if (!this.isCompiled) {
               CustomStructures.getInstance().getLogger().severe("Invalid compiled schematic file for: " + name);
            } else {
               this.compiledSchematic = this.config.getString("CompiledSchematic");
            }
         }

         this.structureLocation = new StructureLocation(this.config);
         this.structureProperties = new StructureProperties(this.config);
         this.structureLimitations = new StructureLimitations(this.config);
         this.sourceMaskProperty = new MaskProperty("SourceMask", this.config);
         this.targetMaskProperty = new MaskProperty("TargetMask", this.config);
         this.subSchematics = new SubSchematics(this.config, CustomStructures.getInstance());
         this.advancedSubSchematics = new AdvancedSubSchematics(this.config, CustomStructures.getInstance());
         this.bottomSpaceFill = new BottomSpaceFill(this.config);
         this.lootTables = new HashMap();
         if (this.config.contains("LootTables")) {
            ConfigurationSection lootableConfig = this.config.getConfigurationSection("LootTables");

            assert lootableConfig != null;

            Iterator var4 = lootableConfig.getKeys(false).iterator();

            label112:
            while(true) {
               String lootTable;
               do {
                  if (!var4.hasNext()) {
                     break label112;
                  }

                  lootTable = (String)var4.next();
               } while(!LootTableType.exists(lootTable));

               LootTableType type = LootTableType.valueOf(lootTable.toUpperCase());
               Iterator var7 = ((ConfigurationSection)Objects.requireNonNull(lootableConfig.getConfigurationSection(lootTable))).getKeys(false).iterator();

               while(var7.hasNext()) {
                  String lootTableName = (String)var7.next();
                  int weight = lootableConfig.getInt(lootTable + "." + lootTableName);
                  LootTable table = CustomStructures.getInstance().getLootTableHandler().getLootTableByName(lootTableName);
                  table.addType(type);
                  if (this.lootTables.containsKey(type)) {
                     ((RandomCollection)this.lootTables.get(type)).add((double)weight, table);
                  } else {
                     this.lootTables.put(type, new RandomCollection());
                     ((RandomCollection)this.lootTables.get(type)).add((double)weight, table);
                  }
               }
            }
         }

         Iterator var16 = CustomStructures.getInstance().getAddonHandler().getCustomStructureAddons().iterator();

         while(var16.hasNext()) {
            CustomStructureAddon addon = (CustomStructureAddon)var16.next();
            Iterator var18 = addon.getProviderSet().iterator();

            StructureSection constructedSection;
            while(var18.hasNext()) {
               StructureSectionProvider provider = (StructureSectionProvider)var18.next();

               try {
                  constructedSection = provider.createSection();
                  if (!this.config.contains(constructedSection.getName())) {
                     constructedSection.setupSection((ConfigurationSection)null);
                  } else {
                     constructedSection.setupSection(this.config.getConfigurationSection(constructedSection.getName()));
                  }

                  this.structureSections.add(constructedSection);
               } catch (StructureConfigurationException var11) {
                  throw new StructureConfigurationException(String.format("[%s Addon] %s. This is notan issue with the CustomStructures plugin.", addon.getName(), var11.getMessage()));
               } catch (Throwable var12) {
                  this.plugin.getLogger().severe(String.format("An error was encountered in the %s addon! Enable debug for more information.", addon.getName()));
                  this.plugin.getLogger().severe(var12.getMessage());
                  this.plugin.getLogger().severe("This is not an issue with CustomStructures! Please contact the addon developer.");
                  if (this.plugin.isDebug()) {
                     var12.printStackTrace();
                  }
               }
            }

            var18 = addon.getStructureSections().iterator();

            while(var18.hasNext()) {
               Class section = (Class)var18.next();

               try {
                  constructedSection = (StructureSection)section.getConstructor().newInstance();
                  if (!this.config.contains(constructedSection.getName())) {
                     constructedSection.setupSection((ConfigurationSection)null);
                  } else {
                     constructedSection.setupSection(this.config.getConfigurationSection(constructedSection.getName()));
                  }

                  this.structureSections.add(constructedSection);
               } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var13) {
                  this.plugin.getLogger().severe(String.format("The section %s for the addon %sis configured incorrectly. If you are the developer please refer to the API documentation.", section.getName(), addon.getName()));
                  this.plugin.getLogger().severe("This is not an issue with CustomStructures.Report this error to the addon developer!!");
               } catch (StructureConfigurationException var14) {
                  throw new StructureConfigurationException(String.format("[%s Addon] %s. This is notan issue with the CustomStructures plugin.", addon.getName(), var14.getMessage()));
               } catch (Exception var15) {
                  this.plugin.getLogger().severe(String.format("An error was encountered in the %s addon! Enable debug for more information.", addon.getName()));
                  this.plugin.getLogger().severe(var15.getMessage());
                  this.plugin.getLogger().severe("This is not an issue with CustomStructures! Please contact the addon developer.");
                  if (this.plugin.isDebug()) {
                     var15.printStackTrace();
                  }
               }
            }
         }

      }
   }

   private void checkValidity() {
      if (!this.config.contains("Schematic")) {
         throw new StructureConfigurationException("Invalid structure config: No Schematic found!");
      } else if (!this.config.contains("Probability.Numerator")) {
         throw new StructureConfigurationException("Invalid structure config: `Probability.Numerator` is required!");
      } else if (!this.config.contains("Probability.Denominator")) {
         throw new StructureConfigurationException("Invalid structure config: `Probability.Denominator` is required!");
      } else if (this.config.isInt("Probability.Numerator") && this.config.getInt("Probability.Numerator") >= 1) {
         if (!this.config.isInt("Probability.Denominator") || this.config.getInt("Probability.Denominator") < 1) {
            throw new StructureConfigurationException("Invalid structure config: `Probability.Denominator` must be a number cannot be less than 1!");
         }
      } else {
         throw new StructureConfigurationException("Invalid structure config: `Probability.Numerator` must be a number cannot be less than 1!");
      }
   }

   public void setProbability(int numerator, int denominator) {
      this.probabilityNumerator = numerator;
      this.probabilityDenominator = denominator;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   public void setCompiledSchematic(String cschem) {
      String var10002 = String.valueOf(CustomStructures.getInstance().getDataFolder());
      if (!(new File(var10002 + "/schematics/" + cschem)).exists()) {
         throw new IllegalArgumentException("Compiled Schem File not found!");
      } else {
         this.compiledSchematic = cschem;
         this.isCompiled = true;
      }
   }

   public void setStructureLimitations(StructureLimitations limitations) {
      this.structureLimitations = limitations;
   }

   public void setStructureProperties(StructureProperties properties) {
      this.structureProperties = properties;
   }

   public void setStructureLocation(StructureLocation location) {
      this.structureLocation = location;
   }

   public void setSourceMaskProperty(MaskProperty mask) {
      this.sourceMaskProperty = mask;
   }

   public void setTargetMaskProperty(MaskProperty mask) {
      this.targetMaskProperty = mask;
   }

   public void setBottomSpaceFill(BottomSpaceFill bottomSpaceFill) {
      this.bottomSpaceFill = bottomSpaceFill;
   }

   public void setSubSchematics(SubSchematics subSchematics) {
      this.subSchematics = subSchematics;
   }

   public void setAdvancedSubSchematics(AdvancedSubSchematics advancedSubSchematics) {
      this.advancedSubSchematics = advancedSubSchematics;
   }

   public void setLootTables(ConfigurationSection lootableConfig) {
      this.lootTables = new HashMap();

      assert lootableConfig != null;

      Iterator var2 = lootableConfig.getKeys(false).iterator();

      while(true) {
         String lootTable;
         do {
            if (!var2.hasNext()) {
               return;
            }

            lootTable = (String)var2.next();
         } while(!LootTableType.exists(lootTable));

         LootTableType type = LootTableType.valueOf(lootTable.toUpperCase());
         Iterator var5 = ((ConfigurationSection)Objects.requireNonNull(lootableConfig.getConfigurationSection(lootTable))).getKeys(false).iterator();

         while(var5.hasNext()) {
            String lootTableName = (String)var5.next();
            int weight = lootableConfig.getInt(lootTable + "." + lootTableName);
            LootTable table = CustomStructures.getInstance().getLootTableHandler().getLootTableByName(lootTableName);
            table.addType(type);
            if (this.lootTables.containsKey(type)) {
               ((RandomCollection)this.lootTables.get(type)).add((double)weight, table);
            } else {
               this.lootTables.put(type, new RandomCollection());
               ((RandomCollection)this.lootTables.get(type)).add((double)weight, table);
            }
         }
      }
   }

   public void setLootTables(Map<LootTableType, RandomCollection<LootTable>> lootTables) {
      this.lootTables = lootTables;
   }

   public void addLootTable(LootTable lootTable, double weight) {
      LootTableType type;
      for(Iterator var4 = lootTable.getTypes().iterator(); var4.hasNext(); ((RandomCollection)this.lootTables.get(type)).add(weight, lootTable)) {
         type = (LootTableType)var4.next();
         if (!this.lootTables.containsKey(type)) {
            this.lootTables.put(type, new RandomCollection());
         }
      }

   }

   public void setBaseRotation(double baseRotation) {
      this.baseRotation = baseRotation;
   }

   public void addStructureSection(StructureSection structureSection) {
      this.structureSections.add(structureSection);
   }

   public Structure build() {
      Objects.requireNonNull(this.name, "The structure name cannot be null.");
      Objects.requireNonNull(this.schematic, "The structure schematic cannot be null.");
      Objects.requireNonNull(this.structureLocation, "The structure location cannot be null.");
      Objects.requireNonNull(this.structureProperties, "The structure property cannot be null.");
      Objects.requireNonNull(this.structureLimitations, "The structure limitations cannot be null.");
      Objects.requireNonNull(this.sourceMaskProperty, "The structure source mask property cannot be null.");
      Objects.requireNonNull(this.targetMaskProperty, "The structure target mask property cannot be null.");
      Objects.requireNonNull(this.subSchematics, "The structure sub-schematic property cannot be null.");
      Objects.requireNonNull(this.advancedSubSchematics, "The structure advanced sub-schematic property cannot be null.");
      Objects.requireNonNull(this.bottomSpaceFill, "The structure bottom space fill property cannot be null.");
      Objects.requireNonNull(this.lootTables, "The structure loot tables cannot be null.");
      Objects.requireNonNull(this.structureSections, "The structure sections list cannot be null.");
      return new Structure(this);
   }

   public void save(File file) throws IOException {
      file.createNewFile();
      this.config = YamlConfiguration.loadConfiguration(file);
      this.config.set("Schematic", this.schematic);
      this.config.set("Probability.Numerator", this.probabilityNumerator);
      this.config.set("Probability.Denominator", this.probabilityDenominator);
      this.config.set("StructureLocation.Worlds", this.structureLocation.getWorlds());
      this.config.set("StructureLocation.WorldBlacklist", this.structureLocation.getWorldBlacklist());
      this.config.set("StructureLocation.SpawnY", this.structureLocation.getSpawnSettings().getValue());
      this.config.set("StructureLocation.SpawnYHeightMap", this.structureLocation.getSpawnSettings().getHeightMap().toString());
      this.config.set("StructureLocation.Biome", this.structureLocation.getBiomes());
      this.config.set("StructureProperties.PlaceAir", this.structureProperties.canPlaceAir());
      this.config.set("StructureProperties.RandomRotation", this.structureProperties.isRandomRotation());
      this.config.set("StructureProperties.IgnorePlants", this.structureProperties.isIgnoringPlants());
      this.config.set("StructureProperties.SpawnInWater", this.structureProperties.canSpawnInWater());
      this.config.set("StructureProperties.SpawnInLavaLakes", this.structureProperties.canSpawnInLavaLakes());
      this.config.set("StructureLimitations.WhitelistSpawnBlocks", this.structureLimitations.getWhitelistBlocks());
      if (this.isCompiled) {
         this.config.set("CompiledSchematic", this.compiledSchematic);
      }

      Iterator var2 = this.lootTables.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<LootTableType, RandomCollection<LootTable>> loot = (Entry)var2.next();
         Iterator var4 = ((RandomCollection)loot.getValue()).getMap().entrySet().iterator();

         while(var4.hasNext()) {
            Entry<Double, LootTable> entry = (Entry)var4.next();
            this.config.set("LootTables." + ((LootTableType)loot.getKey()).toString() + "." + ((LootTable)entry.getValue()).getName(), entry.getKey());
         }
      }

      this.config.save(file);
   }
}
