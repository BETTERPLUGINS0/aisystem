package fr.xephi.authme.command.executable.logout;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.Management;
import java.util.List;
import org.bukkit.entity.Player;

public class LogoutCommand extends PlayerCommand {
   @Inject
   private Management management;

   public void runCommand(Player player, List<String> arguments) {
      this.management.performLogout(player);
   }
}
