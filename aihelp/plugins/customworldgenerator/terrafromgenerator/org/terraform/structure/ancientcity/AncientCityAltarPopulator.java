package org.terraform.structure.ancientcity;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayoutGenerator;

public class AncientCityAltarPopulator extends AncientCityAbstractRoomPopulator {
   public AncientCityAltarPopulator(TerraformWorld tw, HashSet<SimpleLocation> occupied, RoomLayoutGenerator gen, Random rand, boolean forceSpawn, boolean unique) {
      super(tw, gen, rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Iterator var3 = this.effectiveRoom.getFourWalls(data, 0).entrySet().iterator();

      Entry entry;
      Wall center;
      boolean shouldPlaceAltar;
      do {
         if (!var3.hasNext()) {
            super.sculkUp(this.tw, data, this.effectiveRoom);
            return;
         }

         entry = (Entry)var3.next();
         Wall w = (Wall)entry.getKey();
         center = null;
         shouldPlaceAltar = true;

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            if (i == (Integer)entry.getValue() / 2) {
               center = w;
            }

            if (this.containsPaths.contains(w)) {
               shouldPlaceAltar = false;
               break;
            }

            w = w.getLeft();
         }
      } while(!shouldPlaceAltar);

      String altarFile;
      if ((Integer)entry.getValue() < 7) {
         altarFile = "ancient-city/ancient-city-altar-small";
      } else if ((Integer)entry.getValue() < 11) {
         altarFile = "ancient-city/ancient-city-altar-medium";
      } else {
         altarFile = "ancient-city/ancient-city-altar-large";
      }

      try {
         TerraSchematic schema = TerraSchematic.load(altarFile, center);
         schema.parser = new AncientCitySchematicParser();
         schema.setFace(center.getDirection());
         schema.apply();
      } catch (FileNotFoundException var11) {
         TerraformGeneratorPlugin.logger.stackTrace(var11);
      }

      int pillarSpacing = (Integer)entry.getValue() / 3;

      for(int i = pillarSpacing; i < Math.min(this.effectiveRoom.getWidthX(), this.effectiveRoom.getWidthZ()); i += 3) {
         center.getFront(i).getLeft(pillarSpacing).LPillar(this.rand.nextInt(room.getHeight() / 3), AncientCityUtils.deepslateBricks);
         center.getFront(i).getRight(pillarSpacing).LPillar(this.rand.nextInt(room.getHeight() / 3), AncientCityUtils.deepslateBricks);
      }

      super.sculkUp(this.tw, data, this.effectiveRoom);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() % 2 == 1 || room.getWidthZ() % 2 == 1;
   }
}
