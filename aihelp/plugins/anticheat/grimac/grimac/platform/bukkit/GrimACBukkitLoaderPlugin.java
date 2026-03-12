package ac.grim.grimac.platform.bukkit;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.GrimExternalAPI;
import ac.grim.grimac.api.GrimAPIProvider;
import ac.grim.grimac.api.GrimAbstractAPI;
import ac.grim.grimac.api.event.EventBus;
import ac.grim.grimac.api.event.events.CommandExecuteEvent;
import ac.grim.grimac.api.event.events.CompletePredictionEvent;
import ac.grim.grimac.api.event.events.FlagEvent;
import ac.grim.grimac.api.event.events.GrimJoinEvent;
import ac.grim.grimac.api.event.events.GrimQuitEvent;
import ac.grim.grimac.api.event.events.GrimReloadEvent;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.command.CloudCommandService;
import ac.grim.grimac.internal.platform.bukkit.resolver.BukkitResolverRegistrar;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.manager.init.start.ExemptOnlinePlayersOnReload;
import ac.grim.grimac.platform.api.Platform;
import ac.grim.grimac.platform.api.PlatformLoader;
import ac.grim.grimac.platform.api.PlatformServer;
import ac.grim.grimac.platform.api.command.CommandService;
import ac.grim.grimac.platform.api.manager.ItemResetHandler;
import ac.grim.grimac.platform.api.manager.MessagePlaceHolderManager;
import ac.grim.grimac.platform.api.manager.PlatformPluginManager;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory;
import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.api.sender.SenderFactory;
import ac.grim.grimac.platform.bukkit.initables.BukkitBStats;
import ac.grim.grimac.platform.bukkit.initables.BukkitEventManager;
import ac.grim.grimac.platform.bukkit.initables.BukkitTickEndEvent;
import ac.grim.grimac.platform.bukkit.manager.BukkitItemResetHandler;
import ac.grim.grimac.platform.bukkit.manager.BukkitMessagePlaceHolderManager;
import ac.grim.grimac.platform.bukkit.manager.BukkitParserDescriptorFactory;
import ac.grim.grimac.platform.bukkit.manager.BukkitPermissionRegistrationManager;
import ac.grim.grimac.platform.bukkit.manager.BukkitPlatformPluginManager;
import ac.grim.grimac.platform.bukkit.player.BukkitPlatformPlayerFactory;
import ac.grim.grimac.platform.bukkit.scheduler.bukkit.BukkitPlatformScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.folia.FoliaPlatformScheduler;
import ac.grim.grimac.platform.bukkit.sender.BukkitSenderFactory;
import ac.grim.grimac.platform.bukkit.utils.placeholder.PlaceholderAPIExpansion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierSetting;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.CloudBukkitCapabilities;
import ac.grim.grimac.shaded.incendo.cloud.execution.ExecutionCoordinator;
import ac.grim.grimac.shaded.incendo.cloud.paper.LegacyPaperCommandManager;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.lazy.LazyHolder;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrimACBukkitLoaderPlugin extends JavaPlugin implements PlatformLoader {
   public static GrimACBukkitLoaderPlugin LOADER;
   private final LazyHolder<PlatformScheduler> scheduler = LazyHolder.simple(this::createScheduler);
   private final LazyHolder<PacketEventsAPI<?>> packetEvents = LazyHolder.simple(() -> {
      return SpigotPacketEventsBuilder.build(this);
   });
   private final LazyHolder<BukkitSenderFactory> senderFactory = LazyHolder.simple(BukkitSenderFactory::new);
   private final LazyHolder<ItemResetHandler> itemResetHandler = LazyHolder.simple(BukkitItemResetHandler::new);
   private final LazyHolder<CommandService> commandService = LazyHolder.simple(this::createCommandService);
   private final CloudCommandAdapter commandAdapter = new BukkitParserDescriptorFactory();
   private final PlatformPlayerFactory platformPlayerFactory = new BukkitPlatformPlayerFactory();
   private final PlatformPluginManager pluginManager = new BukkitPlatformPluginManager();
   private final GrimPlugin plugin;
   private final PlatformServer platformServer = new BukkitPlatformServer();
   private final MessagePlaceHolderManager messagePlaceHolderManager = new BukkitMessagePlaceHolderManager();
   private final BukkitPermissionRegistrationManager permissionManager = new BukkitPermissionRegistrationManager();

   public GrimACBukkitLoaderPlugin() {
      BukkitResolverRegistrar registrar = new BukkitResolverRegistrar();
      registrar.registerAll(GrimAPI.INSTANCE.getExtensionManager());
      this.plugin = registrar.resolvePlugin(this);
   }

   public void onLoad() {
      LOADER = this;
      GrimAPI.INSTANCE.load(this, this.getBukkitInitTasks());
   }

   private Initable[] getBukkitInitTasks() {
      return new Initable[]{new ExemptOnlinePlayersOnReload(), new BukkitEventManager(), new BukkitTickEndEvent(), new BukkitBStats(), () -> {
         if (BukkitMessagePlaceHolderManager.hasPlaceholderAPI) {
            (new PlaceholderAPIExpansion()).register();
         }

      }};
   }

   public void onEnable() {
      GrimAPI.INSTANCE.start();
   }

   public void onDisable() {
      GrimAPI.INSTANCE.stop();
   }

   public PlatformScheduler getScheduler() {
      return (PlatformScheduler)this.scheduler.get();
   }

   public PacketEventsAPI<?> getPacketEvents() {
      return (PacketEventsAPI)this.packetEvents.get();
   }

   public ItemResetHandler getItemResetHandler() {
      return (ItemResetHandler)this.itemResetHandler.get();
   }

   public CommandService getCommandService() {
      return (CommandService)this.commandService.get();
   }

   public SenderFactory<CommandSender> getSenderFactory() {
      return (SenderFactory)this.senderFactory.get();
   }

   public void registerAPIService() {
      GrimExternalAPI externalAPI = GrimAPI.INSTANCE.getExternalAPI();
      EventBus eventBus = externalAPI.getEventBus();
      GrimPlugin context = GrimAPI.INSTANCE.getGrimPlugin();
      eventBus.subscribe(context, GrimJoinEvent.class, (event) -> {
         ac.grim.grimac.api.events.GrimJoinEvent bukkitEvent = new ac.grim.grimac.api.events.GrimJoinEvent(event.getUser());
         Bukkit.getPluginManager().callEvent(bukkitEvent);
      });
      eventBus.subscribe(context, GrimQuitEvent.class, (event) -> {
         ac.grim.grimac.api.events.GrimQuitEvent bukkitEvent = new ac.grim.grimac.api.events.GrimQuitEvent(event.getUser());
         Bukkit.getPluginManager().callEvent(bukkitEvent);
      });
      eventBus.subscribe(context, GrimReloadEvent.class, (event) -> {
         ac.grim.grimac.api.events.GrimReloadEvent bukkitEvent = new ac.grim.grimac.api.events.GrimReloadEvent(event.isSuccess());
         Bukkit.getPluginManager().callEvent(bukkitEvent);
      });
      eventBus.subscribe(context, FlagEvent.class, (event) -> {
         ac.grim.grimac.api.events.FlagEvent bukkitEvent = new ac.grim.grimac.api.events.FlagEvent(event.getUser(), event.getCheck(), event.getVerbose());
         Bukkit.getPluginManager().callEvent(bukkitEvent);
         if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
         }

      });
      eventBus.subscribe(context, CommandExecuteEvent.class, (event) -> {
         ac.grim.grimac.api.events.CommandExecuteEvent bukkitEvent = new ac.grim.grimac.api.events.CommandExecuteEvent(event.getUser(), event.getCheck(), event.getVerbose(), event.getCommand());
         Bukkit.getPluginManager().callEvent(bukkitEvent);
         if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
         }

      });
      eventBus.subscribe(context, CompletePredictionEvent.class, (event) -> {
         ac.grim.grimac.api.events.CompletePredictionEvent bukkitEvent = new ac.grim.grimac.api.events.CompletePredictionEvent(event.getUser(), event.getCheck(), "", event.getOffset());
         Bukkit.getPluginManager().callEvent(bukkitEvent);
         if (bukkitEvent.isCancelled()) {
            event.setCancelled(true);
         }

      });
      GrimAPIProvider.init(externalAPI);
      Bukkit.getServicesManager().register(GrimAbstractAPI.class, externalAPI, LOADER, ServicePriority.Normal);
   }

   private PlatformScheduler createScheduler() {
      return (PlatformScheduler)(GrimAPI.INSTANCE.getPlatform() == Platform.FOLIA ? new FoliaPlatformScheduler() : new BukkitPlatformScheduler());
   }

   private CommandService createCommandService() {
      try {
         return new CloudCommandService(this::createCloudCommandManager, this.commandAdapter);
      } catch (Throwable var2) {
         LogUtil.warn("CRITICAL: Failed to initialize Command Framework. Grim will continue to run with no commands.", var2);
         return () -> {
         };
      }
   }

   private CommandManager<Sender> createCloudCommandManager() {
      LegacyPaperCommandManager<Sender> manager = new LegacyPaperCommandManager(this, ExecutionCoordinator.simpleCoordinator(), (SenderMapper)this.senderFactory.get());
      if (manager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
         try {
            manager.registerBrigadier();
            CloudBrigadierManager<Sender, ?> cbm = manager.brigadierManager();
            cbm.settings().set(BrigadierSetting.FORCE_EXECUTABLE, true);
         } catch (Throwable var3) {
            LogUtil.error("Failed to register Brigadier native completions. Falling back to standard completions.", var3);
         }
      } else if (manager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
         manager.registerAsynchronousCompletions();
      }

      return manager;
   }

   public BukkitSenderFactory getBukkitSenderFactory() {
      return (BukkitSenderFactory)LOADER.senderFactory.get();
   }

   @Generated
   public PlatformPlayerFactory getPlatformPlayerFactory() {
      return this.platformPlayerFactory;
   }

   @Generated
   public PlatformPluginManager getPluginManager() {
      return this.pluginManager;
   }

   @Generated
   public GrimPlugin getPlugin() {
      return this.plugin;
   }

   @Generated
   public PlatformServer getPlatformServer() {
      return this.platformServer;
   }

   @Generated
   public MessagePlaceHolderManager getMessagePlaceHolderManager() {
      return this.messagePlaceHolderManager;
   }

   @Generated
   public BukkitPermissionRegistrationManager getPermissionManager() {
      return this.permissionManager;
   }
}
