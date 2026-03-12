package fr.xephi.authme.command.executable.login;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.Management;
import java.util.List;
import org.bukkit.entity.Player;

public class LoginCommand extends PlayerCommand {
   @Inject
   private Management management;

   public void runCommand(Player player, List<String> arguments) {
      String password = (String)arguments.get(0);
      this.management.performLogin(player, password);
   }

   public MessageKey getArgumentsMismatchMessage() {
      return MessageKey.USAGE_LOGIN;
   }

   protected String getAlternativeCommand() {
      return "/authme forcelogin <player>";
   }
}
