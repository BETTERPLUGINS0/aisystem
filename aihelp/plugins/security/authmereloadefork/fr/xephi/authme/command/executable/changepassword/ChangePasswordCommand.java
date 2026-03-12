package fr.xephi.authme.command.executable.changepassword;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.VerificationCodeManager;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.util.List;
import java.util.Locale;
import org.bukkit.entity.Player;

public class ChangePasswordCommand extends PlayerCommand {
   @Inject
   private CommonService commonService;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private ValidationService validationService;
   @Inject
   private Management management;
   @Inject
   private VerificationCodeManager codeManager;

   public void runCommand(Player player, List<String> arguments) {
      String name = player.getName().toLowerCase(Locale.ROOT);
      if (!this.playerCache.isAuthenticated(name)) {
         this.commonService.send(player, MessageKey.NOT_LOGGED_IN);
      } else if ((Boolean)this.commonService.getProperty(SecuritySettings.CHANGE_PASSWORD_EMAIL_VERIFICATION_REQUIRED) && this.codeManager.isVerificationRequired(player)) {
         this.codeManager.codeExistOrGenerateNew(name);
         this.commonService.send(player, MessageKey.VERIFICATION_CODE_REQUIRED);
      } else {
         String oldPassword = (String)arguments.get(0);
         String newPassword = (String)arguments.get(1);
         ValidationService.ValidationResult passwordValidation = this.validationService.validatePassword(newPassword, name);
         if (passwordValidation.hasError()) {
            this.commonService.send(player, passwordValidation.getMessageKey(), passwordValidation.getArgs());
         } else {
            this.management.performPasswordChange(player, oldPassword, newPassword);
         }
      }
   }

   protected String getAlternativeCommand() {
      return "/authme password <playername> <password>";
   }

   public MessageKey getArgumentsMismatchMessage() {
      return MessageKey.USAGE_CHANGE_PASSWORD;
   }
}
