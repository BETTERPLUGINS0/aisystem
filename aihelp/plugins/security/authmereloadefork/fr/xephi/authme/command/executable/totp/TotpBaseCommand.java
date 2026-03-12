package fr.xephi.authme.command.executable.totp;

import fr.xephi.authme.command.CommandMapper;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.command.FoundCommandResult;
import fr.xephi.authme.command.help.HelpProvider;
import fr.xephi.authme.libs.javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;

public class TotpBaseCommand implements ExecutableCommand {
   @Inject
   private CommandMapper commandMapper;
   @Inject
   private HelpProvider helpProvider;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      FoundCommandResult result = this.commandMapper.mapPartsToCommand(sender, Collections.singletonList("totp"));
      this.helpProvider.outputHelp(sender, result, 64);
   }
}
