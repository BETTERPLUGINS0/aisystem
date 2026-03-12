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
import fr.xephi.authme.service.velocity.VMessageType;
import fr.xephi.authme.service.velocity.VelocitySender;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.entity.Player;

public class ProcessSyncEmailRegister implements SynchronousProcess {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(ProcessSyncEmailRegister.class);
   @Inject
   private BukkitService bukkitService;
   @Inject
   private CommonService service;
   @Inject
   private LimboService limboService;
   @Inject
   private VelocitySender velocitySender;

   ProcessSyncEmailRegister() {
   }

   public void processEmailRegister(Player player) {
      this.service.send(player, MessageKey.ACCOUNT_NOT_ACTIVATED);
      this.limboService.replaceTasksAfterRegistration(player);
      this.velocitySender.sendAuthMeVelocityMessage(player, VMessageType.REGISTER);
      this.bukkitService.callEvent(new RegisterEvent(player));
      this.logger.fine(player.getName() + " registered " + PlayerUtils.getPlayerIp(player));
   }
}
