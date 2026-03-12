package fr.xephi.authme.command.executable;

import fr.xephi.authme.command.CommandMapper;
import fr.xephi.authme.command.CommandUtils;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.command.FoundCommandResult;
import fr.xephi.authme.command.FoundResultStatus;
import fr.xephi.authme.command.help.HelpProvider;
import fr.xephi.authme.libs.javax.inject.Inject;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements ExecutableCommand {
   @Inject
   private CommandMapper commandMapper;
   @Inject
   private HelpProvider helpProvider;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      FoundCommandResult result = this.commandMapper.mapPartsToCommand(sender, arguments);
      FoundResultStatus resultStatus = result.getResultStatus();
      if (FoundResultStatus.MISSING_BASE_COMMAND.equals(resultStatus)) {
         sender.sendMessage(ChatColor.DARK_RED + "Could not get base command");
      } else {
         if (FoundResultStatus.UNKNOWN_LABEL.equals(resultStatus)) {
            if (result.getCommandDescription() == null) {
               sender.sendMessage(ChatColor.DARK_RED + "Unknown command");
               return;
            }

            sender.sendMessage(ChatColor.GOLD + "Assuming " + ChatColor.WHITE + CommandUtils.constructCommandPath(result.getCommandDescription()));
         }

         int mappedCommandLevel = result.getCommandDescription().getLabelCount();
         if (mappedCommandLevel == 1) {
            this.helpProvider.outputHelp(sender, result, 99);
         } else {
            this.helpProvider.outputHelp(sender, result, -1);
         }

      }
   }
}
