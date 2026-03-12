package fr.xephi.authme.settings.commandconfig;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.migration.MigrationService;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.SettingsMigrationService;
import fr.xephi.authme.util.RandomStringUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class CommandMigrationService implements MigrationService {
   @VisibleForTesting
   static final List<String> COMMAND_CONFIG_PROPERTIES = ImmutableList.of("onJoin", "onLogin", "onSessionLogin", "onFirstLogin", "onRegister", "onUnregister", "onLogout");
   @Inject
   private SettingsMigrationService settingsMigrationService;

   public boolean checkAndMigrate(PropertyReader reader, ConfigurationData configurationData) {
      CommandConfig commandConfig = (CommandConfig)CommandSettingsHolder.COMMANDS.determineValue(reader).getValue();
      if (!this.moveOtherAccountsConfig(commandConfig) && !isAnyCommandMissing(reader)) {
         return false;
      } else {
         configurationData.setValue(CommandSettingsHolder.COMMANDS, commandConfig);
         return true;
      }
   }

   private boolean moveOtherAccountsConfig(CommandConfig commandConfig) {
      if (this.settingsMigrationService.hasOldOtherAccountsCommand()) {
         OnLoginCommand command = new OnLoginCommand();
         command.setCommand(replaceOldPlaceholdersWithNew(this.settingsMigrationService.getOldOtherAccountsCommand()));
         command.setExecutor(Executor.CONSOLE);
         command.setIfNumberOfAccountsAtLeast(Optional.of(this.settingsMigrationService.getOldOtherAccountsCommandThreshold()));
         Map<String, OnLoginCommand> onLoginCommands = commandConfig.getOnLogin();
         onLoginCommands.put(RandomStringUtils.generate(10), command);
         return true;
      } else {
         return false;
      }
   }

   private static String replaceOldPlaceholdersWithNew(String oldOtherAccountsCommand) {
      return oldOtherAccountsCommand.replace("%playername%", "%p").replace("%playerip%", "%ip");
   }

   private static boolean isAnyCommandMissing(PropertyReader reader) {
      return COMMAND_CONFIG_PROPERTIES.stream().anyMatch((property) -> {
         return reader.getObject(property) == null;
      });
   }
}
