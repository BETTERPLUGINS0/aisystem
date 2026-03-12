package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.DataSourceType;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.ch.jalu.injector.factory.SingletonStore;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SettingsWarner;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.util.Utils;
import java.util.List;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements ExecutableCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(ReloadCommand.class);
   @Inject
   private AuthMe plugin;
   @Inject
   private Settings settings;
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService commonService;
   @Inject
   private SettingsWarner settingsWarner;
   @Inject
   private SingletonStore<Reloadable> reloadableStore;
   @Inject
   private SingletonStore<SettingsDependent> settingsDependentStore;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      try {
         this.settings.reload();
         ConsoleLoggerFactory.reloadSettings(this.settings);
         this.settingsWarner.logWarningsForMisconfigurations();
         if (!((DataSourceType)this.settings.getProperty(DatabaseSettings.BACKEND)).equals(this.dataSource.getType())) {
            Utils.logAndSendMessage(sender, "Note: cannot change database type during /authme reload");
         }

         this.performReloadOnServices();
         this.commonService.send(sender, MessageKey.CONFIG_RELOAD_SUCCESS);
      } catch (Exception var4) {
         sender.sendMessage("Error occurred during reload of AuthMe: aborting");
         this.logger.logException("Aborting! Encountered exception during reload of AuthMe:", var4);
         this.plugin.stopOrUnload();
      }

   }

   private void performReloadOnServices() {
      this.reloadableStore.retrieveAllOfType().forEach((r) -> {
         r.reload();
      });
      this.settingsDependentStore.retrieveAllOfType().forEach((s) -> {
         s.reload(this.settings);
      });
   }
}
