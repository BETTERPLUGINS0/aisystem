package com.lenis0012.bukkit.loginsecurity.modules.general;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import org.bukkit.plugin.Plugin;

public class InventoryPacketListener extends PacketAdapter {
   public static void register(Plugin plugin) {
      ProtocolLibrary.getProtocolManager().addPacketListener(new InventoryPacketListener(plugin));
   }

   public InventoryPacketListener(Plugin plugin) {
      super(plugin, ListenerPriority.LOW, new PacketType[]{Server.WINDOW_ITEMS, Server.SET_SLOT, Server.WINDOW_DATA, Client.WINDOW_CLICK});
   }

   private boolean hideInvCheck(PacketEvent event) {
      if (!LoginSecurity.getConfiguration().isHideInventory()) {
         return false;
      } else if ((Integer)event.getPacket().getIntegers().read(0) != 0) {
         return false;
      } else {
         PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(event.getPlayer());
         return !session.isAuthorized() && session.isRegistered();
      }
   }

   public void onPacketReceiving(PacketEvent event) {
      if (this.hideInvCheck(event)) {
         event.setCancelled(true);
      }

   }

   public void onPacketSending(PacketEvent event) {
      if (this.hideInvCheck(event)) {
         event.setCancelled(true);
      }

   }
}
