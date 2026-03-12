package fr.xephi.authme.command.executable.totp;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.data.limbo.LimboPlayerState;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.process.login.AsynchronousLogin;
import fr.xephi.authme.security.totp.TotpAuthenticator;
import java.util.List;
import org.bukkit.entity.Player;

public class TotpCodeCommand extends PlayerCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(TotpCodeCommand.class);
   @Inject
   private LimboService limboService;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private Messages messages;
   @Inject
   private TotpAuthenticator totpAuthenticator;
   @Inject
   private DataSource dataSource;
   @Inject
   private AsynchronousLogin asynchronousLogin;

   protected void runCommand(Player player, List<String> arguments) {
      if (this.playerCache.isAuthenticated(player.getName())) {
         this.messages.send(player, MessageKey.ALREADY_LOGGED_IN_ERROR);
      } else {
         PlayerAuth auth = this.dataSource.getAuth(player.getName());
         if (auth == null) {
            this.messages.send(player, MessageKey.REGISTER_MESSAGE);
         } else {
            LimboPlayer limbo = this.limboService.getLimboPlayer(player.getName());
            if (limbo != null && limbo.getState() == LimboPlayerState.TOTP_REQUIRED) {
               this.processCode(player, auth, (String)arguments.get(0));
            } else {
               this.logger.debug(() -> {
                  return "Aborting TOTP check for player '" + player.getName() + "'. Invalid limbo state: " + (limbo == null ? "no limbo" : limbo.getState());
               });
               this.messages.send(player, MessageKey.LOGIN_MESSAGE);
            }

         }
      }
   }

   private void processCode(Player player, PlayerAuth auth, String inputCode) {
      boolean isCodeValid = this.totpAuthenticator.checkCode(auth, inputCode);
      if (isCodeValid) {
         this.logger.debug("Successfully checked TOTP code for `{0}`", (Object)player.getName());
         this.asynchronousLogin.performLogin(player, auth);
      } else {
         this.logger.debug("Input TOTP code was invalid for player `{0}`", (Object)player.getName());
         this.messages.send(player, MessageKey.TWO_FACTOR_INVALID_CODE);
      }

   }
}
