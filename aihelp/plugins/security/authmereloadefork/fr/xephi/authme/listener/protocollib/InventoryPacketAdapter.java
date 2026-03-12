package fr.xephi.authme.listener.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.BukkitService;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class InventoryPacketAdapter extends PacketAdapter {
   private static final int PLAYER_INVENTORY = 0;
   private static final int CRAFTING_SIZE = 5;
   private static final int ARMOR_SIZE = 4;
   private static final int MAIN_SIZE = 27;
   private static final int HOTBAR_SIZE = 9;
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(InventoryPacketAdapter.class);
   private final PlayerCache playerCache;
   private final DataSource dataSource;

   InventoryPacketAdapter(AuthMe plugin, PlayerCache playerCache, DataSource dataSource) {
      super(plugin, new PacketType[]{Server.SET_SLOT, Server.WINDOW_ITEMS});
      this.playerCache = playerCache;
      this.dataSource = dataSource;
   }

   public void onPacketSending(PacketEvent packetEvent) {
      Player player = packetEvent.getPlayer();
      PacketContainer packet = packetEvent.getPacket();
      int windowId = (Integer)packet.getIntegers().read(0);
      if (windowId == 0 && this.shouldHideInventory(player.getName())) {
         packetEvent.setCancelled(true);
      }

   }

   public void register(BukkitService bukkitService) {
      ProtocolLibrary.getProtocolManager().addPacketListener(this);
      bukkitService.getOnlinePlayers().stream().filter((player) -> {
         return this.shouldHideInventory(player.getName());
      }).forEach(this::sendBlankInventoryPacket);
   }

   private boolean shouldHideInventory(String playerName) {
      return !this.playerCache.isAuthenticated(playerName) && this.dataSource.isAuthAvailable(playerName);
   }

   public void unregister() {
      ProtocolLibrary.getProtocolManager().removePacketListener(this);
   }

   public void sendBlankInventoryPacket(Player player) {
      ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
      PacketContainer inventoryPacket = protocolManager.createPacket(Server.WINDOW_ITEMS);
      inventoryPacket.getIntegers().write(0, 0);
      int inventorySize = 45;
      ItemStack[] blankInventory = new ItemStack[inventorySize];
      Arrays.fill(blankInventory, new ItemStack(Material.AIR));
      StructureModifier<ItemStack[]> itemArrayModifier = inventoryPacket.getItemArrayModifier();
      if (itemArrayModifier.size() > 0) {
         itemArrayModifier.write(0, blankInventory);
      } else {
         StructureModifier<List<ItemStack>> itemListModifier = inventoryPacket.getItemListModifier();
         itemListModifier.write(0, Arrays.asList(blankInventory));
      }

      protocolManager.sendServerPacket(player, inventoryPacket, false);
   }
}
