package fr.xephi.authme.listener.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.output.ConsoleLoggerFactory;

class TabCompletePacketAdapter extends PacketAdapter {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(TabCompletePacketAdapter.class);
   private final PlayerCache playerCache;

   TabCompletePacketAdapter(AuthMe plugin, PlayerCache playerCache) {
      super(plugin, ListenerPriority.NORMAL, new PacketType[]{Client.TAB_COMPLETE});
      this.playerCache = playerCache;
   }

   public void onPacketReceiving(PacketEvent event) {
      if (event.getPacketType() == Client.TAB_COMPLETE) {
         try {
            if (!this.playerCache.isAuthenticated(event.getPlayer().getName())) {
               event.setCancelled(true);
            }
         } catch (FieldAccessException var3) {
            this.logger.logException("Couldn't access field:", var3);
         }
      }

   }

   public void register() {
      ProtocolLibrary.getProtocolManager().addPacketListener(this);
   }

   public void unregister() {
      ProtocolLibrary.getProtocolManager().removePacketListener(this);
   }
}
