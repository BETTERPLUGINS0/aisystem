package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class SpawnLocationViewer implements DebugSection {
   @Inject
   private SpawnLoader spawnLoader;
   @Inject
   private Settings settings;
   @Inject
   private BukkitService bukkitService;

   public String getName() {
      return "spawn";
   }

   public String getDescription() {
      return "Shows the spawn location that AuthMe will use";
   }

   public void execute(CommandSender sender, List<String> arguments) {
      sender.sendMessage(ChatColor.BLUE + "AuthMe spawn location viewer");
      if (arguments.isEmpty()) {
         this.showGeneralInfo(sender);
      } else if ("?".equals(arguments.get(0))) {
         this.showHelp(sender);
      } else {
         this.showPlayerSpawn(sender, (String)arguments.get(0));
      }

   }

   public PermissionNode getRequiredPermission() {
      return DebugSectionPermissions.SPAWN_LOCATION;
   }

   private void showGeneralInfo(CommandSender sender) {
      sender.sendMessage("Spawn priority: " + String.join(", ", (CharSequence)this.settings.getProperty(RestrictionSettings.SPAWN_PRIORITY)));
      sender.sendMessage("AuthMe spawn location: " + DebugSectionUtils.formatLocation(this.spawnLoader.getSpawn()));
      sender.sendMessage("AuthMe first spawn location: " + DebugSectionUtils.formatLocation(this.spawnLoader.getFirstSpawn()));
      sender.sendMessage("AuthMe (first)spawn are only used depending on the configured priority!");
      sender.sendMessage("Use '/authme debug spawn ?' for further help");
   }

   private void showHelp(CommandSender sender) {
      sender.sendMessage("Use /authme spawn and /authme firstspawn to teleport to the spawns.");
      sender.sendMessage("/authme set(first)spawn sets the (first) spawn to your current location.");
      sender.sendMessage("Use /authme debug spawn <player> to view where a player would be teleported to.");
      sender.sendMessage("Read more at https://github.com/AuthMe/AuthMeReloaded/wiki/Spawn-Handling");
   }

   private void showPlayerSpawn(CommandSender sender, String playerName) {
      Player player = this.bukkitService.getPlayerExact(playerName);
      if (player == null) {
         sender.sendMessage("Player '" + playerName + "' is not online!");
      } else {
         Location spawn = this.spawnLoader.getSpawnLocation(player);
         sender.sendMessage("Player '" + playerName + "' has spawn location: " + DebugSectionUtils.formatLocation(spawn));
         sender.sendMessage("Note: this check excludes the AuthMe firstspawn.");
      }

   }
}
