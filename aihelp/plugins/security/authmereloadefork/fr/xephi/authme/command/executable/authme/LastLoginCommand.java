package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import java.util.Date;
import java.util.List;
import org.bukkit.command.CommandSender;

public class LastLoginCommand implements ExecutableCommand {
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService commonService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String playerName = arguments.isEmpty() ? sender.getName() : (String)arguments.get(0);
      PlayerAuth auth = this.dataSource.getAuth(playerName);
      if (auth == null) {
         this.commonService.send(sender, MessageKey.UNKNOWN_USER);
      } else {
         Long lastLogin = auth.getLastLogin();
         String lastLoginDate = lastLogin == null ? "never" : (new Date(lastLogin)).toString();
         sender.sendMessage("[AuthMe] " + playerName + " last login: " + lastLoginDate);
         if (lastLogin != null) {
            sender.sendMessage("[AuthMe] The player " + playerName + " last logged in " + createLastLoginIntervalMessage(lastLogin) + " ago");
         }

         sender.sendMessage("[AuthMe] Last player's IP: " + auth.getLastIp());
      }
   }

   private static String createLastLoginIntervalMessage(long lastLogin) {
      long diff = System.currentTimeMillis() - lastLogin;
      return (int)(diff / 86400000L) + " days " + (int)(diff / 3600000L % 24L) + " hours " + (int)(diff / 60000L % 60L) + " mins " + (int)(diff / 1000L % 60L) + " secs";
   }
}
