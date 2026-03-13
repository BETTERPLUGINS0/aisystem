package org.terraform.structure.village.plains.house;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.FaceAttachable.AttachedFace;
import org.bukkit.block.data.type.Switch;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageKitchenPiece extends PlainsVillageStandardPiece {
   public PlainsVillageKitchenPiece(PlainsVillagePopulator plainsVillagePopulator, PlainsVillageHouseVariant variant, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(plainsVillagePopulator, variant, widthX, height, widthZ, type, validDirs);
   }

   public void postBuildDecoration(@NotNull Random random, @NotNull PopulatorDataAbstract data) {
      super.postBuildDecoration(random, data);
      if (!this.getWalledFaces().isEmpty()) {
         BlockFace primaryWall = (BlockFace)this.getWalledFaces().get(random.nextInt(this.getWalledFaces().size()));
         SimpleBlock core = new SimpleBlock(data, this.getRoom().getX(), this.getRoom().getY() + 1, this.getRoom().getZ());
         int numUtilities = 5;
         if (core.getRelative(primaryWall, 3).getType() == this.plainsVillagePopulator.woodDoor) {
            --numUtilities;
         }

         SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, primaryWall, 0);
         Wall w = (Wall)entry.getKey();
         ArrayList<Material> utilities = Lists.newArrayList(new Material[]{Material.SMOKER});

         int i;
         for(i = 0; i < numUtilities; ++i) {
            utilities.add((Material)GenUtils.randChoice(random, Material.HOPPER, Material.FURNACE, Material.CRAFTING_TABLE));
         }

         Collections.shuffle(utilities);

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            if (w.getRear().getType() != this.plainsVillagePopulator.woodDoor) {
               --numUtilities;
               Material mat = (Material)utilities.get(numUtilities);
               switch(mat) {
               case HOPPER:
                  w.setType(mat);
                  if (w.getRear().getUp().getType() != Material.GLASS_PANE) {
                     Switch lever = (Switch)Bukkit.createBlockData(Material.LEVER);
                     lever.setPowered(true);
                     lever.setAttachedFace(AttachedFace.WALL);
                     lever.setFacing(w.getDirection());
                     w.getUp().setBlockData(lever);
                  }
                  break;
               case FURNACE:
               case SMOKER:
                  (new DirectionalBuilder(mat)).setFacing(w.getDirection()).apply(w);
                  w.getUp().setType(this.plainsVillagePopulator.woodPressurePlate);
                  (new StairBuilder(Material.BRICK_STAIRS)).setFacing(w.getDirection().getOppositeFace()).setHalf(Half.TOP).apply(w.getUp(2));
                  Wall chimneyWall = w.getUp(3);
                  boolean hitCeiling = false;

                  for(int chimneyHeight = 0; chimneyHeight < 4; chimneyWall = chimneyWall.getUp()) {
                     if (chimneyWall.isSolid()) {
                        hitCeiling = true;
                     } else if (hitCeiling) {
                        ++chimneyHeight;
                        if (GenUtils.chance(random, chimneyHeight, 3)) {
                           break;
                        }
                     }

                     chimneyWall.setType(Material.BRICKS);
                  }

                  chimneyWall.setType(Material.BRICK_WALL);
                  break;
               case CRAFTING_TABLE:
                  w.setType(mat);
               }
            }

            w = w.getLeft();
         }

         Iterator var14 = this.getWalledFaces().iterator();

         while(true) {
            BlockFace face;
            do {
               if (!var14.hasNext()) {
                  return;
               }

               face = (BlockFace)var14.next();
            } while(face == primaryWall);

            entry = this.getRoom().getWall(data, face, 0);
            w = (Wall)entry.getKey();

            for(int i = 0; i < (Integer)entry.getValue(); ++i) {
               if (w.getRear().getType() != this.plainsVillagePopulator.woodDoor && !w.isSolid()) {
                  int decor = random.nextInt(5);
                  switch(decor) {
                  case 0:
                     (new StairBuilder(new Material[]{Material.STONE_BRICK_STAIRS, Material.POLISHED_ANDESITE_STAIRS, this.plainsVillagePopulator.woodStairs})).setFacing(w.getDirection().getOppositeFace()).setHalf(Half.TOP).apply(w);
                     break;
                  case 1:
                     w.setType(new Material[]{Material.SMOOTH_STONE, Material.POLISHED_ANDESITE, Material.PUMPKIN, Material.DRIED_KELP_BLOCK, Material.MELON});
                     break;
                  case 2:
                     (new ChestBuilder(Material.CHEST)).setFacing(w.getDirection()).setLootTable(TerraLootTable.VILLAGE_BUTCHER, TerraLootTable.VILLAGE_PLAINS_HOUSE);
                  }
               }

               w = w.getLeft();
            }
         }
      }
   }

   public void build(@NotNull PopulatorDataAbstract data, @NotNull Random rand) {
      super.build(data, rand);
   }
}
