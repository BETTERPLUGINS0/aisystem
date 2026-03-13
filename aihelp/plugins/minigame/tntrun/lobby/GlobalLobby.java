package tntrun.lobby;

import java.io.File;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import tntrun.TNTRun;
import tntrun.messages.Messages;

public class GlobalLobby {
   private File lobbyFile;
   private LobbyScoreboard lobbysb;
   private LobbyLocation lobbyLocation;

   public GlobalLobby(TNTRun plugin) {
      String var10003 = String.valueOf(plugin.getDataFolder());
      this.lobbyFile = new File(var10003 + File.separator + "lobby.yml");
      this.lobbysb = new LobbyScoreboard(plugin);
   }

   public boolean isLobbyLocationWorldAvailable() {
      return this.isLobbyLocationSet() ? this.lobbyLocation.isWorldAvailable() : false;
   }

   public boolean isLobbyLocationSet() {
      return this.lobbyLocation != null;
   }

   public void joinLobby(Player player) {
      if (!this.isLobbyLocationSet()) {
         Messages.sendMessage(player, Messages.nolobby);
      } else if (!this.isLobbyLocationWorldAvailable()) {
         Messages.sendMessage(player, Messages.lobbyunloaded);
      } else {
         player.teleport(this.getLobbyLocation());
         Messages.sendMessage(player, Messages.teleporttolobby);
         this.lobbysb.createLobbyScoreboard(player);
      }
   }

   public Location getLobbyLocation() {
      return this.lobbyLocation.getLocation();
   }

   public void setLobbyLocation(Location location) {
      if (location != null) {
         this.lobbyLocation = new LobbyLocation(location.getWorld().getName(), location.toVector(), location.getYaw(), location.getPitch());
      } else {
         FileConfiguration config = new YamlConfiguration();
         this.lobbyLocation = null;
         config.set("lobby", (Object)null);

         try {
            config.save(this.lobbyFile);
         } catch (IOException var4) {
            var4.printStackTrace();
         }

      }
   }

   public void saveToConfig() {
      FileConfiguration config = new YamlConfiguration();
      if (this.isLobbyLocationSet()) {
         config.set("lobby.world", this.lobbyLocation.getWorldName());
         config.set("lobby.vector", this.lobbyLocation.getVector());
         config.set("lobby.yaw", this.lobbyLocation.getYaw());
         config.set("lobby.pitch", this.lobbyLocation.getPitch());

         try {
            config.save(this.lobbyFile);
         } catch (IOException var3) {
         }
      }

   }

   public void loadFromConfig() {
      FileConfiguration config = YamlConfiguration.loadConfiguration(this.lobbyFile);
      String worldname = config.getString("lobby.world", (String)null);
      Vector vector = config.getVector("lobby.vector", (Vector)null);
      float yaw = (float)config.getDouble("lobby.yaw", 0.0D);
      float pitch = (float)config.getDouble("lobby.pitch", 0.0D);
      if (worldname != null && vector != null) {
         this.lobbyLocation = new LobbyLocation(worldname, vector, yaw, pitch);
      }

   }
}
