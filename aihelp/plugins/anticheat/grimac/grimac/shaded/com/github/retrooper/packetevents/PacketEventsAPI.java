package ac.grim.grimac.shaded.com.github.retrooper.packetevents;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.EventManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.injector.ChannelInjector;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.player.PlayerManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.registry.ItemRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.registry.RegistryManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.NettyManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.settings.PacketEventsSettings;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.LogManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PEVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PEVersions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.updatechecker.UpdateChecker;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.logging.Logger;

public abstract class PacketEventsAPI<T> {
   private final EventManager eventManager = new EventManager();
   private final PacketEventsSettings settings = new PacketEventsSettings();
   private final UpdateChecker updateChecker = new UpdateChecker();
   private final LogManager logManager = new LogManager();
   private static final Logger LOGGER = Logger.getLogger(PacketEventsAPI.class.getName());

   public EventManager getEventManager() {
      return this.eventManager;
   }

   public PacketEventsSettings getSettings() {
      return this.settings;
   }

   public UpdateChecker getUpdateChecker() {
      return this.updateChecker;
   }

   public PEVersion getVersion() {
      return PEVersions.CURRENT;
   }

   public Logger getLogger() {
      return LOGGER;
   }

   public LogManager getLogManager() {
      return this.logManager;
   }

   public abstract void load();

   public abstract boolean isLoaded();

   public abstract void init();

   public abstract boolean isInitialized();

   public abstract void terminate();

   public abstract boolean isTerminated();

   public abstract T getPlugin();

   public abstract ServerManager getServerManager();

   public abstract ProtocolManager getProtocolManager();

   public abstract PlayerManager getPlayerManager();

   public abstract NettyManager getNettyManager();

   public abstract ChannelInjector getInjector();

   public RegistryManager getRegistryManager() {
      return new RegistryManager() {
         public ItemRegistry getItemRegistry() {
            return new ItemRegistry() {
               @Nullable
               public ItemType getByName(String name) {
                  return null;
               }

               @Nullable
               public ItemType getById(int id) {
                  return null;
               }
            };
         }
      };
   }
}
