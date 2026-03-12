package fr.xephi.authme.process.register;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.events.RegisterEvent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.process.SynchronousProcess;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.service.velocity.VMessageType;
import fr.xephi.authme.service.velocity.VelocitySender;
import fr.xephi.authme.settings.commandconfig.CommandManager;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.entity.Player;

public class ProcessSyncPasswordRegister implements SynchronousProcess {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(ProcessSyncPasswordRegister.class);
   @Inject
   private BungeeSender bungeeSender;
   @Inject
   private VelocitySender velocitySender;
   @Inject
   private CommonService service;
   @Inject
   private LimboService limboService;
   @Inject
   private CommandManager commandManager;
   @Inject
   private BukkitService bukkitService;

   ProcessSyncPasswordRegister() {
   }

   private void requestLogin(Player player) {
      this.limboService.replaceTasksAfterRegistration(player);
      if (player.isInsideVehicle() && player.getVehicle() != null) {
         player.getVehicle().eject();
      }

   }

   public void processPasswordRegister(Player player) {
      this.service.send(player, MessageKey.REGISTER_SUCCESS);
      if (!((String)this.service.getProperty(EmailSettings.MAIL_ACCOUNT)).isEmpty()) {
         this.service.send(player, MessageKey.ADD_EMAIL_MESSAGE);
      }

      this.velocitySender.sendAuthMeVelocityMessage(player, VMessageType.REGISTER);
      this.bukkitService.callEvent(new RegisterEvent(player));
      this.logger.fine(player.getName() + " registered " + PlayerUtils.getPlayerIp(player));
      if ((Boolean)this.service.getProperty(RegistrationSettings.FORCE_KICK_AFTER_REGISTER)) {
         player.kickPlayer(this.service.retrieveSingleMessage(player, MessageKey.REGISTER_SUCCESS));
      } else {
         this.commandManager.runCommandsOnRegister(player);
         if ((Boolean)this.service.getProperty(RegistrationSettings.FORCE_LOGIN_AFTER_REGISTER)) {
            this.requestLogin(player);
         } else {
            this.bungeeSender.connectPlayerOnLogin(player);
         }
      }
   }
}
