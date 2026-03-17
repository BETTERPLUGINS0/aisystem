package com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api;

import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.event.PlayerSmoothCoastersHandshakeEvent;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.implementation.Implementation;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PlayerListener implements Listener, PluginMessageListener {
   private static final String CHANNEL = "smoothcoasters:hs";
   private static final String SUPPORTED_VERSIONS = "smoothcoasters_supported_versions";
   private static final String OFFERED_VERSIONS = "smoothcoasters_offered_versions";
   private final SmoothCoastersAPI api;

   public PlayerListener(SmoothCoastersAPI api) {
      this.api = api;
      Bukkit.getPluginManager().registerEvents(this, api.getPlugin());
      Bukkit.getMessenger().registerIncomingPluginChannel(api.getPlugin(), "smoothcoasters:hs", this);
      Bukkit.getMessenger().registerOutgoingPluginChannel(api.getPlugin(), "smoothcoasters:hs");
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerRegisterChannelStart(PlayerRegisterChannelEvent event) {
      if (event.getChannel().equals("smoothcoasters:hs")) {
         event.getPlayer().setMetadata("smoothcoasters_supported_versions", new FixedMetadataValue(this.api.getPlugin(), this.api.getImplementations().keySet()));
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerRegisterChannel(PlayerRegisterChannelEvent event) {
      if (event.getChannel().equals("smoothcoasters:hs")) {
         if (!event.getPlayer().hasMetadata("smoothcoasters_offered_versions")) {
            Set<Byte> versions = null;
            Iterator var3 = event.getPlayer().getMetadata("smoothcoasters_supported_versions").iterator();

            MetadataValue value;
            Set set;
            while(var3.hasNext()) {
               value = (MetadataValue)var3.next();
               set = (Set)value.value();
               if (versions == null) {
                  versions = new TreeSet(set);
               } else {
                  versions.retainAll(set);
               }
            }

            event.getPlayer().setMetadata("smoothcoasters_offered_versions", new FixedMetadataValue(this.api.getPlugin(), versions));
            if (versions != null && !versions.isEmpty()) {
               byte[] message = new byte[versions.size() + 1];
               message[0] = (byte)versions.size();
               int i = 1;

               Byte version;
               for(Iterator var9 = versions.iterator(); var9.hasNext(); message[i++] = version) {
                  version = (Byte)var9.next();
               }

               event.getPlayer().sendPluginMessage(this.api.getPlugin(), "smoothcoasters:hs", message);
            } else {
               this.api.getPlugin().getLogger().warning("[SmoothCoastersAPI] Plugins have no supported SmoothCoasters versions in common");
               var3 = event.getPlayer().getMetadata("smoothcoasters_supported_versions").iterator();

               while(var3.hasNext()) {
                  value = (MetadataValue)var3.next();
                  set = (Set)value.value();
                  this.api.getPlugin().getLogger().warning("[SmoothCoastersAPI] " + value.getOwningPlugin().getName() + ": " + (String)set.stream().map(String::valueOf).collect(Collectors.joining(", ")));
               }

            }
         }
      }
   }

   public void onPluginMessageReceived(String channel, Player player, byte[] payload) {
      if (channel.equals("smoothcoasters:hs") && payload.length >= 1) {
         ByteBuffer buffer = ByteBuffer.wrap(payload);

         Implementation implementation;
         String version;
         try {
            implementation = (Implementation)this.api.getImplementations().get(buffer.get());
            version = buffer.hasRemaining() ? Util.readString(buffer, 32) : null;
         } catch (Exception var8) {
            this.api.getPlugin().getLogger().log(Level.SEVERE, "[SmoothCoastersAPI] Received invalid handshake from " + player.getName(), var8);
            return;
         }

         PlayerEntry entry = this.api.getOrCreateEntry(player);
         entry.setImplementation(implementation);
         entry.setVersion(version);
         if (implementation != null) {
            Bukkit.getPluginManager().callEvent(new PlayerSmoothCoastersHandshakeEvent(player, implementation, version));
         }

      }
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      player.removeMetadata("smoothcoasters_supported_versions", this.api.getPlugin());
      player.removeMetadata("smoothcoasters_offered_versions", this.api.getPlugin());
      this.api.removeEntry(player);
   }

   public void unregister() {
      HandlerList.unregisterAll(this);
      Bukkit.getMessenger().unregisterIncomingPluginChannel(this.api.getPlugin(), "smoothcoasters:hs", this);
      Bukkit.getMessenger().unregisterOutgoingPluginChannel(this.api.getPlugin(), "smoothcoasters:hs");
   }
}
