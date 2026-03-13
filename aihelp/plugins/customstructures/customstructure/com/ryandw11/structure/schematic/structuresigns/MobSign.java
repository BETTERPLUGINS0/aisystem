package com.ryandw11.structure.schematic.structuresigns;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.StructureSign;
import com.ryandw11.structure.structure.Structure;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class MobSign extends StructureSign {
   private final CustomStructures plugin = CustomStructures.getInstance();

   public boolean onStructureSpawn(@NotNull Location location, @NotNull Structure structure) {
      if (!this.hasArgument(0)) {
         this.plugin.getLogger().warning(String.format("Invalid mob type on a structure sign! (%s)", structure.getName()));
         return true;
      } else {
         String mobName = this.getStringArgument(0).toUpperCase();
         int count = 1;
         if (this.hasArgument(1)) {
            count = Math.min(this.getStylizedIntArgument(1), 40);
         }

         try {
            for(int i = 0; i < count; ++i) {
               Entity ent = ((World)Objects.requireNonNull(location.getWorld())).spawnEntity(location, EntityType.valueOf(mobName));
               if (ent instanceof LivingEntity) {
                  LivingEntity livingEntity = (LivingEntity)ent;
                  livingEntity.setRemoveWhenFarAway(false);
               }
            }
         } catch (IllegalArgumentException var8) {
            this.plugin.getLogger().warning(String.format("Invalid mob type on a structure sign! (%s)", structure.getName()));
         }

         return true;
      }
   }
}
