package com.ryandw11.structure.citizens;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.NpcHandler;
import org.bukkit.Location;

public class CitizensDisabled implements CitizensNpcHook {
   public void spawnNpc(NpcHandler npcHandler, String alias, Location loc) {
      CustomStructures.plugin.getLogger().info("A schematic tried to spawn a Citizen NPC, but the server does not have that plugin installed!");
   }
}
