package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.util.TeleportUtils;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SpawnCommand extends PlayerCommand {
   @Inject
   private SpawnLoader spawnLoader;
   @Inject
   private BukkitService bukkitService;

   public void runCommand(Player player, List<String> arguments) {
      if (this.spawnLoader.getSpawn() == null) {
         player.sendMessage("[AuthMe] Spawn has failed, please try to define the spawn");
      } else {
         this.bukkitService.runTaskIfFolia((Entity)player, () -> {
            TeleportUtils.teleport(player, this.spawnLoader.getSpawn());
         });
      }

   }
}
