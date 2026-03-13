package org.terraform.structure.ancientcity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class AncientCityRuinsPlatform extends AncientCityAbstractRoomPopulator {
   public AncientCityRuinsPlatform(TerraformWorld tw, HashSet<SimpleLocation> occupied, RoomLayoutGenerator gen, Random rand, boolean forceSpawn, boolean unique) {
      super(tw, gen, rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      FastNoise ruinsNoise = NoiseCacheHandler.getNoise(this.tw, NoiseCacheHandler.NoiseCacheEntry.STRUCTURE_ANCIENTCITY_RUINS, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 11L));
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(0.09F);
         return n;
      });
      int totalPillars = 0;
      Iterator var5 = this.effectiveRoom.getFourWalls(data, 2).entrySet().iterator();

      while(var5.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var5.next();
         Wall w = (Wall)entry.getKey();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            w.LPillar(Math.round(Math.abs(5.0F * ruinsNoise.GetNoise((float)totalPillars, 0.0F))), AncientCityUtils.deepslateBricks);
            w = w.getLeft();
            ++totalPillars;
         }
      }

      super.sculkUp(this.tw, data, this.effectiveRoom);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() < 20 || room.getWidthZ() < 20;
   }
}
