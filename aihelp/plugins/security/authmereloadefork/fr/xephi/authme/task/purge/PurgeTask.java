package fr.xephi.authme.task.purge;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.UniversalRunnable;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class PurgeTask extends UniversalRunnable {
   private static final int INTERVAL_CHECK = 5;
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(PurgeTask.class);
   private final PurgeService purgeService;
   private final PermissionsManager permissionsManager;
   private final UUID sender;
   private final Set<String> toPurge;
   private final OfflinePlayer[] offlinePlayers;
   private final int totalPurgeCount;
   private int currentPage = 0;

   PurgeTask(PurgeService service, PermissionsManager permissionsManager, CommandSender sender, Set<String> toPurge, OfflinePlayer[] offlinePlayers) {
      this.purgeService = service;
      this.permissionsManager = permissionsManager;
      if (sender instanceof Player) {
         this.sender = ((Player)sender).getUniqueId();
      } else {
         this.sender = null;
      }

      this.toPurge = toPurge;
      this.totalPurgeCount = toPurge.size();
      this.offlinePlayers = offlinePlayers;
   }

   public void run() {
      if (this.toPurge.isEmpty()) {
         this.finish();
      } else {
         Set<OfflinePlayer> playerPortion = new HashSet(5);
         Set<String> namePortion = new HashSet(5);

         int completed;
         for(completed = 0; completed < 5; ++completed) {
            int nextPosition = this.currentPage * 5 + completed;
            if (this.offlinePlayers.length <= nextPosition) {
               break;
            }

            OfflinePlayer offlinePlayer = this.offlinePlayers[nextPosition];
            if (offlinePlayer.getName() != null && this.toPurge.remove(offlinePlayer.getName().toLowerCase(Locale.ROOT))) {
               if (!this.permissionsManager.loadUserData(offlinePlayer)) {
                  this.logger.warning("Unable to check if the user " + offlinePlayer.getName() + " can be purged!");
               } else if (!this.permissionsManager.hasPermissionOffline((OfflinePlayer)offlinePlayer, PlayerStatePermission.BYPASS_PURGE)) {
                  playerPortion.add(offlinePlayer);
                  namePortion.add(offlinePlayer.getName());
               }
            }
         }

         if (!this.toPurge.isEmpty() && playerPortion.isEmpty()) {
            this.logger.info("Finished lookup of offlinePlayers. Begin looking purging player names only");
            Iterator var6 = this.toPurge.iterator();

            while(var6.hasNext()) {
               String name = (String)var6.next();
               if (!this.permissionsManager.hasPermissionOffline((String)name, PlayerStatePermission.BYPASS_PURGE)) {
                  namePortion.add(name);
               }
            }

            this.toPurge.clear();
         }

         ++this.currentPage;
         this.purgeService.executePurge(playerPortion, namePortion);
         if (this.currentPage % 20 == 0) {
            completed = this.totalPurgeCount - this.toPurge.size();
            this.sendMessage("[AuthMe] Purge progress " + completed + '/' + this.totalPurgeCount);
         }

      }
   }

   private void finish() {
      this.cancel();
      this.sendMessage(ChatColor.GREEN + "[AuthMe] Database has been purged successfully");
      this.logger.info("Purge finished!");
      this.purgeService.setPurging(false);
   }

   private void sendMessage(String message) {
      if (this.sender == null) {
         Bukkit.getConsoleSender().sendMessage(message);
      } else {
         Player player = Bukkit.getPlayer(this.sender);
         if (player != null) {
            player.sendMessage(message);
         }
      }

   }
}
