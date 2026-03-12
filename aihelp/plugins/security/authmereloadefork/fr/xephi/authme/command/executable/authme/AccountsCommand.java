package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;

public class AccountsCommand implements ExecutableCommand {
   @Inject
   private DataSource dataSource;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private CommonService commonService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String playerName = arguments.isEmpty() ? sender.getName() : (String)arguments.get(0);
      if (playerName.contains(".")) {
         this.bukkitService.runTaskAsynchronously(() -> {
            List<String> accountList = this.dataSource.getAllAuthsByIp(playerName);
            if (accountList.isEmpty()) {
               sender.sendMessage("[AuthMe] This IP does not exist in the database.");
            } else if (accountList.size() == 1) {
               sender.sendMessage("[AuthMe] " + playerName + " is a single account player");
            } else {
               outputAccountsList(sender, playerName, accountList);
            }

         });
      } else {
         this.bukkitService.runTaskAsynchronously(() -> {
            PlayerAuth auth = this.dataSource.getAuth(playerName.toLowerCase(Locale.ROOT));
            if (auth == null) {
               this.commonService.send(sender, MessageKey.UNKNOWN_USER);
            } else if (auth.getLastIp() == null) {
               sender.sendMessage("No known last IP address for player");
            } else {
               List<String> accountList = this.dataSource.getAllAuthsByIp(auth.getLastIp());
               if (accountList.isEmpty()) {
                  this.commonService.send(sender, MessageKey.UNKNOWN_USER);
               } else if (accountList.size() == 1) {
                  sender.sendMessage("[AuthMe] " + playerName + " is a single account player");
               } else {
                  outputAccountsList(sender, playerName, accountList);
               }

            }
         });
      }

   }

   private static void outputAccountsList(CommandSender sender, String playerName, List<String> accountList) {
      sender.sendMessage("[AuthMe] " + playerName + " has " + accountList.size() + " accounts.");
      String message = "[AuthMe] " + String.join(", ", accountList) + ".";
      sender.sendMessage(message);
   }
}
