package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnregisterAdminCommand implements ExecutableCommand {
   @Inject
   private DataSource dataSource;
   @Inject
   private CommonService commonService;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private Management management;

   UnregisterAdminCommand() {
   }

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String playerName = (String)arguments.get(0);
      if (!this.dataSource.isAuthAvailable(playerName)) {
         this.commonService.send(sender, MessageKey.UNKNOWN_USER);
      } else {
         Player target = this.bukkitService.getPlayerExact(playerName);
         this.management.performUnregisterByAdmin(sender, playerName, target);
      }
   }
}
