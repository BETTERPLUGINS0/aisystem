package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.chunk.ForcedChunk;
import com.bergerkiller.bukkit.common.collections.ImplicitlySharedSet;
import com.bergerkiller.bukkit.common.component.LibraryComponentList;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.controller.DefaultEntityController;
import com.bergerkiller.bukkit.common.conversion.type.HandleConversion;
import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.internal.legacy.MaterialsByName;
import com.bergerkiller.bukkit.common.inventory.ItemParser;
import com.bergerkiller.bukkit.common.metrics.Metrics;
import com.bergerkiller.bukkit.common.metrics.Metrics.DrilldownPie;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.ChunkUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.sl.API.Variables;
import com.bergerkiller.bukkit.sl.API.events.SignVariablesDetectEvent;
import com.bergerkiller.bukkit.tc.actions.registry.ActionRegistry;
import com.bergerkiller.bukkit.tc.attachments.FakePlayerSpawner;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentTypeRegistry;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModelStore;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachment;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentLight;
import com.bergerkiller.bukkit.tc.attachments.control.GlowColorTeamProvider;
import com.bergerkiller.bukkit.tc.attachments.control.SeatAttachmentMap;
import com.bergerkiller.bukkit.tc.attachments.control.TeamProvider;
import com.bergerkiller.bukkit.tc.attachments.control.effect.EffectLoop;
import com.bergerkiller.bukkit.tc.attachments.control.schematic.WorldEditSchematicLoader;
import com.bergerkiller.bukkit.tc.attachments.ui.models.ResourcePackModelListing;
import com.bergerkiller.bukkit.tc.chest.TrainChestListener;
import com.bergerkiller.bukkit.tc.commands.Commands;
import com.bergerkiller.bukkit.tc.commands.selector.SelectorHandlerRegistry;
import com.bergerkiller.bukkit.tc.commands.selector.TCSelectorHandlerRegistry;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberNetwork;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.global.ActionSignHighlighter;
import com.bergerkiller.bukkit.tc.controller.global.EffectLoopPlayerController;
import com.bergerkiller.bukkit.tc.controller.global.SignController;
import com.bergerkiller.bukkit.tc.controller.global.TrainCartsPlayer;
import com.bergerkiller.bukkit.tc.controller.global.TrainCartsPlayerStore;
import com.bergerkiller.bukkit.tc.controller.global.TrainUpdateController;
import com.bergerkiller.bukkit.tc.controller.player.TrainCartsAttachmentViewerMap;
import com.bergerkiller.bukkit.tc.controller.player.network.PlayerClientSynchronizer;
import com.bergerkiller.bukkit.tc.controller.player.network.PlayerPacketListener;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.SmoothCoastersAPI;
import com.bergerkiller.bukkit.tc.dep.neznamytabnametaghider.TabNameTagHider;
import com.bergerkiller.bukkit.tc.dep.neznamytabnametaghider.TabNameTagHiderDependency;
import com.bergerkiller.bukkit.tc.dep.softdependency.SoftDependency;
import com.bergerkiller.bukkit.tc.dep.softdependency.SoftServiceDependency;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.itemanimation.ItemAnimation;
import com.bergerkiller.bukkit.tc.locator.TrainLocator;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSignStore;
import com.bergerkiller.bukkit.tc.offline.train.OfflineGroup;
import com.bergerkiller.bukkit.tc.offline.train.OfflineGroupManager;
import com.bergerkiller.bukkit.tc.pathfinding.PathProvider;
import com.bergerkiller.bukkit.tc.pathfinding.RouteManager;
import com.bergerkiller.bukkit.tc.portals.PortalProvider;
import com.bergerkiller.bukkit.tc.portals.TCPortalManager;
import com.bergerkiller.bukkit.tc.portals.plugins.MultiversePortalsProvider;
import com.bergerkiller.bukkit.tc.portals.plugins.MyWorldsPortalsProvider;
import com.bergerkiller.bukkit.tc.properties.SavedTrainPropertiesStore;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.IPropertyRegistry;
import com.bergerkiller.bukkit.tc.properties.registry.TCPropertyRegistry;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import com.bergerkiller.bukkit.tc.properties.standard.category.PaperPlayerViewDistanceProperty;
import com.bergerkiller.bukkit.tc.properties.standard.category.PaperTrackingRangeProperty;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.TrackedSignLookup;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionDetector;
import com.bergerkiller.bukkit.tc.signactions.SignActionSpawn;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneCache;
import com.bergerkiller.bukkit.tc.signactions.spawner.SpawnSignManager;
import com.bergerkiller.bukkit.tc.statements.Statement;
import com.bergerkiller.bukkit.tc.tickets.TicketStore;
import com.bergerkiller.bukkit.tc.utils.BlockPhysicsEventDataAccessor;
import com.bergerkiller.generated.net.minecraft.world.item.ItemHandle;
import com.bergerkiller.mountiplex.conversion.Conversion;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.stream.Collectors;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TrainCarts extends PluginBase {
   public static TrainCarts plugin;
   private final LibraryComponentList<TrainCarts> optionalComponents = LibraryComponentList.forPlugin(this);
   private final LibraryComponentList<TrainCarts> criticalComponents = LibraryComponentList.forPlugin(this);
   private final Task autosaveTask = new TrainCarts.AutosaveTask(this);
   private Task cacheCleanupTask;
   private Task mutexZoneUpdateTask;
   private final List<TrainCarts.ChunkPreloadTask> chunkPreloadTasks = new ArrayList();
   private TCPropertyRegistry propertyRegistry;
   private TCListener listener;
   private TCPacketListener packetListener;
   private TCSuppressSeatTeleportPacketListener suppressSeatTeleportPacketListener;
   private TCInteractionPacketListener interactionPacketListener;
   private FileConfiguration config;
   private final SpawnSignManager spawnSignManager = new SpawnSignManager(this);
   private SavedAttachmentModelStore savedAttachmentModels;
   private SavedTrainPropertiesStore savedTrainsStore;
   private SeatAttachmentMap seatAttachmentMap;
   private final TeamProvider teamProvider = new TeamProvider(this);
   private PathProvider pathProvider;
   private RouteManager routeManager;
   private final TrainLocator trainLocator = new TrainLocator(this);
   private TrainUpdateController trainUpdateController = new TrainUpdateController(this);
   private final TCSelectorHandlerRegistry selectorHandlerRegistry = new TCSelectorHandlerRegistry(this);
   private final OfflineGroupManager offlineGroupManager = new OfflineGroupManager(this);
   private final OfflineSignStore offlineSignStore = new OfflineSignStore(this);
   private final ActionRegistry actionRegistry = new ActionRegistry(this);
   private final TrackedSignLookup trackedSignLookup = new TrackedSignLookup(this);
   private final SignController signController = new SignController(this);
   private final TrainCartsAttachmentViewerMap attachmentViewerMap = new TrainCartsAttachmentViewerMap(this);
   private ResourcePackModelListing modelListing = new ResourcePackModelListing();
   private ActionSignHighlighter actionSignHighlighter = null;
   private final WorldEditSchematicLoader worldEditSchematicLoader = new WorldEditSchematicLoader(this);
   private final TrainCartsPlayerStore playerStore = new TrainCartsPlayerStore(this);
   private final EffectLoopPlayerController effectLoopPlayerController = new EffectLoopPlayerController(this);
   private final PlayerClientSynchronizer.Provider playerClientSynchronizerProvider = PlayerClientSynchronizer.Provider.create(this);
   private final PlayerPacketListener.Provider playerPacketListenerProvider = PlayerPacketListener.Provider.create(this);
   private SmoothCoastersAPI smoothCoastersAPI;
   private Commands commands;
   private final SoftDependency<TabNameTagHider> tabNameTagHider = new TabNameTagHiderDependency(this) {
      protected void onEnable() {
         TrainCarts.this.getLogger().info("Neznamy TAB plugin detected! Seats with nametag hidden will also hide TAB nametags.");
      }
   };
   private final SoftDependency<Plugin> signLink = new SoftDependency<Plugin>(this, "SignLink") {
      private Task signtask;
      private Listener variableSuppressionListener = null;

      protected Plugin initialize(Plugin plugin) {
         return plugin;
      }

      protected void onEnable() {
         TrainCarts.this.log(Level.INFO, "SignLink detected, support for arrival signs added!");
         Task.stop(this.signtask);
         this.signtask = new Task(TrainCarts.this) {
            public void run() {
               ArrivalSigns.updateAll();
            }
         };
         this.signtask.start(0L, 10L);
         boolean hasEvent = false;

         try {
            Class.forName("com.bergerkiller.bukkit.sl.API.events.SignVariablesDetectEvent");
            hasEvent = true;
         } catch (Throwable var3) {
         }

         if (hasEvent) {
            this.variableSuppressionListener = this.createVariableSuppressionListener();
            TrainCarts.this.register(this.variableSuppressionListener);
         }

      }

      private Listener createVariableSuppressionListener() {
         return new Listener() {
            @EventHandler(
               priority = EventPriority.LOWEST,
               ignoreCancelled = true
            )
            public void onSignVariablesDetected(SignVariablesDetectEvent event) {
               if (SignActionHeader.parse(event.getLine(0)).isValid()) {
                  event.setCancelled(true);
               }

            }
         };
      }

      protected void onDisable() {
         Task.stop(this.signtask);
         this.signtask = null;
         if (this.variableSuppressionListener != null) {
            CommonUtil.unregisterListener(this.variableSuppressionListener);
            this.variableSuppressionListener = null;
         }

      }
   };
   private final SoftDependency<Plugin> lightAPI = SoftDependency.build(this, "LightAPI").withInitializer((p) -> {
      return p;
   }).whenEnable((p) -> {
      this.log(Level.INFO, "LightAPI detected, the Light attachment is now available");
      AttachmentTypeRegistry.instance().register(CartAttachmentLight.TYPE);
   }).whenDisable((p) -> {
      AttachmentTypeRegistry.instance().unregister(CartAttachmentLight.TYPE);
   }).create();
   private final SoftDependency<MyWorldsPortalsProvider> myWorldsPortalProvider = SoftDependency.build(this, "My_Worlds").withInitializer((p) -> {
      return new MyWorldsPortalsProvider(this, p);
   }).whenEnable((s) -> {
      TCPortalManager.addPortalSupport(s.name(), (PortalProvider)s.get());
   }).whenDisable((s) -> {
      TCPortalManager.removePortalSupport(s.name());
   }).create();
   private final SoftDependency<MultiversePortalsProvider> multiversePortalProvider = SoftDependency.build(this, "Multiverse-Portals").withInitializer((p) -> {
      return new MultiversePortalsProvider(this, p);
   }).whenEnable((s) -> {
      TCPortalManager.addPortalSupport(s.name(), (PortalProvider)s.get());
   }).whenDisable((s) -> {
      TCPortalManager.removePortalSupport(s.name());
   }).create();
   private final SoftServiceDependency<Economy> vaultEconomy = new SoftServiceDependency<Economy>(this, "net.milkbowl.vault.economy.Economy") {
      protected Economy initialize(Object service) throws Error, Exception {
         return (Economy)Economy.class.cast(service);
      }

      protected void onEnable() {
         TrainCarts.this.log(Level.INFO, "Support for Economy plugin '" + this.getServicePlugin().getName() + "' enabled");
      }
   };

   public IPropertyRegistry getPropertyRegistry() {
      return this.propertyRegistry;
   }

   public GlowColorTeamProvider getGlowColorTeamProvider() {
      return this.teamProvider.glowColors();
   }

   public TeamProvider getTeamProvider() {
      return this.teamProvider;
   }

   public SeatAttachmentMap getSeatAttachmentMap() {
      return this.seatAttachmentMap;
   }

   public SpawnSignManager getSpawnSignManager() {
      return this.spawnSignManager;
   }

   public SavedAttachmentModelStore getSavedAttachmentModels() {
      return this.savedAttachmentModels;
   }

   public SavedTrainPropertiesStore getSavedTrains() {
      return this.savedTrainsStore;
   }

   public PathProvider getPathProvider() {
      return this.pathProvider;
   }

   public RouteManager getRouteManager() {
      return this.routeManager;
   }

   public SelectorHandlerRegistry getSelectorHandlerRegistry() {
      return this.selectorHandlerRegistry;
   }

   public TrainLocator getTrainLocator() {
      return this.trainLocator;
   }

   public TrainUpdateController getTrainUpdateController() {
      return this.trainUpdateController;
   }

   public OfflineGroupManager getOfflineGroups() {
      return this.offlineGroupManager;
   }

   public OfflineSignStore getOfflineSigns() {
      return this.offlineSignStore;
   }

   public TrackedSignLookup getTrackedSignLookup() {
      return this.trackedSignLookup;
   }

   public ActionRegistry getActionRegistry() {
      return this.actionRegistry;
   }

   public SignController getSignController() {
      return this.signController;
   }

   public TrainCartsAttachmentViewerMap getAttachmentViewers() {
      return this.attachmentViewerMap;
   }

   public AttachmentViewer getAttachmentViewer(Player player) {
      return this.attachmentViewerMap.getViewer(player);
   }

   public TabNameTagHider.TabPlayerNameTagHider getTabNameHider(Player player) {
      return ((TabNameTagHider)this.tabNameTagHider.get()).get(player);
   }

   public ResourcePackModelListing getModelListing() {
      ResourcePackModelListing listing = this.modelListing;
      if (listing.loadedResourcePack() != TCConfig.resourcePack) {
         listing = new ResourcePackModelListing(this);
         listing.load(TCConfig.resourcePack);
         this.modelListing = listing;
      }

      return listing;
   }

   public WorldEditSchematicLoader getWorldEditSchematicLoader() {
      return this.worldEditSchematicLoader;
   }

   public TrainCartsPlayerStore getPlayerStore() {
      return this.playerStore;
   }

   public TrainCartsPlayer getPlayer(UUID playerUUID) {
      return this.playerStore.get(playerUUID);
   }

   public TrainCartsPlayer getPlayer(Player player) {
      return this.playerStore.get(player);
   }

   public EffectLoopPlayerController getEffectLoopPlayerController() {
      return this.effectLoopPlayerController;
   }

   public EffectLoop.Player createEffectLoopPlayer() {
      return this.effectLoopPlayerController.createPlayer();
   }

   public EffectLoop.Player createEffectLoopPlayer(int limit) {
      return this.effectLoopPlayerController.createPlayer(limit);
   }

   public PlayerClientSynchronizer.Provider getPlayerClientSynchronizerProvider() {
      return this.playerClientSynchronizerProvider;
   }

   public PlayerPacketListener.Provider getPlayerPacketListenerProvider() {
      return this.playerPacketListenerProvider;
   }

   public Economy getEconomy() {
      return (Economy)this.vaultEconomy.get();
   }

   public SmoothCoastersAPI getSmoothCoastersAPI() {
      return this.smoothCoastersAPI;
   }

   public boolean isSignLinkEnabled() {
      return this.signLink.isEnabled();
   }

   public static boolean canBreak(Material type) {
      return TCConfig.allowedBlockBreakTypes.contains(type);
   }

   public static String getCurrencyText(double value) {
      Economy econ = (Economy)plugin.vaultEconomy.get();
      return econ != null ? econ.format(value) : TCConfig.currencyFormat.replace("%value%", Double.toString(value));
   }

   public static String getMessage(String text) {
      return StringUtil.ampToColor(TCConfig.messageShortcuts.replace(text));
   }

   public static void sendMessage(Player player, String text) {
      int startindex;
      String value;
      if (plugin.isSignLinkEnabled()) {
         for(int endindex = 0; (startindex = text.indexOf(37, endindex)) != -1 && (endindex = text.indexOf(37, startindex + 1)) != -1; endindex = startindex + value.length()) {
            String varname = text.substring(startindex + 1, endindex);
            value = varname.isEmpty() ? "%" : Variables.get(varname).get(player.getName());
            text = text.substring(0, startindex) + value + text.substring(endindex + 1);
         }
      }

      player.sendMessage(text);
   }

   public static boolean isWorldDisabled(BlockEvent event) {
      return isWorldDisabled(event.getBlock().getWorld());
   }

   public static boolean isWorldDisabled(Block worldContainer) {
      return isWorldDisabled(worldContainer.getWorld());
   }

   public static boolean isWorldDisabled(World world) {
      if (!TCConfig.enabledWorlds.isEmpty()) {
         return !TCConfig.enabledWorlds.contains(world);
      } else {
         return TCConfig.disabledWorlds.contains(world);
      }
   }

   public static boolean isWorldDisabled(String worldname) {
      if (!TCConfig.enabledWorlds.isEmpty()) {
         return !TCConfig.enabledWorlds.contains(worldname);
      } else {
         return TCConfig.disabledWorlds.contains(worldname);
      }
   }

   public boolean handlePlayerVehicleChange(Player player, Entity newVehicle) {
      try {
         MinecartMember<?> newMinecart = MinecartMemberStore.getFromEntity(newVehicle);
         MinecartMember<?> entered = MinecartMemberStore.getFromEntity(player.getVehicle());
         if (entered != null && !entered.getProperties().getPlayersExit()) {
            return false;
         }

         if (newMinecart != null && !newMinecart.getProperties().getPlayersEnter()) {
            return false;
         }
      } catch (Throwable var5) {
         this.handle(var5);
      }

      return true;
   }

   public void saveShortcuts() {
      TCConfig.messageShortcuts.save(this.config.getNode("messageShortcuts"));
      this.config.save();
   }

   public ItemParser[] getParsers(String key, int amount) {
      ItemParser[] rval = (ItemParser[])TCConfig.parsers.get(key.toLowerCase(Locale.ENGLISH));
      if (rval == null) {
         return new ItemParser[]{ItemParser.parse(key, amount == -1 ? null : Integer.toString(amount))};
      } else {
         rval = (ItemParser[])rval.clone();
         int i;
         if (amount == -1) {
            for(i = 0; i < rval.length; ++i) {
               rval[i] = rval[i].setAmount(-1);
            }
         } else if (amount > 1) {
            for(i = 0; i < rval.length; ++i) {
               rval[i] = rval[i].multiplyAmount(amount);
            }
         }

         return rval;
      }
   }

   public void putParsers(String key, ItemParser[] parsers) {
      TCConfig.putParsers(key, parsers);
   }

   protected void preloadChunks(Map<OfflineGroup, List<ForcedChunk>> chunks) {
      chunks.values().stream().flatMap((list) -> {
         return list.stream();
      }).forEachOrdered((chunk) -> {
         try {
            RailLookup.forWorld(chunk.getWorld());
            chunk.getChunk();
         } catch (Throwable var3) {
            this.getLogger().log(Level.SEVERE, "Failed to load chunk " + chunk.getWorld().getName() + " [" + chunk.getX() + ", " + chunk.getZ() + "]", var3);
         }

      });
      TrainCarts.ChunkPreloadTask preloadTask = new TrainCarts.ChunkPreloadTask(this, chunks);
      preloadTask.startPreloading();
      this.chunkPreloadTasks.add(preloadTask);
   }

   private void loadConfig(boolean isEnabling) {
      this.config = new FileConfiguration(this);
      this.config.load();
      TCConfig.load(this, this.config);
      this.config.save();
      this.autosaveTask.stop().start((long)TCConfig.autoSaveInterval, (long)TCConfig.autoSaveInterval);
      this.modelListing = new ResourcePackModelListing(this);
      this.modelListing.load(TCConfig.resourcePack);
      if (!isEnabling) {
         this.signController.updateEnabled();
         this.actionSignHighlighter.updateEnabled();
      }

   }

   public void loadConfig() {
      this.loadConfig(false);
   }

   public int getMinimumLibVersion() {
      return 12111;
   }

   public void onLoad() {
      this.commands = new Commands();
      this.propertyRegistry = new TCPropertyRegistry(this, this.commands.getHandler());
      this.propertyRegistry.registerAll(StandardProperties.class);
      if (Util.hasPaperViewDistanceSupport()) {
         try {
            this.propertyRegistry.register(PaperPlayerViewDistanceProperty.INSTANCE);
         } catch (Throwable var3) {
            this.getLogger().log(Level.SEVERE, "Failed to register paper player view distance property", var3);
         }
      }

      if (Util.hasPaperCustomTrackingRangeSupport()) {
         try {
            this.propertyRegistry.register(PaperTrackingRangeProperty.INSTANCE);
         } catch (Throwable var2) {
            this.getLogger().log(Level.SEVERE, "Failed to register paper tracking range property", var2);
         }
      }

      CartAttachment.registerDefaultAttachments();
      RailType.values();
      DetectorRegion.init(this);
      SignAction.init();
      this.offlineSignStore.load();
      MutexZoneCache.init(this);
      this.spawnSignManager.load();
      SignActionDetector.INSTANCE.enable(this);
      this.pathProvider = new PathProvider(this, this.getDataFolder() + File.separator + "destinations.dat");
      plugin = this;
   }

   public void enable() {
      plugin = this;
      CommonEntity.forceControllerInitialization();
      Conversion.registerConverters(MinecartMemberStore.class);
      this.commands.enable(this);
      this.criticalComponents.enable(this.propertyRegistry);
      this.criticalComponents.enable(this.selectorHandlerRegistry);
      this.criticalComponents.enable(this.effectLoopPlayerController);
      this.loadConfig(true);
      SoftDependency.detectAll(this);
      if (TCConfig.maxMinecartStackSize != 1) {
         Material[] var1 = MaterialsByName.getAllMaterials();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Material material = var1[var3];
            if (MaterialUtil.ISMINECART.get(material)) {
               ItemHandle.createHandle(HandleConversion.toItemHandle(material)).setMaxStackSize(TCConfig.maxMinecartStackSize);
            }
         }
      }

      this.criticalComponents.enable(this.worldEditSchematicLoader);
      this.optionalComponents.enable(this.signController);
      this.criticalComponents.enable(this.offlineSignStore);
      this.criticalComponents.enable(this.teamProvider);
      this.criticalComponents.enable(this.trainLocator);
      this.routeManager = (RouteManager)this.optionalComponents.enable(new RouteManager(this.getDataFolder() + File.separator + "routes.yml"));
      this.smoothCoastersAPI = new SmoothCoastersAPI(this);
      this.seatAttachmentMap = new SeatAttachmentMap();
      this.register(this.seatAttachmentMap, SeatAttachmentMap.LISTENED_TYPES);
      Statement.init();
      this.optionalComponents.enable(this.pathProvider);
      this.optionalComponents.enable(this.trainUpdateController);
      TrainProperties.load((TrainCarts)this);
      TicketStore.load(this);
      this.savedAttachmentModels = SavedAttachmentModelStore.create(this, "SavedModels.yml", "savedModelModules");
      this.savedTrainsStore = SavedTrainPropertiesStore.create(this, "SavedTrainProperties.yml", "savedTrainModules");
      this.offlineGroupManager.load();
      MinecartMemberStore.convertAllAutomatically(this);
      ArrivalSigns.init(this.getDataFolder() + File.separator + "arrivaltimes.txt");
      this.cacheCleanupTask = (new TrainCarts.CacheCleanupTask(this)).start(1L, 1L);
      RailLookup.forceRecalculation();
      this.mutexZoneUpdateTask = (new TrainCarts.MutexZoneUpdateTask(this)).start(1L, 1L);
      this.spawnSignManager.enable();
      CommonUtil.nextTick(new Runnable() {
         public void run() {
            Iterator var1 = WorldUtil.getWorlds().iterator();

            while(var1.hasNext()) {
               World world = (World)var1.next();
               OfflineGroupManager.removeBuggedMinecarts(world);
            }

         }
      });
      this.register(this.packetListener = new TCPacketListener(this), TCPacketListener.LISTENED_TYPES);
      this.register(this.interactionPacketListener = new TCInteractionPacketListener(this.packetListener), TCInteractionPacketListener.TYPES);
      this.register(this.listener = new TCListener(this));
      this.register(new TCSeatChangeListener());
      this.register(new TrainChestListener(this));
      this.optionalComponents.enable(this.playerClientSynchronizerProvider);
      this.optionalComponents.enable(this.playerPacketListenerProvider);
      if (CommonCapabilities.HAS_DISPLAY_ENTITY && Common.hasCapability("Common:Block:RayTraceUtilImprovements") && Common.hasCapability("Common:BlockData:GetInteractableBox")) {
         this.actionSignHighlighter = (ActionSignHighlighter)this.optionalComponents.enable(new ActionSignHighlighter(this));
      }

      if (TCSuppressSeatTeleportPacketListener.SUPPRESS_POST_ENTER_PLAYER_POSITION_PACKET) {
         this.suppressSeatTeleportPacketListener = new TCSuppressSeatTeleportPacketListener(this);
         this.register(this.suppressSeatTeleportPacketListener, TCSuppressSeatTeleportPacketListener.LISTENED_TYPES);
         this.register(this.suppressSeatTeleportPacketListener);
      }

      this.log(Level.INFO, "Restoring trains and loading nearby chunks...");
      this.offlineGroupManager.refresh();
      this.preloadChunks(this.offlineGroupManager.getForceLoadedChunks());
      DetectorRegion.detectAllMinecarts();
      if (Util.hasPaperViewDistanceSupport()) {
         try {
            PaperPlayerViewDistanceProperty.INSTANCE.enable(this);
         } catch (Throwable var6) {
            this.getLogger().log(Level.SEVERE, "Failed to enable paper player view distance property", var6);
            this.propertyRegistry.unregister(PaperPlayerViewDistanceProperty.INSTANCE);
         }
      }

      if (Util.hasPaperCustomTrackingRangeSupport()) {
         try {
            PaperTrackingRangeProperty.INSTANCE.enable(this);
         } catch (Throwable var5) {
            this.getLogger().log(Level.SEVERE, "Failed to enable paper tracking range property", var5);
            this.propertyRegistry.unregister(PaperTrackingRangeProperty.INSTANCE);
         }
      }

      if (TCConfig.destroyAllOnShutdown) {
         this.offlineGroupManager.destroyAllAsync(false).thenAccept((count) -> {
            this.getLogger().info("[DestroyOnShutdown] Destroyed " + count + " trains");
         });
      }

      if (this.hasMetrics()) {
         Metrics metrics = this.getMetrics();
         metrics.addCustomChart(new DrilldownPie("smoothCoastersInstalled", () -> {
            Map<String, Integer> versions = new HashMap();
            int disabled = 0;
            Iterator var3 = Bukkit.getOnlinePlayers().iterator();

            while(var3.hasNext()) {
               Player player = (Player)var3.next();
               if (this.smoothCoastersAPI.isEnabled(player)) {
                  String version = this.smoothCoastersAPI.getModVersion(player);
                  if (version == null) {
                     version = "unknown";
                  }

                  versions.merge(version, 1, Integer::sum);
               } else {
                  ++disabled;
               }
            }

            Map<String, Map<String, Integer>> categories = new HashMap();
            categories.put("installed", versions);
            categories.put("not installed", Collections.singletonMap("not installed", disabled));
            return categories;
         }));
      }

      this.trainUpdateController.startUpdatingAttachments();
   }

   public void disable() {
      Iterator var2;
      MinecartGroup mg;
      if (TCConfig.destroyAllOnShutdown) {
         ImplicitlySharedSet groups = MinecartGroupStore.getGroups().clone();

         try {
            var2 = groups.iterator();

            while(true) {
               if (!var2.hasNext()) {
                  this.getLogger().info("[DestroyOnShutdown] Destroyed " + groups.size() + " trains");
                  break;
               }

               mg = (MinecartGroup)var2.next();
               mg.destroy();
            }
         } catch (Throwable var25) {
            if (groups != null) {
               try {
                  groups.close();
               } catch (Throwable var20) {
                  var25.addSuppressed(var20);
               }
            }

            throw var25;
         }

         if (groups != null) {
            groups.close();
         }
      }

      if (Util.hasPaperViewDistanceSupport()) {
         try {
            PaperPlayerViewDistanceProperty.INSTANCE.disable(this);
         } catch (Throwable var23) {
            this.getLogger().log(Level.SEVERE, "Failed to disable paper player view distance property", var23);
         }
      }

      if (Util.hasPaperCustomTrackingRangeSupport()) {
         try {
            PaperTrackingRangeProperty.INSTANCE.disable(this);
         } catch (Throwable var22) {
            this.getLogger().log(Level.SEVERE, "Failed to disable paper tracking range property", var22);
         }
      }

      try {
         ResourcePackModelListing.closeAllDialogs();
      } catch (Throwable var21) {
         this.getLogger().log(Level.SEVERE, "Failed to shut down all open resource pack model dialogs");
      }

      this.unregister(this.packetListener);
      this.unregister(this.interactionPacketListener);
      this.smoothCoastersAPI.unregister();
      this.listener = null;
      this.packetListener = null;
      this.interactionPacketListener = null;
      this.smoothCoastersAPI = null;
      FakePlayerSpawner.runAndClearCleanupTasks();
      Task.stop(this.autosaveTask);
      Task.stop(this.cacheCleanupTask);
      Task.stop(this.mutexZoneUpdateTask);
      Iterator var26 = this.chunkPreloadTasks.iterator();

      while(var26.hasNext()) {
         TrainCarts.ChunkPreloadTask preloadTask = (TrainCarts.ChunkPreloadTask)var26.next();
         preloadTask.abortPreloading();
      }

      if (TCConfig.maxMinecartStackSize != 1) {
         Material[] var27 = MaterialsByName.getAllMaterials();
         int var30 = var27.length;

         for(int var32 = 0; var32 < var30; ++var32) {
            Material material = var27[var32];
            if (MaterialUtil.ISMINECART.get(material)) {
               ItemHandle.createHandle(HandleConversion.toItemHandle(material)).setMaxStackSize(1);
            }
         }
      }

      MinecartGroupStore.doPostMoveLogic();
      if (!Common.hasCapability("Common:EntityController:isPlayerTakeable")) {
         var26 = WorldUtil.getWorlds().iterator();

         while(var26.hasNext()) {
            World world = (World)var26.next();
            Iterator var33 = WorldUtil.getChunks(world).iterator();

            while(var33.hasNext()) {
               Chunk chunk = (Chunk)var33.next();
               Iterator var5 = ChunkUtil.getEntities(chunk).iterator();

               while(var5.hasNext()) {
                  Entity entity = (Entity)var5.next();
                  if (entity instanceof Minecart) {
                     CommonEntity<?> commonEntity = CommonEntity.get(entity);
                     if (commonEntity.hasPlayerPassenger()) {
                        MinecartMember<?> member = (MinecartMember)commonEntity.getController(MinecartMember.class);
                        if (member != null && !member.isPlayerTakeable()) {
                           commonEntity.eject();
                        }
                     }
                  }
               }
            }
         }
      }

      ArrayList allForcedChunks = new ArrayList();
      boolean var19 = false;

      try {
         var19 = true;
         var2 = MinecartGroup.getGroups().cloneAsIterable().iterator();

         while(var2.hasNext()) {
            mg = (MinecartGroup)var2.next();
            mg.getChunkArea().getForcedChunks(allForcedChunks);
            mg.unload();
         }

         var2 = WorldUtil.getWorlds().iterator();

         while(var2.hasNext()) {
            World world = (World)var2.next();
            Iterator var36 = WorldUtil.getChunks(world).iterator();

            while(var36.hasNext()) {
               Chunk chunk = (Chunk)var36.next();
               Iterator var39 = ChunkUtil.getEntities(chunk).iterator();

               while(var39.hasNext()) {
                  Entity entity = (Entity)var39.next();
                  if (!entity.isDead()) {
                     MinecartGroup group = MinecartGroup.get(entity);
                     if (group != null) {
                        group.unload();
                     }

                     if (entity instanceof Minecart) {
                        CommonEntity<?> commonEntity = CommonEntity.get(entity);
                        if (commonEntity.getController(MinecartMember.class) != null) {
                           commonEntity.setController(new DefaultEntityController());
                        }
                     }
                  }
               }
            }
         }

         var19 = false;
      } finally {
         if (var19) {
            Iterator var11 = allForcedChunks.iterator();

            while(var11.hasNext()) {
               ForcedChunk forcedChunk = (ForcedChunk)var11.next();
               forcedChunk.close();
            }

            allForcedChunks.clear();
         }
      }

      var2 = allForcedChunks.iterator();

      while(var2.hasNext()) {
         ForcedChunk forcedChunk = (ForcedChunk)var2.next();
         forcedChunk.close();
      }

      allForcedChunks.clear();
      this.save(TrainCarts.SaveMode.SHUTDOWN);
      ArrivalSigns.deinit();
      SignActionSpawn.deinit();
      Statement.deinit();
      SignAction.deinit();
      ItemAnimation.deinit();
      this.offlineGroupManager.deinit();
      RailLookup.clear();
      this.optionalComponents.disable();
      this.pathProvider = null;
      this.undoAllTCControllers();
      AttachmentTypeRegistry.instance().unregisterAll();
      MutexZoneCache.deinit(this);
      this.spawnSignManager.disable();
      SignActionDetector.INSTANCE.disable(this);
      this.criticalComponents.disable();
   }

   private void undoAllTCControllers() {
      List<Entity> entities = new ArrayList();
      Iterator var2 = WorldUtil.getWorlds().iterator();

      label27:
      while(var2.hasNext()) {
         World world = (World)var2.next();
         Iterator var4 = WorldUtil.getEntities(world).iterator();

         while(true) {
            Entity entity;
            CommonEntity ce;
            do {
               if (!var4.hasNext()) {
                  continue label27;
               }

               entity = (Entity)var4.next();
               ce = CommonEntity.get(entity);
            } while(ce.getController(MinecartMember.class) == null && !(ce.getNetworkController() instanceof MinecartMemberNetwork));

            entities.add(entity);
         }
      }

      entities.forEach(CommonEntity::clearControllers);
   }

   public void redetectSignActions() {
      this.getSignController().redetectSignActions();
      RailLookup.redetectSignActions();
      Iterator var1 = MinecartGroupStore.getGroups().iterator();

      while(var1.hasNext()) {
         MinecartGroup group = (MinecartGroup)var1.next();
         group.getSignTracker().updatePosition();
      }

   }

   public void save(TrainCarts.SaveMode saveMode) {
      boolean autosave = saveMode.isAutoSave();
      TrainProperties.save(autosave);
      this.savedAttachmentModels.save(autosave);
      this.savedTrainsStore.save(autosave);
      TicketStore.save(this, autosave);
      this.pathProvider.save(autosave, this.getDataFolder() + File.separator + "destinations.dat");
      if (!autosave) {
         ArrivalSigns.save(this.getDataFolder() + File.separator + "arrivaltimes.txt");
      }

      DetectorRegion.save(this, autosave);
      this.routeManager.save(autosave);
      this.offlineGroupManager.save(saveMode);
   }

   public void setBlockDataWithoutBreaking(Block block, BlockData blockData) {
      if (Common.evaluateMCVersion(">=", "1.19")) {
         WorldUtil.setBlockDataFast(block, blockData);
         WorldUtil.queueBlockSend(block);
         this.applyBlockPhysics(block, blockData);
      } else {
         WorldUtil.setBlockData(block, blockData);
      }

   }

   public void applyBlockPhysics(Block block, BlockData blockData) {
      if (Common.evaluateMCVersion(">=", "1.19")) {
         this.listener.onBlockPhysics(BlockPhysicsEventDataAccessor.INSTANCE.createEvent(block, blockData));
         BlockFace[] var3 = FaceUtil.BLOCK_SIDES;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            BlockFace face = var3[var5];
            this.listener.onBlockPhysics(BlockPhysicsEventDataAccessor.INSTANCE.createEvent(block.getRelative(face), blockData));
         }
      } else {
         BlockUtil.applyPhysics(block, blockData.getType());
      }

   }

   public boolean command(CommandSender sender, String cmd, String[] args) {
      return false;
   }

   public void localization() {
      this.loadLocales(Localization.class);
   }

   public void permissions() {
      this.loadPermissions(Permission.class);
   }

   private static class AutosaveTask extends Task {
      public AutosaveTask(TrainCarts plugin) {
         super(plugin);
      }

      public void run() {
         ((TrainCarts)this.getPlugin()).save(TrainCarts.SaveMode.AUTOSAVE);
      }
   }

   private static class ChunkPreloadTask extends Task {
      private final Map<OfflineGroup, List<ForcedChunk>> chunks;
      private final List<TrainCarts.ChunkPreloadTask.FinishedChunks> finished = new ArrayList();
      private int deadline;

      public ChunkPreloadTask(JavaPlugin plugin, Map<OfflineGroup, List<ForcedChunk>> chunks) {
         super(plugin);
         this.chunks = chunks;
      }

      public void startPreloading() {
         this.start(5L, 5L);
         this.deadline = CommonUtil.getServerTicks() + 12000;
      }

      public void abortPreloading() {
         this.stop();
         this.finished.forEach(TrainCarts.ChunkPreloadTask.FinishedChunks::close);
         this.finished.clear();
         this.chunks.values().forEach((chunks) -> {
            chunks.forEach(ForcedChunk::close);
         });
         this.chunks.clear();
      }

      public void run() {
         if (this.finished.isEmpty() && this.chunks.isEmpty()) {
            ((TrainCarts)this.getPlugin()).chunkPreloadTasks.remove(this);
            this.stop();
         } else {
            int ticks = CommonUtil.getServerTicks();
            if (!this.chunks.isEmpty() && ticks > this.deadline) {
               List<String> trainNames = (List)this.chunks.keySet().stream().map((g) -> {
                  return g.name;
               }).collect(Collectors.toList());
               this.getPlugin().getLogger().log(Level.SEVERE, "Failed to restore " + trainNames.size() + " keep-chunks-loaded trains in time!");
               if (trainNames.size() < 10) {
                  this.getPlugin().getLogger().log(Level.SEVERE, "Trains: " + StringUtil.combineNames(trainNames));
               }

               this.abortPreloading();
            } else {
               Iterator iter = this.finished.iterator();

               while(iter.hasNext()) {
                  TrainCarts.ChunkPreloadTask.FinishedChunks chunks = (TrainCarts.ChunkPreloadTask.FinishedChunks)iter.next();
                  if (ticks > chunks.deadline) {
                     chunks.close();
                     iter.remove();
                  }
               }

               iter = this.chunks.entrySet().iterator();

               while(iter.hasNext()) {
                  Entry<OfflineGroup, List<ForcedChunk>> entry = (Entry)iter.next();
                  if (((OfflineGroup)entry.getKey()).isLoadedAsGroup()) {
                     this.finished.add(new TrainCarts.ChunkPreloadTask.FinishedChunks((List)entry.getValue()));
                     iter.remove();
                  }
               }

            }
         }
      }

      private static class FinishedChunks implements AutoCloseable {
         public final List<ForcedChunk> chunks;
         public final int deadline;

         public FinishedChunks(List<ForcedChunk> chunks) {
            this.chunks = chunks;
            this.deadline = CommonUtil.getServerTicks() + 10;
         }

         public void close() {
            this.chunks.forEach(ForcedChunk::close);
            this.chunks.clear();
         }
      }
   }

   private static class CacheCleanupTask extends Task {
      public CacheCleanupTask(JavaPlugin plugin) {
         super(plugin);
      }

      public void run() {
         RailLookup.update();
      }
   }

   private static class MutexZoneUpdateTask extends Task {
      public MutexZoneUpdateTask(JavaPlugin plugin) {
         super(plugin);
      }

      public void run() {
         MutexZoneCache.refreshAll();
      }
   }

   public static enum SaveMode {
      AUTOSAVE,
      COMMAND,
      SHUTDOWN;

      public boolean isAutoSave() {
         return this == AUTOSAVE;
      }

      // $FF: synthetic method
      private static TrainCarts.SaveMode[] $values() {
         return new TrainCarts.SaveMode[]{AUTOSAVE, COMMAND, SHUTDOWN};
      }
   }

   public interface Provider {
      TrainCarts getTrainCarts();
   }
}
