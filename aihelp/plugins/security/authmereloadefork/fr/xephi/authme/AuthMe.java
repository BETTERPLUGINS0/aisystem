package fr.xephi.authme;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.command.CommandHandler;
import fr.xephi.authme.command.TabCompleteHandler;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.initialization.DataSourceProvider;
import fr.xephi.authme.initialization.OnShutdownPlayerSaver;
import fr.xephi.authme.initialization.OnStartupTasks;
import fr.xephi.authme.initialization.SettingsProvider;
import fr.xephi.authme.initialization.TaskCloser;
import fr.xephi.authme.libs.ch.jalu.injector.Injector;
import fr.xephi.authme.libs.ch.jalu.injector.InjectorBuilder;
import fr.xephi.authme.libs.com.alessiodp.libby.BukkitLibraryManager;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.UniversalScheduler;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.listener.AdvancedShulkerFixListener;
import fr.xephi.authme.listener.BedrockAutoLoginListener;
import fr.xephi.authme.listener.BlockListener;
import fr.xephi.authme.listener.DoubleLoginFixListener;
import fr.xephi.authme.listener.EntityListener;
import fr.xephi.authme.listener.LoginLocationFixListener;
import fr.xephi.authme.listener.PlayerListener;
import fr.xephi.authme.listener.PlayerListener111;
import fr.xephi.authme.listener.PlayerListener19;
import fr.xephi.authme.listener.PlayerListener19Spigot;
import fr.xephi.authme.listener.PlayerListenerHigherThan18;
import fr.xephi.authme.listener.PurgeListener;
import fr.xephi.authme.listener.ServerListener;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.crypts.Sha256;
import fr.xephi.authme.service.BackupService;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.MigrationService;
import fr.xephi.authme.service.bungeecord.BungeeReceiver;
import fr.xephi.authme.service.velocity.VelocityReceiver;
import fr.xephi.authme.service.yaml.YamlParseException;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SettingsWarner;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.task.CleanupTask;
import fr.xephi.authme.task.Updater;
import fr.xephi.authme.task.purge.PurgeService;
import fr.xephi.authme.util.ExceptionUtils;
import fr.xephi.authme.util.Utils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AuthMe extends JavaPlugin {
   private static final String PLUGIN_NAME = "AuthMeReloaded";
   private static final String LOG_FILENAME = "authme.log";
   private static final int CLEANUP_INTERVAL = 6000;
   private static String pluginVersion = "5.7.0-Fork";
   private static final String pluginBuild = "b";
   private static String pluginBuildNumber = "53";
   private EmailService emailService;
   private CommandHandler commandHandler;
   private static TaskScheduler scheduler;
   @Inject
   public static Settings settings;
   private DataSource database;
   private BukkitService bukkitService;
   private Injector injector;
   private BackupService backupService;
   public static ConsoleLogger logger;
   public static BukkitLibraryManager libraryManager;

   public static String getPluginBuild() {
      return "b";
   }

   public static String getPluginName() {
      return "AuthMeReloaded";
   }

   public static String getPluginVersion() {
      return pluginVersion;
   }

   public static String getPluginBuildNumber() {
      return pluginBuildNumber;
   }

   public static TaskScheduler getScheduler() {
      return scheduler;
   }

   public void onEnable() {
      loadPluginInfo(this.getDescription().getVersion());
      scheduler = UniversalScheduler.getScheduler(this);
      libraryManager = new BukkitLibraryManager(this);
      ConsoleLogger.initialize(this.getLogger(), new File(this.getDataFolder(), "authme.log"));
      logger = ConsoleLoggerFactory.get(AuthMe.class);
      logger.info("You are running an unofficial fork version of AuthMe!");
      if (Utils.isClassLoaded("org.spigotmc.event.player.PlayerSpawnLocationEvent") && Utils.isClassLoaded("org.bukkit.event.player.PlayerInteractAtEntityEvent")) {
         if (this.getServer().getPluginManager().isPluginEnabled("AuthMeBridge")) {
            logger.warning("Detected AuthMeBridge, support for it has been dropped as it was causing exploit issues, please use AuthMeBungee instead! Aborting!");
            this.stopOrUnload();
         } else {
            try {
               this.initialize();
            } catch (Throwable var4) {
               YamlParseException yamlParseException = (YamlParseException)ExceptionUtils.findThrowableInCause(YamlParseException.class, var4);
               if (yamlParseException == null) {
                  logger.logException("Aborting initialization of AuthMe:", var4);
                  var4.printStackTrace();
               } else {
                  logger.logException("File '" + yamlParseException.getFile() + "' contains invalid YAML. Please run its contents through http://yamllint.com", yamlParseException);
               }

               this.stopOrUnload();
               return;
            }

            ((SettingsWarner)this.injector.getSingleton(SettingsWarner.class)).logWarningsForMisconfigurations();
            CleanupTask cleanupTask = (CleanupTask)this.injector.getSingleton(CleanupTask.class);
            cleanupTask.runTaskTimerAsynchronously(this, 6000L, 6000L);
            this.backupService.doBackup(BackupService.BackupCause.START);
            OnStartupTasks.sendMetrics(this, settings);
            if ((Boolean)settings.getProperty(SecuritySettings.SHOW_STARTUP_BANNER)) {
               logger.info("\n    ___         __  __    __  ___   \n   /   | __  __/ /_/ /_  /  |/  /__ \n  / /| |/ / / / __/ __ \\/ /|_/ / _ \\\n / ___ / /_/ / /_/ / / / /  / /  __/\n/_/  |_\\__,_/\\__/_/ /_/_/  /_/\\___/ \n                                    ");
            }

            this.checkServerType();

            try {
               ((PluginCommand)Objects.requireNonNull(this.getCommand("register"))).setTabCompleter(new TabCompleteHandler());
               ((PluginCommand)Objects.requireNonNull(this.getCommand("login"))).setTabCompleter(new TabCompleteHandler());
            } catch (NullPointerException var3) {
            }

            logger.info("AuthMeReReloaded is enabled successfully!");
            PurgeService purgeService = (PurgeService)this.injector.getSingleton(PurgeService.class);
            purgeService.runAutoPurge();
            logger.info("GitHub: https://github.com/HaHaWTH/AuthMeReReloaded/");
            if ((Boolean)settings.getProperty(SecuritySettings.CHECK_FOR_UPDATES)) {
               this.checkForUpdates();
            }

         }
      } else {
         logger.warning("You are running an unsupported server version (" + this.getServerNameVersionSafe() + "). AuthMe requires Spigot 1.8.X or later!");
         this.stopOrUnload();
      }
   }

   private static void loadPluginInfo(String versionRaw) {
      int index = versionRaw.lastIndexOf("-");
      if (index != -1) {
         pluginVersion = versionRaw.substring(0, index);
         pluginBuildNumber = versionRaw.substring(index + 1);
         if (pluginBuildNumber.startsWith("b")) {
            pluginBuildNumber = pluginBuildNumber.substring(1);
         }
      }

   }

   private void initialize() {
      this.getDataFolder().mkdir();
      this.injector = (new InjectorBuilder()).addDefaultHandlers("fr.xephi.authme").create();
      this.injector.register(AuthMe.class, this);
      this.injector.register(Server.class, this.getServer());
      this.injector.register(PluginManager.class, this.getServer().getPluginManager());
      this.injector.provide(DataFolder.class, this.getDataFolder());
      this.injector.registerProvider(Settings.class, SettingsProvider.class);
      this.injector.registerProvider(DataSource.class, DataSourceProvider.class);
      settings = (Settings)this.injector.getSingleton(Settings.class);
      ConsoleLoggerFactory.reloadSettings(settings);
      OnStartupTasks.setupConsoleFilter(this.getLogger());
      this.instantiateServices(this.injector);
      MigrationService.changePlainTextToSha256(settings, this.database, new Sha256());
      if (this.bukkitService.getOnlinePlayers().isEmpty()) {
         this.database.purgeLogged();
      }

      this.registerEventListeners(this.injector);
      OnStartupTasks onStartupTasks = (OnStartupTasks)this.injector.newInstance(OnStartupTasks.class);
      onStartupTasks.scheduleRecallEmailTask();
   }

   void instantiateServices(Injector injector) {
      this.database = (DataSource)injector.getSingleton(DataSource.class);
      this.bukkitService = (BukkitService)injector.getSingleton(BukkitService.class);
      this.commandHandler = (CommandHandler)injector.getSingleton(CommandHandler.class);
      this.emailService = (EmailService)injector.getSingleton(EmailService.class);
      this.backupService = (BackupService)injector.getSingleton(BackupService.class);
      injector.getSingleton(BungeeReceiver.class);
      injector.getSingleton(VelocityReceiver.class);
      injector.getSingleton(AuthMeApi.class);
   }

   void registerEventListeners(Injector injector) {
      PluginManager pluginManager = this.getServer().getPluginManager();
      pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener.class), this);
      pluginManager.registerEvents((Listener)injector.getSingleton(BlockListener.class), this);
      pluginManager.registerEvents((Listener)injector.getSingleton(EntityListener.class), this);
      pluginManager.registerEvents((Listener)injector.getSingleton(ServerListener.class), this);
      if (Utils.isClassLoaded("org.bukkit.event.entity.EntityPickupItemEvent") && Utils.isClassLoaded("org.bukkit.event.player.PlayerSwapHandItemsEvent")) {
         pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListenerHigherThan18.class), this);
      } else if (Utils.isClassLoaded("org.bukkit.event.player.PlayerSwapHandItemsEvent")) {
         pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener19.class), this);
      }

      if (Utils.isClassLoaded("org.spigotmc.event.player.PlayerSpawnLocationEvent")) {
         pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener19Spigot.class), this);
      }

      if (Utils.isClassLoaded("org.bukkit.event.entity.EntityAirChangeEvent")) {
         pluginManager.registerEvents((Listener)injector.getSingleton(PlayerListener111.class), this);
      }

      if ((Boolean)settings.getProperty(SecuritySettings.FORCE_LOGIN_BEDROCK) && (Boolean)settings.getProperty(HooksSettings.HOOK_FLOODGATE_PLAYER) && this.getServer().getPluginManager().getPlugin("floodgate") != null) {
         pluginManager.registerEvents((Listener)injector.getSingleton(BedrockAutoLoginListener.class), this);
      } else if ((Boolean)settings.getProperty(SecuritySettings.FORCE_LOGIN_BEDROCK) && (!(Boolean)settings.getProperty(HooksSettings.HOOK_FLOODGATE_PLAYER) || this.getServer().getPluginManager().getPlugin("floodgate") == null)) {
         logger.warning("Failed to enable BedrockAutoLogin, ensure hookFloodgate: true and floodgate is loaded.");
      }

      if ((Boolean)settings.getProperty(SecuritySettings.LOGIN_LOC_FIX_SUB_UNDERGROUND) || (Boolean)settings.getProperty(SecuritySettings.LOGIN_LOC_FIX_SUB_PORTAL)) {
         pluginManager.registerEvents((Listener)injector.getSingleton(LoginLocationFixListener.class), this);
      }

      if ((Boolean)settings.getProperty(SecuritySettings.ANTI_GHOST_PLAYERS)) {
         pluginManager.registerEvents((Listener)injector.getSingleton(DoubleLoginFixListener.class), this);
      }

      if ((Boolean)settings.getProperty(SecuritySettings.ADVANCED_SHULKER_FIX) && !Utils.isClassLoaded("org.bukkit.event.player.PlayerCommandSendEvent")) {
         pluginManager.registerEvents((Listener)injector.getSingleton(AdvancedShulkerFixListener.class), this);
      } else if ((Boolean)settings.getProperty(SecuritySettings.ADVANCED_SHULKER_FIX) && Utils.isClassLoaded("org.bukkit.event.player.PlayerCommandSendEvent")) {
         logger.warning("You are running an 1.13+ minecraft server, AdvancedShulkerFix won't enable.");
      }

      if ((Boolean)settings.getProperty(SecuritySettings.PURGE_DATA_ON_QUIT)) {
         pluginManager.registerEvents((Listener)injector.getSingleton(PurgeListener.class), this);
      }

   }

   public void stopOrUnload() {
      if (settings != null && !(Boolean)settings.getProperty(SecuritySettings.STOP_SERVER_ON_PROBLEM)) {
         this.setEnabled(false);
      } else {
         this.getLogger().warning("THE SERVER IS GOING TO SHUT DOWN AS DEFINED IN THE CONFIGURATION!");
         this.setEnabled(false);
         this.getServer().shutdown();
      }

   }

   public void onDisable() {
      OnShutdownPlayerSaver onShutdownPlayerSaver = this.injector == null ? null : (OnShutdownPlayerSaver)this.injector.createIfHasDependencies(OnShutdownPlayerSaver.class);
      if (onShutdownPlayerSaver != null) {
         onShutdownPlayerSaver.saveAllPlayers();
      }

      if (settings != null && (Boolean)settings.getProperty(EmailSettings.SHUTDOWN_MAIL)) {
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'.'MM'.'dd'.' HH:mm:ss");
         Date date = new Date(System.currentTimeMillis());
         this.emailService.sendShutDown((String)settings.getProperty(EmailSettings.SHUTDOWN_MAIL_ADDRESS), dateFormat.format(date));
      }

      if (this.backupService != null) {
         this.backupService.doBackup(BackupService.BackupCause.STOP);
      }

      (new TaskCloser(this.database)).run();
      Consumer var4;
      if (logger == null) {
         Logger var10000 = this.getLogger();
         Objects.requireNonNull(var10000);
         var4 = var10000::info;
      } else {
         ConsoleLogger var5 = logger;
         Objects.requireNonNull(var5);
         var4 = var5::info;
      }

      Consumer<String> infoLogMethod = var4;
      infoLogMethod.accept("AuthMe " + this.getDescription().getVersion() + " is unloaded successfully!");
      ConsoleLogger.closeFileWriter();
   }

   private void checkForUpdates() {
      logger.info("Checking for updates...");
      Updater updater = new Updater("b" + pluginBuildNumber);
      this.bukkitService.runTaskAsynchronously(() -> {
         if (updater.isUpdateAvailable()) {
            String message = "New version available! Latest:" + updater.getLatestVersion() + " Current:" + "b" + pluginBuildNumber;
            logger.warning(message);
            logger.warning("Download from here: https://modrinth.com/plugin/authmerereloaded");
         } else {
            logger.info("You are running the latest version.");
         }

      });
   }

   private void checkServerType() {
      if (Utils.isClassLoaded("io.papermc.paper.threadedregions.RegionizedServer")) {
         logger.info("AuthMeReReloaded is running on Folia");
      } else if (Utils.isClassLoaded("com.destroystokyo.paper.PaperConfig")) {
         logger.info("AuthMeReReloaded is running on Paper");
      } else if (Utils.isClassLoaded("catserver.server.CatServerConfig")) {
         logger.info("AuthMeReReloaded is running on CatServer");
      } else if (Utils.isClassLoaded("org.spigotmc.SpigotConfig")) {
         logger.info("AuthMeReReloaded is running on Spigot");
      } else if (Utils.isClassLoaded("org.bukkit.craftbukkit.CraftServer")) {
         logger.info("AuthMeReReloaded is running on Bukkit");
      } else {
         logger.info("AuthMeReReloaded is running on Unknown*");
      }

   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
      if (this.commandHandler == null) {
         this.getLogger().severe("AuthMe command handler is not available");
         return false;
      } else {
         return this.commandHandler.processCommand(sender, commandLabel, args);
      }
   }

   private String getServerNameVersionSafe() {
      try {
         Server server = this.getServer();
         return server.getName() + " v. " + server.getVersion();
      } catch (Throwable var2) {
         return "-";
      }
   }
}
