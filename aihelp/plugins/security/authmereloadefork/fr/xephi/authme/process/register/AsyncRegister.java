package fr.xephi.authme.process.register;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.AuthMeAsyncPreRegisterEvent;
import fr.xephi.authme.libs.ch.jalu.injector.factory.SingletonStore;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.process.register.executors.RegistrationExecutor;
import fr.xephi.authme.process.register.executors.RegistrationMethod;
import fr.xephi.authme.process.register.executors.RegistrationParameters;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.InternetProtocolUtils;
import fr.xephi.authme.util.PlayerUtils;
import java.util.List;
import java.util.Locale;
import org.bukkit.entity.Player;

public class AsyncRegister implements AsynchronousProcess {
   @Inject
   private DataSource database;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private CommonService service;
   @Inject
   private SingletonStore<RegistrationExecutor> registrationExecutorFactory;

   AsyncRegister() {
   }

   public synchronized <P extends RegistrationParameters> void register(RegistrationMethod<P> variant, P parameters) {
      if (this.preRegisterCheck(variant, parameters.getPlayer())) {
         RegistrationExecutor<P> executor = (RegistrationExecutor)this.registrationExecutorFactory.getSingleton(variant.getExecutorClass());
         if (executor.isRegistrationAdmitted(parameters)) {
            this.executeRegistration(parameters, executor);
         }
      }

   }

   private boolean preRegisterCheck(RegistrationMethod<?> variant, Player player) {
      String name = player.getName().toLowerCase(Locale.ROOT);
      if (this.playerCache.isAuthenticated(name)) {
         this.service.send(player, MessageKey.ALREADY_LOGGED_IN_ERROR);
         return false;
      } else if (!(Boolean)this.service.getProperty(RegistrationSettings.IS_ENABLED)) {
         this.service.send(player, MessageKey.REGISTRATION_DISABLED);
         return false;
      } else if (this.database.isAuthAvailable(name)) {
         this.service.send(player, MessageKey.NAME_ALREADY_REGISTERED);
         return false;
      } else {
         AuthMeAsyncPreRegisterEvent event = (AuthMeAsyncPreRegisterEvent)this.bukkitService.createAndCallEvent((isAsync) -> {
            return new AuthMeAsyncPreRegisterEvent(player, isAsync);
         });
         if (!event.canRegister()) {
            return false;
         } else {
            return variant == RegistrationMethod.API_REGISTRATION || this.isPlayerIpAllowedToRegister(player);
         }
      }
   }

   private <P extends RegistrationParameters> void executeRegistration(P parameters, RegistrationExecutor<P> executor) {
      PlayerAuth auth = executor.buildPlayerAuth(parameters);
      if (this.database.saveAuth(auth)) {
         executor.executePostPersistAction(parameters);
      } else {
         this.service.send(parameters.getPlayer(), MessageKey.ERROR);
      }

   }

   private boolean isPlayerIpAllowedToRegister(Player player) {
      int maxRegPerIp = (Integer)this.service.getProperty(RestrictionSettings.MAX_REGISTRATION_PER_IP);
      String ip = PlayerUtils.getPlayerIp(player);
      if (maxRegPerIp > 0 && !InternetProtocolUtils.isLoopbackAddress(ip) && !this.service.hasPermission(player, PlayerStatePermission.ALLOW_MULTIPLE_ACCOUNTS)) {
         List<String> otherAccounts = this.database.getAllAuthsByIp(ip);
         if (otherAccounts.size() >= maxRegPerIp) {
            this.service.send(player, MessageKey.MAX_REGISTER_EXCEEDED, Integer.toString(maxRegPerIp), Integer.toString(otherAccounts.size()), String.join(", ", otherAccounts));
            return false;
         }
      }

      return true;
   }
}
