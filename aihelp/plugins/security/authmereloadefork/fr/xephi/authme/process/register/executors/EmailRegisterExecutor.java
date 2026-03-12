package fr.xephi.authme.process.register.executors;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.process.SyncProcessManager;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.util.PlayerUtils;
import fr.xephi.authme.util.RandomStringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.entity.Player;

class EmailRegisterExecutor implements RegistrationExecutor<EmailRegisterParams> {
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService commonService;
   @Inject
   private EmailService emailService;
   @Inject
   private SyncProcessManager syncProcessManager;
   @Inject
   private PasswordSecurity passwordSecurity;

   public boolean isRegistrationAdmitted(EmailRegisterParams params) {
      int maxRegPerEmail = (Integer)this.commonService.getProperty(EmailSettings.MAX_REG_PER_EMAIL);
      if (maxRegPerEmail > 0 && !this.commonService.hasPermission(params.getPlayer(), PlayerStatePermission.ALLOW_MULTIPLE_ACCOUNTS)) {
         int otherAccounts = this.dataSource.countAuthsByEmail(params.getEmail());
         if (otherAccounts >= maxRegPerEmail) {
            this.commonService.send(params.getPlayer(), MessageKey.MAX_REGISTER_EXCEEDED, Integer.toString(maxRegPerEmail), Integer.toString(otherAccounts), "@");
            return false;
         }
      }

      return true;
   }

   public PlayerAuth buildPlayerAuth(EmailRegisterParams params) {
      String password = RandomStringUtils.generate((Integer)this.commonService.getProperty(EmailSettings.RECOVERY_PASSWORD_LENGTH));
      HashedPassword hashedPassword = this.passwordSecurity.computeHash(password, params.getPlayer().getName());
      params.setPassword(password);
      return PlayerAuthBuilderHelper.createPlayerAuth(params.getPlayer(), hashedPassword, params.getEmail());
   }

   public void executePostPersistAction(EmailRegisterParams params) {
      Player player = params.getPlayer();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'年'MM'月'dd'日' HH:mm:ss");
      Date date = new Date(System.currentTimeMillis());
      boolean couldSendMail = this.emailService.sendNewPasswordMail(player.getName(), params.getEmail(), params.getPassword(), PlayerUtils.getPlayerIp(player), dateFormat.format(date));
      if (couldSendMail) {
         this.syncProcessManager.processSyncEmailRegister(player);
      } else {
         this.commonService.send(player, MessageKey.EMAIL_SEND_FAILURE);
      }

   }
}
