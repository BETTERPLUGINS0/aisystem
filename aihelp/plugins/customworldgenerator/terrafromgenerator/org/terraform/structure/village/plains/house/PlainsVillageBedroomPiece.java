package org.terraform.structure.village.plains.house;

import java.util.Iterator;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.small_items.DecorationsBuilder;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageBedroomPiece extends PlainsVillageStandardPiece {
   public PlainsVillageBedroomPiece(PlainsVillagePopulator plainsVillagePopulator, PlainsVillageHouseVariant variant, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(plainsVillagePopulator, variant, widthX, height, widthZ, type, validDirs);
   }

   public void build(@NotNull PopulatorDataAbstract data, @NotNull Random rand) {
      super.build(data, rand);
   }

   public void postBuildDecoration(@NotNull Random random, @NotNull PopulatorDataAbstract data) {
      super.postBuildDecoration(random, data);
      if (this.getWalledFaces().isEmpty()) {
         SimpleBlock core = new SimpleBlock(data, this.getRoom().getX(), this.getRoom().getY() + 1, this.getRoom().getZ());
         core.setType(Material.SMOOTH_STONE);
         BlockFace[] var10 = BlockUtils.xzPlaneBlockFaces;
         int var11 = var10.length;

         int var12;
         BlockFace face;
         for(var12 = 0; var12 < var11; ++var12) {
            face = var10[var12];
            (new SlabBuilder(Material.SMOOTH_STONE_SLAB)).setType(Type.TOP).apply(core.getRelative(face));
         }

         var10 = BlockUtils.directBlockFaces;
         var11 = var10.length;

         for(var12 = 0; var12 < var11; ++var12) {
            face = var10[var12];
            (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(face).apply(core.getRelative(face, 2));
         }

         BlockUtils.pickPottedPlant().build(core.getUp());
      } else {
         int placedBeds = 0;
         Iterator var4 = this.getWalledFaces().iterator();

         while(var4.hasNext()) {
            BlockFace face = (BlockFace)var4.next();
            SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, face, 0);
            Wall w = (Wall)entry.getKey();

            int i;
            for(i = 0; i < (Integer)entry.getValue(); ++i) {
               if (!w.getFront().isSolid() && placedBeds < 2 && w.getRear().getType() != this.plainsVillagePopulator.woodDoor && (GenUtils.chance(random, 2, 5) && placedBeds == 0 || GenUtils.chance(random, 1, 10) && placedBeds == 1)) {
                  BlockUtils.placeBed(w.get(), BlockUtils.pickBed(), w.getDirection());
                  ++placedBeds;
                  data.addEntity(w.getX(), w.getY() + 1, w.getZ(), EntityType.VILLAGER);
               }

               w = w.getLeft();
            }

            w = (Wall)entry.getKey();

            for(i = 0; i < (Integer)entry.getValue(); ++i) {
               if (w.getRear().getType() != this.plainsVillagePopulator.woodDoor && !Tag.BEDS.isTagged(w.getType())) {
                  if (!Tag.BEDS.isTagged(w.getRight().getType()) && !Tag.BEDS.isTagged(w.getLeft().getType())) {
                     if (GenUtils.chance(random, 1, 10)) {
                        (new ChestBuilder(Material.CHEST)).setFacing(w.getDirection()).setLootTable(TerraLootTable.VILLAGE_PLAINS_HOUSE).apply(w);
                     } else if (GenUtils.chance(random, 1, 5) && !w.getFront().isSolid()) {
                        (new SlabBuilder(new Material[]{Material.SMOOTH_STONE_SLAB, Material.POLISHED_ANDESITE_SLAB})).setType(Type.TOP).apply(w);
                        (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(w.getDirection()).apply(w.getFront());
                     }
                  } else if (random.nextBoolean()) {
                     (new StairBuilder(new Material[]{Material.STONE_BRICK_STAIRS, Material.POLISHED_ANDESITE_STAIRS})).setFacing(w.getDirection().getOppositeFace()).setHalf(Half.TOP).apply(w);
                     BlockUtils.pickPottedPlant().build(w.getUp());
                  } else {
                     DecorationsBuilder.CRAFTING_TABLE.build(w);
                  }
               }

               w = w.getLeft();
            }
         }

      }
   }
}
