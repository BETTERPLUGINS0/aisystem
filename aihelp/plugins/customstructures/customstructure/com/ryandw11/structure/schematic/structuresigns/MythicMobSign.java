package com.ryandw11.structure.schematic.structuresigns;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.StructureSign;
import com.ryandw11.structure.structure.Structure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MythicMobSign extends StructureSign {
   private final CustomStructures plugin = CustomStructures.getInstance();

   public boolean onStructureSpawn(@NotNull Location location, @NotNull Structure structure) {
      if (!this.hasArgument(0)) {
         this.plugin.getLogger().warning(String.format("Invalid mythic mob type on a structure sign! (%s)", structure.getName()));
         return true;
      } else {
         String mythicMob = this.getStringArgument(0);
         int count = 1;
         if (this.hasArgument(2)) {
            count = Math.min(this.getStylizedIntArgument(1), 40);
         }

         if (!this.hasArgument(1)) {
            this.plugin.getMythicalMobHook().spawnMob(mythicMob, location, count);
         } else {
            this.plugin.getMythicalMobHook().spawnMob(mythicMob, location, (double)this.getStylizedIntArgument(1), count);
         }

         return true;
      }
   }
}
