package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.task.purge.PurgeService;
import java.util.Calendar;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class PurgeCommand implements ExecutableCommand {
   private static final int MINIMUM_LAST_SEEN_DAYS = 30;
   @Inject
   private PurgeService purgeService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String daysStr = (String)arguments.get(0);
      Integer days = Ints.tryParse(daysStr);
      if (days == null) {
         sender.sendMessage(ChatColor.RED + "The value you've entered is invalid!");
      } else if (days < 30) {
         sender.sendMessage(ChatColor.RED + "You can only purge data older than " + 30 + " days");
      } else {
         Calendar calendar = Calendar.getInstance();
         calendar.add(5, -days);
         long until = calendar.getTimeInMillis();
         this.purgeService.runPurge(sender, until);
      }
   }
}
