package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TotpViewStatusCommand implements ExecutableCommand {
   @Inject
   private DataSource dataSource;
   @Inject
   private Messages messages;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String player = (String)arguments.get(0);
      PlayerAuth auth = this.dataSource.getAuth(player);
      if (auth == null) {
         this.messages.send(sender, MessageKey.UNKNOWN_USER);
      } else if (auth.getTotpKey() == null) {
         sender.sendMessage(ChatColor.RED + "Player '" + player + "' does NOT have two-factor auth enabled");
      } else {
         sender.sendMessage(ChatColor.DARK_GREEN + "Player '" + player + "' has enabled two-factor authentication");
      }

   }
}
