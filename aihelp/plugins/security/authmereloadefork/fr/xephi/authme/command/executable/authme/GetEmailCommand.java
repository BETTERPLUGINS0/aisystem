package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValue;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import org.bukkit.command.CommandSender;

public class GetEmailCommand implements ExecutableCommand {
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService commonService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String playerName = arguments.isEmpty() ? sender.getName() : (String)arguments.get(0);
      DataSourceValue<String> email = this.dataSource.getEmail(playerName);
      if (email.rowExists()) {
         sender.sendMessage("[AuthMe] " + playerName + "'s email: " + (String)email.getValue());
      } else {
         this.commonService.send(sender, MessageKey.UNKNOWN_USER);
      }

   }
}
