package org.terraform.structure.ancientcity;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class AncientCityLargePillarRoomPopulator extends AncientCityAbstractRoomPopulator {
   public AncientCityLargePillarRoomPopulator(TerraformWorld tw, HashSet<SimpleLocation> occupied, RoomLayoutGenerator gen, Random rand, boolean forceSpawn, boolean unique) {
      super(tw, gen, rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      this.effectiveRoom = room;
      int[] lowerCorner = this.effectiveRoom.getLowerCorner(0);
      int[] upperCorner = this.effectiveRoom.getUpperCorner(0);
      int y = this.effectiveRoom.getY();

      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            SimpleBlock b = new SimpleBlock(data, x, y, z);
            if (x != lowerCorner[0] && x != upperCorner[0] && z != lowerCorner[1] && z != upperCorner[1]) {
               b.lsetType(AncientCityUtils.deepslateBricks);
            } else if (this.rand.nextBoolean()) {
               b.lsetType(AncientCityUtils.deepslateBricks);
            }
         }
      }

      FastNoise ruinsNoise = NoiseCacheHandler.getNoise(this.tw, NoiseCacheHandler.NoiseCacheEntry.STRUCTURE_ANCIENTCITY_RUINS, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 11L));
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(0.09F);
         return n;
      });

      try {
         SimpleBlock center = room.getCenterSimpleBlock(data).getUp();
         int maxHeight = 70;

         AncientCityPillarSchematicParser lastParser;
         TerraSchematic schema;
         for(lastParser = null; maxHeight > 0; --maxHeight) {
            schema = TerraSchematic.load("ancient-city/ancient-city-pillar-segment", center);
            lastParser = new AncientCityPillarSchematicParser();
            schema.parser = lastParser;
            schema.setFace(BlockUtils.getDirectBlockFace(this.rand));
            schema.apply();
            center = center.getUp(3);
            if (lastParser.calculateFailRate() > 0.3F) {
               break;
            }
         }

         Iterator var16 = lastParser.getTouchedOffsets().iterator();

         SimpleBlock b;
         while(var16.hasNext()) {
            b = (SimpleBlock)var16.next();
            b.getUp().LPillar((int)(5.0F * ruinsNoise.GetNoise((float)b.getX(), (float)b.getZ())), AncientCityUtils.deepslateBricks);
         }

         center = room.getCenterSimpleBlock(data).getDown(3);
         maxHeight = 70;

         for(lastParser = null; maxHeight > 0; --maxHeight) {
            schema = TerraSchematic.load("ancient-city/ancient-city-pillar-segment", center);
            lastParser = new AncientCityPillarSchematicParser();
            schema.parser = lastParser;
            schema.setFace(BlockUtils.getDirectBlockFace(this.rand));
            schema.apply();
            center = center.getDown(3);
            if (lastParser.calculateFailRate() > 0.3F || center.getY() <= TerraformGeneratorPlugin.injector.getMinY()) {
               break;
            }
         }

         var16 = lastParser.getTouchedOffsets().iterator();

         while(var16.hasNext()) {
            b = (SimpleBlock)var16.next();
            b.getDown(3).downLPillar(new Random(), (int)Math.abs(5.0F * ruinsNoise.GetNoise((float)b.getX(), (float)b.getZ())), AncientCityUtils.deepslateBricks);
         }
      } catch (FileNotFoundException var12) {
         TerraformGeneratorPlugin.logger.stackTrace(var12);
      }

      super.sculkUp(this.tw, data, room);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() <= 20 && room.getWidthZ() <= 20;
   }
}
