package fr.xephi.authme.process.logout;

import fr.xephi.authme.data.VerificationCodeManager;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.process.SyncProcessManager;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.SessionService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.service.bungeecord.MessageType;
import fr.xephi.authme.service.velocity.VMessageType;
import fr.xephi.authme.service.velocity.VelocitySender;
import java.util.Locale;
import org.bukkit.entity.Player;

public class AsynchronousLogout implements AsynchronousProcess {
   @Inject
   private DataSource database;
   @Inject
   private CommonService service;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private VerificationCodeManager codeManager;
   @Inject
   private SyncProcessManager syncProcessManager;
   @Inject
   private SessionService sessionService;
   @Inject
   private BungeeSender bungeeSender;
   @Inject
   private VelocitySender velocitySender;

   AsynchronousLogout() {
   }

   public void logout(Player player) {
      String name = player.getName().toLowerCase(Locale.ROOT);
      if (!this.playerCache.isAuthenticated(name)) {
         this.service.send(player, MessageKey.NOT_LOGGED_IN);
      } else {
         PlayerAuth auth = this.playerCache.getAuth(name);
         this.database.updateSession(auth);
         auth.setQuitLocation(player.getLocation());
         this.database.updateQuitLoc(auth);
         this.playerCache.removePlayer(name);
         this.codeManager.unverify(name);
         this.database.setUnlogged(name);
         this.sessionService.revokeSession(name);
         this.bungeeSender.sendAuthMeBungeecordMessage(player, MessageType.LOGOUT);
         this.velocitySender.sendAuthMeVelocityMessage(player, VMessageType.LOGOUT);
         this.syncProcessManager.processSyncPlayerLogout(player);
      }
   }
}
