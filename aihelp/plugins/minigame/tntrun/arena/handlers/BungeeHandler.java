package tntrun.arena.handlers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.ServerListPingEvent;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;

public class BungeeHandler implements Listener {
   private TNTRun plugin;

   public BungeeHandler(TNTRun plugin) {
      this.plugin = plugin;
      plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
   }

   public void connectToHub(Player player) {
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF("Connect");
      out.writeUTF(this.getHubServerName());
      player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());
   }

   private String getHubServerName() {
      return this.plugin.getConfig().getString("bungeecord.hub");
   }

   private String getMOTD() {
      Arena arena = this.plugin.amanager.getBungeeArena();
      if (arena == null) {
         return "";
      } else {
         return arena.getStatusManager().isArenaStarting() && arena.getGameHandler().count <= 3 ? arena.getStatusManager().getFormattedMessage(Messages.arenarunning) : arena.getStatusManager().getArenaStatusMesssage();
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onServerListPing(ServerListPingEvent event) {
      Arena arena = this.plugin.amanager.getBungeeArena();
      if (arena != null && this.plugin.getConfig().getBoolean("bungeecord.useMOTD")) {
         event.setMaxPlayers(arena.getStructureManager().getMaxPlayers());
         event.setMotd(this.getMOTD());
      }
   }

   @EventHandler
   public void onLogin(PlayerLoginEvent event) {
      if (this.plugin.isBungeecord()) {
         if (this.plugin.getConfig().getBoolean("bungeecord.teleporttohub")) {
            Arena arena = this.plugin.amanager.getBungeeArena();
            if (arena == null || !event.getPlayer().hasPermission("tntrun.spectate") && !arena.getPlayerHandler().checkJoin(event.getPlayer())) {
               event.disallow(Result.KICK_OTHER, "You cannot join the arena at this time");
            }

         }
      }
   }

   @EventHandler
   public void onJoin(PlayerJoinEvent event) {
      if (this.plugin.isBungeecord()) {
         if (!this.plugin.getConfig().getBoolean("bungeecord.showdefaultjoinmessage")) {
            event.setJoinMessage((String)null);
         }

         if (!this.plugin.getConfig().getBoolean("bungeecord.teleporttohub")) {
            this.plugin.getGlobalLobby().joinLobby(event.getPlayer());
         } else {
            Arena arena = this.plugin.amanager.getBungeeArena();
            if (arena != null) {
               Player player = event.getPlayer();
               if (!player.hasPermission("tntrun.spectate")) {
                  arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoothers);
               } else if (this.plugin.getConfig().getBoolean("bungeecord.playorspectate") && arena.getPlayerHandler().checkJoin(player)) {
                  arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoothers);
               } else if (!arena.getPlayerHandler().canSpectate(player)) {
                  this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                     this.connectToHub(player);
                  }, 20L);
               } else {
                  arena.getPlayerHandler().spectatePlayer(player, Messages.playerjoinedasspectator, "");
               }
            }
         }
      }
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent event) {
      if (this.plugin.isBungeecord()) {
         if (!this.plugin.getConfig().getBoolean("bungeecord.showdefaultjoinmessage")) {
            event.setQuitMessage((String)null);
         }

      }
   }
}
