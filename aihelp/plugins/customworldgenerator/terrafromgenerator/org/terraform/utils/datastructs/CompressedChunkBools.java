package org.terraform.utils.datastructs;

import org.terraform.main.TerraformGeneratorPlugin;

public class CompressedChunkBools {
   short[][] matrix;

   public CompressedChunkBools() {
      this.matrix = new short[TerraformGeneratorPlugin.injector.getMaxY() - TerraformGeneratorPlugin.injector.getMinY() + 1][16];
   }

   public void set(int x, int y, int z) {
      int idY = y - TerraformGeneratorPlugin.injector.getMinY();
      if (idY >= 0 && idY < this.matrix.length) {
         this.matrix[idY][x] = (short)(this.matrix[idY][x] | 1 << z);
      }
   }

   public void unSet(int x, int y, int z) {
      int idY = y - TerraformGeneratorPlugin.injector.getMinY();
      if (idY >= 0 && idY < this.matrix.length) {
         this.matrix[idY][x] = (short)(this.matrix[idY][x] & (255 ^ 1 << z));
      }
   }

   public boolean isSet(int x, int y, int z) {
      int idY = y - TerraformGeneratorPlugin.injector.getMinY();
      if (idY >= 0 && idY < this.matrix.length) {
         return (this.matrix[idY][x] & 1 << z) > 0;
      } else {
         return false;
      }
   }
}
