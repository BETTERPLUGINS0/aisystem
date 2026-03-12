package fr.xephi.authme.process.quit;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.data.VerificationCodeManager;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.process.SyncProcessManager;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.SessionService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.util.PlayerUtils;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AsynchronousQuit implements AsynchronousProcess {
   @Inject
   private AuthMe plugin;
   @Inject
   private DataSource database;
   @Inject
   private CommonService service;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private SyncProcessManager syncProcessManager;
   @Inject
   private SpawnLoader spawnLoader;
   @Inject
   private ValidationService validationService;
   @Inject
   private VerificationCodeManager codeManager;
   @Inject
   private SessionService sessionService;

   AsynchronousQuit() {
   }

   public void processQuit(Player player) {
      if (player != null && !this.validationService.isUnrestricted(player.getName())) {
         String name = player.getName().toLowerCase(Locale.ROOT);
         boolean wasLoggedIn = this.playerCache.isAuthenticated(name);
         if (wasLoggedIn) {
            Location loc = this.spawnLoader.getPlayerLocationOrSpawn(player);
            PlayerAuth authLoc = PlayerAuth.builder().name(name).location(loc).realName(player.getName()).build();
            this.database.updateQuitLoc(authLoc);
            String ip = PlayerUtils.getPlayerIp(player);
            PlayerAuth auth = PlayerAuth.builder().name(name).realName(player.getName()).lastIp(ip).lastLogin(System.currentTimeMillis()).build();
            this.database.updateSession(auth);
         }

         this.playerCache.removePlayer(name);
         this.codeManager.unverify(name);
         if (wasLoggedIn) {
            this.database.setUnlogged(name);
            if (!(Boolean)this.service.getProperty(PluginSettings.SESSIONS_ENABLED)) {
               this.sessionService.revokeSession(name);
            }
         }

         if (this.plugin.isEnabled()) {
            this.syncProcessManager.processSyncPlayerQuit(player, wasLoggedIn);
         }

         this.database.invalidateCache(name);
      }
   }
}
