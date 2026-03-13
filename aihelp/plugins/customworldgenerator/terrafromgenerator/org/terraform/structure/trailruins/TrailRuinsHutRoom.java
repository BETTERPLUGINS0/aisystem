package org.terraform.structure.trailruins;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.version.V_1_19;
import org.terraform.utils.version.V_1_20;
import org.terraform.utils.version.Version;

public class TrailRuinsHutRoom extends RoomPopulatorAbstract {
   public TrailRuinsHutRoom(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      Material terracottaType = (Material)GenUtils.randChoice((Object[])BlockUtils.TERRACOTTA);
      Iterator var4 = room.getFourWalls(data, 1).entrySet().iterator();

      while(var4.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var4.next();
         Wall w = (Wall)entry.getKey();

         for(int i = 1; i < (Integer)entry.getValue() - 1; ++i) {
            if (w.getDown().isSolid()) {
               int h = GenUtils.randInt(this.rand, 1, 5);
               w.Pillar(Math.min(2, h), new Material[]{V_1_19.MUD_BRICKS});
               w.getUp(2).Pillar(Math.min(0, h - 2), new Material[]{terracottaType});
               if ((data.getBiome(w.getX(), w.getZ()) == Biome.JUNGLE || data.getBiome(w.getX(), w.getZ()) == Biome.BAMBOO_JUNGLE) && i > 1 && i < (Integer)entry.getValue() - 2 && GenUtils.chance(this.rand, 1, 9)) {
                  (new ChestBuilder(Material.CHEST)).setFacing(w.getDirection()).setLootTable(TerraLootTable.JUNGLE_TEMPLE).apply(w.getFront().getRight().getUp());
               }
            }

            w = w.getLeft();
         }
      }

      if (Version.VERSION.isAtLeast(Version.v1_20)) {
         for(int i = 0; i < this.rand.nextInt(4); ++i) {
            int[] coords = room.randomCoords(this.rand);
            data.setType(coords[0], room.getY(), coords[2], V_1_20.SUSPICIOUS_GRAVEL);
            data.lootTableChest(coords[0], room.getY(), coords[2], GenUtils.chance(this.rand, 1, 3) ? TerraLootTable.TRAIL_RUINS_ARCHAEOLOGY_RARE : TerraLootTable.TRAIL_RUINS_ARCHAEOLOGY_COMMON);
         }
      }

   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
