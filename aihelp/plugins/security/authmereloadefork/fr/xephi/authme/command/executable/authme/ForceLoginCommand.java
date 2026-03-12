package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerPermission;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.BukkitService;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceLoginCommand implements ExecutableCommand {
   @Inject
   private PermissionsManager permissionsManager;
   @Inject
   private Management management;
   @Inject
   private BukkitService bukkitService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String playerName = arguments.isEmpty() ? sender.getName() : (String)arguments.get(0);
      Player player = this.bukkitService.getPlayerExact(playerName);
      if (player != null && player.isOnline()) {
         if (!this.permissionsManager.hasPermission(player, PlayerPermission.CAN_LOGIN_BE_FORCED)) {
            sender.sendMessage("You cannot force login the player " + playerName + "!");
         } else {
            this.management.forceLogin(player);
            sender.sendMessage("Force login for " + playerName + " performed!");
         }
      } else {
         sender.sendMessage("Player needs to be online!");
      }

   }
}
