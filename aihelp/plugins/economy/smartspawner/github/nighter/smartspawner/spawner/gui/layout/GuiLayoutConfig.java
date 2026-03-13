package github.nighter.smartspawner.spawner.gui.layout;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.updates.GuiLayoutUpdater;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class GuiLayoutConfig {
   private static final String GUI_LAYOUTS_DIR = "gui_layouts";
   private static final String STORAGE_GUI_FILE = "storage_gui.yml";
   private static final String MAIN_GUI_FILE = "main_gui.yml";
   private static final String SELL_CONFIRM_GUI_FILE = "sell_confirm_gui.yml";
   private static final String GUI_CONFIG_FILE = "gui_config.yml";
   private static final String DEFAULT_LAYOUT = "default";
   private static final int MIN_SLOT = 1;
   private static final int MAX_SLOT = 9;
   private static final int SLOT_OFFSET = 44;
   private static final int MAIN_GUI_SIZE = 27;
   private static final int SELL_CONFIRM_GUI_SIZE = 27;
   private final SmartSpawner plugin;
   private final File layoutsDir;
   private final GuiLayoutUpdater layoutUpdater;
   private String currentLayout;
   private GuiLayout currentStorageLayout;
   private GuiLayout currentMainLayout;
   private GuiLayout currentSellConfirmLayout;
   private boolean skipMainGui;
   private boolean skipSellConfirmation;

   public GuiLayoutConfig(SmartSpawner plugin) {
      this.plugin = plugin;
      this.layoutsDir = new File(plugin.getDataFolder(), "gui_layouts");
      this.layoutUpdater = new GuiLayoutUpdater(plugin);
      this.loadLayout();
   }

   public void loadLayout() {
      this.currentLayout = this.plugin.getConfig().getString("gui_layout", "default");
      this.initializeLayoutsDirectory();
      this.layoutUpdater.checkAndUpdateLayouts();
      this.loadGuiConfig();
      this.currentStorageLayout = this.loadCurrentStorageLayout();
      this.currentMainLayout = this.loadCurrentMainLayout();
      this.currentSellConfirmLayout = this.loadCurrentSellConfirmLayout();
   }

   private void initializeLayoutsDirectory() {
      if (!this.layoutsDir.exists()) {
         this.layoutsDir.mkdirs();
      }

      this.autoSaveLayoutFiles();
   }

   private void autoSaveLayoutFiles() {
      try {
         String[] layoutNames = new String[]{"default", "DonutSMP"};
         String[] var2 = layoutNames;
         int var3 = layoutNames.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String layoutName = var2[var4];
            File layoutDir = new File(this.layoutsDir, layoutName);
            if (!layoutDir.exists()) {
               layoutDir.mkdirs();
            }

            File storageFile = new File(layoutDir, "storage_gui.yml");
            String storageResourcePath = "gui_layouts/" + layoutName + "/storage_gui.yml";
            if (!storageFile.exists()) {
               try {
                  this.plugin.saveResource(storageResourcePath, false);
               } catch (Exception var17) {
                  this.plugin.getLogger().log(Level.WARNING, "Failed to auto-save storage layout resource for " + layoutName + ": " + var17.getMessage(), var17);
               }
            }

            File mainFile = new File(layoutDir, "main_gui.yml");
            String mainResourcePath = "gui_layouts/" + layoutName + "/main_gui.yml";
            if (!mainFile.exists()) {
               try {
                  this.plugin.saveResource(mainResourcePath, false);
               } catch (Exception var16) {
                  this.plugin.getLogger().log(Level.WARNING, "Failed to auto-save main layout resource for " + layoutName + ": " + var16.getMessage(), var16);
               }
            }

            File sellConfirmFile = new File(layoutDir, "sell_confirm_gui.yml");
            String sellConfirmResourcePath = "gui_layouts/" + layoutName + "/sell_confirm_gui.yml";
            if (!sellConfirmFile.exists()) {
               try {
                  this.plugin.saveResource(sellConfirmResourcePath, false);
               } catch (Exception var15) {
                  this.plugin.getLogger().log(Level.WARNING, "Failed to auto-save sell confirm layout resource for " + layoutName + ": " + var15.getMessage(), var15);
               }
            }
         }

         File guiConfigFile = new File(this.layoutsDir, "gui_config.yml");
         String guiConfigResourcePath = "gui_layouts/gui_config.yml";
         if (!guiConfigFile.exists()) {
            try {
               this.plugin.saveResource(guiConfigResourcePath, false);
            } catch (Exception var14) {
               this.plugin.getLogger().log(Level.WARNING, "Failed to auto-save GUI config file: " + var14.getMessage(), var14);
            }
         }
      } catch (Exception var18) {
         this.plugin.getLogger().log(Level.SEVERE, "Failed to auto-save layout files", var18);
      }

   }

   private void loadGuiConfig() {
      File guiConfigFile = new File(this.layoutsDir, "gui_config.yml");
      if (!guiConfigFile.exists()) {
         this.plugin.getLogger().warning("GUI config file not found, using defaults");
         this.skipMainGui = false;
         this.skipSellConfirmation = false;
      } else {
         try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(guiConfigFile);
            this.skipMainGui = config.getBoolean("skip_main_gui", false);
            this.skipSellConfirmation = config.getBoolean("skip_sell_confirmation", false);
         } catch (Exception var3) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to load GUI config, using defaults: " + var3.getMessage(), var3);
            this.skipMainGui = false;
            this.skipSellConfirmation = false;
         }

      }
   }

   private GuiLayout loadCurrentStorageLayout() {
      return this.loadLayoutFromFile("storage_gui.yml", "storage");
   }

   private GuiLayout loadCurrentMainLayout() {
      return this.loadLayoutFromFile("main_gui.yml", "main");
   }

   private GuiLayout loadCurrentSellConfirmLayout() {
      return this.loadLayoutFromFile("sell_confirm_gui.yml", "sell_confirm");
   }

   private GuiLayout loadLayoutFromFile(String fileName, String layoutType) {
      File layoutDir = new File(this.layoutsDir, this.currentLayout);
      File layoutFile = new File(layoutDir, fileName);
      if (layoutFile.exists()) {
         GuiLayout layout = this.loadLayout(layoutFile, layoutType);
         if (layout != null) {
            return layout;
         }
      }

      if (!this.currentLayout.equals("default")) {
         this.plugin.getLogger().warning("Layout '" + this.currentLayout + "' not found. Attempting to use default layout.");
         File defaultLayoutDir = new File(this.layoutsDir, "default");
         File defaultLayoutFile = new File(defaultLayoutDir, fileName);
         if (defaultLayoutFile.exists()) {
            GuiLayout defaultLayout = this.loadLayout(defaultLayoutFile, layoutType);
            if (defaultLayout != null) {
               this.plugin.getLogger().info("Loaded default " + layoutType + " layout as fallback");
               return defaultLayout;
            }
         }
      }

      this.plugin.getLogger().severe("No valid " + layoutType + " layout found! Creating empty layout as fallback.");
      return new GuiLayout();
   }

   private GuiLayout loadLayout(File file, String layoutType) {
      try {
         FileConfiguration config = YamlConfiguration.loadConfiguration(file);
         GuiLayout layout = new GuiLayout();
         Set<String> buttonKeys = config.getKeys(false);
         if (buttonKeys.isEmpty()) {
            this.plugin.getLogger().warning("No buttons found in GUI layout: " + file.getName());
            return layout;
         } else {
            Iterator var6 = buttonKeys.iterator();

            while(var6.hasNext()) {
               String buttonKey = (String)var6.next();
               if (buttonKey.startsWith("slot_") && !this.loadButton(config, layout, buttonKey, layoutType)) {
                  this.plugin.getLogger().warning("Failed to load button: " + buttonKey);
               }
            }

            return layout;
         }
      } catch (Exception var8) {
         this.plugin.getLogger().log(Level.WARNING, "Failed to load " + layoutType + " layout from " + file.getName() + ": " + var8.getMessage(), var8);
         return null;
      }
   }

   private boolean loadButton(FileConfiguration config, GuiLayout layout, String buttonKey, String layoutType) {
      int slot = this.parseSlotFromKey(buttonKey);
      if (slot == -1) {
         this.plugin.getLogger().warning("Invalid button key format: " + buttonKey + ". Expected format: slot_X or slot_X_name");
         return false;
      } else if (!config.getBoolean(buttonKey + ".enabled", true)) {
         return false;
      } else {
         String materialName = config.getString(buttonKey + ".material", "STONE");
         String condition = config.getString(buttonKey + ".condition", (String)null);
         boolean infoButton = config.getBoolean(buttonKey + ".info_button", false);
         if (!this.isValidSlot(slot, layoutType)) {
            this.plugin.getLogger().warning(String.format("Invalid slot %d for button %s in %s layout. Must be between %d and %d.", slot, buttonKey, layoutType, this.getMinSlot(layoutType), this.getMaxSlot(layoutType)));
            return false;
         } else if (condition != null && !this.evaluateCondition(condition)) {
            return false;
         } else {
            Material material = this.parseMaterial(materialName, buttonKey);
            int actualSlot = this.calculateActualSlot(slot, layoutType);
            Map<String, String> actions = new HashMap();
            ConfigurationSection ifSection = config.getConfigurationSection(buttonKey + ".if");
            if (ifSection != null) {
               Iterator var13 = ifSection.getKeys(false).iterator();

               while(var13.hasNext()) {
                  String conditionKey = (String)var13.next();
                  if (this.evaluateCondition(conditionKey)) {
                     ConfigurationSection conditionActions = ifSection.getConfigurationSection(conditionKey);
                     if (conditionActions != null) {
                        String conditionalMaterial = conditionActions.getString("material");
                        if (conditionalMaterial != null) {
                           material = this.parseMaterial(conditionalMaterial, buttonKey);
                        }

                        String[] clickTypes = new String[]{"click", "left_click", "right_click", "shift_left_click", "shift_right_click"};
                        String[] var18 = clickTypes;
                        int var19 = clickTypes.length;

                        for(int var20 = 0; var20 < var19; ++var20) {
                           String clickType = var18[var20];
                           String action = conditionActions.getString(clickType);
                           if (action != null && !action.isEmpty()) {
                              actions.put(clickType, action);
                           }
                        }
                     }
                     break;
                  }
               }
            } else {
               String[] clickTypes = new String[]{"click", "left_click", "right_click", "shift_left_click", "shift_right_click"};
               String[] var25 = clickTypes;
               int var26 = clickTypes.length;

               for(int var27 = 0; var27 < var26; ++var27) {
                  String clickType = var25[var27];
                  String action = config.getString(buttonKey + "." + clickType);
                  if (action != null && !action.isEmpty()) {
                     actions.put(clickType, action);
                  }
               }
            }

            GuiButton button = new GuiButton(buttonKey, actualSlot, material, true, condition, actions, infoButton);
            layout.addButton(buttonKey, button);
            return true;
         }
      }
   }

   private int parseSlotFromKey(String buttonKey) {
      if (!buttonKey.startsWith("slot_")) {
         return -1;
      } else {
         try {
            String slotPart = buttonKey.substring(5);
            int underscoreIndex = slotPart.indexOf(95);
            if (underscoreIndex > 0) {
               slotPart = slotPart.substring(0, underscoreIndex);
            }

            return Integer.parseInt(slotPart);
         } catch (NumberFormatException var4) {
            return -1;
         }
      }
   }

   private boolean isValidSlot(int slot, String layoutType) {
      return slot >= this.getMinSlot(layoutType) && slot <= this.getMaxSlot(layoutType);
   }

   private int getMinSlot(String layoutType) {
      return "storage".equals(layoutType) ? 1 : 1;
   }

   private int getMaxSlot(String layoutType) {
      if ("storage".equals(layoutType)) {
         return 9;
      } else {
         return "sell_confirm".equals(layoutType) ? 27 : 27;
      }
   }

   private int calculateActualSlot(int slot, String layoutType) {
      return "storage".equals(layoutType) ? 44 + slot : slot - 1;
   }

   private boolean evaluateCondition(String condition) {
      byte var3 = -1;
      switch(condition.hashCode()) {
      case -792184729:
         if (condition.equals("sell_integration")) {
            var3 = 0;
         }
         break;
      case 1514710821:
         if (condition.equals("no_sell_integration")) {
            var3 = 1;
         }
      }

      switch(var3) {
      case 0:
         return this.plugin.hasSellIntegration();
      case 1:
         return !this.plugin.hasSellIntegration();
      default:
         this.plugin.getLogger().warning("Unknown condition: " + condition);
         return true;
      }
   }

   private Material parseMaterial(String materialName, String buttonKey) {
      try {
         return Material.valueOf(materialName.toUpperCase());
      } catch (IllegalArgumentException var4) {
         this.plugin.getLogger().warning(String.format("Invalid material %s for button %s. Using STONE instead.", materialName, buttonKey));
         return Material.STONE;
      }
   }

   public GuiLayout getCurrentLayout() {
      return this.getCurrentStorageLayout();
   }

   public void reloadLayouts() {
      this.loadLayout();
   }

   @Generated
   public GuiLayout getCurrentStorageLayout() {
      return this.currentStorageLayout;
   }

   @Generated
   public GuiLayout getCurrentMainLayout() {
      return this.currentMainLayout;
   }

   @Generated
   public GuiLayout getCurrentSellConfirmLayout() {
      return this.currentSellConfirmLayout;
   }

   @Generated
   public boolean isSkipMainGui() {
      return this.skipMainGui;
   }

   @Generated
   public boolean isSkipSellConfirmation() {
      return this.skipSellConfirmation;
   }
}
