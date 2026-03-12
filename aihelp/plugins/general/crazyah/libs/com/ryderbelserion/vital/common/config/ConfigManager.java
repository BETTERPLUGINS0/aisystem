package libs.com.ryderbelserion.vital.common.config;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import java.io.File;
import libs.com.ryderbelserion.vital.common.VitalAPI;
import libs.com.ryderbelserion.vital.common.api.Provider;
import libs.com.ryderbelserion.vital.common.config.keys.ConfigKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class ConfigManager {
   private static final VitalAPI api = Provider.getApi();
   private static final File directory;
   private static final YamlFileResourceOptions options;
   private static SettingsManager config;

   public ConfigManager() {
      throw new AssertionError();
   }

   @Internal
   public static void load() {
      config = SettingsManagerBuilder.withYamlFile(new File(directory, "vital.yml"), options).useDefaultMigrationService().configurationData(ConfigKeys.class).create();
   }

   @Internal
   public static void reload() {
      config.reload();
   }

   @Internal
   public static void save() {
      config.save();
   }

   @Internal
   @NotNull
   public static SettingsManager getConfig() {
      return config;
   }

   static {
      directory = api.getDirectory();
      options = YamlFileResourceOptions.builder().indentationSize(2).build();
   }
}
