package github.nighter.smartspawner.updates;

import github.nighter.smartspawner.SmartSpawner;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class GuiLayoutUpdater {
   private final String currentVersion;
   private final SmartSpawner plugin;
   private static final String GUI_LAYOUT_VERSION_KEY = "gui_layout_version";
   private static final String GUI_LAYOUTS_DIR = "gui_layouts";
   private static final String[] LAYOUT_FILES = new String[]{"storage_gui.yml", "main_gui.yml", "sell_confirm_gui.yml"};
   private static final String[] LAYOUT_NAMES = new String[]{"default", "DonutSMP"};

   public GuiLayoutUpdater(SmartSpawner plugin) {
      this.plugin = plugin;
      this.currentVersion = plugin.getDescription().getVersion();
   }

   public void checkAndUpdateLayouts() {
      File layoutsDir = new File(this.plugin.getDataFolder(), "gui_layouts");
      if (!layoutsDir.exists()) {
         layoutsDir.mkdirs();
      }

      String[] var2 = LAYOUT_NAMES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String layoutName = var2[var4];
         File layoutDir = new File(layoutsDir, layoutName);
         if (!layoutDir.exists()) {
            layoutDir.mkdirs();
         }

         String[] var7 = LAYOUT_FILES;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String fileName = var7[var9];
            this.checkAndUpdateLayoutFile(layoutDir, layoutName, fileName);
         }
      }

   }

   private void checkAndUpdateLayoutFile(File layoutDir, String layoutName, String fileName) {
      File layoutFile = new File(layoutDir, fileName);
      if (!layoutFile.exists()) {
         this.createDefaultLayoutWithHeader(layoutDir, layoutName, fileName);
      } else {
         FileConfiguration currentLayout = YamlConfiguration.loadConfiguration(layoutFile);
         String layoutVersionStr = currentLayout.getString("gui_layout_version", "0.0.0");
         Version layoutVersion = new Version(layoutVersionStr);
         Version pluginVersion = new Version(this.currentVersion);
         if (layoutVersion.compareTo(pluginVersion) < 0) {
            boolean isNewFile = layoutVersionStr.equals("0.0.0");
            if (!isNewFile) {
               this.plugin.getLogger().info("Updating GUI layout " + layoutName + "/" + fileName + " from version " + layoutVersionStr + " to " + this.currentVersion);
            }

            try {
               Map<String, Object> userValues = this.flattenConfig(currentLayout);
               File tempFile = new File(layoutDir, fileName + ".new");
               this.createDefaultLayoutWithHeader(layoutDir, layoutName, fileName, tempFile);
               FileConfiguration newLayout = YamlConfiguration.loadConfiguration(tempFile);
               newLayout.set("gui_layout_version", this.currentVersion);
               if (!isNewFile) {
                  boolean layoutDiffers = this.hasLayoutDifferences(userValues, newLayout);
                  if (layoutDiffers) {
                     File backupFile = new File(layoutDir, fileName.replace(".yml", "_backup_" + layoutVersionStr + ".yml"));
                     Files.copy(layoutFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                     this.plugin.getLogger().info("GUI layout backup created at " + layoutName + "/" + backupFile.getName());
                  } else {
                     this.plugin.debug("No significant GUI layout changes detected for " + layoutName + "/" + fileName + ", skipping backup creation");
                  }
               }

               this.applyUserValues(newLayout, userValues);
               newLayout.save(layoutFile);
               tempFile.delete();
            } catch (Exception var15) {
               this.plugin.getLogger().severe("Failed to update GUI layout " + layoutName + "/" + fileName + ": " + var15.getMessage());
               var15.printStackTrace();
            }

         }
      }
   }

   private boolean hasLayoutDifferences(Map<String, Object> userValues, FileConfiguration newLayout) {
      Map<String, Object> newLayoutMap = this.flattenConfig(newLayout);
      Iterator var4 = userValues.entrySet().iterator();

      while(var4.hasNext()) {
         Entry<String, Object> entry = (Entry)var4.next();
         String path = (String)entry.getKey();
         Object oldValue = entry.getValue();
         if (!path.equals("gui_layout_version")) {
            if (!newLayout.contains(path)) {
               return true;
            }

            Object newDefaultValue = newLayout.get(path);
            if (newDefaultValue != null && !newDefaultValue.equals(oldValue)) {
               return true;
            }
         }
      }

      var4 = newLayoutMap.keySet().iterator();

      String path;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         path = (String)var4.next();
      } while(path.equals("gui_layout_version") || userValues.containsKey(path));

      return true;
   }

   private void createDefaultLayoutWithHeader(File layoutDir, String layoutName, String fileName) {
      this.createDefaultLayoutWithHeader(layoutDir, layoutName, fileName, new File(layoutDir, fileName));
   }

   private void createDefaultLayoutWithHeader(File layoutDir, String layoutName, String fileName, File destinationFile) {
      try {
         File parentDir = destinationFile.getParentFile();
         if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
         }

         String resourcePath = "gui_layouts/" + layoutName + "/" + fileName;
         InputStream in = this.plugin.getResource(resourcePath);

         try {
            if (in != null) {
               List<String> defaultLines = (new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))).lines().toList();
               List<String> newLines = new ArrayList();
               newLines.add("# GUI Layout version - Do not modify this value");
               newLines.add("gui_layout_version: " + this.currentVersion);
               newLines.add("");
               newLines.addAll(defaultLines);
               Files.write(destinationFile.toPath(), newLines, StandardCharsets.UTF_8);
            } else {
               this.plugin.getLogger().warning("Default GUI layout " + resourcePath + " not found in the plugin's resources.");
               destinationFile.createNewFile();
            }
         } catch (Throwable var11) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (in != null) {
            in.close();
         }
      } catch (IOException var12) {
         this.plugin.getLogger().severe("Failed to create default GUI layout with header for " + layoutName + "/" + fileName + ": " + var12.getMessage());
         var12.printStackTrace();
      }

   }

   private Map<String, Object> flattenConfig(ConfigurationSection config) {
      Map<String, Object> result = new HashMap();
      Iterator var3 = config.getKeys(true).iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         if (!config.isConfigurationSection(key)) {
            result.put(key, config.get(key));
         }
      }

      return result;
   }

   private void applyUserValues(FileConfiguration newLayout, Map<String, Object> userValues) {
      Iterator var3 = userValues.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Object> entry = (Entry)var3.next();
         String path = (String)entry.getKey();
         Object value = entry.getValue();
         if (!path.equals("gui_layout_version")) {
            if (newLayout.contains(path)) {
               newLayout.set(path, value);
            } else {
               this.plugin.getLogger().warning("GUI layout path '" + path + "' from old layout no longer exists in new layout");
            }
         }
      }

   }
}
