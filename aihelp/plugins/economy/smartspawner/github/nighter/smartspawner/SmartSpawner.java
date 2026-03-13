package github.nighter.smartspawner;

import github.nighter.smartspawner.api.SmartSpawnerAPI;
import github.nighter.smartspawner.api.SmartSpawnerAPIImpl;
import github.nighter.smartspawner.api.SmartSpawnerPlugin;
import github.nighter.smartspawner.bstats.Metrics;
import github.nighter.smartspawner.commands.BrigadierCommandManager;
import github.nighter.smartspawner.commands.list.ListSubCommand;
import github.nighter.smartspawner.commands.list.gui.adminstacker.AdminStackerHandler;
import github.nighter.smartspawner.commands.list.gui.list.SpawnerListGUI;
import github.nighter.smartspawner.commands.list.gui.list.UserPreferenceCache;
import github.nighter.smartspawner.commands.list.gui.management.SpawnerManagementGUI;
import github.nighter.smartspawner.commands.list.gui.management.SpawnerManagementHandler;
import github.nighter.smartspawner.commands.list.gui.serverselection.ServerSelectionHandler;
import github.nighter.smartspawner.commands.prices.PricesGUI;
import github.nighter.smartspawner.extras.HopperConfig;
import github.nighter.smartspawner.extras.HopperService;
import github.nighter.smartspawner.hooks.IntegrationManager;
import github.nighter.smartspawner.hooks.economy.ItemPriceManager;
import github.nighter.smartspawner.hooks.economy.shops.providers.shopguiplus.SpawnerProvider;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.logging.LoggingConfig;
import github.nighter.smartspawner.logging.SpawnerActionLogger;
import github.nighter.smartspawner.logging.SpawnerAuditListener;
import github.nighter.smartspawner.migration.SpawnerDataMigration;
import github.nighter.smartspawner.nms.VersionInitializer;
import github.nighter.smartspawner.spawner.config.ItemSpawnerSettingsConfig;
import github.nighter.smartspawner.spawner.config.SpawnerMobHeadTexture;
import github.nighter.smartspawner.spawner.config.SpawnerSettingsConfig;
import github.nighter.smartspawner.spawner.data.SpawnerFileHandler;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.data.WorldEventHandler;
import github.nighter.smartspawner.spawner.data.database.DatabaseManager;
import github.nighter.smartspawner.spawner.data.database.SpawnerDatabaseHandler;
import github.nighter.smartspawner.spawner.data.database.SqliteToMySqlMigration;
import github.nighter.smartspawner.spawner.data.database.YamlToDatabaseMigration;
import github.nighter.smartspawner.spawner.data.storage.SpawnerStorage;
import github.nighter.smartspawner.spawner.data.storage.StorageMode;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayoutConfig;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuAction;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuFormUI;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuUI;
import github.nighter.smartspawner.spawner.gui.sell.SpawnerSellConfirmListener;
import github.nighter.smartspawner.spawner.gui.sell.SpawnerSellConfirmUI;
import github.nighter.smartspawner.spawner.gui.stacker.SpawnerStackerHandler;
import github.nighter.smartspawner.spawner.gui.stacker.SpawnerStackerUI;
import github.nighter.smartspawner.spawner.gui.storage.SpawnerStorageAction;
import github.nighter.smartspawner.spawner.gui.storage.SpawnerStorageUI;
import github.nighter.smartspawner.spawner.gui.storage.filter.FilterConfigUI;
import github.nighter.smartspawner.spawner.gui.synchronization.SpawnerGuiViewManager;
import github.nighter.smartspawner.spawner.interactions.click.SpawnerClickManager;
import github.nighter.smartspawner.spawner.interactions.destroy.SpawnerBreakListener;
import github.nighter.smartspawner.spawner.interactions.destroy.SpawnerExplosionListener;
import github.nighter.smartspawner.spawner.interactions.place.SpawnerPlaceListener;
import github.nighter.smartspawner.spawner.interactions.stack.SpawnerStackHandler;
import github.nighter.smartspawner.spawner.interactions.type.SpawnEggHandler;
import github.nighter.smartspawner.spawner.item.SpawnerItemFactory;
import github.nighter.smartspawner.spawner.lootgen.SpawnerLootGenerator;
import github.nighter.smartspawner.spawner.lootgen.SpawnerRangeChecker;
import github.nighter.smartspawner.spawner.natural.NaturalSpawnerListener;
import github.nighter.smartspawner.spawner.sell.SpawnerSellManager;
import github.nighter.smartspawner.spawner.utils.SpawnerLocationLockManager;
import github.nighter.smartspawner.spawner.utils.SpawnerTypeChecker;
import github.nighter.smartspawner.updates.ConfigUpdater;
import github.nighter.smartspawner.updates.LanguageUpdater;
import github.nighter.smartspawner.updates.UpdateChecker;
import github.nighter.smartspawner.utils.TimeFormatter;
import java.util.Objects;
import java.util.logging.Level;
import lombok.Generated;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SmartSpawner extends JavaPlugin implements SmartSpawnerPlugin {
   private static SmartSpawner instance;
   public final int DATA_VERSION = 3;
   private final boolean debugMode = this.getConfig().getBoolean("debug", false);
   private IntegrationManager integrationManager;
   private TimeFormatter timeFormatter;
   private ConfigUpdater configUpdater;
   private LanguageManager languageManager;
   private LanguageUpdater languageUpdater;
   private MessageService messageService;
   private SpawnerSettingsConfig spawnerSettingsConfig;
   private ItemSpawnerSettingsConfig itemSpawnerSettingsConfig;
   private SpawnerItemFactory spawnerItemFactory;
   private GuiLayoutConfig guiLayoutConfig;
   private SpawnerMenuUI spawnerMenuUI;
   private SpawnerMenuFormUI spawnerMenuFormUI;
   private SpawnerStorageUI spawnerStorageUI;
   private FilterConfigUI filterConfigUI;
   private SpawnerStackerUI spawnerStackerUI;
   private SpawnerSellConfirmUI spawnerSellConfirmUI;
   private SpawnEggHandler spawnEggHandler;
   private SpawnerClickManager spawnerClickManager;
   private SpawnerStackHandler spawnerStackHandler;
   private SpawnerMenuAction spawnerMenuAction;
   private SpawnerStackerHandler spawnerStackerHandler;
   private SpawnerStorageAction spawnerStorageAction;
   private SpawnerSellManager spawnerSellManager;
   private SpawnerSellConfirmListener spawnerSellConfirmListener;
   private SpawnerFileHandler spawnerFileHandler;
   private SpawnerStorage spawnerStorage;
   private DatabaseManager databaseManager;
   private SpawnerManager spawnerManager;
   private HopperService hopperService;
   private HopperConfig hopperConfig;
   private SpawnerLocationLockManager spawnerLocationLockManager;
   private NaturalSpawnerListener naturalSpawnerListener;
   private SpawnerLootGenerator spawnerLootGenerator;
   private SpawnerRangeChecker rangeChecker;
   private SpawnerGuiViewManager spawnerGuiViewManager;
   private SpawnerExplosionListener spawnerExplosionListener;
   private SpawnerBreakListener spawnerBreakListener;
   private SpawnerPlaceListener spawnerPlaceListener;
   private WorldEventHandler worldEventHandler;
   private ItemPriceManager itemPriceManager;
   private UpdateChecker updateChecker;
   private BrigadierCommandManager brigadierCommandManager;
   private ListSubCommand listSubCommand;
   private UserPreferenceCache userPreferenceCache;
   private SpawnerListGUI spawnerListGUI;
   private SpawnerManagementHandler spawnerManagementHandler;
   private AdminStackerHandler adminStackerHandler;
   private ServerSelectionHandler serverSelectionHandler;
   private PricesGUI pricesGUI;
   private SpawnerActionLogger spawnerActionLogger;
   private SpawnerAuditListener spawnerAuditListener;
   private LoggingConfig loggingConfig;
   private SmartSpawnerAPIImpl apiImpl;

   public void onEnable() {
      long startTime = System.currentTimeMillis();
      instance = this;
      this.initializeVersionComponents();
      this.integrationManager = new IntegrationManager(this);
      this.integrationManager.initializeIntegrations();
      this.migrateDataIfNeeded();
      this.initializeComponents();
      this.setupCommand();
      this.setupBtatsMetrics();
      this.registerListeners();
      if (this.worldEventHandler != null) {
         this.worldEventHandler.attemptInitialSpawnerLoad();
      }

      long loadTime = System.currentTimeMillis() - startTime;
      this.getLogger().info("SmartSpawner has been enabled! (Loaded in " + loadTime + "ms)");
   }

   public SmartSpawnerAPI getAPI() {
      return this.apiImpl;
   }

   private void initializeVersionComponents() {
      try {
         (new VersionInitializer(this)).initialize();
      } catch (Exception var2) {
         this.getLogger().log(Level.SEVERE, "Failed to initialize version-specific components", var2);
         this.getServer().getPluginManager().disablePlugin(this);
      }

   }

   private void migrateDataIfNeeded() {
      SpawnerDataMigration migration = new SpawnerDataMigration(this);
      if (migration.checkAndMigrateData()) {
         this.getLogger().info("Data migration completed. Loading with new format...");
      }

   }

   private void initializeComponents() {
      this.initializeServices();
      this.initializeEconomyComponents();
      this.initializeCoreComponents();
      this.initializeHandlers();
      this.initializeUIAndActions();
      this.setUpHopperHandler();
      this.initializeListeners();
      this.apiImpl = new SmartSpawnerAPIImpl(this);
      this.updateChecker = new UpdateChecker(this);
   }

   private void initializeServices() {
      SpawnerTypeChecker.init(this);
      this.timeFormatter = new TimeFormatter(this);
      this.configUpdater = new ConfigUpdater(this);
      this.configUpdater.checkAndUpdateConfig();
      this.languageManager = new LanguageManager(this);
      this.languageUpdater = new LanguageUpdater(this);
      this.messageService = new MessageService(this, this.languageManager);
      this.spawnerSettingsConfig = new SpawnerSettingsConfig(this);
      this.itemSpawnerSettingsConfig = new ItemSpawnerSettingsConfig(this);
      this.loggingConfig = new LoggingConfig(this);
      this.spawnerActionLogger = new SpawnerActionLogger(this, this.loggingConfig);
      this.spawnerAuditListener = new SpawnerAuditListener(this, this.spawnerActionLogger);
   }

   private void initializeEconomyComponents() {
      this.itemPriceManager = new ItemPriceManager(this);
      this.itemPriceManager.init();
      if (this.spawnerSettingsConfig != null) {
         this.spawnerSettingsConfig.load();
      }

      if (this.itemSpawnerSettingsConfig != null) {
         this.itemSpawnerSettingsConfig.load();
      }

      SpawnerMobHeadTexture.prewarmCache();
      this.spawnerItemFactory = new SpawnerItemFactory(this);
   }

   private void initializeCoreComponents() {
      this.initializeStorage();
      this.spawnerManager = new SpawnerManager(this);
      this.spawnerLocationLockManager = new SpawnerLocationLockManager(this);
      this.spawnerManager.reloadAllHolograms();
      this.guiLayoutConfig = new GuiLayoutConfig(this);
      this.spawnerStorageUI = new SpawnerStorageUI(this);
      this.filterConfigUI = new FilterConfigUI(this);
      this.spawnerMenuUI = new SpawnerMenuUI(this);
      this.spawnerSellConfirmUI = new SpawnerSellConfirmUI(this);
      this.spawnerGuiViewManager = new SpawnerGuiViewManager(this);
      this.spawnerLootGenerator = new SpawnerLootGenerator(this);
      this.spawnerSellManager = new SpawnerSellManager(this);
      this.rangeChecker = new SpawnerRangeChecker(this);
      this.initializeFormUIComponents();
   }

   private void initializeStorage() {
      String modeStr = this.getConfig().getString("database.mode", "YAML").toUpperCase();

      StorageMode mode;
      try {
         mode = StorageMode.valueOf(modeStr);
      } catch (IllegalArgumentException var8) {
         this.getLogger().warning("Invalid storage mode '" + modeStr + "', defaulting to YAML");
         mode = StorageMode.YAML;
      }

      if (mode != StorageMode.MYSQL && mode != StorageMode.SQLITE) {
         this.initializeYamlStorage();
      } else {
         String dbType = mode == StorageMode.MYSQL ? "MySQL/MariaDB" : "SQLite";
         this.getLogger().info("Initializing " + dbType + " database storage mode...");
         this.databaseManager = new DatabaseManager(this, mode);
         if (this.databaseManager.initialize()) {
            SpawnerDatabaseHandler dbHandler = new SpawnerDatabaseHandler(this, this.databaseManager);
            if (dbHandler.initialize()) {
               this.spawnerStorage = dbHandler;
               boolean migrateFromLocal = this.getConfig().getBoolean("database.migrate_from_local", true);
               if (migrateFromLocal) {
                  YamlToDatabaseMigration yamlMigration = new YamlToDatabaseMigration(this, this.databaseManager);
                  if (yamlMigration.needsMigration()) {
                     this.getLogger().info("YAML data detected, starting migration to " + dbType + "...");
                     if (yamlMigration.migrate()) {
                        this.getLogger().info("YAML migration completed successfully!");
                     } else {
                        this.getLogger().warning("YAML migration completed with some errors. Check logs for details.");
                     }
                  }

                  if (mode == StorageMode.MYSQL) {
                     SqliteToMySqlMigration sqliteMigration = new SqliteToMySqlMigration(this, this.databaseManager);
                     if (sqliteMigration.needsMigration()) {
                        this.getLogger().info("SQLite data detected, starting migration to MySQL...");
                        if (sqliteMigration.migrate()) {
                           this.getLogger().info("SQLite to MySQL migration completed successfully!");
                        } else {
                           this.getLogger().warning("SQLite migration completed with some errors. Check logs for details.");
                        }
                     }
                  }
               } else {
                  this.debug("Local data migration is disabled in config.");
               }

               this.getLogger().info(dbType + " database storage initialized successfully.");
            } else {
               this.getLogger().severe("Failed to initialize database handler, falling back to YAML");
               this.databaseManager.shutdown();
               this.databaseManager = null;
               this.initializeYamlStorage();
            }
         } else {
            this.getLogger().severe("Failed to initialize database connection, falling back to YAML");
            this.databaseManager = null;
            this.initializeYamlStorage();
         }
      }

   }

   private void initializeYamlStorage() {
      this.spawnerFileHandler = new SpawnerFileHandler(this);
      this.spawnerStorage = this.spawnerFileHandler;
      this.getLogger().info("Using YAML file storage mode.");
   }

   private void initializeFormUIComponents() {
      boolean formUIEnabled = this.getConfig().getBoolean("bedrock_support.enable_formui", true);
      if (!formUIEnabled) {
         this.spawnerMenuFormUI = null;
         this.debug("FormUI components not initialized - disabled in config");
      } else {
         if (this.integrationManager != null && this.integrationManager.getFloodgateHook() != null && this.integrationManager.getFloodgateHook().isEnabled()) {
            try {
               this.spawnerMenuFormUI = new SpawnerMenuFormUI(this);
               this.getLogger().info("FormUI components initialized successfully for Bedrock player support");
            } catch (Exception | NoClassDefFoundError var3) {
               this.getLogger().warning("Failed to initialize FormUI components: " + var3.getMessage());
               this.spawnerMenuFormUI = null;
            }
         } else {
            this.spawnerMenuFormUI = null;
            this.debug("FormUI components not initialized - Floodgate integration not available");
         }

      }
   }

   private void initializeHandlers() {
      this.spawnerStackerUI = new SpawnerStackerUI(this);
      this.spawnEggHandler = new SpawnEggHandler(this);
      this.spawnerStackHandler = new SpawnerStackHandler(this);
      this.spawnerClickManager = new SpawnerClickManager(this);
   }

   private void initializeUIAndActions() {
      this.spawnerMenuAction = new SpawnerMenuAction(this);
      this.spawnerStackerHandler = new SpawnerStackerHandler(this);
      this.spawnerStorageAction = new SpawnerStorageAction(this);
      this.spawnerSellConfirmListener = new SpawnerSellConfirmListener(this);
   }

   private void initializeListeners() {
      this.naturalSpawnerListener = new NaturalSpawnerListener(this);
      this.spawnerExplosionListener = new SpawnerExplosionListener(this);
      this.spawnerBreakListener = new SpawnerBreakListener(this);
      this.spawnerPlaceListener = new SpawnerPlaceListener(this);
      this.worldEventHandler = new WorldEventHandler(this);
   }

   public void setUpHopperHandler() {
      this.hopperConfig = new HopperConfig(this);
      if (this.hopperService != null) {
         this.hopperService.cleanup();
         this.hopperService = null;
      }

      if (this.hopperConfig.isHopperEnabled()) {
         this.hopperService = new HopperService(this);
      }

   }

   private void registerListeners() {
      PluginManager pm = this.getServer().getPluginManager();
      pm.registerEvents(this.naturalSpawnerListener, this);
      pm.registerEvents(this.spawnerBreakListener, this);
      pm.registerEvents(this.spawnerPlaceListener, this);
      pm.registerEvents(this.spawnerStorageAction, this);
      pm.registerEvents(this.spawnerExplosionListener, this);
      pm.registerEvents(this.spawnerClickManager, this);
      pm.registerEvents(this.spawnerMenuAction, this);
      pm.registerEvents(this.spawnerStackerHandler, this);
      pm.registerEvents(this.worldEventHandler, this);
      pm.registerEvents(this.spawnerListGUI, this);
      pm.registerEvents(this.spawnerManagementHandler, this);
      pm.registerEvents(this.adminStackerHandler, this);
      pm.registerEvents(this.serverSelectionHandler, this);
      pm.registerEvents(this.pricesGUI, this);
      pm.registerEvents(this.spawnerSellConfirmListener, this);
      if (this.spawnerAuditListener != null) {
         pm.registerEvents(this.spawnerAuditListener, this);
      }

   }

   private void setupCommand() {
      this.brigadierCommandManager = new BrigadierCommandManager(this);
      this.brigadierCommandManager.registerCommands();
      this.userPreferenceCache = new UserPreferenceCache(this);
      this.listSubCommand = new ListSubCommand(this);
      this.spawnerListGUI = new SpawnerListGUI(this);
      this.spawnerManagementHandler = new SpawnerManagementHandler(this, this.listSubCommand);
      this.adminStackerHandler = new AdminStackerHandler(this, new SpawnerManagementGUI(this));
      this.serverSelectionHandler = new ServerSelectionHandler(this, this.listSubCommand);
      this.pricesGUI = new PricesGUI(this);
   }

   private void setupBtatsMetrics() {
      Metrics metrics = new Metrics(this, 24822);
      metrics.addCustomChart(new Metrics.SimplePie("holograms", () -> {
         return String.valueOf(this.getConfig().getBoolean("hologram.enabled", false));
      }));
      metrics.addCustomChart(new Metrics.SimplePie("hoppers", () -> {
         return String.valueOf(this.getConfig().getBoolean("hopper.enabled", false));
      }));
      metrics.addCustomChart(new Metrics.SimplePie("spawners", () -> {
         return String.valueOf(this.spawnerManager.getTotalSpawners() / 1000 * 1000);
      }));
   }

   public void reload() {
      this.guiLayoutConfig.reloadLayouts();
      this.spawnerGuiViewManager.clearSlotCache();
      if (this.spawnerMenuUI != null) {
         this.spawnerMenuUI.clearCache();
      }

      this.spawnerStorageAction.loadConfig();
      this.spawnerStorageUI.reload();
      this.filterConfigUI.reload();
      if (this.spawnerSellConfirmUI != null) {
         this.spawnerSellConfirmUI.reload();
      }

      this.integrationManager.reload();
      this.spawnerMenuAction.reload();
      this.timeFormatter.clearCache();
      if (this.spawnerSettingsConfig != null) {
         this.spawnerSettingsConfig.reload();
         SpawnerMobHeadTexture.clearCache();
      }

      if (this.itemSpawnerSettingsConfig != null) {
         this.itemSpawnerSettingsConfig.reload();
      }

      this.loggingConfig.loadConfig();
      this.spawnerActionLogger.shutdown();
      this.spawnerActionLogger = new SpawnerActionLogger(this, this.loggingConfig);
      this.spawnerAuditListener = new SpawnerAuditListener(this, this.spawnerActionLogger);
      this.getServer().getPluginManager().registerEvents(this.spawnerAuditListener, this);
      this.initializeFormUIComponents();
   }

   public void onDisable() {
      this.saveAndCleanup();
      SpawnerMobHeadTexture.clearCache();
      this.getLogger().info("SmartSpawner has been disabled!");
   }

   private void saveAndCleanup() {
      if (this.spawnerManager != null) {
         try {
            if (this.spawnerStorage != null) {
               this.spawnerStorage.shutdown();
            }

            if (this.databaseManager != null) {
               this.databaseManager.shutdown();
            }

            this.spawnerManager.cleanupAllSpawners();
         } catch (Exception var2) {
            this.getLogger().log(Level.SEVERE, "Error saving spawner data during shutdown", var2);
         }
      }

      if (this.itemPriceManager != null) {
         this.itemPriceManager.cleanup();
      }

      if (this.spawnerActionLogger != null) {
         this.spawnerActionLogger.shutdown();
      }

      this.cleanupResources();
   }

   private void cleanupResources() {
      if (this.rangeChecker != null) {
         this.rangeChecker.cleanup();
      }

      if (this.spawnerGuiViewManager != null) {
         this.spawnerGuiViewManager.cleanup();
      }

      if (this.hopperService != null) {
         this.hopperService.cleanup();
      }

      if (this.spawnerClickManager != null) {
         this.spawnerClickManager.cleanup();
      }

      if (this.spawnerStackerHandler != null) {
         this.spawnerStackerHandler.cleanupAll();
      }

      if (this.spawnerStorageUI != null) {
         this.spawnerStorageUI.cleanup();
      }

      if (this.spawnerLocationLockManager != null) {
         this.spawnerLocationLockManager.shutdown();
      }

   }

   public SpawnerProvider getSpawnerProvider() {
      return new SpawnerProvider(this);
   }

   public boolean hasSellIntegration() {
      return this.itemPriceManager == null ? false : this.itemPriceManager.hasSellIntegration();
   }

   public long getTimeFromConfig(String path, String defaultValue) {
      return this.timeFormatter.getTimeFromConfig(path, defaultValue);
   }

   public void debug(String message) {
      if (this.debugMode) {
         this.getLogger().info("[DEBUG] " + message);
      }

   }

   @Generated
   public int getDATA_VERSION() {
      Objects.requireNonNull(this);
      return 3;
   }

   @Generated
   public boolean isDebugMode() {
      return this.debugMode;
   }

   @Generated
   public IntegrationManager getIntegrationManager() {
      return this.integrationManager;
   }

   @Generated
   public TimeFormatter getTimeFormatter() {
      return this.timeFormatter;
   }

   @Generated
   public ConfigUpdater getConfigUpdater() {
      return this.configUpdater;
   }

   @Generated
   public LanguageManager getLanguageManager() {
      return this.languageManager;
   }

   @Generated
   public LanguageUpdater getLanguageUpdater() {
      return this.languageUpdater;
   }

   @Generated
   public MessageService getMessageService() {
      return this.messageService;
   }

   @Generated
   public SpawnerSettingsConfig getSpawnerSettingsConfig() {
      return this.spawnerSettingsConfig;
   }

   @Generated
   public ItemSpawnerSettingsConfig getItemSpawnerSettingsConfig() {
      return this.itemSpawnerSettingsConfig;
   }

   @Generated
   public SpawnerItemFactory getSpawnerItemFactory() {
      return this.spawnerItemFactory;
   }

   @Generated
   public GuiLayoutConfig getGuiLayoutConfig() {
      return this.guiLayoutConfig;
   }

   @Generated
   public SpawnerMenuUI getSpawnerMenuUI() {
      return this.spawnerMenuUI;
   }

   @Generated
   public SpawnerMenuFormUI getSpawnerMenuFormUI() {
      return this.spawnerMenuFormUI;
   }

   @Generated
   public SpawnerStorageUI getSpawnerStorageUI() {
      return this.spawnerStorageUI;
   }

   @Generated
   public FilterConfigUI getFilterConfigUI() {
      return this.filterConfigUI;
   }

   @Generated
   public SpawnerStackerUI getSpawnerStackerUI() {
      return this.spawnerStackerUI;
   }

   @Generated
   public SpawnerSellConfirmUI getSpawnerSellConfirmUI() {
      return this.spawnerSellConfirmUI;
   }

   @Generated
   public SpawnEggHandler getSpawnEggHandler() {
      return this.spawnEggHandler;
   }

   @Generated
   public SpawnerClickManager getSpawnerClickManager() {
      return this.spawnerClickManager;
   }

   @Generated
   public SpawnerStackHandler getSpawnerStackHandler() {
      return this.spawnerStackHandler;
   }

   @Generated
   public SpawnerMenuAction getSpawnerMenuAction() {
      return this.spawnerMenuAction;
   }

   @Generated
   public SpawnerStackerHandler getSpawnerStackerHandler() {
      return this.spawnerStackerHandler;
   }

   @Generated
   public SpawnerStorageAction getSpawnerStorageAction() {
      return this.spawnerStorageAction;
   }

   @Generated
   public SpawnerSellManager getSpawnerSellManager() {
      return this.spawnerSellManager;
   }

   @Generated
   public SpawnerSellConfirmListener getSpawnerSellConfirmListener() {
      return this.spawnerSellConfirmListener;
   }

   @Generated
   public SpawnerFileHandler getSpawnerFileHandler() {
      return this.spawnerFileHandler;
   }

   @Generated
   public SpawnerStorage getSpawnerStorage() {
      return this.spawnerStorage;
   }

   @Generated
   public DatabaseManager getDatabaseManager() {
      return this.databaseManager;
   }

   @Generated
   public SpawnerManager getSpawnerManager() {
      return this.spawnerManager;
   }

   @Generated
   public HopperService getHopperService() {
      return this.hopperService;
   }

   @Generated
   public HopperConfig getHopperConfig() {
      return this.hopperConfig;
   }

   @Generated
   public SpawnerLocationLockManager getSpawnerLocationLockManager() {
      return this.spawnerLocationLockManager;
   }

   @Generated
   public NaturalSpawnerListener getNaturalSpawnerListener() {
      return this.naturalSpawnerListener;
   }

   @Generated
   public SpawnerLootGenerator getSpawnerLootGenerator() {
      return this.spawnerLootGenerator;
   }

   @Generated
   public SpawnerRangeChecker getRangeChecker() {
      return this.rangeChecker;
   }

   @Generated
   public SpawnerGuiViewManager getSpawnerGuiViewManager() {
      return this.spawnerGuiViewManager;
   }

   @Generated
   public SpawnerExplosionListener getSpawnerExplosionListener() {
      return this.spawnerExplosionListener;
   }

   @Generated
   public SpawnerBreakListener getSpawnerBreakListener() {
      return this.spawnerBreakListener;
   }

   @Generated
   public SpawnerPlaceListener getSpawnerPlaceListener() {
      return this.spawnerPlaceListener;
   }

   @Generated
   public WorldEventHandler getWorldEventHandler() {
      return this.worldEventHandler;
   }

   @Generated
   public ItemPriceManager getItemPriceManager() {
      return this.itemPriceManager;
   }

   @Generated
   public UpdateChecker getUpdateChecker() {
      return this.updateChecker;
   }

   @Generated
   public BrigadierCommandManager getBrigadierCommandManager() {
      return this.brigadierCommandManager;
   }

   @Generated
   public ListSubCommand getListSubCommand() {
      return this.listSubCommand;
   }

   @Generated
   public UserPreferenceCache getUserPreferenceCache() {
      return this.userPreferenceCache;
   }

   @Generated
   public SpawnerListGUI getSpawnerListGUI() {
      return this.spawnerListGUI;
   }

   @Generated
   public SpawnerManagementHandler getSpawnerManagementHandler() {
      return this.spawnerManagementHandler;
   }

   @Generated
   public AdminStackerHandler getAdminStackerHandler() {
      return this.adminStackerHandler;
   }

   @Generated
   public ServerSelectionHandler getServerSelectionHandler() {
      return this.serverSelectionHandler;
   }

   @Generated
   public PricesGUI getPricesGUI() {
      return this.pricesGUI;
   }

   @Generated
   public SpawnerAuditListener getSpawnerAuditListener() {
      return this.spawnerAuditListener;
   }

   @Generated
   public LoggingConfig getLoggingConfig() {
      return this.loggingConfig;
   }

   @Generated
   public SmartSpawnerAPIImpl getApiImpl() {
      return this.apiImpl;
   }

   @Generated
   public static SmartSpawner getInstance() {
      return instance;
   }

   @Generated
   public SpawnerActionLogger getSpawnerActionLogger() {
      return this.spawnerActionLogger;
   }
}
