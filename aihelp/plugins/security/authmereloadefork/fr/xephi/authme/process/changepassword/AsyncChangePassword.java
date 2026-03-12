package fr.xephi.authme.process.changepassword;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.service.CommonService;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AsyncChangePassword implements AsynchronousProcess {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(AsyncChangePassword.class);
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService commonService;
   @Inject
   private PasswordSecurity passwordSecurity;
   @Inject
   private PlayerCache playerCache;

   AsyncChangePassword() {
   }

   public void changePassword(Player player, String oldPassword, String newPassword) {
      String name = player.getName().toLowerCase(Locale.ROOT);
      PlayerAuth auth = this.playerCache.getAuth(name);
      if (this.passwordSecurity.comparePassword(oldPassword, auth.getPassword(), player.getName())) {
         HashedPassword hashedPassword = this.passwordSecurity.computeHash(newPassword, name);
         auth.setPassword(hashedPassword);
         if (!this.dataSource.updatePassword(auth)) {
            this.commonService.send(player, MessageKey.ERROR);
            return;
         }

         this.playerCache.updatePlayer(auth);
         this.commonService.send(player, MessageKey.PASSWORD_CHANGED_SUCCESS);
         this.logger.info(player.getName() + " changed his password");
      } else {
         this.commonService.send(player, MessageKey.WRONG_PASSWORD);
      }

   }

   public void changePasswordAsAdmin(CommandSender sender, String playerName, String newPassword) {
      String lowerCaseName = playerName.toLowerCase(Locale.ROOT);
      if (!this.playerCache.isAuthenticated(lowerCaseName) && !this.dataSource.isAuthAvailable(lowerCaseName)) {
         if (sender == null) {
            this.logger.warning("Tried to change password for user " + lowerCaseName + " but it doesn't exist!");
         } else {
            this.commonService.send(sender, MessageKey.UNKNOWN_USER);
         }

      } else {
         HashedPassword hashedPassword = this.passwordSecurity.computeHash(newPassword, lowerCaseName);
         if (this.dataSource.updatePassword(lowerCaseName, hashedPassword)) {
            if (sender != null) {
               this.commonService.send(sender, MessageKey.PASSWORD_CHANGED_SUCCESS);
               this.logger.info(sender.getName() + " changed password of " + lowerCaseName);
            } else {
               this.logger.info("Changed password of " + lowerCaseName);
            }
         } else {
            if (sender != null) {
               this.commonService.send(sender, MessageKey.ERROR);
            }

            this.logger.warning("An error occurred while changing password for user " + lowerCaseName + "!");
         }

      }
   }
}
