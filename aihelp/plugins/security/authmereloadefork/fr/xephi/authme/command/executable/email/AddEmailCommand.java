package fr.xephi.authme.command.executable.email;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import org.bukkit.entity.Player;

public class AddEmailCommand extends PlayerCommand {
   @Inject
   private Management management;
   @Inject
   private CommonService commonService;

   public void runCommand(Player player, List<String> arguments) {
      String email = (String)arguments.get(0);
      String emailConfirmation = (String)arguments.get(1);
      if (email.equals(emailConfirmation)) {
         this.management.performAddEmail(player, email);
      } else {
         this.commonService.send(player, MessageKey.CONFIRM_EMAIL_MESSAGE);
      }

   }

   public MessageKey getArgumentsMismatchMessage() {
      return MessageKey.USAGE_ADD_EMAIL;
   }
}
