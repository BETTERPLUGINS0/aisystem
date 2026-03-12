package fr.xephi.authme.settings.commandconfig;

import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.ch.jalu.configme.SettingsManager;
import fr.xephi.authme.libs.ch.jalu.configme.SettingsManagerBuilder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.GeoIpService;
import fr.xephi.authme.service.yaml.YamlFileResourceProvider;
import fr.xephi.authme.util.FileUtils;
import fr.xephi.authme.util.PlayerUtils;
import fr.xephi.authme.util.lazytags.Tag;
import fr.xephi.authme.util.lazytags.TagBuilder;
import fr.xephi.authme.util.lazytags.WrappedTagReplacer;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CommandManager implements Reloadable {
   private final File dataFolder;
   private final BukkitService bukkitService;
   private final GeoIpService geoIpService;
   private final CommandMigrationService commandMigrationService;
   private final List<Tag<Player>> availableTags = this.buildAvailableTags();
   private WrappedTagReplacer<Command, Player> onJoinCommands;
   private WrappedTagReplacer<OnLoginCommand, Player> onLoginCommands;
   private WrappedTagReplacer<Command, Player> onSessionLoginCommands;
   private WrappedTagReplacer<OnLoginCommand, Player> onFirstLoginCommands;
   private WrappedTagReplacer<Command, Player> onRegisterCommands;
   private WrappedTagReplacer<Command, Player> onUnregisterCommands;
   private WrappedTagReplacer<Command, Player> onLogoutCommands;

   @Inject
   CommandManager(@DataFolder File dataFolder, BukkitService bukkitService, GeoIpService geoIpService, CommandMigrationService commandMigrationService) {
      this.dataFolder = dataFolder;
      this.bukkitService = bukkitService;
      this.geoIpService = geoIpService;
      this.commandMigrationService = commandMigrationService;
      this.reload();
   }

   public void runCommandsOnJoin(Player player) {
      this.executeCommands(player, this.onJoinCommands.getAdaptedItems(player));
   }

   public void runCommandsOnRegister(Player player) {
      this.executeCommands(player, this.onRegisterCommands.getAdaptedItems(player));
   }

   public void runCommandsOnLogin(Player player, List<String> otherAccounts) {
      int numberOfOtherAccounts = otherAccounts.size();
      this.executeCommands(player, this.onLoginCommands.getAdaptedItems(player), (cmd) -> {
         return shouldCommandBeRun(cmd, numberOfOtherAccounts);
      });
   }

   public void runCommandsOnSessionLogin(Player player) {
      this.executeCommands(player, this.onSessionLoginCommands.getAdaptedItems(player));
   }

   public void runCommandsOnFirstLogin(Player player, List<String> otherAccounts) {
      int numberOfOtherAccounts = otherAccounts.size();
      this.executeCommands(player, this.onFirstLoginCommands.getAdaptedItems(player), (cmd) -> {
         return shouldCommandBeRun(cmd, numberOfOtherAccounts);
      });
   }

   public void runCommandsOnUnregister(Player player) {
      this.executeCommands(player, this.onUnregisterCommands.getAdaptedItems(player));
   }

   public void runCommandsOnLogout(Player player) {
      this.executeCommands(player, this.onLogoutCommands.getAdaptedItems(player));
   }

   private void executeCommands(Player player, List<Command> commands) {
      this.executeCommands(player, commands, (c) -> {
         return true;
      });
   }

   private <T extends Command> void executeCommands(Player player, List<T> commands, Predicate<T> predicate) {
      Iterator var4 = commands.iterator();

      while(var4.hasNext()) {
         T cmd = (Command)var4.next();
         if (predicate.test(cmd)) {
            long delay = cmd.getDelay();
            if (delay > 0L) {
               this.bukkitService.runTaskLater(player, () -> {
                  this.dispatchCommand(player, cmd);
               }, delay);
            } else {
               this.bukkitService.runTaskIfFolia((Entity)player, () -> {
                  this.dispatchCommand(player, cmd);
               });
            }
         }
      }

   }

   private void dispatchCommand(Player player, Command command) {
      if (Executor.CONSOLE.equals(command.getExecutor())) {
         this.bukkitService.dispatchConsoleCommand(command.getCommand());
      } else {
         this.bukkitService.dispatchCommand(player, command.getCommand());
      }

   }

   private static boolean shouldCommandBeRun(OnLoginCommand command, int numberOfOtherAccounts) {
      return (!command.getIfNumberOfAccountsAtLeast().isPresent() || (Integer)command.getIfNumberOfAccountsAtLeast().get() <= numberOfOtherAccounts) && (!command.getIfNumberOfAccountsLessThan().isPresent() || (Integer)command.getIfNumberOfAccountsLessThan().get() > numberOfOtherAccounts);
   }

   public void reload() {
      File file = new File(this.dataFolder, "commands.yml");
      FileUtils.copyFileFromResource(file, "commands.yml");
      SettingsManager settingsManager = SettingsManagerBuilder.withResource(YamlFileResourceProvider.loadFromFile(file)).configurationData(CommandSettingsHolder.class).migrationService(this.commandMigrationService).create();
      CommandConfig commandConfig = (CommandConfig)settingsManager.getProperty(CommandSettingsHolder.COMMANDS);
      this.onJoinCommands = this.newReplacer(commandConfig.getOnJoin());
      this.onLoginCommands = this.newOnLoginCmdReplacer(commandConfig.getOnLogin());
      this.onFirstLoginCommands = this.newOnLoginCmdReplacer(commandConfig.getOnFirstLogin());
      this.onSessionLoginCommands = this.newReplacer(commandConfig.getOnSessionLogin());
      this.onRegisterCommands = this.newReplacer(commandConfig.getOnRegister());
      this.onUnregisterCommands = this.newReplacer(commandConfig.getOnUnregister());
      this.onLogoutCommands = this.newReplacer(commandConfig.getOnLogout());
   }

   private WrappedTagReplacer<Command, Player> newReplacer(Map<String, Command> commands) {
      return new WrappedTagReplacer(this.availableTags, commands.values(), Command::getCommand, Command::copyWithCommand);
   }

   private WrappedTagReplacer<OnLoginCommand, Player> newOnLoginCmdReplacer(Map<String, OnLoginCommand> commands) {
      return new WrappedTagReplacer(this.availableTags, commands.values(), Command::getCommand, OnLoginCommand::copyWithCommand);
   }

   private List<Tag<Player>> buildAvailableTags() {
      return Arrays.asList(TagBuilder.createTag("%p", Player::getName), TagBuilder.createTag("%nick", Player::getDisplayName), TagBuilder.createTag("%ip", PlayerUtils::getPlayerIp), TagBuilder.createTag("%country", (pl) -> {
         return this.geoIpService.getCountryName(PlayerUtils.getPlayerIp(pl));
      }));
   }
}
