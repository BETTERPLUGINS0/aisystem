package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.CacheDataSource;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.ch.jalu.injector.factory.SingletonStore;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PermissionNode;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class DataStatistics implements DebugSection {
   @Inject
   private PlayerCache playerCache;
   @Inject
   private LimboService limboService;
   @Inject
   private DataSource dataSource;
   @Inject
   private SingletonStore<Object> singletonStore;

   public String getName() {
      return "stats";
   }

   public String getDescription() {
      return "Outputs general data statistics";
   }

   public void execute(CommandSender sender, List<String> arguments) {
      sender.sendMessage(ChatColor.BLUE + "AuthMe statistics");
      sender.sendMessage("LimboPlayers in memory: " + DebugSectionUtils.applyToLimboPlayersMap(this.limboService, Map::size));
      sender.sendMessage("PlayerCache size: " + this.playerCache.getLogged() + " (= logged in players)");
      this.outputDatabaseStats(sender);
      this.outputInjectorStats(sender);
      sender.sendMessage("Total logger instances: " + ConsoleLoggerFactory.getTotalLoggers());
   }

   public PermissionNode getRequiredPermission() {
      return DebugSectionPermissions.DATA_STATISTICS;
   }

   private void outputDatabaseStats(CommandSender sender) {
      sender.sendMessage("Total players in DB: " + this.dataSource.getAccountsRegistered());
      if (this.dataSource instanceof CacheDataSource) {
         CacheDataSource cacheDataSource = (CacheDataSource)this.dataSource;
         sender.sendMessage("Cached PlayerAuth objects: " + cacheDataSource.getCachedAuths().size());
      }

   }

   private void outputInjectorStats(CommandSender sender) {
      sender.sendMessage("Singleton Java classes: " + this.singletonStore.retrieveAllOfType().size());
      sender.sendMessage(String.format("(Reloadable: %d / SettingsDependent: %d / HasCleanup: %d)", this.singletonStore.retrieveAllOfType(Reloadable.class).size(), this.singletonStore.retrieveAllOfType(SettingsDependent.class).size(), this.singletonStore.retrieveAllOfType(HasCleanup.class).size()));
   }
}
