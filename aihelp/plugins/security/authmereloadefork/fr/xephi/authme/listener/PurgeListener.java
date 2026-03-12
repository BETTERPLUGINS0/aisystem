package fr.xephi.authme.listener;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.io.File;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PurgeListener implements Listener {
   private final AuthMeApi authmeApi = AuthMeApi.getInstance();
   @Inject
   private Settings settings;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private AuthMe plugin;

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      String name = player.getName();
      UUID playerUUID = event.getPlayer().getUniqueId();
      if (!this.authmeApi.isRegistered(name) && (Boolean)this.settings.getProperty(SecuritySettings.PURGE_DATA_ON_QUIT)) {
         this.bukkitService.runTaskLater(() -> {
            if (!player.isOnline()) {
               this.deletePlayerData(playerUUID);
               this.deletePlayerStats(playerUUID);
               this.deleteAuthMePlayerData(playerUUID);
            }

         }, 100L);
      }

   }

   private void deletePlayerData(UUID playerUUID) {
      File serverFolder = Bukkit.getServer().getWorldContainer();
      String worldFolderName = (String)this.settings.getProperty(SecuritySettings.DELETE_PLAYER_DATA_WORLD);
      File playerDataFolder = new File(serverFolder, File.separator + worldFolderName + File.separator + "playerdata");
      File playerDataFile = new File(playerDataFolder, File.separator + playerUUID + ".dat");
      File playerDataOldFile = new File(playerDataFolder, File.separator + playerUUID + ".dat_old");
      if (playerDataFile.exists()) {
         playerDataFile.delete();
      }

      if (playerDataOldFile.exists()) {
         playerDataOldFile.delete();
      }

   }

   private void deleteAuthMePlayerData(UUID playerUUID) {
      File pluginFolder = this.plugin.getDataFolder();
      File path = new File(pluginFolder, File.separator + "playerdata" + File.separator + playerUUID);
      File dataFile = new File(path, File.separator + "data.json");
      if (dataFile.exists()) {
         dataFile.delete();
         path.delete();
      }

   }

   private void deletePlayerStats(UUID playerUUID) {
      File serverFolder = Bukkit.getServer().getWorldContainer();
      String worldFolderName = (String)this.settings.getProperty(SecuritySettings.DELETE_PLAYER_DATA_WORLD);
      File statsFolder = new File(serverFolder, File.separator + worldFolderName + File.separator + "stats");
      File statsFile = new File(statsFolder, File.separator + playerUUID + ".json");
      if (statsFile.exists()) {
         statsFile.delete();
      }

   }
}
