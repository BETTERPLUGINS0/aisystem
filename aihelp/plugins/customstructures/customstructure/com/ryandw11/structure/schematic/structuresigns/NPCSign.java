package com.ryandw11.structure.schematic.structuresigns;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.StructureSign;
import com.ryandw11.structure.structure.Structure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class NPCSign extends StructureSign {
   public boolean onStructureSpawn(@NotNull Location location, @NotNull Structure structure) {
      CustomStructures plugin = CustomStructures.getInstance();
      if (!this.hasArgument(0)) {
         plugin.getLogger().warning(String.format("Invalid NPC on structure sign. (%s)", structure.getName()));
         return true;
      } else {
         plugin.getCitizensNpcHook().spawnNpc(plugin.getNpcHandler(), this.getStringArgument(0), location);
         return true;
      }
   }
}
