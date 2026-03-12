package fr.xephi.authme.process.logout;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.events.LogoutEvent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.listener.protocollib.ProtocolLibService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.process.SynchronousProcess;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.TeleportationService;
import fr.xephi.authme.settings.commandconfig.CommandManager;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import org.bukkit.entity.Player;

public class ProcessSyncPlayerLogout implements SynchronousProcess {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(ProcessSyncPlayerLogout.class);
   @Inject
   private CommonService service;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private ProtocolLibService protocolLibService;
   @Inject
   private LimboService limboService;
   @Inject
   private TeleportationService teleportationService;
   @Inject
   private CommandManager commandManager;

   ProcessSyncPlayerLogout() {
   }

   public void processSyncLogout(Player player) {
      if ((Boolean)this.service.getProperty(RestrictionSettings.PROTECT_INVENTORY_BEFORE_LOGIN)) {
         this.protocolLibService.sendBlankInventoryPacket(player);
      }

      this.applyLogoutEffect(player);
      this.commandManager.runCommandsOnLogout(player);
      this.bukkitService.callEvent(new LogoutEvent(player));
      this.service.send(player, MessageKey.LOGOUT_SUCCESS);
      this.logger.info(player.getName() + " logged out");
   }

   private void applyLogoutEffect(Player player) {
      player.leaveVehicle();
      this.teleportationService.teleportOnJoin(player);
      if ((Boolean)this.service.getProperty(RegistrationSettings.APPLY_BLIND_EFFECT)) {
         int timeout = (Integer)this.service.getProperty(RestrictionSettings.TIMEOUT) * 20;
         player.addPotionEffect(this.bukkitService.createBlindnessEffect(timeout));
      }

      this.limboService.createLimboPlayer(player, true);
   }
}
