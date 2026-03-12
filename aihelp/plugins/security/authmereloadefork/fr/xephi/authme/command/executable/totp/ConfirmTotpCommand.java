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
import fr.xephi.authme.security.totp.GenerateTotpService;
import fr.xephi.authme.security.totp.TotpAuthenticator;
import java.util.List;
import org.bukkit.entity.Player;

public class ConfirmTotpCommand extends PlayerCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(ConfirmTotpCommand.class);
   @Inject
   private GenerateTotpService generateTotpService;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private DataSource dataSource;
   @Inject
   private Messages messages;

   protected void runCommand(Player player, List<String> arguments) {
      PlayerAuth auth = this.playerCache.getAuth(player.getName());
      if (auth == null) {
         this.messages.send(player, MessageKey.NOT_LOGGED_IN);
      } else if (auth.getTotpKey() != null) {
         this.messages.send(player, MessageKey.TWO_FACTOR_ALREADY_ENABLED);
      } else {
         this.verifyTotpCodeConfirmation(player, auth, (String)arguments.get(0));
      }

   }

   private void verifyTotpCodeConfirmation(Player player, PlayerAuth auth, String inputTotpCode) {
      TotpAuthenticator.TotpGenerationResult totpDetails = this.generateTotpService.getGeneratedTotpKey(player);
      if (totpDetails == null) {
         this.messages.send(player, MessageKey.TWO_FACTOR_ENABLE_ERROR_NO_CODE);
      } else {
         boolean isCodeValid = this.generateTotpService.isTotpCodeCorrectForGeneratedTotpKey(player, inputTotpCode);
         if (isCodeValid) {
            this.generateTotpService.removeGenerateTotpKey(player);
            this.insertTotpKeyIntoDatabase(player, auth, totpDetails);
         } else {
            this.messages.send(player, MessageKey.TWO_FACTOR_ENABLE_ERROR_WRONG_CODE);
         }
      }

   }

   private void insertTotpKeyIntoDatabase(Player player, PlayerAuth auth, TotpAuthenticator.TotpGenerationResult totpDetails) {
      if (this.dataSource.setTotpKey(player.getName(), totpDetails.getTotpKey())) {
         this.messages.send(player, MessageKey.TWO_FACTOR_ENABLE_SUCCESS);
         auth.setTotpKey(totpDetails.getTotpKey());
         this.playerCache.updatePlayer(auth);
         this.logger.info("Player '" + player.getName() + "' has successfully added a TOTP key to their account");
      } else {
         this.messages.send(player, MessageKey.ERROR);
      }

   }
}
