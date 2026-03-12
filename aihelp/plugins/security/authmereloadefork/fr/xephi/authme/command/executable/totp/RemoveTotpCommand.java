package fr.xephi.authme.command.executable.totp;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.totp.TotpAuthenticator;
import java.util.List;
import org.bukkit.entity.Player;

public class RemoveTotpCommand extends PlayerCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(RemoveTotpCommand.class);
   @Inject
   private DataSource dataSource;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private TotpAuthenticator totpAuthenticator;
   @Inject
   private Messages messages;

   protected void runCommand(Player player, List<String> arguments) {
      PlayerAuth auth = this.playerCache.getAuth(player.getName());
      if (auth == null) {
         this.messages.send(player, MessageKey.NOT_LOGGED_IN);
      } else if (auth.getTotpKey() == null) {
         this.messages.send(player, MessageKey.TWO_FACTOR_NOT_ENABLED_ERROR);
      } else if (this.totpAuthenticator.checkCode(auth, (String)arguments.get(0))) {
         this.removeTotpKeyFromDatabase(player, auth);
      } else {
         this.messages.send(player, MessageKey.TWO_FACTOR_INVALID_CODE);
      }

   }

   private void removeTotpKeyFromDatabase(Player player, PlayerAuth auth) {
      if (this.dataSource.removeTotpKey(auth.getNickname())) {
         auth.setTotpKey((String)null);
         this.playerCache.updatePlayer(auth);
         this.messages.send(player, MessageKey.TWO_FACTOR_REMOVED_SUCCESS);
         this.logger.info("Player '" + player.getName() + "' removed their TOTP key");
      } else {
         this.messages.send(player, MessageKey.ERROR);
      }

   }
}
