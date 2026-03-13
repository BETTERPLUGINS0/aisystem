package com.ryandw11.structure.schematic;

import com.ryandw11.structure.api.structaddon.StructureSign;
import com.ryandw11.structure.schematic.structuresigns.CommandSign;
import com.ryandw11.structure.schematic.structuresigns.MobSign;
import com.ryandw11.structure.schematic.structuresigns.MythicMobSign;
import com.ryandw11.structure.schematic.structuresigns.NPCSign;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class StructureSignHandler {
   private final Map<String, Class<? extends StructureSign>> structureSigns = new HashMap();

   public StructureSignHandler() {
      this.registerStructureSign("mob", MobSign.class);
      this.registerStructureSign("npc", NPCSign.class);
      this.registerStructureSign("command", CommandSign.class);
      this.registerStructureSign("commands", CommandSign.class);
      this.registerStructureSign("mythicmob", MythicMobSign.class);
      this.registerStructureSign("mythicalmob", MythicMobSign.class);
   }

   public boolean registerStructureSign(@NotNull String name, @NotNull Class<? extends StructureSign> structureSignClass) {
      if (this.structureSigns.containsKey(name.toUpperCase())) {
         return false;
      } else if (!name.equalsIgnoreCase("schem") && !name.equalsIgnoreCase("schematic") && !name.equalsIgnoreCase("advschem")) {
         this.structureSigns.put(name.toUpperCase(), structureSignClass);
         return true;
      } else {
         return false;
      }
   }

   public Map<String, Class<? extends StructureSign>> getRegisteredStructureSigns() {
      return this.structureSigns;
   }

   public Class<? extends StructureSign> getStructureSign(@NotNull String name) {
      return (Class)this.structureSigns.get(name.toUpperCase());
   }

   public boolean structureSignExists(@NotNull String name) {
      return this.structureSigns.containsKey(name.toUpperCase());
   }
}
