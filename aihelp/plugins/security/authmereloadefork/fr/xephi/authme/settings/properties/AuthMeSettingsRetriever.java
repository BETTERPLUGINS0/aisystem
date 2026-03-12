package fr.xephi.authme.settings.properties;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationDataBuilder;

public final class AuthMeSettingsRetriever {
   private AuthMeSettingsRetriever() {
   }

   public static ConfigurationData buildConfigurationData() {
      return ConfigurationDataBuilder.createConfiguration(DatabaseSettings.class, PluginSettings.class, RestrictionSettings.class, EmailSettings.class, HooksSettings.class, ProtectionSettings.class, PurgeSettings.class, SecuritySettings.class, RegistrationSettings.class, LimboSettings.class, BackupSettings.class, ConverterSettings.class);
   }
}
