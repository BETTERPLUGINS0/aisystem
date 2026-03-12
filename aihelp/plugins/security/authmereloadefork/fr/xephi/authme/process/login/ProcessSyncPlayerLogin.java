package fr.xephi.authme.process.login;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RestoreInventoryEvent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.process.SynchronousProcess;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.JoinMessageService;
import fr.xephi.authme.service.TeleportationService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.service.velocity.VMessageType;
import fr.xephi.authme.service.velocity.VelocitySender;
import fr.xephi.authme.settings.commandconfig.CommandManager;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import java.util.List;
import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ProcessSyncPlayerLogin implements SynchronousProcess {
   @Inject
   private BungeeSender bungeeSender;
   @Inject
   private VelocitySender velocitySender;
   @Inject
   private LimboService limboService;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private TeleportationService teleportationService;
   @Inject
   private PlayerCache playerCache;
   @Inject
   private CommandManager commandManager;
   @Inject
   private CommonService commonService;
   @Inject
   private JoinMessageService joinMessageService;
   @Inject
   private PermissionsManager permissionsManager;

   ProcessSyncPlayerLogin() {
   }

   private void restoreInventory(Player player) {
      RestoreInventoryEvent event = new RestoreInventoryEvent(player);
      this.bukkitService.callEvent(event);
      if (!event.isCancelled()) {
         player.updateInventory();
      }

   }

   public void processPlayerLogin(Player player, boolean isFirstLogin, List<String> authsWithSameIp) {
      String name = player.getName().toLowerCase(Locale.ROOT);
      LimboPlayer limbo = this.limboService.getLimboPlayer(name);
      if (limbo != null) {
         this.limboService.restoreData(player);
      }

      if ((Boolean)this.commonService.getProperty(RestrictionSettings.PROTECT_INVENTORY_BEFORE_LOGIN)) {
         this.restoreInventory(player);
      }

      PlayerAuth auth = this.playerCache.getAuth(name);
      if (isFirstLogin) {
         auth.setQuitLocation(player.getLocation());
      }

      this.teleportationService.teleportOnLogin(player, auth, limbo);
      this.joinMessageService.sendMessage(name);
      if ((Boolean)this.commonService.getProperty(RegistrationSettings.APPLY_BLIND_EFFECT)) {
         player.removePotionEffect(PotionEffectType.BLINDNESS);
      }

      if (this.velocitySender.isEnabled()) {
         this.bukkitService.scheduleSyncDelayedTask(() -> {
            this.velocitySender.sendAuthMeVelocityMessage(player, VMessageType.LOGIN);
         }, (Long)this.commonService.getProperty(HooksSettings.PROXY_SEND_DELAY));
      }

      this.bukkitService.callEvent(new LoginEvent(player));
      if (isFirstLogin) {
         this.commandManager.runCommandsOnFirstLogin(player, authsWithSameIp);
      }

      this.commandManager.runCommandsOnLogin(player, authsWithSameIp);
      if (!this.permissionsManager.hasPermission(player, PlayerStatePermission.BYPASS_BUNGEE_SEND)) {
         this.bungeeSender.connectPlayerOnLogin(player);
      }

   }
}
