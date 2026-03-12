package fr.xephi.authme.process.join;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.ProxySessionManager;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.ProtectInventoryEvent;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.process.login.AsynchronousLogin;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.PluginHookService;
import fr.xephi.authme.service.SessionService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.service.bungeecord.MessageType;
import fr.xephi.authme.service.velocity.VMessageType;
import fr.xephi.authme.service.velocity.VelocitySender;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.commandconfig.CommandManager;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.InternetProtocolUtils;
import fr.xephi.authme.util.PlayerUtils;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AsynchronousJoin implements AsynchronousProcess {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(AsynchronousJoin.class);
   @Inject
   private Server server;
   @Inject
   private Settings settings;
   @Inject
   private DataSource database;
   @Inject
   private CommonService service;
   @Inject
   private LimboService limboService;
   @Inject
   private PluginHookService pluginHookService;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private AsynchronousLogin asynchronousLogin;
   @Inject
   private CommandManager commandManager;
   @Inject
   private ValidationService validationService;
   @Inject
   private SessionService sessionService;
   @Inject
   private BungeeSender bungeeSender;
   @Inject
   private VelocitySender velocitySender;
   @Inject
   private ProxySessionManager proxySessionManager;

   AsynchronousJoin() {
   }

   public void processJoin(Player player) {
      String name = player.getName().toLowerCase(Locale.ROOT);
      String ip = PlayerUtils.getPlayerIp(player);
      if (!this.validationService.fulfillsNameRestrictions(player)) {
         this.handlePlayerWithUnmetNameRestriction(player, ip);
      } else if (!((Set)this.service.getProperty(RestrictionSettings.UNRESTRICTED_NAMES)).contains(name)) {
         if ((Boolean)this.service.getProperty(RestrictionSettings.FORCE_SURVIVAL_MODE) && player.getGameMode() != GameMode.SURVIVAL && !this.service.hasPermission(player, PlayerStatePermission.BYPASS_FORCE_SURVIVAL)) {
            this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
               player.setGameMode(GameMode.SURVIVAL);
            });
         }

         if ((Boolean)this.service.getProperty(HooksSettings.DISABLE_SOCIAL_SPY)) {
            this.pluginHookService.setEssentialsSocialSpyStatus(player, false);
         }

         if (this.validatePlayerCountForIp(player, ip)) {
            boolean isAuthAvailable = this.database.isAuthAvailable(name);
            if (isAuthAvailable) {
               if ((Boolean)this.service.getProperty(RestrictionSettings.PROTECT_INVENTORY_BEFORE_LOGIN)) {
                  ProtectInventoryEvent ev = (ProtectInventoryEvent)this.bukkitService.createAndCallEvent((isAsync) -> {
                     return new ProtectInventoryEvent(player, isAsync);
                  });
                  if (ev.isCancelled()) {
                     player.updateInventory();
                     this.logger.fine("ProtectInventoryEvent has been cancelled for " + player.getName() + "...");
                  }
               }

               if (this.sessionService.canResumeSession(player)) {
                  if (this.velocitySender.isEnabled()) {
                     this.bukkitService.scheduleSyncDelayedTask(() -> {
                        this.velocitySender.sendAuthMeVelocityMessage(player, VMessageType.LOGIN);
                     }, (Long)this.service.getProperty(HooksSettings.PROXY_SEND_DELAY));
                  }

                  this.service.send(player, MessageKey.SESSION_RECONNECTION);
                  this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
                     this.commandManager.runCommandsOnSessionLogin(player);
                  });
                  this.bukkitService.runTaskOptionallyAsync(() -> {
                     this.asynchronousLogin.forceLogin(player);
                  });
                  return;
               }

               if (this.proxySessionManager.shouldResumeSession(name)) {
                  this.service.send(player, MessageKey.SESSION_RECONNECTION);
                  this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
                     this.commandManager.runCommandsOnSessionLogin(player);
                  });
                  this.bukkitService.runTaskOptionallyAsync(() -> {
                     this.asynchronousLogin.forceLogin(player);
                  });
                  this.logger.info("The user " + player.getName() + " has been automatically logged in, as present in autologin queue.");
                  return;
               }
            } else if (!(Boolean)this.service.getProperty(RegistrationSettings.FORCE)) {
               if (this.bungeeSender.isEnabled()) {
                  this.bukkitService.scheduleSyncDelayedTask(() -> {
                     this.bungeeSender.sendAuthMeBungeecordMessage(player, MessageType.LOGIN);
                  }, (Long)this.settings.getProperty(HooksSettings.PROXY_SEND_DELAY));
               }

               if (this.velocitySender.isEnabled()) {
                  this.bukkitService.scheduleSyncDelayedTask(() -> {
                     this.velocitySender.sendAuthMeVelocityMessage(player, VMessageType.LOGIN);
                  }, (Long)this.settings.getProperty(HooksSettings.PROXY_SEND_DELAY));
               }

               return;
            }

            this.processJoinSync(player, isAuthAvailable);
         }
      }
   }

   private void handlePlayerWithUnmetNameRestriction(Player player, String ip) {
      this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
         player.kickPlayer(this.service.retrieveSingleMessage(player, MessageKey.NOT_OWNER_ERROR));
         if ((Boolean)this.service.getProperty(RestrictionSettings.BAN_UNKNOWN_IP)) {
            this.server.banIP(ip);
         }

      });
   }

   private void processJoinSync(Player player, boolean isAuthAvailable) {
      int registrationTimeout = (Integer)this.service.getProperty(RestrictionSettings.TIMEOUT) * 20;
      this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
         this.limboService.createLimboPlayer(player, isAuthAvailable);
         player.setNoDamageTicks(registrationTimeout);
         if (this.pluginHookService.isEssentialsAvailable() && (Boolean)this.service.getProperty(HooksSettings.USE_ESSENTIALS_MOTD)) {
            player.performCommand("motd");
         }

         if ((Boolean)this.service.getProperty(RegistrationSettings.APPLY_BLIND_EFFECT)) {
            int blindTimeOut = registrationTimeout <= 0 ? 99999 : registrationTimeout;
            this.bukkitService.runTaskIfFolia((Entity)player, () -> {
               player.addPotionEffect(this.bukkitService.createBlindnessEffect(blindTimeOut));
            });
         }

         this.commandManager.runCommandsOnJoin(player);
      });
   }

   private boolean validatePlayerCountForIp(Player player, String ip) {
      if ((Integer)this.service.getProperty(RestrictionSettings.MAX_JOIN_PER_IP) > 0 && !this.service.hasPermission(player, PlayerStatePermission.ALLOW_MULTIPLE_ACCOUNTS) && !InternetProtocolUtils.isLoopbackAddress(ip) && this.countOnlinePlayersByIp(ip) > (Integer)this.service.getProperty(RestrictionSettings.MAX_JOIN_PER_IP)) {
         this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
            player.kickPlayer(this.service.retrieveSingleMessage(player, MessageKey.SAME_IP_ONLINE));
         });
         return false;
      } else {
         return true;
      }
   }

   private int countOnlinePlayersByIp(String ip) {
      int count = 0;
      Iterator var3 = this.bukkitService.getOnlinePlayers().iterator();

      while(var3.hasNext()) {
         Player player = (Player)var3.next();
         if (ip.equalsIgnoreCase(PlayerUtils.getPlayerIp(player))) {
            ++count;
         }
      }

      return count;
   }
}
