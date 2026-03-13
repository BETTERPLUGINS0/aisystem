package me.ag4.playershop.hooks;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ProtocolLibAPI {
   private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

   public static void chestOpenAnimation(Player player, Location loc) {
      BlockPosition position = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
      PacketContainer packetContainer = protocolManager.createPacket(Server.BLOCK_ACTION);
      packetContainer.getBlockPositionModifier().write(0, position);
      packetContainer.getIntegers().write(0, 1);
      packetContainer.getIntegers().write(1, 1);
      player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
      protocolManager.sendServerPacket(player, packetContainer);
   }

   public static void chestCloseAnimation(Player player, Location loc) {
      BlockPosition position = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
      PacketContainer packetContainer = protocolManager.createPacket(Server.BLOCK_ACTION);
      packetContainer.getBlockPositionModifier().write(0, position);
      packetContainer.getIntegers().write(0, 1);
      packetContainer.getIntegers().write(1, 0);
      player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.0F);
      protocolManager.sendServerPacket(player, packetContainer);
   }
}
