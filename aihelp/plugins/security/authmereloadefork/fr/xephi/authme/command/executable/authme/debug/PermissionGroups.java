package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.data.limbo.UserGroup;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.permission.DebugSectionPermissions;
import fr.xephi.authme.permission.PermissionNode;
import fr.xephi.authme.permission.PermissionsManager;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class PermissionGroups implements DebugSection {
   @Inject
   private PermissionsManager permissionsManager;

   public String getName() {
      return "groups";
   }

   public String getDescription() {
      return "Show permission groups a player belongs to";
   }

   public void execute(CommandSender sender, List<String> arguments) {
      sender.sendMessage(ChatColor.BLUE + "AuthMe permission groups");
      String name = arguments.isEmpty() ? sender.getName() : (String)arguments.get(0);
      Player player = Bukkit.getPlayer(name);
      if (player == null) {
         sender.sendMessage("Player " + name + " could not be found");
      } else {
         List<String> groupNames = (List)this.permissionsManager.getGroups(player).stream().map(UserGroup::getGroupName).collect(Collectors.toList());
         sender.sendMessage("Player " + name + " has permission groups: " + String.join(", ", groupNames));
         sender.sendMessage("Primary group is: " + this.permissionsManager.getGroups(player));
      }

   }

   public PermissionNode getRequiredPermission() {
      return DebugSectionPermissions.PERM_GROUPS;
   }
}
