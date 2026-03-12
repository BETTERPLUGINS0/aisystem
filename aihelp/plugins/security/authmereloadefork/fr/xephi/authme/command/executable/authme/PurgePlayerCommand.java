package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.task.purge.PurgeExecutor;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class PurgePlayerCommand implements ExecutableCommand {
   @Inject
   private PurgeExecutor purgeExecutor;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private DataSource dataSource;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String option = arguments.size() > 1 ? (String)arguments.get(1) : null;
      this.bukkitService.runTaskAsynchronously(() -> {
         this.executeCommand(sender, (String)arguments.get(0), option);
      });
   }

   private void executeCommand(CommandSender sender, String name, String option) {
      if (!"force".equals(option) && this.dataSource.isAuthAvailable(name)) {
         sender.sendMessage("This player is still registered! Are you sure you want to proceed? Use '/authme purgeplayer " + name + " force' to run the command anyway");
      } else {
         OfflinePlayer offlinePlayer = this.bukkitService.getOfflinePlayer(name);
         this.purgeExecutor.executePurge(Collections.singletonList(offlinePlayer), Collections.singletonList(name.toLowerCase(Locale.ROOT)));
         sender.sendMessage("Purged data for player " + name);
      }

   }
}
