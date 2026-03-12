package fr.xephi.authme.process;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.login.ProcessSyncPlayerLogin;
import fr.xephi.authme.process.logout.ProcessSyncPlayerLogout;
import fr.xephi.authme.process.quit.ProcessSyncPlayerQuit;
import fr.xephi.authme.process.register.ProcessSyncEmailRegister;
import fr.xephi.authme.process.register.ProcessSyncPasswordRegister;
import fr.xephi.authme.service.BukkitService;
import java.util.List;
import org.bukkit.entity.Player;

public class SyncProcessManager {
   @Inject
   private BukkitService bukkitService;
   @Inject
   private ProcessSyncEmailRegister processSyncEmailRegister;
   @Inject
   private ProcessSyncPasswordRegister processSyncPasswordRegister;
   @Inject
   private ProcessSyncPlayerLogin processSyncPlayerLogin;
   @Inject
   private ProcessSyncPlayerLogout processSyncPlayerLogout;
   @Inject
   private ProcessSyncPlayerQuit processSyncPlayerQuit;

   public void processSyncEmailRegister(Player player) {
      this.runTask(() -> {
         this.processSyncEmailRegister.processEmailRegister(player);
      });
   }

   public void processSyncPasswordRegister(Player player) {
      this.runTask(() -> {
         this.processSyncPasswordRegister.processPasswordRegister(player);
      });
   }

   public void processSyncPlayerLogout(Player player) {
      this.runTask(() -> {
         this.processSyncPlayerLogout.processSyncLogout(player);
      });
   }

   public void processSyncPlayerLogin(Player player, boolean isFirstLogin, List<String> authsWithSameIp) {
      this.runTask(() -> {
         this.processSyncPlayerLogin.processPlayerLogin(player, isFirstLogin, authsWithSameIp);
      });
   }

   public void processSyncPlayerQuit(Player player, boolean wasLoggedIn) {
      this.runTask(() -> {
         this.processSyncPlayerQuit.processSyncQuit(player, wasLoggedIn);
      });
   }

   private void runTask(Runnable runnable) {
      this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(runnable);
   }
}
