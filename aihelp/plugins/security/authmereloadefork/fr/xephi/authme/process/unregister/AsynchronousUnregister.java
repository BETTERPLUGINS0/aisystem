package fr.xephi.authme.process.unregister;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.UnregisterByAdminEvent;
import fr.xephi.authme.events.UnregisterByPlayerEvent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.TeleportationService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.service.bungeecord.MessageType;
import fr.xephi.authme.service.velocity.VMessageType;
import fr.xephi.authme.service.velocity.VelocitySender;
import fr.xephi.authme.settings.commandconfig.CommandManager;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AsynchronousUnregister implements AsynchronousProcess {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(AsynchronousUnregister.class);
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService service;
   @Inject
   private PasswordSecurity passwordSecurity;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private LimboService limboService;
   @Inject
   private TeleportationService teleportationService;
   @Inject
   private CommandManager commandManager;
   @Inject
   private VelocitySender velocitySender;
   @Inject
   private BungeeSender bungeeSender;

   AsynchronousUnregister() {
   }

   public void unregister(Player player, String password) {
      String name = player.getName();
      PlayerAuth cachedAuth = this.playerCache.getAuth(name);
      if (this.passwordSecurity.comparePassword(password, cachedAuth.getPassword(), name)) {
         if (this.dataSource.removeAuth(name)) {
            this.performPostUnregisterActions(name, player);
            this.logger.info(name + " unregistered himself");
            this.velocitySender.sendAuthMeVelocityMessage(player, VMessageType.UNREGISTER);
            this.bukkitService.createAndCallEvent((isAsync) -> {
               return new UnregisterByPlayerEvent(player, isAsync);
            });
         } else {
            this.service.send(player, MessageKey.ERROR);
         }
      } else {
         this.service.send(player, MessageKey.WRONG_PASSWORD);
      }

   }

   public void adminUnregister(CommandSender initiator, String name, Player player) {
      if (this.dataSource.removeAuth(name)) {
         this.performPostUnregisterActions(name, player);
         if (player != null) {
            this.velocitySender.sendAuthMeVelocityMessage(player, VMessageType.FORCE_UNREGISTER);
         }

         this.bukkitService.createAndCallEvent((isAsync) -> {
            return new UnregisterByAdminEvent(player, name, isAsync, initiator);
         });
         if (initiator == null) {
            this.logger.info(name + " was unregistered");
         } else {
            this.logger.info(name + " was unregistered by " + initiator.getName());
            this.service.send(initiator, MessageKey.UNREGISTERED_SUCCESS);
         }
      } else if (initiator != null) {
         this.service.send(initiator, MessageKey.ERROR);
      }

   }

   private void performPostUnregisterActions(String name, Player player) {
      if (player != null && this.playerCache.isAuthenticated(name)) {
         this.bungeeSender.sendAuthMeBungeecordMessage(player, MessageType.LOGOUT);
      }

      this.playerCache.removePlayer(name);
      if (player != null && player.isOnline()) {
         this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
            this.commandManager.runCommandsOnUnregister(player);
         });
         if ((Boolean)this.service.getProperty(RegistrationSettings.FORCE)) {
            this.teleportationService.teleportOnJoin(player);
            this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
               this.limboService.createLimboPlayer(player, false);
               this.applyBlindEffect(player);
            });
         }

         this.service.send(player, MessageKey.UNREGISTERED_SUCCESS);
      }
   }

   private void applyBlindEffect(Player player) {
      if ((Boolean)this.service.getProperty(RegistrationSettings.APPLY_BLIND_EFFECT)) {
         int timeout = (Integer)this.service.getProperty(RestrictionSettings.TIMEOUT) * 20;
         this.bukkitService.runTaskIfFolia((Entity)player, () -> {
            player.addPotionEffect(this.bukkitService.createBlindnessEffect(timeout));
         });
      }

   }
}
