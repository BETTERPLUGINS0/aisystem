package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.RestoreSessionEvent;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.entity.Player;

public class SessionService implements Reloadable {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(SessionService.class);
   private final CommonService service;
   private final BukkitService bukkitService;
   private final DataSource database;
   private boolean isEnabled;

   @Inject
   SessionService(CommonService service, BukkitService bukkitService, DataSource database) {
      this.service = service;
      this.bukkitService = bukkitService;
      this.database = database;
      this.reload();
   }

   public boolean canResumeSession(Player player) {
      String name = player.getName();
      if (this.isEnabled && this.database.hasSession(name)) {
         this.database.setUnlogged(name);
         this.database.revokeSession(name);
         PlayerAuth auth = this.database.getAuth(name);
         SessionState state = this.fetchSessionStatus(auth, player);
         if (state.equals(SessionState.VALID)) {
            RestoreSessionEvent event = (RestoreSessionEvent)this.bukkitService.createAndCallEvent((isAsync) -> {
               return new RestoreSessionEvent(player, isAsync);
            });
            return !event.isCancelled();
         }

         if (state.equals(SessionState.IP_CHANGED)) {
            this.service.send(player, MessageKey.SESSION_EXPIRED);
         }
      }

      return false;
   }

   private SessionState fetchSessionStatus(PlayerAuth auth, Player player) {
      if (auth == null) {
         this.logger.warning("No PlayerAuth in database for '" + player.getName() + "' during session check");
         return SessionState.NOT_VALID;
      } else if (auth.getLastLogin() == null) {
         return SessionState.NOT_VALID;
      } else {
         long timeSinceLastLogin = System.currentTimeMillis() - auth.getLastLogin();
         if (timeSinceLastLogin > 0L && timeSinceLastLogin < (long)(Integer)this.service.getProperty(PluginSettings.SESSIONS_TIMEOUT) * 60000L) {
            return PlayerUtils.getPlayerIp(player).equals(auth.getLastIp()) ? SessionState.VALID : SessionState.IP_CHANGED;
         } else {
            return SessionState.OUTDATED;
         }
      }
   }

   public void grantSession(String name) {
      if (this.isEnabled) {
         this.database.grantSession(name);
      }

   }

   public void revokeSession(String name) {
      this.database.revokeSession(name);
   }

   public void reload() {
      this.isEnabled = (Boolean)this.service.getProperty(PluginSettings.SESSIONS_ENABLED);
   }
}
