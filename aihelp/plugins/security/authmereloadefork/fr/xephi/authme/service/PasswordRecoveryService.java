package fr.xephi.authme.service;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.HasCleanup;
import fr.xephi.authme.initialization.Reloadable;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.PlayerUtils;
import fr.xephi.authme.util.RandomStringUtils;
import fr.xephi.authme.util.expiring.Duration;
import fr.xephi.authme.util.expiring.ExpiringMap;
import fr.xephi.authme.util.expiring.ExpiringSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.bukkit.entity.Player;

public class PasswordRecoveryService implements Reloadable, HasCleanup {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(PasswordRecoveryService.class);
   @Inject
   private CommonService commonService;
   @Inject
   private DataSource dataSource;
   @Inject
   private EmailService emailService;
   @Inject
   private PasswordSecurity passwordSecurity;
   @Inject
   private RecoveryCodeService recoveryCodeService;
   @Inject
   private Messages messages;
   private ExpiringSet<String> emailCooldown;
   private ExpiringMap<String, String> successfulRecovers;

   @PostConstruct
   private void initEmailCooldownSet() {
      this.emailCooldown = new ExpiringSet((long)(Integer)this.commonService.getProperty(SecuritySettings.EMAIL_RECOVERY_COOLDOWN_SECONDS), TimeUnit.SECONDS);
      this.successfulRecovers = new ExpiringMap((long)(Integer)this.commonService.getProperty(SecuritySettings.PASSWORD_CHANGE_TIMEOUT), TimeUnit.MINUTES);
   }

   public void createAndSendRecoveryCode(Player player, String email) {
      if (this.checkEmailCooldown(player)) {
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'年'MM'月'dd'日' HH:mm:ss");
         Date date = new Date(System.currentTimeMillis());
         String recoveryCode = this.recoveryCodeService.generateCode(player.getName());
         boolean couldSendMail = this.emailService.sendRecoveryCode(player.getName(), email, recoveryCode, dateFormat.format(date));
         if (couldSendMail) {
            this.commonService.send(player, MessageKey.RECOVERY_CODE_SENT);
            this.emailCooldown.add(player.getName().toLowerCase(Locale.ROOT));
         } else {
            this.commonService.send(player, MessageKey.EMAIL_SEND_FAILURE);
         }

      }
   }

   public void generateAndSendNewPassword(Player player, String email) {
      if (this.checkEmailCooldown(player)) {
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'年'MM'月'dd'日' HH:mm:ss");
         Date date = new Date(System.currentTimeMillis());
         String name = player.getName();
         String thePass = RandomStringUtils.generate((Integer)this.commonService.getProperty(EmailSettings.RECOVERY_PASSWORD_LENGTH));
         HashedPassword hashNew = this.passwordSecurity.computeHash(thePass, name);
         this.logger.info("Generating new password for '" + name + "'");
         this.dataSource.updatePassword(name, hashNew);
         boolean couldSendMail = this.emailService.sendPasswordMail(name, email, thePass, dateFormat.format(date));
         if (couldSendMail) {
            this.commonService.send(player, MessageKey.RECOVERY_EMAIL_SENT_MESSAGE);
            this.emailCooldown.add(player.getName().toLowerCase(Locale.ROOT));
         } else {
            this.commonService.send(player, MessageKey.EMAIL_SEND_FAILURE);
         }

      }
   }

   public void addSuccessfulRecovery(Player player) {
      String name = player.getName();
      String address = PlayerUtils.getPlayerIp(player);
      this.successfulRecovers.put(name, address);
      this.commonService.send(player, MessageKey.RECOVERY_CHANGE_PASSWORD);
   }

   public void removeFromSuccessfulRecovery(Player player) {
      this.successfulRecovers.remove(player.getName());
   }

   private boolean checkEmailCooldown(Player player) {
      Duration waitDuration = this.emailCooldown.getExpiration(player.getName().toLowerCase(Locale.ROOT));
      if (waitDuration.getDuration() > 0L) {
         String durationText = this.messages.formatDuration(waitDuration);
         this.messages.send(player, MessageKey.EMAIL_COOLDOWN_ERROR, durationText);
         return false;
      } else {
         return true;
      }
   }

   public boolean canChangePassword(Player player) {
      String name = player.getName();
      String playerAddress = PlayerUtils.getPlayerIp(player);
      String storedAddress = (String)this.successfulRecovers.get(name);
      return storedAddress != null && playerAddress.equals(storedAddress);
   }

   public void reload() {
      this.emailCooldown.setExpiration((long)(Integer)this.commonService.getProperty(SecuritySettings.EMAIL_RECOVERY_COOLDOWN_SECONDS), TimeUnit.SECONDS);
      this.successfulRecovers.setExpiration((long)(Integer)this.commonService.getProperty(SecuritySettings.PASSWORD_CHANGE_TIMEOUT), TimeUnit.MINUTES);
   }

   public void performCleanup() {
      this.emailCooldown.removeExpiredEntries();
      this.successfulRecovers.removeExpiredEntries();
   }
}
