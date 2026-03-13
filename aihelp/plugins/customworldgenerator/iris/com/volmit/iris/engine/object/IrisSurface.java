package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;

@Desc("The type of surface entities should spawn on")
public enum IrisSurface {
   @Desc("Land surfaces")
   LAND,
   @Desc("Any surfaces animals can spawn on, such as dirt, grass and podzol")
   ANIMAL,
   @Desc("Within the water")
   WATER,
   @Desc("On land or on water")
   OVERWORLD,
   @Desc("Within lava")
   LAVA;

   public boolean matches(Block state) {
      Material var2 = var1.getType();
      if (var2.isSolid()) {
         return this == LAND || this == OVERWORLD || this == ANIMAL && (var2 == Material.GRASS_BLOCK || var2 == Material.DIRT || var2 == Material.DIRT_PATH || var2 == Material.COARSE_DIRT || var2 == Material.ROOTED_DIRT || var2 == Material.PODZOL || var2 == Material.MYCELIUM || var2 == Material.SNOW_BLOCK);
      } else if (var2 == Material.LAVA) {
         return this == LAVA;
      } else if (var2 == Material.WATER || var2 == Material.SEAGRASS || var2 == Material.TALL_SEAGRASS || var2 == Material.KELP_PLANT || var2 == Material.KELP || var1 instanceof Waterlogged && ((Waterlogged)var1).isWaterlogged()) {
         return this == WATER || this == OVERWORLD;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   private static IrisSurface[] $values() {
      return new IrisSurface[]{LAND, ANIMAL, WATER, OVERWORLD, LAVA};
   }
}
