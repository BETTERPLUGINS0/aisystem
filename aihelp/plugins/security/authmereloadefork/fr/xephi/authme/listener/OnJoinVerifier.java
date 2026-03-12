package fr.xephi.authme.listener;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.service.AntiBotService;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.ProtectionSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.StringUtils;
import fr.xephi.authme.util.Utils;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class OnJoinVerifier implements Reloadable {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(OnJoinVerifier.class);
   @Inject
   private Settings settings;
   @Inject
   private DataSource dataSource;
   @Inject
   private Messages messages;
   @Inject
   private PermissionsManager permissionsManager;
   @Inject
   private AntiBotService antiBotService;
   @Inject
   private ValidationService validationService;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private Server server;
   private Pattern nicknamePattern;

   OnJoinVerifier() {
   }

   @PostConstruct
   public void reload() {
      String nickRegEx = (String)this.settings.getProperty(RestrictionSettings.ALLOWED_NICKNAME_CHARACTERS);
      this.nicknamePattern = Utils.safePatternCompile(nickRegEx);
   }

   public void checkAntibot(String name, boolean isAuthAvailable) throws FailedVerificationException {
      if (!isAuthAvailable && !this.permissionsManager.hasPermissionOffline((String)name, PlayerStatePermission.BYPASS_ANTIBOT)) {
         if (this.antiBotService.shouldKick()) {
            this.antiBotService.addPlayerKick(name);
            throw new FailedVerificationException(MessageKey.KICK_ANTIBOT, new String[0]);
         }
      }
   }

   public void checkKickNonRegistered(boolean isAuthAvailable) throws FailedVerificationException {
      if (!isAuthAvailable && (Boolean)this.settings.getProperty(RestrictionSettings.KICK_NON_REGISTERED)) {
         throw new FailedVerificationException(MessageKey.MUST_REGISTER_MESSAGE, new String[0]);
      }
   }

   public void checkIsValidName(String name) throws FailedVerificationException {
      if (name.length() <= (Integer)this.settings.getProperty(RestrictionSettings.MAX_NICKNAME_LENGTH) && name.length() >= (Integer)this.settings.getProperty(RestrictionSettings.MIN_NICKNAME_LENGTH)) {
         if (!this.nicknamePattern.matcher(name).matches()) {
            throw new FailedVerificationException(MessageKey.INVALID_NAME_CHARACTERS, new String[]{this.nicknamePattern.pattern()});
         }
      } else {
         throw new FailedVerificationException(MessageKey.INVALID_NAME_LENGTH, new String[0]);
      }
   }

   public boolean refusePlayerForFullServer(PlayerLoginEvent event) {
      Player player = event.getPlayer();
      if (event.getResult() != Result.KICK_FULL) {
         return false;
      } else if (!this.permissionsManager.hasPermission(player, PlayerStatePermission.IS_VIP)) {
         event.setKickMessage(this.messages.retrieveSingle((CommandSender)player, MessageKey.KICK_FULL_SERVER));
         return true;
      } else {
         Collection<Player> onlinePlayers = this.bukkitService.getOnlinePlayers();
         if (onlinePlayers.size() < this.server.getMaxPlayers()) {
            event.allow();
            return false;
         } else {
            Player nonVipPlayer = this.generateKickPlayer(onlinePlayers);
            if (nonVipPlayer != null) {
               this.bukkitService.runTaskIfFolia((Entity)nonVipPlayer, () -> {
                  nonVipPlayer.kickPlayer(this.messages.retrieveSingle((CommandSender)player, MessageKey.KICK_FOR_VIP));
               });
               event.allow();
               return false;
            } else {
               this.logger.info("VIP player " + player.getName() + " tried to join, but the server was full");
               event.setKickMessage(this.messages.retrieveSingle((CommandSender)player, MessageKey.KICK_FULL_SERVER));
               return true;
            }
         }
      }
   }

   public void checkNameCasing(String connectingName, PlayerAuth auth) throws FailedVerificationException {
      if (auth != null && (Boolean)this.settings.getProperty(RegistrationSettings.PREVENT_OTHER_CASE)) {
         String realName = auth.getRealName();
         if (!StringUtils.isBlank(realName) && !"Player".equals(realName)) {
            if (!realName.equals(connectingName)) {
               throw new FailedVerificationException(MessageKey.INVALID_NAME_CASE, new String[]{realName, connectingName});
            }
         } else {
            this.dataSource.updateRealName(connectingName.toLowerCase(Locale.ROOT), connectingName);
         }
      }

   }

   public void checkPlayerCountry(String name, String address, boolean isAuthAvailable) throws FailedVerificationException {
      if ((!isAuthAvailable || (Boolean)this.settings.getProperty(ProtectionSettings.ENABLE_PROTECTION_REGISTERED)) && (Boolean)this.settings.getProperty(ProtectionSettings.ENABLE_PROTECTION) && !this.permissionsManager.hasPermissionOffline((String)name, PlayerStatePermission.BYPASS_COUNTRY_CHECK) && !this.validationService.isCountryAdmitted(address)) {
         throw new FailedVerificationException(MessageKey.COUNTRY_BANNED_ERROR, new String[0]);
      }
   }

   public void checkSingleSession(String name) throws FailedVerificationException {
      if ((Boolean)this.settings.getProperty(RestrictionSettings.FORCE_SINGLE_SESSION)) {
         Player onlinePlayer = this.bukkitService.getPlayerExact(name);
         if (onlinePlayer != null) {
            throw new FailedVerificationException(MessageKey.USERNAME_ALREADY_ONLINE_ERROR, new String[0]);
         }
      }
   }

   private Player generateKickPlayer(Collection<Player> onlinePlayers) {
      Iterator var2 = onlinePlayers.iterator();

      Player player;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         player = (Player)var2.next();
      } while(this.permissionsManager.hasPermission(player, PlayerStatePermission.IS_VIP));

      return player;
   }
}
