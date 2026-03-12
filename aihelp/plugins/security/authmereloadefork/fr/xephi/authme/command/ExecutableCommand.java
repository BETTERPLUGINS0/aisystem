package fr.xephi.authme.command;

import fr.xephi.authme.message.MessageKey;
import java.util.List;
import org.bukkit.command.CommandSender;

public interface ExecutableCommand {
   void executeCommand(CommandSender var1, List<String> var2);

   default MessageKey getArgumentsMismatchMessage() {
      return null;
   }
}
