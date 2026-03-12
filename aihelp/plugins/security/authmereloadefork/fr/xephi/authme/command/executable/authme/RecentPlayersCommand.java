package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RecentPlayersCommand implements ExecutableCommand {
   private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("hh:mm a, dd MMM");
   @Inject
   private DataSource dataSource;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      List<PlayerAuth> recentPlayers = this.dataSource.getRecentlyLoggedInPlayers();
      sender.sendMessage(ChatColor.BLUE + "[AuthMe] Recently logged in players");
      Iterator var4 = recentPlayers.iterator();

      while(var4.hasNext()) {
         PlayerAuth auth = (PlayerAuth)var4.next();
         sender.sendMessage(this.formatPlayerMessage(auth));
      }

   }

   @VisibleForTesting
   ZoneId getZoneId() {
      return ZoneId.systemDefault();
   }

   private String formatPlayerMessage(PlayerAuth auth) {
      String lastLoginText;
      if (auth.getLastLogin() == null) {
         lastLoginText = "never";
      } else {
         LocalDateTime lastLogin = LocalDateTime.ofInstant(Instant.ofEpochMilli(auth.getLastLogin()), this.getZoneId());
         lastLoginText = DATE_FORMAT.format(lastLogin);
      }

      return "- " + auth.getRealName() + " (" + lastLoginText + " with IP " + auth.getLastIp() + ")";
   }
}
