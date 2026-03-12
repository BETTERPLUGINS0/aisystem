package fr.xephi.authme.message;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.message.I18NUtils;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class AbstractMessageFileHandler implements Reloadable {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(AbstractMessageFileHandler.class);
   @DataFolder
   @Inject
   private File dataFolder;
   @Inject
   private Settings settings;
   private String filename;
   private FileConfiguration configuration;
   private Map<String, FileConfiguration> i18nConfiguration;
   private final String defaultFile = this.createFilePath("en");

   protected AbstractMessageFileHandler() {
   }

   @PostConstruct
   public void reload() {
      String language = (String)this.settings.getProperty(PluginSettings.MESSAGES_LANGUAGE);
      this.filename = this.createFilePath(language);
      File messagesFile = this.initializeFile(this.filename);
      this.configuration = YamlConfiguration.loadConfiguration(messagesFile);
      this.i18nConfiguration = null;
   }

   protected String getLanguage() {
      return (String)this.settings.getProperty(PluginSettings.MESSAGES_LANGUAGE);
   }

   protected File getUserLanguageFile() {
      return new File(this.dataFolder, this.filename);
   }

   protected String getFilename() {
      return this.filename;
   }

   public boolean hasSection(String path) {
      return this.configuration.get(path) != null;
   }

   public String getMessage(String key) {
      String message = this.configuration.getString(key);
      return message == null ? "Error retrieving message '" + key + "'" : message;
   }

   public String getMessageByLocale(String key, String locale) {
      if (locale != null && (Boolean)this.settings.getProperty(PluginSettings.I18N_MESSAGES)) {
         String message = this.getI18nConfiguration(locale).getString(key);
         return message == null ? "Error retrieving message '" + key + "'" : message;
      } else {
         return this.getMessage(key);
      }
   }

   public String getMessageIfExists(String key) {
      return this.configuration.getString(key);
   }

   public FileConfiguration getI18nConfiguration(String locale) {
      if (this.i18nConfiguration == null) {
         this.i18nConfiguration = new ConcurrentHashMap();
      }

      locale = I18NUtils.localeToCode(locale, this.settings);
      if (this.i18nConfiguration.containsKey(locale)) {
         return (FileConfiguration)this.i18nConfiguration.get(locale);
      } else {
         String i18nFilename = this.createFilePath(locale);
         File i18nMessagesFile = this.initializeFile(i18nFilename);
         FileConfiguration config = YamlConfiguration.loadConfiguration(i18nMessagesFile);
         this.i18nConfiguration.put(locale, config);
         return config;
      }
   }

   protected abstract String createFilePath(String var1);

   @VisibleForTesting
   File initializeFile(String filePath) {
      File file = new File(this.dataFolder, filePath);
      if (FileUtils.getResourceFromJar(filePath) != null && FileUtils.copyFileFromResource(file, filePath)) {
         return file;
      } else if (FileUtils.copyFileFromResource(file, this.defaultFile)) {
         return file;
      } else {
         this.logger.warning("Wanted to copy default messages file '" + this.defaultFile + "' from JAR but it didn't exist");
         return null;
      }
   }
}
