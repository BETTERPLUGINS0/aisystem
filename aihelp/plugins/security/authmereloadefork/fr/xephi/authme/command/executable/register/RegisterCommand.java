package fr.xephi.authme.command.executable.register;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.captcha.RegistrationCaptchaManager;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.process.register.RegisterSecondaryArgument;
import fr.xephi.authme.process.register.RegistrationType;
import fr.xephi.authme.process.register.executors.EmailRegisterParams;
import fr.xephi.authme.process.register.executors.PasswordRegisterParams;
import fr.xephi.authme.process.register.executors.RegistrationMethod;
import fr.xephi.authme.process.register.executors.TwoFactorRegisterParams;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand extends PlayerCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(RegisterCommand.class);
   @Inject
   private Management management;
   @Inject
   private CommonService commonService;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private DataSource dataSource;
   @Inject
   private EmailService emailService;
   @Inject
   private ValidationService validationService;
   @Inject
   private RegistrationCaptchaManager registrationCaptchaManager;

   public void runCommand(Player player, List<String> arguments) {
      if (this.isCaptchaFulfilled(player)) {
         if (this.commonService.getProperty(SecuritySettings.PASSWORD_HASH) == HashAlgorithm.TWO_FACTOR) {
            this.management.performRegister(RegistrationMethod.TWO_FACTOR_REGISTRATION, TwoFactorRegisterParams.of(player));
         } else if (arguments.size() < 1) {
            this.commonService.send(player, MessageKey.USAGE_REGISTER);
         } else {
            RegistrationType registrationType = (RegistrationType)this.commonService.getProperty(RegistrationSettings.REGISTRATION_TYPE);
            if (registrationType == RegistrationType.PASSWORD) {
               this.handlePasswordRegistration(player, arguments);
            } else {
               if (registrationType != RegistrationType.EMAIL) {
                  throw new IllegalStateException("Unknown registration type '" + registrationType + "'");
               }

               this.handleEmailRegistration(player, arguments);
            }

         }
      }
   }

   protected String getAlternativeCommand() {
      return "/authme register <playername> <password>";
   }

   public MessageKey getArgumentsMismatchMessage() {
      return MessageKey.USAGE_REGISTER;
   }

   private boolean isCaptchaFulfilled(Player player) {
      if (this.registrationCaptchaManager.isCaptchaRequired(player.getName())) {
         String code = this.registrationCaptchaManager.getCaptchaCodeOrGenerateNew(player.getName());
         this.commonService.send(player, MessageKey.CAPTCHA_FOR_REGISTRATION_REQUIRED, code);
         return false;
      } else {
         return true;
      }
   }

   private void handlePasswordRegistration(Player player, List<String> arguments) {
      if (this.isSecondArgValidForPasswordRegistration(player, arguments)) {
         String password = (String)arguments.get(0);
         String email = this.getEmailIfAvailable(arguments);
         this.management.performRegister(RegistrationMethod.PASSWORD_REGISTRATION, PasswordRegisterParams.of(player, password, email));
      }

   }

   private String getEmailIfAvailable(List<String> arguments) {
      if (arguments.size() >= 2) {
         RegisterSecondaryArgument secondArgType = (RegisterSecondaryArgument)this.commonService.getProperty(RegistrationSettings.REGISTER_SECOND_ARGUMENT);
         if (secondArgType == RegisterSecondaryArgument.EMAIL_MANDATORY || secondArgType == RegisterSecondaryArgument.EMAIL_OPTIONAL) {
            return (String)arguments.get(1);
         }
      }

      return null;
   }

   private boolean isSecondArgValidForPasswordRegistration(Player player, List<String> arguments) {
      RegisterSecondaryArgument secondArgType = (RegisterSecondaryArgument)this.commonService.getProperty(RegistrationSettings.REGISTER_SECOND_ARGUMENT);
      if (secondArgType != RegisterSecondaryArgument.NONE && (secondArgType != RegisterSecondaryArgument.EMAIL_OPTIONAL || arguments.size() >= 2)) {
         if (arguments.size() < 2) {
            this.commonService.send(player, MessageKey.USAGE_REGISTER);
            return false;
         } else if (secondArgType == RegisterSecondaryArgument.CONFIRMATION) {
            if (((String)arguments.get(0)).equals(arguments.get(1))) {
               return true;
            } else {
               this.commonService.send(player, MessageKey.PASSWORD_MATCH_ERROR);
               return false;
            }
         } else if (secondArgType != RegisterSecondaryArgument.EMAIL_MANDATORY && secondArgType != RegisterSecondaryArgument.EMAIL_OPTIONAL) {
            throw new IllegalStateException("Unknown secondary argument type '" + secondArgType + "'");
         } else if (this.validationService.validateEmail((String)arguments.get(1))) {
            return true;
         } else {
            this.commonService.send(player, MessageKey.INVALID_EMAIL);
            return false;
         }
      } else {
         return true;
      }
   }

   private void handleEmailRegistration(Player player, List<String> arguments) {
      if (!this.emailService.hasAllInformation()) {
         this.commonService.send(player, MessageKey.INCOMPLETE_EMAIL_SETTINGS);
         this.logger.warning("Cannot register player '" + player.getName() + "': no email or password is set to send emails from. Please adjust your config at " + EmailSettings.MAIL_ACCOUNT.getPath());
      } else {
         String email = (String)arguments.get(0);
         if (!this.validationService.validateEmail(email)) {
            this.commonService.send(player, MessageKey.INVALID_EMAIL);
         } else if (this.isSecondArgValidForEmailRegistration(player, arguments)) {
            this.management.performRegister(RegistrationMethod.EMAIL_REGISTRATION, EmailRegisterParams.of(player, email));
            if ((Boolean)this.commonService.getProperty(RegistrationSettings.UNREGISTER_ON_EMAIL_VERIFICATION_FAILURE) && (Long)this.commonService.getProperty(RegistrationSettings.UNREGISTER_AFTER_MINUTES) > 0L) {
               this.bukkitService.runTaskLater(player, () -> {
                  if (this.dataSource.getAuth(player.getName()) != null && this.dataSource.getAuth(player.getName()).getLastLogin() == null) {
                     this.management.performUnregisterByAdmin((CommandSender)null, player.getName(), player);
                  }

               }, 1200L * (Long)this.commonService.getProperty(RegistrationSettings.UNREGISTER_AFTER_MINUTES));
            }
         }

      }
   }

   private boolean isSecondArgValidForEmailRegistration(Player player, List<String> arguments) {
      RegisterSecondaryArgument secondArgType = (RegisterSecondaryArgument)this.commonService.getProperty(RegistrationSettings.REGISTER_SECOND_ARGUMENT);
      if (secondArgType != RegisterSecondaryArgument.NONE && (secondArgType != RegisterSecondaryArgument.EMAIL_OPTIONAL || arguments.size() >= 2)) {
         if (arguments.size() < 2) {
            this.commonService.send(player, MessageKey.USAGE_REGISTER);
            return false;
         } else if (secondArgType != RegisterSecondaryArgument.EMAIL_OPTIONAL && secondArgType != RegisterSecondaryArgument.EMAIL_MANDATORY && secondArgType != RegisterSecondaryArgument.CONFIRMATION) {
            throw new IllegalStateException("Unknown secondary argument type '" + secondArgType + "'");
         } else if (((String)arguments.get(0)).equals(arguments.get(1))) {
            return true;
         } else {
            this.commonService.send(player, MessageKey.USAGE_REGISTER);
            return false;
         }
      } else {
         return true;
      }
   }
}
