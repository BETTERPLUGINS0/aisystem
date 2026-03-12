package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.settings.SpawnLoader;
import java.util.List;
import org.bukkit.entity.Player;

public class SetFirstSpawnCommand extends PlayerCommand {
   @Inject
   private SpawnLoader spawnLoader;

   public void runCommand(Player player, List<String> arguments) {
      if (this.spawnLoader.setFirstSpawn(player.getLocation())) {
         player.sendMessage("[AuthMe] Correctly defined new first spawn point");
      } else {
         player.sendMessage("[AuthMe] SetFirstSpawn has failed, please retry");
      }

   }
}
