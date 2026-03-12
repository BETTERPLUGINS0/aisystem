package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.command.help.HelpMessagesService;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.HelpTranslationGenerator;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.command.CommandSender;

public class UpdateHelpMessagesCommand implements ExecutableCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(UpdateHelpMessagesCommand.class);
   @Inject
   private HelpTranslationGenerator helpTranslationGenerator;
   @Inject
   private HelpMessagesService helpMessagesService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      try {
         File updatedFile = this.helpTranslationGenerator.updateHelpFile();
         sender.sendMessage("Successfully updated the help file '" + updatedFile.getName() + "'");
         this.helpMessagesService.reloadMessagesFile();
      } catch (IOException var4) {
         sender.sendMessage("Could not update help file: " + var4.getMessage());
         this.logger.logException("Could not update help file:", var4);
      }

   }
}
