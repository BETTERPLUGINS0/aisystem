package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;

public class PurgeLastPositionCommand implements ExecutableCommand {
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService commonService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String playerName = arguments.isEmpty() ? sender.getName() : (String)arguments.get(0);
      if ("*".equals(playerName)) {
         Iterator var4 = this.dataSource.getAllAuths().iterator();

         while(var4.hasNext()) {
            PlayerAuth auth = (PlayerAuth)var4.next();
            resetLastPosition(auth);
            this.dataSource.updateQuitLoc(auth);
         }

         sender.sendMessage("All players last position locations are now reset");
      } else {
         PlayerAuth auth = this.dataSource.getAuth(playerName);
         if (auth == null) {
            this.commonService.send(sender, MessageKey.UNKNOWN_USER);
            return;
         }

         resetLastPosition(auth);
         this.dataSource.updateQuitLoc(auth);
         sender.sendMessage(playerName + "'s last position location is now reset");
      }

   }

   private static void resetLastPosition(PlayerAuth auth) {
      auth.setQuitLocX(0.0D);
      auth.setQuitLocY(0.0D);
      auth.setQuitLocZ(0.0D);
      auth.setWorld("world");
   }
}
