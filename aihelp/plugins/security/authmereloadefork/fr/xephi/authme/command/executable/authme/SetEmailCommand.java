package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SetEmailCommand implements ExecutableCommand {
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService commonService;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private ValidationService validationService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String playerName = (String)arguments.get(0);
      String playerEmail = (String)arguments.get(1);
      if (!this.validationService.validateEmail(playerEmail)) {
         this.commonService.send(sender, MessageKey.INVALID_EMAIL);
      } else {
         this.bukkitService.runTaskOptionallyAsync(() -> {
            PlayerAuth auth = this.dataSource.getAuth(playerName);
            if (auth == null) {
               this.commonService.send(sender, MessageKey.UNKNOWN_USER);
            } else if (!this.validationService.isEmailFreeForRegistration(playerEmail, sender)) {
               this.commonService.send(sender, MessageKey.EMAIL_ALREADY_USED_ERROR);
            } else {
               auth.setEmail(playerEmail);
               if (!this.dataSource.updateEmail(auth)) {
                  this.commonService.send(sender, MessageKey.ERROR);
               } else {
                  if (this.playerCache.getAuth(playerName) != null) {
                     this.playerCache.updatePlayer(auth);
                  }

                  this.commonService.send(sender, MessageKey.EMAIL_CHANGED_SUCCESS);
               }
            }
         });
      }
   }
}
