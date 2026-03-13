package me.ag4.playershop.objects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class SignMenu {
   private final Plugin plugin;
   private final Map<Player, SignMenu.Menu> inputs;

   public SignMenu(Plugin plugin) {
      this.plugin = plugin;
      this.inputs = new HashMap();
      this.listen();
   }

   public SignMenu.Menu newMenu(List<String> text) {
      return new SignMenu.Menu(text);
   }

   private void listen() {
      ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.plugin, new PacketType[]{Client.UPDATE_SIGN}) {
         public void onPacketReceiving(PacketEvent event) {
            Player player = event.getPlayer();
            SignMenu.Menu menu = (SignMenu.Menu)SignMenu.this.inputs.remove(player);
            if (menu != null) {
               event.setCancelled(true);
               boolean success = menu.response.test(player, (String[])event.getPacket().getStringArrays().read(0));
               if (!success && menu.reopenIfFail && !menu.forceClose) {
                  Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                     menu.open(player);
                  }, 2L);
               }

               Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                  if (player.isOnline()) {
                     player.sendBlockChange(menu.location, menu.location.getBlock().getBlockData());
                  }

               }, 2L);
            }
         }
      });
   }

   public final class Menu {
      private final List<String> text;
      private BiPredicate<Player, String[]> response;
      private boolean reopenIfFail;
      private Location location;
      private boolean forceClose;

      Menu(List<String> param2) {
         this.text = text;
      }

      public SignMenu.Menu reopenIfFail(boolean value) {
         this.reopenIfFail = value;
         return this;
      }

      public SignMenu.Menu response(BiPredicate<Player, String[]> response) {
         this.response = response;
         return this;
      }

      public void open(Player player) {
         Objects.requireNonNull(player, "player");
         if (player.isOnline()) {
            this.location = player.getLocation();
            this.location.setY((double)(this.location.getBlockY() - 4));
            player.sendBlockChange(this.location, Material.OAK_SIGN.createBlockData());
            player.sendSignChange(this.location, (String[])((List)this.text.stream().map(this::color).collect(Collectors.toList())).toArray(new String[4]));
            int McVersion = ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
            PacketContainer openSign = ProtocolLibrary.getProtocolManager().createPacket(Server.OPEN_SIGN_EDITOR);
            BlockPosition position = new BlockPosition(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ());
            openSign.getBlockPositionModifier().write(0, position);
            if (McVersion > 762) {
               openSign.getBooleans().write(0, true);
            }

            ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSign);
            SignMenu.this.inputs.put(player, this);
         }
      }

      public void close(Player player, boolean force) {
         this.forceClose = force;
         if (player.isOnline()) {
            player.closeInventory();
         }

      }

      public void close(Player player) {
         this.close(player, false);
      }

      private String color(String input) {
         return ChatColor.translateAlternateColorCodes('&', input);
      }
   }
}
