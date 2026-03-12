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
import java.util.Locale;
import org.bukkit.entity.Player;

public class AsyncChangeEmail implements AsynchronousProcess {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(AsyncChangeEmail.class);
   @Inject
   private CommonService service;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private DataSource dataSource;
   @Inject
   private ValidationService validationService;
   @Inject
   private BukkitService bukkitService;

   AsyncChangeEmail() {
   }

   public void changeEmail(Player player, String oldEmail, String newEmail) {
      String playerName = player.getName().toLowerCase(Locale.ROOT);
      if (this.playerCache.isAuthenticated(playerName)) {
         PlayerAuth auth = this.playerCache.getAuth(playerName);
         String currentEmail = auth.getEmail();
         if (currentEmail == null) {
            this.service.send(player, MessageKey.USAGE_ADD_EMAIL);
         } else if (newEmail != null && this.validationService.validateEmail(newEmail)) {
            if (!oldEmail.equalsIgnoreCase(currentEmail)) {
               this.service.send(player, MessageKey.INVALID_OLD_EMAIL);
            } else if (!this.validationService.isEmailFreeForRegistration(newEmail, player)) {
               this.service.send(player, MessageKey.EMAIL_ALREADY_USED_ERROR);
            } else {
               this.saveNewEmail(auth, player, oldEmail, newEmail);
            }
         } else {
            this.service.send(player, MessageKey.INVALID_NEW_EMAIL);
         }
      } else {
         this.outputUnloggedMessage(player);
      }

   }

   private void saveNewEmail(PlayerAuth auth, Player player, String oldEmail, String newEmail) {
      EmailChangedEvent event = (EmailChangedEvent)this.bukkitService.createAndCallEvent((isAsync) -> {
         return new EmailChangedEvent(player, oldEmail, newEmail, isAsync);
      });
      if (event.isCancelled()) {
         this.logger.info("Could not change email for player '" + player + "' – event was cancelled");
         this.service.send(player, MessageKey.EMAIL_CHANGE_NOT_ALLOWED);
      } else {
         auth.setEmail(newEmail);
         if (this.dataSource.updateEmail(auth)) {
            this.playerCache.updatePlayer(auth);
            this.service.send(player, MessageKey.EMAIL_CHANGED_SUCCESS);
         } else {
            this.service.send(player, MessageKey.ERROR);
         }

      }
   }

   private void outputUnloggedMessage(Player player) {
      if (this.dataSource.isAuthAvailable(player.getName())) {
         this.service.send(player, MessageKey.LOGIN_MESSAGE);
      } else {
         this.service.send(player, MessageKey.REGISTER_MESSAGE);
      }

   }
}
