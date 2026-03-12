package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValue;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.libs.org.apache.commons.mail.EmailException;
import fr.xephi.authme.libs.org.apache.commons.mail.HtmlEmail;
import fr.xephi.authme.mail.SendMailSsl;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.util.StringUtils;
import fr.xephi.authme.util.Utils;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

class TestEmailSender implements DebugSection {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(TestEmailSender.class);
   @Inject
   private DataSource dataSource;
   @Inject
   private SendMailSsl sendMailSsl;
   @Inject
   private Server server;

   public String getName() {
      return "mail";
   }

   public String getDescription() {
      return "Sends out a test email";
   }

   public void execute(CommandSender sender, List<String> arguments) {
      sender.sendMessage(ChatColor.BLUE + "AuthMe test email sender");
      if (!this.sendMailSsl.hasAllInformation()) {
         sender.sendMessage(ChatColor.RED + "You haven't set all required configurations in config.yml for sending emails. Please check your config.yml");
      } else {
         String email = this.getEmail(sender, arguments);
         if (email != null) {
            boolean sendMail = this.sendTestEmail(email);
            if (sendMail) {
               sender.sendMessage("Test email sent to " + email + " with success");
            } else {
               sender.sendMessage(ChatColor.RED + "Failed to send test mail to " + email + "; please check your logs");
            }
         }

      }
   }

   public PermissionNode getRequiredPermission() {
      return DebugSectionPermissions.TEST_EMAIL;
   }

   private String getEmail(CommandSender sender, List<String> arguments) {
      if (arguments.isEmpty()) {
         DataSourceValue<String> emailResult = this.dataSource.getEmail(sender.getName());
         if (!emailResult.rowExists()) {
            sender.sendMessage(ChatColor.RED + "Please provide an email address, e.g. /authme debug mail test@example.com");
            return null;
         } else {
            String email = (String)emailResult.getValue();
            if (Utils.isEmailEmpty(email)) {
               sender.sendMessage(ChatColor.RED + "No email set for your account! Please use /authme debug mail <email>");
               return null;
            } else {
               return email;
            }
         }
      } else {
         String email = (String)arguments.get(0);
         if (StringUtils.isInsideString('@', email)) {
            return email;
         } else {
            sender.sendMessage(ChatColor.RED + "Invalid email! Usage: /authme debug mail test@example.com");
            return null;
         }
      }
   }

   private boolean sendTestEmail(String email) {
      HtmlEmail htmlEmail;
      try {
         htmlEmail = this.sendMailSsl.initializeMail(email);
      } catch (EmailException var4) {
         this.logger.logException("Failed to create email for sample email:", var4);
         return false;
      }

      htmlEmail.setSubject("AuthMe test email");
      String message = "Hello there!<br />This is a sample email sent to you from a Minecraft server (" + this.server.getName() + ") via /authme debug mail. If you're seeing this, sending emails should be fine.";
      return this.sendMailSsl.sendEmail(message, htmlEmail);
   }
}
