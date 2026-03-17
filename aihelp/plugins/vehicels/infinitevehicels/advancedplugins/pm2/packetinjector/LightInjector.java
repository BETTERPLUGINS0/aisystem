package advancedplugins.pm2.packetinjector;

import advancedplugins.pm2.cv.api.enums.MinecraftVersion;
import com.mojang.authlib.GameProfile;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public abstract class LightInjector {
   private static final String COMPLETE_VERSION = MinecraftVersion.isPaper() ? MinecraftVersion.getVersionFullRaw() : Bukkit.getServer().getClass().getName().split("\\.")[3];
   private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
   private static final Class<?> SERVER_CLASS = getNMSClass("MinecraftServer", "server");
   private static final Class<?> SERVER_CONNECTION_CLASS = getNMSClass("ServerConnection", "server.network", "ServerConnectionListener");
   private static final Class<?> NETWORK_MANAGER_CLASS = getNMSClass("NetworkManager", "network", "Connection");
   private static final Class<?> ENTITY_PLAYER_CLASS = getNMSClass("EntityPlayer", "server.level", "ServerPlayer");
   private static final Class<?> PLAYER_CONNECTION_CLASS = getNMSClass("PlayerConnection", "server.network", "ServerGamePacketListenerImpl");
   private static final Class<?> PACKET_LOGIN_OUT_SUCCESS_CLASS = getNMSClass("PacketLoginOutSuccess", "network.protocol.login", "ClientboundLoginFinishedPacket");
   private static final Field NMS_SERVER;
   private static final Field NMS_SERVER_CONNECTION;
   private static final Field NMS_NETWORK_MANAGERS_LIST;
   @Nullable
   private static final Field NMS_PENDING_NETWORK_MANAGERS;
   private static final Field NMS_CHANNEL_FROM_NM;
   private static final Field GAME_PROFILE_FROM_PACKET;
   private static final Field GET_PLAYER_CONNECTION;
   private static final Field GET_NETWORK_MANAGER;
   private static final Method GET_PLAYER_HANDLE;
   private static int ID;
   private final Plugin plugin;
   private final String identifier;
   private final List<?> networkManagers;
   @Nullable
   private final Iterable<?> pendingNetworkManagers;
   private final LightInjector.EventListener listener = new LightInjector.EventListener();
   private final AtomicBoolean closed = new AtomicBoolean(false);
   private final Map<UUID, Player> playerCache = Collections.synchronizedMap(new HashMap());
   private final Set<Channel> injectedChannels = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap()));

   public LightInjector(@NotNull Plugin plugin) {
      if (!Bukkit.isPrimaryThread()) {
         throw new IllegalStateException("must be constructed on the main thread.");
      } else if (!((Plugin)Objects.requireNonNull(var1, "Plugin is null.")).isEnabled()) {
         throw new IllegalArgumentException("Plugin " + var1.getName() + " is not enabled");
      } else {
         this.plugin = var1;
         String var10001 = (String)Objects.requireNonNull(this.getIdentifier(), "getIdentifier() returned a null value.");
         this.identifier = var10001 + "-" + ID++;

         try {
            Object var2 = NMS_SERVER_CONNECTION.get(NMS_SERVER.get(Bukkit.getServer()));
            if (var2 == null) {
               throw new RuntimeException("ServerConnection is null.");
            }

            this.networkManagers = (List)NMS_NETWORK_MANAGERS_LIST.get(var2);
            if (NMS_PENDING_NETWORK_MANAGERS != null) {
               this.pendingNetworkManagers = (Iterable)NMS_PENDING_NETWORK_MANAGERS.get(var2);
            } else {
               this.pendingNetworkManagers = null;
            }
         } catch (Throwable var6) {
            throw new RuntimeException("An error occurred while injecting.", var6);
         }

         Bukkit.getPluginManager().registerEvents(this.listener, var1);
         Iterator var7 = Bukkit.getOnlinePlayers().iterator();

         while(var7.hasNext()) {
            Player var3 = (Player)var7.next();

            try {
               this.injectPlayer(var3);
            } catch (Throwable var5) {
               var1.getLogger().log(Level.SEVERE, "An error occurred while injecting a player:", var5);
            }
         }

      }
   }

   @Nullable
   protected abstract Object onPacketReceiveAsync(@Nullable Player sender, @NotNull Channel channel, @NotNull Object packet);

   @Nullable
   protected abstract Object onPacketSendAsync(@Nullable Player receiver, @NotNull Channel channel, @NotNull Object packet);

   public final void sendPacket(@NotNull Player receiver, @NotNull Object packet) {
      Objects.requireNonNull(var1, "Player is null.");
      Objects.requireNonNull(var2, "Packet is null.");
      this.sendPacket(this.getChannel(var1), var2);
   }

   public final void sendPacket(@NotNull Channel channel, @NotNull Object packet) {
      Objects.requireNonNull(var1, "Channel is null.");
      Objects.requireNonNull(var2, "Packet is null.");
      var1.pipeline().writeAndFlush(var2);
   }

   public final void receivePacket(@NotNull Player sender, @NotNull Object packet) {
      Objects.requireNonNull(var1, "Player is null.");
      Objects.requireNonNull(var2, "Packet is null.");
      this.receivePacket(this.getChannel(var1), var2);
   }

   public final void receivePacket(@NotNull Channel channel, @NotNull Object packet) {
      Objects.requireNonNull(var1, "Channel is null.");
      Objects.requireNonNull(var2, "Packet is null.");
      ChannelHandlerContext var3 = var1.pipeline().context("encoder");
      ((ChannelHandlerContext)Objects.requireNonNull(var3, "Channel is not a player channel")).fireChannelRead(var2);
   }

   @NotNull
   protected String getIdentifier() {
      return this.plugin.getName() + "-packet-injection";
   }

   public final void close() {
      if (!this.closed.getAndSet(true)) {
         this.listener.unregister();
         synchronized(this.networkManagers) {
            Iterator var2 = this.networkManagers.iterator();

            while(true) {
               if (!var2.hasNext()) {
                  break;
               }

               Object var3 = var2.next();

               try {
                  Channel var4 = this.getChannel(var3);
                  var4.eventLoop().submit(() -> {
                     return var4.pipeline().remove(this.identifier);
                  });
               } catch (Throwable var6) {
                  this.plugin.getLogger().log(Level.SEVERE, "An error occurred while uninjecting a player:", var6);
               }
            }
         }

         this.playerCache.clear();
         this.injectedChannels.clear();
      }
   }

   public final boolean isClosed() {
      return this.closed.get();
   }

   @NotNull
   public final Plugin getPlugin() {
      return this.plugin;
   }

   private void injectPlayer(Player player) {
      this.injectChannel(this.getChannel(var1)).player = var1;
   }

   private LightInjector.PacketHandler injectChannel(Channel channel) {
      LightInjector.PacketHandler var2 = new LightInjector.PacketHandler();
      var1.eventLoop().submit(() -> {
         if (!this.isClosed()) {
            if (this.injectedChannels.add(var1)) {
               try {
                  var1.pipeline().addBefore("packet_handler", this.identifier, var2);
               } catch (IllegalArgumentException var4) {
                  this.plugin.getLogger().severe("Couldn't inject a player, an handler with identifier '" + this.identifier + "' is already present");
               }
            }

         }
      });
      return var2;
   }

   private void injectNetworkManager(Object networkManager) {
      Channel var2 = this.getChannel(var1);
      if (!this.injectedChannels.contains(var2)) {
         this.injectChannel(var2);
      }

   }

   private Object getNetworkManager(Player player) {
      try {
         return GET_NETWORK_MANAGER.get(GET_PLAYER_CONNECTION.get(GET_PLAYER_HANDLE.invoke(var1)));
      } catch (ReflectiveOperationException var3) {
         throw new RuntimeException("Couldn't get player's network manager.", var3);
      }
   }

   private Channel getChannel(Player player) {
      return this.getChannel(this.getNetworkManager(var1));
   }

   private Channel getChannel(Object networkManager) {
      try {
         return (Channel)NMS_CHANNEL_FROM_NM.get(var1);
      } catch (ReflectiveOperationException var3) {
         throw new RuntimeException("Couldn't get network manager's channel.", var3);
      }
   }

   private static Class<?> getNMSClass(String name, String mcPackage) {
      return getNMSClass(var0, var1, (String)null);
   }

   private static Class<?> getNMSClass(String name, String mcPackage, @Nullable String backupName) {
      String var3 = "net.minecraft." + var1 + "." + var0;

      try {
         return Class.forName(var3);
      } catch (ClassNotFoundException var5) {
         if (var2 != null) {
            return getNMSClass(var2, var1);
         } else {
            throw new RuntimeException("Cannot find NMS Class! (" + var3 + ")", var5);
         }
      }
   }

   private static Class<?> getCBClass(String name) {
      String var1 = CRAFTBUKKIT_PACKAGE + "." + var0;

      try {
         return Class.forName(var1);
      } catch (ClassNotFoundException var3) {
         throw new RuntimeException("[LightInjector] Can not find CB Class! (" + var1 + ")", var3);
      }
   }

   private static Field getField(Class<?> clazz, String name) {
      try {
         Field var2 = var0.getDeclaredField(var1);
         var2.setAccessible(true);
         return var2;
      } catch (ReflectiveOperationException var3) {
         throw new RuntimeException("Cannot find field! (" + var0.getName() + "." + var1 + ")", var3);
      }
   }

   private static Field getField(Class<?> clazz, Class<?> type, @Range(from = 1L,to = 2147483647L) int index) {
      return getField(var0, var1, var2, 0);
   }

   private static Field getField(Class<?> clazz, Class<?> type, @Range(from = 1L,to = 2147483647L) int index, @Range(from = 0L,to = 2147483647L) int superClassesToTry) {
      int var5 = var2;

      for(int var6 = 0; var6 <= var3; ++var6) {
         Field[] var7 = var0.getDeclaredFields();
         Field[] var8 = var7;
         int var9 = var7.length;

         int var10;
         Field var11;
         for(var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];
            if (var1.equals(var11.getType())) {
               --var2;
               if (var2 <= 0) {
                  var11.setAccessible(true);
                  return var11;
               }
            }
         }

         var2 = var5;
         var8 = var7;
         var9 = var7.length;

         for(var10 = 0; var10 < var9; ++var10) {
            var11 = var8[var10];
            if (var1.isAssignableFrom(var11.getType())) {
               --var2;
               if (var2 <= 0) {
                  var11.setAccessible(true);
                  return var11;
               }
            }
         }

         var0 = var0.getSuperclass();
         if (var0 == null || var0 == Object.class) {
            break;
         }

         var2 = var5;
      }

      String var12 = "Cannot find field! (" + var5 + getOrdinal(var5) + var1.getName() + " in " + var0.getName();
      if (var3 > 0) {
         var12 = var12 + " and in its " + var3 + (var3 == 1 ? " super class" : " super classes");
      }

      var12 = var12 + ")";
      throw new RuntimeException(var12);
   }

   @Nullable
   private static Field getPendingNetworkManagersFieldOrNull() {
      try {
         Field var0 = getField(SERVER_CONNECTION_CLASS, "pending");
         if (var0.getType() == Queue.class || var0.getType() == List.class) {
            return var0;
         }
      } catch (Exception var3) {
      }

      try {
         return getField(SERVER_CONNECTION_CLASS, Queue.class, 1);
      } catch (Exception var2) {
         try {
            return getField(SERVER_CONNECTION_CLASS, List.class, 3);
         } catch (Exception var1) {
            return null;
         }
      }
   }

   private static Method getMethod(Class<?> clazz, String name, Class<?>... parameters) {
      try {
         Method var3 = var0.getDeclaredMethod(var1, var2);
         var3.setAccessible(true);
         return var3;
      } catch (ReflectiveOperationException var9) {
         StringJoiner var4 = new StringJoiner(", ");
         Class[] var5 = var2;
         int var6 = var2.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Class var8 = var5[var7];
            var4.add(var8.getName());
         }

         throw new RuntimeException("Cannot find method! (" + var0.getName() + "." + var1 + "(" + String.valueOf(var4) + ")", var9);
      }
   }

   private static Method getMethod(Class<?> clazz, Class<?> returnType, @Range(from = 1L,to = 2147483647L) int index) {
      int var3 = var2;
      Method[] var4 = var0.getDeclaredMethods();
      Method[] var5 = var4;
      int var6 = var4.length;

      int var7;
      Method var8;
      for(var7 = 0; var7 < var6; ++var7) {
         var8 = var5[var7];
         if (var1.equals(var8.getReturnType())) {
            --var2;
            if (var2 <= 0) {
               var8.setAccessible(true);
               return var8;
            }
         }
      }

      var2 = var2;
      var5 = var4;
      var6 = var4.length;

      for(var7 = 0; var7 < var6; ++var7) {
         var8 = var5[var7];
         if (var1.isAssignableFrom(var8.getReturnType())) {
            --var2;
            if (var2 <= 0) {
               var8.setAccessible(true);
               return var8;
            }
         }
      }

      throw new RuntimeException("Cannot find method! (" + var3 + getOrdinal(var3) + " returning " + var1.getName() + " in " + var0.getName() + ")");
   }

   private static String getOrdinal(int i) {
      String var10000;
      switch(var0) {
      case 1:
         var10000 = "st ";
         break;
      case 2:
         var10000 = "nd ";
         break;
      case 3:
         var10000 = "rd ";
         break;
      default:
         var10000 = "th ";
      }

      return var10000;
   }

   static {
      NMS_SERVER = getField(getCBClass("CraftServer"), SERVER_CLASS, 1);
      NMS_SERVER_CONNECTION = getField(SERVER_CLASS, SERVER_CONNECTION_CLASS, 1);
      NMS_NETWORK_MANAGERS_LIST = getField(SERVER_CONNECTION_CLASS, List.class, 2);
      NMS_PENDING_NETWORK_MANAGERS = getPendingNetworkManagersFieldOrNull();
      NMS_CHANNEL_FROM_NM = getField(NETWORK_MANAGER_CLASS, Channel.class, 1);
      GAME_PROFILE_FROM_PACKET = getField(PACKET_LOGIN_OUT_SUCCESS_CLASS, GameProfile.class, 1);
      GET_PLAYER_CONNECTION = getField(ENTITY_PLAYER_CLASS, PLAYER_CONNECTION_CLASS, 1);
      GET_NETWORK_MANAGER = getField(PLAYER_CONNECTION_CLASS, NETWORK_MANAGER_CLASS, 1, 1);
      GET_PLAYER_HANDLE = getMethod(getCBClass("entity.CraftPlayer"), "getHandle");
      ID = 0;
   }

   private final class EventListener implements Listener {
      @EventHandler(
         priority = EventPriority.LOWEST
      )
      private void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
         if (!LightInjector.this.isClosed()) {
            synchronized(LightInjector.this.networkManagers) {
               Object var4;
               if (LightInjector.this.networkManagers instanceof RandomAccess) {
                  for(int var3 = LightInjector.this.networkManagers.size() - 1; var3 >= 0; --var3) {
                     var4 = LightInjector.this.networkManagers.get(var3);
                     LightInjector.this.injectNetworkManager(var4);
                  }
               } else {
                  Iterator var10 = LightInjector.this.networkManagers.iterator();

                  while(var10.hasNext()) {
                     var4 = var10.next();
                     LightInjector.this.injectNetworkManager(var4);
                  }
               }

               if (LightInjector.this.pendingNetworkManagers != null) {
                  synchronized(LightInjector.this.pendingNetworkManagers) {
                     Iterator var11 = LightInjector.this.pendingNetworkManagers.iterator();

                     while(var11.hasNext()) {
                        Object var5 = var11.next();
                        LightInjector.this.injectNetworkManager(var5);
                     }
                  }
               }

            }
         }
      }

      @EventHandler(
         priority = EventPriority.LOWEST
      )
      private void onPlayerLoginEvent(PlayerLoginEvent event) {
         if (!LightInjector.this.isClosed()) {
            LightInjector.this.playerCache.put(var1.getPlayer().getUniqueId(), var1.getPlayer());
         }
      }

      @EventHandler(
         priority = EventPriority.LOWEST
      )
      private void onPlayerJoinEvent(PlayerJoinEvent event) {
         if (!LightInjector.this.isClosed()) {
            Player var2 = var1.getPlayer();
            Object var3 = LightInjector.this.getNetworkManager(var2);
            Channel var4 = LightInjector.this.getChannel(var3);
            ChannelHandler var5 = var4.pipeline().get(LightInjector.this.identifier);
            if (var5 != null) {
               if (var5 instanceof LightInjector.PacketHandler) {
                  ((LightInjector.PacketHandler)var5).player = var2;
                  LightInjector.this.playerCache.remove(var2.getUniqueId());
               }

            } else {
               LightInjector.this.plugin.getLogger().info("Late injection for player " + var2.getName());
               LightInjector.this.injectChannel(var4).player = var2;
            }
         }
      }

      @EventHandler(
         priority = EventPriority.MONITOR
      )
      private void onPluginDisableEvent(PluginDisableEvent event) {
         if (LightInjector.this.plugin.equals(var1.getPlugin())) {
            LightInjector.this.close();
         }

      }

      private void unregister() {
         AsyncPlayerPreLoginEvent.getHandlerList().unregister(this);
         PlayerLoginEvent.getHandlerList().unregister(this);
         PlayerJoinEvent.getHandlerList().unregister(this);
         PluginDisableEvent.getHandlerList().unregister(this);
      }
   }

   private final class PacketHandler extends ChannelDuplexHandler {
      private volatile Player player;

      public void channelUnregistered(ChannelHandlerContext ctx) {
         LightInjector.this.injectedChannels.remove(var1.channel());
         super.channelUnregistered(var1);
      }

      public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) {
         if (this.player == null && LightInjector.PACKET_LOGIN_OUT_SUCCESS_CLASS.isInstance(var2)) {
            try {
               GameProfile var4 = (GameProfile)LightInjector.GAME_PROFILE_FROM_PACKET.get(var2);

               UUID var5;
               try {
                  var5 = var4.getId();
               } catch (NoSuchMethodError var10) {
                  Method var7 = var4.getClass().getDeclaredMethod("id");
                  var5 = (UUID)var7.invoke(var4);
               }

               Player var6 = (Player)LightInjector.this.playerCache.remove(var5);
               if (var6 != null) {
                  this.player = var6;
               }
            } catch (Throwable var11) {
               LightInjector.this.plugin.getLogger().log(Level.SEVERE, "An error occurred while handling PacketLoginOutSuccess:", var11);
            }
         }

         Object var12;
         try {
            var12 = LightInjector.this.onPacketSendAsync(this.player, var1.channel(), var2);
         } catch (OutOfMemoryError var8) {
            throw var8;
         } catch (Throwable var9) {
            LightInjector.this.plugin.getLogger().log(Level.SEVERE, "An error occurred while calling onPacketSendAsync:", var9);
            super.write(var1, var2, var3);
            return;
         }

         if (var12 != null) {
            super.write(var1, var12, var3);
         }

      }

      public void channelRead(ChannelHandlerContext ctx, Object packet) {
         Object var3;
         try {
            var3 = LightInjector.this.onPacketReceiveAsync(this.player, var1.channel(), var2);
         } catch (OutOfMemoryError var5) {
            throw var5;
         } catch (Throwable var6) {
            LightInjector.this.plugin.getLogger().log(Level.SEVERE, "An error occurred while calling onPacketReceiveAsync:", var6);
            super.channelRead(var1, var2);
            return;
         }

         if (var3 != null) {
            super.channelRead(var1, var3);
         }

      }
   }
}
