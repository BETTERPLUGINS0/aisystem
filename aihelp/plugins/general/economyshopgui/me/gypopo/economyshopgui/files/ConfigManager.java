package me.gypopo.economyshopgui.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.config.CommentedConfig;
import me.gypopo.economyshopgui.files.config.Config;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.ConfigUtil;
import me.gypopo.economyshopgui.util.NBTMaps;
import me.gypopo.economyshopgui.util.ServerInfo;
import me.gypopo.economyshopgui.util.exceptions.ConfigLoadException;
import me.gypopo.economyshopgui.util.exceptions.ConfigSaveException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
   private final EconomyShopGUI plugin;
   public static String badYaml = null;
   private static Config config;
   private final File sectionsFolder;
   private final File shopsFolder;
   private static final Map<String, Config> sections = new HashMap();
   private static final Map<String, Config> shops = new HashMap();

   public ConfigManager(EconomyShopGUI plugin) {
      this.plugin = plugin;
      this.sectionsFolder = new File(this.plugin.getDataFolder() + "/sections/");
      this.shopsFolder = new File(this.plugin.getDataFolder() + "/shops/");
   }

   public static Config getConfig() {
      return config;
   }

   public static void saveConfig() {
      try {
         config.save();
         config.reload();
      } catch (ConfigLoadException | ConfigSaveException var1) {
         SendMessage.errorMessage(var1.getMessage());
         badYaml = "config";
      }

   }

   public static Config getSection(String section) {
      return (Config)sections.get(section);
   }

   public static Set<String> getSections() {
      return sections.keySet();
   }

   public static void saveSection(String section) {
      try {
         Config config = (Config)sections.get(section);
         config.save();
         config.reload();
      } catch (ConfigLoadException | ConfigSaveException var2) {
         SendMessage.errorMessage(var2.getMessage());
         badYaml = section;
      }

   }

   public Config createSectionConfig(String section) {
      File file = new File(this.plugin.getDataFolder() + File.separator + "sections" + File.separator, section + ".yml");

      try {
         if (!file.exists()) {
            file.createNewFile();
         }

         sections.put(section, this.loadConf(file, false));
         return (Config)sections.get(section);
      } catch (IOException var4) {
         SendMessage.warnMessage("A unknown IOE exception occurred while creating a new section config at " + file.getAbsolutePath());
         return null;
      }
   }

   public void deleteSection(String section) {
      try {
         Config config = (Config)sections.remove(section);
         ConfigUtil.createBackup(config.getFilePath());
         Files.deleteIfExists(config.getFilePath());
      } catch (IOException var3) {
         SendMessage.errorMessage("Failed to delete '" + section + "' sections config");
         var3.printStackTrace();
      }

   }

   public static Config getShop(String shop) {
      return (Config)shops.get(shop);
   }

   public static Set<String> getShops() {
      return shops.keySet();
   }

   public static void saveShop(String shop) {
      try {
         Config config = (Config)shops.get(shop);
         config.save();
         config.reload();
      } catch (ConfigLoadException | ConfigSaveException var2) {
         SendMessage.errorMessage(var2.getMessage());
         badYaml = shop;
      }

   }

   public List<String> getItemsRaw(String shop) {
      List<String> raw = new ArrayList();
      Iterator var3 = this.getPages(shop, false).iterator();

      while(var3.hasNext()) {
         String p = (String)var3.next();
         ((Config)shops.get(shop)).getConfigurationSection("pages." + p + ".items").getKeys(false).forEach((i) -> {
            raw.add(p + ".items." + i);
         });
      }

      return raw;
   }

   public List<String> getPages(String shop, boolean canBeEmpty) {
      List<String> pages = new LinkedList();
      Config config = (Config)shops.get(shop);
      if (config.contains("pages")) {
         Iterator var5 = config.getConfigurationSection("pages").getKeys(false).iterator();

         while(true) {
            String p;
            do {
               if (!var5.hasNext()) {
                  return pages;
               }

               p = (String)var5.next();
            } while(!canBeEmpty && !config.contains("pages." + p + ".items"));

            pages.add(p);
         }
      } else {
         return pages;
      }
   }

   public void deleteShop(String itemPath) {
      try {
         Config config = (Config)shops.remove(itemPath.split("\\.")[0]);
         ConfigUtil.createBackup(config.getFilePath());
         Files.deleteIfExists(config.getFilePath());
      } catch (IOException var3) {
         SendMessage.errorMessage("Failed to delete '" + itemPath.split("\\.")[0] + "' shops config");
         var3.printStackTrace();
      }

   }

   public Config createShopConfig(String shop) {
      File file = new File(this.plugin.getDataFolder() + File.separator + "shops" + File.separator, shop + ".yml");

      try {
         if (!file.exists()) {
            file.createNewFile();
         }

         shops.put(shop, this.loadConf(file, false));
         return (Config)shops.get(shop);
      } catch (IOException var4) {
         SendMessage.warnMessage("A unknown IOE exception occurred while creating a new shop config at " + file.getAbsolutePath());
         return null;
      }
   }

   public void deleteShopsConfig(String shop) {
      try {
         Config config = (Config)shops.remove(shop);
         ConfigUtil.createBackup(config.getFilePath());
         Files.deleteIfExists(config.getFilePath());
      } catch (IOException var3) {
         SendMessage.errorMessage("Failed to delete '" + shop + "' shops config");
         var3.printStackTrace();
      }

   }

   public static String getBadYaml() {
      return badYaml;
   }

   public boolean init() {
      if (badYaml == null) {
         config.setDef(YamlConfiguration.loadConfiguration(new InputStreamReader(this.plugin.getResource("config.yml"))));
      }

      try {
         this.loadSections();
         this.loadShops();
         return true;
      } catch (ConfigSaveException | ConfigLoadException var2) {
         SendMessage.errorMessage(var2.getMessage());
         return false;
      }
   }

   public boolean reload() {
      try {
         if (config != null) {
            File conf = config.getFilePath().toFile();
            if (!conf.exists()) {
               this.plugin.saveResource(conf.getName(), false);
               config.reload();
               this.initIgnoredTags();
            } else {
               config.reload();
               config.save();
            }
         } else {
            this.loadConfig();
         }

         if (badYaml != null && badYaml.equals("config")) {
            badYaml = null;
         }
      } catch (ConfigLoadException | ConfigSaveException var3) {
         SendMessage.errorMessage(var3.getMessage());
         badYaml = "config";
         return false;
      }

      try {
         this.loadSections();
         this.loadShops();
         return true;
      } catch (ConfigSaveException | ConfigLoadException var2) {
         SendMessage.errorMessage(var2.getMessage());
         return false;
      }
   }

   public void loadConfig() {
      boolean created = false;
      File conf = new File(this.plugin.getDataFolder(), "config.yml");
      if (!conf.exists()) {
         this.plugin.saveResource("config.yml", false);
         created = true;
      }

      config = this.loadConf(conf, true);
      if (config != null) {
         try {
            config.reload();
            config.save();
         } catch (ConfigLoadException | ConfigSaveException var4) {
            SendMessage.errorMessage(var4.getMessage());
            badYaml = "config";
         }

         if (created) {
            this.initIgnoredTags();
         }

      }
   }

   private void initIgnoredTags() {
      if (ServerInfo.supportsComponents()) {
         List<String> ignoredTags = config.getStringList("sold-items-ignored-NBTtags");

         for(int i = 0; i < ignoredTags.size(); ++i) {
            String tag = (String)ignoredTags.get(i);
            ignoredTags.set(i, NBTMaps.getTag(tag));
         }

         config.set("sold-items-ignored-NBTtags", ignoredTags);
         saveConfig();
      }
   }

   private void loadSections() throws ConfigSaveException, ConfigLoadException {
      if (!sections.isEmpty()) {
         sections.clear();
      }

      int loaded = 0;
      if (!this.sectionsFolder.exists() || !this.sectionsFolder.isDirectory()) {
         this.saveResources(this.sectionsFolder, "sections");
         this.createExampleSection();
      }

      File[] var2 = this.sectionsFolder.listFiles();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         File file = var2[var4];
         if (!file.isDirectory() && file.getName().split("\\.")[1].equals("yml")) {
            Config section = this.loadConf(file, false);
            if (section != null) {
               ++loaded;
               sections.put(file.getName().split("\\.")[0], section);
            }
         }
      }

      SendMessage.infoMessage(Lang.LOADED_SECTION_CONFIGURATIONS.get().replace("%total%", String.valueOf(loaded)));
   }

   private void saveSections() throws ConfigSaveException {
      try {
         InputStream stream = this.getClass().getClassLoader().getResourceAsStream("example.yml");
         OutputStream out = new FileOutputStream(this.plugin.getDataFolder().getAbsolutePath() + "/example.yml");
         byte[] buf = new byte[1024];

         int len;
         while((len = stream.read(buf)) > 0) {
            out.write(buf, 0, len);
         }

         out.close();
         stream.close();
         SendMessage.infoMessage(Lang.DEFAULT_SECTIONS_CREATED.get().replace("%path%", this.sectionsFolder.getAbsolutePath()));
      } catch (IOException var5) {
         throw new ConfigSaveException("A unknown IOE exception occurred while creating the default sections folder at " + this.sectionsFolder.getPath() + ":\n" + this.stackTraceToString(var5));
      }
   }

   private void loadShops() throws ConfigSaveException, ConfigLoadException {
      if (!shops.isEmpty()) {
         shops.clear();
      }

      int loaded = 0;
      if (!this.shopsFolder.exists() || !this.shopsFolder.isDirectory()) {
         String fileName = this.plugin.version + "-shops";
         if (this.getClass().getClassLoader().getResourceAsStream(fileName) == null) {
            fileName = "120-shops";
            SendMessage.warnMessage(Lang.CREATING_LATEST_SHOPS.get().replace("%version%", String.valueOf(this.plugin.version)));
         }

         this.saveResources(this.shopsFolder, fileName);

         try {
            Files.move((new File(this.plugin.getDataFolder() + File.separator + fileName + File.separator)).toPath(), this.shopsFolder.toPath());
            SendMessage.infoMessage(Lang.DEFAULT_SHOPS_CREATED.get().replace("%version%", String.valueOf(this.plugin.version)).replace("%path%", this.shopsFolder.getAbsolutePath()));
         } catch (IOException var7) {
            throw new ConfigLoadException("A unknown IOE occurred while renaming shops directory.\n" + this.stackTraceToString(var7));
         }

         this.createExampleShop();
      }

      File[] var8 = this.shopsFolder.listFiles();
      int var3 = var8.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         File file = var8[var4];
         if (!file.isDirectory() && file.getName().split("\\.")[1].equals("yml")) {
            Config shop = this.loadConf(file, false);
            if (shop != null) {
               ++loaded;
               shops.put(file.getName().split("\\.")[0], shop);
            }
         }
      }

      SendMessage.infoMessage(Lang.LOADED_SHOP_CONFIGURATIONS.get().replace("%total%", String.valueOf(loaded)));
   }

   public void validate() {
      SendMessage.infoMessage(Lang.UPDATING_SHOP_SETTINGS.get());
      ArrayList<String> shopShops = new ArrayList(getShops());
      ArrayList<String> shopSections = new ArrayList(getSections());
      Set<String> displayItems = (Set)shopSections.stream().filter((s) -> {
         return ((Config)sections.get(s)).getBoolean("display-item");
      }).collect(Collectors.toSet());
      if (!displayItems.isEmpty()) {
         shopSections.removeAll(displayItems);
         shopShops.removeAll(displayItems);
      }

      Iterator var4;
      String section;
      if (shopShops.size() != shopSections.size()) {
         Config config;
         if (shopShops.size() < shopSections.size()) {
            var4 = shopSections.iterator();

            while(var4.hasNext()) {
               section = (String)var4.next();
               if (!shopShops.contains(section)) {
                  SendMessage.warnMessage(Lang.CREATING_NEW_SHOP_CONFIG.get().replace("%section%", section));
                  config = this.createShopConfig(section);
                  if (config != null) {
                     config.set("pages.page1.items.example-item", this.getExampleItem(section));
                     saveShop(section);
                     shopShops.add(section);
                     shops.put(section, config);
                  }
               }
            }
         } else {
            var4 = shopShops.iterator();

            while(var4.hasNext()) {
               section = (String)var4.next();
               if (!shopSections.contains(section)) {
                  SendMessage.warnMessage(Lang.CREATING_NEW_SECTION_CONFIG.get().replace("%section%", section));
                  config = this.createSectionConfig(section);
                  if (config == null) {
                     return;
                  }

                  Map var10000 = this.getExampleShop(section);
                  Objects.requireNonNull(config);
                  var10000.forEach(config::set);
                  saveSection(section);
                  shopSections.add(section);
                  sections.put(section, config);
                  this.plugin.getSectionTitles().put(section, Lang.fromConfig("&a" + section));
               }
            }
         }
      }

      var4 = (new ArrayList(shopSections)).iterator();

      String section;
      while(var4.hasNext()) {
         section = (String)var4.next();
         if (!shopShops.contains(section)) {
            section = (String)shopShops.stream().filter((s) -> {
               return !shopSections.contains(s);
            }).findFirst().orElse((Object)null);
            if (section != null) {
               shops.remove(section);
            }

            shopSections.remove(section);
            sections.remove(section);
            SendMessage.errorMessage("Failed to match section file with shop file " + section + ".yml");
            SendMessage.errorMessage("Both files inside the /shops/ and /sections/ directory should have the same naming schema for this shop category, skipping...");
         }
      }

      List<String> disabledSections = new ArrayList();
      Iterator var8 = shopSections.iterator();

      while(var8.hasNext()) {
         section = (String)var8.next();
         if (!((Config)sections.get(section)).getBoolean("enable", true)) {
            disabledSections.add(section);
         }
      }

      shopSections.removeAll(disabledSections);
      shopSections.addAll(displayItems);
      this.plugin.setShopSections(shopSections);
   }

   private Map<String, Object> getExampleShop(String section) {
      Map<String, Object> keys = new HashMap();
      keys.put("enable", true);
      keys.put("slot", 0);
      keys.put("display-item", false);
      keys.put("hidden", false);
      keys.put("title", "&c&lExample section");
      keys.put("fill-item.material", "AIR");
      keys.put("nav-bar.mode", "INHERIT");
      keys.put("item.material", "BEACON");
      keys.put("item.displayname", "&a" + section);
      keys.put("item.enchantment-glint", false);
      keys.put("item.lore", Arrays.asList("&4This example section was automatically added", "&4because the shops config still exists,", "&4but there was no sections config found", "&4for section &c'" + section + "'.", "&fThis is a commun mistake while removing shops,", "&fmake sure both section and shop config files are removed."));
      return keys;
   }

   private Map<String, Object> getExampleItem(String section) {
      Map<String, Object> keys = new HashMap();
      keys.put("material", "STICK");
      keys.put("name", "&c&lPower stick v2");
      keys.put("displayname", "&c&lExample power stick");
      keys.put("buy", 1234);
      keys.put("sell", 123);
      keys.put("lore", Arrays.asList("&4This example item", "&4was added to shop", "&4because the shops.yml config", "&4did not contain section &c" + section));
      keys.put("enchantments", Arrays.asList("KNOCKBACK:2", "SHARPNESS:5", "FIRE_ASPECT:5", "BANE_OF_ARTHROPODS:5", "SMITE:5", "UNBREAKING:3", "LOOTING:5", "MENDING", "SWEEPING_EDGE:3"));
      keys.put("slot", 5);
      keys.put("stack-size", 2);
      keys.put("hidePricingLore", true);
      return keys;
   }

   private Config loadConf(File file, boolean comments) {
      try {
         if (badYaml != null && badYaml.equals(file.getName())) {
            badYaml = null;
         }

         return (Config)(comments ? new CommentedConfig(this.plugin, file, this.plugin.getResource(file.getName())) : new Config(file));
      } catch (ConfigLoadException var4) {
         SendMessage.errorMessage(var4.getMessage());
         badYaml = file.getName();
         return null;
      }
   }

   public void saveResources(File folder, String path) throws ConfigSaveException {
      try {
         JarFile file = new JarFile(EconomyShopGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
         Enumeration entries = file.entries();

         while(entries.hasMoreElements()) {
            JarEntry je = (JarEntry)entries.nextElement();
            if (je.getName().startsWith(path) && !je.getName().endsWith("/")) {
               this.plugin.saveResource(je.getName(), false);
            }
         }

      } catch (URISyntaxException | IOException var6) {
         throw new ConfigSaveException("A unknown IOE exception occurred while saving the default " + path + " folder at " + folder.getAbsolutePath() + ":\n" + this.stackTraceToString(var6));
      }
   }

   private void createExampleShop() {
      if (!(new File(this.plugin.getDataFolder(), "ExampleShop.yml")).exists()) {
         this.plugin.saveResource("ExampleShop.yml", false);
      }

   }

   private void createExampleSection() {
      if (!(new File(this.plugin.getDataFolder(), "ExampleSection.yml")).exists()) {
         this.plugin.saveResource("ExampleSection.yml", false);
      }

   }

   private String stackTraceToString(Exception e) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      return sw.toString();
   }

   public static final class CFile {
      final ConfigManager.CFile.Type type;
      final String name;

      public CFile(ConfigManager.CFile.Type type, String name) {
         this.type = type;
         this.name = name;
      }

      public String getName() {
         switch(this.type.ordinal()) {
         case 0:
         default:
            return this.name + ".yml";
         case 1:
            return "sections/" + this.name + ".yml";
         case 2:
            return "shops/" + this.name + ".yml";
         }
      }

      public void save() {
         switch(this.type.ordinal()) {
         case 2:
            ConfigManager.saveShop(this.name);
         case 1:
            ConfigManager.saveSection(this.name);
         case 0:
            ConfigManager.saveConfig();
         default:
         }
      }

      public static enum Type {
         CONFIG,
         SECTION,
         SHOP;

         // $FF: synthetic method
         private static ConfigManager.CFile.Type[] $values() {
            return new ConfigManager.CFile.Type[]{CONFIG, SECTION, SHOP};
         }
      }
   }
}
