package ac.grim.grimac;

import ac.grim.grimac.api.event.EventBus;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.internal.event.OptimizedEventBus;
import ac.grim.grimac.internal.plugin.resolver.GrimExtensionManager;
import ac.grim.grimac.manager.AlertManagerImpl;
import ac.grim.grimac.manager.DiscordManager;
import ac.grim.grimac.manager.InitManager;
import ac.grim.grimac.manager.SpectateManager;
import ac.grim.grimac.manager.TickManager;
import ac.grim.grimac.manager.config.BaseConfigManager;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.manager.violationdatabase.ViolationDatabaseManager;
import ac.grim.grimac.platform.api.Platform;
import ac.grim.grimac.platform.api.PlatformLoader;
import ac.grim.grimac.platform.api.PlatformServer;
import ac.grim.grimac.platform.api.command.CommandService;
import ac.grim.grimac.platform.api.manager.ItemResetHandler;
import ac.grim.grimac.platform.api.manager.MessagePlaceHolderManager;
import ac.grim.grimac.platform.api.manager.PermissionRegistrationManager;
import ac.grim.grimac.platform.api.manager.PlatformPluginManager;
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory;
import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.platform.api.sender.SenderFactory;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.PlayerDataManager;
import ac.grim.grimac.utils.common.arguments.CommonGrimArguments;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import lombok.Generated;

public final class GrimAPI {
   public static final GrimAPI INSTANCE = new GrimAPI();
   private final Platform platform = detectPlatform();
   private final BaseConfigManager configManager = new BaseConfigManager();
   private final AlertManagerImpl alertManager = new AlertManagerImpl();
   private final SpectateManager spectateManager = new SpectateManager();
   private final DiscordManager discordManager = new DiscordManager();
   private final PlayerDataManager playerDataManager = new PlayerDataManager();
   private final TickManager tickManager = new TickManager();
   private final GrimExtensionManager extensionManager = new GrimExtensionManager();
   private final EventBus eventBus;
   private final GrimExternalAPI externalAPI;
   private ViolationDatabaseManager violationDatabaseManager;
   private PlatformLoader loader;
   private InitManager initManager;
   private boolean initialized = false;

   private GrimAPI() {
      this.eventBus = new OptimizedEventBus(this.extensionManager);
      this.externalAPI = new GrimExternalAPI(this);
   }

   private static Platform detectPlatform() {
      Platform override = (Platform)CommonGrimArguments.PLATFORM_OVERRIDE.value();
      if (override != null) {
         return override;
      } else if (ReflectionUtils.hasClass("io.papermc.paper.threadedregions.RegionizedServer")) {
         return Platform.FOLIA;
      } else if (ReflectionUtils.hasClass("org.bukkit.Bukkit")) {
         return Platform.BUKKIT;
      } else if (ReflectionUtils.hasClass("net.fabricmc.loader.api.FabricLoader")) {
         return Platform.FABRIC;
      } else {
         throw new IllegalStateException("Unknown platform!");
      }
   }

   public void load(PlatformLoader platformLoader, Initable... platformSpecificInitables) {
      this.loader = platformLoader;
      this.violationDatabaseManager = new ViolationDatabaseManager(this.getGrimPlugin());
      this.initManager = new InitManager(this.loader.getPacketEvents(), platformSpecificInitables);
      this.initManager.load();
      this.initialized = true;
   }

   public void start() {
      this.checkInitialized();
      this.initManager.start();
   }

   public void stop() {
      this.checkInitialized();
      this.initManager.stop();
   }

   public PlatformScheduler getScheduler() {
      return this.loader.getScheduler();
   }

   public PlatformPlayerFactory getPlatformPlayerFactory() {
      return this.loader.getPlatformPlayerFactory();
   }

   public GrimPlugin getGrimPlugin() {
      return this.loader.getPlugin();
   }

   public SenderFactory<?> getSenderFactory() {
      return this.loader.getSenderFactory();
   }

   public ItemResetHandler getItemResetHandler() {
      return this.loader.getItemResetHandler();
   }

   public PlatformPluginManager getPluginManager() {
      return this.loader.getPluginManager();
   }

   public PlatformServer getPlatformServer() {
      return this.loader.getPlatformServer();
   }

   @NotNull
   public MessagePlaceHolderManager getMessagePlaceHolderManager() {
      return this.loader.getMessagePlaceHolderManager();
   }

   public CommandService getCommandService() {
      return this.loader.getCommandService();
   }

   private void checkInitialized() {
      if (!this.initialized) {
         throw new IllegalStateException("GrimAPI has not been initialized!");
      }
   }

   public PermissionRegistrationManager getPermissionManager() {
      return this.loader.getPermissionManager();
   }

   public GrimExtensionManager getExtensionManager() {
      return this.extensionManager;
   }

   @Generated
   public BaseConfigManager getConfigManager() {
      return this.configManager;
   }

   @Generated
   public AlertManagerImpl getAlertManager() {
      return this.alertManager;
   }

   @Generated
   public SpectateManager getSpectateManager() {
      return this.spectateManager;
   }

   @Generated
   public DiscordManager getDiscordManager() {
      return this.discordManager;
   }

   @Generated
   public PlayerDataManager getPlayerDataManager() {
      return this.playerDataManager;
   }

   @Generated
   public TickManager getTickManager() {
      return this.tickManager;
   }

   @Generated
   public EventBus getEventBus() {
      return this.eventBus;
   }

   @Generated
   public GrimExternalAPI getExternalAPI() {
      return this.externalAPI;
   }

   @Generated
   public ViolationDatabaseManager getViolationDatabaseManager() {
      return this.violationDatabaseManager;
   }

   @Generated
   public PlatformLoader getLoader() {
      return this.loader;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public Platform getPlatform() {
      return this.platform;
   }

   @Generated
   public InitManager getInitManager() {
      return this.initManager;
   }
}
