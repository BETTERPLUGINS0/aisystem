package fr.xephi.authme.command.executable.authme.debug;

import fr.xephi.authme.permission.PermissionNode;
import java.util.List;
import org.bukkit.command.CommandSender;

interface DebugSection {
   String getName();

   String getDescription();

   void execute(CommandSender var1, List<String> var2);

   PermissionNode getRequiredPermission();
}
