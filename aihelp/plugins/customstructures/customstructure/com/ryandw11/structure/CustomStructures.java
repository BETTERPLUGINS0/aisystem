package com.ryandw11.structure;

import com.ryandw11.structure.api.structaddon.CustomStructureAddon;
import com.ryandw11.structure.bstats.bukkit.Metrics;
import com.ryandw11.structure.bstats.charts.AdvancedPie;
import com.ryandw11.structure.citizens.CitizensDisabled;
import com.ryandw11.structure.citizens.CitizensEnabled;
import com.ryandw11.structure.citizens.CitizensNpcHook;
import com.ryandw11.structure.commands.SCommand;
import com.ryandw11.structure.commands.SCommandTab;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks_1_13;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks_1_14;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks_1_15;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks_1_16;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks_1_17;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks_1_19;
import com.ryandw11.structure.ignoreblocks.IgnoreBlocks_1_20;
import com.ryandw11.structure.listener.ChunkLoad;
import com.ryandw11.structure.listener.PlayerJoin;
import com.ryandw11.structure.loottables.LootTableHandler;
import com.ryandw11.structure.loottables.customitems.CustomItemManager;
import com.ryandw11.structure.mythicalmobs.MMDisabled;
import com.ryandw11.structure.mythicalmobs.MMEnabled;
import com.ryandw11.structure.mythicalmobs.MythicalMobHook;
import com.ryandw11.structure.schematic.StructureSignHandler;
import com.ryandw11.structure.structure.StructureHandler;
import com.ryandw11.structure.utils.CSUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomStructures extends JavaPlugin {
   public static CustomStructures plugin;
   private final File lootTableFile = new File(String.valueOf(this.getDataFolder()) + "/lootTables/lootTable.yml");
   private final FileConfiguration lootTablesFC;
   private MythicalMobHook mythicalMobHook;
   private CitizensNpcHook citizensNpcHook;
   private SignCommandsHandler signCommandsHandler;
   private NpcHandler npcHandler;
   private StructureHandler structureHandler;
   private LootTableHandler lootTableHandler;
   private CustomItemManager customItemManager;
   private IgnoreBlocks blockIgnoreManager;
   private AddonHandler addonHandler;
   private StructureSignHandler structureSignHandler;
   private Metrics metrics;
   private boolean debugMode;
   public static boolean enabled;
   private boolean initialized;
   public static final int COMPILED_STRUCT_VER = 1;
   public static final int CONFIG_VERSION = 9;
   private static boolean papiEnabled = false;

   public CustomStructures() {
      this.lootTablesFC = YamlConfiguration.loadConfiguration(this.lootTableFile);
      this.initialized = false;
   }

   public void onEnable() {
      enabled = true;
      plugin = this;
      this.loadManager();
      this.registerConfig();
      this.setupBlockIgnore();
      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
         this.getLogger().info("Placeholder API found, placeholders supported.");
         papiEnabled = true;
      } else {
         this.getLogger().info("PlaceholderAPI not found.");
      }

      if (this.getServer().getPluginManager().getPlugin("Citizens") != null) {
         this.citizensNpcHook = new CitizensEnabled(this);
         this.getLogger().info("Citizens detected! Activating plugin hook!");
      } else {
         this.citizensNpcHook = new CitizensDisabled();
      }

      if (this.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
         this.mythicalMobHook = new MMEnabled();
         this.getLogger().info("MythicMobs detected! Activating plugin hook!");
      } else {
         this.mythicalMobHook = new MMDisabled();
      }

      this.loadFiles();
      this.debugMode = this.getConfig().getBoolean("debug");
      if (this.getConfig().getInt("configversion") < 9) {
         this.lootTableHandler = new LootTableHandler();
         this.updateConfig(this.getConfig().getInt("configversion"));
      }

      String var10002 = String.valueOf(this.getDataFolder());
      File f = new File(var10002 + File.separator + "schematics");
      if (!f.exists()) {
         this.getLogger().info("Loading the plugin for the first time.");
         this.getLogger().info("A demo structure will be added! Please make sure to test out this plugin in a test world!");
      }

      this.exportResource(new File(this.getDataFolder(), "schematics"), "demo.schem", "schematics/");
      this.exportResource(new File(this.getDataFolder(), "structures"), "demo.yml", "structures/");
      this.exportResource(this.getDataFolder(), "npcs.yml", "");
      this.exportResource(this.getDataFolder(), "signcommands.yml", "");
      String var10006 = String.valueOf(this.getDataFolder());
      File var10004 = new File(var10006 + File.separator + "items" + File.separator + "customitems.yml");
      String var10007 = String.valueOf(this.getDataFolder());
      this.customItemManager = new CustomItemManager(this, var10004, new File(var10007 + File.separator + "items"));
      this.signCommandsHandler = new SignCommandsHandler(this.getDataFolder(), this);
      this.npcHandler = new NpcHandler(this.getDataFolder(), plugin);
      this.lootTableHandler = new LootTableHandler();
      this.addonHandler = new AddonHandler();
      this.structureSignHandler = new StructureSignHandler();
      Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::initialize, 30L);
      if (this.getConfig().getBoolean("bstats")) {
         this.metrics = new Metrics(this, 7056);
         this.getLogger().info("Bstat metrics for this plugin is enabled. Disable it in the config if you do not want it on.");
      } else {
         this.getLogger().info("Bstat metrics is disabled for this plugin.");
      }

   }

   public void initialize() {
      if (!this.initialized) {
         if (this.getConfig().getInt("configversion") != 9) {
            ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
            String var10001 = String.valueOf(ChatColor.RED);
            var10000.sendMessage(var10001 + "[CustomStructures] Cannot enable plugin, your config version is outdated. Check the above for errors that may have occurred during the auto-update process." + String.valueOf(ChatColor.RESET));
         } else {
            this.structureHandler = new StructureHandler(this.getConfig().getStringList("Structures"), this);
            this.getLogger().info("The plugin has been fully enabled with " + this.structureHandler.getStructures().size() + " structures.");
            this.getLogger().info(this.addonHandler.getCustomStructureAddons().size() + " addons were found.");
            if (this.metrics != null) {
               this.metrics.addCustomChart(new AdvancedPie("used_addons", () -> {
                  Map<String, Integer> valueMap = new HashMap();
                  Iterator var2 = this.addonHandler.getCustomStructureAddons().iterator();

                  while(var2.hasNext()) {
                     CustomStructureAddon addon = (CustomStructureAddon)var2.next();
                     valueMap.put(addon.getName(), 1);
                  }

                  return valueMap;
               }));
            }

            this.initialized = true;
         }
      }
   }

   public static String replacePAPIPlaceholders(String text) {
      return papiEnabled ? PlaceholderAPI.setPlaceholders((Player)null, text) : text;
   }

   private void exportResource(File targetDirectory, String filename, String resourcePath) {
      if (!targetDirectory.exists()) {
         targetDirectory.mkdirs();
      }

      File targetFile = new File(targetDirectory, filename);
      if (!targetFile.exists()) {
         this.saveResource(resourcePath + filename, false);
      }

   }

   public void onDisable() {
      if (this.structureHandler == null) {
         this.getLogger().severe("ERROR: The Structure Handler was never initialized during setup.");
      } else {
         this.structureHandler.cleanup();
         this.npcHandler.cleanUp();
         this.signCommandsHandler.cleanUp();
      }
   }

   private void setupBlockIgnore() {
      String[] version = Bukkit.getBukkitVersion().split("-");
      if (version.length > 0 && version[0].split("\\.").length > 1) {
         if (this.debugMode) {
            this.getLogger().info("Using Version: " + version[0]);
         }

         String[] verNumbers = version[0].split("\\.");
         String var3 = verNumbers[1];
         byte var4 = -1;
         switch(var3.hashCode()) {
         case 1570:
            if (var3.equals("13")) {
               var4 = 6;
            }
            break;
         case 1571:
            if (var3.equals("14")) {
               var4 = 5;
            }
            break;
         case 1572:
            if (var3.equals("15")) {
               var4 = 4;
            }
            break;
         case 1573:
            if (var3.equals("16")) {
               var4 = 3;
            }
            break;
         case 1574:
            if (var3.equals("17")) {
               var4 = 1;
            }
            break;
         case 1575:
            if (var3.equals("18")) {
               var4 = 2;
            }
            break;
         case 1576:
            if (var3.equals("19")) {
               var4 = 0;
            }
         }

         switch(var4) {
         case 0:
            this.blockIgnoreManager = new IgnoreBlocks_1_19();
            break;
         case 1:
         case 2:
            this.blockIgnoreManager = new IgnoreBlocks_1_17();
            break;
         case 3:
            this.blockIgnoreManager = new IgnoreBlocks_1_16();
            break;
         case 4:
            this.blockIgnoreManager = new IgnoreBlocks_1_15();
            break;
         case 5:
            this.blockIgnoreManager = new IgnoreBlocks_1_14();
            break;
         case 6:
            this.blockIgnoreManager = new IgnoreBlocks_1_13();
            break;
         default:
            this.blockIgnoreManager = new IgnoreBlocks_1_20();
         }

      } else {
         this.getLogger().info("Unable to detect Minecraft version! Defaulting to latest version.");
         this.blockIgnoreManager = new IgnoreBlocks_1_20();
      }
   }

   public IgnoreBlocks getBlockIgnoreManager() {
      return this.blockIgnoreManager;
   }

   public StructureHandler getStructureHandler() {
      return this.structureHandler;
   }

   public LootTableHandler getLootTableHandler() {
      return this.lootTableHandler;
   }

   public void reloadHandlers() {
      this.signCommandsHandler.cleanUp();
      this.signCommandsHandler = new SignCommandsHandler(this.getDataFolder(), this);
      this.npcHandler.cleanUp();
      this.npcHandler = new NpcHandler(this.getDataFolder(), plugin);
      this.structureHandler.cleanup();
      this.structureHandler = new StructureHandler(this.getConfig().getStringList("Structures"), this);
      this.lootTableHandler = new LootTableHandler();
      this.addonHandler.handlePluginReload();
   }

   public static CustomStructures getInstance() {
      return plugin;
   }

   private void loadManager() {
      Bukkit.getServer().getPluginManager().registerEvents(new ChunkLoad(), this);
      Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
      ((PluginCommand)Objects.requireNonNull(this.getCommand("customstructure"))).setExecutor(new SCommand(this));
      ((PluginCommand)Objects.requireNonNull(this.getCommand("customstructure"))).setTabCompleter(new SCommandTab(this));
   }

   private void registerConfig() {
      this.saveDefaultConfig();
   }

   public void loadFiles() {
      if (this.lootTableFile.exists()) {
         try {
            this.lootTablesFC.load(this.lootTableFile);
         } catch (InvalidConfigurationException | IOException var2) {
            var2.printStackTrace();
         }
      } else {
         this.saveResource("lootTables/lootTable.yml", false);
      }

   }

   private void updateConfig(int ver) {
      this.getLogger().info("An older version of the plugin has been detected!");
      this.getLogger().info("Automatically converting old format into the new one.");
      if (ver < 5) {
         this.getLogger().severe("Error: Your config is too old for the plugin to update.");
         this.getLogger().severe("Please use custom structures 1.5.4 or older before updating to the latest version.");
         this.getLogger().severe("The plugin will now disable itself.");
         this.getServer().getPluginManager().disablePlugin(this);
      } else if (ver < 7) {
         this.getLogger().severe("Error: Your config is too old for the plugin to update.");
         this.getLogger().severe("Please consult the plugin wiki to see what version to use to update your configuration files.");
         this.getServer().getPluginManager().disablePlugin(this);
      } else {
         File structDir;
         File backupDirectory;
         File backupData;
         YamlConfiguration fileConfiguration;
         int backupVer;
         Iterator var7;
         String s;
         YamlConfiguration structConfig;
         Iterator var10;
         String key;
         ConfigurationSection section;
         ConsoleCommandSender var10000;
         String var10001;
         ArrayList updatedStructures;
         if (ver < 8) {
            this.getLogger().info("Updating all structure config files...");
            structDir = new File(this.getDataFolder(), "structures");
            if (!structDir.exists() && !structDir.isDirectory()) {
               this.getLogger().severe("An error occurred when trying to update the structure format: Unable to find structure directory! Does it exist?");
               return;
            }

            backupDirectory = new File(this.getDataFolder(), "backup");
            backupData = new File(backupDirectory, ".backups");
            if (!backupDirectory.exists() && !backupDirectory.mkdir()) {
               this.getLogger().severe("Error: Unable to create backup directory!");
               return;
            }

            if (!backupData.exists()) {
               try {
                  backupData.createNewFile();
               } catch (IOException var27) {
                  this.getLogger().severe("Error: Unable to create backup file.");
                  return;
               }
            }

            fileConfiguration = YamlConfiguration.loadConfiguration(backupData);
            if (fileConfiguration.contains("backupVer")) {
               backupVer = fileConfiguration.getInt("backupVer");
               if (backupVer != 8) {
                  var10000 = Bukkit.getConsoleSender();
                  var10001 = String.valueOf(ChatColor.RED);
                  var10000.sendMessage(var10001 + "===============[CUSTOM STRUCTURES UPDATE]===============" + String.valueOf(ChatColor.RESET));
                  this.getLogger().severe("Unable to update plugin! Backup data is outdated!");
                  this.getLogger().severe("Please delete the backup folder in the CustomStructures directory before continuing!");
                  var10000 = Bukkit.getConsoleSender();
                  var10001 = String.valueOf(ChatColor.RED);
                  var10000.sendMessage(var10001 + "===============[CUSTOM STRUCTURES UPDATE]===============" + String.valueOf(ChatColor.RESET));
                  return;
               }
            } else {
               fileConfiguration.set("backupVer", 8);

               try {
                  fileConfiguration.save(backupData);
               } catch (IOException var26) {
                  this.getLogger().severe("A critical error has occurred while backing up the plugin data.");
                  return;
               }
            }

            updatedStructures = new ArrayList(fileConfiguration.getStringList("UpdatedStructures"));
            if (!updatedStructures.isEmpty()) {
               this.getLogger().info("Previous update attempt detected.");
               this.getLogger().info(String.format("%s completed structure updates were found. If this is your first time updating to this version of CustomStructures, then please delete the backup directory and restart the server.", updatedStructures.size()));
               this.getLogger().info("The server will now wait 5 seconds to give you a chance to stop the server before the update automatically continues. Press ctrl+c to cancel running the server.");

               try {
                  Thread.sleep(5000L);
               } catch (InterruptedException var25) {
                  this.getLogger().info("Server shutdown detected. Stopping update.");
                  return;
               }
            }

            this.createBackupForFile("config.yml", "/backup/config.yml.backup");
            var7 = this.getConfig().getStringList("Structures").iterator();

            label257:
            while(true) {
               do {
                  if (!var7.hasNext()) {
                     this.getConfig().set("configversion", 8);
                     this.saveConfig();
                     this.getLogger().info("Successfully updated all structure files to the latest version (8).");
                     this.getLogger().info("Please delete the backup folder that was created in the CustomStructures directory after you confirm everything was updated correctly.");
                     break label257;
                  }

                  s = (String)var7.next();
               } while(updatedStructures.contains(s));

               try {
                  this.createBackupForFile("/structures/" + s + ".yml", "/backup/" + s + ".yml.backup");
                  structConfig = YamlConfiguration.loadConfiguration(new File(structDir, s + ".yml"));
                  if (structConfig.contains("SubSchematics.Schematics")) {
                     var10 = structConfig.getConfigurationSection("SubSchematics.Schematics").getKeys(false).iterator();

                     while(var10.hasNext()) {
                        key = (String)var10.next();
                        section = structConfig.getConfigurationSection("SubSchematics.Schematics." + key);
                        if (!section.contains("file")) {
                           this.getLogger().severe(String.format("An error has occurred when attempting to update structure %s!", s));
                           this.getLogger().severe(String.format("Cannot find 'file' option on %s when update the SubSchematics property!", key));
                           this.getLogger().severe("After fixing the error, restart the server for the plugin to continue updating from where it left off.");
                           return;
                        }

                        section.set("File", section.getString("file"));
                        section.set("file", (Object)null);
                     }

                     try {
                        structConfig.save(new File(structDir, s + ".yml"));
                     } catch (IOException var24) {
                        this.getLogger().info(String.format("An error has occurred when updating %s!", s));
                        this.getLogger().severe("Error: unable to save updated structure file!");
                        return;
                     }

                     this.getLogger().info(String.format("Successfully updated the structure: %s!", s));
                     updatedStructures.add(s);
                     fileConfiguration.set("UpdatedStructures", updatedStructures);
                     fileConfiguration.save(backupData);
                  }
               } catch (Exception var30) {
                  this.getLogger().severe(String.format("An error has occurred when updating %s:", s));
                  var30.printStackTrace();
                  this.getLogger().severe("After fixing the error, restart the server for the plugin to continue updating from where it left off.");
                  return;
               }
            }
         }

         if (ver < 9) {
            this.getLogger().info("Updating all structure config files...");
            structDir = new File(this.getDataFolder(), "structures");
            if (!structDir.exists() && !structDir.isDirectory()) {
               this.getLogger().severe("An error occurred when trying to update the structure format: Unable to find structure directory! Does it exist?");
               return;
            }

            backupDirectory = new File(this.getDataFolder(), "backup");
            backupData = new File(backupDirectory, ".backups");
            if (!backupDirectory.exists() && !backupDirectory.mkdir()) {
               this.getLogger().severe("Error: Unable to create backup directory!");
               return;
            }

            if (!backupData.exists()) {
               try {
                  backupData.createNewFile();
               } catch (IOException var23) {
                  this.getLogger().severe("Error: Unable to create backup file.");
                  return;
               }
            }

            fileConfiguration = YamlConfiguration.loadConfiguration(backupData);
            if (fileConfiguration.contains("backupVer")) {
               backupVer = fileConfiguration.getInt("backupVer");
               if (backupVer != 9) {
                  var10000 = Bukkit.getConsoleSender();
                  var10001 = String.valueOf(ChatColor.RED);
                  var10000.sendMessage(var10001 + "===============[CUSTOM STRUCTURES UPDATE]===============" + String.valueOf(ChatColor.RESET));
                  this.getLogger().severe("Unable to update plugin! Backup data is outdated!");
                  this.getLogger().severe("Please delete the backup folder in the CustomStructures directory before continuing!");
                  var10000 = Bukkit.getConsoleSender();
                  var10001 = String.valueOf(ChatColor.RED);
                  var10000.sendMessage(var10001 + "===============[CUSTOM STRUCTURES UPDATE]===============" + String.valueOf(ChatColor.RESET));
                  return;
               }
            } else {
               fileConfiguration.set("backupVer", 9);

               try {
                  fileConfiguration.save(backupData);
               } catch (IOException var22) {
                  this.getLogger().severe("A critical error has occurred while backing up the plugin data.");
                  return;
               }
            }

            if (!fileConfiguration.getBoolean("finishedStructureUpdates", false)) {
               updatedStructures = new ArrayList(fileConfiguration.getStringList("UpdatedStructures"));
               if (!updatedStructures.isEmpty()) {
                  this.getLogger().info("Previous update attempt detected.");
                  this.getLogger().info(String.format("%s completed structure updates were found. If this is your first time updating to this version of CustomStructures, then please delete the backup directory and restart the server.", updatedStructures.size()));
                  this.getLogger().info("The server will now wait 5 seconds to give you a chance to stop the server before the update automatically continues. Press ctrl+c to cancel running the server.");

                  try {
                     Thread.sleep(5000L);
                  } catch (InterruptedException var21) {
                     this.getLogger().info("Server shutdown detected. Stopping update.");
                     return;
                  }
               }

               this.createBackupForFile("config.yml", "/backup/config.yml.backup");
               var7 = this.getConfig().getStringList("Structures").iterator();

               label229:
               while(true) {
                  do {
                     if (!var7.hasNext()) {
                        fileConfiguration.set("finishedStructureUpdates", true);

                        try {
                           fileConfiguration.save(backupData);
                        } catch (IOException var19) {
                           this.getLogger().severe("An error has occurred when trying to save the backup file!");
                           var19.printStackTrace();
                           this.getLogger().severe("After fixing the error, restart the server for the plugin to continue updating from where it left off.");
                        }
                        break label229;
                     }

                     s = (String)var7.next();
                  } while(updatedStructures.contains(s));

                  try {
                     this.createBackupForFile("/structures/" + s + ".yml", "/backup/" + s + ".yml.backup");
                     structConfig = YamlConfiguration.loadConfiguration(new File(structDir, s + ".yml"));
                     CSUtils.renameConfigString(structConfig, "schematic", "Schematic");
                     CSUtils.renameConfigString(structConfig, "compiled_schematic", "CompiledSchematic");
                     CSUtils.renameConfigInteger(structConfig, "Chance.Number", "Probability.Numerator");
                     CSUtils.renameConfigInteger(structConfig, "Chance.OutOf", "Probability.Denominator");
                     structConfig.set("Chance", (Object)null);
                     CSUtils.renameConfigInteger(structConfig, "StructureLocation.spawn_distance.x", "StructureLocation.SpawnDistance.x");
                     CSUtils.renameConfigInteger(structConfig, "StructureLocation.spawn_distance.z", "StructureLocation.SpawnDistance.z");
                     structConfig.set("StructureLocation.spawn_distance", (Object)null);
                     CSUtils.renameConfigBoolean(structConfig, "StructureProperties.randomRotation", "StructureProperties.RandomRotation");
                     CSUtils.renameConfigBoolean(structConfig, "StructureProperties.ignorePlants", "StructureProperties.IgnorePlants");
                     CSUtils.renameConfigBoolean(structConfig, "StructureProperties.spawnInWater", "StructureProperties.SpawnInWater");
                     CSUtils.renameConfigBoolean(structConfig, "StructureProperties.spawnInLavaLakes", "StructureProperties.SpawnInLavaLakes");
                     CSUtils.renameConfigBoolean(structConfig, "StructureProperties.spawnInVoid", "StructureProperties.SpawnInVoid");
                     CSUtils.renameConfigBoolean(structConfig, "StructureProperties.ignoreWater", "StructureProperties.IgnoreWater");
                     CSUtils.renameConfigStringList(structConfig, "StructureLimitations.whitelistSpawnBlocks", "StructureLimitations.WhitelistSpawnBlocks");
                     CSUtils.renameConfigStringList(structConfig, "StructureLimitations.blacklistSpawnBlocks", "StructureLimitations.BlacklistSpawnBlocks");
                     CSUtils.renameConfigString(structConfig, "StructureLimitations.BlockLevelLimit.mode", "StructureLimitations.BlockLevelLimit.Mode");
                     CSUtils.renameConfigInteger(structConfig, "StructureLimitations.BlockLevelLimit.cornerOne.x", "StructureLimitations.BlockLevelLimit.CornerOne.x");
                     CSUtils.renameConfigInteger(structConfig, "StructureLimitations.BlockLevelLimit.cornerOne.z", "StructureLimitations.BlockLevelLimit.CornerOne.z");
                     CSUtils.renameConfigInteger(structConfig, "StructureLimitations.BlockLevelLimit.cornerTwo.x", "StructureLimitations.BlockLevelLimit.CornerTwo.x");
                     CSUtils.renameConfigInteger(structConfig, "StructureLimitations.BlockLevelLimit.cornerTwo.z", "StructureLimitations.BlockLevelLimit.CornerTwo.z");
                     CSUtils.renameConfigInteger(structConfig, "StructureLimitations.BlockLevelLimit.error", "StructureLimitations.BlockLevelLimit.Error");
                     structConfig.set("StructureLimitations.BlockLevelLimit.cornerOne", (Object)null);
                     structConfig.set("StructureLimitations.BlockLevelLimit.cornerTwo", (Object)null);
                     CSUtils.renameStringConfigurationSection(structConfig, "StructureLimitations.replacement_blocks", "StructureLimitations.ReplaceBlocks");
                     CSUtils.renameConfigInteger(structConfig, "StructureLimitations.replacement_blocks_delay", "StructureLimitations.ReplaceBlockDelay");
                     CSUtils.renameConfigInteger(structConfig, "StructureLimitations.iterationLimit", "StructureLimitations.IterationLimit");
                     if (structConfig.contains("SubSchematics")) {
                        if (structConfig.getBoolean("SubSchematics.Enabled", true) && structConfig.getConfigurationSection("SubSchematics.Schematics") != null) {
                           structConfig.set("SubSchematics.Enabled", (Object)null);
                           var10 = structConfig.getConfigurationSection("SubSchematics.Schematics").getKeys(false).iterator();

                           while(var10.hasNext()) {
                              key = (String)var10.next();
                              section = structConfig.getConfigurationSection("SubSchematics.Schematics." + key);
                              structConfig.set("SubSchematics." + key, section);
                           }

                           structConfig.set("SubSchematics.Schematics", (Object)null);
                        } else {
                           structConfig.set("SubSchematics", (Object)null);
                        }
                     }

                     if (structConfig.contains("Masks")) {
                        structConfig.set("SourceMask.Enabled", structConfig.getBoolean("Masks.enabled"));
                        structConfig.set("SourceMask.UnionType", structConfig.getString("Masks.union_type"));
                        structConfig.set("SourceMask.BlockTypeMask", structConfig.getStringList("Masks.BlockTypeMask"));
                        structConfig.set("SourceMask.NegatedBlockMask", structConfig.getStringList("Masks.NegatedBlockMask"));
                        structConfig.set("Masks", (Object)null);
                     }

                     try {
                        structConfig.save(new File(structDir, s + ".yml"));
                     } catch (IOException var20) {
                        this.getLogger().info(String.format("An error has occurred when updating %s!", s));
                        this.getLogger().severe("Error: unable to save updated structure file!");
                        return;
                     }

                     this.getLogger().info(String.format("Successfully updated the structure: %s!", s));
                     updatedStructures.add(s);
                     fileConfiguration.set("UpdatedStructures", updatedStructures);
                     fileConfiguration.save(backupData);
                  } catch (Exception var29) {
                     this.getLogger().severe(String.format("An error has occurred when updating %s.", s));
                     var29.printStackTrace();
                     this.getLogger().severe("After fixing the error, restart the server for the plugin to continue updating from where it left off.");
                     return;
                  }
               }
            }

            File lootTableFolder = new File(this.getDataFolder(), "lootTables");
            File[] lootTableFiles = lootTableFolder.listFiles();
            if (lootTableFiles != null) {
               List<String> updatedLootTables = new ArrayList(fileConfiguration.getStringList("UpdatedLootTables"));
               File[] var35 = lootTableFiles;
               int var36 = lootTableFiles.length;

               for(int var37 = 0; var37 < var36; ++var37) {
                  File file = var35[var37];
                  if (file.getName().endsWith(".yml")) {
                     String lootTableName = file.getName().replace(".yml", "");

                     try {
                        if (!updatedLootTables.contains(lootTableName)) {
                           YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                           Iterator var15 = configuration.getConfigurationSection("Items").getKeys(false).iterator();

                           while(var15.hasNext()) {
                              String key = (String)var15.next();
                              ConfigurationSection section = configuration.getConfigurationSection("Items." + key);
                              String type = section.getString("Type", "AIR");
                              if (type.equalsIgnoreCase("CUSTOM")) {
                                 section.set("Type", "CUSTOM");
                              } else {
                                 section.set("Material", type);
                                 section.set("Type", "STANDARD");
                              }
                           }

                           configuration.save(file);
                           this.getLogger().info(String.format("Successfully updated the loot table: %s!", lootTableName));
                           updatedLootTables.add(lootTableName);
                           fileConfiguration.set("UpdatedLootTables", updatedLootTables);
                           fileConfiguration.save(backupData);
                        }
                     } catch (Exception var28) {
                        this.getLogger().severe(String.format("An error has occurred when updating the loot table %s.", lootTableName));
                        var28.printStackTrace();
                        this.getLogger().severe("After fixing the error, restart the server for the plugin to continue updating from where it left off.");
                        return;
                     }
                  }
               }
            }

            this.getConfig().set("configversion", 9);
            this.saveConfig();
            this.getLogger().info("Successfully updated all structure files to the latest version (9).");
            this.getLogger().info("Please delete the backup folder that was created in the CustomStructures directory after you confirm everything was updated correctly.");
         }

      }
   }

   private boolean createBackupForFile(String file, String backupFile) {
      File config = new File(this.getDataFolder(), file);
      File configBackup = new File(this.getDataFolder(), backupFile);

      try {
         configBackup.createNewFile();
         FileUtils.copyFile(config, configBackup);
         return true;
      } catch (IOException var6) {
         this.getLogger().severe("A critical error was encountered when attempting to update plugin configuration files!");
         this.getLogger().severe("Unable to create a backup for " + file);
         return false;
      }
   }

   public boolean isDebug() {
      return this.debugMode;
   }

   public CustomItemManager getCustomItemManager() {
      return this.customItemManager;
   }

   public SignCommandsHandler getSignCommandsHandler() {
      return this.signCommandsHandler;
   }

   public NpcHandler getNpcHandler() {
      return this.npcHandler;
   }

   public AddonHandler getAddonHandler() {
      return this.addonHandler;
   }

   public StructureSignHandler getStructureSignHandler() {
      return this.structureSignHandler;
   }

   public MythicalMobHook getMythicalMobHook() {
      return this.mythicalMobHook;
   }

   public CitizensNpcHook getCitizensNpcHook() {
      return this.citizensNpcHook;
   }

   public boolean canStructureSpawnInWorld(World world) {
      List<String> whitelist = this.getConfig().getStringList("GlobalWorldWhitelist");
      List<String> blacklist = this.getConfig().getStringList("GlobalWorldBlacklist");
      if (!whitelist.isEmpty() && !whitelist.contains(world.getName())) {
         return false;
      } else {
         return !blacklist.contains(world.getName());
      }
   }
}
