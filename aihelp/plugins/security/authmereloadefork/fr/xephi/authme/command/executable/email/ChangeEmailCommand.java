package fr.xephi.authme.command.executable.email;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.VerificationCodeManager;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import org.bukkit.entity.Player;

public class ChangeEmailCommand extends PlayerCommand {
   @Inject
   private Management management;
   @Inject
   private CommonService commonService;
   @Inject
   private VerificationCodeManager codeManager;

   public void runCommand(Player player, List<String> arguments) {
      String playerName = player.getName();
      if (this.codeManager.isVerificationRequired(player)) {
         this.codeManager.codeExistOrGenerateNew(playerName);
         this.commonService.send(player, MessageKey.VERIFICATION_CODE_REQUIRED);
      } else {
         String playerMailOld = (String)arguments.get(0);
         String playerMailNew = (String)arguments.get(1);
         this.management.performChangeEmail(player, playerMailOld, playerMailNew);
      }
   }

   public MessageKey getArgumentsMismatchMessage() {
      return MessageKey.USAGE_CHANGE_EMAIL;
   }
}
