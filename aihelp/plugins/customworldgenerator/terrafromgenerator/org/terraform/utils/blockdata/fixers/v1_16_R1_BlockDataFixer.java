package org.terraform.utils.blockdata.fixers;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Wall;
import org.bukkit.block.data.type.Wall.Height;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.BlockDataFixerAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.utils.BlockUtils;

public class v1_16_R1_BlockDataFixer extends BlockDataFixerAbstract {
   public static void correctWallData(@NotNull SimpleBlock target) {
      BlockData var2 = target.getBlockData();
      if (var2 instanceof Wall) {
         Wall data = (Wall)var2;
         BlockFace[] var7 = BlockUtils.directBlockFaces;
         int var3 = var7.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BlockFace face = var7[var4];
            Material relType = target.getRelative(face).getType();
            if (relType.isSolid() && !Tag.BANNERS.isTagged(relType) && !Tag.PRESSURE_PLATES.isTagged(relType) && !Tag.TRAPDOORS.isTagged(relType) && !Tag.SLABS.isTagged(relType)) {
               data.setHeight(face, Height.LOW);
               if (target.getRelative(BlockFace.UP).isSolid()) {
                  data.setHeight(face, Height.TALL);
               }

               if (!BlockUtils.glassPanes.contains(target.getType()) || !Tag.FENCE_GATES.isTagged(relType) && !Tag.FENCES.isTagged(relType)) {
                  if ((Tag.FENCES.isTagged(target.getType()) || Tag.FENCE_GATES.isTagged(target.getType())) && BlockUtils.glassPanes.contains(relType)) {
                     data.setHeight(face, Height.NONE);
                  }
               } else {
                  data.setHeight(face, Height.NONE);
               }
            } else {
               data.setHeight(face, Height.NONE);
            }
         }

         target.setBlockData(data);
      }
   }

   public static void correctSurroundingWallData(@NotNull SimpleBlock target) {
      if (target.getBlockData() instanceof Wall) {
         correctWallData(target);
         BlockFace[] var1 = BlockUtils.directBlockFaces;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            BlockFace face = var1[var3];
            if (Tag.WALLS.isTagged(target.getRelative(face).getType())) {
               correctWallData(target.getRelative(face));
            }
         }

      }
   }

   public String updateSchematic(double schematicVersion, String schematic) {
      return schematic;
   }

   public void correctFacing(Vector v, @Nullable SimpleBlock b, @Nullable BlockData data, BlockFace face) {
      if (data == null && b != null) {
         data = b.getBlockData();
      }

      if (!this.hasFlushed && data instanceof Wall) {
         this.pushChanges(v);
      } else {
         if (data instanceof Wall && b != null) {
            correctSurroundingWallData(b);
         }

      }
   }
}
