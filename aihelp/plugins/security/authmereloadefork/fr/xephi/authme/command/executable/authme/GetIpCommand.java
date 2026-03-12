package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.util.PlayerUtils;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetIpCommand implements ExecutableCommand {
   @Inject
   private BukkitService bukkitService;
   @Inject
   private DataSource dataSource;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String playerName = (String)arguments.get(0);
      Player player = this.bukkitService.getPlayerExact(playerName);
      PlayerAuth auth = this.dataSource.getAuth(playerName);
      if (player != null) {
         sender.sendMessage("Current IP of " + player.getName() + " is " + PlayerUtils.getPlayerIp(player) + ":" + player.getAddress().getPort());
      }

      if (auth == null) {
         String displayName = player == null ? playerName : player.getName();
         sender.sendMessage(displayName + " is not registered in the database");
      } else {
         sender.sendMessage("Database: last IP: " + auth.getLastIp() + ", registration IP: " + auth.getRegistrationIp());
      }

   }
}
