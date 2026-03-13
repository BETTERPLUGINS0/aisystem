package github.nighter.smartspawner.language;

import github.nighter.smartspawner.SmartSpawner;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import lombok.Generated;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class LanguageManager {
   private final JavaPlugin plugin;
   private String defaultLocale;
   private final Map<String, LocaleData> localeMap = new HashMap();
   private final Set<String> activeLocales = new HashSet();
   private final Set<LanguageManager.LanguageFileType> activeFileTypes = new HashSet();
   private LocaleData cachedDefaultLocaleData;
   private static final Map<String, String> EMPTY_PLACEHOLDERS = Collections.emptyMap();
   private final LRUCache<String, String> formattedStringCache;
   private final LRUCache<String, String[]> loreCache;
   private final LRUCache<String, List<String>> loreListCache;
   private final LRUCache<String, String> guiItemNameCache;
   private final LRUCache<String, String[]> guiItemLoreCache;
   private final LRUCache<String, List<String>> guiItemLoreListCache;
   private final LRUCache<String, String> entityNameCache;
   private final LRUCache<String, String> smallCapsCache;
   private final LRUCache<String, String> materialNameCache;
   private final AtomicInteger cacheHits = new AtomicInteger(0);
   private final AtomicInteger cacheMisses = new AtomicInteger(0);
   private static final int DEFAULT_STRING_CACHE_SIZE = 1000;
   private static final int DEFAULT_LORE_CACHE_SIZE = 250;
   private static final int DEFAULT_LORE_LIST_CACHE_SIZE = 250;

   public LanguageManager(JavaPlugin plugin) {
      this.plugin = plugin;
      this.defaultLocale = plugin.getConfig().getString("language", "en_US");
      this.activeFileTypes.addAll(Arrays.asList(LanguageManager.LanguageFileType.values()));
      this.formattedStringCache = new LRUCache(1000);
      this.loreCache = new LRUCache(250);
      this.loreListCache = new LRUCache(250);
      this.guiItemNameCache = new LRUCache(1000);
      this.guiItemLoreCache = new LRUCache(250);
      this.guiItemLoreListCache = new LRUCache(250);
      this.entityNameCache = new LRUCache(250);
      this.smallCapsCache = new LRUCache(500);
      this.materialNameCache = new LRUCache(250);
      this.loadLanguages();
      this.saveDefaultFiles();
      this.cacheDefaultLocaleData();
   }

   public LanguageManager(SmartSpawner plugin, LanguageManager.LanguageFileType... fileTypes) {
      this.plugin = plugin;
      this.defaultLocale = plugin.getConfig().getString("language", "en_US");
      this.activeFileTypes.addAll(Arrays.asList(fileTypes));
      this.formattedStringCache = new LRUCache(1000);
      this.loreCache = new LRUCache(250);
      this.loreListCache = new LRUCache(250);
      this.guiItemNameCache = new LRUCache(1000);
      this.guiItemLoreCache = new LRUCache(250);
      this.guiItemLoreListCache = new LRUCache(250);
      this.entityNameCache = new LRUCache(250);
      this.smallCapsCache = new LRUCache(500);
      this.materialNameCache = new LRUCache(250);
      this.loadLanguages(fileTypes);
      this.saveDefaultFiles();
      this.cacheDefaultLocaleData();
   }

   private void saveDefaultFiles() {
      Map<String, Set<LanguageManager.LanguageFileType>> localeFileMap = new HashMap();
      localeFileMap.put("vi_VN", EnumSet.allOf(LanguageManager.LanguageFileType.class));
      localeFileMap.put("DonutSMP", EnumSet.allOf(LanguageManager.LanguageFileType.class));
      localeFileMap.put("de_DE", EnumSet.allOf(LanguageManager.LanguageFileType.class));
      localeFileMap.forEach((locale, fileTypes) -> {
         fileTypes.forEach((fileType) -> {
            this.saveResource(String.format("language/%s/%s", locale, fileType.getFileName()));
         });
      });
   }

   private void saveResource(String resourcePath) {
      File resourceFile = new File(this.plugin.getDataFolder(), resourcePath);
      if (!resourceFile.exists()) {
         resourceFile.getParentFile().mkdirs();
         this.plugin.saveResource(resourcePath, false);
      }

   }

   public void loadLanguages() {
      this.loadLanguages((LanguageManager.LanguageFileType[])this.activeFileTypes.toArray(new LanguageManager.LanguageFileType[0]));
   }

   public void loadLanguages(LanguageManager.LanguageFileType... fileTypes) {
      File langDir = new File(this.plugin.getDataFolder(), "language");
      if (!langDir.exists() && !langDir.mkdirs()) {
         this.plugin.getLogger().severe("Failed to create language directory!");
      } else {
         this.localeMap.remove(this.defaultLocale);
         this.loadLocale(this.defaultLocale, fileTypes);
         this.activeLocales.add(this.defaultLocale);
      }
   }

   private void cacheDefaultLocaleData() {
      this.cachedDefaultLocaleData = (LocaleData)this.localeMap.get(this.defaultLocale);
      if (this.cachedDefaultLocaleData == null) {
         this.plugin.getLogger().severe("Failed to cache default locale data for " + this.defaultLocale);
         this.cachedDefaultLocaleData = new LocaleData(new YamlConfiguration(), new YamlConfiguration(), new YamlConfiguration(), new YamlConfiguration());
         this.localeMap.put(this.defaultLocale, this.cachedDefaultLocaleData);
      }

   }

   public void reloadLanguages() {
      this.clearCache();
      String previousDefaultLocale = this.defaultLocale;
      this.defaultLocale = this.plugin.getConfig().getString("language", "en_US");
      Iterator var2 = this.activeLocales.iterator();

      while(var2.hasNext()) {
         String locale = (String)var2.next();
         this.localeMap.remove(locale);
         Iterator var4 = this.activeFileTypes.iterator();

         while(var4.hasNext()) {
            LanguageManager.LanguageFileType fileType = (LanguageManager.LanguageFileType)var4.next();
            YamlConfiguration config = this.loadOrCreateFile(locale, fileType.getFileName(), true);
            this.updateLocaleData(locale, fileType, config);
         }
      }

      if (!this.activeLocales.contains(this.defaultLocale)) {
         this.loadLocale(this.defaultLocale, (LanguageManager.LanguageFileType[])this.activeFileTypes.toArray(new LanguageManager.LanguageFileType[0]));
         this.activeLocales.add(this.defaultLocale);
      }

      this.cacheDefaultLocaleData();
      this.plugin.getLogger().info("Successfully reloaded language files for language " + this.defaultLocale);
   }

   private void updateLocaleData(String locale, LanguageManager.LanguageFileType fileType, YamlConfiguration config) {
      LocaleData existingData = (LocaleData)this.localeMap.getOrDefault(locale, new LocaleData(new YamlConfiguration(), new YamlConfiguration(), new YamlConfiguration(), new YamlConfiguration()));
      switch(fileType.ordinal()) {
      case 0:
         this.localeMap.put(locale, new LocaleData(config, existingData.gui(), existingData.formatting(), existingData.items()));
         break;
      case 1:
         this.localeMap.put(locale, new LocaleData(existingData.messages(), config, existingData.formatting(), existingData.items()));
         break;
      case 2:
         this.localeMap.put(locale, new LocaleData(existingData.messages(), existingData.gui(), config, existingData.items()));
         break;
      case 3:
         this.localeMap.put(locale, new LocaleData(existingData.messages(), existingData.gui(), existingData.formatting(), config));
      }

   }

   private YamlConfiguration loadOrCreateFile(String locale, String fileName, boolean forceReload) {
      File file = new File(this.plugin.getDataFolder(), "language/" + locale + "/" + fileName);
      YamlConfiguration defaultConfig = new YamlConfiguration();
      YamlConfiguration userConfig = new YamlConfiguration();
      String var10001 = this.defaultLocale;
      boolean defaultResourceExists = this.plugin.getResource("language/" + var10001 + "/" + fileName) != null;
      InputStream inputStream;
      if (defaultResourceExists) {
         try {
            inputStream = this.plugin.getResource("language/" + this.defaultLocale + "/" + fileName);

            try {
               if (inputStream != null) {
                  defaultConfig.loadFromString(new String(inputStream.readAllBytes()));
               }
            } catch (Throwable var17) {
               if (inputStream != null) {
                  try {
                     inputStream.close();
                  } catch (Throwable var13) {
                     var17.addSuppressed(var13);
                  }
               }

               throw var17;
            }

            if (inputStream != null) {
               inputStream.close();
            }
         } catch (Exception var18) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load default " + fileName, var18);
         }
      }

      if (!file.exists() && defaultResourceExists) {
         try {
            inputStream = this.plugin.getResource("language/" + this.defaultLocale + "/" + fileName);

            try {
               if (inputStream != null) {
                  file.getParentFile().mkdirs();
                  Files.copy(inputStream, file.toPath(), new CopyOption[0]);
               }
            } catch (Throwable var15) {
               if (inputStream != null) {
                  try {
                     inputStream.close();
                  } catch (Throwable var11) {
                     var15.addSuppressed(var11);
                  }
               }

               throw var15;
            }

            if (inputStream != null) {
               inputStream.close();
            }
         } catch (IOException var16) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to create " + fileName + " for locale " + locale, var16);
            return new YamlConfiguration();
         }
      }

      if (!file.exists()) {
         return new YamlConfiguration();
      } else {
         try {
            if (forceReload) {
               userConfig = new YamlConfiguration();
            }

            if (forceReload) {
               userConfig = YamlConfiguration.loadConfiguration(file);
            } else {
               userConfig.load(file);
            }
         } catch (Exception var14) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to load " + fileName + " for locale " + locale + ". Using defaults.", var14);
            return defaultConfig;
         }

         boolean updated = false;
         Iterator var9 = defaultConfig.getKeys(false).iterator();

         while(var9.hasNext()) {
            String key = (String)var9.next();
            if (!userConfig.contains(key)) {
               userConfig.set(key, defaultConfig.get(key));
               updated = true;
            }
         }

         if (updated) {
            try {
               userConfig.save(file);
               this.plugin.getLogger().info("Updated " + fileName + " for locale " + locale);
            } catch (IOException var12) {
               this.plugin.getLogger().log(Level.WARNING, "Failed to save updated " + fileName + " for locale " + locale, var12);
            }
         }

         return userConfig;
      }
   }

   private YamlConfiguration loadOrCreateFile(String locale, String fileName) {
      return this.loadOrCreateFile(locale, fileName, false);
   }

   private void loadLocale(String locale, LanguageManager.LanguageFileType... fileTypes) {
      File localeDir = new File(this.plugin.getDataFolder(), "language/" + locale);
      if (!localeDir.exists() && !localeDir.mkdirs()) {
         this.plugin.getLogger().severe("Failed to create locale directory for " + locale);
      } else {
         YamlConfiguration messages = null;
         YamlConfiguration gui = null;
         YamlConfiguration formatting = null;
         YamlConfiguration items = null;
         LanguageManager.LanguageFileType[] var8 = fileTypes;
         int var9 = fileTypes.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            LanguageManager.LanguageFileType fileType = var8[var10];
            switch(fileType.ordinal()) {
            case 0:
               messages = this.loadOrCreateFile(locale, fileType.getFileName());
               break;
            case 1:
               gui = this.loadOrCreateFile(locale, fileType.getFileName());
               break;
            case 2:
               formatting = this.loadOrCreateFile(locale, fileType.getFileName());
               break;
            case 3:
               items = this.loadOrCreateFile(locale, fileType.getFileName());
            }
         }

         if (messages == null) {
            messages = new YamlConfiguration();
         }

         if (gui == null) {
            gui = new YamlConfiguration();
         }

         if (formatting == null) {
            formatting = new YamlConfiguration();
         }

         if (items == null) {
            items = new YamlConfiguration();
         }

         this.localeMap.put(locale, new LocaleData(messages, gui, formatting, items));
      }
   }

   public String getMessage(String key, Map<String, String> placeholders) {
      if (!this.isMessageEnabled(key)) {
         return null;
      } else {
         String message = this.cachedDefaultLocaleData.messages().getString(key + ".message");
         if (message == null) {
            return "Missing message: " + key;
         } else {
            String prefix = this.getPrefix();
            message = prefix + message;
            return this.applyPlaceholdersAndColors(message, placeholders);
         }
      }
   }

   public String getMessageWithoutPrefix(String key, Map<String, String> placeholders) {
      if (!this.isMessageEnabled(key)) {
         return null;
      } else {
         String message = this.cachedDefaultLocaleData.messages().getString(key + ".message");
         return message == null ? "Missing message: " + key : this.applyPlaceholdersAndColors(message, placeholders);
      }
   }

   public String getMessageForConsole(String key, Map<String, String> placeholders) {
      if (!this.isMessageEnabled(key)) {
         return null;
      } else {
         String message = this.cachedDefaultLocaleData.messages().getString(key + ".message");
         return message == null ? "Missing message: " + key : this.applyOnlyPlaceholders(message, placeholders);
      }
   }

   public String getTitle(String key, Map<String, String> placeholders) {
      return !this.isMessageEnabled(key) ? null : this.getRawMessage(key + ".title", placeholders);
   }

   public String getSubtitle(String key, Map<String, String> placeholders) {
      return !this.isMessageEnabled(key) ? null : this.getRawMessage(key + ".subtitle", placeholders);
   }

   public String getActionBar(String key, Map<String, String> placeholders) {
      return !this.isMessageEnabled(key) ? null : this.getRawMessage(key + ".action_bar", placeholders);
   }

   public String getSound(String key) {
      return !this.isMessageEnabled(key) ? null : this.cachedDefaultLocaleData.messages().getString(key + ".sound");
   }

   private String getPrefix() {
      return this.cachedDefaultLocaleData.messages().getString("prefix", "&7[Server] &r");
   }

   String getRawMessage(String path, Map<String, String> placeholders) {
      String message = this.cachedDefaultLocaleData.messages().getString(path);
      return message == null ? null : this.applyPlaceholdersAndColors(message, placeholders);
   }

   private boolean isMessageEnabled(String key) {
      return this.cachedDefaultLocaleData.messages().getBoolean(key + ".enabled", true);
   }

   public boolean keyExists(String key) {
      return this.cachedDefaultLocaleData.messages().contains(key);
   }

   public String getGuiTitle(String key) {
      return this.getGuiTitle(key, EMPTY_PLACEHOLDERS);
   }

   public String getGuiTitle(String key, Map<String, String> placeholders) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.GUI)) {
         return null;
      } else {
         String title = this.cachedDefaultLocaleData.gui().getString(key);
         return title == null ? "Missing GUI title: " + key : this.applyPlaceholdersAndColors(title, placeholders);
      }
   }

   public String getGuiItemName(String key) {
      return this.getGuiItemName(key, EMPTY_PLACEHOLDERS);
   }

   public String getGuiItemName(String key, Map<String, String> placeholders) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.GUI)) {
         return null;
      } else {
         String cacheKey = key + "|" + this.generateCacheKey("", placeholders);
         String cachedName = (String)this.guiItemNameCache.get(cacheKey);
         if (cachedName != null) {
            this.cacheHits.incrementAndGet();
            return cachedName;
         } else {
            this.cacheMisses.incrementAndGet();
            String name = this.cachedDefaultLocaleData.gui().getString(key);
            if (name == null) {
               return "Missing item name: " + key;
            } else {
               String result = this.applyPlaceholdersAndColors(name, placeholders);
               this.guiItemNameCache.put(cacheKey, result);
               return result;
            }
         }
      }
   }

   public String[] getGuiItemLore(String key) {
      return this.getGuiItemLore(key, EMPTY_PLACEHOLDERS);
   }

   public String[] getGuiItemLore(String key, Map<String, String> placeholders) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.GUI)) {
         return new String[0];
      } else {
         String cacheKey = key + "|" + this.generateCacheKey("", placeholders);
         String[] cachedLore = (String[])this.guiItemLoreCache.get(cacheKey);
         if (cachedLore != null) {
            this.cacheHits.incrementAndGet();
            return cachedLore;
         } else {
            this.cacheMisses.incrementAndGet();
            List<String> loreList = this.cachedDefaultLocaleData.gui().getStringList(key);
            String[] result = (String[])loreList.stream().map((line) -> {
               return this.applyPlaceholdersAndColors(line, placeholders);
            }).toArray((x$0) -> {
               return new String[x$0];
            });
            this.guiItemLoreCache.put(cacheKey, result);
            return result;
         }
      }
   }

   public List<String> getGuiItemLoreAsList(String key) {
      return this.getGuiItemLoreAsList(key, EMPTY_PLACEHOLDERS);
   }

   public List<String> getGuiItemLoreAsList(String key, Map<String, String> placeholders) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.GUI)) {
         return Collections.emptyList();
      } else {
         String cacheKey = key + "|" + this.generateCacheKey("", placeholders);
         List<String> cachedLore = (List)this.guiItemLoreListCache.get(cacheKey);
         if (cachedLore != null) {
            this.cacheHits.incrementAndGet();
            return cachedLore;
         } else {
            this.cacheMisses.incrementAndGet();
            List<String> loreList = this.cachedDefaultLocaleData.gui().getStringList(key);
            List<String> result = loreList.stream().map((line) -> {
               return this.applyPlaceholdersAndColors(line, placeholders);
            }).toList();
            this.guiItemLoreListCache.put(cacheKey, result);
            return result;
         }
      }
   }

   public List<String> getGuiItemLoreWithMultilinePlaceholders(String key, Map<String, String> placeholders) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.GUI)) {
         return Collections.emptyList();
      } else {
         List<String> result = new ArrayList();
         List<String> loreList = this.cachedDefaultLocaleData.gui().getStringList(key);
         Iterator var5 = loreList.iterator();

         while(true) {
            label48:
            while(var5.hasNext()) {
               String line = (String)var5.next();
               boolean containsMultilinePlaceholder = false;
               Iterator var8 = placeholders.entrySet().iterator();

               while(var8.hasNext()) {
                  Entry<String, String> entry = (Entry)var8.next();
                  String placeholder = "{" + (String)entry.getKey() + "}";
                  if (line.contains(placeholder) && ((String)entry.getValue()).contains("\n")) {
                     containsMultilinePlaceholder = true;
                     break;
                  }
               }

               if (containsMultilinePlaceholder) {
                  String processedLine = line;
                  Iterator var18 = placeholders.entrySet().iterator();

                  String placeholder;
                  String value;
                  Entry entry;
                  while(var18.hasNext()) {
                     entry = (Entry)var18.next();
                     placeholder = "{" + (String)entry.getKey() + "}";
                     value = (String)entry.getValue();
                     if (!value.contains("\n")) {
                        processedLine = processedLine.replace(placeholder, value);
                     }
                  }

                  var18 = placeholders.entrySet().iterator();

                  while(true) {
                     do {
                        do {
                           if (!var18.hasNext()) {
                              continue label48;
                           }

                           entry = (Entry)var18.next();
                           placeholder = "{" + (String)entry.getKey() + "}";
                           value = (String)entry.getValue();
                        } while(!processedLine.contains(placeholder));
                     } while(!value.contains("\n"));

                     String[] valueLines = value.split("\n");
                     String firstLine = processedLine.replace(placeholder, valueLines[0]);
                     result.add(ColorUtil.translateHexColorCodes(firstLine));
                     String lineStart = processedLine.substring(0, processedLine.indexOf(placeholder));

                     for(int i = 1; i < valueLines.length; ++i) {
                        result.add(ColorUtil.translateHexColorCodes(lineStart + valueLines[i]));
                     }
                  }
               } else {
                  result.add(this.applyPlaceholdersAndColors(line, placeholders));
               }
            }

            return result;
         }
      }
   }

   public String getVanillaItemName(Material material) {
      if (material == null) {
         return "Unknown Item";
      } else {
         String cacheKey = "material|" + material.name();
         String cachedName = (String)this.materialNameCache.get(cacheKey);
         if (cachedName != null) {
            this.cacheHits.incrementAndGet();
            return cachedName;
         } else {
            this.cacheMisses.incrementAndGet();
            String key = "item." + material.name() + ".name";
            String name = null;
            if (this.activeFileTypes.contains(LanguageManager.LanguageFileType.ITEMS)) {
               name = this.cachedDefaultLocaleData.items().getString(key);
            }

            if (name == null) {
               name = this.formatEnumName(material.name());
            } else {
               name = this.applyPlaceholdersAndColors(name, (Map)null);
            }

            this.materialNameCache.put(cacheKey, name);
            return name;
         }
      }
   }

   public String[] getVanillaItemLore(Material material) {
      if (material == null) {
         return new String[0];
      } else {
         String key = "item." + material.name() + ".lore";
         return this.getItemLore(key);
      }
   }

   public String getItemName(String key) {
      return this.getItemName(key, EMPTY_PLACEHOLDERS);
   }

   public String getItemName(String key, Map<String, String> placeholders) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.ITEMS)) {
         return key;
      } else {
         String name = this.cachedDefaultLocaleData.items().getString(key);
         return name == null ? key : this.applyPlaceholdersAndColors(name, placeholders);
      }
   }

   public String[] getItemLore(String key) {
      return this.getItemLore(key, EMPTY_PLACEHOLDERS);
   }

   public String[] getItemLore(String key, Map<String, String> placeholders) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.ITEMS)) {
         return new String[0];
      } else {
         String cacheKey = key + "|" + this.generateCacheKey("", placeholders);
         String[] cachedLore = (String[])this.loreCache.get(cacheKey);
         if (cachedLore != null) {
            this.cacheHits.incrementAndGet();
            return cachedLore;
         } else {
            this.cacheMisses.incrementAndGet();
            List<String> loreList = this.cachedDefaultLocaleData.items().getStringList(key);
            String[] result = (String[])loreList.stream().map((line) -> {
               return this.applyPlaceholdersAndColors(line, placeholders);
            }).toArray((x$0) -> {
               return new String[x$0];
            });
            this.loreCache.put(cacheKey, result);
            return result;
         }
      }
   }

   public List<String> getItemLoreWithMultilinePlaceholders(String key, Map<String, String> placeholders) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.ITEMS)) {
         return Collections.emptyList();
      } else {
         List<String> result = new ArrayList();
         List<String> loreList = this.cachedDefaultLocaleData.items().getStringList(key);
         Iterator var5 = loreList.iterator();

         while(true) {
            label48:
            while(var5.hasNext()) {
               String line = (String)var5.next();
               boolean containsMultilinePlaceholder = false;
               Iterator var8 = placeholders.entrySet().iterator();

               while(var8.hasNext()) {
                  Entry<String, String> entry = (Entry)var8.next();
                  String placeholder = "{" + (String)entry.getKey() + "}";
                  if (line.contains(placeholder) && ((String)entry.getValue()).contains("\n")) {
                     containsMultilinePlaceholder = true;
                     break;
                  }
               }

               if (containsMultilinePlaceholder) {
                  String processedLine = line;
                  Iterator var18 = placeholders.entrySet().iterator();

                  String placeholder;
                  String value;
                  Entry entry;
                  while(var18.hasNext()) {
                     entry = (Entry)var18.next();
                     placeholder = "{" + (String)entry.getKey() + "}";
                     value = (String)entry.getValue();
                     if (!value.contains("\n")) {
                        processedLine = processedLine.replace(placeholder, value);
                     }
                  }

                  var18 = placeholders.entrySet().iterator();

                  while(true) {
                     do {
                        do {
                           if (!var18.hasNext()) {
                              continue label48;
                           }

                           entry = (Entry)var18.next();
                           placeholder = "{" + (String)entry.getKey() + "}";
                           value = (String)entry.getValue();
                        } while(!processedLine.contains(placeholder));
                     } while(!value.contains("\n"));

                     String[] valueLines = value.split("\n");
                     String firstLine = processedLine.replace(placeholder, valueLines[0]);
                     result.add(ColorUtil.translateHexColorCodes(firstLine));
                     String lineStart = processedLine.substring(0, processedLine.indexOf(placeholder));

                     for(int i = 1; i < valueLines.length; ++i) {
                        result.add(ColorUtil.translateHexColorCodes(lineStart + valueLines[i]));
                     }
                  }
               } else {
                  result.add(this.applyPlaceholdersAndColors(line, placeholders));
               }
            }

            return result;
         }
      }
   }

   public String formatNumber(double number) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.FORMATTING)) {
         String var10000;
         double value;
         if (number >= 1.0E12D) {
            value = (double)Math.round(number / 1.0E12D * 10.0D) / 10.0D;
            var10000 = this.formatDecimal(value);
            return var10000 + "T";
         } else if (number >= 1.0E9D) {
            value = (double)Math.round(number / 1.0E9D * 10.0D) / 10.0D;
            var10000 = this.formatDecimal(value);
            return var10000 + "B";
         } else if (number >= 1000000.0D) {
            value = (double)Math.round(number / 1000000.0D * 10.0D) / 10.0D;
            var10000 = this.formatDecimal(value);
            return var10000 + "M";
         } else if (number >= 1000.0D) {
            value = (double)Math.round(number / 1000.0D * 10.0D) / 10.0D;
            var10000 = this.formatDecimal(value);
            return var10000 + "K";
         } else {
            value = (double)Math.round(number * 10.0D) / 10.0D;
            return this.formatDecimal(value);
         }
      } else {
         String format;
         double value;
         if (number >= 1.0E12D) {
            format = this.cachedDefaultLocaleData.formatting().getString("format_number.trillion", "{s}T");
            value = (double)Math.round(number / 1.0E12D * 10.0D) / 10.0D;
         } else if (number >= 1.0E9D) {
            format = this.cachedDefaultLocaleData.formatting().getString("format_number.billion", "{s}B");
            value = (double)Math.round(number / 1.0E9D * 10.0D) / 10.0D;
         } else if (number >= 1000000.0D) {
            format = this.cachedDefaultLocaleData.formatting().getString("format_number.million", "{s}M");
            value = (double)Math.round(number / 1000000.0D * 10.0D) / 10.0D;
         } else if (number >= 1000.0D) {
            format = this.cachedDefaultLocaleData.formatting().getString("format_number.thousand", "{s}K");
            value = (double)Math.round(number / 1000.0D * 10.0D) / 10.0D;
         } else {
            format = this.cachedDefaultLocaleData.formatting().getString("format_number.default", "{s}");
            value = (double)Math.round(number * 10.0D) / 10.0D;
         }

         return format.replace("{s}", this.formatDecimal(value));
      }
   }

   private String formatDecimal(double value) {
      return value == Math.floor(value) ? String.valueOf((int)value) : String.valueOf(value);
   }

   public String getFormattedMobName(EntityType type) {
      if (type != null && type != EntityType.UNKNOWN) {
         String mobNameKey = type.name();
         String cacheKey = "mob_name|" + mobNameKey;
         String cachedName = (String)this.entityNameCache.get(cacheKey);
         if (cachedName != null) {
            this.cacheHits.incrementAndGet();
            return cachedName;
         } else {
            this.cacheMisses.incrementAndGet();
            String result;
            if (this.activeFileTypes.contains(LanguageManager.LanguageFileType.FORMATTING)) {
               String formattedName = this.cachedDefaultLocaleData.formatting().getString("mob_names." + mobNameKey);
               if (formattedName != null) {
                  result = this.applyPlaceholdersAndColors(formattedName, (Map)null);
                  this.entityNameCache.put(cacheKey, result);
                  return result;
               }
            }

            result = this.formatEnumName(mobNameKey);
            this.entityNameCache.put(cacheKey, result);
            return result;
         }
      } else {
         return "Unknown";
      }
   }

   public String formatEnumName(String enumName) {
      String[] words = enumName.split("_");
      StringBuilder result = new StringBuilder();
      String[] var4 = words;
      int var5 = words.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String word = var4[var6];
         if (word.length() > 0) {
            result.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
         }
      }

      return result.toString().trim();
   }

   public String getSmallCaps(String text) {
      if (text != null && !text.isEmpty()) {
         String cacheKey = "smallcaps|" + text;
         String cachedText = (String)this.smallCapsCache.get(cacheKey);
         if (cachedText != null) {
            this.cacheHits.incrementAndGet();
            return cachedText;
         } else {
            this.cacheMisses.incrementAndGet();
            StringBuilder result = new StringBuilder();
            char[] var5 = text.toCharArray();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               char c = var5[var7];
               if (Character.isLetter(c)) {
                  char lowercaseChar = Character.toLowerCase(c);
                  char smallCapsChar = this.getSmallCapsChar(lowercaseChar);
                  result.append(smallCapsChar);
               } else {
                  result.append(c);
               }
            }

            String smallCapsText = result.toString();
            this.smallCapsCache.put(cacheKey, smallCapsText);
            return smallCapsText;
         }
      } else {
         return "";
      }
   }

   private char getSmallCapsChar(char c) {
      char var10000;
      switch(c) {
      case 'a':
         var10000 = 7424;
         break;
      case 'b':
         var10000 = 665;
         break;
      case 'c':
         var10000 = 7428;
         break;
      case 'd':
         var10000 = 7429;
         break;
      case 'e':
         var10000 = 7431;
         break;
      case 'f':
         var10000 = 'ꜰ';
         break;
      case 'g':
         var10000 = 610;
         break;
      case 'h':
         var10000 = 668;
         break;
      case 'i':
         var10000 = 618;
         break;
      case 'j':
         var10000 = 7434;
         break;
      case 'k':
         var10000 = 7435;
         break;
      case 'l':
         var10000 = 671;
         break;
      case 'm':
         var10000 = 7437;
         break;
      case 'n':
         var10000 = 628;
         break;
      case 'o':
         var10000 = 7439;
         break;
      case 'p':
         var10000 = 7448;
         break;
      case 'q':
         var10000 = 491;
         break;
      case 'r':
         var10000 = 640;
         break;
      case 's':
         var10000 = 'ꜱ';
         break;
      case 't':
         var10000 = 7451;
         break;
      case 'u':
         var10000 = 7452;
         break;
      case 'v':
         var10000 = 7456;
         break;
      case 'w':
         var10000 = 7457;
         break;
      case 'x':
         var10000 = 'x';
         break;
      case 'y':
         var10000 = 655;
         break;
      case 'z':
         var10000 = 7458;
         break;
      default:
         var10000 = c;
      }

      return var10000;
   }

   public String applyPlaceholdersAndColors(String text, Map<String, String> placeholders) {
      if (text == null) {
         return null;
      } else {
         String cacheKey = this.generateCacheKey(text, placeholders);
         String cachedResult = (String)this.formattedStringCache.get(cacheKey);
         if (cachedResult != null) {
            this.cacheHits.incrementAndGet();
            return cachedResult;
         } else {
            this.cacheMisses.incrementAndGet();
            String result = text;
            Entry entry;
            if (placeholders != null && !placeholders.isEmpty()) {
               for(Iterator var6 = placeholders.entrySet().iterator(); var6.hasNext(); result = result.replace("{" + (String)entry.getKey() + "}", (CharSequence)entry.getValue())) {
                  entry = (Entry)var6.next();
               }
            }

            result = ColorUtil.translateHexColorCodes(result);
            this.formattedStringCache.put(cacheKey, result);
            return result;
         }
      }
   }

   public String getColorCode(String path) {
      if (!this.activeFileTypes.contains(LanguageManager.LanguageFileType.GUI)) {
         return ChatColor.WHITE.toString();
      } else {
         String colorStr = this.cachedDefaultLocaleData.gui().getString(path);
         return colorStr == null ? ChatColor.WHITE.toString() : this.applyPlaceholdersAndColors(colorStr, EMPTY_PLACEHOLDERS);
      }
   }

   public String applyOnlyPlaceholders(String text, Map<String, String> placeholders) {
      if (text == null) {
         return null;
      } else {
         String cacheKey = this.generateCacheKey(text, placeholders);
         String cachedResult = (String)this.formattedStringCache.get(cacheKey);
         if (cachedResult != null) {
            this.cacheHits.incrementAndGet();
            return cachedResult;
         } else {
            this.cacheMisses.incrementAndGet();
            String result = text;
            Entry entry;
            if (placeholders != null && !placeholders.isEmpty()) {
               for(Iterator var6 = placeholders.entrySet().iterator(); var6.hasNext(); result = result.replace("{" + (String)entry.getKey() + "}", (CharSequence)entry.getValue())) {
                  entry = (Entry)var6.next();
               }
            }

            this.formattedStringCache.put(cacheKey, result);
            return result;
         }
      }
   }

   public String getHologramText() {
      if (this.plugin.getConfig().contains("hologram.text")) {
         List<String> lines = this.plugin.getConfig().getStringList("hologram.text");
         return !lines.isEmpty() ? String.join("\n", lines) : this.plugin.getConfig().getString("hologram.text");
      } else {
         return "&e%entity% Spawner &7[&f%stack_size%x&7]\n&7XP: &a%current_exp%&7/&a%max_exp%\n&7Items: &a%used_slots%&7/&a%max_slots%";
      }
   }

   public void clearCache() {
      this.formattedStringCache.clear();
      this.loreCache.clear();
      this.loreListCache.clear();
      this.guiItemNameCache.clear();
      this.guiItemLoreCache.clear();
      this.guiItemLoreListCache.clear();
      this.entityNameCache.clear();
      this.smallCapsCache.clear();
      this.materialNameCache.clear();
   }

   private String generateCacheKey(String text, Map<String, String> placeholders) {
      if (placeholders != null && !placeholders.isEmpty()) {
         StringBuilder keyBuilder = new StringBuilder(text);
         List<String> keys = new ArrayList(placeholders.keySet());
         Collections.sort(keys);
         Iterator var5 = keys.iterator();

         while(var5.hasNext()) {
            String key = (String)var5.next();
            keyBuilder.append('|').append(key).append('=').append((String)placeholders.get(key));
         }

         return keyBuilder.toString();
      } else {
         return text;
      }
   }

   public Map<String, Object> getCacheStats() {
      Map<String, Object> stats = new HashMap();
      stats.put("string_cache_size", this.formattedStringCache.size());
      stats.put("string_cache_capacity", this.formattedStringCache.capacity());
      stats.put("lore_cache_size", this.loreCache.size());
      stats.put("lore_cache_capacity", this.loreCache.capacity());
      stats.put("lore_list_cache_size", this.loreListCache.size());
      stats.put("lore_list_cache_capacity", this.loreListCache.capacity());
      stats.put("gui_name_cache_size", this.guiItemNameCache.size());
      stats.put("gui_name_cache_capacity", this.guiItemNameCache.capacity());
      stats.put("gui_lore_cache_size", this.guiItemLoreCache.size());
      stats.put("gui_lore_cache_capacity", this.guiItemLoreCache.capacity());
      stats.put("gui_lore_list_cache_size", this.guiItemLoreListCache.size());
      stats.put("gui_lore_list_cache_capacity", this.guiItemLoreListCache.capacity());
      stats.put("entity_name_cache_size", this.entityNameCache.size());
      stats.put("entity_name_cache_capacity", this.entityNameCache.capacity());
      stats.put("small_caps_cache_size", this.smallCapsCache.size());
      stats.put("small_caps_cache_capacity", this.smallCapsCache.capacity());
      stats.put("material_name_cache_size", this.materialNameCache.size());
      stats.put("material_name_cache_capacity", this.materialNameCache.capacity());
      stats.put("cache_hits", this.cacheHits.get());
      stats.put("cache_misses", this.cacheMisses.get());
      stats.put("hit_ratio", this.cacheHits.get() > 0 ? (double)this.cacheHits.get() / (double)(this.cacheHits.get() + this.cacheMisses.get()) : 0.0D);
      return stats;
   }

   @Generated
   public String getDefaultLocale() {
      return this.defaultLocale;
   }

   public static enum LanguageFileType {
      MESSAGES("messages.yml"),
      GUI("gui.yml"),
      FORMATTING("formatting.yml"),
      ITEMS("items.yml");

      private final String fileName;

      private LanguageFileType(String param3) {
         this.fileName = fileName;
      }

      @Generated
      public String getFileName() {
         return this.fileName;
      }

      // $FF: synthetic method
      private static LanguageManager.LanguageFileType[] $values() {
         return new LanguageManager.LanguageFileType[]{MESSAGES, GUI, FORMATTING, ITEMS};
      }
   }
}
