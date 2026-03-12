package fr.xephi.authme.process.quit;

import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.UniversalScheduler;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.SynchronousProcess;
import fr.xephi.authme.settings.commandconfig.CommandManager;
import org.bukkit.entity.Player;

public class ProcessSyncPlayerQuit implements SynchronousProcess {
   @Inject
   private LimboService limboService;
   @Inject
   private CommandManager commandManager;

   public void processSyncQuit(Player player, boolean wasLoggedIn) {
      if (wasLoggedIn) {
         this.commandManager.runCommandsOnLogout(player);
      } else {
         this.limboService.restoreData(player);
         if (!UniversalScheduler.isFolia) {
         }
      }

      player.leaveVehicle();
   }
}
