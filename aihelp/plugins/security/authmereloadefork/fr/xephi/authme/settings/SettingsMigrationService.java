package fr.xephi.authme.settings;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.migration.PlainMigrationService;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.properties.PropertyInitializer;
import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.PropertyValue;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.output.LogLevel;
import fr.xephi.authme.process.register.RegisterSecondaryArgument;
import fr.xephi.authme.process.register.RegistrationType;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.StringUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SettingsMigrationService extends PlainMigrationService {
   private static ConsoleLogger logger = ConsoleLoggerFactory.get(SettingsMigrationService.class);
   private final File pluginFolder;
   private String oldOtherAccountsCommand;
   private int oldOtherAccountsCommandThreshold;

   @Inject
   SettingsMigrationService(@DataFolder File pluginFolder) {
      this.pluginFolder = pluginFolder;
   }

   protected boolean performMigrations(PropertyReader reader, ConfigurationData configurationData) {
      boolean changes = false;
      if ("[a-zA-Z0-9_?]*".equals(reader.getString(RestrictionSettings.ALLOWED_NICKNAME_CHARACTERS.getPath()))) {
         configurationData.setValue(RestrictionSettings.ALLOWED_NICKNAME_CHARACTERS, "[a-zA-Z0-9_]*");
         changes = true;
      }

      String driverClass = reader.getString("DataSource.mySQLDriverClassName");
      if ("fr.xephi.authme.libs.org.mariadb.jdbc.Driver".equals(driverClass)) {
         configurationData.setValue(DatabaseSettings.BACKEND, DataSourceType.MARIADB);
         changes = true;
      }

      this.setOldOtherAccountsCommandFieldsIfSet(reader);
      return changes | this.performMailTextToFileMigration(reader) | migrateJoinLeaveMessages(reader, configurationData) | migrateForceSpawnSettings(reader, configurationData) | migratePoolSizeSetting(reader, configurationData) | changeBooleanSettingToLogLevelProperty(reader, configurationData) | hasOldHelpHeaderProperty(reader) | hasSupportOldPasswordProperty(reader) | convertToRegistrationType(reader, configurationData) | mergeAndMovePermissionGroupSettings(reader, configurationData) | moveDeprecatedHashAlgorithmIntoLegacySection(reader, configurationData) | moveSaltColumnConfigWithOtherColumnConfigs(reader, configurationData) || hasDeprecatedProperties(reader);
   }

   private static boolean hasDeprecatedProperties(PropertyReader reader) {
      String[] deprecatedProperties = new String[]{"Converter.Rakamak.newPasswordHash", "Hooks.chestshop", "Hooks.legacyChestshop", "Hooks.notifications", "Passpartu", "Performances", "settings.restrictions.enablePasswordVerifier", "Xenoforo.predefinedSalt", "VeryGames", "settings.restrictions.allowAllCommandsIfRegistrationIsOptional", "DataSource.mySQLWebsite", "Hooks.customAttributes", "Security.stop.kickPlayersBeforeStopping", "settings.restrictions.keepCollisionsDisabled", "settings.forceCommands", "settings.forceCommandsAsConsole", "settings.forceRegisterCommands", "settings.forceRegisterCommandsAsConsole", "settings.sessions.sessionExpireOnIpChange", "settings.restrictions.otherAccountsCmd", "settings.restrictions.otherAccountsCmdThreshold, DataSource.mySQLDriverClassName"};
      String[] var2 = deprecatedProperties;
      int var3 = deprecatedProperties.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String deprecatedPath = var2[var4];
         if (reader.contains(deprecatedPath)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasOldOtherAccountsCommand() {
      return !StringUtils.isBlank(this.oldOtherAccountsCommand);
   }

   public String getOldOtherAccountsCommand() {
      return this.oldOtherAccountsCommand;
   }

   public int getOldOtherAccountsCommandThreshold() {
      return this.oldOtherAccountsCommandThreshold;
   }

   private boolean performMailTextToFileMigration(PropertyReader reader) {
      String oldSettingPath = "Email.mailText";
      String oldMailText = reader.getString("Email.mailText");
      if (oldMailText == null) {
         return false;
      } else {
         File emailFile = new File(this.pluginFolder, "email.html");
         String mailText = oldMailText.replace("<playername>", "<playername />").replace("%playername%", "<playername />").replace("<servername>", "<servername />").replace("%servername%", "<servername />").replace("<generatedpass>", "<generatedpass />").replace("%generatedpass%", "<generatedpass />").replace("<image>", "<image />").replace("%image%", "<image />");
         if (!emailFile.exists()) {
            try {
               FileWriter fw = new FileWriter(emailFile);

               try {
                  fw.write(mailText);
               } catch (Throwable var10) {
                  try {
                     fw.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }

                  throw var10;
               }

               fw.close();
            } catch (IOException var11) {
               logger.logException("Could not create email.html configuration file:", var11);
            }
         }

         return true;
      }
   }

   private static boolean migrateJoinLeaveMessages(PropertyReader reader, ConfigurationData configData) {
      Property<Boolean> oldDelayJoinProperty = PropertyInitializer.newProperty("settings.delayJoinLeaveMessages", false);
      boolean hasMigrated = moveProperty(oldDelayJoinProperty, RegistrationSettings.DELAY_JOIN_MESSAGE, reader, configData);
      if (hasMigrated) {
         logger.info(String.format("Note that we now also have the settings %s and %s", RegistrationSettings.REMOVE_JOIN_MESSAGE.getPath(), RegistrationSettings.REMOVE_LEAVE_MESSAGE.getPath()));
      }

      return hasMigrated;
   }

   private static boolean migrateForceSpawnSettings(PropertyReader reader, ConfigurationData configData) {
      Property<Boolean> oldForceLocEnabled = PropertyInitializer.newProperty("settings.restrictions.ForceSpawnLocOnJoinEnabled", false);
      Property<List<String>> oldForceWorlds = PropertyInitializer.newListProperty("settings.restrictions.ForceSpawnOnTheseWorlds", "world", "world_nether", "world_the_ed");
      return moveProperty(oldForceLocEnabled, RestrictionSettings.FORCE_SPAWN_LOCATION_AFTER_LOGIN, reader, configData) | moveProperty(oldForceWorlds, RestrictionSettings.FORCE_SPAWN_ON_WORLDS, reader, configData);
   }

   private static boolean migratePoolSizeSetting(PropertyReader reader, ConfigurationData configData) {
      Integer oldValue = reader.getInt(DatabaseSettings.MYSQL_POOL_SIZE.getPath());
      if (oldValue != null && oldValue <= 0) {
         configData.setValue(DatabaseSettings.MYSQL_POOL_SIZE, 10);
         return true;
      } else {
         return false;
      }
   }

   private static boolean changeBooleanSettingToLogLevelProperty(PropertyReader reader, ConfigurationData configData) {
      String oldPath = "Security.console.noConsoleSpam";
      Property<LogLevel> newProperty = PluginSettings.LOG_LEVEL;
      if (!newProperty.isValidInResource(reader) && reader.contains("Security.console.noConsoleSpam")) {
         logger.info("Moving 'Security.console.noConsoleSpam' to '" + newProperty.getPath() + "'");
         boolean oldValue = (Boolean)Optional.ofNullable(reader.getBoolean("Security.console.noConsoleSpam")).orElse(false);
         LogLevel level = oldValue ? LogLevel.INFO : LogLevel.FINE;
         configData.setValue(newProperty, level);
         return true;
      } else {
         return false;
      }
   }

   private static boolean hasOldHelpHeaderProperty(PropertyReader reader) {
      if (reader.contains("settings.helpHeader")) {
         logger.warning("Help header setting is now in messages/help_xx.yml, please check the file to set it again");
         return true;
      } else {
         return false;
      }
   }

   private static boolean hasSupportOldPasswordProperty(PropertyReader reader) {
      String path = "settings.security.supportOldPasswordHash";
      if (reader.contains(path)) {
         logger.warning("Property '" + path + "' is no longer supported. Use '" + SecuritySettings.LEGACY_HASHES.getPath() + "' instead.");
         return true;
      } else {
         return false;
      }
   }

   private static boolean convertToRegistrationType(PropertyReader reader, ConfigurationData configData) {
      String oldEmailRegisterPath = "settings.registration.enableEmailRegistrationSystem";
      if (!RegistrationSettings.REGISTRATION_TYPE.isValidInResource(reader) && reader.contains(oldEmailRegisterPath)) {
         boolean useEmail = (Boolean)PropertyInitializer.newProperty(oldEmailRegisterPath, false).determineValue(reader).getValue();
         RegistrationType registrationType = useEmail ? RegistrationType.EMAIL : RegistrationType.PASSWORD;
         String useConfirmationPath = useEmail ? "settings.registration.doubleEmailCheck" : "settings.restrictions.enablePasswordConfirmation";
         boolean hasConfirmation = (Boolean)PropertyInitializer.newProperty(useConfirmationPath, false).determineValue(reader).getValue();
         RegisterSecondaryArgument secondaryArgument = hasConfirmation ? RegisterSecondaryArgument.CONFIRMATION : RegisterSecondaryArgument.NONE;
         logger.warning("Merging old registration settings into '" + RegistrationSettings.REGISTRATION_TYPE.getPath() + "'");
         configData.setValue(RegistrationSettings.REGISTRATION_TYPE, registrationType);
         configData.setValue(RegistrationSettings.REGISTER_SECOND_ARGUMENT, secondaryArgument);
         return true;
      } else {
         return false;
      }
   }

   private static boolean mergeAndMovePermissionGroupSettings(PropertyReader reader, ConfigurationData configData) {
      Property<String> oldUnloggedInGroup = PropertyInitializer.newProperty("settings.security.unLoggedinGroup", "");
      Property<String> oldRegisteredGroup = PropertyInitializer.newProperty("GroupOptions.RegisteredPlayerGroup", "");
      boolean performedChanges;
      if (!((String)oldUnloggedInGroup.determineValue(reader).getValue()).isEmpty()) {
         performedChanges = moveProperty(oldUnloggedInGroup, PluginSettings.REGISTERED_GROUP, reader, configData);
      } else {
         performedChanges = moveProperty(oldRegisteredGroup, PluginSettings.REGISTERED_GROUP, reader, configData);
      }

      performedChanges |= moveProperty(PropertyInitializer.newProperty("GroupOptions.UnregisteredPlayerGroup", ""), PluginSettings.UNREGISTERED_GROUP, reader, configData);
      performedChanges |= moveProperty(PropertyInitializer.newProperty("permission.EnablePermissionCheck", false), PluginSettings.ENABLE_PERMISSION_CHECK, reader, configData);
      return performedChanges;
   }

   private static boolean moveDeprecatedHashAlgorithmIntoLegacySection(PropertyReader reader, ConfigurationData configData) {
      HashAlgorithm currentHash = (HashAlgorithm)SecuritySettings.PASSWORD_HASH.determineValue(reader).getValue();
      if (currentHash != HashAlgorithm.CUSTOM && currentHash != HashAlgorithm.PLAINTEXT) {
         Class<?> encryptionClass = currentHash.getClazz();
         if (encryptionClass.isAnnotationPresent(Deprecated.class)) {
            configData.setValue(SecuritySettings.PASSWORD_HASH, HashAlgorithm.SHA256);
            Set<HashAlgorithm> legacyHashes = (Set)SecuritySettings.LEGACY_HASHES.determineValue(reader).getValue();
            legacyHashes.add(currentHash);
            configData.setValue(SecuritySettings.LEGACY_HASHES, legacyHashes);
            logger.warning("The hash algorithm '" + currentHash + "' is no longer supported for active use. New hashes will be in SHA256.");
            return true;
         }
      }

      return false;
   }

   private static boolean moveSaltColumnConfigWithOtherColumnConfigs(PropertyReader reader, ConfigurationData configData) {
      Property<String> oldProperty = PropertyInitializer.newProperty("ExternalBoardOptions.mySQLColumnSalt", (String)DatabaseSettings.MYSQL_COL_SALT.getDefaultValue());
      return moveProperty(oldProperty, DatabaseSettings.MYSQL_COL_SALT, reader, configData);
   }

   private void setOldOtherAccountsCommandFieldsIfSet(PropertyReader reader) {
      Property<String> commandProperty = PropertyInitializer.newProperty("settings.restrictions.otherAccountsCmd", "");
      Property<Integer> commandThresholdProperty = PropertyInitializer.newProperty("settings.restrictions.otherAccountsCmdThreshold", (int)0);
      PropertyValue<String> commandPropValue = commandProperty.determineValue(reader);
      int commandThreshold = (Integer)commandThresholdProperty.determineValue(reader).getValue();
      if (commandPropValue.isValidInResource() && commandThreshold >= 2) {
         this.oldOtherAccountsCommand = (String)commandPropValue.getValue();
         this.oldOtherAccountsCommandThreshold = commandThreshold;
      }

   }

   protected static <T> boolean moveProperty(Property<T> oldProperty, Property<T> newProperty, PropertyReader reader, ConfigurationData configData) {
      PropertyValue<T> oldPropertyValue = oldProperty.determineValue(reader);
      if (oldPropertyValue.isValidInResource()) {
         if (reader.contains(newProperty.getPath())) {
            logger.info("Detected deprecated property " + oldProperty.getPath());
         } else {
            logger.info("Renaming " + oldProperty.getPath() + " to " + newProperty.getPath());
            configData.setValue(newProperty, oldPropertyValue.getValue());
         }

         return true;
      } else {
         return false;
      }
   }
}
