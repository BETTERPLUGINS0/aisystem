package org.terraform.structure.monument;

import java.util.Locale;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public enum MonumentDesign {
   DARK_PRISMARINE_CORNERS(new Material[]{Material.DARK_PRISMARINE, Material.PRISMARINE_BRICKS}),
   PRISMARINE_LANTERNS(new Material[]{Material.PRISMARINE_BRICKS, Material.PRISMARINE_BRICKS, Material.PRISMARINE}),
   DARK_LIGHTLESS(new Material[]{Material.PRISMARINE, Material.DARK_PRISMARINE, Material.DARK_PRISMARINE, Material.DARK_PRISMARINE});

   final Material[] tileSet;

   private MonumentDesign(Material... param3) {
      this.tileSet = tileSet;
   }

   public Material[] tileSet() {
      return this.tileSet;
   }

   @Nullable
   public Material slab() {
      Material var10000;
      switch(this.ordinal()) {
      case 0:
         var10000 = (Material)GenUtils.randChoice((Object[])(Material.DARK_PRISMARINE_SLAB, Material.PRISMARINE_BRICK_SLAB));
         break;
      case 1:
         var10000 = (Material)GenUtils.randChoice((Object[])(Material.PRISMARINE_SLAB, Material.PRISMARINE_BRICK_SLAB));
         break;
      case 2:
         var10000 = Material.DARK_PRISMARINE_SLAB;
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   @NotNull
   public Material stairs() {
      return Material.DARK_PRISMARINE_STAIRS;
   }

   public Material mat(@NotNull Random rand) {
      return (Material)GenUtils.randChoice(rand, this.tileSet);
   }

   public void spawnLargeLight(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      try {
         ++x;
         ++z;
         ++y;
         TerraSchematic schema = TerraSchematic.load(this.toString().toLowerCase(Locale.ENGLISH) + "-largelight", new SimpleBlock(data, x, y, z));
         schema.parser = new MonumentSchematicParser();
         schema.setFace(BlockFace.NORTH);
         schema.apply();
      } catch (Throwable var6) {
         TerraformGeneratorPlugin.logger.stackTrace(var6);
      }

   }

   public void upSpire(@NotNull SimpleBlock base, @NotNull Random rand) {
      do {
         if (!base.isSolid() && !base.getUp().isSolid()) {
            this.spire(new Wall(base, BlockFace.NORTH), rand);
            return;
         }

         base = base.getUp();
      } while(base.getY() <= TerraformGenerator.seaLevel);

   }

   public void spire(@NotNull Wall w, @NotNull Random rand) {
      this.spire(w, rand, 7);
   }

   public void spire(@NotNull Wall w, @NotNull Random rand, int height) {
      int i;
      Stairs stairs;
      switch(this.ordinal()) {
      case 0:
         for(i = 0; i < height; ++i) {
            if (i == 0) {
               w.setType(Material.DARK_PRISMARINE);
            } else if (i == 3) {
               w.setType(Material.SEA_LANTERN);
            } else {
               w.setType((Material)GenUtils.randChoice((Object[])(Material.DARK_PRISMARINE, Material.PRISMARINE_WALL)));
               if (rand.nextBoolean()) {
                  stairs = (Stairs)Bukkit.createBlockData(Material.DARK_PRISMARINE_STAIRS);
                  stairs.setFacing(BlockUtils.getDirectBlockFace(rand));
                  stairs.setHalf(rand.nextBoolean() ? Half.TOP : Half.BOTTOM);
               }
            }

            w = w.getUp();
         }

         return;
      case 1:
         for(i = 0; i < height; ++i) {
            if (i == 0) {
               w.setType(Material.PRISMARINE_BRICKS);
            } else if (i > height - 2) {
               w.setType(Material.PRISMARINE_WALL);
            } else if (i == height - 2) {
               w.setType(Material.PRISMARINE_BRICKS);
            } else {
               w.setType(Material.PRISMARINE_WALL);
               if (i == 3) {
                  w.setType(Material.SEA_LANTERN);
               }
            }

            w = w.getUp();
         }

         return;
      case 2:
         for(i = 0; i < height; ++i) {
            if (i == 0) {
               w.setType(Material.DARK_PRISMARINE);
            } else if (i > height - 3) {
               w.setType(Material.PRISMARINE_WALL);
            } else {
               w.setType((Material)GenUtils.randChoice((Object[])(Material.DARK_PRISMARINE, Material.PRISMARINE_WALL)));
               if (rand.nextBoolean()) {
                  stairs = (Stairs)Bukkit.createBlockData(Material.DARK_PRISMARINE_STAIRS);
                  stairs.setFacing(BlockUtils.getDirectBlockFace(rand));
                  stairs.setHalf(rand.nextBoolean() ? Half.TOP : Half.BOTTOM);
                  w.setBlockData(stairs);
               }
            }

            w = w.getUp();
         }
      }

   }

   // $FF: synthetic method
   private static MonumentDesign[] $values() {
      return new MonumentDesign[]{DARK_PRISMARINE_CORNERS, PRISMARINE_LANTERNS, DARK_LIGHTLESS};
   }
}
