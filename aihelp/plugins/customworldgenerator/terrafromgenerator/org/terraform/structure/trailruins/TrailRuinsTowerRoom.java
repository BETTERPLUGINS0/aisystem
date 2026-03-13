package org.terraform.structure.trailruins;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.schematic.SchematicParser;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.V_1_20;
import org.terraform.utils.version.Version;

public class TrailRuinsTowerRoom extends RoomPopulatorAbstract {
   public TrailRuinsTowerRoom(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      try {
         SimpleBlock core = room.getCenterSimpleBlock(data);
         BlockFace direction = BlockUtils.getDirectBlockFace(this.rand);
         TerraSchematic towerBase = TerraSchematic.load("trailruins/trailruins-base-1", core);
         towerBase.parser = new TrailRuinsTowerRoom.TrailRuinsTowerParser(core, this.rand);
         towerBase.setFace(direction);
         towerBase.apply();
         TerraSchematic towerTop = TerraSchematic.load("trailruins/trailruins-top-1", core.getUp(10));
         towerTop.parser = new TrailRuinsTowerRoom.TrailRuinsTowerParser(core.getUp(10), this.rand);
         towerTop.setFace(direction);
         towerTop.apply();
      } catch (FileNotFoundException var7) {
         throw new RuntimeException(var7);
      }
   }

   public boolean canPopulate(CubeRoom room) {
      return false;
   }

   public static class TrailRuinsTowerParser extends SchematicParser {
      @NotNull
      final HashMap<Material, Material> newMapping = new HashMap();
      @NotNull
      private final SimpleBlock destruction;
      @NotNull
      private final Random random;

      public TrailRuinsTowerParser(@NotNull SimpleBlock core, @NotNull Random rand) {
         this.random = rand;
         this.destruction = core.getRelative(GenUtils.getSign(rand) * rand.nextInt(3), GenUtils.getSign(rand) * rand.nextInt(5), GenUtils.getSign(rand) * rand.nextInt(3));
         this.newMapping.put(Material.YELLOW_GLAZED_TERRACOTTA, (Material)GenUtils.randChoice((Object[])BlockUtils.GLAZED_TERRACOTTA));
         this.newMapping.put(Material.LIGHT_BLUE_TERRACOTTA, (Material)GenUtils.randChoice((Object[])BlockUtils.TERRACOTTA));
         this.newMapping.put(Material.CYAN_GLAZED_TERRACOTTA, (Material)GenUtils.randChoice((Object[])BlockUtils.GLAZED_TERRACOTTA));
         this.newMapping.put(Material.CYAN_TERRACOTTA, (Material)GenUtils.randChoice((Object[])BlockUtils.TERRACOTTA));
      }

      public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
         if (!(block.distance(this.destruction) < 3.0D)) {
            if (this.newMapping.containsKey(data.getMaterial())) {
               if (data instanceof Directional) {
                  BlockFace f = ((Directional)data).getFacing();
                  data = Bukkit.createBlockData((Material)this.newMapping.get(data.getMaterial()));
                  ((Directional)data).setFacing(f);
               } else {
                  data = Bukkit.createBlockData((Material)this.newMapping.get(data.getMaterial()));
               }
            }

            super.applyData(block, data);

            for(int i = 1; i < 3 && GenUtils.chance(this.random, 1, 4) && BlockUtils.isStoneLike(block.getUp(i).getType()); ++i) {
               if (Version.VERSION.isAtLeast(Version.v1_20) && GenUtils.chance(this.random, 1, 5)) {
                  block.getUp(i).setType(V_1_20.SUSPICIOUS_GRAVEL);
                  block.getPopData().lootTableChest(block.getX(), block.getY() + i, block.getZ(), GenUtils.chance(this.random, 1, 3) ? TerraLootTable.TRAIL_RUINS_ARCHAEOLOGY_RARE : TerraLootTable.TRAIL_RUINS_ARCHAEOLOGY_COMMON);
               } else {
                  block.getUp(i).setType(Material.SAND, Material.GRAVEL);
               }
            }

         }
      }
   }
}
