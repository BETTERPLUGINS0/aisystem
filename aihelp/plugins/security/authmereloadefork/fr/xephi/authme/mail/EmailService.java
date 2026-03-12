package fr.xephi.authme.mail;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.libs.org.apache.commons.mail.EmailException;
import fr.xephi.authme.libs.org.apache.commons.mail.HtmlEmail;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.SecuritySettings;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.IOException;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;

public class EmailService {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(EmailService.class);
   private final File dataFolder;
   private final Settings settings;
   private final SendMailSsl sendMailSsl;

   @Inject
   EmailService(@DataFolder File dataFolder, Settings settings, SendMailSsl sendMailSsl) {
      this.dataFolder = dataFolder;
      this.settings = settings;
      this.sendMailSsl = sendMailSsl;
   }

   public boolean hasAllInformation() {
      return this.sendMailSsl.hasAllInformation();
   }

   public boolean sendNewPasswordMail(String name, String mailAddress, String newPass, String ip, String time) {
      HtmlEmail email;
      try {
         email = this.sendMailSsl.initializeMail(mailAddress);
      } catch (EmailException var11) {
         this.logger.logException("Failed to create email with the given settings:", var11);
         return false;
      }

      String mailText = this.replaceTagsForPasswordMail(this.settings.getNewPasswordEmailMessage(), name, newPass, ip, time);
      File file = null;
      if ((Boolean)this.settings.getProperty(EmailSettings.PASSWORD_AS_IMAGE)) {
         try {
            file = this.generatePasswordImage(name, newPass);
            mailText = embedImageIntoEmailContent(file, email, mailText);
         } catch (EmailException | IOException var10) {
            this.logger.logException("Unable to send new password as image for email " + mailAddress + ":", var10);
         }
      }

      boolean couldSendEmail = this.sendMailSsl.sendEmail(mailText, email);
      FileUtils.delete(file);
      return couldSendEmail;
   }

   public boolean sendPasswordMail(String name, String mailAddress, String newPass, String time) {
      if (!this.hasAllInformation()) {
         this.logger.warning("Cannot perform email registration: not all email settings are complete");
         return false;
      } else {
         HtmlEmail email;
         try {
            email = this.sendMailSsl.initializeMail(mailAddress);
         } catch (EmailException var10) {
            this.logger.logException("Failed to create email with the given settings:", var10);
            return false;
         }

         String mailText = this.replaceTagsForPasswordMail(this.settings.getPasswordEmailMessage(), name, newPass, time);
         File file = null;
         if ((Boolean)this.settings.getProperty(EmailSettings.PASSWORD_AS_IMAGE)) {
            try {
               file = this.generatePasswordImage(name, newPass);
               mailText = embedImageIntoEmailContent(file, email, mailText);
            } catch (EmailException | IOException var9) {
               this.logger.logException("Unable to send new password as image for email " + mailAddress + ":", var9);
            }
         }

         boolean couldSendEmail = this.sendMailSsl.sendEmail(mailText, email);
         FileUtils.delete(file);
         return couldSendEmail;
      }
   }

   public void sendVerificationMail(String name, String mailAddress, String code, String time) {
      if (!this.hasAllInformation()) {
         this.logger.warning("Cannot send verification email: not all email settings are complete");
      } else {
         HtmlEmail email;
         try {
            email = this.sendMailSsl.initializeMail(mailAddress);
         } catch (EmailException var7) {
            this.logger.logException("Failed to create verification email with the given settings:", var7);
            return;
         }

         String mailText = this.replaceTagsForVerificationEmail(this.settings.getVerificationEmailMessage(), name, code, (Integer)this.settings.getProperty(SecuritySettings.VERIFICATION_CODE_EXPIRATION_MINUTES), time);
         this.sendMailSsl.sendEmail(mailText, email);
      }
   }

   public boolean sendRecoveryCode(String name, String email, String code, String time) {
      HtmlEmail htmlEmail;
      try {
         htmlEmail = this.sendMailSsl.initializeMail(email);
      } catch (EmailException var7) {
         this.logger.logException("Failed to create email for recovery code:", var7);
         return false;
      }

      String message = this.replaceTagsForRecoveryCodeMail(this.settings.getRecoveryCodeEmailMessage(), name, code, (Integer)this.settings.getProperty(SecuritySettings.RECOVERY_CODE_HOURS_VALID), time);
      return this.sendMailSsl.sendEmail(message, htmlEmail);
   }

   public void sendShutDown(String email, String time) {
      HtmlEmail htmlEmail;
      try {
         htmlEmail = this.sendMailSsl.initializeMail(email);
      } catch (EmailException var5) {
         this.logger.logException("Failed to create email for shutdown:", var5);
         return;
      }

      String message = this.replaceTagsForShutDownMail(this.settings.getShutdownEmailMessage(), time);
      this.sendMailSsl.sendEmail(message, htmlEmail);
   }

   private File generatePasswordImage(String name, String newPass) throws IOException {
      ImageGenerator gen = new ImageGenerator(newPass);
      File file = new File(this.dataFolder, name + "_new_pass.jpg");
      ImageIO.write(gen.generateImage(), "jpg", file);
      return file;
   }

   private static String embedImageIntoEmailContent(File image, HtmlEmail email, String content) throws EmailException {
      DataSource source = new FileDataSource(image);
      String tag = email.embed((DataSource)source, image.getName());
      return content.replace("<image />", "<img src=\"cid:" + tag + "\">");
   }

   private String replaceTagsForPasswordMail(String mailText, String name, String newPass, String ip, String time) {
      return mailText.replace("<playername />", name).replace("<servername />", (CharSequence)this.settings.getProperty(PluginSettings.SERVER_NAME)).replace("<generatedpass />", newPass).replace("<playerip />", ip).replace("<time />", time);
   }

   private String replaceTagsForPasswordMail(String mailText, String name, String newPass, String time) {
      return mailText.replace("<playername />", name).replace("<servername />", (CharSequence)this.settings.getProperty(PluginSettings.SERVER_NAME)).replace("<generatedpass />", newPass).replace("<time />", time);
   }

   private String replaceTagsForVerificationEmail(String mailText, String name, String code, int minutesValid, String time) {
      return mailText.replace("<playername />", name).replace("<servername />", (CharSequence)this.settings.getProperty(PluginSettings.SERVER_NAME)).replace("<generatedcode />", code).replace("<minutesvalid />", String.valueOf(minutesValid)).replace("<time />", time);
   }

   private String replaceTagsForRecoveryCodeMail(String mailText, String name, String code, int hoursValid, String time) {
      return mailText.replace("<playername />", name).replace("<servername />", (CharSequence)this.settings.getProperty(PluginSettings.SERVER_NAME)).replace("<recoverycode />", code).replace("<hoursvalid />", String.valueOf(hoursValid)).replace("<time />", time);
   }

   private String replaceTagsForShutDownMail(String mailText, String time) {
      return mailText.replace("<servername />", (CharSequence)this.settings.getProperty(PluginSettings.SERVER_NAME)).replace("<time />", time);
   }
}
