package fr.xephi.authme.command.executable.verification;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.VerificationCodeManager;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import org.bukkit.entity.Player;

public class VerificationCommand extends PlayerCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(VerificationCommand.class);
   @Inject
   private CommonService commonService;
   @Inject
   private VerificationCodeManager codeManager;

   public void runCommand(Player player, List<String> arguments) {
      String playerName = player.getName();
      if (!this.codeManager.canSendMail()) {
         this.logger.warning("Mail API is not set");
         this.commonService.send(player, MessageKey.INCOMPLETE_EMAIL_SETTINGS);
      } else {
         if (this.codeManager.isVerificationRequired(player)) {
            if (this.codeManager.isCodeRequired(playerName)) {
               if (this.codeManager.checkCode(playerName, (String)arguments.get(0))) {
                  this.commonService.send(player, MessageKey.VERIFICATION_CODE_VERIFIED);
               } else {
                  this.commonService.send(player, MessageKey.INCORRECT_VERIFICATION_CODE);
               }
            } else {
               this.commonService.send(player, MessageKey.VERIFICATION_CODE_EXPIRED);
            }
         } else if (this.codeManager.hasEmail(playerName)) {
            this.commonService.send(player, MessageKey.VERIFICATION_CODE_ALREADY_VERIFIED);
         } else {
            this.commonService.send(player, MessageKey.VERIFICATION_CODE_EMAIL_NEEDED);
            this.commonService.send(player, MessageKey.ADD_EMAIL_MESSAGE);
         }

      }
   }

   public MessageKey getArgumentsMismatchMessage() {
      return MessageKey.USAGE_VERIFICATION_CODE;
   }
}
