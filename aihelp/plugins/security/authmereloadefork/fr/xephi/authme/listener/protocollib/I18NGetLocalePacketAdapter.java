package fr.xephi.authme.listener.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.util.message.I18NUtils;
import java.util.UUID;

class I18NGetLocalePacketAdapter extends PacketAdapter {
   I18NGetLocalePacketAdapter(AuthMe plugin) {
      super(plugin, ListenerPriority.NORMAL, new PacketType[]{Client.SETTINGS});
   }

   public void onPacketReceiving(PacketEvent event) {
      if (event.getPacketType() == Client.SETTINGS) {
         String locale = ((String)event.getPacket().getStrings().read(0)).toLowerCase();
         UUID uuid = event.getPlayer().getUniqueId();
         I18NUtils.addLocale(uuid, locale);
      }

   }

   public void register() {
      ProtocolLibrary.getProtocolManager().addPacketListener(this);
   }

   public void unregister() {
      ProtocolLibrary.getProtocolManager().removePacketListener(this);
   }
}
