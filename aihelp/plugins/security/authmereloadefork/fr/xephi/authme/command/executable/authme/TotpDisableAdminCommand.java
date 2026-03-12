package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.BukkitService;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TotpDisableAdminCommand implements ExecutableCommand {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(TotpDisableAdminCommand.class);
   @Inject
   private DataSource dataSource;
   @Inject
   private Messages messages;
   @Inject
   private BukkitService bukkitService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      String player = (String)arguments.get(0);
      PlayerAuth auth = this.dataSource.getAuth(player);
      if (auth == null) {
         this.messages.send(sender, MessageKey.UNKNOWN_USER);
      } else if (auth.getTotpKey() == null) {
         sender.sendMessage(ChatColor.RED + "Player '" + player + "' does not have two-factor auth enabled");
      } else {
         this.removeTotpKey(sender, player);
      }

   }

   private void removeTotpKey(CommandSender sender, String player) {
      if (this.dataSource.removeTotpKey(player)) {
         sender.sendMessage("Disabled two-factor authentication successfully for '" + player + "'");
         this.logger.info(sender.getName() + " disable two-factor authentication for '" + player + "'");
         Player onlinePlayer = this.bukkitService.getPlayerExact(player);
         if (onlinePlayer != null) {
            this.messages.send(onlinePlayer, MessageKey.TWO_FACTOR_REMOVED_SUCCESS);
         }
      } else {
         this.messages.send(sender, MessageKey.ERROR);
      }

   }
}
