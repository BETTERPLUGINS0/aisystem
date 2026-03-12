package ac.grim.grimac.shaded.io.github.retrooper.packetevents.factory.spigot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.injector.ChannelInjector;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.player.PlayerManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.NettyManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.settings.PacketEventsSettings;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.LogManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PEVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.SynchronizedRegistriesHandler;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.updatechecker.UpdateChecker;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bstats.bukkit.Metrics;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bstats.charts.SimplePie;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalBukkitListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalBukkitLoginListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalGlobalBukkitListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalPaperJoinListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalPaperListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.InternalBukkitPacketListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.protocol.ProtocolManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.netty.NettyManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.BukkitLogManager;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpigotPacketEventsBuilder {
   private static PacketEventsAPI<Plugin> API_INSTANCE;

   public static void clearBuildCache() {
      API_INSTANCE = null;
   }

   public static PacketEventsAPI<Plugin> build(Plugin plugin) {
      if (API_INSTANCE == null) {
         API_INSTANCE = buildNoCache(plugin);
      }

      return API_INSTANCE;
   }

   public static PacketEventsAPI<Plugin> build(Plugin plugin, PacketEventsSettings settings) {
      if (API_INSTANCE == null) {
         API_INSTANCE = buildNoCache(plugin, settings);
      }

      return API_INSTANCE;
   }

   public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin) {
      return buildNoCache(plugin, new PacketEventsSettings());
   }

   public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin, PacketEventsSettings inSettings) {
      return new PacketEventsAPI<Plugin>() {
         private final PacketEventsSettings settings = inSettings;
         private final ProtocolManager protocolManager = new ProtocolManagerImpl();
         private final ServerManager serverManager = new ServerManagerImpl();
         private final PlayerManager playerManager = new PlayerManagerImpl();
         private final NettyManager nettyManager = new NettyManagerImpl();
         private final SpigotChannelInjector injector = new SpigotChannelInjector();
         private final LogManager logManager = new BukkitLogManager();
         private boolean loaded;
         private boolean initialized;
         private boolean lateBind = false;
         private boolean terminated = false;

         public void load() {
            if (!this.loaded) {
               String id = plugin.getName().toLowerCase(Locale.ROOT);
               PacketEvents.IDENTIFIER = "pe-" + id;
               PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
               PacketEvents.DECODER_NAME = "pe-decoder-" + id;
               PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
               PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;
               PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + id;

               try {
                  SpigotReflectionUtil.init();
                  CustomPipelineUtil.init();
                  WrappedBlockState.ensureLoad();
                  SynchronizedRegistriesHandler.init();
               } catch (Exception var3) {
                  throw new IllegalStateException(var3);
               }

               PacketType.prepare();
               this.lateBind = !this.injector.isServerBound();
               if (!this.lateBind) {
                  this.injector.inject();
               }

               this.loaded = true;
               this.getEventManager().registerListener(new InternalBukkitPacketListener());
            }

         }

         public boolean isLoaded() {
            return this.loaded;
         }

         public void init() {
            this.load();
            if (!this.initialized) {
               Plugin pluginx = (Plugin)PacketEvents.getAPI().getPlugin();
               String bukkitVersion = Bukkit.getBukkitVersion();
               AtomicBoolean stopping = new AtomicBoolean(false);
               BiConsumer<PEVersion, UpdateChecker.UpdateCheckerStatus> unsupportedSoftwareLogic = (peVersion, status) -> {
                  if (bukkitVersion.contains("Unknown")) {
                     ServerVersion fallbackVersion = ServerVersion.V_1_8_8;
                     String failureToDetectVersionMsg = "Your server software is preventing us from checking the Minecraft Server version. This is what we found: " + bukkitVersion + ". We will assume the Server version is " + fallbackVersion.name() + "...\n If you need assistance, join our Discord server: https://discord.gg/DVHxPPxHZc";
                     pluginx.getLogger().warning(failureToDetectVersionMsg);
                  } else {
                     PEVersion bukkitServerVersion = PEVersion.fromString(bukkitVersion.substring(0, bukkitVersion.indexOf("-")));
                     PEVersion latestSupportedVersion = PEVersion.fromString(ServerVersion.getLatest().getReleaseName());
                     if (bukkitServerVersion.isNewerThan(latestSupportedVersion)) {
                        String developmentBuildsMsg = "Please test the development builds, as they may already have support for your Minecraft version (hint: select the build that contains 'spigot'): https://ci.codemc.io/job/retrooper/job/packetevents";
                        String releaseBuildsMsg = "Please test the latest stable release, as it should already have support for your Minecraft version: https://modrinth.com/plugin/packetevents";
                        String newBuildsMsg = status != UpdateChecker.UpdateCheckerStatus.OUTDATED && status != UpdateChecker.UpdateCheckerStatus.FAILED && status != null ? developmentBuildsMsg : releaseBuildsMsg;
                        pluginx.getLogger().warning("Your build of PacketEvents does not support the Minecraft version " + bukkitServerVersion + "! The latest Minecraft version supported by your build of PacketEvents is " + latestSupportedVersion + ". " + newBuildsMsg + " If you're in need of any help, join our Discord server: https://discord.gg/DVHxPPxHZc");
                        Bukkit.getPluginManager().disablePlugin(pluginx);
                        stopping.set(true);
                     }
                  }

               };
               if (this.settings.shouldCheckForUpdates()) {
                  this.getUpdateChecker().handleUpdateCheck(unsupportedSoftwareLogic);
               } else {
                  unsupportedSoftwareLogic.accept((Object)null, (Object)null);
               }

               if (stopping.get()) {
                  return;
               }

               Metrics metrics = new Metrics(pluginx, 11327);
               metrics.addCustomChart(new SimplePie("packetevents_version", () -> {
                  return this.getVersion().toStringWithoutSnapshot();
               }));
               Bukkit.getPluginManager().registerEvents(new InternalGlobalBukkitListener(), pluginx);

               try {
                  Class.forName("io.papermc.paper.connection.PlayerConnection");
                  if (this.serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
                     Bukkit.getPluginManager().registerEvents(new InternalPaperJoinListener(pluginx), pluginx);
                  } else {
                     Bukkit.getPluginManager().registerEvents(new InternalPaperListener(pluginx), pluginx);
                  }
               } catch (ClassNotFoundException var10) {
                  if (this.serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                     Bukkit.getPluginManager().registerEvents(new InternalBukkitLoginListener(), pluginx);
                  } else {
                     Bukkit.getPluginManager().registerEvents(new InternalBukkitListener(pluginx), pluginx);
                  }
               }

               if (this.lateBind) {
                  Runnable lateBindTask = () -> {
                     if (this.injector.isServerBound()) {
                        this.injector.inject();
                     }

                  };
                  FoliaScheduler.runTaskOnInit(pluginx, lateBindTask);
               }

               if (!"true".equalsIgnoreCase(System.getenv("PE_IGNORE_INCOMPATIBILITY"))) {
                  this.checkCompatibility();
               }

               Iterator var11 = Bukkit.getOnlinePlayers().iterator();

               while(var11.hasNext()) {
                  Player player = (Player)var11.next();
                  User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
                  SpigotChannelInjector injector = (SpigotChannelInjector)PacketEvents.getAPI().getInjector();
                  injector.updatePlayer(user, player);
               }

               this.initialized = true;
            }

         }

         private void checkCompatibility() {
            ViaVersionUtil.checkIfViaIsPresent();
            ProtocolSupportUtil.checkIfProtocolSupportIsPresent();
            Plugin viaPlugin = Bukkit.getPluginManager().getPlugin("ViaVersion");
            int majorVersion;
            if (viaPlugin != null) {
               String[] ver = viaPlugin.getDescription().getVersion().split("\\.", 3);
               majorVersion = Integer.parseInt(ver[0]);
               int minor = Integer.parseInt(ver[1]);
               if (majorVersion < 4 || majorVersion == 4 && minor < 5) {
                  PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a ViaVersion older than 4.5.0, please update your ViaVersion!");
                  Plugin ourPluginx = this.getPlugin();
                  Bukkit.getPluginManager().disablePlugin(ourPluginx);
                  throw new IllegalStateException("ViaVersion incompatibility! Update to v4.5.0 or newer!");
               }
            }

            Plugin protocolLibPlugin = Bukkit.getPluginManager().getPlugin("ProtocolLib");
            if (protocolLibPlugin != null) {
               majorVersion = Integer.parseInt(protocolLibPlugin.getDescription().getVersion().split("\\.", 2)[0]);
               if (majorVersion < 5) {
                  PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a ProtocolLib version older than v5.0.0. This is no longer works, please update to their dev builds. https://ci.dmulloy2.net/job/ProtocolLib/lastBuild/");
                  Plugin ourPlugin = this.getPlugin();
                  Bukkit.getPluginManager().disablePlugin(ourPlugin);
                  throw new IllegalStateException("ProtocolLib incompatibility! Update to v5.0.0 or newer!");
               }
            }

         }

         public boolean isInitialized() {
            return this.initialized;
         }

         public void terminate() {
            if (this.initialized) {
               this.injector.uninject();
               Iterator var1 = this.protocolManager.getUsers().iterator();

               while(var1.hasNext()) {
                  User user = (User)var1.next();
                  ServerConnectionInitializer.destroyHandlers(user.getChannel());
               }

               this.getEventManager().unregisterAllListeners();
               this.initialized = false;
               this.terminated = true;
            }

         }

         public boolean isTerminated() {
            return this.terminated;
         }

         public Plugin getPlugin() {
            return plugin;
         }

         public ProtocolManager getProtocolManager() {
            return this.protocolManager;
         }

         public ServerManager getServerManager() {
            return this.serverManager;
         }

         public PlayerManager getPlayerManager() {
            return this.playerManager;
         }

         public PacketEventsSettings getSettings() {
            return this.settings;
         }

         public NettyManager getNettyManager() {
            return this.nettyManager;
         }

         public ChannelInjector getInjector() {
            return this.injector;
         }

         public LogManager getLogManager() {
            return this.logManager;
         }
      };
   }
}
