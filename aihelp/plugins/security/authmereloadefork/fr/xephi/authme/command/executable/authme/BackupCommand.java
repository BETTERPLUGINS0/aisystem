package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BackupService;
import java.util.List;
import org.bukkit.command.CommandSender;

public class BackupCommand implements ExecutableCommand {
   @Inject
   private BackupService backupService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      this.backupService.doBackup(BackupService.BackupCause.COMMAND, sender);
   }
}
