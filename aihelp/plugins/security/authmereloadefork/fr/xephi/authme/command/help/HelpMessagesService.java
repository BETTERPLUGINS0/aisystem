package fr.xephi.authme.command.help;

import fr.xephi.authme.command.CommandArgumentDescription;
import fr.xephi.authme.command.CommandDescription;
import fr.xephi.authme.command.CommandUtils;
import fr.xephi.authme.libs.com.google.common.base.CaseFormat;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.HelpMessagesFileHandler;
import fr.xephi.authme.permission.DefaultPermission;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HelpMessagesService {
   private static final String COMMAND_PREFIX = "commands.";
   private static final String DESCRIPTION_SUFFIX = ".description";
   private static final String DETAILED_DESCRIPTION_SUFFIX = ".detailedDescription";
   private static final String DEFAULT_PERMISSIONS_PATH = "common.defaultPermissions.";
   private final HelpMessagesFileHandler helpMessagesFileHandler;

   @Inject
   HelpMessagesService(HelpMessagesFileHandler helpMessagesFileHandler) {
      this.helpMessagesFileHandler = helpMessagesFileHandler;
   }

   public CommandDescription buildLocalizedDescription(CommandDescription command) {
      String path = "commands." + getCommandSubPath(command);
      if (!this.helpMessagesFileHandler.hasSection(path)) {
         return command;
      } else {
         CommandDescription.CommandBuilder var10000 = CommandDescription.builder();
         String var10002 = path + ".description";
         Objects.requireNonNull(command);
         var10000 = var10000.description(this.getText(var10002, command::getDescription));
         var10002 = path + ".detailedDescription";
         Objects.requireNonNull(command);
         CommandDescription.CommandBuilder builder = var10000.detailedDescription(this.getText(var10002, command::getDetailedDescription)).executableCommand(command.getExecutableCommand()).parent(command.getParent()).labels(command.getLabels()).permission(command.getPermission());
         int i = 1;

         for(Iterator var5 = command.getArguments().iterator(); var5.hasNext(); ++i) {
            CommandArgumentDescription argument = (CommandArgumentDescription)var5.next();
            String argPath = path + ".arg" + i;
            String var10001 = argPath + ".label";
            Objects.requireNonNull(argument);
            String label = this.getText(var10001, argument::getName);
            var10001 = argPath + ".description";
            Objects.requireNonNull(argument);
            String description = this.getText(var10001, argument::getDescription);
            builder.withArgument(label, description, argument.isOptional());
         }

         CommandDescription localCommand = builder.build();
         localCommand.getChildren().addAll(command.getChildren());
         return localCommand;
      }
   }

   public String getDescription(CommandDescription command) {
      String var10001 = "commands." + getCommandSubPath(command) + ".description";
      Objects.requireNonNull(command);
      return this.getText(var10001, command::getDescription);
   }

   public String getMessage(HelpMessage message) {
      return this.helpMessagesFileHandler.getMessage(message.getKey());
   }

   public String getMessage(HelpSection section) {
      return this.helpMessagesFileHandler.getMessage(section.getKey());
   }

   public String getMessage(DefaultPermission defaultPermission) {
      String path = "common.defaultPermissions." + getDefaultPermissionsSubPath(defaultPermission);
      return this.helpMessagesFileHandler.getMessage(path);
   }

   public static String getDefaultPermissionsSubPath(DefaultPermission defaultPermission) {
      return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, defaultPermission.name());
   }

   private String getText(String path, Supplier<String> defaultTextGetter) {
      String message = this.helpMessagesFileHandler.getMessageIfExists(path);
      return message == null ? (String)defaultTextGetter.get() : message;
   }

   public void reloadMessagesFile() {
      this.helpMessagesFileHandler.reload();
   }

   public static String getCommandSubPath(CommandDescription command) {
      return (String)CommandUtils.constructParentList(command).stream().map((cmd) -> {
         return (String)cmd.getLabels().get(0);
      }).collect(Collectors.joining("."));
   }
}
