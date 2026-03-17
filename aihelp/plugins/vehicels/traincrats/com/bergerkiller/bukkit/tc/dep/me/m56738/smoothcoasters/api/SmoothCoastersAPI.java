package com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api;

import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.implementation.ImplV4;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.implementation.ImplV5;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.implementation.ImplV6;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.implementation.Implementation;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SmoothCoastersAPI {
   private final Plugin plugin;
   private final PlayerListener playerListener;
   private final Map<Byte, Implementation> implementations = new TreeMap();
   private final Map<UUID, PlayerEntry> players = new ConcurrentHashMap();
   private final NetworkInterface defaultNetwork;

   public SmoothCoastersAPI(Plugin plugin) {
      this.plugin = plugin;
      this.playerListener = new PlayerListener(this);
      this.defaultNetwork = new DefaultNetworkInterface(plugin);
      this.registerImplementation(new ImplV4(plugin));
      this.registerImplementation(new ImplV5(plugin));
      this.registerImplementation(new ImplV6(plugin));
   }

   public void registerImplementation(Implementation implementation) {
      this.implementations.put(implementation.getVersion(), implementation);
   }

   public Plugin getPlugin() {
      return this.plugin;
   }

   Map<Byte, Implementation> getImplementations() {
      return this.implementations;
   }

   PlayerEntry getEntry(Player player) {
      return (PlayerEntry)this.players.get(player.getUniqueId());
   }

   PlayerEntry getOrCreateEntry(Player player) {
      return (PlayerEntry)this.players.computeIfAbsent(player.getUniqueId(), (u) -> {
         return new PlayerEntry();
      });
   }

   void removeEntry(Player player) {
      this.players.remove(player.getUniqueId());
   }

   private Implementation getImplementation(Player player) {
      PlayerEntry entry = this.getEntry(player);
      return entry == null ? null : entry.getImplementation();
   }

   public boolean isEnabled(Player player) {
      return this.getImplementation(player) != null;
   }

   public byte getVersion(Player player) {
      Implementation implementation = this.getImplementation(player);
      return implementation != null ? implementation.getVersion() : -1;
   }

   public String getModVersion(Player player) {
      PlayerEntry entry = this.getEntry(player);
      return entry != null ? entry.getVersion() : null;
   }

   public boolean isSupported(Player player, Feature feature) {
      Implementation implementation = this.getImplementation(player);
      return implementation == null ? false : implementation.isSupported(feature);
   }

   public boolean resetRotation(NetworkInterface network, Player player) {
      return this.setRotation(network, player, 0.0F, 0.0F, 0.0F, 1.0F, (byte)0);
   }

   public boolean setRotation(NetworkInterface network, Player player, float x, float y, float z, float w, byte ticks) {
      if (network == null) {
         network = this.defaultNetwork;
      }

      Implementation implementation = this.getImplementation(player);
      if (implementation != null && implementation.isSupported(Feature.ROTATION)) {
         implementation.sendRotation(network, player, x, y, z, w, ticks);
         return true;
      } else {
         return false;
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean setEntityRotation(NetworkInterface network, Player player, int entity, float x, float y, float z, float w, byte ticks) {
      if (network == null) {
         network = this.defaultNetwork;
      }

      Implementation implementation = this.getImplementation(player);
      if (implementation != null && implementation.isSupported(Feature.ENTITY_ROTATION)) {
         implementation.sendEntityRotation(network, player, entity, x, y, z, w, ticks);
         return true;
      } else {
         return false;
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean setEntityLerpTicks(NetworkInterface network, Player player, int entity, byte ticks) {
      if (network == null) {
         network = this.defaultNetwork;
      }

      Implementation implementation = this.getImplementation(player);
      if (implementation != null && implementation.isSupported(Feature.ENTITY_PROPERTIES)) {
         implementation.sendEntityProperties(network, player, entity, ticks);
         return true;
      } else {
         return false;
      }
   }

   public boolean setRotationLimit(NetworkInterface network, Player player, float minYaw, float maxYaw, float minPitch, float maxPitch) {
      if (network == null) {
         network = this.defaultNetwork;
      }

      Implementation implementation = this.getImplementation(player);
      if (implementation != null && implementation.isSupported(Feature.ROTATION_LIMIT)) {
         implementation.sendRotationLimit(network, player, minYaw, maxYaw, minPitch, maxPitch);
         return true;
      } else {
         return false;
      }
   }

   public boolean resetRotationLimit(NetworkInterface network, Player player) {
      return this.setRotationLimit(network, player, -180.0F, 180.0F, -90.0F, 90.0F);
   }

   public void unregister() {
      this.implementations.clear();
      this.playerListener.unregister();
      this.players.clear();
   }
}
