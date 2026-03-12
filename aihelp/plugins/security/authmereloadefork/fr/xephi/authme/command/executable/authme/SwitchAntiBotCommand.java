package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.CommandMapper;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.command.FoundCommandResult;
import fr.xephi.authme.command.help.HelpProvider;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.AntiBotService;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SwitchAntiBotCommand implements ExecutableCommand {
   @Inject
   private AntiBotService antiBotService;
   @Inject
   private CommandMapper commandMapper;
   @Inject
   private HelpProvider helpProvider;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      if (arguments.isEmpty()) {
         sender.sendMessage("[AuthMe] AntiBot status: " + this.antiBotService.getAntiBotStatus().name());
      } else {
         String newState = (String)arguments.get(0);
         if ("ON".equalsIgnoreCase(newState)) {
            this.antiBotService.overrideAntiBotStatus(true);
            sender.sendMessage("[AuthMe] AntiBot Manual Override: enabled!");
         } else if ("OFF".equalsIgnoreCase(newState)) {
            this.antiBotService.overrideAntiBotStatus(false);
            sender.sendMessage("[AuthMe] AntiBot Manual Override: disabled!");
         } else {
            sender.sendMessage(ChatColor.DARK_RED + "Invalid AntiBot mode!");
            FoundCommandResult result = this.commandMapper.mapPartsToCommand(sender, Arrays.asList("authme", "antibot"));
            this.helpProvider.outputHelp(sender, result, 8);
            sender.sendMessage(ChatColor.GOLD + "Detailed help: " + ChatColor.WHITE + "/authme help antibot");
         }

      }
   }
}
