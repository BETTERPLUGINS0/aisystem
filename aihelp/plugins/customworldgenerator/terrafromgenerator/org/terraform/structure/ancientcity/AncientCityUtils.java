package org.terraform.structure.ancientcity;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.data.SimpleBlock;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.MultipleFacingBuilder;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.version.V_1_19;

public class AncientCityUtils {
   public static final Material[] deepslateBricks;
   public static final Material[] deepslateTiles;
   @Nullable
   private static Tag<Material> t;

   public static void placeSupportPillar(@NotNull SimpleBlock w) {
      Random dud = new Random();
      w.downUntilSolid(dud, Material.DEEPSLATE_BRICKS);
      BlockFace[] var2 = BlockUtils.directBlockFaces;
      int var3 = var2.length;

      int var4;
      BlockFace face;
      for(var4 = 0; var4 < var3; ++var4) {
         face = var2[var4];
         w.getRelative(face).downUntilSolid(dud, Material.DEEPSLATE_BRICKS);
      }

      var2 = BlockUtils.xzDiagonalPlaneBlockFaces;
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         face = var2[var4];
         int height = w.getRelative(face).downUntilSolid(dud, Material.COBBLED_DEEPSLATE_WALL);
         w.getRelative(face).getDown(height - 1).CorrectMultipleFacing(height);
      }

   }

   public static void spreadSculk(@NotNull FastNoise circleNoise, @NotNull Random random, float radius, @NotNull SimpleBlock center) {
      if (t == null) {
         try {
            t = (Tag)Tag.class.getDeclaredField("SCULK_REPLACEABLE_WORLD_GEN").get((Object)null);
         } catch (Exception var20) {
            TerraformGeneratorPlugin.logger.stackTrace(var20);
         }
      }

      boolean placedShrieker = false;
      boolean placedSensor = false;
      boolean placedCatalyst = false;

      for(float nx = -radius; nx <= radius; ++nx) {
         for(float nz = -radius; nz <= radius; ++nz) {
            for(float ny = -radius; ny <= radius; ++ny) {
               SimpleBlock rel = center.getRelative(Math.round(nx), Math.round(ny), Math.round(nz));
               if (rel.isSolid() && rel.getType() != V_1_19.SCULK_VEIN) {
                  double equationResult = Math.pow((double)nx, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)nz, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)ny, 2.0D) / Math.pow((double)radius, 2.0D);
                  float noiseVal = circleNoise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ());
                  if (equationResult <= 1.0D + 0.7D * (double)noiseVal && (BlockUtils.isExposedToNonSolid(rel) || !rel.getDown().isSolid() || !rel.getUp().isSolid())) {
                     if (t.isTagged(rel.getType()) && equationResult <= 0.7D * (1.0D + 0.7D * (double)noiseVal)) {
                        assert V_1_19.SCULK != null;

                        rel.setType(V_1_19.SCULK);
                        if (!rel.getUp().isSolid()) {
                           if (!placedCatalyst && GenUtils.chance(random, 1, 40)) {
                              placedCatalyst = true;
                              rel.getUp().setType(V_1_19.SCULK_CATALYST);
                           } else if (!placedSensor && GenUtils.chance(random, 1, 20)) {
                              placedSensor = true;
                              rel.getUp().setType(V_1_19.SCULK_SENSOR);
                           } else if (!placedShrieker && GenUtils.chance(random, 1, 90)) {
                              placedShrieker = true;
                              rel.getUp().setBlockData(V_1_19.getActiveSculkShrieker());
                           }
                        }
                     } else if (rel.getType() != V_1_19.SCULK_SHRIEKER && rel.getType() != V_1_19.SCULK_SENSOR && !Tag.STAIRS.isTagged(rel.getType()) && !Tag.SLABS.isTagged(rel.getType())) {
                        BlockFace[] var14 = BlockUtils.sixBlockFaces;
                        int var15 = var14.length;

                        for(int var16 = 0; var16 < var15; ++var16) {
                           BlockFace face = var14[var16];
                           SimpleBlock adj = rel.getRelative(face);
                           if (adj.isAir()) {
                              (new MultipleFacingBuilder(V_1_19.SCULK_VEIN)).setFace(face.getOppositeFace(), true).apply(adj);
                           } else if (adj.getType() == V_1_19.SCULK_VEIN) {
                              MultipleFacing mf = (MultipleFacing)adj.getBlockData();
                              mf.setFace(face.getOppositeFace(), true);
                              adj.setBlockData(mf);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   static {
      deepslateBricks = new Material[]{Material.DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_BRICKS};
      deepslateTiles = new Material[]{Material.DEEPSLATE_TILES, Material.CRACKED_DEEPSLATE_TILES};
      t = null;
   }
}
