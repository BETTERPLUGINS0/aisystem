package com.nisovin.shopkeepers;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.ShopkeepersStartupEvent;
import com.nisovin.shopkeepers.api.internal.ApiInternals;
import com.nisovin.shopkeepers.api.internal.InternalShopkeepersAPI;
import com.nisovin.shopkeepers.api.internal.InternalShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.commands.Commands;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.compat.MC_1_21_11;
import com.nisovin.shopkeepers.compat.MC_1_21_9;
import com.nisovin.shopkeepers.compat.ServerAssumptionsTest;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.config.lib.ConfigLoadException;
import com.nisovin.shopkeepers.container.protection.ProtectedContainers;
import com.nisovin.shopkeepers.container.protection.RemoveShopOnContainerBreak;
import com.nisovin.shopkeepers.debug.Debug;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.debug.events.EventDebugger;
import com.nisovin.shopkeepers.debug.trades.TradingCountListener;
import com.nisovin.shopkeepers.dependencies.worldguard.WorldGuardDependency;
import com.nisovin.shopkeepers.input.chat.ChatInput;
import com.nisovin.shopkeepers.input.interaction.InteractionInput;
import com.nisovin.shopkeepers.internals.SKApiInternals;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.metrics.PluginMetrics;
import com.nisovin.shopkeepers.moving.ShopkeeperMoving;
import com.nisovin.shopkeepers.naming.ShopkeeperNaming;
import com.nisovin.shopkeepers.playershops.PlayerShops;
import com.nisovin.shopkeepers.shopcreation.ShopkeeperCreation;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.SKDefaultShopTypes;
import com.nisovin.shopkeepers.shopkeeper.SKShopTypesRegistry;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.shopobjects.SKDefaultShopObjectTypes;
import com.nisovin.shopkeepers.shopobjects.SKShopObjectTypesRegistry;
import com.nisovin.shopkeepers.shopobjects.block.base.BaseBlockShops;
import com.nisovin.shopkeepers.shopobjects.citizens.CitizensShops;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShops;
import com.nisovin.shopkeepers.shopobjects.living.LivingShops;
import com.nisovin.shopkeepers.spigot.SpigotFeatures;
import com.nisovin.shopkeepers.storage.SKShopkeeperStorage;
import com.nisovin.shopkeepers.tradelog.TradeLoggers;
import com.nisovin.shopkeepers.tradelog.history.TradingHistoryProvider;
import com.nisovin.shopkeepers.tradenotifications.TradeNotifications;
import com.nisovin.shopkeepers.trading.commandtrading.CommandTrading;
import com.nisovin.shopkeepers.ui.SKDefaultUITypes;
import com.nisovin.shopkeepers.ui.SKUIRegistry;
import com.nisovin.shopkeepers.ui.SKUISystem;
import com.nisovin.shopkeepers.util.bukkit.SchedulerUtils;
import com.nisovin.shopkeepers.util.java.ClassUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import com.nisovin.shopkeepers.villagers.RegularVillagers;
import com.nisovin.shopkeepers.world.ForcingEntitySpawner;
import com.nisovin.shopkeepers.world.ForcingEntityTeleporter;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKShopkeepersPlugin extends JavaPlugin implements InternalShopkeepersPlugin {
   private static final Set<? extends String> SKIP_PRELOADING_CLASSES = Collections.unmodifiableSet(new HashSet(Arrays.asList("com.nisovin.shopkeepers.dependencies.worldguard.WorldGuardDependency$Internal", "com.nisovin.shopkeepers.dependencies.citizens.CitizensUtils$Internal", "com.nisovin.shopkeepers.shopobjects.citizens.CitizensShopkeeperTrait", "com.nisovin.shopkeepers.spigot.text.SpigotText$Internal")));
   private static final int ASYNC_TASKS_TIMEOUT_SECONDS = 10;
   @Nullable
   private static SKShopkeepersPlugin plugin;
   private final Executor syncExecutor = SchedulerUtils.createSyncExecutor((Plugin)Unsafe.initialized(this));
   private final Executor asyncExecutor = SchedulerUtils.createAsyncExecutor((Plugin)Unsafe.initialized(this));
   private final ForcingEntitySpawner forcingEntitySpawner = new ForcingEntitySpawner((SKShopkeepersPlugin)Unsafe.initialized(this));
   private final ForcingEntityTeleporter forcingEntityTeleporter = new ForcingEntityTeleporter((SKShopkeepersPlugin)Unsafe.initialized(this));
   private final ApiInternals apiInternals = new SKApiInternals();
   private final SKShopTypesRegistry shopTypesRegistry = new SKShopTypesRegistry();
   private final SKShopObjectTypesRegistry shopObjectTypesRegistry = new SKShopObjectTypesRegistry();
   private final SKUISystem uiSystem = new SKUISystem((ShopkeepersPlugin)Unsafe.initialized(this));
   private final SKUIRegistry uiRegistry = new SKUIRegistry();
   private final SKDefaultUITypes defaultUITypes = new SKDefaultUITypes();
   private final SKShopkeeperRegistry shopkeeperRegistry = new SKShopkeeperRegistry((SKShopkeepersPlugin)Unsafe.initialized(this));
   private final SKShopkeeperStorage shopkeeperStorage = new SKShopkeeperStorage((SKShopkeepersPlugin)Unsafe.initialized(this));
   private final Commands commands = new Commands((SKShopkeepersPlugin)Unsafe.initialized(this));
   private final ChatInput chatInput = new ChatInput((Plugin)Unsafe.initialized(this));
   private final InteractionInput interactionInput = new InteractionInput((Plugin)Unsafe.initialized(this));
   private final CommandTrading commandTrading = new CommandTrading((ShopkeepersPlugin)Unsafe.initialized(this));
   private final TradeLoggers tradeLoggers = new TradeLoggers((SKShopkeepersPlugin)Unsafe.initialized(this));
   private final TradeNotifications tradeNotifications = new TradeNotifications((Plugin)Unsafe.initialized(this));
   private final EventDebugger eventDebugger = new EventDebugger((SKShopkeepersPlugin)Unsafe.initialized(this));
   private final PlayerShops playerShops = new PlayerShops((SKShopkeepersPlugin)Unsafe.initialized(this));
   private final ProtectedContainers protectedContainers = new ProtectedContainers((SKShopkeepersPlugin)Unsafe.initialized(this));
   private final ShopkeeperCreation shopkeeperCreation;
   private final ShopkeeperNaming shopkeeperNaming;
   private final ShopkeeperMoving shopkeeperMoving;
   private final RemoveShopOnContainerBreak removeShopOnContainerBreak;
   private final BaseBlockShops blockShops;
   private final BaseEntityShops entityShops;
   private final LivingShops livingShops;
   private final CitizensShops citizensShops;
   private final RegularVillagers regularVillagers;
   private final SKDefaultShopTypes defaultShopTypes;
   private final SKDefaultShopObjectTypes defaultShopObjectTypes;
   private final PluginMetrics pluginMetrics;
   private boolean outdatedServer;
   private boolean incompatibleServer;
   @Nullable
   private ConfigLoadException configLoadError;

   public static boolean isPluginEnabled() {
      return plugin != null;
   }

   public static SKShopkeepersPlugin getInstance() {
      return (SKShopkeepersPlugin)Validate.State.notNull(plugin, (String)"Plugin is not enabled!");
   }

   private void loadAllPluginClasses() {
      File pluginJarFile = this.getFile();
      long startNanos = System.nanoTime();
      boolean success = ClassUtils.loadAllClassesFromJar(pluginJarFile, (className) -> {
         if (className.startsWith("com.nisovin.shopkeepers.compat.")) {
            return false;
         } else {
            return !SKIP_PRELOADING_CLASSES.contains(className);
         }
      }, this.getLogger());
      if (success) {
         long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
         Log.info("Loaded all plugin classes (" + durationMillis + " ms).");
      }

   }

   private boolean isOutdatedServerVersion() {
      return !EntityType.ITEM.isSpawnable();
   }

   private void registerDefaults() {
      Log.info("Registering defaults.");
      this.defaultShopObjectTypes.onRegisterDefaults();
      this.uiRegistry.registerAll(this.defaultUITypes.getAllUITypes());
      this.shopTypesRegistry.registerAll(this.defaultShopTypes.getAll());
      this.shopObjectTypesRegistry.registerAll(this.defaultShopObjectTypes.getAll());
   }

   public SKShopkeepersPlugin() {
      this.shopkeeperCreation = new ShopkeeperCreation((SKShopkeepersPlugin)Unsafe.initialized(this), this.shopkeeperRegistry, this.protectedContainers);
      this.shopkeeperNaming = new ShopkeeperNaming(this.chatInput);
      this.shopkeeperMoving = new ShopkeeperMoving(this.interactionInput, this.shopkeeperCreation.getShopkeeperPlacement());
      this.removeShopOnContainerBreak = new RemoveShopOnContainerBreak((SKShopkeepersPlugin)Unsafe.initialized(this), this.protectedContainers);
      this.blockShops = new BaseBlockShops((SKShopkeepersPlugin)Unsafe.initialized(this));
      this.entityShops = new BaseEntityShops((SKShopkeepersPlugin)Unsafe.initialized(this));
      this.livingShops = new LivingShops((SKShopkeepersPlugin)Unsafe.initialized(this), this.entityShops);
      this.citizensShops = new CitizensShops((SKShopkeepersPlugin)Unsafe.initialized(this));
      this.regularVillagers = new RegularVillagers((SKShopkeepersPlugin)Unsafe.initialized(this));
      this.defaultShopTypes = new SKDefaultShopTypes();
      this.defaultShopObjectTypes = new SKDefaultShopObjectTypes((SKShopkeepersPlugin)Unsafe.initialized(this), this.blockShops, this.entityShops);
      this.pluginMetrics = new PluginMetrics((SKShopkeepersPlugin)Unsafe.initialized(this));
      this.outdatedServer = false;
      this.incompatibleServer = false;
      this.configLoadError = null;
   }

   public void onLoad() {
      Log.setLogger(this.getLogger());
      plugin = this;
      InternalShopkeepersAPI.enable(this);
      this.outdatedServer = this.isOutdatedServerVersion();
      if (!this.outdatedServer) {
         this.incompatibleServer = !Compat.load(this);
         if (!this.incompatibleServer) {
            this.configLoadError = Settings.loadConfig();
            if (this.configLoadError == null) {
               Messages.loadLanguageFile();
               if (Settings.registerWorldGuardAllowShopFlag) {
                  WorldGuardDependency.registerAllowShopFlag();
               }

               this.registerDefaults();
               this.loadAllPluginClasses();
            }
         }
      }
   }

   public void onEnable() {
      assert Log.getLogger() != null;

      boolean alreadySetUp = true;
      if (plugin == null) {
         alreadySetUp = false;
         plugin = this;
         InternalShopkeepersAPI.enable(this);
      }

      if (this.outdatedServer) {
         Log.severe("Outdated server version (" + Bukkit.getVersion() + "): Shopkeepers cannot be enabled. Please update your server!");
         this.setEnabled(false);
      } else if (this.incompatibleServer) {
         Log.severe("Incompatible server version: Shopkeepers cannot be enabled.");
         this.setEnabled(false);
      } else {
         if (!alreadySetUp) {
            this.configLoadError = Settings.loadConfig();
         } else {
            Log.debug("Config already loaded.");
         }

         if (this.configLoadError != null) {
            Log.severe((String)"Could not load the config!", (Throwable)this.configLoadError);
            this.setEnabled(false);
         } else {
            if (!alreadySetUp) {
               Messages.loadLanguageFile();
            } else {
               Log.debug("Language file already loaded.");
            }

            MC_1_21_9.init();
            MC_1_21_11.init();
            Compat.getProvider().onEnable();
            if (SpigotFeatures.isSpigotAvailable()) {
               Log.debug("Spigot-based server found: Enabling Spigot exclusive features.");
            } else {
               Log.info("No Spigot-based server found: Disabling Spigot exclusive features!");
            }

            if (!ServerAssumptionsTest.run()) {
               if (!Settings.ignoreFailedServerAssumptionTests) {
                  Log.severe("Server incompatibility detected! Disabling the plugin!");
                  this.setEnabled(false);
                  return;
               }

               Log.severe("Server incompatibility detected! But we continue to enable the plugin anyway, because setting 'ignore-failed-server-assumption-tests' is enabled. Runnning the plugin in this mode is unsupported!");
            }

            if (!alreadySetUp) {
               this.registerDefaults();
            } else {
               Log.debug("Defaults already registered.");
            }

            Bukkit.getPluginManager().callEvent(new ShopkeepersStartupEvent());
            this.forcingEntitySpawner.onEnable();
            this.forcingEntityTeleporter.onEnable();
            this.uiSystem.onEnable();
            this.protectedContainers.enable();
            this.removeShopOnContainerBreak.onEnable();
            PluginManager pm = Bukkit.getPluginManager();
            pm.registerEvents(new PlayerJoinQuitListener(this), this);
            (new TradingCountListener(this)).onEnable();
            this.blockShops.onEnable();
            this.entityShops.onEnable();
            this.livingShops.onEnable();
            this.citizensShops.onEnable();
            this.regularVillagers.onEnable();
            this.commands.onEnable();
            this.chatInput.onEnable();
            this.interactionInput.onEnable();
            this.shopkeeperCreation.onEnable();
            this.shopkeeperNaming.onEnable();
            this.shopkeeperMoving.onEnable();
            this.shopkeeperStorage.onEnable();
            this.shopkeeperRegistry.onEnable();
            if (Debug.isDebugging()) {
               ShopkeeperDataMigrator.logRegisteredMigrations();
            }

            boolean loadingSuccessful = this.shopkeeperStorage.reload();
            if (!loadingSuccessful) {
               Log.severe("Detected an issue during the loading of the saved shopkeepers data! Disabling the plugin!");
               this.shopkeeperStorage.disableSaving();
               Bukkit.getPluginManager().disablePlugin(this);
            } else {
               this.shopkeeperRegistry.getChunkActivator().activateShopkeepersInAllWorlds();
               this.playerShops.onEnable();
               this.commandTrading.onEnable();
               this.tradeLoggers.onEnable();
               this.tradeNotifications.onEnable();
               this.shopkeeperStorage.saveIfDirty();
               this.pluginMetrics.onEnable();
               this.eventDebugger.onEnable();
            }
         }
      }
   }

   public void onDisable() {
      SchedulerUtils.awaitAsyncTasksCompletion(this, 10, this.getLogger());
      this.uiSystem.onDisable();
      this.shopkeeperRegistry.getChunkActivator().deactivateShopkeepersInAllWorlds();
      this.blockShops.onDisable();
      this.livingShops.onDisable();
      this.entityShops.onDisable();
      this.citizensShops.onDisable();
      this.protectedContainers.disable();
      this.removeShopOnContainerBreak.onDisable();
      this.shopkeeperRegistry.onDisable();
      this.shopkeeperStorage.onDisable();
      this.shopTypesRegistry.clearAllSelections();
      this.shopObjectTypesRegistry.clearAllSelections();
      this.commands.onDisable();
      this.chatInput.onDisable();
      this.interactionInput.onDisable();
      this.regularVillagers.onDisable();
      this.shopkeeperNaming.onDisable();
      this.shopkeeperMoving.onDisable();
      this.shopkeeperCreation.onDisable();
      this.playerShops.onDisable();
      this.commandTrading.onDisable();
      this.tradeLoggers.onDisable();
      this.tradeNotifications.onDisable();
      this.shopTypesRegistry.clearAll();
      this.shopObjectTypesRegistry.clearAll();
      this.uiRegistry.clearAll();
      this.forcingEntityTeleporter.onDisable();
      this.forcingEntitySpawner.onDisable();
      this.pluginMetrics.onDisable();
      this.eventDebugger.onDisable();
      if (Compat.hasProvider()) {
         Compat.getProvider().onDisable();
      }

      HandlerList.unregisterAll(this);
      Bukkit.getScheduler().cancelTasks(this);
      InternalShopkeepersAPI.disable();
      plugin = null;
   }

   public void reload() {
      this.onDisable();
      this.onEnable();
   }

   void onPlayerJoin(Player player) {
   }

   void onPlayerQuit(Player player) {
      this.shopTypesRegistry.clearSelection(player);
      this.shopObjectTypesRegistry.clearSelection(player);
      this.shopkeeperCreation.onPlayerQuit(player);
      this.commands.onPlayerQuit(player);
   }

   public ApiInternals getApiInternals() {
      return this.apiInternals;
   }

   public Executor getSyncExecutor() {
      return this.syncExecutor;
   }

   public Executor getAsyncExecutor() {
      return this.asyncExecutor;
   }

   public ForcingEntitySpawner getForcingEntitySpawner() {
      return this.forcingEntitySpawner;
   }

   public ForcingEntityTeleporter getForcingEntityTeleporter() {
      return this.forcingEntityTeleporter;
   }

   public SKShopkeeperRegistry getShopkeeperRegistry() {
      return this.shopkeeperRegistry;
   }

   public SKShopkeeperStorage getShopkeeperStorage() {
      return this.shopkeeperStorage;
   }

   public int updateItems() {
      Log.debug(DebugOptions.itemUpdates, "Updating all items.");
      this.uiRegistry.abortUISessions();
      int updatedItems = Settings.getInstance().updateItems();
      int shopkeeperUpdatedItems = 0;

      AbstractShopkeeper shopkeeper;
      for(Iterator var3 = this.shopkeeperRegistry.getAllShopkeepers().iterator(); var3.hasNext(); shopkeeperUpdatedItems += shopkeeper.updateItems()) {
         shopkeeper = (AbstractShopkeeper)var3.next();
      }

      if (shopkeeperUpdatedItems > 0) {
         updatedItems += shopkeeperUpdatedItems;
         this.shopkeeperStorage.save();
      }

      return updatedItems;
   }

   public Commands getCommands() {
      return this.commands;
   }

   public ChatInput getChatInput() {
      return this.chatInput;
   }

   public InteractionInput getInteractionInput() {
      return this.interactionInput;
   }

   public SKUIRegistry getUIRegistry() {
      return this.uiRegistry;
   }

   public SKDefaultUITypes getDefaultUITypes() {
      return this.defaultUITypes;
   }

   public ProtectedContainers getProtectedContainers() {
      return this.protectedContainers;
   }

   public RemoveShopOnContainerBreak getRemoveShopOnContainerBreak() {
      return this.removeShopOnContainerBreak;
   }

   public BaseEntityShops getEntityShops() {
      return this.entityShops;
   }

   public LivingShops getLivingShops() {
      return this.livingShops;
   }

   public BaseBlockShops getBlockShops() {
      return this.blockShops;
   }

   public CitizensShops getCitizensShops() {
      return this.citizensShops;
   }

   public SKShopTypesRegistry getShopTypeRegistry() {
      return this.shopTypesRegistry;
   }

   public SKDefaultShopTypes getDefaultShopTypes() {
      return this.defaultShopTypes;
   }

   public SKShopObjectTypesRegistry getShopObjectTypeRegistry() {
      return this.shopObjectTypesRegistry;
   }

   public SKDefaultShopObjectTypes getDefaultShopObjectTypes() {
      return this.defaultShopObjectTypes;
   }

   public ShopkeeperNaming getShopkeeperNaming() {
      return this.shopkeeperNaming;
   }

   public ShopkeeperMoving getShopkeeperMoving() {
      return this.shopkeeperMoving;
   }

   public RegularVillagers getRegularVillagers() {
      return this.regularVillagers;
   }

   public ShopkeeperCreation getShopkeeperCreation() {
      return this.shopkeeperCreation;
   }

   public boolean hasCreatePermission(Player player) {
      Validate.notNull(player, (String)"player is null");

      assert player != null;

      return this.shopTypesRegistry.getSelection(player) != null && this.shopObjectTypesRegistry.getSelection(player) != null;
   }

   @Nullable
   public AbstractShopkeeper handleShopkeeperCreation(ShopCreationData shopCreationData) {
      Validate.notNull(shopCreationData, (String)"shopCreationData is null");
      ShopType<?> rawShopType = shopCreationData.getShopType();
      Validate.isTrue(rawShopType instanceof AbstractShopType, "ShopType of shopCreationData is not of type AbstractShopType, but: " + rawShopType.getClass().getName());
      AbstractShopType<?> shopType = (AbstractShopType)rawShopType;
      return shopType.handleShopkeeperCreation(shopCreationData);
   }

   public PlayerShops getPlayerShops() {
      return this.playerShops;
   }

   public TradeNotifications getTradeNotifications() {
      return this.tradeNotifications;
   }

   @Nullable
   public TradingHistoryProvider getTradingHistoryProvider() {
      return this.tradeLoggers.getTradingHistoryProvider();
   }
}
