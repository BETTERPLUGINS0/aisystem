package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class RegisterAdminCommand implements ExecutableCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(RegisterAdminCommand.class);
   @Inject
   private PasswordSecurity passwordSecurity;
   @Inject
   private CommonService commonService;
   @Inject
   private DataSource dataSource;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private ValidationService validationService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String playerName = (String)arguments.get(0);
      String playerPass = (String)arguments.get(1);
      String playerNameLowerCase = playerName.toLowerCase(Locale.ROOT);
      ValidationService.ValidationResult passwordValidation = this.validationService.validatePassword(playerPass, playerName);
      if (passwordValidation.hasError()) {
         this.commonService.send(sender, passwordValidation.getMessageKey(), passwordValidation.getArgs());
      } else {
         this.bukkitService.runTaskOptionallyAsync(() -> {
            if (this.dataSource.isAuthAvailable(playerNameLowerCase)) {
               this.commonService.send(sender, MessageKey.NAME_ALREADY_REGISTERED);
            } else {
               HashedPassword hashedPassword = this.passwordSecurity.computeHash(playerPass, playerNameLowerCase);
               PlayerAuth auth = PlayerAuth.builder().name(playerNameLowerCase).realName(playerName).password(hashedPassword).registrationDate(System.currentTimeMillis()).build();
               if (!this.dataSource.saveAuth(auth)) {
                  this.commonService.send(sender, MessageKey.ERROR);
               } else {
                  this.commonService.send(sender, MessageKey.REGISTER_SUCCESS);
                  this.logger.info(sender.getName() + " registered " + playerName);
                  Player player = this.bukkitService.getPlayerExact(playerName);
                  if (player != null) {
                     this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
                        this.bukkitService.runTaskIfFolia((Entity)player, () -> {
                           player.kickPlayer(this.commonService.retrieveSingleMessage(player, MessageKey.KICK_FOR_ADMIN_REGISTER));
                        });
                     });
                  }

               }
            }
         });
      }
   }
}
