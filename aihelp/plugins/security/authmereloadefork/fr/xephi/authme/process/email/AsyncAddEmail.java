package fr.xephi.authme.process.email;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.EmailChangedEvent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.util.Utils;
import java.util.Locale;
import org.bukkit.entity.Player;

public class AsyncAddEmail implements AsynchronousProcess {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(AsyncAddEmail.class);
   @Inject
   private CommonService service;
   @Inject
   private DataSource dataSource;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private ValidationService validationService;
   @Inject
   private BukkitService bukkitService;

   AsyncAddEmail() {
   }

   public void addEmail(Player player, String email) {
      String playerName = player.getName().toLowerCase(Locale.ROOT);
      if (this.playerCache.isAuthenticated(playerName)) {
         PlayerAuth auth = this.playerCache.getAuth(playerName);
         String currentEmail = auth.getEmail();
         if (!Utils.isEmailEmpty(currentEmail)) {
            this.service.send(player, MessageKey.USAGE_CHANGE_EMAIL);
         } else if (!this.validationService.validateEmail(email)) {
            this.service.send(player, MessageKey.INVALID_EMAIL);
         } else if (!this.validationService.isEmailFreeForRegistration(email, player)) {
            this.service.send(player, MessageKey.EMAIL_ALREADY_USED_ERROR);
         } else {
            EmailChangedEvent event = (EmailChangedEvent)this.bukkitService.createAndCallEvent((isAsync) -> {
               return new EmailChangedEvent(player, (String)null, email, isAsync);
            });
            if (event.isCancelled()) {
               this.logger.info("Could not add email to player '" + player + "' – event was cancelled");
               this.service.send(player, MessageKey.EMAIL_ADD_NOT_ALLOWED);
               return;
            }

            auth.setEmail(email);
            if (this.dataSource.updateEmail(auth)) {
               this.playerCache.updatePlayer(auth);
               this.service.send(player, MessageKey.EMAIL_ADDED_SUCCESS);
            } else {
               this.logger.warning("Could not save email for player '" + player + "'");
               this.service.send(player, MessageKey.ERROR);
            }
         }
      } else {
         this.sendUnloggedMessage(player);
      }

   }

   private void sendUnloggedMessage(Player player) {
      if (this.dataSource.isAuthAvailable(player.getName())) {
         this.service.send(player, MessageKey.LOGIN_MESSAGE);
      } else {
         this.service.send(player, MessageKey.REGISTER_MESSAGE);
      }

   }
}
