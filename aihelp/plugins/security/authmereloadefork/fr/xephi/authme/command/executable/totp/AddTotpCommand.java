package fr.xephi.authme.command.executable.totp;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.security.totp.GenerateTotpService;
import fr.xephi.authme.security.totp.TotpAuthenticator;
import java.util.List;
import org.bukkit.entity.Player;

public class AddTotpCommand extends PlayerCommand {
   @Inject
   private GenerateTotpService generateTotpService;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private Messages messages;

   protected void runCommand(Player player, List<String> arguments) {
      PlayerAuth auth = this.playerCache.getAuth(player.getName());
      if (auth == null) {
         this.messages.send(player, MessageKey.NOT_LOGGED_IN);
      } else if (auth.getTotpKey() == null) {
         TotpAuthenticator.TotpGenerationResult createdTotpInfo = this.generateTotpService.generateTotpKey(player);
         this.messages.send(player, MessageKey.TWO_FACTOR_CREATE, createdTotpInfo.getTotpKey(), createdTotpInfo.getAuthenticatorQrCodeUrl());
         this.messages.send(player, MessageKey.TWO_FACTOR_CREATE_CONFIRMATION_REQUIRED);
      } else {
         this.messages.send(player, MessageKey.TWO_FACTOR_ALREADY_ENABLED);
      }

   }
}
