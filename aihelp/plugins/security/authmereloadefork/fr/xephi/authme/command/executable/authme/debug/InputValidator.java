package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.listener.FailedVerificationException;
import fr.xephi.authme.listener.OnJoinVerifier;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.service.ValidationService;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class InputValidator implements DebugSection {
   @Inject
   private ValidationService validationService;
   @Inject
   private Messages messages;
   @Inject
   private OnJoinVerifier onJoinVerifier;

   public String getName() {
      return "valid";
   }

   public String getDescription() {
      return "Checks if your config.yml allows a password / email";
   }

   public void execute(CommandSender sender, List<String> arguments) {
      if (arguments.size() >= 2 && InputValidator.ValidationObject.matchesAny((String)arguments.get(0))) {
         if (InputValidator.ValidationObject.PASS.matches((String)arguments.get(0))) {
            this.validatePassword(sender, (String)arguments.get(1));
         } else if (InputValidator.ValidationObject.MAIL.matches((String)arguments.get(0))) {
            this.validateEmail(sender, (String)arguments.get(1));
         } else {
            if (!InputValidator.ValidationObject.NAME.matches((String)arguments.get(0))) {
               throw new IllegalStateException("Unexpected validation object with arg[0] = '" + (String)arguments.get(0) + "'");
            }

            this.validateUsername(sender, (String)arguments.get(1));
         }
      } else {
         this.displayUsageHint(sender);
      }

   }

   public PermissionNode getRequiredPermission() {
      return DebugSectionPermissions.INPUT_VALIDATOR;
   }

   private void displayUsageHint(CommandSender sender) {
      sender.sendMessage(ChatColor.BLUE + "Validation tests");
      sender.sendMessage("You can define forbidden emails and passwords in your config.yml. You can test your settings with this command.");
      String command = ChatColor.GOLD + "/authme debug valid";
      sender.sendMessage(" Use " + command + " pass <pass>" + ChatColor.RESET + " to check a password");
      sender.sendMessage(" Use " + command + " mail <mail>" + ChatColor.RESET + " to check an email");
      sender.sendMessage(" Use " + command + " name <name>" + ChatColor.RESET + " to check a username");
   }

   private void validatePassword(CommandSender sender, String password) {
      ValidationService.ValidationResult validationResult = this.validationService.validatePassword(password, "");
      sender.sendMessage(ChatColor.BLUE + "Validation of password '" + password + "'");
      if (validationResult.hasError()) {
         this.messages.send(sender, validationResult.getMessageKey(), validationResult.getArgs());
      } else {
         sender.sendMessage(ChatColor.DARK_GREEN + "Valid password!");
      }

   }

   private void validateEmail(CommandSender sender, String email) {
      boolean isValidEmail = this.validationService.validateEmail(email);
      sender.sendMessage(ChatColor.BLUE + "Validation of email '" + email + "'");
      if (isValidEmail) {
         sender.sendMessage(ChatColor.DARK_GREEN + "Valid email!");
      } else {
         sender.sendMessage(ChatColor.DARK_RED + "Email is not valid!");
      }

   }

   private void validateUsername(CommandSender sender, String username) {
      sender.sendMessage(ChatColor.BLUE + "Validation of username '" + username + "'");

      try {
         this.onJoinVerifier.checkIsValidName(username);
         sender.sendMessage("Valid username!");
      } catch (FailedVerificationException var4) {
         this.messages.send(sender, var4.getReason(), var4.getArgs());
      }

   }

   static enum ValidationObject {
      PASS,
      MAIL,
      NAME;

      static boolean matchesAny(String arg) {
         return Arrays.stream(values()).anyMatch((vo) -> {
            return vo.matches(arg);
         });
      }

      boolean matches(String arg) {
         return this.name().equalsIgnoreCase(arg);
      }

      // $FF: synthetic method
      private static InputValidator.ValidationObject[] $values() {
         return new InputValidator.ValidationObject[]{PASS, MAIL, NAME};
      }
   }
}
