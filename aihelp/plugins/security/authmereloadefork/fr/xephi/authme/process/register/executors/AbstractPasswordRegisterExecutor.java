package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.process.SyncProcessManager;
import fr.xephi.authme.process.login.AsynchronousLogin;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import org.bukkit.entity.Player;

abstract class AbstractPasswordRegisterExecutor<P extends AbstractPasswordRegisterParams> implements RegistrationExecutor<P> {
   private static final int SYNC_LOGIN_DELAY = 5;
   @Inject
   private ValidationService validationService;
   @Inject
   private CommonService commonService;
   @Inject
   private PasswordSecurity passwordSecurity;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private SyncProcessManager syncProcessManager;
   @Inject
   private AsynchronousLogin asynchronousLogin;

   public boolean isRegistrationAdmitted(P params) {
      ValidationService.ValidationResult passwordValidation = this.validationService.validatePassword(params.getPassword(), params.getPlayer().getName());
      if (passwordValidation.hasError()) {
         this.commonService.send(params.getPlayer(), passwordValidation.getMessageKey(), passwordValidation.getArgs());
         return false;
      } else {
         return true;
      }
   }

   public PlayerAuth buildPlayerAuth(P params) {
      HashedPassword hashedPassword = this.passwordSecurity.computeHash(params.getPassword(), params.getPlayerName());
      params.setHashedPassword(hashedPassword);
      return this.createPlayerAuthObject(params);
   }

   protected abstract PlayerAuth createPlayerAuthObject(P var1);

   protected boolean performLoginAfterRegister(P params) {
      return !(Boolean)this.commonService.getProperty(RegistrationSettings.FORCE_LOGIN_AFTER_REGISTER);
   }

   public void executePostPersistAction(P params) {
      Player player = params.getPlayer();
      if (this.performLoginAfterRegister(params)) {
         if ((Boolean)this.commonService.getProperty(PluginSettings.USE_ASYNC_TASKS)) {
            this.bukkitService.runTaskAsynchronously(() -> {
               this.asynchronousLogin.forceLogin(player);
            });
         } else {
            this.bukkitService.scheduleSyncDelayedTask(() -> {
               this.asynchronousLogin.forceLogin(player);
            }, 5L);
         }
      }

      this.syncProcessManager.processSyncPasswordRegister(player);
   }
}
