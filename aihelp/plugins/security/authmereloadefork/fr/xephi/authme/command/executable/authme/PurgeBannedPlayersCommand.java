package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.ExecutableCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.task.purge.PurgeService;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class PurgeBannedPlayersCommand implements ExecutableCommand {
   @Inject
   private PurgeService purgeService;
   @Inject
   private BukkitService bukkitService;

   public void executeCommand(CommandSender sender, List<String> arguments) {
      Set<OfflinePlayer> bannedPlayers = this.bukkitService.getBannedPlayers();
      Set<String> namedBanned = new HashSet(bannedPlayers.size());
      Iterator var5 = bannedPlayers.iterator();

      while(var5.hasNext()) {
         OfflinePlayer offlinePlayer = (OfflinePlayer)var5.next();
         namedBanned.add(offlinePlayer.getName().toLowerCase(Locale.ROOT));
      }

      this.purgeService.purgePlayers(sender, namedBanned, (OfflinePlayer[])bannedPlayers.toArray(new OfflinePlayer[bannedPlayers.size()]));
   }
}
