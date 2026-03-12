package fr.xephi.authme.command;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.command.help.HelpProvider;
import fr.xephi.authme.libs.ch.jalu.injector.factory.Factory;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandHandler {
   private static final double SUGGEST_COMMAND_THRESHOLD = 0.75D;
   private final CommandMapper commandMapper;
   private final PermissionsManager permissionsManager;
   private final Messages messages;
   private final HelpProvider helpProvider;
   private Map<Class<? extends ExecutableCommand>, ExecutableCommand> commands = new HashMap();

   @Inject
   CommandHandler(Factory<ExecutableCommand> commandFactory, CommandMapper commandMapper, PermissionsManager permissionsManager, Messages messages, HelpProvider helpProvider) {
      this.commandMapper = commandMapper;
      this.permissionsManager = permissionsManager;
      this.messages = messages;
      this.helpProvider = helpProvider;
      this.initializeCommands(commandFactory, commandMapper.getCommandClasses());
   }

   public boolean processCommand(CommandSender sender, String bukkitCommandLabel, String[] bukkitArgs) {
      List<String> parts = skipEmptyArguments(bukkitArgs);
      parts.add(0, bukkitCommandLabel);
      FoundCommandResult result = this.commandMapper.mapPartsToCommand(sender, parts);
      this.handleCommandResult(sender, result);
      return !FoundResultStatus.MISSING_BASE_COMMAND.equals(result.getResultStatus());
   }

   private void handleCommandResult(CommandSender sender, FoundCommandResult result) {
      switch(result.getResultStatus()) {
      case SUCCESS:
         this.executeCommand(sender, result);
         break;
      case MISSING_BASE_COMMAND:
         sender.sendMessage(ChatColor.DARK_RED + "Failed to parse " + AuthMe.getPluginName() + " command!");
         break;
      case INCORRECT_ARGUMENTS:
         this.sendImproperArgumentsMessage(sender, result);
         break;
      case UNKNOWN_LABEL:
         sendUnknownCommandMessage(sender, result);
         break;
      case NO_PERMISSION:
         this.messages.send(sender, MessageKey.NO_PERMISSION);
         break;
      default:
         throw new IllegalStateException("Unknown result status '" + result.getResultStatus() + "'");
      }

   }

   private void initializeCommands(Factory<ExecutableCommand> commandFactory, Set<Class<? extends ExecutableCommand>> commandClasses) {
      Iterator var3 = commandClasses.iterator();

      while(var3.hasNext()) {
         Class<? extends ExecutableCommand> clazz = (Class)var3.next();
         this.commands.put(clazz, (ExecutableCommand)commandFactory.newInstance(clazz));
      }

   }

   private void executeCommand(CommandSender sender, FoundCommandResult result) {
      ExecutableCommand executableCommand = (ExecutableCommand)this.commands.get(result.getCommandDescription().getExecutableCommand());
      List<String> arguments = result.getArguments();
      executableCommand.executeCommand(sender, arguments);
   }

   private static List<String> skipEmptyArguments(String[] args) {
      List<String> cleanArguments = new ArrayList();
      String[] var2 = args;
      int var3 = args.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String argument = var2[var4];
         if (!StringUtils.isBlank(argument)) {
            cleanArguments.add(argument);
         }
      }

      return cleanArguments;
   }

   private static void sendUnknownCommandMessage(CommandSender sender, FoundCommandResult result) {
      sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
      if (result.getDifference() <= 0.75D && result.getCommandDescription() != null) {
         sender.sendMessage(ChatColor.YELLOW + "Did you mean " + ChatColor.GOLD + CommandUtils.constructCommandPath(result.getCommandDescription()) + ChatColor.YELLOW + "?");
      }

      sender.sendMessage(ChatColor.YELLOW + "Use the command " + ChatColor.GOLD + "/" + (String)result.getLabels().get(0) + " help" + ChatColor.YELLOW + " to view help.");
   }

   private void sendImproperArgumentsMessage(CommandSender sender, FoundCommandResult result) {
      CommandDescription command = result.getCommandDescription();
      if (!this.permissionsManager.hasPermission(sender, command.getPermission())) {
         this.messages.send(sender, MessageKey.NO_PERMISSION);
      } else {
         ExecutableCommand executableCommand = (ExecutableCommand)this.commands.get(command.getExecutableCommand());
         MessageKey usageMessage = executableCommand.getArgumentsMismatchMessage();
         if (usageMessage == null) {
            this.showHelpForCommand(sender, result);
         } else {
            this.messages.send(sender, usageMessage);
         }

      }
   }

   private void showHelpForCommand(CommandSender sender, FoundCommandResult result) {
      sender.sendMessage(ChatColor.DARK_RED + "Incorrect command arguments!");
      this.helpProvider.outputHelp(sender, result, 8);
      List<String> labels = result.getLabels();
      String childLabel = labels.size() >= 2 ? (String)labels.get(1) : "";
      sender.sendMessage(ChatColor.GOLD + "Detailed help: " + ChatColor.WHITE + "/" + (String)labels.get(0) + " help " + childLabel);
   }
}
