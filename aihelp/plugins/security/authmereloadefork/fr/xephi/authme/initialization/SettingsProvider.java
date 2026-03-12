package fr.xephi.authme.initialization;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyResource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.libs.javax.inject.Provider;
import fr.xephi.authme.service.yaml.YamlFileResourceProvider;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SettingsMigrationService;
import fr.xephi.authme.settings.properties.AuthMeSettingsRetriever;
import fr.xephi.authme.util.FileUtils;
import java.io.File;

public class SettingsProvider implements Provider<Settings> {
   @Inject
   @DataFolder
   private File dataFolder;
   @Inject
   private SettingsMigrationService migrationService;

   SettingsProvider() {
   }

   public Settings get() {
      File configFile = new File(this.dataFolder, "config.yml");
      if (!configFile.exists()) {
         FileUtils.create(configFile);
      }

      PropertyResource resource = YamlFileResourceProvider.loadFromFile(configFile);
      ConfigurationData configurationData = AuthMeSettingsRetriever.buildConfigurationData();
      return new Settings(this.dataFolder, resource, this.migrationService, configurationData);
   }
}
