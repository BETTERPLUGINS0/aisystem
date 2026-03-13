package org.terraform.structure.ancientcity;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.StairwayBuilder;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.StairBuilder;
import org.terraform.utils.version.V_1_19;

public class AncientCityResearchBasementHandler {
   private static final String[] northSouthResearchSchematics = new String[]{"ancient-city/ancient-city-basement-sculkresearch", "ancient-city/ancient-city-basement-redstoneresearch", "ancient-city/ancient-city-basement-sleepingchamber"};
   private static final String[] eastWestResearchSchematics = new String[]{"ancient-city/ancient-city-basement-farm", "ancient-city/ancient-city-basement-cage"};

   public static void populate(@NotNull Random random, @NotNull PopulatorDataAbstract data, @NotNull CubeRoom room, @NotNull BlockFace headFacing) {
      int[] lowerCorner = room.getLowerCorner();
      int[] upperCorner = room.getUpperCorner();

      int x;
      int z;
      int depth;
      for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            for(depth = room.getY(); depth < room.getY() + room.getHeight(); ++depth) {
               if (depth != room.getY() && depth != room.getY() + room.getHeight() - 1) {
                  data.setType(x, depth, z, Material.AIR);
               } else if (depth == room.getY() && x > lowerCorner[0] && x < upperCorner[0] && z > lowerCorner[1] && z < upperCorner[1]) {
                  data.setType(x, depth, z, Material.GRAY_WOOL);
                  data.setType(x, depth - 1, z, AncientCityUtils.deepslateBricks);
               } else {
                  data.setType(x, depth, z, AncientCityUtils.deepslateBricks);
               }
            }
         }
      }

      Iterator var23 = room.getFourWalls(data, 0).entrySet().iterator();

      int nx;
      BlockFace[] var10;
      int relX;
      int relZ;
      BlockFace adj;
      Wall core;
      while(var23.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var23.next();
         Wall w = ((Wall)entry.getKey()).getLeft(3);

         for(nx = 3; nx < (Integer)entry.getValue() - 3; nx += 3) {
            w.getUp(4).setType(Material.CHISELED_DEEPSLATE);
            w.setType(Material.CHISELED_DEEPSLATE);
            (new StairBuilder(Material.DEEPSLATE_TILE_STAIRS)).setFacing(w.getDirection()).apply(w.getUp(3)).setHalf(Half.TOP).apply(w.getUp());
            (new OrientableBuilder(Material.POLISHED_BASALT)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).apply(w.getUp(2)).apply(w.getUp(2).getFront());
            w.getUp().getFront().setType(Material.CHISELED_DEEPSLATE);
            w.getUp(3).getFront().setType(Material.CHISELED_DEEPSLATE);
            w.getFront().setType(Material.DEEPSLATE_TILES);
            w.getUp(4).getFront().setType(Material.DEEPSLATE_TILES);
            var10 = BlockUtils.getAdjacentFaces(w.getDirection());
            relX = var10.length;

            for(relZ = 0; relZ < relX; ++relZ) {
               adj = var10[relZ];
               core = w.getRelative(adj);
               core.getUp(2).setType(Material.POLISHED_BASALT);
               (new StairBuilder(Material.DEEPSLATE_TILE_STAIRS)).setFacing(adj.getOppositeFace()).apply(core).setHalf(Half.TOP).apply(core.getUp(4));
               (new StairBuilder(Material.POLISHED_DEEPSLATE_STAIRS)).setFacing(adj.getOppositeFace()).apply(core.getUp(3)).setHalf(Half.TOP).apply(core.getUp());
               w.getFront().getRelative(adj).Pillar(2, new Material[]{Material.POLISHED_BASALT});
               w.getFront().getRelative(adj).getUp(2).setType(Material.DEEPSLATE_TILES);
               w.getFront().getRelative(adj).getUp(3).Pillar(2, new Material[]{Material.POLISHED_BASALT});
            }

            w = w.getLeft(3);
         }
      }

      int[][] var24 = room.getAllCorners(2);
      z = var24.length;

      int v;
      SimpleBlock pasteCent;
      for(depth = 0; depth < z; ++depth) {
         int[] corner = var24[depth];
         SimpleBlock core = new SimpleBlock(data, corner[0], room.getY(), corner[1]);

         for(relX = -2; relX <= 2; ++relX) {
            for(relZ = -2; relZ <= 2; ++relZ) {
               pasteCent = core.getRelative(relX, 1, relZ);
               pasteCent.RPillar(4, new Random(), Material.DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICK_SLAB);
            }
         }

         BlockFace[] var34 = BlockUtils.directBlockFaces;
         relZ = var34.length;

         for(int var38 = 0; var38 < relZ; ++var38) {
            BlockFace side = var34[var38];
            Wall w = (new Wall(core, side)).getFront(3).getUp();
            if (w.getDown().isSolid() && !w.isSolid()) {
               BlockFace[] var16 = BlockUtils.getAdjacentFaces(side);
               int var17 = var16.length;

               for(v = 0; v < var17; ++v) {
                  BlockFace adj = var16[v];
                  if (!w.getRelative(adj).isSolid()) {
                     (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w).apply(w.getRelative(adj, 1)).apply(w.getRelative(adj, 2)).apply(w.getRelative(adj, 3)).setHalf(Half.TOP).apply(w.getUp(3)).apply(w.getRelative(adj, 1).getUp(3)).apply(w.getRelative(adj, 2).getUp(3)).apply(w.getRelative(adj, 3).getUp(3));
                     BlockUtils.correctStairData(w.getRelative(adj, 3));
                     BlockUtils.correctStairData(w.getRelative(adj, 3).getUp(3));
                     w.getRelative(adj, 2).getRear().Pillar(4, new Material[]{Material.POLISHED_BASALT});
                  }
               }
            }
         }
      }

      x = BlockUtils.getAxisFromBlockFace(headFacing) == Axis.X ? room.getWidthX() : room.getWidthZ();
      SimpleBlock base = room.getCenterSimpleBlock(data).getRelative(headFacing, x / 3);

      int i;
      for(depth = 0; depth < 10; ++depth) {
         boolean breakOut = false;
         var10 = BlockUtils.getAdjacentFaces(headFacing);
         relX = var10.length;

         for(relZ = 0; relZ < relX; ++relZ) {
            adj = var10[relZ];
            core = (new Wall(base.getUp(), adj)).getFront(2).getRelative(headFacing, depth);
            if (core.isSolid()) {
               breakOut = true;
               break;
            }

            if (depth == 0) {
               core.Pillar(4, new Material[]{Material.POLISHED_BASALT});

               for(i = 1; i < 4; ++i) {
                  core.getFront(i).Pillar(6, AncientCityUtils.deepslateBricks);
               }
            } else {
               (new StairwayBuilder(new Material[]{Material.DEEPSLATE_BRICK_STAIRS})).setDownTypes(AncientCityUtils.deepslateBricks).setStairwayDirection(BlockFace.UP).setStopAtY(room.getY() + 6).setUpwardsCarveUntilNotSolid(false).build(core);
            }
         }

         if (breakOut) {
            break;
         }
      }

      SimpleBlock pillarCent = room.getCenterSimpleBlock(data).getUp();

      int nz;
      for(nx = -1; nx <= 1; ++nx) {
         for(nz = -1; nz <= 1; ++nz) {
            pillarCent.getRelative(nx, 0, nz).setType(AncientCityUtils.deepslateTiles);
            pillarCent.getRelative(nx, 3, nz).setType(AncientCityUtils.deepslateTiles);
         }
      }

      pillarCent.getUp().Pillar(2, V_1_19.SCULK_SENSOR);
      BlockFace[] var33 = BlockUtils.directBlockFaces;
      nz = var33.length;

      BlockFace face;
      for(relX = 0; relX < nz; ++relX) {
         face = var33[relX];
         (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(face.getOppositeFace()).apply(pillarCent.getRelative(face, 2)).setHalf(Half.TOP).apply(pillarCent.getRelative(face, 2).getUp(3));
         pillarCent.getRelative(face).getUp().Pillar(2, Material.REDSTONE_LAMP);
         pillarCent.getRelative(face).getRelative(BlockUtils.getLeft(face)).getUp().Pillar(2, Material.DEEPSLATE_BRICK_WALL);
         pillarCent.getRelative(face).getRelative(BlockUtils.getLeft(face)).getUp().CorrectMultipleFacing(2);
      }

      BlockFace[][] var35 = BlockUtils.cornerBlockFaces;
      nz = var35.length;

      for(relX = 0; relX < nz; ++relX) {
         BlockFace[] faces = var35[relX];
         pasteCent = pillarCent.getRelative(faces[0], 3).getRelative(faces[1], 3);
         pasteCent.Pillar(4, Material.POLISHED_BASALT);
         BlockFace[] var42 = faces;
         i = faces.length;

         for(int var45 = 0; var45 < i; ++var45) {
            BlockFace face = var42[var45];
            v = face.getModX() != 0 ? room.getWidthX() / 2 - 6 : room.getWidthZ() / 2 - 6;
            SimpleBlock relrel = pasteCent;

            for(int i = 1; i < v; ++i) {
               relrel = relrel.getRelative(face);
               if (relrel.isSolid()) {
                  break;
               }

               relrel.getUp().Pillar(2, Material.GLASS_PANE);
               relrel.CorrectMultipleFacing(3);
               relrel.setType(AncientCityUtils.deepslateBricks);
               relrel.getUp(3).setType(AncientCityUtils.deepslateBricks);
            }

            relrel.Pillar(4, Material.POLISHED_BASALT);
         }
      }

      try {
         TerraSchematic schema = TerraSchematic.load("ancient-city/ancient-city-pistondoor", base.getUp().getRelative(headFacing.getOppositeFace(), 2));
         schema.parser = new AncientCitySchematicParser();
         schema.setFace(headFacing);
         schema.apply();
      } catch (FileNotFoundException var22) {
         TerraformGeneratorPlugin.logger.stackTrace(var22);
      }

      var33 = BlockUtils.directBlockFaces;
      nz = var33.length;

      for(relX = 0; relX < nz; ++relX) {
         face = var33[relX];
         pasteCent = room.getCenterSimpleBlock(data).getUp().getRelative(face, 2).getRelative(BlockUtils.getLeft(face), 2);

         try {
            String schematic;
            if (BlockUtils.getAxisFromBlockFace(face) == BlockUtils.getAxisFromBlockFace(headFacing)) {
               schematic = northSouthResearchSchematics[random.nextInt(northSouthResearchSchematics.length)];
            } else {
               schematic = eastWestResearchSchematics[random.nextInt(eastWestResearchSchematics.length)];
            }

            TerraSchematic schema = TerraSchematic.load(schematic, pasteCent);
            schema.parser = new AncientCitySchematicParser();
            schema.setFace(face);
            schema.apply();
         } catch (FileNotFoundException var21) {
            TerraformGeneratorPlugin.logger.stackTrace(var21);
         }
      }

   }
}
