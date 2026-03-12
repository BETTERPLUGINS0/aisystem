package fr.xephi.authme.task.purge;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.PluginHookService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PurgeSettings;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public class PurgeExecutor {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(PurgeExecutor.class);
   @Inject
   private Settings settings;
   @Inject
   private DataSource dataSource;
   @Inject
   private PermissionsManager permissionsManager;
   @Inject
   private PluginHookService pluginHookService;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private Server server;

   PurgeExecutor() {
   }

   public void executePurge(Collection<OfflinePlayer> players, Collection<String> names) {
      this.purgeFromAuthMe(names);
      this.purgeEssentials(players);
      this.purgeDat(players);
      this.purgeLimitedCreative(names);
      this.purgeAntiXray(names);
      this.purgePermissions(players);
   }

   synchronized void purgeAntiXray(Collection<String> cleared) {
      if ((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_ANTI_XRAY_FILE)) {
         int i = 0;
         File dataFolder = new File(FileUtils.makePath(".", "plugins", "AntiXRayData", "PlayerData"));
         if (dataFolder.exists() && dataFolder.isDirectory()) {
            String[] var4 = dataFolder.list();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String file = var4[var6];
               if (cleared.contains(file.toLowerCase(Locale.ROOT))) {
                  File playerFile = new File(dataFolder, file);
                  if (playerFile.exists() && playerFile.delete()) {
                     ++i;
                  }
               }
            }

            this.logger.info("AutoPurge: Removed " + i + " AntiXRayData Files");
         }
      }
   }

   synchronized void purgeFromAuthMe(Collection<String> names) {
      this.dataSource.purgeRecords(names);
      this.logger.info(ChatColor.GOLD + "Deleted " + names.size() + " user accounts");
   }

   synchronized void purgeLimitedCreative(Collection<String> cleared) {
      if ((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_LIMITED_CREATIVE_INVENTORIES)) {
         int i = 0;
         File dataFolder = new File(FileUtils.makePath(".", "plugins", "LimitedCreative", "inventories"));
         if (dataFolder.exists() && dataFolder.isDirectory()) {
            String[] var4 = dataFolder.list();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String file = var4[var6];
               String name = file;
               int idx = file.lastIndexOf("_creative.yml");
               if (idx != -1) {
                  name = file.substring(0, idx);
               } else {
                  idx = file.lastIndexOf("_adventure.yml");
                  if (idx != -1) {
                     name = file.substring(0, idx);
                  } else {
                     idx = file.lastIndexOf(".yml");
                     if (idx != -1) {
                        name = file.substring(0, idx);
                     }
                  }
               }

               if (!name.equals(file) && cleared.contains(name.toLowerCase(Locale.ROOT))) {
                  File dataFile = new File(dataFolder, file);
                  if (dataFile.exists() && dataFile.delete()) {
                     ++i;
                  }
               }
            }

            this.logger.info("AutoPurge: Removed " + i + " LimitedCreative Survival, Creative and Adventure files");
         }
      }
   }

   synchronized void purgeDat(Collection<OfflinePlayer> cleared) {
      if ((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_PLAYER_DAT)) {
         int i = 0;
         File dataFolder = new File(this.server.getWorldContainer(), FileUtils.makePath((String)this.settings.getProperty(PurgeSettings.DEFAULT_WORLD), "players"));
         Iterator var4 = cleared.iterator();

         while(var4.hasNext()) {
            OfflinePlayer offlinePlayer = (OfflinePlayer)var4.next();
            File playerFile = new File(dataFolder, offlinePlayer.getUniqueId() + ".dat");
            if (playerFile.delete()) {
               ++i;
            }
         }

         this.logger.info("AutoPurge: Removed " + i + " .dat Files");
      }
   }

   synchronized void purgeEssentials(Collection<OfflinePlayer> cleared) {
      if ((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_ESSENTIALS_FILES)) {
         File essentialsDataFolder = this.pluginHookService.getEssentialsDataFolder();
         if (essentialsDataFolder == null) {
            this.logger.info("Cannot purge Essentials: plugin is not loaded");
         } else {
            File userDataFolder = new File(essentialsDataFolder, "userdata");
            if (userDataFolder.exists() && userDataFolder.isDirectory()) {
               int deletedFiles = 0;
               Iterator var5 = cleared.iterator();

               while(var5.hasNext()) {
                  OfflinePlayer offlinePlayer = (OfflinePlayer)var5.next();
                  File playerFile = new File(userDataFolder, offlinePlayer.getUniqueId() + ".yml");
                  if (playerFile.exists() && playerFile.delete()) {
                     ++deletedFiles;
                  }
               }

               this.logger.info("AutoPurge: Removed " + deletedFiles + " EssentialsFiles");
            }
         }
      }
   }

   synchronized void purgePermissions(Collection<OfflinePlayer> cleared) {
      if ((Boolean)this.settings.getProperty(PurgeSettings.REMOVE_PERMISSIONS)) {
         Iterator var2 = cleared.iterator();

         while(var2.hasNext()) {
            OfflinePlayer offlinePlayer = (OfflinePlayer)var2.next();
            if (!this.permissionsManager.loadUserData(offlinePlayer)) {
               this.logger.warning("Unable to purge the permissions of user " + offlinePlayer + "!");
            } else {
               this.permissionsManager.removeAllGroups(offlinePlayer);
            }
         }

         this.logger.info("AutoPurge: Removed permissions from " + cleared.size() + " player(s).");
      }
   }
}
