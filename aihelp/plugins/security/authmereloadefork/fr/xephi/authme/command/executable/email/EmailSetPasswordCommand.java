package fr.xephi.authme.command.executable.email;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.PasswordRecoveryService;
import fr.xephi.authme.service.ValidationService;
import java.util.List;
import org.bukkit.entity.Player;

public class EmailSetPasswordCommand extends PlayerCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(EmailSetPasswordCommand.class);
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService commonService;
   @Inject
   private PasswordRecoveryService recoveryService;
   @Inject
   private PasswordSecurity passwordSecurity;
   @Inject
   private ValidationService validationService;

   protected void runCommand(Player player, List<String> arguments) {
      if (this.recoveryService.canChangePassword(player)) {
         String name = player.getName();
         String password = (String)arguments.get(0);
         ValidationService.ValidationResult result = this.validationService.validatePassword(password, name);
         if (!result.hasError()) {
            HashedPassword hashedPassword = this.passwordSecurity.computeHash(password, name);
            this.dataSource.updatePassword(name, hashedPassword);
            this.recoveryService.removeFromSuccessfulRecovery(player);
            this.logger.info("Player '" + name + "' has changed their password from recovery");
            this.commonService.send(player, MessageKey.PASSWORD_CHANGED_SUCCESS);
         } else {
            this.commonService.send(player, result.getMessageKey(), result.getArgs());
         }
      } else {
         this.commonService.send(player, MessageKey.CHANGE_PASSWORD_EXPIRED);
      }

   }
}
