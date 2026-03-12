package fr.xephi.authme.command.executable.captcha;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.captcha.LoginCaptchaManager;
import fr.xephi.authme.data.captcha.RegistrationCaptchaManager;
import fr.xephi.authme.data.limbo.LimboMessageType;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import org.bukkit.entity.Player;

public class CaptchaCommand extends PlayerCommand {
   @Inject
   private PlayerCache playerCache;
   @Inject
   private LoginCaptchaManager loginCaptchaManager;
   @Inject
   private RegistrationCaptchaManager registrationCaptchaManager;
   @Inject
   private CommonService commonService;
   @Inject
   private LimboService limboService;
   @Inject
   private DataSource dataSource;

   public void runCommand(Player player, List<String> arguments) {
      String name = player.getName();
      if (this.playerCache.isAuthenticated(name)) {
         this.commonService.send(player, MessageKey.ALREADY_LOGGED_IN_ERROR);
      } else {
         if (this.loginCaptchaManager.isCaptchaRequired(name)) {
            this.checkLoginCaptcha(player, (String)arguments.get(0));
         } else {
            boolean isPlayerRegistered = this.dataSource.isAuthAvailable(name);
            if (!isPlayerRegistered && this.registrationCaptchaManager.isCaptchaRequired(name)) {
               this.checkRegisterCaptcha(player, (String)arguments.get(0));
            } else {
               MessageKey errorMessage = isPlayerRegistered ? MessageKey.USAGE_LOGIN : MessageKey.USAGE_REGISTER;
               this.commonService.send(player, errorMessage);
            }
         }

      }
   }

   private void checkLoginCaptcha(Player player, String captchaCode) {
      boolean isCorrectCode = this.loginCaptchaManager.checkCode(player, captchaCode);
      if (isCorrectCode) {
         this.commonService.send(player, MessageKey.CAPTCHA_SUCCESS);
         this.commonService.send(player, MessageKey.LOGIN_MESSAGE);
         this.limboService.unmuteMessageTask(player);
      } else {
         String newCode = this.loginCaptchaManager.getCaptchaCodeOrGenerateNew(player.getName());
         this.commonService.send(player, MessageKey.CAPTCHA_WRONG_ERROR, newCode);
      }

   }

   private void checkRegisterCaptcha(Player player, String captchaCode) {
      boolean isCorrectCode = this.registrationCaptchaManager.checkCode(player, captchaCode);
      if (isCorrectCode) {
         this.commonService.send(player, MessageKey.REGISTER_CAPTCHA_SUCCESS);
         this.commonService.send(player, MessageKey.REGISTER_MESSAGE);
      } else {
         String newCode = this.registrationCaptchaManager.getCaptchaCodeOrGenerateNew(player.getName());
         this.commonService.send(player, MessageKey.CAPTCHA_WRONG_ERROR, newCode);
      }

      this.limboService.resetMessageTask(player, LimboMessageType.REGISTER);
   }
}
