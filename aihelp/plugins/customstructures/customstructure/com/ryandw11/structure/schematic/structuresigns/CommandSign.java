package com.ryandw11.structure.schematic.structuresigns;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.StructureSign;
import com.ryandw11.structure.structure.Structure;
import com.ryandw11.structure.utils.CSUtils;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class CommandSign extends StructureSign {
   public boolean onStructureSpawn(@NotNull Location location, @NotNull Structure structure) {
      CustomStructures plugin = CustomStructures.getInstance();
      if (!this.hasArgument(0)) {
         plugin.getLogger().warning(String.format("Invalid command configuration on a structure sign! (%s)", structure.getName()));
         return true;
      } else {
         List<String> commands = plugin.getSignCommandsHandler().getCommands(this.getStringArgument(0));
         if (commands != null) {
            Iterator var5 = commands.iterator();

            while(var5.hasNext()) {
               String command = (String)var5.next();
               command = CSUtils.replacePlaceHolders(command, location, this.getStructureMinimumLocation(), this.getStructureMaximumLocation(), structure);
               command = CustomStructures.replacePAPIPlaceholders(command);
               Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
               if (plugin.isDebug()) {
                  plugin.getLogger().info("Executing console command: '" + command + "'");
               }
            }
         } else {
            plugin.getLogger().warning(String.format("Unable to execute command group '%s', no configuration found!", this.getStringArgument(0)));
         }

         return true;
      }
   }
}
