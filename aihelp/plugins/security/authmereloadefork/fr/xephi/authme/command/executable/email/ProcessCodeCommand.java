package fr.xephi.authme.command.executable.email;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.PasswordRecoveryService;
import fr.xephi.authme.service.RecoveryCodeService;
import java.util.List;
import org.bukkit.entity.Player;

public class ProcessCodeCommand extends PlayerCommand {
   @Inject
   private CommonService commonService;
   @Inject
   private RecoveryCodeService codeService;
   @Inject
   private PasswordRecoveryService recoveryService;

   protected void runCommand(Player player, List<String> arguments) {
      String name = player.getName();
      String code = (String)arguments.get(0);
      if (this.codeService.hasTriesLeft(name)) {
         if (this.codeService.isCodeValid(name, code)) {
            this.commonService.send(player, MessageKey.RECOVERY_CODE_CORRECT);
            this.recoveryService.addSuccessfulRecovery(player);
            this.codeService.removeCode(name);
         } else {
            this.commonService.send(player, MessageKey.INCORRECT_RECOVERY_CODE, Integer.toString(this.codeService.getTriesLeft(name)));
         }
      } else {
         this.codeService.removeCode(name);
         this.commonService.send(player, MessageKey.RECOVERY_TRIES_EXCEEDED);
      }

   }
}
